package slava.puzzle.ellen.model;

import slava.puzzle.pentaletters.model.PentaFigures;

public class EllenFigures extends PentaFigures {
	
	protected void initForms() {
		figureSize = 4;
		forms = new int[][][]{
			{{1,1},{2,1},{3,1},{1,2}}
		};
	}
	
}
