package com.example.base;

import java.util.ArrayList;

/**
 * Token 描述所有记号基类
 * @author DELL
 */

public class Token {
	
	public final int tag;//区分不同记号
	public String code;//该token表示的代码
	public String value;
	public String type;
	public ArrayList<Integer> nextlist = new ArrayList<Integer>();//出口列表
	public int next = 0;//出口
	public int instr = 0;//三地址编码
	
	public Token(int t) {
		tag = t;
	}
	
	public String getValue() {
		return this.toString().toLowerCase();
	}
	
	public String toString() {
		return "" + (char)tag;
	}
}