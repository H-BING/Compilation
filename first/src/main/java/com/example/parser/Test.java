package com.example.parser;

import com.example.base.Pair;
import com.example.base.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.example.base.Token;
import com.example.main.Main;
import com.example.tool.ExcelHelper;
import com.example.tool.FileHelper;

public class Test {
    
	static int[][] begin = new int[55][2];
	Closure[] C = new Closure[5500];
	int num = 0;//C集合个数
	Map<Goto,Integer> go = new HashMap<Goto,Integer>();
	Map<Goto,Integer> back = new HashMap<Goto,Integer>();//规约表
	
	int offinit = 100;
	int offset = offinit;//三地址代码初始地址
	int tNum = 0;//临时变量标号
	String[] codes = new String[20000];//存储
	int[] M = new int[20000];//占位符
	
	/**
	 * 构建项目集合与goto表
	 */
	public void items() {
		init();//初始化S0
//		Goto temp = new Goto(1,"$");
//		back.put(temp, 0);
		//拓展
		for(int i = 0; i < num; i++) {
			extendClosure(C[i],i);
		}

//		print();
		ExcelHelper.saveClosure(go, C, num);
		
	}
	
	private void print() {
		// TODO Auto-generated method stub
		for(int i = 0; i < num; i++) {
			System.out.println("----------S"+i+"----------");
			for(int j = 0; j < C[i].number; j++) {
				int a = C[i].result[j][0];
				int position = C[i].result[j][1];
				
				String B = Grammer.getPro(a);
				
				String[] s = B.split(" ");
				for(int k = 0; k < s.length; k++) {
					if(k == position){
						System.out.print("。"+" ");
					}
					System.out.print(s[k]+" ");
				}
				if(position >= s.length) {
					System.out.print("。");
				}
				String CC =Grammer.getNext(a, position);
				Goto temp = new Goto(i,CC);
				System.out.println("-----------S"+go.get(temp));
//				System.out.println(CC);
				
			}
		}
	}

	private void init() {
		
		C[0] = new Closure();
		// TODO Auto-generated method stub
		begin[0][0] = 0;
		begin[0][1] = 0;
		C[0].init(0, begin, 1);
		num++;
		
//		Goto(C[0]);
	}
	
	private void extendClosure(Closure closure, int id) {
		// TODO Auto-generated method stub
		for(int i = 0; i < closure.number; i++) {
			int number = 0;//下一个项目集初始产生式个数
			clear();
			
			//下一个输入符号
			String B = closure.next[i];
			//规约项目,将该文法Follows所有
			if(B == null) {
				int a = closure.result[i][0];//第几条文法

				String second = Grammer.getNext(a, 1);
//				System.out.println(second);


				String[] follows = Grammer.getFollows(a);
				for(int j = 0; j < follows.length; j++) {
					Goto back1 = new Goto(id,follows[j]);
//					back.put(back1, a);

					if (second != null) {
						if (second.equals("+") || second.equals("-")) {
							if (follows[j].equals("/") || follows[j].equals("*") || follows[j].equals("^")) {
								
							} 
							else {
								back.put(back1, a);
							}
						} else if (second.equals("*") || second.equals("/")) {
							if (follows[j].equals("^")) {
								// 不归约
							} else {
								back.put(back1, a);
							}
//							if (follows[j].equals("+") || follows[j].equals("-") || follows[j].equals("*") || follows[j].equals("/")) {
//								back.put(back1, a);
//							}
						} else if (second.equals("bool")) {
							String third = Grammer.getNext(a, 2);
							if(third.equals("then") && follows[j].equals("else")) {
								
							}else {
								back.put(back1, a);
							}
							
						}
						else {
							back.put(back1, a);
						}
					} else {
						back.put(back1, a);
					}

				}
			}

			//B非空，为待约项目
			else {
				Goto goto1 = new Goto(id,B);
				if(go.get(goto1) == null) {
				    //得到下一个项目集合初始文法
					
					begin[number][0] = closure.result[i][0];
					begin[number][1] = closure.result[i][1] + 1;//.位置+1
					number++;
					
					//将所有以B为下个输入符号的文法加入下一个项目集合
					for(int j = i+1; j < closure.number; j++) {
//						System.out.println(i+"?"+closure.number);
						if(B.equals(closure.next[j])) {
							begin[number][0] = closure.result[j][0];
							begin[number][1] = closure.result[j][1] + 1;
							number++;
						}
					}
					
					//判断下个输入是否已经有项目集可以包含，没有需要拓展新的项目集
					int nextStatus = nextClosure(begin,number);
					if(nextStatus == 0) {
						Closure temp = new Closure();
					    temp.init(id, begin, number);
					    C[num] = temp;
					    go.put(goto1, num++);
					    
					    
					    
					}
					else {
						
						
//						System.out.println(id +" "+B);
						if(back.get(goto1) == null) {
							go.put(goto1,nextStatus);
					    }
					}
					
					
//					if(isExistClosure(temp) == false) {
//						
////						System.out.println(num+"???");
//						
//					}
					
				}
				
			}
		}
		
	}
	
