package com.example;
/**
 * Num 描述整数
 * @author DELL
 *
 */
public class Num extends Token {
	public final int value;//识别的词素，例如输入的1
	public Num(int v) { 
		super(Tag.NUM); 
		value = v;
	}
	
	public String toString() {
		return "" + value;
	}
}