package com.example;

import java.io.IOException;
import java.util.Hashtable;

public class Lexer {

    public int line = 1;//行号
    char peek = ' ';//往前看一个字符
    char oldpeek = ' ';
    String input = "";


    int index = 0;

    public static Hashtable<String, Word> words = new Hashtable<String, Word>();//符号表

    public Lexer() {
        /**
         * 将Word中保留字，什么的全部加入符号表
         */
        for (int i = 0; i < Word.keys.length; i++) {
            words.put(Word.keys[i].lexeme, Word.keys[i]);
        }
        for (int i = 0; i < Word.consts.length; i++) {
            words.put(Word.consts[i].lexeme, Word.consts[i]);
        }
        for (int i = 0; i < Word.lopers.length; i++) {
            words.put(Word.lopers[i].lexeme, Word.lopers[i]);
        }
        for (int i = 0; i < Word.sopers.length; i++) {
            words.put(Word.sopers[i].lexeme, Word.sopers[i]);
        }
        for (int i = 0; i < Word.ropers.length; i++) {
            words.put(Word.ropers[i].lexeme, Word.ropers[i]);
        }
        for (int i = 0; i < Word.types.length; i++) {
            words.put(Word.types[i].lexeme, Word.types[i]);
        }

        input = Main.getInputFromText();
//        System.out.println(input.length());
        index = 0;
    }

    /**
     * reserve将给定记号加入words
     *
     * @param w return null
     */
    void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    /**
     * 无输入时读入下一个字符，传给peek
     *
     * @throws IOException
     */

    void readch() {

//        System.out.println(peek + " " + index);
        peek = input.charAt(index++);
        if (peek == '\n'){
            line++;
//            System.out.println("emmm");
//            System.out.println(line);
//            System.out.println("================");
        }
    }

//    void readch() throws IOException {
//    	peek = (char)System.in.read();
//    }

