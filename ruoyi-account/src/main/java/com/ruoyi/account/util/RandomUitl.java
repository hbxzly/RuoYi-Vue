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
                "109.0.5410.0", "109.0.5396.3", "109.0.5384.0", "108.0.5359.48", "108.0.5359.40",
                "108.0.5359.30", "108.0.5359.22", "108.0.5359.19", "108.0.5359.10", "108.0.5355.0",
                "108.0.5343.2", "108.0.5327.0", "107.0.5304.110", "107.0.5304.87", "107.0.5304.62",
                "107.0.5304.36", "107.0.5304.29", "107.0.5304.18", "107.0.5304.10", "107.0.5300.0",
                "107.0.5286.0", "106.0.5249.181", "106.0.5249.168", "106.0.5249.165", "106.0.5249.119",
                "106.0.5249.103", "106.0.5249.91", "106.0.5249.61", "106.0.5249.51", "106.0.5249.40",
                "106.0.5249.30", "106.0.5249.21", "106.0.5249.12", "106.0.5245.0", "106.0.5231.2",
                "106.0.5216.6", "105.0.5195.125", "105.0.5195.102", "105.0.5195.52", "105.0.5195.37",
                "105.0.5195.28", "105.0.5195.19", "105.0.5195.10", "105.0.5191.0", "105.0.5176.3",
                "105.0.5148.2", "105.0.5137.4", "104.0.5112.123", "104.0.5112.114", "104.0.5112.101",
                "104.0.5112.79", "104.0.5112.65", "104.0.5112.57", "104.0.5112.48", "104.0.5112.39",
                "104.0.5112.29", "104.0.5112.20", "104.0.5112.14", "104.0.5112.12", "104.0.5110.0",
                "104.0.5098.0", "104.0.5083.0", "103.0.5060.134", "103.0.5060.53", "103.0.5060.42",
                "103.0.5060.33", "103.0.5060.24", "103.0.5060.13", "103.0.5056.0", "103.0.5042.0",
                "103.0.5028.0", "102.0.5005.167", "102.0.5005.148", "102.0.5005.115", "102.0.5005.61",
                "102.0.5005.49", "102.0.5005.40", "102.0.5005.27", "102.0.5005.22", "102.0.5005.12",
                "102.0.4997.0", "102.0.4987.0", "102.0.4972.0", "101.0.4951.64", "101.0.4951.54",
                "101.0.4951.41", "101.0.4951.34", "101.0.4951.26", "101.0.4951.15", "101.0.4951.7",
                "101.0.4947.3", "101.0.4947.0", "101.0.4929.5", "101.0.4919.0", "100.0.4896.160",
                "100.0.4896.143", "100.0.4896.127", "100.0.4896.88", "100.0.4896.75", "100.0.4896.60",
                "100.0.4896.56", "100.0.4896.45", "100.0.4896.30", "100.0.4896.20", "100.0.4896.12",
                "100.0.4892.0", "100.0.4878.0", "100.0.4867.0"
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
