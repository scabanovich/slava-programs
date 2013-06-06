package forsmarts.csudoku;

import java.util.*;
import com.slava.common.RectangularField;

public class Piece {
	int index;
	int[] values; //-1 means not set; 0-8 - set values
	int[][] points; // array of(x,y)
	int[][] placements;
	RectangularField field;
	
	public Piece(int index, int[] values, int[][] points) {
		this.index = index;
		this.values = values;
		this.points = points;		
	}
	
	public void computePlacements(RectangularField field) {
		this.field = field;
		ArrayList l = createPlacements();
		placements = (int[][])l.toArray(new int[0][]);
	}
	
	private ArrayList createPlacements() {
		ArrayList list = new ArrayList();
		Set codes = new HashSet();
		int[] initial = toPoints(points);
		list.add(initial);
		codes.add(getCode(initial));
		int c = 0;
		while(c < list.size()) {
			int[] pc = (int[])list.get(c);
			int[] pc1 = rotate(pc);
			add(index, pc1, list, codes);
//			if(reflectionAllowed) {
//				pc1 = reflect(pc);
//				add(index, pc1, list, codes);
//			}
			for (int d = 0; d < 4; d++) {
				pc1 = shift(pc, d);
				add(index, pc1, list, codes);
			}
			++c;
		}
		return list;
	}

	private void add(int index, int[] pc, ArrayList list, Set codes) {
		if(pc == null) return;
		String code = getCode(pc);
		if(codes.contains(code)) return;
		codes.add(code);
		list.add(pc);
	}

	private static String getCode(int[] placement) {
		int[] p = (int[])placement.clone();
//      do not sort! values break symmetry!
//		Arrays.sort(p);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < p.length; i++) sb.append(p[i]).append(',');
		return sb.toString();
	}

	private int[] rotate(int[] points) {
		int[] ps = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ps[i] = field.getRotation()[points[i]];
			if(ps[i] < 0) return null;
		}
		return ps;
	}

	private int[] shift(int[] points, int d) {
		int[] ps = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ps[i] = field.jump(points[i], d);
			if(ps[i] < 0) return null;
		}
		return ps;
	}	

	private int[] toPoints(int[][] figure) {
		int[] points = new int[figure.length];
		for (int i = 0; i < points.length; i++) {
			points[i] = field.getIndex(figure[i][0], figure[i][1]);
		}
		return points;
	}

}
