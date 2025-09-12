package com.ruoyi.account.util;

import java.io.File;
import java.util.Random;

public class RandomUitl {

    /**
     * 生成随机文件名
     * @return
     */
    public static String generateRandomFilename() {
        Random random = new Random();
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }

    /**
     * 随机UA
     * @return
     */
    public static String generateRandomPCUserAgent() {

        Random random = new Random();

        String[] osTypes = {
                "Windows NT 10.0; Win64; x64","Windows NT 10.0",
                "Windows NT 6.3; Win64; x64", "Windows NT 6.3",
                "Windows NT 6.2; Win64; x64","Windows NT 6.2",
                "Windows NT 6.1; Win64; x64","Windows NT 6.1"
        };


        String[] chromeVersion = {
                "124.0.6367.207","125.0.6422.141","131.0.6778.204","131.0.6778.205","137.0.7151.115","137.0.7151.119",
                "137.0.7151.120","138.0.7204.49","138.0.7204.50","138.0.7204.35","138.0.7204.53","138.0.7204.56"
        };


        String userAgent = "Mozilla/5.0("+osTypes[random.nextInt(osTypes.length)]+
                ") AppleWebKit/537.36 (KHTML, like Gecko)Chrome/"+ chromeVersion[random.nextInt(chromeVersion.length)] + " Safari/537.36";

        return userAgent;
    }

    /**
     * 获取随机文件
     * @param folderPath
     * @return
     */
    public static String getRandomFile(String folderPath){

        File folder = new File(folderPath);

        // 获取文件夹中的所有文件
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null && listOfFiles.length > 0) {
            // 创建一个随机对象
            Random random = new Random();

            // 随机选择一个文件
            int randomIndex = random.nextInt(listOfFiles.length);
            File randomFile = listOfFiles[randomIndex];

            // 打印随机选择的文件名
            System.out.println("Randomly selected file: " + randomFile.getName());
            return randomFile.getName();
        } else {
            System.out.println("The specified folder does not contain any files.");
            return null;
        }
    }
}
