package com.slava.common;

public class RectangularField extends TwoDimField {
	protected int[] oppositeDirections = {2,3,0,1};
	protected int[] rotation;
	protected int[] reflection;
	
	public RectangularField() {
		setOrts(RECTANGULAR_ORTS);
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);
		initTransforms();
	}
	
	protected void initTransforms() {
		rotation = new int[size];
		for (int i = 0; i < size; i++) {
			int ix = x[i], iy = y[i];
			rotation[i] = getIndex(width - iy - 1, ix);
		}
		reflection = new int[size];
		for (int i = 0; i < size; i++) {
			int ix = x[i], iy = y[i];
			reflection[i] = getIndex(ix, height - iy - 1);
		}
	}

	public int getOppositeDirection(int d) {
		return oppositeDirections[d];
	}
	
	public int[] getRotation() {
		return rotation;
	}
	
	public int[] getReflection() {
		return reflection;
	}
	
}
