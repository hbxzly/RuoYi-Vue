package com.ruoyi.account.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.account.domain.FbAccountForSell;

import java.util.HashMap;
import java.util.Map;

public class Hubstudio {

    public String createBrowser(){
        System.out.println("come in");
        String url = "http://127.0.0.1:6873/api/v1/env/create";
        Map<String,String> map = new HashMap<>();
        map.put("asDynamicType","0");
        map.put("containerName","新建环境");
        map.put("proxyTypeName","不使用代理");
        ObjectMapper mapper = new ObjectMapper();

        try {
            System.out.println("come in 2");
            String createResult = HttpClientUtil.post(url, mapper.writeValueAsString(map));
            System.out.println(createResult);
            return JsonDataUtil.getValueByNodeName(createResult, "data","containerCode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void bindAccount(FbAccountForSell fbAccountForSell, String containerCode){

        String url = "http://127.0.0.1:6873/api/v1/container/add-account";
        Map<String,String> map = new HashMap<>();
        map.put("accountName",fbAccountForSell.getId());
        map.put("name",fbAccountForSell.getId());
        map.put("accountPassword",fbAccountForSell.getPassword());
        map.put("containerCode",containerCode);
        map.put("otpSecret",fbAccountForSell.getSecretKey());
        map.put("domainName","https://www.facebook.com");
        map.put("siteName","Facebook");
        ObjectMapper mapper = new ObjectMapper();
        try {
            HttpClientUtil.post(url, mapper.writeValueAsString(map));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public String getRandomUaWin(){
        String url = "http://127.0.0.1:6873/api/v1/env/random-ua";
        try {
            String uaResult = HttpClientUtil.post(url, "");
            return JsonDataUtil.getValueByNodeName(uaResult, "data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }











}
