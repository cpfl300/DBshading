package org.nhnnext.sharding;


public class MainClass {
	
	static final int GALAXY_NUM = 4;
	public static final int GALAXY_HP = 1000000;

	static final String GLO_DB_IP = "10.73.45.134";
	static final String YO_DB_IP1 = "10.73.45.72";
	static final String YO_DB_IP3 = "10.73.45.59";
	static final String YO_DB_IP2 = "10.211.55.8";
	static final String GLO_DB_NAME = "glodb";
	static final String YODA_DB_NAME = "yoda";
	
	public static void main(String[] args) {
		
		//초기세팅
		Season season = new Season();
		
		season.start();
		season.signUp(100000);
		
	}
	
}
