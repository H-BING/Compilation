package com.example.tool;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by 59800 on 2017/12/15.
 */

public class FileHelper {

//    public static String FILE_INPUT = "file/test2.txt";
    public static String FILE_INPUT = "first/file/test2.txt";
    public static String FILE_OUTPUT = "first/file/output.txt";

    public static String getInputFromText(String filePath) {
        StringBuilder builder = new StringBuilder("");
        try {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String input = "";
                while ((input = bufferedReader.readLine()) != null) {
                    builder.append(input).append("\n");
                }
            } else {
                System.out.println("找不到指定文件！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static String[] getInputFromText() {
        StringBuilder builder = new StringBuilder("");
        try {
            String encoding = "GBK";
            File file = new File(FILE_INPUT);
            if (file.isFile() && file.exists()) {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String input = "";
                while ((input = bufferedReader.readLine()) != null) {
                    input = input.trim();
                    if (!(input.length() > 0))
                        continue;
                    builder.append(input.trim()).append(" ");
                }
                builder.append("$");
            } else {
                System.out.println("找不到指定文件！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString().split(" ");
    }
}
