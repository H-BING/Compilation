package com.example.tool;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by 59800 on 2017/12/15.
 */

public class FileHelper {

//    public static String FILE_INPUT = "file/test2.txt";
    public static String FILE_INPUT = "first/file/test2.txt";
    public static String FILE_OUTPUT = "first/file/output.txt";
    public static String FILE_STATCK = "first/file/stack.txt";
    public static String FILE_ACTION = "first/file/action.txt";

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

    public static void outputToText(String[] list, int start, int length) {
        try {
            File file = new File(FILE_OUTPUT);
            OutputStream outputStream = new FileOutputStream(file);

            StringBuilder builder = new StringBuilder();
            for (int i = start; i < length; i++) {
                builder.append(i).append(":").append(list[i]).append("\r\n");
            }
            builder.append(length).append(":").append("\r\n");

            byte[] data = builder.toString().getBytes();
            outputStream.write(data);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void outputToFileSLR(ArrayList<String> list, String filePath)  {
        try {
            File file = new File(filePath);
            OutputStream outputStream = new FileOutputStream(file);

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                builder.append(list.get(i)).append("\r\n");
            }

            byte[] data = builder.toString().getBytes();
            outputStream.write(data);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
