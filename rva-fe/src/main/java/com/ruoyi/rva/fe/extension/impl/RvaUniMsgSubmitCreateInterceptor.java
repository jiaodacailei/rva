package com.ruoyi.rva.fe.extension.impl;

import com.ruoyi.rva.fe.service.IRvaUniappMsgService;
import com.ruoyi.rva.framework.domain.RvaMap;
import com.ruoyi.rva.framework.domain.RvaObject;
import com.ruoyi.rva.framework.domain.RvaView;
import com.ruoyi.rva.framework.extension.RvaFormSubmitInterceptor;
import com.ruoyi.rva.framework.extension.impl.RvaFormSubmitBaseInterceptor;
import com.ruoyi.rva.framework.mapper.RvaDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 手动发消息
 */
@Component(RvaFormSubmitInterceptor.BEAN_FREFIX + "c0_rva_uniapp_msg")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaUniMsgSubmitCreateInterceptor extends RvaFormSubmitBaseInterceptor {

   @Autowired
   private IRvaUniappMsgService rvaUniappMsgService;

    @Override
    public void preHandle(RvaMap<String, Object> fieldValues, RvaObject object, RvaView view, RvaMap req) {
    }


    @Override
    public void postHandle(RvaMap<String, Object> fieldValues, RvaObject obj, RvaView view, RvaMap req) {
        Long msgId = fieldValues.getLong("id");
        rvaUniappMsgService.sendMsg(msgId);
    }
}