	private int nextClosure(int[][] begin2, int number) {
		// TODO Auto-generated method stub
		boolean exist = true;
		for(int i = 0; i < num; i++) {
			exist = true;
			for(int j = 0;j < number; j++) {
				int num = begin2[j][0];
				int position = begin2[j][1];
				if(C[i].rem[num][position] == 0) {
					exist = false;
					break;
				}
			}
			if(exist == true) {
				return i;
			}	
		}
		return 0;
	}

	private void clear() {
		// TODO Auto-generated method stub
		for(int i = 0; i < 55; i++) {
			begin[i][0] = 0;
			begin[i][1] = 0;
		}
		
	}
	
	public void printStack(Stack<Integer> stack) {
		int length = stack.size();
		int[] temp = new int[length];

		for(int i = 0; i < length; i++) {
			temp[i] = stack.pop();
		}

		for(int i = length - 1; i >= 0; i--) {
			stack.push(temp[i]);
			System.out.print(temp[i] + " ");
		}
		System.out.println();
	}

	public String getSingleStack(Stack<Integer> stack) {

		StringBuilder builder = new StringBuilder();

		int length = stack.size();
		int[] temp = new int[length];

		for(int i = 0; i < length; i++) {
			temp[i] = stack.pop();
		}

		for(int i = length - 1; i >= 0; i--) {
			stack.push(temp[i]);
//			System.out.print(temp[i] + " ");
			builder.append(temp[i] + " ");
		}
//		System.out.println();
		return builder.toString();
	}

	public String getSignStack(Stack<String> stack) {

		StringBuilder builder = new StringBuilder();

		int length = stack.size();
//		int[] temp = new int[length];
		ArrayList<String> temp = new ArrayList<String>();

		for(int i = 0; i < length; i++) {
//			temp[i] = stack.pop();
			temp.add(stack.pop());
		}

		for(int i = length - 1; i >= 0; i--) {
//			stack.push(temp[i]);
////			System.out.print(temp[i] + " ");
//			builder.append(temp[i] + " ");

			stack.push(temp.get(i));
			builder.append(temp.get(i)).append(" ");
		}
//		System.out.println();
		return builder.toString();
	}

