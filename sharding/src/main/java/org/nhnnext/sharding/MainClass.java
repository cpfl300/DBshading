package org.nhnnext.sharding;

public class MainClass {
	
	static final String GLO_DB_IP = "10.73.45.134";
	static final String YO_DB_IP1 = "10.73.45.72";
	static final String YO_DB_IP2 = "10.73.45.59";
	static final String GLO_DB_NAME = "glodb";
	static final String YODA_DB_NAME = "yoda";
	
	
	public static void main(String[] args) {
		
		//초기세팅
		Season season1 = new Season();
		
	
		
		
		// user추가
		for(int i = 0 ; i < 10; i++){
			season1.addUser();
		}
		System.out.println("ok");
//		season1.testYoda();
		
		/*
		try { 
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//실행(공격)
		*/
		season1.start();
//		season1.dbtest();
//		for(int i = 0 ; i < 10000; i++){
//			season1.addUser(i%Season.GALAXY_NUM);
//		}
	}
}
