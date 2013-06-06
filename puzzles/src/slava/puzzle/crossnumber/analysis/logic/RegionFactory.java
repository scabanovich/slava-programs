package slava.puzzle.crossnumber.analysis.logic;

import java.util.ArrayList;

import slava.puzzle.crossnumber.*;

public class RegionFactory {

	public static Region[] createRegions(CrossNumberField f) {
		ArrayList list = new ArrayList();
		for (int i = 0; i < f.size(); i++) {
			if((f.getStatus(i) & 1) != 0) {
				int s = 0;
				int p = f.jump(i, 0);
				while(p >= 0 && f.getStatus(p) == 4) {
					s++;
					p = f.jump(p, 0);
				}
				int[] places = new int[s];
				s = 0;
				p = f.jump(i, 0);
				while(p >= 0 && f.getStatus(p) == 4) {
					places[s] = p;
					s++;
					p = f.jump(p, 0);
				}
				Region r = new Region(f.getHSum(i), places);
				r.setInfo(i, 0);
				list.add(r);
			}
			if((f.getStatus(i) & 2) != 0) {
				int s = 0;
				int p = f.jump(i, 1);
				while(p >= 0 && f.getStatus(p) == 4) {
					s++;
					p = f.jump(p, 1);
				}
				int[] places = new int[s];
				s = 0;
				p = f.jump(i, 1);
				while(p >= 0 && f.getStatus(p) == 4) {
					places[s] = p;
					s++;
					p = f.jump(p, 1);
				}
				Region r = new Region(f.getVSum(i), places);
				r.setInfo(i, 1);
				list.add(r);
			}
		}
		
		if(f.isThroughRow()) {
			createThroughRegions(f, list);
		}
		
//		Region[] res = (Region[])list.toArray(new Region[0]);
//		for (int i = 0; i < res.length; i++) System.out.println(res[i].toString());
		return (Region[])list.toArray(new Region[0]);
	}
	
	private static void createThroughRegions(CrossNumberField f, ArrayList list) {
		for (int ix = 0; ix < f.getWidth(); ix++) {
			int s = 0;
			for (int iy = 0; iy < f.getHeight(); iy++) {
				int p = f.getIndex(ix, iy);
				if(f.getStatus(p) == 4)	s++;
			}
			if(s < 2) continue;
			int[] places = new int[s];
			s = 0;
			for (int iy = 0; iy < f.getHeight(); iy++) {
				int p = f.getIndex(ix, iy);
				if(f.getStatus(p) == 4)	{
					places[s] = p;
					s++;
				}
			}
			Region r = new Region(-1, places);
			r.setInfo(-1, 1);
			list.add(r);
		}
		for (int iy = 0; iy < f.getHeight(); iy++) {
			int s = 0;
			for (int ix = 0; ix < f.getWidth(); ix++) {
				int p = f.getIndex(ix, iy);
				if(f.getStatus(p) == 4)	s++;
			}
			if(s < 2) continue;
			int[] places = new int[s];
			s = 0;
			for (int ix = 0; ix < f.getWidth(); ix++) {
				int p = f.getIndex(ix, iy);
				if(f.getStatus(p) == 4)	{
					places[s] = p;
					s++;
				}
			}
			Region r = new Region(-1, places);
			r.setInfo(-1, 0);
			list.add(r);
		}
	}
	
	public static Region[][] getRegionsByPoints(int size, Region[] regions) {
		ArrayList[] lists = new ArrayList[size];
		for (int i = 0; i < regions.length; i++) {
			for (int k = 0; k < regions[i].points.length; k++) {
				int p = regions[i].points[k];
				if(lists[p] == null) lists[p] = new ArrayList();
				lists[p].add(regions[i]);
			}
		}
		
		Region[][] rs = new Region[size][];
		for (int i = 0; i < rs.length; i++) {
			if(lists[i] != null) {
				rs[i] = (Region[])lists[i].toArray(new Region[0]);
//				System.out.println(i);
//				for (int j = 0; j < rs[i].length; j++) System.out.println(" " + rs[i][j].toString());
			}
		}
		return rs;
	}
	
}
