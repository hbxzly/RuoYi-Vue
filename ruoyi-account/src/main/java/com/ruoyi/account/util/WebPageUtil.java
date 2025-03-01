package com.ruoyi.account.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

public class WebPageUtil {

    public static String getXpathBySelector(String pageSource, String selector){

        // 使用Jsoup解析HTML
        Document doc = Jsoup.parse(pageSource);

        // 查找特定的div元素 (例如, aria-label="添加好友")
        Elements elements = doc.select(selector);

        // 如果找到元素，生成XPath路径
        if (!elements.isEmpty()) {
            Element targetElement = elements.first();
            String xpath = getXPath(targetElement);
            System.out.println("XPath: " + xpath);
            return xpath.replace("/#root[1]","");
        } else {
            System.out.println("Element not found.");
            return null;
        }

    }

    public static String getXpathBySourceCode(String pageSource, String sourceCode){
        // 使用JSoup解析HTML
        Document doc = Jsoup.parse(pageSource);

        // 查找文档中的所有元素
        Elements elements = doc.getAllElements();

        // 使用自定义过滤器匹配完整的HTML标签
        Element matchedElement = null;
        for (Element element : elements) {
            // 获取元素的外部HTML表示
            String outerHtml = element.outerHtml();
            if (outerHtml.equals(sourceCode)) {
                matchedElement = element;
                break;
            }
        }

        // 如果找到了匹配的元素
        if (matchedElement != null) {
            // 构造XPath路径
            String xpath = getXPath(matchedElement);
            System.out.println("XPath: " + xpath);
            return xpath.replace("/#root[1]","");
        } else {
            System.out.println("未找到匹配的元素");
            return null;
        }
    }

    // 生成元素的XPath路径
    private static String getXPath(Element element) {
        StringBuilder xpath = new StringBuilder();
        while (element != null) {
            int index = getElementIndex(element);
            String tagName = element.tagName();
            xpath.insert(0, "/" + tagName + "[" + index + "]");
            element = element.parent();
        }
        return xpath.toString();
    }

    // 获取元素在兄弟元素中的索引位置
    private static int getElementIndex(Element element) {
        // 如果元素没有父元素，说明它是根元素，索引为1
        if (element.parent() == null) {
            return 1;
        }
        Elements siblings = element.parent().children();
        int index = 1;
        for (Element sibling : siblings) {
            if (sibling.tagName().equals(element.tagName())) {
                if (sibling.equals(element)) {
                    return index;
                }
                index++;
            }
        }
        return index;
    }

}
