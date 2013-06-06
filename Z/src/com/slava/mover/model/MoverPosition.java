package com.slava.mover.model;

import java.util.Arrays;

import com.slava.common.RectangularField;

public class MoverPosition {
	byte mover;
	byte[] boxes;

	MoverPosition previous = null;
	int direction = -1;
	boolean pull = false;
	
	public MoverPosition() {}
	
	public void addBox(int p) {
		if(boxes == null) {
			boxes = new byte[]{(byte)p};
		} else {
			byte[] bs = new byte[boxes.length + 1];
			System.arraycopy(boxes, 0, bs, 0, boxes.length);
			bs[boxes.length] = (byte)p;
			boxes = bs;
		}
	}
	
	public boolean equals(MoverPosition position) {
		if(mover != position.mover) return false;
		if(boxes == null && boxes != position.boxes) return false;
		if(boxes.length != position.boxes.length) return false;
		for (int i = 0; i < boxes.length; i++) {
			if(boxes[i] != position.boxes[i]) return false;
		}
		return true;
	}
	
	public boolean canMove(int d, RectangularField field, int[] filter) {
		int p = field.jump(mover, d);
		return p >= 0 && filter[p] > 0 && !isBox(p);
	}
	
	public boolean canPull(int d, RectangularField field, int[] filter) {
		int p = field.jump(mover, field.getOppositeDirection(d));
		return canMove(d, field, filter) && p >= 0 && isBox(p);
	}
	
	public boolean isBox(int p) {
		for (int i = 0; i < boxes.length; i++) {
			if(boxes[i] == p) return true;
		}
		return false;
	}
	
	public MoverPosition copy() {
		MoverPosition c = new MoverPosition();
		c.boxes = (byte[])boxes.clone();
		c.mover = mover;
		return c;
	}
	
	public MoverPosition move(int d, RectangularField field, int[] filter) {
		if(!canMove(d, field, filter)) return null;
		MoverPosition c = copy();
		c.previous = this;
		c.mover = (byte)field.jump(mover, d);
		c.direction = d;
		return c;
	}
	
	public MoverPosition pull(int d, RectangularField field, int[] filter) {
		if(!canPull(d, field, filter)) return null;
		MoverPosition c = move(d, field, filter);
		int p = field.jump(mover, field.getOppositeDirection(d));
		for (int i = 0; i < boxes.length; i++) {
			if(boxes[i] == p) c.boxes[i] = mover;
		}
		Arrays.sort(c.boxes);
		c.pull = true;
		return c;
	}
	
	public int code(int size, int module) {
		int q = mover;
		for (int i = 0; i < boxes.length; i++) {
			q = q * size + boxes[i];
			q = (q % module);
		}
		return q;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(mover).append(';');
		for (int i = 0; i < boxes.length; i++) {
			sb.append(' ').append(boxes[i]);
			
		}
		return sb.toString();
	}
	
}
