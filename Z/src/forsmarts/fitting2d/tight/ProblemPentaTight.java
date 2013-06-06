package forsmarts.fitting2d.tight;

import com.slava.common.RectangularField;

import forsmarts.fitting2d.PackingAnalysis;
import forsmarts.fitting2d.PentaFigures;
import forsmarts.fitting2d.Problem2D;

public class ProblemPentaTight extends Problem2D {
	
	public ProblemPentaTight() {
		setA();
	}

	void setA() {
		xSize = 5;
		ySize = 22;
		figures = PentaFigures.FIGURES;
		isReflectionAllowed = true;
		usageLimit = new int[figures.length];
		for (int i = 0; i < figures.length; i++) usageLimit[i] = 1;
		designations = PentaFigures.DESIGNATIONS;
		shapeMode = PackingAnalysis.NO_SHAPE;
		
		RectangularField f = new RectangularField();
		f.setSize(xSize, ySize);
		int size = xSize * ySize;
		int total = 0;
		fieldRestriction = new int[size];
		for (int p = 0; p < size; p++) {
			boolean odd = ((f.getX(p) + f.getY(p)) % 2) == 1;
			fieldRestriction[p] = (odd) ? 1 : 2;
			
			total += fieldRestriction[p];
		}
		System.out.println("Total = " + total);
	}

}
