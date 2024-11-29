package com.ruoyi.account.service.impl;

import com.ruoyi.account.domain.FbAccount;
import com.ruoyi.account.mapper.FbAccountMapper;
import com.ruoyi.account.service.IFbAccountService;
import com.ruoyi.account.service.ISeleniumService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IFbAccountServiceImpl implements IFbAccountService {


    @Autowired
    ISeleniumService seleniumService;

    private final FbAccountMapper fbAccountMapper;

    @Autowired
    public IFbAccountServiceImpl(FbAccountMapper fbAccountMapper) {
        this.fbAccountMapper = fbAccountMapper;
    }

    @Override
    public int countByFbAccount(FbAccount fbAccount) {
        return fbAccountMapper.countByFbAccount(fbAccount);
    }

    @Override
    public int deleteByFbAccount(FbAccount fbAccount) {
        return fbAccountMapper.deleteByFbAccount(fbAccount);
    }

    @Override
    public int insert(FbAccount fbAccount) {
        return fbAccountMapper.insert(fbAccount);
    }

    @Override
    public List<FbAccount> selectByFbAccount(FbAccount fbAccount) {
        return fbAccountMapper.selectByFbAccount(fbAccount);
    }

    /**
     * 查询账号列表
     *
     * @param fbAccount 账号
     * @return 账号
     */
    @Override
    public List<FbAccount> selectFbAccountList(FbAccount fbAccount) {
        return fbAccountMapper.selectFbAccountList(fbAccount);
    }


    @Override
    public FbAccount selectOneByFbAccountId(String id) {
        return fbAccountMapper.selectOneByFbAccountId(id);
    }

    @Override
    public int updateFbAccount(FbAccount fbAccount) {
        return fbAccountMapper.updateFbAccount(fbAccount);
    }

    @Override
    public void addFbAccount(FbAccount fbAccount) {
        fbAccountMapper.insert(fbAccount);
    }

    @Override
    public void deleteFbAccount(String[] fbaccountIds) {
        for (String id : fbaccountIds) {
            FbAccount fbAccount = selectOneByFbAccountId(id);
            deleteByFbAccount(fbAccount);

                // 指定要重命名的文件的路径
                String oldFilePath = fbAccount.getDataSource()+fbAccount.getBrowserProfile(); // 旧文件路径
                String newFilePath = fbAccount.getDataSource()+fbAccount.getBrowserProfile()+"待删除"; // 新文件路径

                // 创建 File 对象，表示旧文件
                File oldFile = new File(oldFilePath);

                // 创建 File 对象，表示新文件
                File newFile = new File(newFilePath);

                // 尝试重命名文件
                if (oldFile.exists()) {
                    if (oldFile.renameTo(newFile)) {
                        System.out.println("文件已成功重命名。");
                    } else {
                        System.out.println("无法重命名文件。");
                    }
                } else {
                    System.out.println("旧文件不存在。");
                }


        }
    }

    /**
     * 检查账号信息
     *
     * @param webDriver
     * @param fbAccount
     */
    @Override
    public String checkFbAccount(WebDriver webDriver, FbAccount fbAccount) {
        boolean login = seleniumService.login(fbAccount);
        System.out.println("暂停");
        if (!login) {
            return "0";
        }
        webDriver.get("https://www.facebook.com/" + fbAccount.getId());
        String target = "https://static.xx.fbcdn.net/rsrc.php/v3/yz/r/AqoGWewwdNN.png";
        waitingForContent(30, webDriver, target);

        String pageSource = webDriver.getPageSource();

        //账号名字
        String regex = "\"USER_ID\":\"" + fbAccount.getId() + "\".*?\"NAME\":\"(.*?)\"";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pageSource);
        // 尝试查找匹配的 NAME 值
        if (matcher.find()) {
            String name = matcher.group(1);

            // 手动进行 Unicode 解码，将 uXXXX 形式的编码转换为对应的字符
            StringBuilder decodedNameBuilder = new StringBuilder();
            Matcher unicodeMatcher = Pattern.compile("\\\\u([0-9a-fA-F]{4})").matcher(name);
            int lastEnd = 0;
            while (unicodeMatcher.find()) {
                // 将前面的部分追加到结果中
                decodedNameBuilder.append(name, lastEnd, unicodeMatcher.start());
                // 解码当前 uXXXX 部分
                int charCode = Integer.parseInt(unicodeMatcher.group(1), 16);
                decodedNameBuilder.append((char) charCode);
                lastEnd = unicodeMatcher.end();
            }
            decodedNameBuilder.append(name.substring(lastEnd));
            String decodedName = decodedNameBuilder.toString();

            // 获取 NAME 的值
            fbAccount.setName(decodedName);
            fbAccountMapper.updateFbAccount(fbAccount);
        }
        //好有数量
        Document document = Jsoup.parse(pageSource);
        Element element = document.select("h1:containsOwn(" + fbAccount.getName() + ")").first();
        element = getNthParent(element, 5);
        element.select("a[href*='sk=friends']").first();
        if (element != null) {
            // 提取文本并解析数字
            String friendCountText = element.text();
            regex = "\\D+(\\d+)\\D+"; // 匹配非数字开头，跟一个数字，再跟非数字结尾

            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(friendCountText);
            String number = "";
            if (matcher.find()) {
                number = matcher.group(1); // 提取数字部分
            }  // 获取 好友数量 部分
            fbAccount.setNote(fbAccount.getNote()+number+"好友");
            fbAccountMapper.updateFbAccount(fbAccount);
        }
        return "1";
    }

    // 获取第 n 级父元素的方法
    public Element getNthParent(Element element, int levels) {
        Element currentElement = element;

        for (int i = 0; i < levels; i++) {
            if (currentElement == null) {
                return null; // 如果没有父元素，返回 null
            }
            currentElement = currentElement.parent();
        }

        return currentElement; // 返回第 n 级父元素，或找不到时返回 null
    }

    //等待页面加载
    public boolean waitingForContent(int time, WebDriver webDriver, String content) {
        for (int i = 0; i < time; i++) {
            if (webDriver.getPageSource().contains(content)) {
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
