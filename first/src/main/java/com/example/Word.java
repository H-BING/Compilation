package com.example;

/**
 * Word 描述保留字、标识符、各种复合运算符
 * @author DELL
 *
 */
public class Word extends Token {
	
    public String lexeme = "";//词素
    public Word(String s,int tag) { 
    	super(tag); 
    	lexeme = s;
    }

    @Override
    public String toString(){
    	return lexeme;
    }
    
    //报错
    public static final Word error = new Word("ERROR", 0);
    
    //保留字 
    public static final Word[] keys = new Word[] {
    	new Word("ARRAY",101),new Word("BEGIN",102),new Word("CASE",103),
    	new Word("CONST",104),new Word("DO",105),new Word("DOWNTO",106),
    	new Word("ELSE",107),new Word("END",108),new Word("FILE",109),
    	new Word("FOR",110),new Word("FUNCTION",111),new Word("GOTO",112),
    	new Word("IF",113),new Word("IN",114),new Word("LABEL",115),
    	new Word("NIL",116),new Word("OF",117),new Word("PACKED",118),
    	new Word("OCEDURE",119),new Word("PROGRAM",120),new Word("RECORD",121),
    	new Word("REPEAT",122),new Word("SET",123),new Word("THEN",124),
    	new Word("TO",125),new Word("TYPE",126),new Word("UNTIL",127),
    	new Word("VAR",128),new Word("WHILE",129),new Word("WITH",130),
    };
    
    //标准常量
    public static final Word[] consts = new Word[] {
    	new Word("FALSE",200),new Word("MAXINT",201),new Word("TRUE",202)
    };
        
    //标准类型
    public static final Word[] types = new Word[] {
    	new Word("INTEGER",300),new Word("CHAR",301),new Word("BOOLEAN",200)	
    };
        
    //运算符
    public static final Word[] sopers = new Word[] {
    	new Word("+",400),new Word("-",401),new Word("*",402),
    	new Word("/",403),new Word("DIV",404),new Word("MOD",405)
    };
        
    //逻辑运算符
    public static final Word[] lopers = new Word[] {
    	new Word("Not",500),new Word("And",501),new Word("OR",502),new Word("XOR",503)
    };
    
    //关系运算符
    public static final Word[] ropers = new Word[] {
    	new Word("=",600),new Word("<>",601),new Word("<=",602),
    	new Word(">=",603),new Word("<",604),new Word(">",605),
    	new Word(":=",606), new Word(":", 607)
    };
    
    //标准函数暂时不写
    /*
	error = new Word ("error", Tag.ERROR),
    and = new Word ("&&" , Tag.AND), or = new Word ("||" , Tag.OR ),
    eq = new Word ( "==" , Tag.EQ ), ne = new Word ("!=" , Tag.NE ),
    le = new Word ( "<=" , Tag.LE ), ge = new Word (">=" , Tag.GE ),
    minus = new Word ( "minus", Tag.MINUS),
    Ture = new Word ( "ture", Tag.TRUE),
    False = new Word ( "false", Tag.FALSE),
    temp = new Word ( "t", Tag.TEMP);
    */
}
