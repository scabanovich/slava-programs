package slava.puzzle.ellen.model;

import java.util.*;
import slava.puzzle.template.model.PuzzleModel;

public class EllenModel extends PuzzleModel {
	EllenField field = new EllenField();
	private List solutions = new ArrayList();
	private int selectedSolution = -1;
	
	public EllenModel() {
		field.setSize(8, 8);
		setLoader(new EllenLoader());
	}
	
	public EllenField getField() {
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

}
