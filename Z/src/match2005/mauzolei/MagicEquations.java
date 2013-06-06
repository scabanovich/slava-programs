package match2005.mauzolei;

import java.util.ArrayList;

public class MagicEquations {
	MauzoleiField field;
	int[] form;
	int[] layerSums;
	
	int[] sequence;
	Equation[][] positionToEquations;

	public void setField(MauzoleiField field) {
		this.field = field;
	}
	
	public void setForm(int[] form) {
		this.form = form;
	}
	
	public void setLayerSums(int[] sums) {
		layerSums = sums;
	}

	public void build() {
		buildSequence();
		buildEquations();
	}

	private void buildSequence() {
		int c = 0;
		for (int i = 0; i < field.size; i++) if(form[i] >= 0) ++c;
		System.out.println(c);
		sequence = new int[c];
		c = 0;
		for (int z = 0; z < field.zSize; z++) {
			int x = 0;
			int y = 0;
			while(true) {
				int p = field.xyz[x][y][z];
				if(form[p] >= 0) {
					sequence[c] = p;
//					System.out.println(c + ":" + p);
					++c;
				}
				if(x >= y) {
					++x;
					if(x == field.xSize) {
						x = y;
						++y;
					}
				} else {
					++y;
					if(y == field.ySize) {
						y = x + 1;
						x = y;
					}
				}
				if(y == field.ySize) break;
			}
		}
	}
	
	private void buildEquations() {
		ArrayList eqs = new ArrayList();
		for (int z = 0; z < field.zSize; z++) {
			//vertical
			for (int x = 0; x < field.xSize; x++) {
				int c = 0;
				for (int y = 0; y < field.ySize; y++) {
					int p = field.xyz[x][y][z];
					if(form[p] >= 0) ++c;
				}
				if(c == 0) continue;
				int[] positions = new int[c];
				c = 0;
				for (int y = 0; y < field.ySize; y++) {
					int p = field.xyz[x][y][z];
					if(form[p] >= 0) {
						positions[c] = p;
						++c;
					}
				}
				eqs.add(new Equation(positions, layerSums[z]));
			}
			//horizontal
			for (int y = 0; y < field.ySize; y++) {
				int c = 0;
				for (int x = 0; x < field.xSize; x++) {
					int p = field.xyz[x][y][z];
					if(form[p] >= 0) ++c;
				}
				if(c == 0) continue;
				int[] positions = new int[c];
				c = 0;
				for (int x = 0; x < field.xSize; x++) {
					int p = field.xyz[x][y][z];
					if(form[p] >= 0) {
						positions[c] = p;
						++c;
					}
				}
				eqs.add(new Equation(positions, layerSums[z]));
			}
			//diagonal 1
			int c = 0;
			for (int x = 0; x < field.xSize; x++) {
				int p = field.xyz[x][x][z];
				if(form[p] >= 0) ++c;
			}
			int[] positions = new int[c];
			c = 0;
			for (int x = 0; x < field.xSize; x++) {
				int p = field.xyz[x][x][z];
				if(form[p] >= 0) {
					positions[c] = p;
					++c;
				}
			}
			if(c != 0) eqs.add(new Equation(positions, layerSums[z]));
			//diagonal 2
			c = 0;
			for (int x = 0; x < field.xSize; x++) {
				int p = field.xyz[x][field.xSize - 1 - x][z];
				if(form[p] >= 0) ++c;
			}
			positions = new int[c];
			c = 0;
			for (int x = 0; x < field.xSize; x++) {
				int p = field.xyz[x][field.xSize - 1 - x][z];
				if(form[p] >= 0) {
					positions[c] = p;
					++c;
				}
			}
			if(c != 0) eqs.add(new Equation(positions, layerSums[z]));
		}
		//x < 4 y < 4 equation-135
		int c = 0;
		for (int p = 0; p < field.size; p++) {
			if(form[p] < 0 || field.x[p] >= 4 || field.y[p] >= 4) continue;
			++c;
		}
		int[] positions = new int[c];
		c = 0;
		for (int p = 0; p < field.size; p++) {
			if(form[p] < 0 || field.x[p] >= 4 || field.y[p] >= 4) continue;
			positions[c] = p;
			++c;
		}
		if(c != 0) eqs.add(new Equation(positions, 135));

		//x >= 4 y < 5 equation-135
		c = 0;
		for (int p = 0; p < field.size; p++) {
			if(form[p] < 0 || field.x[p] < 4 || field.y[p] >= 4) continue;
			++c;
		}
		positions = new int[c];
		c = 0;
		for (int p = 0; p < field.size; p++) {
			if(form[p] < 0 || field.x[p] < 4 || field.y[p] >= 4) continue;
			positions[c] = p;
			++c;
		}
		if(c != 0) eqs.add(new Equation(positions, 135));

		//x < 4 y >= 4 equation-135
		c = 0;
		for (int p = 0; p < field.size; p++) {
			if(form[p] < 0 || field.x[p] >= 4 || field.y[p] < 4) continue;
			++c;
		}
		positions = new int[c];
		c = 0;
		for (int p = 0; p < field.size; p++) {
			if(form[p] < 0 || field.x[p] >= 4 || field.y[p] < 4) continue;
			positions[c] = p;
			++c;
		}
		if(c != 0) eqs.add(new Equation(positions, 135));

		Equation[] equations = (Equation[])eqs.toArray(new Equation[0]);
//		System.out.println("Equations=" + equations.length);
		buildPositionToEquations(equations);
	}
	
	private void buildPositionToEquations(Equation[] equations) {
		positionToEquations = new Equation[field.size][];
		for (int i = 0; i < field.size; i++) {
			if(form[i] < 0) {
				positionToEquations[i] = new Equation[0];
				continue;
			}
			ArrayList l = new ArrayList();
			for (int j = 0; j < equations.length; j++) {
				if(equations[j].contains(i)) l.add(equations[j]);
			}
			positionToEquations[i] = (Equation[])l.toArray(new Equation[0]);
		}
	}

}

class Equation {
	int[] positions;
	int sum;
	int freePositionCount;
	
	public Equation(int[] positions, int sum) {
		this.sum = sum;
		this.positions = positions;
		freePositionCount = positions.length;
	}
	
	public void add(int number) {
		sum -= number;
		freePositionCount--;
	}
	
	public void remove(int number) {
		sum += number;
		freePositionCount++;
	}
	
	boolean isCorrect() {
		if(freePositionCount == 0) return sum == 0;
		return sum >= 0 && sum <= 9 * freePositionCount;
	}
	
	boolean contains(int position) {
		for (int i = 0; i < positions.length; i++) {
			if(position == positions[i]) return true;
		}
		return false;
	}
	
}