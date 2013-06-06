package slava.puzzle.paths.nook.gui;

import java.awt.event.*;
import slava.puzzle.paths.nook.model.*;

public class NookPathsPuzzleInput extends KeyAdapter {
	static int STATE_A = 4;
	NookPathsModel model;
	NookPathsComponent component;
	
	int position = 0;
	
	int state = STATE_A;
	
	public NookPathsPuzzleInput() {
		
	}
	
	public void setModel(NookPathsModel model) {
		this.model = model;
		validatePosition();
	}
	
	void validatePosition() {
		if(position >= model.getField().getSize()) {
			position = model.getField().getSize() - 1;
		}
	}
	
	public void setComponent(NookPathsComponent component) {
		this.component = component;
		component.addKeyListener(this);
	}
	
	boolean isEnabled() {
		return model != null;
	}
	
	public int getState() {
		return state;
	}

	public void keyPressed(KeyEvent e) {
		if(!isEnabled()) return;
		if(model.isShowingSolution()) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				model.nextSolution();
				fire();
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				model.prevSolution();
				fire();
			} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				model.clearSolutions();
				fire();
			}
			return;
		} else if(state == STATE_A) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				jump(0);
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				jump(2);
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				jump(1);
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				jump(3);
			} else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
				model.getPuzzleInfo().getFilter()[position] = 1;
				model.getPuzzleInfo().getData()[position] = 0;
				fire();
			} else if(e.getKeyChar() == ' ') {
				model.getPuzzleInfo().getFilter()[position] = 0;
				model.getPuzzleInfo().getData()[position] = 0;
				fire();
			} else if(e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
				model.getPuzzleInfo().getFilter()[position] = 0;
				int n = e.getKeyCode() - KeyEvent.VK_0;
				int[] data = model.getPuzzleInfo().getData();
				data[position] = data[position] * 10 + n;
				if(data[position] > data.length) {
					data[position] = data[position] % 100;
				}
				if(data[position] > data.length) {
					data[position] = data[position] % 10;
				}
				fire();
			}
		}
	}

	void jump(int d) {
		int p = model.getField().jump(position, d);
		if(p >= 0) {
			position = p;
			fire();
		}
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int p) {
		if(state == STATE_A & position == p) return;
		state = STATE_A;
		position = p;
		fire();
	}
	
	void fire() {
		component.repaint();
	}

}
