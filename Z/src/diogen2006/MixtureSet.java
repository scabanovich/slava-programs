package diogen2006;

public class MixtureSet {
	Mixture[] mixtures;
	
	public MixtureSet(int size) {
		mixtures = new Mixture[size];
	}
	
	public MixtureSet copy() {
		MixtureSet s = new MixtureSet(mixtures.length);
		for (int i = 0; i < mixtures.length; i++) {
			if(mixtures[i] != null) s.mixtures[i]= mixtures[i].copy();
		}
		return s;
	}
	
	public void move(int from, int to, int amount) {
		mixtures[from].moveTo(amount, mixtures[to]);
	}

	public void move(int from, int to, int amount, MixtureDelta delta) {
		delta.from = from;
		delta.to = to;
		delta.amountDelta = amount;
		delta.fromOldWater = mixtures[from].water;
		delta.toOldWater = mixtures[to].water;
		mixtures[from].moveTo(amount, mixtures[to]);
	}
	
	public void rollback(MixtureDelta delta) {
		mixtures[delta.from].water = delta.fromOldWater;
		mixtures[delta.to].water = delta.toOldWater;
		mixtures[delta.from].amount += delta.amountDelta;
		mixtures[delta.to].amount -= delta.amountDelta;		
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		for (int i = 0; i < mixtures.length; i++) {
			if(i > 0) sb.append(", ");
			sb.append(mixtures[i]);
		}
		sb.append('}');
		return sb.toString();
	}

}
