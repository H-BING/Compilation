package com.example.parser;

import com.example.base.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.example.tool.ExcelHelper;
import com.example.tool.FileHelper;

public class Test {
    
	static int[][] begin = new int[55][2];
	Closure[] C = new Closure[5500];
	int num = 0;//C集合个数
	Map<Goto,Integer> go = new HashMap<Goto,Integer>();
	Map<Goto,Integer> back = new HashMap<Goto,Integer>();//规约表
	
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

		print();
		
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
				String[] follows = Grammer.getFollows(a);
				for(int j = 0; j < follows.length; j++) {
					Goto back1 = new Goto(id,follows[j]);
				    back.put(back1, a);
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
						go.put(goto1,nextStatus);
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

	
	/**
	 * 处理输入，判断对当前输入字符串采取动作
	 */
	public void parser() {
		Stack<Integer> stack = new Stack<Integer>();
		String[] input = FileHelper.getInputFromText();
		
		int status = 0;//初始状态S0
		stack.push(status);
		String peek = input[0];
//		String oldpeek = input[0];
		
		for(int i = 0; i < input.length;) {
//			System.out.println(status+" "+input[i]+" "+peek);
			Goto temp = new Goto(status,peek);
			//判断对当前输入
			if(back.get(temp) == null) {
				if(go.get(temp) == null) {
//					putError();	
					System.out.println("!");
					break;
				}
				else {
					//移入
					System.out.println("Action:移入"+peek);
					status = go.get(temp);
					stack.push(status);
					i++;
					peek = input[i];
				}
			}
			else {
				if(go.get(temp) == null) {
					//规约
					int num = back.get(temp);
					
					if(num == 0) {
						System.out.println("Action:接受");
						break;//ACC
					}
					String A = Grammer.nonTerminal[Grammer.getLeft(num)];
					System.out.println("Action:按照第"+num+"条文法规约为"+A);
					
					
					/**
					 * A->B
					 * 从栈中弹出B的个数个符号
					 */
					String[] nums = Grammer.getPro(num).split(" ");
					for(int k = 0; k < nums.length; k++) {
						stack.pop();
					}
					status = stack.pop();
					stack.push(status);
					
					if(go.get(new Goto(status,A))== null) {
//						System.out.println(status+" "+A);
						//归结
//						break;
//						i++;
//						peek = input[i];
//						continue;
					}
					else {
						status = go.get(new Goto(status,A));
						stack.push(status);
					}				
					
				}
				else {
					System.out.println("?");
					break;
					//二义
				}
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

		ExcelHelper.saveToExcel(tableGoto, tableBack);
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
	
	public static void main(String[] args) {
		Grammer.init();
		Test test = new Test();
		test.items();
//		test.printBack();
//		test.printGo();
		test.parser();
		
		
//		test.ExportExcel();
		
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
