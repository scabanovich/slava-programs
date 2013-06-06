package slava.puzzle.doublepath.model;

import java.util.List;
import slava.puzzle.template.model.PuzzleModel;

public class DoublePathModel extends PuzzleModel {
	DoublePathField field;
	List solutionList;
	int currentSolution = -1;
	int selectedNode = -1;
	
	public DoublePathModel() {
		init();
	}
	
	private void init() {
		field = new DoublePathField();
		setSize(10, 9);
		setLoader(new DoublePathLoader());
	}
	
	public void setSize(int width, int height) {
		clearSolutions();
		field.setSize(width, height);
		field.setState(field.getWidth() - 2, 3);
		field.setState(field.getWidth() - 1, 4);
		field.setState(field.getSize() - field.getWidth(), 3);
		field.setState(field.getSize() - field.getWidth() + 1, 4);
		setSelectedNode(selectedNode); 
	}
	
	public DoublePathField getField() {
		return field;
	}
	
	public void clearSolutions() {
		setSolutions(null);
		setSolutionInfo(null);
	}
	
	public void setSolutions(List solutionList) {
		this.solutionList = solutionList;
		if(solutionList == null || solutionList.size() == 0) currentSolution = -1;
		else currentSolution = 0;
	}
	
	public int[][] getCurrentSolution() {
		if(solutionList == null || currentSolution < 0 || currentSolution >= solutionList.size()) return null;
		return (int[][])solutionList.get(currentSolution);
	}
	
	public void nextSolution() {
		if(solutionList == null || solutionList.size() == 0) return;
		currentSolution++;
		if(currentSolution >= solutionList.size()) currentSolution = 0;
	}
	
	public int getSelectedNode() {
		return selectedNode;
	}
	
	public void setSelectedNode(int i) {
		if(i < 0 || i >= field.getSize()) {
			selectedNode = -1;
		} else {
			selectedNode = i;
		}
	}

}
