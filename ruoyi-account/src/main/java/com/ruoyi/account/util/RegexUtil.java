package com.ruoyi.account.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * 获取添加邮箱验证码
     */
    public static String addEmailToFacebookCode(String message){
        // 正则表达式匹配6位数字
        String regex = "\\d{6}";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        // 创建匹配器
        Matcher matcher = pattern.matcher(message);

        // 查找并输出匹配的验证码
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "未获取到验证码";
        }
    }
}
