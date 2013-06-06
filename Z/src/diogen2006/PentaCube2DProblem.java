package diogen2006;

import forsmarts.fitting2d.PackingAnalysis;
import forsmarts.fitting2d.PentaFigures;
import forsmarts.fitting2d.Problem2D;

public class PentaCube2DProblem extends Problem2D {
	PentaCubeForm cubeForm;

	public PentaCube2DProblem(PentaCubeForm cubeForm) {
		this.cubeForm = cubeForm;
		set();
	}
	
	void set() {
		xSize = cubeForm.cubeWidth + cubeForm.cubeDepht + 1;
		ySize = cubeForm.cubeHeight;
		designations = PentaFigures.DESIGNATIONS;
		fieldRestriction = new int[xSize * ySize];
		for (int iy = 0; iy < ySize; iy++) {
			int p = iy * xSize + cubeForm.cubeWidth;
			fieldRestriction[p] = 1;
		}
		figures = PentaFigures.FIGURES;
		isReflectionAllowed = true;
		shapeMode = PackingAnalysis.HVD_SHAPE;

		usageLimit = new int[designations.length];
		for (int i = 0; i < PentaFigures.DESIGNATIONS.length; i++) {
			if(cubeForm.topPentas.indexOf(PentaFigures.DESIGNATIONS[i]) >= 0) {
				usageLimit[i] = 0;
			} else {
				usageLimit[i] = 1;
			}
		}
	}
}
