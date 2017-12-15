//package com.example.unit;
//
//import android.support.annotation.NonNull;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//
///**
// * Created by 59800 on 2017/12/15.
// */
//
//public class FileHelper {
//
//    public static String getInputFromText(String filePath) {
//        StringBuilder builder = new StringBuilder("");
//        try {
//            String encoding = "GBK";
//            File file = new File(filePath);
//            if (file.isFile() && file.exists()) {
//                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
//                BufferedReader bufferedReader = new BufferedReader(reader);
//                String input = "";
//                while ((input = bufferedReader.readLine()) != null) {
//                    builder.append(input).append("\n");
//                }
//            } else {
//                System.out.println("找不到制定文件！");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return builder.toString();
//    }
//}
