package champukr2013;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.slava.common.RectangularField;

public class DoublePentaminoFigures {
	int[][][] figures = {
		{{0,0}, {1,0}, {2,0}, {0,1}, {2,1}}, //U
		{{0,0}, {1,0}, {2,0}, {3,0}, {4,0}}, //I
		{{0,0}, {1,0}, {2,0}, {3,0}, {0,1}}, //L 			
		{{0,0}, {1,0}, {2,0}, {3,0}, {1,1}}, //Y 			
		{{0,0}, {1,0}, {2,0}, {0,1}, {-1,1}}, //N 			
		{{0,0}, {1,0}, {2,0}, {1,1}, {1,2}}, //T
		{{0,0}, {1,0}, {2,0}, {0,1}, {0,2}}, //V 			
		{{0,0}, {1,0}, {1,1}, {2,1}, {2,2}}, //W 			
		{{0,0}, {-1,1}, {0,1}, {1,1}, {0,2}}, //X 			
		{{0,0}, {1,0}, {2,0}, {0,1}, {1,1}}, //P
		{{0,0}, {1,0}, {0,1}, {0,2}, {-1,2}}, //Z
		{{0,0}, {1,0}, {1,1}, {2,1}, {1,2}}, //F 			
	};

	RectangularField f = new RectangularField();
	Variant[][] variants;

	class Variant {
		int index;
		int[][] nodes;

		Variant(int index, int[][] nodes) {
			this.index = index;
			this.nodes = nodes;
			reduce();
		}

		void reduce() {
			for (int i = 0; i < nodes.length; i++) {
				for (int j = i + 1; j < nodes.length; j++) {
					if(nodes[j][1] < nodes[i][1] || (nodes[j][1] == nodes[i][1] && nodes[j][0] < nodes[i][0])) {
						int c = nodes[i][0]; nodes[i][0] = nodes[j][0]; nodes[j][0] = c;
						    c = nodes[i][1]; nodes[i][1] = nodes[j][1]; nodes[j][1] = c;
					}
				}
			}
			int dx = nodes[0][0], dy = nodes[0][1];
			for (int i = 0; i < nodes.length; i++) {
				nodes[i][0] -= dx;
				nodes[i][1] -= dy;
			}
		}

		String getCode() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < nodes.length; i++) {
				sb.append(nodes[i][0]).append(nodes[i][1]).append(';');
			}
			return sb.toString();
		}
	}

	public DoublePentaminoFigures() {		
		f.setSize(8, 8);
	}

	public void run() {
		variants = new Variant[figures.length][];
		for (int i = 0; i < figures.length; i++) {
			run(i);
		}
	}

	public void run(int index) {
		List<Variant> vs = new ArrayList<Variant>();
		Set<String> codes = new TreeSet<String>();
		
		int[] state = new int[f.getSize()];
		for (int i = 0; i < figures[index].length; i++) {
			int dx = 2 + figures[index][i][0], dy = 2 + figures[index][i][1];
			state[f.getIndex(dx, dy)] = 1;
			state[f.getIndex(dx + 1, dy)] = 1;
			state[f.getIndex(dx, dy + 1)] = 1;
			state[f.getIndex(dx + 1, dy + 1)] = 1;
		}
		if(index == 0) {
			//U
			state[f.getIndex(3, 4)] = 0;
		}
		
		int s = 0;
		for (int ix = 0; ix < f.getWidth(); ix++) {
			for (int iy = 0; iy < f.getHeight(); iy++) {
				int p = f.getIndex(ix, iy);
				if(state[p] == 1) s++;
			}
		}
		int[][] nodes = new int[s][2];
		s = 0;
		for (int iy = 0; iy < f.getHeight(); iy++) {
			for (int ix = 0; ix < f.getWidth(); ix++) {
				int p = f.getIndex(ix, iy);
				if(state[p] == 1) {
					nodes[s][0] = ix; nodes[s][1] = iy; s++;
				}
			}
		}
		Variant v = new Variant(index, nodes);
		String code = v.getCode();
		codes.add(code);
		vs.add(v);
		
		nodes = new int[s][2];
		s = 0;
		for (int ix = f.getWidth() - 1; ix >= 0; ix--) {
			for (int iy = 0; iy < f.getHeight(); iy++) {
				int p = f.getIndex(ix, iy);
				if(state[p] == 1) {
					nodes[s][0] = iy; nodes[s][1] = f.getWidth() - 1 - ix; s++;
				}
			}
		}
		v = new Variant(index, nodes);
		code = v.getCode();
		if(!codes.contains(code)) {
			codes.add(code);
			vs.add(v);
		}
		
		nodes = new int[s][2];
		s = 0;
		for (int iy = 0; iy < f.getHeight(); iy++) {
			for (int ix = 0; ix < f.getWidth(); ix++) {
				int p = f.getIndex(ix, iy);
				if(state[p] == 1) {
					nodes[s][0] = f.getWidth() - 1 - ix; nodes[s][1] = f.getHeight() - 1 - iy; s++;
				}
			}
		}
		v = new Variant(index, nodes);
		code = v.getCode();
		if(!codes.contains(code)) {
			codes.add(code);
			vs.add(v);
		}
		
		nodes = new int[s][2];
		s = 0;
		for (int iy = 0; iy < f.getHeight(); iy++) {
			for (int ix = 0; ix < f.getWidth(); ix++) {
				int p = f.getIndex(ix, iy);
				if(state[p] == 1) {
					nodes[s][0] = f.getHeight() - 1 - iy; nodes[s][1] = ix; s++;
				}
			}
		}
		v = new Variant(index, nodes);
		code = v.getCode();
		if(!codes.contains(code)) {
			codes.add(code);
			vs.add(v);
		}		
		
		variants[index] = vs.toArray(new Variant[0]);
	}

	public static void main(String[] args) {
//		new DoublePentaminoFigures().run();
		DoublePentaminoPacking p = new DoublePentaminoPacking();
		p.solve();
		System.out.println("Tree=" + p.treeCount);
		System.out.println("Solutions=" + p.solutionCount);
	}
}
