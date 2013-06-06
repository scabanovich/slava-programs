package match2011;

public class Equation {
	int[] positions;
	int sum;

	Equation child;
	int lastExcluded = -1;

	public Equation(int[] positions, int sum) {
		this.positions = positions;
		this.sum = sum;
	}
	
	public int[] getPositions() {
		return positions;
	}

	public int getPositionAt(int i) {
		return positions[i];
	}

	public int getLength() {
		return positions.length;
	}

	public int getSum() {
		return sum;
	}

	public void restore(int position) {
		if(position == lastExcluded) {
			positions = child.positions;
			sum = child.sum;
			lastExcluded = child.lastExcluded;
			child = child.child;
		}
	}

	public void exclude(int position, int value) {
		int index = getIndex(position);
		if(index >= 0) {
			int[] ps = new int[positions.length - 1];
			if(index > 0) System.arraycopy(positions, 0, ps, 0, index);
			if(ps.length > index) System.arraycopy(positions, index + 1, ps, index, ps.length - index);

			Equation newchild = new Equation(positions, sum);
			newchild.child = child;
			newchild.lastExcluded = lastExcluded;

			positions = ps;
			sum = sum - value;
			child = newchild;
			lastExcluded = position;
		}
	}

	int getIndex(int position) {
		for (int i = 0; i < positions.length; i++) {
			if(positions[i] == position) {
				return i;
			}
		}
		return -1;
	}
}
