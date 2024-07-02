package com.ruoyi.account.util;

import com.ruoyi.account.domain.Advertise;
import com.ruoyi.account.domain.FbAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountUtil {


    public static List<FbAccount> getFbAccount(String accountStr){
        List<FbAccount> fbAccountList = new ArrayList<>();
        // 使用正则表达式提取所需的数据
        Pattern pattern = Pattern.compile("\"account\":\"(.*?)\"}");
        Matcher matcher = pattern.matcher(accountStr);
        if (matcher.find()) {
            String extractedData = matcher.group(1);
            String[] split = extractedData.split("\\\\n");
            for(String s:split){
                FbAccount fbAccount = new FbAccount();
                String[] account = s.split("-");
                fbAccount.setEmail(account[0]);
                fbAccount.setPassword(account[1]);
                fbAccount.setEmailPassword(account[2]);
                fbAccount.setBirthday(account[3]);
                fbAccount.setId(account[4]);
                fbAccount.setSecretKey(account[5]);
                if (account[6].equals("空")){
                    fbAccount.setUa(RandomUitl.generateRandomPCUserAgent());
                }else {
                    fbAccount.setUa(account[6]);
                }
                fbAccount.setDataSource(account[7]);
                if (account[8].equals("空")){
                    fbAccount.setBrowserProfile(RandomUitl.generateRandomStringKey());
                }else {
                    fbAccount.setBrowserProfile(account[8]);
                }
                fbAccount.setName(account[9]);
                System.out.println(fbAccount);
                fbAccountList.add(fbAccount);
            }
        } else {
            System.out.println("未找到匹配的数据");
        }
        return fbAccountList;
    }

    public static List<Advertise> getAdvertise(String advertiseStr){
        List<Advertise> advertiseList = new ArrayList<>();
        // 使用正则表达式提取所需的数据
        Pattern pattern = Pattern.compile("\"advertise\":\"(.*?)\"}");
        Matcher matcher = pattern.matcher(advertiseStr);
        if (matcher.find()) {
            String extractedData = matcher.group(1);
            String[] split = extractedData.split("\\\\n");
            for(String s:split){
                Advertise advertise = new Advertise();
                String[] accountArray = s.split("\\+");
                advertise.setAdAccountName(accountArray[0]);
                advertise.setAdAccountId(accountArray[1]);
                advertise.setPageName(accountArray[2]);
                advertise.setPageId(accountArray[3]);
                advertise.setOperation(accountArray[4]);
                advertise.setClient(accountArray[5]);
                advertise.setAdvertisingContent(accountArray[6]);
                advertise.setPlacementArea(accountArray[7]);
                advertise.setAdType(accountArray[8]);
                advertise.setOperator(accountArray[9]);
                advertiseList.add(advertise);
            }
        } else {
            System.out.println("未找到匹配的数据");
        }
        return advertiseList;
    }
}
