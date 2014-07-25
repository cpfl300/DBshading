package org.nhnnext.sharding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Season {
	
	public static final int GALAXY_NUM = 4;
	public static List<Galaxy> galaxyList;
	public static ExecutorService shipExecutorService = Executors.newFixedThreadPool(4);
	public static ExecutorService signupExecutorService = Executors.newSingleThreadExecutor();
	
	//은하 4개가 있어야 함
	public Season(){
		createGalaxy();
	}
	
	private void createGalaxy() {
		galaxyList = new ArrayList<Galaxy>();
		
		for (int i=0; i<Season.GALAXY_NUM; i++) {
			
			galaxyList.add(new Galaxy(i));
		}
		
	}
	
	public void start() {
//		스레드 4개로 큐?에 들어있는 작업들을 처리
		for (int i=0; i<Season.GALAXY_NUM; i++) {
			final Galaxy curGalaxy = this.galaxyList.get(i);
			
			Runnable runnable = new Runnable() {

				public void run() {
					curGalaxy.start();
					
				}
								
			};
			
			Thread thread = new Thread(runnable);
			thread.start();
			
		}
		
	}
	
	public void shutdown() {
		Season.shipExecutorService.shutdown();
		Season.signupExecutorService.shutdown();
	}
	
//	random은하에 addUser를 호출하는 메서드 필요(스레드 1개)
	public void addUser(int galaxyPos) {
		final Galaxy galaxy = this.galaxyList.get(galaxyPos);
		
		Runnable runnable = new Runnable() {

			public void run() {		
				galaxy.addUser();
			}
			
		};
		
		Season.signupExecutorService.execute(runnable);
		
	}
	
}
