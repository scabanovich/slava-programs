package slava.puzzles.hexa.fence.analysis;

import com.slava.common.RectangularField;

import slava.puzzles.hexa.common.HexaField;
import slava.puzzles.hexa.fence.model.HexaFenceConstants;

/**
 * Can be used to generate puzzles both for rectangular and hexagonal grids.
 * @author glory
 *
 */
public class HexaFenceGenerator {
	int directionsCount = 6;
	RectangularField field;
	int[] form;
	int[] data;
	int[] solution;
	HexaFenceContinuityCheck continuityCheck = new HexaFenceContinuityCheck(directionsCount);
	
	public HexaFenceGenerator() {}
	
	public void setField(RectangularField field) {
		this.field = field;
		continuityCheck.setField(field);
	}
	
	public void setField(RectangularField field, int directionsCount) {
		this.directionsCount = directionsCount;
		continuityCheck = new HexaFenceContinuityCheck(directionsCount);
		this.field = field;
		continuityCheck.setField(field);
	}
	
	public void setForm(int[] form) {
		this.form = form;
		continuityCheck.setForm(form);
	}
	
	public void generate() {
		data = new int[field.getSize()];
		for (int p = 0; p < data.length; p++) data[p] = HexaFenceConstants.NO_VALUE;
		solution = new int[field.getSize()];
		for (int p = 0; p < data.length; p++) solution[p] = 0;
		int p = getPlace();
		solution[p] = 1;
		for (int k = 0; k < 1000; k++) {
			while(!flip());
		}
		for (p = 0; p < data.length; p++) {
			if(form[p] == 0) continue;
			data[p] = 0;
			boolean bp = solution[p] == 1;
			for (int d = 0; d < directionsCount; d++) {
				int q = field.jump(p, d);
				boolean bq = (q >= 0 && form[q] == 1 && solution[q] == 1);
				if(bq != bp) data[p]++;
			}
		}
		reduce();
	}
	
	int getPlace() {
		while(true) {
			int p = (int)(data.length * Math.random());
			if(form[p] == 0) continue;
			return p;
		}
	}
	
	boolean flip() {
		int p = getPlace();
		solution[p] = 1 - solution[p];
		if(!continuityCheck.check(solution)) {
			solution[p] = 1 - solution[p];
			return false;
		}
		return true;
	}
	
	void reduce() {
		int[] order = new int[data.length];
		for (int p = 0; p < data.length; p++) order[p] = p;
		for (int p = 0; p < data.length; p++) {
			int q = (int)((data.length - p) * Math.random()) + p;
			int s = order[p];
			order[p] = order[q];
			order[q] = s;
		}
		for (int i = 0; i < data.length; i++) {
			int p = order[i];
			if(form[p] == 0) continue;
			int v = data[p];
			data[p] = -1;
			HexaFenceSolver solver = new HexaFenceSolver();
			solver.setDirectionCount(directionsCount);
			solver.setField(field);
			solver.setForm(form);
			solver.setData(data);
			solver.setSolutionLimit(2);
			solver.solve();
			System.out.println("tc=" + solver.treeCount);
			if(solver.getSolutionCount() == 1) {
				//ok
			} else if(solver.getSolutionCount() == 0) {
				System.out.println("Error");
			} else {
				data[p] = v;
			}
		}
		
	}
	
	public int[] getData() {
		return data;
	}
	
	public int[] getSolution() {
		return solution;
	}

	public void printResults() {
		for (int p = 0; p < field.getSize(); p++) {
			String s = "";
			if(form[p] == 0) {
				s = "+";
			} else if(data[p] >= 0) {
				s = "" + data[p];
			} else {
				s = "-";
			}
			System.out.print(" " + s);
			if(field.isRightBorder(p)) System.out.println("");
		}
		System.out.println("");
		for (int p = 0; p < field.getSize(); p++) {
			String s = "";
			if(form[p] == 0) {
				s = "+";
			} else if(solution[p] > 0) {
				s = "x";
			} else {
				s = "-";
			}
			System.out.print(" " + s);
			if(field.isRightBorder(p)) System.out.println("");
		}
		
	}

	public static void main(String[] args) {
		
		HexaFenceGenerator g = new HexaFenceGenerator();
		boolean rect = true;
		int[] form = null;
		if(rect) {
			RectangularField f = new RectangularField();
			f.setSize(5, 4);
			form = new int[f.getSize()];
			for (int i = 0; i < form.length; i++) form[i] = 1;
			g.setField(f, 4);
		} else {
			HexaField hf = new HexaField();
			hf.setSideLength(7);
			g.setField(hf, 6);
			form = new int[hf.getSize()];
			for (int p = 0; p < form.length; p++) {
				if(hf.isInField(p)) {
					form[p] = 1;
				} else {
					form[p] = 0;
				}
			}
		}
		g.setForm(form);
		g.generate();
		g.printResults();
	}

}

/**
 1 - - 3 - - - 2 2 1
 3 - - - - 2 - - - -
 - - 3 2 - 3 - 3 2 2
 1 0 - 2 - 0 2 - 1 -
 - - 3 - - - - - 1 -
 - - - - 1 - 1 - - 3
 2 - - 2 2 - 1 2 - -
 1 - - - 1 - 1 1 - -
 - 2 1 - - - - 2 3 -
 - 1 - 3 3 - - 1 3 -

 - - - x x - x x - -
 x - - - x x x x x x
 x x - x x - x - x x
 x x x x - - - - - -
 x x - - - - x x - -
 x x x - - x x - - x
 - x x - - x x - - x
 - x x x x x x x x x
 - - x x x - - x - -
 - - x - x - x x x -

*/