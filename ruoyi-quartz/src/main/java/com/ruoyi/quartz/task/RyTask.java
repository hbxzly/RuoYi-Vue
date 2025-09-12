package com.ruoyi.quartz.task;

import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.domain.FbAccountForSellQuery;
import com.ruoyi.account.mapper.FbAccountForSellMapper;
import com.ruoyi.account.service.IFbAccountForSellService;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;

import java.util.List;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask {

    @Autowired
    private IFbAccountForSellService fbAccountForSellService;

    @Autowired
    private FbAccountForSellMapper fbaccountForSellMapper;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void addFriend(String addId, Integer number, String region, String canLogin, String isSell) {
        fbAccountForSellService.addFriendNew(addId, number, region, canLogin, isSell);
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }
}
