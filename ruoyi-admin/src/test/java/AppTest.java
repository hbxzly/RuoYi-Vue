import com.ruoyi.RuoYiApplication;
import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.service.IFbAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = RuoYiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {


    @Autowired
    IFbAccountService accountService;

    @Test
    public void TestOne(){
        List<FbAccount> fbAccountList = accountService.selectByFbAccount(new FbAccount());
        for (FbAccount fbAccount : fbAccountList) {
            fbAccount.setNote(keepOnlyLastFriendNumber(fbAccount.getNote()));
            accountService.updateFbAccount(fbAccount);
        }
    }

    public static String keepOnlyLastFriendNumber(String input) {
        if (input == null || input.isEmpty()) {
            return input; // 如果输入为空或null，直接返回
        }

        // 使用正则表达式匹配所有 "数字好友" 的格式
        String regex = "\\d+好友";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(regex).matcher(input);

        String lastMatch = null; // 用于保存最后一个匹配的“数字好友”
        int lastIndex = -1; // 用于记录最后一个匹配的位置

        while (matcher.find()) {
            lastMatch = matcher.group(); // 保存当前匹配的内容
            lastIndex = matcher.start(); // 保存当前匹配的起始位置
        }

        // 如果没有匹配到或只有一个匹配，直接返回原字符串
        if (lastMatch == null || lastIndex == -1 || input.indexOf(lastMatch) == lastIndex) {
            return input;
        }

        // 移除所有匹配的“数字好友”，保留最后一个
        return input.substring(0, lastIndex) + lastMatch;
    }
}
