package com.ruoyi.account.util;

import com.ruoyi.account.domain.Advertise;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class AdvertiseUrl {

    public static String screenshotUrl(Advertise advertise){

        LocalDate today = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayFormatter = dateTimeFormatter.format(today);
        LocalDate yesterday = today.minusDays(1);
        String yesterdayFormatter = dateTimeFormatter.format(yesterday);

        return "https://adsmanager.facebook.com/adsmanager/manage/ads?act="+advertise.getAdAccountId()+"&business_id="+advertise.getAuthorizedBm()+"&nav_entry_point=am_local_scope_selector&date="+yesterdayFormatter+"_"+todayFormatter+"%2Cyesterday&comparison_date&insights_date="+yesterdayFormatter+"_"+todayFormatter+"%2Cyesterday&insights_comparison_date&column_preset=PERFORMANCE_LEGACY&breakdown_regrouping=0&&filter_set=ad.impressions-NUMBER%1EGREATER_THAN%1E\"0\"";

    }

    public static String balanceUrl(Advertise advertise){
        return "https://business.facebook.com/billing_hub/accounts/details/?business_id="+advertise.getAuthorizedBm()+"&asset_id="+advertise.getAdAccountId();
    }

    public static String balance(String pageSource){

        // 开始标记和结束标记
        String startTag = "<div aria-level=\"3\" class=\"x8t9es0 x1uxerd5 x1xlr1w8 xrohxju x4hq6eo xq9mrsl x1yc453h x1h4wwuj xeuugli\" role=\"heading\">";
        String endTag = "</div>";

        // 找到开始标记和结束标记的索引
        int startIndex = pageSource.indexOf(startTag);
        int endIndex = pageSource.indexOf(endTag, startIndex);
        // 截取符合规则的部分
        String extractedText = pageSource.substring(startIndex + startTag.length(), endIndex);

        return extractedText;
    }

}
