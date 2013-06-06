package slava.puzzle.avoidthree.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import slava.puzzle.avoidthree.model.AvoidThreeAndKnightModel;
import slava.puzzle.avoidthree.model.AvoidThreeAndKnightPuzzle;

public class AvoidThreeAndKnightPuzzleInput extends KeyAdapter {
	AvoidThreeAndKnightModel model;
	AvoidThreeAndKnightComponent component;
	int position = 0;

	public AvoidThreeAndKnightPuzzleInput() {}

	public void setModel(AvoidThreeAndKnightModel model) {
		this.model = model;		
	}

	public void setComponent(AvoidThreeAndKnightComponent component) {
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
		} else if(e.getKeyChar() >= 'a' && e.getKeyChar() <= 'd' && model.getPuzzleInfo().isField(position) && model.getPuzzleInfo().getValue(position) == AvoidThreeAndKnightPuzzle.EMPTY_VALUE) {
			int v = e.getKeyCode() - ((int)'A') + AvoidThreeAndKnightPuzzle.A_VALUE;
			if(v > model.getPuzzleInfo().getMaxValue()) return;
			model.getPuzzleInfo().doMove(position, v);
			fire();
		} else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && model.getPuzzleInfo().getMoveCount() > 0) {
			model.getPuzzleInfo().undoMove();
			fire();
		}
	}

	void jump(int d) {
		int p = model.getField().jump(position, d);
		setPosition(p);
	}
	
	void setPosition(int p) {
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
