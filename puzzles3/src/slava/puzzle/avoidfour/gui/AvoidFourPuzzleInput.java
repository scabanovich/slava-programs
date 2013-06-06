package slava.puzzle.avoidfour.gui;

import java.awt.event.*;

import slava.puzzle.avoidfour.model.*;

public class AvoidFourPuzzleInput extends KeyAdapter {
	AvoidFourModel model;
	AvoidFourComponent component;
	int position = 0;
	
	public void setModel(AvoidFourModel model) {
		this.model = model;
	}
	
	public void setComponent(AvoidFourComponent component) {
		this.component = component;
		component.addKeyListener(this);
	}
	
	boolean isEnabled() {
		return model != null && model.isSettingPuzzleMode();
	}

	public void keyPressed(KeyEvent e) {
		if(!isEnabled()) return;
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			jump(0);
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			jump(4);
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			jump(2);
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			jump(6);
		} else if(e.getKeyChar() == 'x' && model.getPuzzleInfo().isField(position) && model.getPuzzleInfo().getValue(position) == AvoidFourPuzzle.EMPTY_VALUE) {
			model.getPuzzleInfo().doMove(position, AvoidFourPuzzle.CROSS_VALUE);
			fire();
		} else if(e.getKeyChar() == 'o' && model.getPuzzleInfo().isField(position) && model.getPuzzleInfo().getValue(position) == AvoidFourPuzzle.EMPTY_VALUE) {
			model.getPuzzleInfo().doMove(position, AvoidFourPuzzle.OSIGN_VALUE);
			fire();
		} else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && model.getPuzzleInfo().getMoveCount() > 0) {
			model.getPuzzleInfo().undoMove();
			fire();
		}
	}

	void jump(int d) {
		int p = model.getField().jp(d, position);
		if(p >= 0) {
			position = p;
			fire();
		}
	}
	
	public int getPosition() {
		return position;
	}
	
	void fire() {
		component.repaint();
	}

}
