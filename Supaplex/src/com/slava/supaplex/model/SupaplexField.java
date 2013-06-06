package com.slava.supaplex.model;

public class SupaplexField {
	int width;
	int height;
	int size;
	int[] x, y;
	int[] border;
	
	public SupaplexField() {
		setSize(60, 24);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		size = width * height;
		x = new int[size];
		y = new int[size];
		border = new int[size];
		for (int i = 0; i < size; i++) {
			x[i] = (i % width);
			y[i] = (i / width);
			if(x[i] == 0 || x[i] == width - 1 || y[i] == 0 || y[i] == height - 1) border[i] = 1;
		}
	}
	
	public int getSize() {
		return size;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int x(int i) {
		return (i < 0 || i >= size) ? -1 : x[i];
	}

	public int y(int i) {
		return (i < 0 || i >= size) ? -1 : y[i];
	}
	
	public boolean isOnBorder(int i) {
		return i < 0 || i >= size || border[i] == 1;
	}

}
