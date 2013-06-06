package com.slava.packedsea;

public class ShipPosition {
	int size;
	int place;
	int direction;
	
	public ShipPosition(int size, int place, int direction) {
		this.size = size;
		this.place = place;
		this.direction = direction;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getPlace() {
		return place;
	}
	
	public void setPlace(int p) {
		place = p;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int d) {
		direction = d;
	}
	
	public String toString() {
		return "" + size + ":" + place + (direction == 0 ? "h" : "v");
	}
	
	public ShipPosition copy() {
		return new ShipPosition(size, place, direction);
	}
	
	public static ShipPosition[] copy(ShipPosition[] original) {
		ShipPosition[] copy = new ShipPosition[original.length];
		for (int i = 0; i < copy.length; i++) copy[i] = original[i].copy();
		return copy;
	}

}
