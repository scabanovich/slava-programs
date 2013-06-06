package fitting2d;

import java.util.*;
import packing3d.Placement;

public class Figures2D {
	boolean reflectionAllowed = false;
	int[][][] figures;
	RectangularField field;
	Placement[][] placements;
	
	public void setReflectionAllowed(boolean b) {
		reflectionAllowed = b;
	}
	
	public void createPlacements(int[][][] figures, RectangularField field) {
		this.field = field;
		this.figures = figures;
		placements = new Placement[figures.length][];
		for (int i = 0; i < figures.length; i++) {
			ArrayList l = createPlacements(i);
			System.out.println(l.size());
			placements[i] = (Placement[])l.toArray(new Placement[0]);
		}
	}
	
	private ArrayList createPlacements(int index) {
		ArrayList list = new ArrayList();
		Set codes = new HashSet();
		Placement initial = new Placement(index, toPoints(figures[index]));
		list.add(initial);
		codes.add(initial.getCode());
		int c = 0;
		while(c < list.size()) {
			Placement placement = (Placement)list.get(c);
			int[] pc = placement.getPoints();
			int[] pc1 = rotate(pc);
			add(index, pc1, list, codes);
			if(reflectionAllowed) {
				pc1 = reflect(pc);
				add(index, pc1, list, codes);
			}
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
		Placement pn = new Placement(index, pc);
		String code = pn.getCode();
		if(codes.contains(code)) return;
		codes.add(code);
		list.add(pn);
	}
	
	private int[] toPoints(int[][] figure) {
		int[] points = new int[figure.length];
		for (int i = 0; i < points.length; i++) {
			points[i] = field.xy[figure[i][0]][figure[i][1]];
		}
		return points;
	}
	
	private int[] rotate(int[] points) {
		int[] ps = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ps[i] = field.rotation[points[i]];
			if(ps[i] < 0) return null;
		}
		Arrays.sort(ps);
		return ps;
	}
	
	private int[] shift(int[] points, int d) {
		int[] ps = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ps[i] = field.jump(points[i], d);
			if(ps[i] < 0) return null;
		}
		Arrays.sort(ps);
		return ps;
	}	

	private int[] reflect(int[] points) {
		int[] ps = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ps[i] = field.reflection[points[i]];
			if(ps[i] < 0) return null;
		}
		Arrays.sort(ps);
		return ps;
	}
	
}