	public String getType(Token temp) {
		String s = temp.toString().toLowerCase();
		switch (temp.tag) {
		case Tag.NUM:
			s = "num";
			break;
		case Tag.ID:
			s = "id";
			break;
		default:
			break;
				
		}
		return s;
	}
	/**
	 * 处理输入，判断对当前输入字符串采取动作
	 */
	public void parser() {
		Stack<Integer> stack = new Stack<Integer>();
		Stack<String> sign = new Stack<>();
		Stack<Token> tok= new Stack<Token>();
		
//		String[] input = FileHelper.getInputFromText();
//		String[] input = Main.getSLR1Input();
		String[] value = Main.getSLR1InputValue();

		Token[] tokens = Main.getSLR1InputToken();
		for (int i = 0; i < tokens.length; i++) {
			if(tokens[i].getValue().equals(":=")) {
				tokens[i].code = "=";
			}else {
				tokens[i].code = tokens[i].getValue();
			}	
			tokens[i].type = getType(tokens[i]);
//			System.out.println(tokens[i].code + " " + tokens[i].type);
		}

		ArrayList<String> arrayStack = new ArrayList<>();
		ArrayList<String> arrayAction = new ArrayList<>();
		
		int status = 0;//初始状态S0
		Token tokPop = new Token(0);//token栈顶
		stack.push(status);
		sign.push("$");
		
		String peek = getType(tokens[0]);//下个读入
		String valuePeek = value[0];
		
		for(int i = 0; i < tokens.length;) {
			
			printStack(stack);
			arrayStack.add(getSignStack(sign));

			Goto temp = new Goto(status,peek);
			//判断对当前输入
			if(back.get(temp) == null) {
				
				if(go.get(temp) == null) {
					
					System.out.println("!!!!!!!!!!!!!Error!!!!!!!!!!!!!");
					arrayAction.add("Error");
					break;
				}
				else {
					//移入
					System.out.println("Action:移入"+peek);
					arrayAction.add("移入"+valuePeek);
//					sign.push(peek);
					sign.push(valuePeek);
					status = go.get(temp);
					stack.push(status);
					
					gotoAction(valuePeek,tok);
					tok.push(tokens[i]);//将下个token放入栈
					if(valuePeek.equals("while") || valuePeek.equals("downto") || valuePeek.equals("to")) {
						tok.peek().instr = offset;
					}
					
					i++;
					peek = getType(tokens[i]);
					valuePeek = value[i];
				}
			}
			else {
				if(go.get(temp) == null) {
					//规约
					int num = back.get(temp);
					
					if(num == 0) {
						System.out.println("Action:接受");
						arrayAction.add("接受");
						break;//ACC
					}
					String A = Grammer.nonTerminal[Grammer.getLeft(num)];
					System.out.println("Action:按照第"+num+"条文法规约为"+A);

					arrayAction.add("按照 "+Grammer.nonTerminal[Grammer.getLeft(num)] + " 👉 " + Grammer.getPro(num) +" 规约");

					/**
					 * A->B
					 * 从栈中弹出B的个数个符号
					 */
					String[] nums = Grammer.getPro(num).split(" ");
					int length = nums.length;
					if(Grammer.getPro(num).equals("")) {
						length--;
//						System.out.println("!!");
					}
					semanticAction(num ,length, tok);//执行该文法对应的语义动作
					tok.peek().type = A;
					tok.peek().num = num;
					for(int k = 0; k < length; k++) {
						stack.pop();
						sign.pop();
					}
					status = stack.pop();
					stack.push(status);
//					System.out.println(nums[0]);
//					System.out.println(A);
					status = go.get(new Goto(status,A));
					
					stack.push(status);
					sign.push(A);
									
					
				}
				else {
					//二义
					System.out.println("二义！");
					break;
//					int num = back.get(temp); // 哪一条文法归约
//					String second = Grammer.getNext(num, 1);
//
//					System.out.println("Action:移入"+peek);
//					arrayAction.add("移入"+valuePeek);
//
////					sign.push(peek);
//					sign.push(valuePeek);
//
//					status = go.get(temp);
//					stack.push(status);
//					i++;
//					peek = getType(tokens[i]);
//					valuePeek = value[i];
				}
			}
			
		}
		
		for(int i = offinit;i < offset;i++) {
			System.out.println(i+":"+ codes[i]);
		}
		System.out.println(offset+":");
		FileHelper.outputToText(codes, offinit, offset);

		ExcelHelper.saveAnalysis(arrayStack, arrayAction);
        FileHelper.outputToFileSLR(arrayStack, FileHelper.FILE_STATCK);
        FileHelper.outputToFileSLR(arrayAction, FileHelper.FILE_ACTION);

	}
	
