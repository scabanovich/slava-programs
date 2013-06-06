package forsmarts;

import java.util.*;

import com.slava.common.RectangularField;

public class ManifoldDividingPlacements {
	public class Placement {
		int index;
		int[] places;
	}
	
	int piecesCount;
	Placement[] placements;

	public void createPlacements(RectangularField f, int[] data) {
		ArrayList list = new ArrayList();
		int size = 0;
		int lv = 2;
		int area = 0;
		for (int length = 2; length < 5; length++) {
			lv = lv * 2;
			for (int c = 0; c < lv; c++) {
				int[] r = decode(length, c);
				int c1 = encodeBack(r);
//				System.out.println("" + c + " - " + c1);
				if(c1 < c) continue;
				int dm = (c1 == c) ? 2 : 4;
				for (int p = 0; p < f.getSize(); p++) {
					for (int d = 0; d < dm; d++) {
						int[] places = new int[length];
						if(data[p] != r[0]) continue;
						places[0] = p;
						int k = 1;
						while(k < places.length) {
							places[k] = f.jump(places[k - 1], d);
							if(places[k] < 0 || data[places[k]] != r[k]) break;
							++k;
						}
						if(k < places.length) continue;
						Placement placement = new Placement();
						placement.index = size;
						placement.places = places;
						list.add(placement);
					}
				}
				size++;
				area += length;
			}
		}
		placements = (Placement[])list.toArray(new Placement[0]);
		piecesCount = size;
		
		System.out.println("Area=" + area);
		System.out.println("Pieces Count=" + size);
		System.out.println("Placement Count=" + placements.length);
		
	}
	
	int[] decode(int length, int c) {
		int[] r = new int[length];
		for (int i = 0; i < length; i++) {
			r[i] = c % 2;
			c = c / 2;
		}
		return r;
	}
	
	int encodeBack(int[] r) {
		int c = 0;
		for (int i = 0; i < r.length; i++) {
			c = c * 2 + r[i];
		}
		return c;
	}


}
