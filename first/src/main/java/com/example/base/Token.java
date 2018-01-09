package com.example.base;

/**
 * Token 描述所有记号基类
 * @author DELL
 */

public class Token {
	
	public final int tag;//区分不同记号
	public String code;//该token表示的代码
	
	public Token(int t) {
		tag = t;
	}
	
	public String toString() {
		return "" + (char)tag;
	}
}