	private void gotoAction(String valuePeek, Stack<Token> tok) {
		// TODO Auto-generated method stub
		if(valuePeek.equals("downto") || valuePeek.equals("to")) {
			Token[] temp = new Token[3];
			temp[0] = tok.pop();
			temp[1] = tok.pop();
			temp[2] = tok.pop();
			codes[offset++] = temp[2].code + temp[1].code + temp[0].code;
			tok.push(temp[2]);
			tok.push(temp[1]);
			tok.push(temp[0]);
		}else if(valuePeek.equals("then")) {
			tok.peek().instr = offset;//L1.instr
			int t = offset + 2;
			codes[offset++] = "if(" + tok.peek().code + ") goto " + t;
			
			tok.peek().nextlist.add(offset);//L1.falselist
			codes[offset++] = "goto ";
		}else if(valuePeek.equals("else")) {
			if(tok.peek().nextlist.size() == 0) {
				tok.peek().nextlist.add(offset);//s1.nextlist
			    codes[offset++] = "goto ";
			}
			tok.peek().instr = offset;//s2.instr
		}else if(valuePeek.equals("do")) {
			if(tok.peek().type.equals("bool")) {
				Token[] temp = new Token[3];
				temp[0] = tok.pop();
				temp[1] = tok.pop();
				tok.push(temp[1]);
				tok.push(temp[0]);
				
				tok.peek().instr = temp[1].instr;//L1.instr
				int t1 = offset + 2;
				codes[offset++] = "if(" + tok.peek().code + ") goto " + t1;
				tok.peek().nextlist.add(offset);//L1.falselist
				codes[offset++] = "goto ";
			}else {
				Token[] temp = new Token[6];
				for(int i = 0; i < 6; i++) {
					temp[i] = tok.pop();
				}
				for(int i = 0; i < 6; i++) {
					tok.push(temp[i]);
				}
				if(temp[1].type.equals("downto")) {
					tok.peek().instr = temp[1].instr;//L1.instr
					int t1 = offset + 5;
					codes[offset++] = "if("+temp[4].code +">="+temp[0].code+") goto " + t1;
					tok.peek().nextlist.add(offset);//L1.falselist
					codes[offset++] = "goto ";
					codes[offset++] = "t" + tNum+"="+temp[4].code+"-1";
					codes[offset++] = temp[4].code+"=t"+tNum;
					int t2 = offset - 4;
					codes[offset++] = "goto " + temp[1].instr;
					tNum++;
				}else {
					tok.peek().instr = temp[1].instr;//L1.instr
					int t1 = offset + 5;
					codes[offset++] = "if("+temp[4].code +"<="+temp[0].code+") goto " + t1;
					tok.peek().nextlist.add(offset);//L1.falselist
					codes[offset++] = "goto ";
					codes[offset++] = "t" + tNum+"="+temp[4].code+"+ 1";
					codes[offset++] = temp[4].code+"= t"+tNum;
					int t2 = offset - 4;
					codes[offset++] = "goto " + temp[1].instr;
					tNum++;
				}
				
			}
		}
	}

