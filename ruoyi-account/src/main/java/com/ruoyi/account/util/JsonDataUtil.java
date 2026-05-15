package com.ruoyi.account.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonDataUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String getValueByNodeName(String json, String... nodes) {
        try {
            JsonNode node = MAPPER.readTree(json);
            for (String n : nodes) {
                if (node == null) return null;
                node = node.get(n);
            }
            return node == null ? null : node.asText();
        } catch (Exception e) {
            throw new RuntimeException("JSON解析失败", e);
        }
    }
    /**
     * 返回字符串集合：个人号ID-广告账户名称-广告账户ID
     * @param id
     * @param html
     * @return
     */
    public static List<String> getAdAccountBmInfo(String id, String html){

        List<String> info = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        //广告账户的div
        Elements adElements = doc.select("li.UNIFIED_LOCAL_SCOPE_SELECTOR_ITEM_LIST-item");
        for (Element element:adElements){
            String accountId = extractNumber(element.select("a").attr("href"), "act=(\\d+)").trim();
            String bmId = extractNumber(element.select("a").attr("href"), "business_id=(\\d+)");
            String accountName = element.select(".ellipsis").first().text().trim();
            info.add(id+"-"+accountName+"-"+accountId);
        }
        return info;
    }

    /**
     * 返回BM信息（BMID，BM名称）
     * @param html
     * @return
     */
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

    /**
     * 获取当前 WebDriver 的所有 cookies 并拼接成字符串
     * 格式：name=value;name=value;...
     */
    public static String getCookiesAsString(WebDriver driver) {
        Set<Cookie> cookies = driver.manage().getCookies();
        StringJoiner joiner = new StringJoiner(";");
        for (Cookie cookie : cookies) {
            joiner.add(cookie.getName() + "=" + cookie.getValue());
        }
        return joiner.toString();
    }

    /**
     * 把数据库里取出的 cookie 字符串重新导入到浏览器
     */
    public static void addCookiesToDriver(WebDriver driver, String cookieStr) {
        if (cookieStr == null || cookieStr.isEmpty()) {
            return;
        }
        String[] cookieArray = cookieStr.split(";");
        for (String cookieItem : cookieArray) {
            String[] parts = cookieItem.split("=", 2); // 只分割成 name 和 value
            if (parts.length == 2) {
                String name = parts[0].trim();
                String value = parts[1].trim();
                driver.manage().addCookie(new Cookie(name, value));
            }
        }
    }


}
