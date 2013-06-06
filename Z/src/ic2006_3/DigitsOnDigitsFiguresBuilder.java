package ic2006_3;

import java.util.*;
import com.slava.common.RectangularField;

public class DigitsOnDigitsFiguresBuilder {
	boolean reflectionAllowed = false;
	int[][][] figures;
	RectangularField field;
	DigitsOnDigitsPlacement[][] placements;
	int[] fieldRestriction;
	
	DigitsOnDigitsPlacement[][] placementsForPoint;
	
	public void setReflectionAllowed(boolean b) {
		reflectionAllowed = b;
	}
	
	public void setFieldRestriction(int[] rs) {
		fieldRestriction = rs;
	}
	
	public void createPlacements(int[][][] figures, RectangularField field) {
		this.field = field;
		this.figures = figures;
		placements = new DigitsOnDigitsPlacement[figures.length][];
		for (int i = 0; i < figures.length; i++) {
			ArrayList l = createPlacements(i);
			Iterator it = l.iterator();
			while(it.hasNext()) {
				DigitsOnDigitsPlacement p = (DigitsOnDigitsPlacement)it.next();
				if(!accept(p.getPoints())) it.remove();
			}
//			System.out.println(l.size());
			placements[i] = (DigitsOnDigitsPlacement[])l.toArray(new DigitsOnDigitsPlacement[0]);
		}
		
		createPlacementsForPoints();
	}
	
	private ArrayList createPlacements(int index) {
		ArrayList list = new ArrayList();
		Set codes = new HashSet();
		DigitsOnDigitsPlacement initial = new DigitsOnDigitsPlacement(index, toValues(figures[index]), toPoints(figures[index]));
		list.add(initial);
		codes.add(initial.getCode());
		int c = 0;
		while(c < list.size()) {
			DigitsOnDigitsPlacement placement = (DigitsOnDigitsPlacement)list.get(c);
			int[] values = placement.getValues();
			int[] pc = placement.getPoints();
			int[] pc1 = rotate(pc);
			add(index, values, pc1, list, codes);
			if(reflectionAllowed) {
				pc1 = reflect(pc);
				add(index, values, pc1, list, codes);
			}
			for (int d = 0; d < 4; d++) {
				pc1 = shift(pc, d);
				add(index, values, pc1, list, codes);
			}
			++c;
		}
		return list;
	}
	
	private void add(int index, int[] dots, int[] pc, ArrayList list, Set codes) {
		if(pc == null) return;
		DigitsOnDigitsPlacement pn = new DigitsOnDigitsPlacement(index, dots, pc);		
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
	
	private int[] toValues(int[][] figure) {
		int[] values = new int[figure.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = figure[i][2];
		}
		return values;
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
		placementsForPoint = new DigitsOnDigitsPlacement[field.getSize()][];
		for (int p = 0; p < placementsForPoint.length; p++) {
			int v = 0;
			for (int i = 0; i < placements.length; i++) {
				for (int j = 0; j < placements[i].length; j++) {
					if(placements[i][j].getValue(p) >= 0) v++;					
				}
			}
			placementsForPoint[p] = new DigitsOnDigitsPlacement[v];
			v = 0;
			for (int i = 0; i < placements.length; i++) {
				for (int j = 0; j < placements[i].length; j++) {
					if(placements[i][j].getValue(p) >= 0) {
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
