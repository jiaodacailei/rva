package com.ruoyi.rva.dingtalk.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.ActionCard;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.Msg;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.ruoyi.rva.dingtalk.config.AppConfig;
import com.ruoyi.rva.fe.domain.RvaUniappMsg;
import com.ruoyi.rva.fe.domain.RvaUniappPushMsg;
import com.ruoyi.rva.fe.service.IRvaUniappPushMsgService;
import com.ruoyi.rva.framework.util.RvaJsonUtils;
import com.ruoyi.rva.framework.util.RvaUtils;
import com.ruoyi.system.service.ISysConfigService;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.ruoyi.rva.dingtalk.config.UrlConstant.URL_SEND_MSG;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RvaDingtalkMsgService {

    private final AppConfig appConfig;

    private final RvaDingtalkTokenService tokenService;

    private final ISysConfigService configService;

    public void sendMsg(Msg msg, List userIds) {
        String accessToken = tokenService.getAccessToken();
        DefaultDingTalkClient client = new DefaultDingTalkClient(URL_SEND_MSG);
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(RvaUtils.getLong(appConfig.getAgentId()));
        request.setUseridList(RvaUtils.join(userIds));
        request.setMsg(msg);
        OapiMessageCorpconversationAsyncsendV2Response response;

        try {
            response = client.execute(request, accessToken);
            Map map = RvaJsonUtils.readAsType(response.getBody(), Map.class);
            Integer errcode = (Integer) map.get("errcode");
            if (errcode != 0) {
                throw new RuntimeException(map.get("errmsg") + ":" + errcode);
            }
            log.info("send action card response: " + response.getBody());
        } catch (ApiException e) {
            throw new RuntimeException(e.getErrMsg() + ":" + e.getErrCode(), e);
        }
    }

    /**
     * https://open.dingtalk.com/document/orgapp-server/message-types-and-data-format
     * @param title
     * @param content
     * @param url PC端url
     * @param appUrl 手机端url
     * @param userIds
     */
    public void sendActionCardMsg(String title, String content, String url, String appUrl, List userIds) {
        ActionCard actionCard = new ActionCard();
        List<BtnJsonList> list = new ArrayList<>();

        BtnJsonList btn1 = new BtnJsonList();
        btn1.setTitle("详情");
        btn1.setActionUrl(url);
        list.add(btn1);

        actionCard.setBtnJsonList(list);
        actionCard.setBtnOrientation("1");
        actionCard.setTitle(title);
        actionCard.setMarkdown(content);

        Msg msg = new Msg();
        msg.setMsgtype("action_card");
        msg.setActionCard(actionCard);

        sendMsg(msg, userIds);
    }

    private final IRvaUniappPushMsgService rvaUniappPushMsgService;

    /**
     *
     */
    public void handleActionCardMsg() {
        String urlPrifx = configService.selectConfigByKey("rva.url.prefix");
        List<RvaUniappPushMsg> rvaUniappPushMsgList = rvaUniappPushMsgService.selectPendingListByPushChannelAndMinutes(RvaUniappMsg.PUSH_CHANNEL_DING_PUSH,60);
        for (RvaUniappPushMsg rvaUniappPushMsg : rvaUniappPushMsgList) {
            try {
                sendActionCardMsg(rvaUniappPushMsg.getTitle(), rvaUniappPushMsg.getContent(), urlPrifx + rvaUniappPushMsg.getMsgUrl(), urlPrifx + rvaUniappPushMsg.getMsgAppUrl(), Arrays.asList(rvaUniappPushMsg.getCid()));
                rvaUniappPushMsg.setPushStatus(RvaUniappPushMsg.PUSH_COMPLETE);
                rvaUniappPushMsgService.updateRvaUniappPushMsg(rvaUniappPushMsg);
            } catch (Exception e) {
                log.warn("sendActionCardMsg err:" + e.getMessage(), e);
            }
        }
    }
}
