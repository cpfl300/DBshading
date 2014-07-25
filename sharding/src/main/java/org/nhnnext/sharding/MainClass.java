package org.nhnnext.sharding;

public class MainClass {
	public static void main(String[] args) {
		//초기세팅
		Season season1 = new Season();
		
		// user추가
		for(int i = 0 ; i < 10000; i++){
			season1.addUser(i%Season.GALAXY_NUM);
		}
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//실행(공격)
		season1.start();
		
//		for(int i = 0 ; i < 10000; i++){
//			season1.addUser(i%Season.GALAXY_NUM);
//		}
	}
}
