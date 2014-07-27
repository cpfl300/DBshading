package org.nhnnext.sharding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User {
	private int userId;
	private int galaxyPos;
	private List<Ship> shipList;
	
	public User(int galaxyPos){
		int num = 10;
		this.galaxyPos = galaxyPos;
		createShip(num);
	}

	private void createShip(int num) {
		shipList = new ArrayList<Ship>();
		for(int i = 0 ; i < num ; i++){
			shipList.add(new Ship(galaxyPos));
		}
	}

	public void attack() {
		
//		System.out.println(galaxyPos+" shipSize: "+shipList.size());
		Iterator<Ship> ir = this.shipList.iterator();
		while (ir.hasNext()) {
			Ship curShip = ir.next();
			curShip.attack(true);
		}
		
	}
}
