package com.ruoyi.browser.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "browser.hubstudio")
public class HubstudioProperties {

    private String baseUrl;

    private Integer timeout;

}
