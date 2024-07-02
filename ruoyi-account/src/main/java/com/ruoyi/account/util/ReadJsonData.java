package com.ruoyi.account.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadJsonData {


    public static String getInfoByNodeName(String jsonData,String nodeName) throws JsonProcessingException {
        JsonMapper jsonMapper = new JsonMapper();
        JsonNode jsonNode = jsonMapper.readTree(jsonData);
        JsonNode addressJsonNode = jsonNode.get("address");
        return addressJsonNode.get(nodeName).asText();
    }

    public static List<String> getAdAccountBmInfo(String id, String html, Map<String,String> bmMap){

        List<String> info = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        //广告账户的div
        Elements adElements = doc.select("li.UNIFIED_LOCAL_SCOPE_SELECTOR_ITEM_LIST-item");
        for (Element element:adElements){
            String accountId = extractNumber(element.select("a").attr("href"), "act=(\\d+)").trim();
            String bmId = extractNumber(element.select("a").attr("href"), "business_id=(\\d+)");
            String accountName = element.select(".ellipsis").first().text().trim();
            info.add(id+"-"+accountName+"-"+accountId+"-"+bmMap.get(bmId)+"-"+bmId);
        }
        return info;
    }

    public static Map<String,String> getBmInfo(String html){
        Map<String,String> bmMap = new HashMap<>();
        Document bmDoc = Jsoup.parse(html);
        Elements bmElements = bmDoc.select("a.x6s0dn4.x1ypdohk.x78zum5.x1a2a7pz.x1t7mnj6.x1ba4aug.xhk9q7s.x1otrzb0.x1i1ezom.x1o6z2jb.x1lku1pv");
        for (Element element:bmElements){
            String bmName = "";
            Element ellipsisElement = element.select(".ellipsis").first();
            if (ellipsisElement != null && ellipsisElement.text().trim().length() > 0) {
                bmName = ellipsisElement.text().trim();
            }else {
                bmName = element.select("div.x8t9es0.x1fvot60.xxio538.x4hq6eo.xuxw1ft.x6ikm8r.x10wlt62.xlyipyv.x1h4wwuj.x1fcty0u.xeuugli").text();
            }
            bmMap.put(extractNumber(element.select("a").attr("href"),"business_id=(\\d+)"),bmName);
        }
        return bmMap;
    }

    private static String extractNumber(String input, String pattern) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }


}
