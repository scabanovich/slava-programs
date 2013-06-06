package ic2006_3;

import java.util.*;

import com.slava.common.RectangularField;

public class PentaPathFiguresBuilder {
	boolean reflectionAllowed = true;
	int[][][] figures;
	RectangularField field;
	PentaPathPlacement[][] placements;
	int[] fieldRestriction;
	
	PentaPathPlacement[][] placementsForPoint;
	
	public void setReflectionAllowed(boolean b) {
		reflectionAllowed = b;
	}
	
	public void setFieldRestriction(int[] rs) {
		fieldRestriction = rs;
	}
	
	public void createPlacements(int[][][] figures, RectangularField field) {
		this.field = field;
		this.figures = figures;
		placements = new PentaPathPlacement[figures.length][];
		for (int i = 0; i < figures.length; i++) {
			ArrayList l = createPlacements(i);
			Iterator it = l.iterator();
			while(it.hasNext()) {
				PentaPathPlacement p = (PentaPathPlacement)it.next();
				if(!accept(p.getPoints())) it.remove();
			}
//			System.out.println(l.size());
			placements[i] = (PentaPathPlacement[])l.toArray(new PentaPathPlacement[0]);
		}
		
		createPlacementsForPoints();
	}
	
	private ArrayList createPlacements(int index) {
		ArrayList list = new ArrayList();
		Set codes = new HashSet();
		PentaPathPlacement initial = new PentaPathPlacement(index, toDots(figures[index]), toPoints(figures[index]));
		list.add(initial);
		codes.add(initial.getCode());
		int c = 0;
		while(c < list.size()) {
			PentaPathPlacement placement = (PentaPathPlacement)list.get(c);
			int[] dots = placement.getDots();
			int[] pc = placement.getPoints();
			int[] pc1 = rotate(pc);
			add(index, dots, pc1, list, codes);
			if(reflectionAllowed) {
				pc1 = reflect(pc);
				add(index, dots, pc1, list, codes);
			}
			for (int d = 0; d < 4; d++) {
				pc1 = shift(pc, d);
				add(index, dots, pc1, list, codes);
			}
			++c;
		}
		return list;
	}
	
	private void add(int index, int[] dots, int[] pc, ArrayList list, Set codes) {
		if(pc == null) return;
		PentaPathPlacement pn = new PentaPathPlacement(index, dots, pc);		
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
	
	private int[] toDots(int[][] figure) {
		int[] dots = new int[figure.length];
		for (int i = 0; i < dots.length; i++) {
			dots[i] = figure[i][2];
		}
		return dots;
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

	private int[] reflect(int[] points) {
		int[] ps = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ps[i] = field.getReflection()[points[i]];
			if(ps[i] < 0) return null;
		}
		return ps;
	}
	
	private void createPlacementsForPoints() {
		placementsForPoint = new PentaPathPlacement[field.getSize()][];
		for (int p = 0; p < placementsForPoint.length; p++) {
			int v = 0;
			for (int i = 0; i < placements.length; i++) {
				for (int j = 0; j < placements[i].length; j++) {
					if(placements[i][j].hasAsDot(p)) v++;					
				}
			}
			placementsForPoint[p] = new PentaPathPlacement[v];
			v = 0;
			for (int i = 0; i < placements.length; i++) {
				for (int j = 0; j < placements[i].length; j++) {
					if(placements[i][j].hasAsDot(p)) {
						placementsForPoint[p][v] = placements[i][j];
						v++;					
					}
				}
			}
		}
//		for (int p = 0; p < placementsForPoint.length; p++) {
//			System.out.print(" " + placementsForPoint[p].length);
//			if(field.isRightBorder(p)) System.out.println("");
//		}
	}
	
}
