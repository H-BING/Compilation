package com.example.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.example.base.Pair;

/**
 * Created by 59800 on 2017/12/15.
 */

public class getSLR1 {

    static String[][] rules = new String[][]{
            new String[] {
                    "ss -> s "
            },
            new String[] {
                    "s -> program id ; compound_stmt . "
            },
            new String[] {
                    "compound_stmt -> begin stmts end "
            },
            new String[] {
                    "stmts -> stmt ",
                    "stmts -> stmts ; stmt "
            },
            new String[] {
                    "stmt -> id := expr ",
                    "stmt -> compound_stmt ",
                    "stmt -> if_stmt ",
                    "stmt -> for_stmt ",
                    "stmt -> while bool do stmt ",
                    "stmt -> "
            },
            new String[] {
                    "if_stmt -> if bool then stmt ",
                    "if_stmt -> if bool then stmt else stmt "
            },
            new String[] {
                    "for_stmt -> for id := expr to expr do stmt ",
                    "for_stmt -> for id := expr downto expr do stmt "
            },
            new String[] {
                    "bool -> expr > expr ",
                    "bool -> expr < expr "
            },
            new String[] {
                    "expr -> expr + expr ",
                    "expr -> expr - expr ",
                    "expr -> expr * expr ",
                    "expr -> expr / expr ",
                    "expr -> expr ^ factor ",
                    "expr -> factor "
            },
            new String[] {
                    "factor -> id ",
                    "factor -> num ",
                    "factor -> ( expr ) "
            }
    };

    static String[][] result = new String[100][100];
    static int[][] status = new int[100][100];
    static HashMap<String, Integer> map = new HashMap<>();
    static int[] len = new int[100];
    static int COUNT = 20;
    static int INF = 999999;
    static int END = 888888;
    static int conut = 1;
    static HashMap<String, Integer> finalRules = new HashMap<>();


    public static void main(String[] args) {
        initData();
        SLR1();
        print(COUNT);
    }

    public static void SLR1(){
        result[0][0] = "ss ->·s ";
        String str, next;
        int positionStart;
        int positionEnd;

//        str = result[0][0];
        len[0]++;
        for (int i = 0; i < COUNT; i++) {
            for (int j = 0; j < len[i]; j++) {
                str = result[i][j];
                len[i] = exploreSingle(i, j, str);
            }
        }
    }

    static int exploreSingle(int i, int k, String str) {

        int newLength = len[i];
        // 判断当前字符串是否可拓展
        if (str.endsWith("·")) { // 当前字符串不可拓展
            status[i][k] = END;
            return newLength;
        }
        // 当前字符串可继续拓展

        String next;
        int positionStart;
        int positionEnd;
        // 获取下一个字符串
        positionStart = str.indexOf("·");
        positionEnd = str.indexOf(" ", positionStart);
        next = str.substring(positionStart + 1, positionEnd);
        // 判断是否为终结符号
        if (map.containsKey(next)) { // 非终结符号
            int index = map.get(next);
            int fix = 0;
            for (int j = 0; j < rules[index].length; j++) {
                String temp = rules[index][j];
                int pos = temp.indexOf("->");
                temp = temp.substring(0, pos + 2) + "·" + temp.substring(pos + 3);

                // 判断temp是否在当前状态已经存在
                if (isExist(temp, i, newLength + j)) {
                    fix++;
                    continue;
                }

                result[i][newLength + j] = temp;
//                System.out.println(temp);
            }
            newLength += rules[index].length - fix;
        } else { // 终结符号
            if (k + 1 < len[i]) { // 继续拓展
//                exploreSingle(i, k + 1, result[i][k + 1]);
            } else { // 无需拓展
                String temp = new String();
                HashMap<String, Integer> tempMap = new HashMap<>();
//                HashMap<String, Pair<Integer, String>> tempRules = new HashMap<>();
                ArrayList<Pair<Pair<Integer, Integer>, String>> tempRules = new ArrayList<>();
//                int tempCount = conut;

                // 根据后继符号判断转跳情况并将已有项目填入对应状态
                for (int j = 0; j < len[i]; j++) {
                    temp = result[i][j];
                    if (!temp.endsWith("·")) { // 当前文法不可规约，仍需继续移入
                        // 查找后继符号
                        int pos = temp.indexOf("·");
                        int posEnd = temp.indexOf(" ", pos);
                        next = temp.substring(pos + 1, posEnd);

                        // 移动·的位置
                        int relpace = temp.indexOf(" ", pos);
                        temp = temp.substring(0, pos) + " " + temp.substring(pos + 1, relpace) + "·"
                                + temp.substring(relpace + 1);

                        if (tempMap.containsKey(next)) { // 相同后继符号应转入相同状态
                            int tempCount = tempMap.get(next);
                            result[tempCount][len[tempCount]] = temp;
                            status[i][j] = tempCount;

                            tempRules.add(new Pair<>(new Pair<>(tempCount, j), temp));

                            len[tempCount]++;


//                            int tempTempCount = tempMap.get(next);
//                            status[i][j] = tempTempCount;
//                            tempRules.add(new Pair<>(new Pair<>(tempTempCount, j), temp));
                        } else {
                            tempMap.put(next, conut);
                            result[conut][len[conut]] = temp;
                            status[i][j] = conut;
                            tempRules.add(new Pair<>(new Pair<>(conut, j), temp));

                            len[conut]++;
                            conut++;

//                            tempMap.put(next, tempCount);
//                            tempRules.add(new Pair<>(new Pair<>(tempCount, j), temp));
//                            status[i][j] = tempCount;
//
//                            tempCount++;
                        }
                    } else { // 当前文法可规约
                        status[i][j] = END;
                    }
                }

                // 调整后继状态
//                if (i == 15)
//                    adjustFollowState(tempRules, i);


//                conut = tempCount;
            }
        }

        return newLength;
    }

