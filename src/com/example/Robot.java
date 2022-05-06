package com.example;

public class Robot {
	String robotId;
	long x;
	long y;	
	long batteryLevel;
	double distance;
	
	public Robot(String robotId, long x, long y, long batteryLevel) {
		this.robotId = robotId;
		this.x = x;
		this.y = y;
		this.batteryLevel = batteryLevel;
	}
	
	public void computeDistance(long tx, long ty) {
		this.distance = Math.sqrt(Math.pow(tx - x, 2) + Math.pow(ty - y, 2));
	}
}
