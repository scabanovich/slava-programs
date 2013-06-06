package slava.puzzle.hitori.model;

import slava.puzzle.template.model.PuzzleModel;

import com.slava.common.*;

public class HitoriModel extends PuzzleModel {
	RectangularField field = new RectangularField();
	HitoriModelListener listener;
	HitoriProblemInfo problem;

	public HitoriModel() {
		field.setSize(9, 9);
		problem = new HitoriProblemInfo();
		problem.setNumbers(new int[field.getSize()]);
		setLoader(new HitoriLoader());
	}
	
	public RectangularField getField() {
		return field;		
	}
	
	public void setListener(HitoriModelListener l) {
		listener = l;
	}
	
	public HitoriProblemInfo getProblemInfo() {
		return problem;
	}

	public void changeFieldSize(int width) {
		if(field.getWidth() == width) return;
		field.setSize(width, width);
		int[] numbers = problem.getNumbers();
		if(numbers == null || numbers.length != field.getSize()) {
			problem.setNumbers(new int[field.getSize()]);
		}
		problem.setSolution(null);
		setSolutionInfo(null);		
		if(listener != null) listener.fieldSizeChanged();
	}

}
