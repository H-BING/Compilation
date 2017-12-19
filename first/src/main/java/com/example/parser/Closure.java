package com.example.parser;

public class Closure {

	int id;//项目集编号
	public int [][] result = new int[550][2];//产生式，第一个数字记录产生式编号，第二个记录当前.的位置
//	LinkedHashMap<Integer,Integer> production = new LinkedHashMap<Integer, Integer>();
	int[][] rem = new int[55][55];//根据产生式编号与.位置记录产生式是否添加过
	String[] next = new String[55];//下一个字符
	public int number = 0;
	
	public void init(int a,int[][] b,int c) {
		for(int i = 0; i < 55; i++) {
			for(int j = 0; j < 55; j++) {
				rem[i][j] = 0;
			}
		}
		id = a;
		for(int i = 0;i < c; i++) {
			result[i][0] = b[i][0];
			result[i][1] = b[i][1];
			
			rem[b[i][0]][b[i][1]]=1;
		}
		extendProduction(c);
	}

	private void extendProduction(int a) {
		
		/**
		 * 对production中每一个A->a.Bc,且B->r是产生式，
		 * 如果不在项目中，则加入后,直到没有项目可以加进来停止
		 */
		int row = a;
		for(int i = 0; i < row; i++) {
			int number = result[i][0];
			int position = result[i][1];
			
			//获取下一个字符
			String B = Grammer.getNext(number, position);
			next[i] = B;
//			System.out.println(B);		
			
			//B可以拓展
			if(Grammer.frules.get(B) != null) {
	
				int first = Grammer.frules.get(B);
				int last = Grammer.lrules.get(B);
				
				for(int j = first; j <= last; j++) {
//					System.out.println(row);
					//B没有拓展过
					if(rem[j][0] == 0) {
						rem[j][0] = 1;
						result[row][0] = j;
					    result[row][1] = 0;
					    row++;
					}   
					
				}
				
			}
		}
		
		number = row;
		print(row);
		
//		Iterator iter = production.entrySet().iterator();
//		while (iter.hasNext()) {
//			
//			java.util.Map.Entry entry = (java.util.Map.Entry) iter.next() ;
//			int number = (int) entry.getKey();
//			int position = (int) entry.getValue();
//			
//			String B = Grammer.getNext(number, position);//获取下一个字符
//			
//			System.out.println(Grammer.frules.get(B)+"!");
//			if(Grammer.frules.get(B) != null) {//B可拓展
////				System.out.println(B);
//				int first = Grammer.frules.get(B);
//				int last = Grammer.lrules.get(B);
//				
//				for(int i = first; i <= last; i++) {
//					production.put(i, 0);
//					
//				}
//
//			}
//			
//		}
		
		
	}
	/*
	 * 　　Map map = new HashMap();
　　Iterator iter = map.entrySet().iterator();
　　while (iter.hasNext()) {
　　Map.Entry entry = (Map.Entry) iter.next();
　　Object key = entry.getKey();
　　Object val = entry.getValue();
　　}
	 */
	
	public void print(int c) {
		
		for(int i = 0; i < c; i++) {
			int number = result[i][0];
			String B = Grammer.getPro(number);
//			System.out.println(B);
		}
		
	}
	
	
}
