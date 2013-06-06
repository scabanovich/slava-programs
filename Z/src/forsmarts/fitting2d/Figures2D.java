package forsmarts.fitting2d;

import java.util.*;

import com.slava.common.RectangularField;

public class Figures2D {
	boolean reflectionAllowed = false;
	int[][][] figures;
	RectangularField field;
	Placement[][] placements;
	int[] fieldRestriction;
	
	public void setReflectionAllowed(boolean b) {
		reflectionAllowed = b;
	}
	
	public void setFieldRestriction(int[] rs) {
		fieldRestriction = rs;
	}
	
	public void createPlacements(int[][][] figures, RectangularField field) {
		this.field = field;
		this.figures = figures;
		placements = new Placement[figures.length][];
		for (int i = 0; i < figures.length; i++) {
			ArrayList l = createPlacements(i);
			Iterator it = l.iterator();
			while(it.hasNext()) {
				Placement p = (Placement)it.next();
				if(!accept(p.getPoints())) it.remove();
			}
//			System.out.println(l.size());
			placements[i] = (Placement[])l.toArray(new Placement[0]);
		}
	}
	
	public Placement[][] getPlacements() {
		return placements;
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
	
	boolean accept(int[] points) {
		int out = 0;
		for (int i = 0; i < points.length; i++) {
			if(fieldRestriction != null && fieldRestriction[points[i]] > 0) ++out;
		}
		return out == 0;
	}
	
	private int[] toPoints(int[][] figure) {
		int[] points = new int[figure.length];
		for (int i = 0; i < points.length; i++) {
			points[i] = field.getIndex(figure[i][0], figure[i][1]);
		}
		return points;
	}
	
	private int[] rotate(int[] points) {
		int[] ps = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ps[i] = field.getRotation()[points[i]];
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
			ps[i] = field.getReflection()[points[i]];
			if(ps[i] < 0) return null;
		}
		Arrays.sort(ps);
		return ps;
	}
	
}
