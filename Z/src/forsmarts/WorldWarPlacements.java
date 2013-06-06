package forsmarts;

import java.util.ArrayList;
import com.slava.common.*;

public class WorldWarPlacements {
	public class Placement {
		int index;
		int[] places;
	}

	int piecesCount;
	Placement[] placements;

	public void createPlacements(TwoDimField f, int[] form) {
		ArrayList list = new ArrayList();
		int size = 0;
		piecesCount = 4;
		
		int orientationCount = 2;

		for (int p = 0; p < f.getSize(); p++) {
			if(form[p] == 0) continue;
			for (int d0 = 0; d0 < orientationCount; d0++) {
				for (int l = 1; l < 5; l++) {
					int[] places = new int[3 * l + 1];
					places[0] = p;
					int i = 1;
					boolean ok = true;
					for (int di = 0; di < 3 && ok; di++) {
						int d = d0 + 2 * di;
						int q = p;
						for (int s = 0; s < l && ok; s++) {
							q = f.jump(q, d);
							if(q < 0 || form[q] == 0) {
								ok = false;
							} else {
								places[i] = q;
								i++;
							}
						}
					}
					if(ok) {
						Placement pl = new Placement();
						pl.index = l;
						pl.places = places;
						list.add(pl);
					} else {
						break;
					}
				}
			}
		}
		
		
		placements = (Placement[])list.toArray(new Placement[0]);
		
//		System.out.println("Pieces Count=" + size);
//		System.out.println("Placement Count=" + placements.length);
		
//		for (int i = 0; i < placements.length; i++) {
//			int[] ps = placements[i].places;
//			for (int k = 0; k < ps.length; k++) System.out.print(" " + ps[k]);
//			System.out.println("");
//		}
		
	}

}
