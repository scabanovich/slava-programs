package forsmarts;

import forsmarts.fitting2d.*;

public class Pentagroups {
	static int[][] FIELDS = {
		{
			0,1,0,1,0,1,0,
			0,1,1,1,1,1,0,
			0,1,1,1,1,1,0,
			0,0,1,1,1,0,0,
			0,0,0,1,0,0,0,
		},
		{
			0,0,0,1,0,0,0,
			0,1,1,1,1,1,0,
			0,1,1,1,1,1,0,
			1,1,1,1,1,1,1,
			0,0,0,0,0,0,0,
		},
		{
			0,0,1,0,1,0,0,
			0,0,1,1,1,0,0,
			1,1,1,1,1,1,1,
			0,1,1,1,1,1,0,
			0,0,0,0,0,0,0,
		},
		{
			0,0,0,1,0,0,0,
			0,0,1,1,1,0,0,
			0,1,1,1,1,1,0,
			0,0,1,1,1,0,0,
			0,0,1,1,1,0,0,
		},
	};
	
	public void run() {
		for (int i1 = 0; i1 < 12; i1++) {
			for (int i2 = i1 + 1; i2 < 12; i2++) {
				for (int i3 = i2 + 1; i3 < 12; i3++) {
					int i = getUniqueRegion(i1, i2, i3);
					if(i < 0) continue;
					String s = "" + PentaFigures.DESIGNATIONS[i1] + 
					                PentaFigures.DESIGNATIONS[i2] +
					                PentaFigures.DESIGNATIONS[i3] + " " + i;
					System.out.println(s);
				}
			}
		}
	}
	
	int getUniqueRegion(int i1, int i2, int i3) {
		int fc = -1;
		for (int f = 0; f < 4; f++) {
			if(canFit(i1, i2, i3, f)) {
				if(fc != -1) return -1; else fc = f;
			}
		}
		return fc;
	}
	
	
	boolean canFit(int i1, int i2, int i3, int f) {
		Problem2D p = new Problem2D();
		p.xSize = 7;
		p.ySize = 5;
		p.figures = new int[][][]{PentaFigures.FIGURES[i1], PentaFigures.FIGURES[i2], PentaFigures.FIGURES[i3]};
		p.isReflectionAllowed = true;
		p.usageLimit = new int[p.figures.length];
		for (int i = 0; i < p.figures.length; i++) p.usageLimit[i] = 1;
		p.designations = new char[]{
			PentaFigures.DESIGNATIONS[i1], PentaFigures.DESIGNATIONS[i2], PentaFigures.DESIGNATIONS[i3]
		};
		p.shapeMode = PackingAnalysis.NO_SHAPE;
		p.fieldRestriction = new int[FIELDS[f].length];
		for (int i = 0; i < FIELDS[f].length; i++) p.fieldRestriction[i] = 1 - FIELDS[f][i];
		int sc = new Packing2dRunner().run(p);
		return sc > 0;
	}

	public static void main(String[] args) {
		new Pentagroups().run();
	}

}

/*  Selection done by hand
WPU 0

ILY 1
NLY 1

TIV 2
TNV 2

XFZ 3

*/