	/*对应文法执行规约时候相应的语义动作*/
	private void semanticAction(int num2, int length, Stack<Token> tok) {
		// TODO Auto-generated method stub
		Token[] temp = new Token[length];
//		System.out.println(length+" "+tok.size());
		for(int i = 0; i < length - 1; i++) {
			temp[i] = tok.pop();
		}
		for(int i = length - 2; i >= 0; i--) {
			tok.peek().code = tok.peek().code + temp[i].code;
		}
		
		if(num2 == 25) {
//			tok.peek().code = "("+tok.peek()+")";
		}else if (num2 == 24 || num2 == 23) {
//			tok.peek().code = tok.peek().getValue();
		}else if(num2 >= 17 && num2 <= 21) {
			codes[offset++] = "t"+tNum+"=" + tok.peek().code;
//			System.out.println("t"+tNum+" = " + tok.peek().code);
			tok.peek().code = "t" + tNum;
			tNum++;
		}else if(num2 == 16 || num2 == 15 ) {
			
		}else if (num2 == 14) {
			//backpath(s1.nextlist,L2.instr)
			System.out.println(temp[0].type+" "+temp[0].value);
			if(temp[0].num == 5 || temp[0].num == 6 || temp[0].num == 7) {
				temp[0].nextlist.add(offset);//s1.nextlist
			    codes[offset++] = "goto ";
			}
			int t1 = temp[2].instr + 2;//L2.instr
			for (int i = 0; i < temp[0].nextlist.size(); i++) {
				int result = (int) temp[0].nextlist.get(i);
				codes[result] = codes[result] + t1;
		    }
			//s.next = L1.falselist;
			tok.peek().nextlist = temp[2].nextlist;

			for (int i = 0; i < tok.peek().nextlist.size(); i++) {
				System.out.println("============*****" + tok.peek().nextlist.get(i));
			}
			
		}else if (num2 == 13) {
			//backpath(s1.nextlist,L2.instr)
			if(temp[0].num == 5 || temp[0].num == 6 || temp[0].num == 7) {
				temp[0].nextlist.add(offset);//s1.nextlist
			    codes[offset++] = "goto ";
			}
			int t1 = temp[2].instr + 2;//L2.instr
			for (int i = 0; i < temp[0].nextlist.size(); i++) {
				int result = (int) temp[0].nextlist.get(i);
				codes[result] = codes[result] + t1;
		    }
			//s.next = L1.falselist;
			tok.peek().nextlist = temp[2].nextlist;
		}else if (num2 == 12) {
			//backpath(L1.falseList,s2.instr)
			ArrayList<Integer> tem = temp[4].nextlist;//L1.falseList
			int t1 = temp[2].instr;//s2.instr
			for (int i = 0; i < tem.size(); i++) {
				int result = (int) tem.get(i);
				codes[result] = codes[result] + t1;
		    }
			
			//s.nextlist = merge(s1.nextlist,s2.nextlist)
//			if(temp[0].nextlist.size() == 0) {
//				tok.peek().nextlist.add(offset);//s2.nextlist
//				codes[offset++] = "goto ";
//			}
//			tok.peek().nextlist.addAll(temp[0].nextlist);
//			tok.peek().nextlist.addAll(temp[2].nextlist);//s1.nextlist
			tok.peek().nextlist = temp[2].nextlist;
		}else if (num2 == 11) {//if_stmt -> if bool then stmt
			//s.nextlist = merge(L1.falselist,s1.nextlist)
//			if(temp[0].nextlist.size() == 0) {
//				tok.peek().nextlist.add(offset);
//				codes[offset++] = "goto ";
//			}
//			tok.peek().nextlist.addAll(temp[0].nextlist);//s1.nextlist
//			tok.peek().nextlist.addAll(temp[2].nextlist);//L1.falselist
//			for (int i = 0; i < tok.peek().nextlist.size(); i++) {
//				System.out.println("============" + tok.peek().nextlist.get(i));
//			}
			tok.peek().nextlist = temp[2].nextlist;
		}else if(num2 == 10){
			Token item = new Token(0);
			tok.push(item);
		}else if (num2 == 9) {
			//batchpath(s1.nextlist,L1.instr)
			int t1 = temp[2].instr;
			if(temp[0].num == 5 || temp[0].num == 6 || temp[0].num == 7) {
				temp[0].nextlist.add(offset);//s1.nextlist
				codes[offset++] = "goto ";
			}
			for (int i = 0; i < temp[0].nextlist.size(); i++) {
				int result = (int) temp[0].nextlist.get(i);
				codes[result] = codes[result] + t1;
		    }
			//s.nextlist = L1.falselist
			tok.peek().nextlist = temp[2].nextlist;
		}else if (num2 == 5) {//stmt->id:=expr
			codes[offset++] = tok.peek().code;
		}
		else {
			if(length > 1) {
				tok.peek().nextlist = temp[0].nextlist;
			}
			if(num2 == 2) {
				tok.peek().nextlist = temp[1].nextlist;
			}
			if(num2 == 3 || num2 == 4) {//stmts->stmt stmts->stmts;stmt
				//backpath(s.nextlist,offset)
				for (int i = 0; i < tok.peek().nextlist.size(); i++) {
					int result = (int) tok.peek().nextlist.get(i);
					codes[result] = codes[result] + offset;
			    }
				tok.peek().nextlist = new ArrayList<Integer>();
			}
		}
	}
	
	public void printGo() {
		ArrayList<Pair<Pair<Integer, String>, Integer>> tableGoto = new ArrayList<>();

		Iterator iter = go.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Goto go = new Goto();
			go = (Goto) entry.getKey();
			Integer next = (Integer) entry.getValue();
//			System.out.println(go.Cid+" "+go.B+" "+next);

			Integer state = Integer.valueOf(go.Cid);
			tableGoto.add(new Pair<>(new Pair<>(state, go.B), next));
		}

		Collections.sort(tableGoto, new Comparator<Pair<Pair<Integer, String>, Integer>>() {
			@Override
			public int compare(Pair<Pair<Integer, String>, Integer> p1, Pair<Pair<Integer, String>, Integer> p2) {
				if (p1.getFirst().getFirst() < p2.getFirst().getFirst()) {
					return -1;
				} else {
					return 1;
				}
			}
		});

