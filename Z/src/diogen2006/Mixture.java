package diogen2006;

public class Mixture {
	int amount;
	double water;
	
	public Mixture() {}
	
	public Mixture(int amount, double water) {
		this.amount = amount;
		this.water = water;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public double getWater() {
		return water;
	}
	
	public double getRatio() {
		if(amount == 0) return 0;
		if(water >= amount) return Double.MAX_VALUE;
		return water / (amount - water);
	}
	
	/**
	 * Returns amount of water deducted
	 * @param amount
	 * @return
	 */
	public double deduct(int amount) {
		if(amount > this.amount) {
			throw new RuntimeException("Cannot deduct " + amount + ".");
		}
		double dw = water * amount / this.amount;
		water -= dw;
		this.amount -= amount;
		return dw;
	}
	
	public void addWater(int amount) {
		this.amount += amount;
		water += amount;
	}
	
	public void addShit(int amount) {
		this.amount += amount;
	}
	
	public void moveTo(int amount, Mixture other) {
		double dw = deduct(amount);
		other.amount += amount;
		other.water += dw;
	}
	
	public Mixture copy() {
		Mixture copy = new Mixture();
		copy.amount = amount;
		copy.water = water;
		return copy;
	}
	
	public String toString() {
		return "amount=" + amount + " water=" + water;
	}

}
