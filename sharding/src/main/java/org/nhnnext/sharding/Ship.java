package org.nhnnext.sharding;

import java.util.ArrayList;
import java.util.List;

public class Ship {
	private int galaxyPos;
	private int power;
	private List<Integer> attackable;
	
	public Ship(int galaxyPos){
		this.galaxyPos = galaxyPos;
		this.power = 5 + (int)(Math.random()*100);
		makeAttackable();
	}
	
	private void makeAttackable() {
		this.attackable = new ArrayList<Integer>();
		
		for (int i=0; i<Season.GALAXY_NUM; ++i) {
			if (i != this.galaxyPos) {
				attackable.add(i);
			}
		}
	}
	
	public void attackRandom(){
		final int pos = this.galaxyPos;
		
		
		Runnable runnable = new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				Galaxy galaxy = Season.galaxyList.get(pickOne());
				galaxy.minusHP(power);
				
				// log 찍기 ()번 은하의 함선이 ()번 은하를 공격하여 ()번 은하의 HP가 ()이 되었습니다.
				System.out.println( pos+"번 은하의 함선이 "+galaxy.getPos()+"번 은하를 공격하여 "+galaxy.getPos()+"번 은하의 HP가 "
						+galaxy.getHP()+"이 되었습니다.");				
				galaxy.isAlive();
			}
			
		};
		
		Season.shipExecutorService.execute(runnable);
		
	}
	
	private int pickOne() {
		
		int one = (int)(Math.random() * Season.GALAXY_NUM-1);
		
		return attackable.get(one);
	}

	public void attack(boolean condition) {
		while (condition) {
			this.attackRandom();
		}
		
	}
}
