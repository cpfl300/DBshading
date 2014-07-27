package org.nhnnext.sharding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Galaxy {
	
	private int pos;
	
	public int getPos() {
		return pos;
	}

	private int HP = 100000;
	
	public int getHP() {
		return HP;
	}

	private List<User> userList; 
	
	public Galaxy(int pos){
		this.pos = pos;
		this.userList = new ArrayList<User>();
	}
	
	public void addUser(){
		// procedure
		// uid, gid, dbid
		
		userList.add(new User(pos));
	}
	
	synchronized public void minusHP(int power){
		this.HP -= power;
	}

	public void isAlive() {
		if (this.HP <= 0) {
//			스레드를 모두 종료시킨 후 프로그램을 종료한다.
			Season.shipExecutorService.shutdown();
			Season.signupExecutorService.shutdown();

//			각 은하의 HP를 모두 출력해 준 후
			for(int i = 0; i < Season.GALAXY_NUM; i++){
				System.out.println(Season.galaxyList.get(i).getPos()+"번 은하 HP: "+Season.galaxyList.get(i).getHP());
			}
			System.out.println("--GAME END--");
			
			System.exit(1);
		}
		
	}

	public void start() {
//		try {
//			Thread.sleep(1000);
//			System.out.println(pos+" user size: " + this.userList.size());
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Iterator<User> ir = this.userList.iterator();
		
		while(ir.hasNext()) {
			User curUser = ir.next();
			curUser.attack();
		}
		
	}
}
