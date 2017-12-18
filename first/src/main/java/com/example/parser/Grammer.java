package com.example.parser;

import java.util.HashMap;

public class Grammer {
	
	//表达式左部，非终结符号
	public static String[]  nonTerminal = new String[] {
			"S'","S","compound_stmt","stmts","stmt",
			"if_stmt","for_stmt","bool","expr","factor"
	};
	
	//表达式右部
	static String[] grammer = new String[] {
			"S",
			"program id ; compound_stmt .",
			"begin stmts end",
			"stmt","stmts ; stmt",
			"id := expr","compound_stmt","if_stmt","for_stmt","while bool do stmt","",
			"if bool then stmt","if bool then stmt else stmt",
			"for id := expr to expr do stmt","for id := expr downto expr do stmt",
			"expr > expr","expr < expr",
			"expr + expr","expr - expr","expr * expr","expr / expr","expr ^ factor","factor",
			"id","num","( expr )"
			
	};
	
	//frules与lrules分别是非终结符号第一条与最后一条表达式编号
	static HashMap<String,Integer> frules = new HashMap<String,Integer>();
	static HashMap<String,Integer> lrules = new HashMap<String,Integer>();
	
	/*每个非终结符为左部的表达式的个数*/
	public static int[] nums = new int[] {
//			1,1,1,2,6,2,2,2,6,3,
			1,2,3,5,11,13,15,17,23,26
	};
	
	Grammer() {
		init();
	}
	
	public static void init() {
		frules.put(nonTerminal[0], 0);
		lrules.put(nonTerminal[0], nums[0] - 1);
		for(int i = 1; i < nums.length; i++) {
//			System.out.println(i+" "+nums[i-1]+" "+nums[i]+" ");
			frules.put(nonTerminal[i], nums[i - 1]);
			lrules.put(nonTerminal[i], nums[i] - 1);
		}
	}
	
	public static String getPro(int number) {	
		String pro = grammer[number];
		return pro;		
	}
	
	public static String getNext(int number, int position) {
		
		String pro = grammer[number];
		
		String[] s = pro.split(" ");
		
		//test code
//		System.out.println(pro);
//		for(int k = 0; k < s.length; k++) {
//			System.out.println(s[k]);
//		}
		//end test
		
		if(position >= s.length) {
			return null;
		}
		String B = s[position];
        //产生式右边为空，归结
		if(B.equals("")) {
			return null;
		}
		return B;
	}
	
	
//	public static void main(String[] args) {
//		init();
//		int row = rules.get("stmt");
////		System.out.println(rules.get("bool"));
//		String[] rule = grammer[row];
//		
//		for(int i = 0; i < rule.length; i++) {
//			System.out.println(rule[i]);
//		}
//        
//    }

}