    // 调整后继状态 ArrayList(Pair<Pair<state, index>, rule>)
    static void adjustFollowState(ArrayList<Pair<Pair<Integer, Integer>, String>> rule, int i) {

        Collections.sort(rule, new Comparator<Pair<Pair<Integer, Integer>, String>>() {
            @Override
            public int compare(Pair<Pair<Integer, Integer>, String> p1, Pair<Pair<Integer, Integer>, String> p2) {
                if (p1.getFirst().getFirst() < p2.getFirst().getFirst()){
                    return -1;
                } else {
                    return 1;
                }
            }
        });

//        for (int j = 0; j < rule.size(); j++) {
//            System.out.println("" + rule.get(j).getFirst().getFirst() + ", " + rule.get(j).getFirst().getSecond()
//                    + ", " + rule.get(j).getSecond());
//        }


        for (int k = 0, j = 0; k < i && j < rule.size(); k++) {
            Boolean change = false;
            while (k == rule.get(j).getFirst().getFirst()) {

            }
        }
    }

    // 判断单个项目内是否循环
    static Boolean isExist(String str, int i, int length) {
        for (int j = 0; j < length; j++) {
            if (result[i][j].equals(str)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否之前所有项目中出现过
    static int isExist(String str, int i) {
        for (int k = 0; k <= i; k++) {
            if (result[k][0].equals(str)) {
                return k;
            }
        }
        return -1;
    }

    static void print(int n){
        for (int i = 0; i < n; i++) {
            System.out.printf("============= %03d =============\n", i);
            for (int j = 0; j < len[i]; j++) {
                System.out.println(result[i][j] + " ---------- " + status[i][j]);
            }
        }
    }

    static void initData() {
        map.put("ss", 0);
        map.put("s", 1);
        map.put("compound_stmt", 2);
        map.put("stmts", 3);
        map.put("stmt", 4);
        map.put("if_stmt", 5);
        map.put("for_stmt", 6);
        map.put("bool", 7);
        map.put("expr", 8);
        map.put("factor", 9);

        for (int i = 0; i < status.length; i++) {
            for (int j = 0; j < status[i].length; j++) {
                status[i][j] = INF;
            }
        }
    }

    static void testForInitData(){
        for (int i = 0; i < rules.length; i++) {
            for (int j = 0; j < len[i]; j++) {
                System.out.println(rules[i][j]);
            }
        }
    }
}
