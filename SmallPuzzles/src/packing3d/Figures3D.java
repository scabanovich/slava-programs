package packing3d;

import java.util.*;

public class Figures3D {
	int[][][] figures;
	CubeField cubeField;
	Placement[] placements;
	
	public void createPlacements(int[][][] figures, CubeField cubeField) {
		this.cubeField = cubeField;
		this.figures = figures;
		ArrayList list = new ArrayList();
		for (int i = 0; i < figures.length; i++) {
			ArrayList l = createPlacements(i);
			System.out.println(l.size());
			list.addAll(l);
		}
		placements = (Placement[])list.toArray(new Placement[0]);
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
			for (int d = 0; d < 3; d++) {
				int[] pc1 = rotate(pc, d);
				if(pc1 == null) continue;
				Placement pn = new Placement(index, pc1);
				String code = pn.getCode();
				if(codes.contains(code)) continue;
				codes.add(code);
				list.add(pn);
			}
			for (int d = 0; d < 6; d++) {
				int[] pc1 = shift(pc, d);
				if(pc1 == null) continue;
				Placement pn = new Placement(index, pc1);
				String code = pn.getCode();
				if(codes.contains(code)) continue;
				codes.add(code);
				list.add(pn);
			}
			++c;
		}
		return list;
	}
	
	private int[] toPoints(int[][] figure) {
		int[] points = new int[figure.length];
		for (int i = 0; i < points.length; i++) {
			points[i] = cubeField.xyz[figure[i][0]][figure[i][1]][figure[i][2]];
		}
		return points;
	}
	
	private int[] rotate(int[] points, int d) {
		int[] ps = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ps[i] = cubeField.rotation[d][points[i]];
			if(ps[i] < 0) return null;
		}
		Arrays.sort(ps);
		return ps;
	}
	
	private int[] shift(int[] points, int d) {
		int[] ps = new int[points.length];
		for (int i = 0; i < points.length; i++) {
			ps[i] = cubeField.jump(points[i], d);
			if(ps[i] < 0) return null;
		}
		Arrays.sort(ps);
		return ps;
	}
	
}
