package barrels;

public class Barrel {
	int amount = 0;
	double water = 0d;
	
	public Barrel(int amount, double water) {
		this.amount = amount;
		this.water = water;
	}
	
	public void pourTo(Barrel b, int a) {
		double delta = (water * a) / amount;
		amount -= a;
		water -= delta;
		b.amount += a;
		b.water += delta;
	}
	
	public double getRelation() {
		if(amount == 0) return -1d;
		if(Math.abs(water - amount) < 1E-12) return -1d;
		return water / (amount - water);
	}
	
	public String toString() {
		return "(" + amount + "," + getRelation() + ")";
	}

}
