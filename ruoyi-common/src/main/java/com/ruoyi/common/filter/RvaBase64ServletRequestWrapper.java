package com.ruoyi.common.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sign.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * XSS过滤处理
 *
 * @author ruoyi
 */
@Slf4j
public class RvaBase64ServletRequestWrapper extends HttpServletRequestWrapper {


    /**
     * @param request
     */
    public RvaBase64ServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {

        return super.getParameterValues(name);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 非json类型，直接返回
        if (!isJsonRequest()) {
            return super.getInputStream();
        }
        // 为空，直接返回
        String json = IOUtils.toString(super.getInputStream(), "utf-8");


        // base64 decode
        JSONObject jsonObject = JSON.parseObject(json);


        String base64 = jsonObject.getString("rvaBase64Data");
        log.info("RvaBase64ServletRequestWrapper base64 :  {}", base64);

        byte[] jsonBytes = json.getBytes("utf-8");
        if (jsonObject.containsKey("rvaBase64Data")) {
            jsonBytes = Base64.decode(base64);
        }
        log.info("RvaBase64ServletRequestWrapper base64 decode-json :  {}", new String(jsonBytes));
        final ByteArrayInputStream bis = new ByteArrayInputStream(jsonBytes);
        byte[] finalJsonBytes = jsonBytes;
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public int available() throws IOException {
                return finalJsonBytes.length;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }

    /**
     * 是否是Json请求
     *
     * @param request
     */
    public boolean isJsonRequest() {
        String header = super.getHeader(HttpHeaders.CONTENT_TYPE);
        return StringUtils.startsWithIgnoreCase(header, MediaType.APPLICATION_JSON_VALUE);
    }
}
