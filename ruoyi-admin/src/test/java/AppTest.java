import com.ruoyi.RuoYiApplication;
import com.ruoyi.account.domain.*;
import com.ruoyi.account.mapper.FbAccountForSellMapper;
import com.ruoyi.account.service.*;
import com.ruoyi.account.util.Hubstudio;
import com.ruoyi.browser.client.HubstudioClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutput;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {


    @Autowired
    IFbAccountService fbAccountService;

    @Autowired
    IFbAccountForSellService fbAccountForSellService;

    @Autowired
    IEmailService emailService;

    @Autowired
    ICreateInfoService createInfoService;

    @Autowired
    IOperationLogService operationLogService;

    @Autowired
    FbAccountForSellMapper  fbAccountForSellMapper;

    @Autowired
    ISeleniumService seleniumService;

    @Autowired
    IAccountNameService accountNameService;

    @Autowired
    private HubstudioClient hubstudioClient;





//    @Test
    public void TestOne(){

        String filePath = "C:/Users/carrotKK/Desktop/nsdame.txt"; // 本地txt路径
//        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                AccountName accountName = new AccountName();
                String[] strings = line.split("----");
                accountName.setName(strings[0]);
                accountName.setGender(strings[1]);
                accountName.setIsUse("0");
                accountNameService.insertAccountName(accountName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    @Test
    public void TestTwo(){
        Map<String, Object> map = new HashMap<>();
        map.put("asDynamicType","0");
        map.put("containerName","新建环境");
        map.put("proxyTypeName","不使用代理");

    }


//    @Test
    public void testTwo(){
        // 获取所有账号
        FbAccountForSell fbAccountForSell = new FbAccountForSell();
        fbAccountForSell.setCanLogin("1");
        fbAccountForSell.setIsSell("0");
        fbAccountForSell.setRegion("中文");

        List<FbAccountForSell> allAccounts  = fbAccountForSellService.selectFbAccountForSellList(fbAccountForSell);

        // 计算5天前的截止日期
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -5);
        Date cutoffDate = cal.getTime();

        // 过滤掉近5天内有操作记录的账号
        List<FbAccountForSell> filteredAccounts = allAccounts.stream()
                .filter(account -> {
                    int count = operationLogService.countOperationsAfter(account.getId(), cutoffDate);
                    return count == 0;
                })
                .collect(Collectors.toList());

        System.out.println("大小："+filteredAccounts.size());

    }

//    @Test
    public void testThree(){
       FbAccountForSell fbAccountForSell = new FbAccountForSell();
       fbAccountForSell.setNote("自用");
        List<FbAccountForSell> fbAccountForSellList = fbAccountForSellService.selectFbAccountForSellList(fbAccountForSell);
        for (FbAccountForSell fbAccount : fbAccountForSellList) {
            String region = fbAccount.getRegion();
            String newRegion = region.replace("中文", "自用");
            fbAccount.setRegion(newRegion);
            fbAccount.setNote("验证用");
            fbAccountForSellService.updateFbAccountForSell(fbAccount);
        }

    }

//    @Test
    public void testFour(){



    }


    public static boolean containsChinese(String input) {
        // 正则表达式匹配中文字符（包括常用汉字）
        if (input != null && input.matches(".*[\u4e00-\u9fa5].*")) {
            return true;  // 包含中文字符
        }
        return false;  // 不包含中文字符
    }
}
