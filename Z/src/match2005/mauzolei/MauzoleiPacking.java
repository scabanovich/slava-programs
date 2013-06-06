package match2005.mauzolei;

import java.io.*;

public class MauzoleiPacking {
	MauzoleiField field;
	MauzoleiPlacement[] placements;
	int figureCount = 24;

	int[] state;
	int[] figureUsage;
	
	int t;
	int[] wayCount;
	int[] way;
	int[][] ways;
	
	int[] distribution;
	
	int solutionCount;
	
	public MauzoleiPacking() {}
	
	public void setField(MauzoleiField field) {
		this.field = field;
	}
	
	public void setPlacements(MauzoleiPlacement[] placements) {
		this.placements = placements;
	}
	
	public void solve() {
		init();
		anal();
	}
	
	void init() {
		state = new int[field.size];
		for (int i = 0; i < state.length; i++) {
			state[i] = field.filter[i] == 0 ? 25 : -1;
		}
		figureUsage = new int[figureCount];
		distribution = new int[field.size];
		wayCount = new int[figureCount + 1];
		way = new int[figureCount + 1];
		ways = new int[figureCount + 1][200];
		t = 0;
		solutionCount = 0;
	}
	
	void srch() {
		wayCount[t] = 0;
		if(t == figureCount) return;
		makeDistribution();
		int p = getNarrowPlace();
		if(p < 0 || distribution[p] == 0) return;
		for (int i = 0; i < placements.length; i++) {
			if(!canPut(placements[i])) continue;
			if(!placements[i].contains(p)) continue;
			ways[t][wayCount[t]] = i;
			wayCount[t]++;			
		}
		if(t < 9) {
//			System.out.println("ways=" + wayCount[t]);
			randomize();
		}
	}
	
	void randomize() {
		if(wayCount[t] < 2) return;
		for (int i = wayCount[t] - 1; i >= 1; i--) {
			int k = (int)((i + 1) * Math.random());
			if(k == i) continue;
			int c = ways[t][i];
			ways[t][i] = ways[t][k];
			ways[t][k] = c;
		}
		if(wayCount[t] > 4) wayCount[t] = 4;
	}
	
	
	void makeDistribution() {
		for (int i = 0; i < field.size; i++) distribution[i] = 0;
		for (int i = 0; i < placements.length; i++) {
			if(!canPut(placements[i])) continue;
			int[] points = placements[i].points;
			for (int j = 0; j < points.length; j++) distribution[points[j]]++;
		}
	}
	int getNarrowPlace() {
		int pn = -1;
		int q = 10000;
		for (int p = 0; p < field.size; p++) {
			if(state[p] >= 0) continue;
			if(distribution[p] < q) {
				q = distribution[p];
				pn = p;
				if(q == 0) return p;
			}
		}
		return pn;
	}
	
	boolean canPut(MauzoleiPlacement placement) {
		if(figureUsage[placement.index] >= 2) return false;
		int[] points = placement.points;
		for (int i = 0; i < points.length; i++) {
			if(state[points[i]] >= 0) return false;
		}
		return true;
	}
	
	void move() {
		int index = ways[t][way[t]];
		MauzoleiPlacement placement = placements[index];
		figureUsage[placement.index]++;
		int[] points = placement.points;
		for (int i = 0; i < points.length; i++) {
			state[points[i]] = placement.index;
		}
		++t;
		srch();
		way[t] = -1;
	}
	
	void back() {
		--t;
		int index = ways[t][way[t]];
		MauzoleiPlacement placement = placements[index];
		figureUsage[placement.index]--;
		int[] points = placement.points;
		for (int i = 0; i < points.length; i++) {
			state[points[i]] = -1;
		}
	}
	
	void anal() {
		srch();
		way[t] = -1;
		int count = 0;
		while(true) {
			while(way[t] == wayCount[t] - 1) {
				if(t == 0) return;
				back();
			}
			way[t]++;
			move();
			if(t == figureCount) {
				++solutionCount;
				solutionFound();
			}
			if(wayCount[t] == 0) {
				count++;
				if(count == 10000) {
					while(t > 10) back();
					count = 0;
				}
			}
		}
	}
	
	void solutionFound() {
		printSolution();
	}
	
	void printSolution() {
		System.out.println("Solution " + solutionCount);
		String solution = getSolutionString();
		if(solutionCount == 1) System.out.print(solution);
		saveSolutionToFile(solution);
	}
	
	String getSolutionString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < field.size; i++) {
			char c = state[i] < 0 ? '+' : (char)(97 + state[i]);
			sb.append(" ").append(c);
			if(field.x[i] == field.xSize - 1) {
				sb.append("\n");
				if(field.y[i] == field.ySize - 1) {
					sb.append("\n");
				}
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
	void saveSolutionToFile(String solution) {
		File f = new File("./packings.txt");
		if(solutionCount == 1) System.out.println("File = " + f.getAbsolutePath());
		StringBuffer sb = new StringBuffer();
		try {
			if(!f.exists()) f.createNewFile();
			FileReader r = new FileReader(f);
			BufferedReader br = new BufferedReader(r);
			String s = null;
			while((s = br.readLine()) != null) {
				sb.append(s).append("\n");
			}
			r.close();
			sb.append(solution);
			BufferedWriter w = new BufferedWriter(new FileWriter(f));
			w.write(sb.toString());
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
