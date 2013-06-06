package olia;

public class MariaStuartPatience {
	CardPack pack;
	
	public void setPack(CardPack pack) {
		this.pack = pack;
	}
	
	public int[] getRandomPack(int size) {
		int[] s = new int[size];
		for (int i = 0; i < s.length; i++) s[i] = i;
		for (int i = s.length - 1; i >= 1; i--) {
			int j = (int)((i + 1) * Math.random());
			if(j == i) continue;
			int c = s[i];
			s[i] = s[j];
			s[j] = c;
		}
		return s;
	}
	
	int[] getRandomPack2(int size) {
		while(true) {
			int[] cs = getRandomPack(size);
			if(pack.areOfEqualValueOrSuit(cs[size - 3], cs[size - 1])) return cs;
		}
	}
	
	public int applyPatienceRules(int[] cs) {
		int c = 0;
		while(c + 2 < cs.length) {
			if(pack.areOfEqualValueOrSuit(cs[c], cs[c + 2])) {
				int[] u = new int[cs.length - 1];
				System.arraycopy(cs, 0, u, 0, c);
				System.arraycopy(cs, c + 1, u, c, u.length - c);
				cs = u;
				c -= 2;
				if(c < 0) c = 0;
			} else {
				c++;
			}
		}		
		return cs.length;
	}
	
	public void buildStatistics() {
		int[] statistics = new int[pack.size];
		for (int k = 0; k < 1000000; k++) {
			int[] cs = getRandomPack(pack.size);
			int leftover = applyPatienceRules(cs);
			if(leftover < statistics.length) statistics[leftover]++;
		}		
		for (int i = 0; i < statistics.length; i++) {
			if(statistics[i] == 0) continue;
			System.out.println("i=" + i + " r=" + statistics[i]);
		}
	}

	public static void main(String[] args) {
		CardPack pack = new CardPack();
		pack.init(9, 4);
		MariaStuartPatience p = new MariaStuartPatience();
		p.setPack(pack);
		p.buildStatistics();
	}

}