    /**
     * 有输入时，读入字符到peek，与所传参数比较，相同为true,不同为false
     *
     * @param c
     * @return true/false
     * @throws IOException
     */
    boolean readch(char c) throws IOException {
        readch();
        if (peek != c) {
            return false;
        }
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {

        peek = ' ';//清空，防止上次数据干扰

        if (index >= input.length()) {
            return new Word("over", 40000);
        }

        /**
         * 略去空格
         * 遇见换行，行数加1
         */
//        if (oldpeek != ' ' && oldpeek != '\n' && oldpeek != '\r') {
            peek = oldpeek;
            oldpeek = ' ';
//        }

        while (true) {
            //'\t'为tab
            if (peek == ' ' || peek == '\t') {

            }
            //换行会产生两个字符'\n'回车,'\r'换行，但行数只需要加1
            else if (peek == '\n') {
                if (index >= input.length()) {
                    return new Word("over", 40000);
                }
            } else break;
            readch();
        }

        /**
         * 对运算符处理
         * 非法情况未处理
         */
        switch (peek) {
            case '+':
                return Word.sopers[0];
            case '-':
                return Word.sopers[1];
            case '*':
                return Word.sopers[2];
            case '/':
                return Word.sopers[3];
            case '=':
                return Word.ropers[0];
            case '<':
                readch();
                if (peek == '=') {
                    return Word.ropers[2];
                } else if (peek == '>') {
                    return Word.ropers[1];
                } else {
                    oldpeek = peek;
                    return Word.ropers[4];
                }
            case '>':
                readch();
                if (peek == '=') {
                    return Word.ropers[3];
                } else {
                    oldpeek = peek;
                    return Word.ropers[5];
                }
            case ':':
                readch();
                if (peek == '='){
                    return Word.ropers[6];
                } else {
                    oldpeek = peek;
                    return Word.ropers[7];
                }
//                if (readch('=')) {
//                    return Word.ropers[6];
//                }
        }

        /**
         * Character.isDigit 判断字符是否为 0-9
         * 如果一个整数以单个“0”开头,则按八进制处理；以“0X”开头，按十六进制处理。
         * 以“00”开头时，自适应处理：当后续数字均不大于1时，按二进制；否则，当后续数字均不大于7时，按八进制处理；
         * 否则，当后续数字均不大于9时，按十进制处理；否则按十六进制处理。
         * 所有数字在输出词法单元时，均转为十进制。
         */

        if (Character.isDigit(peek)) {
            int numPoint = 0;
            int numChar = 0;
            int status = 2;//初始化数字为2进制
            StringBuffer num = new StringBuffer();
            char c;
            char first = peek;

            while (Character.isLetterOrDigit(peek) || peek == '.') {
                num.append(peek);
                readch();
                if (Character.isLetter(peek) || peek == '.') {
                    numChar++;
                    if (peek == '.') {
                        numPoint++;
                    }
                }
            }

            if (numPoint == 1) {
                if (numPoint != numChar) {
                    return Word.error;
                }
                float numfloat = Float.valueOf(num.toString());

                oldpeek = peek;
                Word word = new Word(num.toString(),Tag.REAL);
                words.put(num.toString(),word);
                Real real = new Real(numfloat);

                return real;

            } else if (numPoint > 1){
                return Word.error;
            }

//            do {
//                num.append(peek);
//                readch();
//            } while (Character.isLetterOrDigit(peek));

            String s = "" + num;
            s = s.toLowerCase();//全部转换为小写
            int[] values = new int[s.length()];

            if (first != '0') {
                status = 10;
            } else {
                //查看第二个字符
                c = s.charAt(1);
                if (c == 'x' && s.length() > 2) { // 16进制
                    status = 16;
                } else if ((c < '8' && c > '0')) { // 8进制
                    status = 8;
                } else if (c != '0') {
                    return Word.error;
                }
            }

            if (status == 8) {
                values[0] = 0;
                for (int i = 1; i < s.length(); i++) {
                    c = s.charAt(i);
                    if (c > '8') {
                        return Word.error;
                    }
                    values[i] = Character.digit(c, 10);
                }
            } else if (status == 10) {
                for (int i = 0; i < s.length(); i++) {
                    c = s.charAt(i);
                    if (c >= 'a') {
                        return Word.error;
                    }
                    values[i] = Character.digit(c, 10);
                }
            } else if(status == 16) {
                values[0] = 0;
                values[1] = 0;
                for (int i = 2; i < s.length(); i++) {
                    c = s.charAt(i);
                    if (c > 'f') {
                        return Word.error;
                    } else if (c >= 'a') {
                        values[i] = c - 'a' + 10;
                    } else {
                        values[i] = Character.digit(c, 10);
                    }
                }
            } else {
                values[0] = 0;
                values[1] = 0;
                for (int i = 2; i < s.length(); i++) {
                    c = s.charAt(i);
                    if (c > 'f') {
                        return Word.error;
                    }
                    if (Character.isDigit(c)) {
                        if (c > '1' && c < '8') {
                            if (status < 8)
                                status = 8;
                        } else if (c >= '8') {
                            if (status < 10)
                                status = 10;
                        }
                        values[i] = Character.digit(c, 10);
                    } else {
                        status = 16;
                        values[i] = c - 'a' + 10;
                    }
                }
            }

    		/*转换成10进制输出*/

    		int ans = 0;
            for (int i = 0; i < values.length; i++) {
                ans = (ans) * status + values[i];
            }
            if (ans < 0) {
//                System.out.println(Integer.valueOf(peek));
                System.out.print(peek);
                System.out.print(line);
                System.out.print(line);
                return Word.error;
            }

            oldpeek = peek;
            Word word = new Word(s,Tag.NUM);
            words.put(s,word);
            Num n = new Num(ans);

            return n;

        }


        /**
         * 能处理字符串，以双引号开头，双引号结尾,中间可能有大小写字母，数字，常见符号
         * （如+-* /=:._等)，空格和若干种常见转义符号。
         * 字符串不跨行（跨行报词法错误）。
         * 要求至少能处理如下两种转义符号例:
         * "abcde\"\\FGhij   opqr'st"
         * 对应词法单元是<string,abcde"\FGhij   opqr'st>
         */

        if (peek == '"') {
            StringBuffer b = new StringBuffer();

            readch();
            while (peek != '"') {
                //转义字符处理
                if (peek == '\\') {
                    readch();
                }
                b.append(peek);//添加peek

                if (peek == '\n') {

                    return Word.error;
                }
                readch();
            }
            readch();
            String s = "" + b;

            Word word = new Word(s,3000);
            words.put(s, word);
            return word;

//            return new Word(s, 3000);
        }

        //数字只可以出现在变量末尾
        if (Character.isLetter(peek)) {

            StringBuffer b = new StringBuffer();
            do {
                b.append(peek);//添加peek
                readch();
            } while (Character.isLetter(peek));
            if (Character.isDigit(peek)) {
                do {
                    b.append(peek);//添加peek
                    readch();
                } while (Character.isDigit(peek));
            }
            if (Character.isLetter(peek)) {
                return Word.error;
            }
            String s = "" + b;
            s = s.toUpperCase();
            oldpeek = peek;
            /**
             * 与标识符什么的匹配
             */
            for (int i = 0; i < Word.keys.length; i++) {
                if (s.equals(Word.keys[i].lexeme)) {
                    return Word.keys[i];
                }
            }
            for (int i = 0; i < Word.consts.length; i++) {
                if (s.equals(Word.consts[i].lexeme)) {
                    return Word.consts[i];
                }
            }
            for (int i = 0; i < Word.lopers.length; i++) {
                if (s.equals(Word.lopers[i].lexeme)) {
                    return Word.lopers[i];
                }
            }
            for (int i = 0; i < Word.sopers.length; i++) {
                if (s.equals(Word.sopers[i].lexeme)) {
                    return Word.sopers[i];
                }
            }
            for (int i = 0; i < Word.types.length; i++) {
                if (s.equals(Word.types[i].lexeme)) {
                    return Word.types[i];
                }
            }

            Word w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }

        //其他字符输入
        Token tok = new Token(peek);
        //peek = ' ';
        return tok;

    }
    /**
     * WHILE (next<>NIL)  DO BEGIN x:=6; Y:=x*y+z END;
     * WHILE (next<>NIL)  DO BEGIN x:=6; Y:=x*y+z END;
     */


}
