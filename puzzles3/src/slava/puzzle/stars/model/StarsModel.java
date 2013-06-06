package slava.puzzle.stars.model;

import java.util.*;
import slava.puzzle.template.model.*;

public class StarsModel extends PuzzleModel {
	StarsSetsField field = new StarsSetsField();
	protected List solutions = new ArrayList();
	protected int selectedSolution = -1;
	
	StarsModelListener listener;
	
	int[] solution;

	public StarsModel() {
		field.setSize(11);
		setLoader(new StarsLoader());
	}
	
	public void setListener(StarsModelListener listener) {
		this.listener = listener;		
	}
	
	public StarsSetsField getField() {
		return field;
	}

	public void setSolutions(List solutions) {
		this.solutions.clear();
		this.solutions.addAll(solutions);
		selectedSolution = -1;
		if(solutions.size() == 0) {
			solution = null;
		} else {
			nextSolution();
		}
	}
	
	public void nextSolution() {
		if(solutions.size() == 0) {
			if(selectedSolution < 0) return;
			selectedSolution = -1;
			solution = null;
		} else {
			selectedSolution++;
			if(selectedSolution >= solutions.size()) selectedSolution = 0;
			solution = (int[])solutions.get(selectedSolution);
		}
	}
	
	public int[] getSelectedSolution() {
		return solution;
	}
	
	public void changeFieldSize(int width) {
		if(field.getWidth() == width) return;
		field.setSize(width);
		solutions.clear();
		selectedSolution = -1;
		solution = null;
		setSolutionInfo(null);		
		if(listener != null) listener.fieldSizeChanged();
	}

}
