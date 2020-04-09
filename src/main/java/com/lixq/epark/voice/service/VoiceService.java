package com.lixq.epark.voice.service;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.lixq.epark.voice.config.VoiceConfiguration;
import com.winnerlook.model.PrivacyBindBodyAx;
import com.winnerlook.model.PrivacyUnbindBody;
import com.winnerlook.model.VoiceResponseResult;
import com.winnerlook.util.Base64;
import com.winnerlook.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * 云信语音服务类
 *
 * @author lixiangqian
 * @since 2020-04-09 14:08
 */
@Service
@Slf4j
public class VoiceService {

    private VoiceConfiguration voiceConfig;

    public VoiceService(VoiceConfiguration voiceConfig) {
        this.voiceConfig = voiceConfig;
    }

    /**
     * 绑定小号
     *
     * @param middleNumber 小号
     * @param bindNumber   要绑定的真实手机号
     * @return
     */
    public VoiceResponseResult bindWithAx(String middleNumber, String bindNumber) {
        PrivacyBindBodyAx bindBodyAx = new PrivacyBindBodyAx();
        /** 设定绑定的隐私小号*/
        bindBodyAx.setMiddleNumber(middleNumber);
        /** 设定绑定模式（小号的模式） 1: AXN; 2: AX, 默认为AXN模式*/
        bindBodyAx.setMode(2);
        /** 设定与该隐私小号绑定的号码A*/
        bindBodyAx.setBindNumberA(bindNumber);
        /** 设置号码A归属人姓名*/
        bindBodyAx.setCalleeName("xxx");
        /** 设置号码A归属人身份证号码*/
        bindBodyAx.setCalleeId("xxx");
        /** 设置是否开启通话录音  1:开启，0:关闭*/
        bindBodyAx.setCallRec(0);
        /** 设置是否开启分机号模式: 1:开启，0:关闭*/
        bindBodyAx.setWithExtNumber(0);
        /** 设置绑定关系有效时长 ,为空表示绑定关系永久，单位:秒*/
        bindBodyAx.setMaxBindingTime(3600);
        /** 设置用于接收呼叫结果的服务地址*/
        //bindBodyAx.setCallbackUrl("http://myip../...");
        /** 设置是否透传主叫的号码到A  只针对AX模式的小号有效，0:不透传; 1: 透传*/
        bindBodyAx.setPassthroughCallerToA(0);

        return sendRequest("middleNumberAX", middleNumber, bindNumber, JSON.toJSONString(bindBodyAx));
    }

    /**
     * 解绑小号
     *
     * @param middleNumber 小号
     * @param bindNumber   要解绑的真实手机号
     * @return
     */
    public VoiceResponseResult unBindWithAx(String middleNumber, String bindNumber) {
        PrivacyUnbindBody unbindBody = new PrivacyUnbindBody();
        /** 设置需要解绑的小号*/
        unbindBody.setMiddleNumber(middleNumber);
        /** 设置与该小号绑定的号码A*/
        unbindBody.setBindNumberA(bindNumber);

        return sendRequest("middleNumberUnbind", middleNumber, bindNumber, JSON.toJSONString(unbindBody));
    }

    /**
     * @param option
     * @param middleNumber
     * @param bindNumber
     * @param body
     * @return
     * @throws Exception
     */
    private VoiceResponseResult sendRequest(String option, String middleNumber, String bindNumber, String body) {
        //1、准备参数
        long timestamp = System.currentTimeMillis();
        String authorization = Base64.encodeBase64(voiceConfig.getAppId() + ":" + timestamp);
        String sig = MD5Util.getMD5(voiceConfig.getAppId() + voiceConfig.getToken() + timestamp);
        String url = MessageFormat.format(voiceConfig.getUrl(), option, voiceConfig.getAppId(), sig);

        //2、调用接口
        log.info("云信接口调用请求: url={}, body={}", url, body);
        String result = HttpRequest.post(url)
                .header("Authorization", authorization)
                .header("Content-type", "application/json; charset=utf-8")
                .body(body)
                .timeout(20000)
                .execute().body();

        //3、解析结果
        log.info("云信接口调用返回: url={}, body={}, result={}", middleNumber, bindNumber, url, result);
        return JSON.parseObject(result, VoiceResponseResult.class);
    }
}