		for (int i = 0; i < tableGoto.size(); i++) {
			System.out.println(tableGoto.get(i).getFirst().getFirst() + " "
					+tableGoto.get(i).getFirst().getSecond() + " " + tableGoto.get(i).getSecond());
		}

//		ExcelHelper.saveToExcel(tableGoto);
	}

	public void ExportExcel() {
		ArrayList<Pair<Pair<Integer, String>, Integer>> tableGoto = new ArrayList<>();
		ArrayList<Pair<Pair<Integer, String>, Integer>> tableBack = new ArrayList<>();

		Iterator itGo = go.entrySet().iterator();
		Iterator itBack = back.entrySet().iterator();
		while (itGo.hasNext()) {
			Map.Entry entry = (Map.Entry) itGo.next();
			Goto go = new Goto();
			go = (Goto) entry.getKey();
			Integer next = (Integer) entry.getValue();
//			System.out.println(go.Cid+" "+go.B+" "+next);

			Integer state = Integer.valueOf(go.Cid);
			tableGoto.add(new Pair<>(new Pair<>(state, go.B), next));
		}

		Collections.sort(tableGoto, new Comparator<Pair<Pair<Integer, String>, Integer>>() {
			@Override
			public int compare(Pair<Pair<Integer, String>, Integer> p1, Pair<Pair<Integer, String>, Integer> p2) {
				if (p1.getFirst().getFirst() < p2.getFirst().getFirst()) {
					return -1;
				} else {
					return 1;
				}
			}
		});

//		for (int i = 0; i < tableGoto.size(); i++) {
//			System.out.println(tableGoto.get(i).getFirst().getFirst() + " "
//					+tableGoto.get(i).getFirst().getSecond() + " " + tableGoto.get(i).getSecond());
//		}

		while (itBack.hasNext()) {
			Map.Entry entry = (Map.Entry) itBack.next();
			Goto go = new Goto();
			go = (Goto) entry.getKey();
			Integer next = (Integer) entry.getValue();
//			System.out.println(go.Cid+" "+go.B+" "+next);

			Integer state = Integer.valueOf(go.Cid);
			tableBack.add(new Pair<>(new Pair<>(state, go.B), next));
		}

		Collections.sort(tableBack, new Comparator<Pair<Pair<Integer, String>, Integer>>() {
			@Override
			public int compare(Pair<Pair<Integer, String>, Integer> p1, Pair<Pair<Integer, String>, Integer> p2) {
				if (p1.getFirst().getFirst() < p2.getFirst().getFirst()) {
					return -1;
				} else {
					return 1;
				}
			}
		});

//		for (int i = 0; i < tableBack.size(); i++) {
//			System.out.println(tableGoto.get(i).getFirst().getFirst() + " "
//					+tableGoto.get(i).getFirst().getSecond() + " " + tableGoto.get(i).getSecond());
//		}

//		ExcelHelper.saveToExcel(tableGoto, tableBack);
	}
	
	public void printBack() {
		ArrayList<Pair<Pair<Integer, String>, Integer>> tableGoto = new ArrayList<>();

		Iterator iter = back.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Goto go = new Goto();
			go = (Goto) entry.getKey();
			Integer next = (Integer) entry.getValue();
//			System.out.println(go.Cid+" "+go.B+" "+next);

			Integer state = Integer.valueOf(go.Cid);
			tableGoto.add(new Pair<>(new Pair<>(state, go.B), next));
		}

		Collections.sort(tableGoto, new Comparator<Pair<Pair<Integer, String>, Integer>>() {
			@Override
			public int compare(Pair<Pair<Integer, String>, Integer> p1, Pair<Pair<Integer, String>, Integer> p2) {
				if (p1.getFirst().getFirst() < p2.getFirst().getFirst()) {
					return -1;
				} else {
					return 1;
				}
			}
		});

		for (int i = 0; i < tableGoto.size(); i++) {
			System.out.println(tableGoto.get(i).getFirst().getFirst() + " "
					+tableGoto.get(i).getFirst().getSecond() + " " + tableGoto.get(i).getSecond());
		}
	}

	public String getInputFromToken(Token token) {
		return token.toString().toLowerCase();
	}
	
	public static void main(String[] args) {
		Grammer.init();
		Test test = new Test();
		test.items();
//		test.printBack();
//		test.printGo();
		test.parser();

		test.ExportExcel();
		
//		for(int i = 0; i < 26; i++) {
////     	    String next = Grammer.getNext(i, 2);
//			begin[0][0] = i;
//			begin[0][1] = 2;
//		    Closure closure = new Closure();
//	     	closure.init(0, begin, 1);
//		    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		}
		

		
	}

}
