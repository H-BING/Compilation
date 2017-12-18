package com.example.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Test {
    
	static int[][] begin = new int[55][2];
	Closure[] C = new Closure[5500];
	int num = 0;//C集合个数
	Map<Goto,Integer> go = new HashMap<Goto,Integer>();
	
	public void items() {
		init();
		
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
			

			//B非空，为待约项目
			if(B != null ) {
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

//	public boolean isExistClosure(Closure temp) {
////		System.out.println("!");
//		
//		for(int j = 0; j < 30;j++) {
//			for(int k = 0; k < 10; k++) {
//				System.out.print(temp.rem[j][k]+" ");
//			}
//			System.out.println();
//		}
//		System.out.println();
//		boolean exist = true;
//		for(int i = 0; i < num; i++) {
//			exist = true;
//			for(int j = 0;j < temp.number; j++) {
//				int number = temp.result[j][0];
//				int position = temp.result[j][1];
//				if(C[i].rem[number][position] == 0) {
//					exist = false;
//					break;
//				}
//			}
//			
//		}
//		if(exist == true)System.out.println("!!!!!!!!!!!!!");
//		return exist;
//		
//	}
	
	public void printGo() {
		Iterator iter = go.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Goto go = new Goto();
			go = (Goto) entry.getKey();
			int next = (int) entry.getValue();
			System.out.println(go.Cid+" "+go.B+" "+next);
		}
	}
		 
	public static void main(String[] args) {
		Grammer.init();
		Test test = new Test();
		test.items();
		
//		test.printGo();
		
		
//		// TODO Auto-generated method stub
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