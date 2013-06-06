package slava.puzzle.pentaletters.analysis;

import java.util.*;
import slava.puzzle.pentaletters.model.PentaLettersField;

public class AbstractPentaLettersAnalyzer {
	protected int figureSize;
	protected int[][][] figures;
	protected int[] figureIndex;
	protected PentaLettersField field;
	protected PentaFieldChecker checker;
	protected int figureCount;
	protected int[] groups;
	protected List solutions = new ArrayList();
	
	public AbstractPentaLettersAnalyzer() {
		initChecker();
	}
	
	protected void initChecker() {
		setChecker(new PentaFieldChecker());
		figureSize = 5;
	}

	public void setField(PentaLettersField field) {
		this.field = field;
	}
	
	public void setChecker(PentaFieldChecker checker) {
		this.checker = checker;
	}
	
	public void setFigures(int[][][] figures, int[] figureIndex) {
		this.figures = figures;
		this.figureIndex = figureIndex;
	}

	public List getSolutions() {
		return solutions;
	}

	public void solve() {
		check();
		init();
		anal();
	}
	
	void check() {
		checker.check(field);
	}

	protected void init() {
	}
	
	protected void anal() {
	}

}
