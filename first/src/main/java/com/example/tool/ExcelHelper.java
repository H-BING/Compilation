package com.example.tool;

import com.example.base.Pair;
import com.example.parser.Closure;
import com.example.parser.Goto;
import com.example.parser.Grammer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by 59800 on 2017/12/18.
 */

public class ExcelHelper {
    public static String FILE_SLR = "file/SLR(merge).xls";//?
    public static String FILE_CLOSURE = "file/LR(0).xls";
    public static String FILE_ANALYSIS = "file/analysis.xls";
    public static HashMap<String, Integer> map = new HashMap<>();
    public static HashMap<Integer, String> map2 = new HashMap<>();
    public static int i = 0, k = 30;

    public static void main(String[] args) {
//        File file = saveExcel();
//        System.out.println(file.getAbsolutePath());
    }

    public static void saveToExcel(ArrayList<Pair<Pair<Integer, String>, Integer>> table,
                                   ArrayList<Pair<Pair<Integer, String>, Integer>> back) {
        init();
        try {
            File file = saveExcel(FILE_SLR);
            if (file.exists()) {
                file.delete();
            }

            WritableWorkbook wwb = Workbook.createWorkbook(file);
            WritableSheet ws = wwb.createSheet("SLR(1)分析表", 0);

            for (int i = 0; i <= k; i++) {
                if (map2.containsKey(i)) {
                    String now = map2.get(i);
                    ws.addCell(new Label(i, 0, now));
                }
            }

            for (int i = 0; i < table.size(); i++) {
                int now = table.get(i).getFirst().getFirst();
                String next = table.get(i).getFirst().getSecond();
                String go = table.get(i).getSecond().toString();

                ws.addCell(new Label(0, now + 1, now + ""));
                if (map.containsKey(next)) {
                    int column = map.get(next);

                    String content = ws.getCell(column, now + 1).getContents();
                    if (content != null && content.length() > 0) {
                        System.out.println(now + " " + next + " " + go);
                        continue;
                    }

                    if (column < 30)
                        go = "S" + go;
                    ws.addCell(new Label(column, now + 1, go));
                }
            }

            for (int i = 0; i < back.size(); i++) {
                int now = back.get(i).getFirst().getFirst();
                String next = back.get(i).getFirst().getSecond();
                String go = back.get(i).getSecond().toString();

                ws.addCell(new Label(0, now + 1, now + ""));
                if (map.containsKey(next)) {
                    int column = map.get(next);

                    go = "r" + go;

                    String content = ws.getCell(column, now + 1).getContents();
                    if (content != null && content.length() > 0) {
                        System.out.println(now + " " + next + " " + go);
                        go = content + " | " + go;
//                        continue;
                    }

//                    if (column < 30)
//                        go = "r" + go;
                    ws.addCell(new Label(column, now + 1, go));
                }
            }

            wwb.write();
            wwb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {

        map.put("program", ++i); map2.put(i, "program");
        map.put("id", ++i); map2.put(i, "id");
        map.put(";", ++i); map2.put(i, ";");
        map.put(".", ++i); map2.put(i, ".");
        map.put("begin", ++i); map2.put(i, "begin");
        map.put("end", ++i); map2.put(i, "end");
        map.put(":=", ++i); map2.put(i, ":=");
        map.put("while", ++i); map2.put(i, "while");
        map.put("do", ++i); map2.put(i, "do");
        map.put("if", ++i); map2.put(i, "if");
        map.put("then", ++i); map2.put(i, "then");
        map.put("else", ++i); map2.put(i, "else");
        map.put("for", ++i); map2.put(i, "for");
        map.put("to", ++i); map2.put(i, "to");
        map.put("do", ++i); map2.put(i, "do");
        map.put("downto", ++i); map2.put(i, "downto");
        map.put(">", ++i); map2.put(i, ">");
        map.put("<", ++i); map2.put(i, "<");
        map.put("+", ++i); map2.put(i, "+");
        map.put("-", ++i); map2.put(i, "-");
        map.put("*", ++i); map2.put(i, "*");
        map.put("/", ++i); map2.put(i, "/");
        map.put("^", ++i); map2.put(i, "^");
        map.put("id", ++i); map2.put(i, "id");
        map.put("num", ++i); map2.put(i, "num");
        map.put("(", ++i); map2.put(i, "(");
        map.put(")", ++i); map2.put(i, ")");
        map.put("$", ++i); map2.put(i, "$");

        map.put("s", ++k); map2.put(k, "s");
        map.put("compound_stmt", ++k); map2.put(k, "compound_stmt");
        map.put("stmts", ++k); map2.put(k, "stmts");
        map.put("stmt", ++k); map2.put(k, "stmt");
        map.put("if_stmt", ++k); map2.put(k, "if_stmt");
        map.put("for_stmt", ++k); map2.put(k, "for_stmt");
        map.put("bool", ++k); map2.put(k, "bool");
        map.put("expr", ++k); map2.put(k, "expr");
        map.put("factor", ++k); map2.put(k, "factor");
    }

    public static void saveClosure(Map<Goto,Integer> go, Closure[] C, int num) {

        try {
            File file = saveExcel(FILE_CLOSURE);
            if (file.exists()) {
                file.delete();
            }

            WritableWorkbook wwb = Workbook.createWorkbook(file);
            WritableSheet ws = wwb.createSheet("LR(0)项目集族", 0);


            ws.addCell(new Label(0, 0, "状态"));
            ws.addCell(new Label(1, 0, "项目集"));
            ws.addCell(new Label(2, 0, "后继符号"));
            ws.addCell(new Label(3, 0, "后继状态"));

            int row = 1;
            for(int i = 0; i < num; i++) {
                ws.addCell(new Label(0, row, "S" + i)); // 状态
//                System.out.println("----------S"+i+"----------");
                for(int j = 0; j < C[i].number; j++, row++) {
                    int a = C[i].result[j][0];
                    int position = C[i].result[j][1];

                    String B = Grammer.getPro(a);

                    String[] s = B.split(" ");

                    StringBuilder builder = new StringBuilder(Grammer.nonTerminal[Grammer.getLeft(a)] + " 👉 ");

                    for(int k = 0; k < s.length; k++) {
                        if(k == position){
                            builder.append("· ");
//                            System.out.print("。"+" ");
                        }
                        builder.append(s[k] + " ");
//                        System.out.print(s[k]+" ");
                    }
                    if(position >= s.length) {
                        builder.append("· ");
//                        System.out.print("。");
                    }

                    ws.addCell(new Label(1, row, builder.toString())); // 项目集

                    // 后继符号
                    String next = Grammer.getNext(a, position);
                    if (next == null) {
                        next = Grammer.nonTerminal[Grammer.getLeft(a)] + " 👉 " + B;
                        next = next.trim();
                        if (next.endsWith("👉")) {
                            next = next + " ε";
                        }
                        ws.addCell(new Label(2, row, "# " + next));
                    } else {
                        ws.addCell(new Label(2, row, next));
                    }

                    String CC =Grammer.getNext(a, position);
                    Goto temp = new Goto(i,CC);
//                    System.out.println("-----------S"+go.get(temp));

                    // 后继状态
                    if (go.get(temp) == null) {
                        ws.addCell(new Label(3, row, "S" + num));
                    } else {
                        ws.addCell(new Label(3, row, "S" + go.get(temp)));
                    }
                }
            }
            wwb.write();
            wwb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void saveAnalysis(ArrayList<String> stack, ArrayList<String> action) {

        try {
            File file = saveExcel(FILE_ANALYSIS);
            if (file.exists()) {
                file.delete();
            }

            WritableWorkbook wwb = Workbook.createWorkbook(file);
            WritableSheet ws = wwb.createSheet("语法分析", 0);


            ws.addCell(new Label(0, 0, "栈"));
            ws.addCell(new Label(1, 0, "动作"));

            System.out.println(stack.size());
            System.out.println(action.size());
            for (int i = 0; i < stack.size(); i++) {
                ws.addCell(new Label(0, i + 1, stack.get(i))); // 栈
                ws.addCell(new Label(1, i + 1, action.get(i))); // 动作
            }

            wwb.write();
            wwb.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static File saveExcel(String filename) {
//        String rootPah = "first\\file\\";
    	String rootPah = "file/";
        File dir = new File(rootPah);
        return new File(dir, filename);
    }
}
