package forsmarts.fitting2d;

public class Problem2D_c extends Problem2D {
	static int[] SELECTED = new int[]{
//		0,0,0,0,0,0,0,0,0,0,0,0,0,
		1,1,1,1,1,0,1,1,1,1,1,
		1,1,1,1,1,0,1,1,1,1,1,
		0,0,0,0,1,0,1,0,0,0,1,
		0,0,0,1,1,0,1,0,0,0,1,
		0,0,1,1,1,0,1,0,0,0,1,
		0,1,1,1,0,0,1,0,0,0,1,
		0,1,1,1,0,0,1,0,0,0,1,
		0,1,1,0,0,0,1,0,0,0,1,
		0,1,1,0,0,0,1,1,1,1,1,
		0,1,1,0,0,0,1,1,1,1,1,
//		0,0,0,0,0,0,0,0,0,0,0,0,0,
	};
	
	Problem2D_c(int i1, int i2) {
		setB(i1, i2);
	}

	void setB(int i1, int i2) {
		xSize = 11;
		ySize = 10;
		figures = PentaFigures.FIGURES;
		isReflectionAllowed = true;
		usageLimit = new int[figures.length];
		for (int i = 0; i < figures.length; i++) usageLimit[i] = 1;
//		usageLimit[3] = 0;
//		usageLimit[8] = 0;
		designations = PentaFigures.DESIGNATIONS;
		shapeMode = PackingAnalysis.NO_SHAPE;
		int[] selected = (int[])SELECTED.clone();
		selected[i1] = 1;
		selected[i2] = 1;
		fieldRestriction = new int[selected.length];
		for (int i = 0; i < selected.length; i++) fieldRestriction[i] = 1 - selected[i];
	}

}

/*
.............,
.UUUW..NNNLL.,
.U.UWW.ZZNNL.,
.....WWZ...L.,
....FFZZ...L.,
...FFVVV...I.,
...XF..V...I.,
..XxX..V...I.,
..PX...T...I.,
..PP...TTTYI.,
..PP...TYYYY.,
.............,

*/ 
