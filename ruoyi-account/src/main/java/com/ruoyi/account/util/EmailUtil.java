package com.ruoyi.account.util;




import com.ruoyi.account.domain.FbAccount;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

    /**
     * 测试邮箱
     * @param email
     * @param password
     * @return
     */
    public static String checkEmail(String email,String password){

        String text = "NO";

        // 设置邮件属性
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps");
        properties.setProperty("mail.imaps.host", "imap-mail.outlook.com");
        properties.setProperty("mail.imaps.port", "993");


        try {
            // 创建会话
            Session session = Session.getDefaultInstance(properties, null);

            // 连接到邮件服务器
            Store store = session.getStore("imaps");
            store.connect("imap-mail.outlook.com", email, password);

            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // 获取邮件消息
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            int length = messages.length;

            // 提取邮件内容
            Object content = messages[length-1].getContent();
            if (content instanceof MimeMultipart) {
                MimeMultipart multipart = (MimeMultipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(i);
                    String contentType = part.getContentType();
                    if (contentType.startsWith("text/plain")) {
                        text = "YES";
                        System.out.println("Content (Text): " + text);
                    }
                }
            }
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            if (!e.toString().contains("Login to your account via a web browser")){
                text = "NO"+"-"+"可验证";
            }
        }
        return text;
    }

    /**
     * 获取邮件
     * @param fbAccount
     * @return
     */
    public static String getMessage(FbAccount fbAccount){

        String text = "";
        // 设置邮件属性
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps");
        properties.setProperty("mail.imaps.host", "imap-mail.outlook.com");
        properties.setProperty("mail.imaps.port", "993");

        try {
            // 创建会话
            Session session = Session.getDefaultInstance(properties, null);

            // 连接到邮件服务器
            Store store = session.getStore("imaps");
            store.connect("imap-mail.outlook.com", fbAccount.getEmail(), fbAccount.getEmailPassword());

            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // 获取邮件消息
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            int length = messages.length;

            // 提取邮件内容
            Object content = messages[length-1].getContent();
            if (content instanceof MimeMultipart) {
                MimeMultipart multipart = (MimeMultipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(i);
                    String contentType = part.getContentType();
                    if (contentType.startsWith("text/plain")) {
                        text = (String) part.getContent();
                    }
                }
            }
            // 关闭连接
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "获取异常";
        }
        return text;
    }

    public static String getVerificationCode(String message) {

        String code = "";

        // 正则表达式模式，匹配6到8位的数字
        String regex = "\\b\\d{8}\\b|\\b\\d{6}\\b";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        // 创建匹配器
        Matcher matcher = pattern.matcher(message);

        // 查找匹配的数字
        while (matcher.find()) {
            // 获取匹配的数字
            code = matcher.group();
        }
        return code;
    }


}



