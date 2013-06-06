package slava.puzzle.pentasets.model;

import java.util.*;
import slava.puzzle.pentaletters.model.*;

public class PentaSetsModel extends PentaLettersModel {
	
	int[] conditions;

	protected void init() {
		field = new PentaLettersField();
		field.setSize(10, 10);
		setLoader(new PentaSetsLoader());
		init2();
	}

	protected void init2() {
		conditions = new int[field.getSize()];
		for (int i = 0; i < field.getSize(); i++) {
			conditions[i] = -1;
		}
		resetLetters();
	}

	public void setSolutions(List solutions) {
		this.solutions.clear();
		this.solutions.addAll(solutions);
		selectedSolution = -1;
		if(solutions.size() == 0) {
			resetLetters();
		} else {
			nextSolution();
		}
	}
	
	public void nextSolution() {
		if(solutions.size() == 0) {
			if(selectedSolution < 0) return;
			selectedSolution = -1;
			resetLetters();
		} else {
			selectedSolution++;
			if(selectedSolution >= solutions.size()) selectedSolution = 0;
			int[] ls = (int[])solutions.get(selectedSolution);
			for (int i = 0; i < ls.length; i++) {
				field.setLetterAt(i, ls[i]);
			}
		}
	}
	
	public void resetLetters() {
		for (int i = 0; i < field.getSize(); i++) {
			field.setLetterAt(i, conditions[i]);
		}
	}
	
	public void setConditions(int[] c) {
		conditions = (int[])c.clone();
	}
	
	public int[] getConditions() {
		return conditions;
	}

}
