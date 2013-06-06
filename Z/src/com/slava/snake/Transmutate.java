package com.slava.snake;

public class Transmutate {
	static int[] factorials;
	
	static {
		factorials = new int[12];
		factorials[0] = 1;
		for (int i = 1; i < factorials.length; i++) {
			factorials[i] = i * factorials[i - 1];
		}
	}

	public static int getIndex(int[] permutation) {
		int[] u = new int[permutation.length];
		int p = 0;
		for (int i = 0; i < permutation.length; i++) {
			int k = permutation[i];
			int s = 0;
			for (int j = 0; j < k; j++) if(u[j] == 0) ++s;
			p += factorials[permutation.length - 1 - i] * s;
			u[k] = 1;
			
		}
		return p;
	}
	
	public static int[] getPermutation(int index, int size) {
		int[] permutation = new int[size];
		int[] u = new int[size];
		for (int i = 0; i < size - 1; i++) {
			int f = factorials[size - 1 - i];
			int k = 0;
			while(u[k] == 1) k++;
			while(index >= f) {
				index -= f;
				++k;
				while(u[k] == 1) k++;
			}
			permutation[i] = k;
			u[k] = 1;
		}		
		int k = 0;
		while(u[k] == 1) k++;
		permutation[size - 1] = k;
		return permutation;
	}
	
	int size = 8;
	
	int[] paths;
	
	public void init(int size) {
		this.size = size;
		paths = new int[factorials[size]];
		for (int i = 0; i < paths.length; i++) paths[i] = -1;
		paths[0] = 0;
	}
	
	public int process(int t) {
		int changes = 0;
		for (int i = 0; i < paths.length; i++) {
			if(paths[i] != t) continue;
			int[] pm = getPermutation(i, size);
			for (int p1 = 0; p1 < size; p1++) {
				for (int dp = 1; dp < 3 && dp + p1 < size; dp++) {
					int p2 = p1 + dp;
					if(Math.abs(pm[p1] - pm[p2]) > 2) continue;
//				if(p2 == 2 && p1 == 0) continue;
//				if((pm[p1] == 0 && pm[p2] == 2) || (pm[p1] == 2 && pm[p2] == 0)) continue;
					flip(pm, p1, p2);
					
					int j = getIndex(pm);
					if(paths[j] < 0) {
						paths[j] = t + 1;
						++changes;
					}

					flip(pm, p1, p2);
				}
			}
		}
		return changes;
	}
	
	public static void flip(int[] pm, int p1, int p2) {
		int c = pm[p1];
		pm[p1] = pm[p2];
		pm[p2] = c;
	}
	
	public static int reverse(int index, int size) {
		int[] pm = getPermutation(index, size);
		int ii = size / 2;
		for (int i = 0; i < ii; i++) {
			int k = pm[i];
			pm[i] = pm[size - 1 - i];
			pm[size - 1 - i] = k;
		}
		return getIndex(pm);		
	}
	
	int totalChanges = 0;
	int maxT = 0;
	
	public int getTimeForReverse() {
		int[] p = new int[size];
		for (int i = 0; i < size; i++) p[i] = size - 1 - i;
		return paths[getIndex(p)];
	}
	
	public int getTimeForZeroToEnd() {
		int[] p = new int[size];
		for (int i = 0; i < size; i++) p[i] = i + 1;
		p[size - 1] = 0;
		return paths[getIndex(p)];
	}
	
	public void process() {
		totalChanges = 1;
		maxT = 0;
		while(true) {
			int changes = process(maxT);
			++maxT;
			totalChanges += changes;
			if(changes == 0) break;
		}
	}
	
	static void test() {
		for (int i = 0; i < factorials[8]; i++) {
			int[] pm = getPermutation(i, 8);
			int j = getIndex(pm);
			if(i != j) {
				System.out.println("Error=" + i + ":" + j);
				for (int m = 0; m < pm.length; m++) System.out.print(" " + pm[m]);
				System.out.println();
			}
		}
//		int[] pm0 = new int[]{0,2,1};
//		int p = Transmutate.getIndex(pm0);
//		System.out.println(p);
//		int[] pm = Transmutate.getPermutation(p, pm0.length);
//		for (int i = 0; i < pm.length; i++) System.out.print(" " + pm[i]);
//		System.out.println("");
	}
	
	public static void run() {
		for (int size = 1; size < 12; size++) {
			Transmutate t = new Transmutate();
			t.init(size);
			t.process();
//			System.out.println("totalChanges=" + t.totalChanges);
//			System.out.println("maxT=" + t.maxT);
//			System.out.println("Size = " + size + " Reverse time=" + t.getTimeForReverse());
			System.out.println("Size = " + size + " Reverse time=" + t.getTimeForZeroToEnd());
		}
	}
	
	public static void main(String[] args) {
		Transmutate.run();
	}

}
