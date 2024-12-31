import com.ruoyi.RuoYiApplication;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.domain.FbAccountForSell;
import com.ruoyi.account.service.IFbAccountForSellService;
import com.ruoyi.account.service.IFbAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {


    @Autowired
    IFbAccountService accountService;

    @Autowired
    IFbAccountForSellService accountForSellService;

    @Test
    public void TestOne(){
        List<FbAccount> fbAccounts = accountService.selectFbAccountList(new FbAccount());
        for (FbAccount fbAccount : fbAccounts) {
            if (fbAccount.getKeyId()>1078){
                fbAccount.setRegion("中文");
                accountService.updateFbAccount(fbAccount);
            }
        }
    }


    public static boolean containsChinese(String input) {
        // 正则表达式匹配中文字符（包括常用汉字）
        if (input != null && input.matches(".*[\u4e00-\u9fa5].*")) {
            return true;  // 包含中文字符
        }
        return false;  // 不包含中文字符
    }
}
