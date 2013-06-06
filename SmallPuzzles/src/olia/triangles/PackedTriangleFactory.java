package olia.triangles;

import java.util.ArrayList;
import java.util.List;

import com.slava.common.RectangularField;

public class PackedTriangleFactory {
	PackedTriangle[] all;
	PackedTriangle[][] byPoint;
	
	public PackedTriangleFactory() {}
	
	public void create(RectangularField f) {
		List allList = new ArrayList();
		for (int ix = 0; ix < f.getWidth() - 1; ix++) {
			for (int iy = 0; iy < f.getHeight() - 1; iy++) {
				int p = f.getIndex(ix, iy);
				int px = f.getIndex(ix + 1, iy);
				int py = f.getIndex(ix, iy + 1);
				int pxy = f.getIndex(ix + 1, iy + 1);
				allList.add(new PackedTriangle(ix, iy, new int[]{p,px,py}, 0));
				allList.add(new PackedTriangle(ix, iy, new int[]{px,py,pxy}, 0));
				allList.add(new PackedTriangle(ix, iy, new int[]{p,px,pxy}, 1));
				allList.add(new PackedTriangle(ix, iy, new int[]{p,py,pxy}, 1));
			}
		}
		all = (PackedTriangle[])allList.toArray(new PackedTriangle[0]);
		byPoint = new PackedTriangle[f.getSize()][];
		for (int p = 0; p < byPoint.length; p++) {
			List l = new ArrayList();
			for (int i = 0; i < all.length; i++) {
				if(all[i].contains(p)) l.add(all[i]);
			}
			byPoint[p] = (PackedTriangle[])l.toArray(new PackedTriangle[0]);
		}
	}
	
	

}
