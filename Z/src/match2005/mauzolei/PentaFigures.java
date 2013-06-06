package match2005.mauzolei;

import java.util.*;

public class PentaFigures {
	int[][] forms = {
		{0,0,0,0,0,	 1,1,1,1,1,	 0,0,0,0,0}, //I
		{0,0,0,0,0,	 0,0,1,1,0,	 0,1,1,1,0}, //P
		{0,0,0,1,0,	 0,1,1,1,0,	 0,1,0,0,0}, //Z
		{0,0,1,1,0,	 0,1,1,0,0,	 0,1,0,0,0}, //W
		{0,0,1,0,0,	 0,1,1,1,0,	 0,0,1,0,0}, //X
		{0,0,0,1,0,	 0,0,0,1,0,	 0,1,1,1,0}, //V
		{0,0,1,0,0,	 0,0,1,0,0,	 0,1,1,1,0}, //T
		{0,0,1,0,0,	 0,1,1,1,1,	 0,0,0,0,0}, //Y
		{0,1,1,0,0,	 0,0,1,1,0,	 0,0,1,0,0}, //F
		{0,1,0,0,0,	 0,1,1,1,1,	 0,0,0,0,0}, //L
		{0,0,0,1,1,	 0,1,1,1,0,	 0,0,0,0,0}, //S
		{0,1,0,1,0,	 0,1,1,1,0,	 0,0,0,0,0}, //U
	};
	
	MauzoleiField field;
	MauzoleiPlacement[] placements;
	
	public void createPlacements(MauzoleiField field) {
		this.field = field;
		ArrayList list = new ArrayList();
		for (int i = 0; i < forms.length; i++) {
			ArrayList l = new ArrayList();
			MauzoleiPlacement placement = getInitialPlacemant(i);
			collectPlacements(l, placement);
			System.out.println(i + ":" + l.size());
			list.addAll(l);
		}
		
		removeBadPlacements(list);
		
		placements = (MauzoleiPlacement[])list.toArray(new MauzoleiPlacement[0]);
	}
	
	MauzoleiPlacement getInitialPlacemant(int index) {
		int[] points = new int[5];
		int c = 0;
		for (int i = 0; i < forms[index].length; i++) {
			if(forms[index][i] == 0) continue;
			points[c] = field.position(i % 5, i / 5, 0);
			++c;
		}
		if(c != 5) throw new RuntimeException("This is not pentamino: " + c);
		return new MauzoleiPlacement(index, points);
	}
	
	void collectPlacements(ArrayList l, MauzoleiPlacement placement) {
		if(contains(l, placement)) return;
		l.add(placement);
		int[] points;
		for (int d = 0; d < 6; d++) {
			points = shift(placement.points, d);
			if(points == null) continue;
			collectPlacements(l, new MauzoleiPlacement(placement.index, points));
		}
		points = zReflect(placement.points);
		if(points != null) {
			collectPlacements(l, new MauzoleiPlacement(placement.index, points));
		}
		points = zRotate(placement.points);
		if(points != null) {
			collectPlacements(l, new MauzoleiPlacement(placement.index, points));
		}
		for (int j = 0; j < field.ySize; j++) {
			points = xRotate(placement.points, j);
			if(points != null) {
				collectPlacements(l, new MauzoleiPlacement(placement.index, points));
			}
		}
	}
	
	boolean contains(ArrayList l, MauzoleiPlacement placement) {
		for (int i = 0; i < l.size(); i++) {
			if(placement.equals((MauzoleiPlacement)l.get(i))) return true;
		}
		return false;
	}
	
	int[] shift(int[] points, int d) {
		int[] ps = new int[points.length];
		for (int i = 0; i < ps.length; i++) {
			ps[i] = field.jump(points[i], d);
			if(ps[i] < 0) return null;
		}
		return ps;
	}
	int[] zReflect(int[] points) {
		int[] ps = new int[points.length];
		for (int i = 0; i < ps.length; i++) {
			ps[i] = field.zReflection[points[i]];
			if(ps[i] < 0) return null;
		}
		return ps;
	}
	int[] zRotate(int[] points) {
		int[] ps = new int[points.length];
		for (int i = 0; i < ps.length; i++) {
			ps[i] = field.zRotation[points[i]];
			if(ps[i] < 0) return null;
		}
		return ps;
	}
	int[] xRotate(int[] points, int k) {
		int[] ps = new int[points.length];
		for (int i = 0; i < ps.length; i++) {
			ps[i] = field.xRotations[points[i]][k];
			if(ps[i] < 0) return null;
		}
		return ps;
	}
	
	void removeBadPlacements(ArrayList l) {
		System.out.println("sizeA=" + l.size());
		Iterator it = l.iterator();
		while(it.hasNext()) {
			MauzoleiPlacement p = (MauzoleiPlacement)it.next();
			int zc = 0;
			for (int i = 0; i < p.points.length; i++) {
				if(field.z[p.points[i]] == field.zSize - 1) ++zc;
			}
			if(zc > 1) it.remove();
			
		}
		System.out.println("sizeB=" + l.size());
	}

}
