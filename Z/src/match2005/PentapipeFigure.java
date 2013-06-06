package match2005;

public class PentapipeFigure {
	int index;
	char letter;
	int[][] points;
	int[][] shades;
	
	public PentapipeFigure(int index, char letter, int[] form, PentapipeField field) {
		this.index = index;
		this.letter = letter;
		int pc = 0;
		int sc = 0;
		for (int i = 0; i < field.getSize(); i++) {
			if(form[i] == 1) ++pc;
			else if(form[i] == 2) ++sc;
		}
		points = new int[pc][2];
		shades = new int[sc][2];
		pc = 0;
		sc = 0;
		for (int i = 0; i < field.getSize(); i++) {
			if(form[i] == 1) {
				points[pc][0] = field.getX(i);
				points[pc][1] = field.getY(i);
				++pc;
			} else if(form[i] == 2) {
				shades[sc][0] = field.getX(i);
				shades[sc][1] = field.getY(i);
				++sc;
			}
		}
		normalize();
	}
	
	void normalize() {
		int y0 = 20;
		for (int i = 0; i < points.length; i++) if(points[i][1] < y0) y0 = points[i][1];
		for (int i = 0; i < points.length; i++) points[i][1] -= y0;
		for (int i = 0; i < shades.length; i++) shades[i][1] -= y0;
		int x0 = 20;
		for (int i = 0; i < points.length; i++) {
			if(points[i][1] == 0 && points[i][0] < x0) x0 = points[i][0];
		}
		for (int i = 0; i < points.length; i++) points[i][0] -= x0;
		for (int i = 0; i < shades.length; i++) shades[i][0] -= x0;
	}
	
	public boolean equals(PentapipeFigure f) {
		if(points.length != f.points.length) return false;
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < 2; j++) {
				if(points[i][j] != f.points[i][j]) return false;
			}
		}
//		if(shades.length != f.shades.length) return false;
//		for (int i = 0; i < shades.length; i++) {
//			for (int j = 0; j < 2; j++) {
//				if(shades[i][j] != f.shades[i][j]) return false;
//			}
//		}
		return true;
	}
	
	public int[][] getPoints() {
		return points;
	}
	
	public int[][] getShades() {
		return shades;
	}

}
