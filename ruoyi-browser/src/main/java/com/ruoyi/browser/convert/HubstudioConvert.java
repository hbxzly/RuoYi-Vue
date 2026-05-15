package com.ruoyi.browser.convert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.browser.domain.BrowserContainer;
import com.ruoyi.browser.domain.HubEnv;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class HubstudioConvert {

    private final ObjectMapper mapper;

    public HubstudioConvert() {
        this.mapper = new ObjectMapper();
        // 时间格式适配
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<HubEnv> parseList(String json) throws JsonProcessingException {

        JsonNode listNode = mapper.readTree(json)
                .path("data")
                .path("list");

        List<HubEnv> result = new ArrayList<>();

        if (!listNode.isArray()) {
            return result;
        }

        for (JsonNode node : listNode) {

            // 1️⃣ 基础字段自动映射
            HubEnv env = mapper.convertValue(node, HubEnv.class);

            // 2️⃣ accounts 特殊处理
            JsonNode accountsNode = node.path("accounts");
            if (accountsNode.isArray() && accountsNode.size() > 0) {
                JsonNode account = accountsNode.get(0);
                env.setPlatformName(account.path("platformName").asText(null));
                env.setAccountName(account.path("accountName").asText(null));
            }

            result.add(env);
        }

        return result;
    }
}
