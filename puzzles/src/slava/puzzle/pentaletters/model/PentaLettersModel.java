package slava.puzzle.pentaletters.model;

import java.util.*;
import slava.puzzle.template.model.PuzzleModel;

public class PentaLettersModel extends PuzzleModel {
	protected PentaLettersField field;
	protected List solutions = new ArrayList();
	protected int selectedSolution = -1;
	
	public PentaLettersModel() {
		init();
	}
	
	protected void init() {
		field = new PentaLettersField();
		field.setSize(10, 10);
		setLoader(new PentaLettersLoader());
	}
	
	protected void init2() {
		
	}
	
	public PentaLettersField getField() {
		return field;
	}
	
	public void setSolutions(List solutions) {
		this.solutions.clear();
		this.solutions.addAll(solutions);
		selectedSolution = -1;
		if(solutions.size() == 0) {
			field.resetGroups();
		} else {
			nextSolution();
		}
	}
	
	public void nextSolution() {
		if(solutions.size() == 0) {
			if(selectedSolution < 0) return;
			selectedSolution = -1;
			field.resetGroups();
		} else {
			selectedSolution++;
			if(selectedSolution >= solutions.size()) selectedSolution = 0;
			int[] gs = (int[])solutions.get(selectedSolution);
			for (int i = 0; i < gs.length; i++) {
				field.setGroupAt(i, gs[i]);
			}
		}
	}
	
	public void clearSolutions() {
		solutions.clear();
		selectedSolution = -1;
	}

	public void setSize(int width, int height) {
		clearSolutions();
		field.setSize(width, height);
		init2();
	}
	
}
