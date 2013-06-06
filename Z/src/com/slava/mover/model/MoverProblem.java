package com.slava.mover.model;

import com.slava.common.RectangularField;

public class MoverProblem implements MoverConstants {
	RectangularField field;
	int[] filter;
	MoverPosition initialPosition;
	MoverPosition finalPosition;
	
	public void setField(RectangularField field) {
		this.field = field;
	}
	
	public void init(int[] state) {
		filter = new int[field.getSize()];
		initialPosition = new MoverPosition();
		finalPosition = new MoverPosition();
		for (int i = 0; i < filter.length; i++) {
			int s = state[i];
			if(s == x) {
				filter[i] = 0;
			} else {
				filter[i] = 1;
				if(s == e) {					
				} else if(s == B) {
					initialPosition.addBox(i);
				} else if(s == D) {
					finalPosition.addBox(i);
				} else if(s == G) {
					initialPosition.addBox(i);
					finalPosition.addBox(i);
				} else if(s == M) {
					initialPosition.mover = (byte)i;
				} else if(s == Z) {
					initialPosition.mover = (byte)i;
					finalPosition.addBox(i);
				} else {
					throw new RuntimeException("Wrong value " + s + " at " + i);
				}
			}
		}
	}
	
	public RectangularField getField() {
		return field;
	}
	
	public MoverPosition getInitialPosition() {
		return initialPosition;
	}
	
	public MoverPosition getFinalPosition() {
		return finalPosition;
	}
	
	public int[] getFilter() {
		return filter;
	}

}
