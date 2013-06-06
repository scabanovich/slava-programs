package slava.puzzle.sudoku.ui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class PartitioningSumMouseListener extends MouseAdapter implements MouseMotionListener {
	SudokuComponent component;
	
	boolean drawMode = true;
	int partitionIndex = -1;

	public PartitioningSumMouseListener(SudokuComponent component) {
		this.component = component;
	}

	public void mousePressed(MouseEvent e) {
		if(component.getSudokuModel().isRunning()) return;
		Point p = e.getPoint();
		if(!component.isInField(p)) return;
		component.requestFocus();
		component.removeSolutionMode();
		int i = component.getNodeIndex(p);
		if(i != component.selectedNode || partitionIndex != component.problem.getPartitioning()[i]) {
			component.selectedNode = i;
			if(drawMode) {
				partitionIndex = component.problem.getPartitioning()[i];
			}
			component.repaint();
		}
		if(drawMode) {
			if(partitionIndex < 0) {
				partitionIndex = getNextIndex();
			}
			setPartition(i, partitionIndex);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if(component.getSudokuModel().isRunning()) return;
		Point p = e.getPoint();
		if(!component.isInField(p)) return;
		component.requestFocus();
		int i = component.getNodeIndex(p);
		if(drawMode) {
			partitionIndex = getNextIndex();
			setPartition(i, partitionIndex);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(component.getSudokuModel().isRunning()) return;
		Point p = e.getPoint();
		if(!component.isInField(p)) return;
		component.requestFocus();
		int i = component.getNodeIndex(p);
		if(drawMode) {
			if(partitionIndex < 0) {
				partitionIndex = getNextIndex();
			}
			setPartition(i, partitionIndex);
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	int getNextIndex() {
		return component.problem.getNextFreePartitionIndex();
	}

	public void setPartition(int p, int c) {
		if(component.problem.getPartitioning()[p] == c) return;
		component.removeSolutionMode();
		component.problem.changePartitioning(p, c);
		partitionIndex = component.problem.getPartitioning()[p];
		component.repaint();
	}

}

