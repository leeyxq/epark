package com.lixq.epark.voice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixiangqian
 * @since 2020-04-09 14:12
 */
@Configuration
@ConfigurationProperties(prefix = "winnerlook")
public class VoiceConfiguration {

    private String url;
    private String appId;
    private String token;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
