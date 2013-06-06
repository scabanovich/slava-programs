package smallpuzzles.tent;

import java.io.IOException;
import java.util.Random;

import com.slava.common.RectangularField;

public class TentGenerator {
	RectangularField field;
	int tentCount;
	
	int[] trees;
	int[] tents;
	
	int[] vData;
	int[] hData;
	
	TentSolver solver = new TentSolver();

	public TentGenerator() {}
	
	public void setField(RectangularField f) {
		field = f;
		solver.setField(f);
	}
	
	public void setTentCount(int c) {
		tentCount = c;
	}
	
	boolean generateRandom() {
		tents = new int[field.getSize()];
		trees = new int[tents.length];
		vData = new int[field.getWidth()];
		hData = new int[field.getHeight()];
		int[] restriction = new int[tents.length];
		int available = tents.length;
		Random r = new Random();
		for (int i = 0; i < tentCount; i++) {
			if(available == 0) return false;
			int p = r.nextInt(tents.length);
			while(restriction[p] > 0) {
				p = r.nextInt(tents.length);
			}
			tents[p] = 1;
			vData[field.getX(p)]++;
			hData[field.getY(p)]++;
			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					int q = field.jumpXY(p, dx, dy);
					if(q < 0 || restriction[q] > 0) continue;
					restriction[q] = 1;
					available--;
				}
			}
			int dc = 0;
			for (int d = 0; d < 4; d++) {
				int q = field.jump(p, d);
				if(q >= 0 && trees[q] == 0) dc++;
			}
			if(dc == 0) return false;
			int d = r.nextInt(4);
			while(true) {
				int q = field.jump(p, d);
				if(q < 0 || trees[q] > 0) {
					d = r.nextInt(4);
					continue;
				} else {
					trees[q] = 1;
					break;
				}
			}
		}
		//avoid zeros in data
		for (int x = 0; x < vData.length; x++) if(vData[x] == 0) return false;
		for (int y = 0; y < hData.length; y++) if(hData[y] == 0) return false;
		return true;
	}
	
	public void generate() {
		int attempt = 0;
		while(true) {
			attempt++;
			while(!generateRandom()) {
				attempt++;
			}
			solver.setData(trees, hData, vData);
			solver.solve();
			int sc = solver.getSolutionCount();
			if(sc == 1) break;
			if(sc == 0) {
				System.out.println("Error");
			}
			System.out.println(sc);
		}
		System.out.println("Attempts = " + attempt);
		print();
		System.out.println(" Tree count = " + solver.treeCount);
		
//		reduce();
	}
	
	void reduce() {
		int m = hData.length + vData.length;
		int[] order = new int[m];
		for (int i = 0; i < m; i++) order[i] = i;
		Random r = new Random();
		for (int i = 0; i < m; i++) {
			int j = i + r.nextInt(m - i);
			int c = order[i];
			order[i] = order[j];
			order[j] = c;
		}
		for (int i = 0; i < m; i++) {
			int j = order[i];
			if(j < hData.length) {
				int c = hData[j];
				hData[j] = -1;
				solver.setData(trees, hData, vData);
				solver.solve();
				if(solver.getSolutionCount() != 1) {
					hData[j] = c;
				}
			} else {
				j -= hData.length;
				int c = vData[j];
				vData[j] = -1;
				solver.setData(trees, hData, vData);
				solver.solve();
				if(solver.getSolutionCount() != 1) {
					vData[j] = c;
				}
			}
		}
		print();
	}
	
	void print() {
		for (int p = 0; p  < tents.length; p++) {
			String s = tents[p] == 1 ? "n" : trees[p] == 1 ? "T" : "+";
			System.out.print(" " + s);
			if(field.isRightBorder(p)) {
				int h = hData[field.getY(p)];
				String hs = h < 0 ? "-" : "" + h;
				System.out.print("  " + hs);
				System.out.println("");
			}
		}
		for (int x = 0; x < field.getWidth(); x++) {
			int v = vData[x];
			String vs = v < 0 ? "-" : "" + v;
			System.out.print(" " + vs);
		}
		System.out.println("");
	}

	public void generateMany() {
		generate();
		String command = "";
		System.out.println("Enter command (h for help):");
		while(true) {
			while((command = readCommand()) == null || command.trim().length() == 0) {
				// do nothing
			}
			if("h".equals(command)) {
				String help = "  g : generate one puzzle\n" 
				+ "  g=10 : generate 10 puzzles\n"
				+ "  t=20 : set number of tents to 20\n" 
				+ "  w=10 : set width to 10\n" 
				+ "  h=10 : set height to 10\n"
				+ "  e : exit\n";
				System.out.println(help);
			} else if("e".equals(command)) {
				break;
			} else if(command.startsWith("w=")) {
				try {
					int w = Integer.parseInt(command.substring(2).trim());
					if(w > 0 && w != field.getWidth()) {
						field.setSize(w, field.getHeight());
					}
				} catch (NumberFormatException e) {
					System.out.println("Wrong number");
				}
			} else if(command.startsWith("h=")) {
				try {
					int h = Integer.parseInt(command.substring(2).trim());
					if(h > 0 && h != field.getHeight()) {
						field.setSize(field.getWidth(), h);
					}
				} catch (NumberFormatException e) {
					System.out.println("Wrong number");
				}
			} else if(command.startsWith("t=")) {
				try {
					int c = Integer.parseInt(command.substring(2).trim());
					if(c > 0 && c != tentCount) {
						tentCount = c;
					}
				} catch (NumberFormatException e) {
					System.out.println("Wrong number");
				}
			} else if("g".equals(command)) {
				generate();
			} else if(command.startsWith("g=")) {
				try {
					int n = Integer.parseInt(command.substring(2).trim());
					for (int i = 0; i < n; i++) generate();
				} catch (NumberFormatException e) {
					System.out.println("Wrong number");
				}
			}
			System.out.print("-->");
		}
	}
	
	String readCommand() {
		StringBuffer sb = new StringBuffer();
		while(true) {
			try {
				int c = System.in.read();
				if(c == 13 || c == 10) break;
				sb.append((char)c);
			} catch (IOException e) {
			}
		}
		return sb.toString().trim();
	}

	public static void main(String[] args) {
		TentGenerator g = new TentGenerator();
		RectangularField f = new RectangularField();
		f.setSize(10, 10);
		g.setField(f);
		g.setTentCount(20);
		g.generateMany();
	}

}
