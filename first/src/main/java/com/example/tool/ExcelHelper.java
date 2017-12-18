package com.example.tool;

import com.example.base.Pair;

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
    public static String FILENAME = "SLR(merge).xls";
    public static HashMap<String, Integer> map = new HashMap<>();
    public static HashMap<Integer, String> map2 = new HashMap<>();
    public static int i = 0, k = 30;

    public static void main(String[] args) {
        File file = saveExcel();
        System.out.println(file.getAbsolutePath());
    }

    public static void saveToExcel(ArrayList<Pair<Pair<Integer, String>, Integer>> table) {
        init();
        try {
            File file = saveExcel();
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

    public static File saveExcel() {
        String rootPah = "first\\file\\";
        File dir = new File(rootPah);
        return new File(dir, FILENAME);
    }
}
