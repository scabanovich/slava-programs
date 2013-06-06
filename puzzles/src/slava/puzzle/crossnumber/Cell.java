package slava.puzzle.crossnumber;

public class Cell {
	int place;
	Cell[] neighbours;
	Sum hsum;
	Sum vsum;
	int value;
	
	public int getPlace() {
		return place;
	}
	
	public void setPlace(int p) {
		place = p;
	}
	
	public Sum getHSum() {
		return hsum;
	}

	public Sum getVSum() {
		return vsum;
	}
	
	public void setHSum(Sum s) {
		hsum = s;
	}
	
	public void setVSum(Sum s) {
		vsum = s;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value; 
	}

}
