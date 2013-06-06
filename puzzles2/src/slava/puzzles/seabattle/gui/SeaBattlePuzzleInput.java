package slava.puzzles.seabattle.gui;

import java.awt.event.*;

import slava.puzzles.seabattle.model.SeaBattleConstants;
import slava.puzzles.seabattle.model.SeaBattleModel;

public class SeaBattlePuzzleInput extends KeyAdapter implements SeaBattleConstants {
	static int STATE_B = 0;
	static int STATE_H = 1;
	static int STATE_V = 2;
	static int STATE_S = 3;
	SeaBattleModel model;
	SeaBattleComponent component;
	
	int position = 0;
	int hPosition = 0;
	int vPosition = 0;
	int sPosition = 0;
	
	int state = STATE_B;
	
	public void setModel(SeaBattleModel model) {
		this.model = model;
		validatePosition();
	}
	
	void validatePosition() {
		if(position >= model.getField().getSize()) {
			position = model.getField().getSize() - 1;
		}
		if(hPosition >= model.getField().getHeight()) {
			hPosition = model.getField().getHeight() - 1;
		}
		if(vPosition >= model.getField().getWidth()) {
			vPosition = model.getField().getWidth() - 1;
		}
		if(sPosition >= 6) {
			sPosition = 5;
		}
	}
	
	public void setComponent(SeaBattleComponent component) {
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
		} else if(state == STATE_H) {
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				hPosition++;
				if(hPosition >= model.getField().getHeight()) hPosition = 0;
				fire();
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				hPosition--;
				if(hPosition < 0) hPosition = model.getField().getHeight() - 1;
				fire();
			} else if(e.getKeyChar() == ' ') {
				if(model.getPuzzleInfo().getHValues()[hPosition] < 0) return;
				model.getPuzzleInfo().getHValues()[hPosition] = -1;
				fire();
			} else if(e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
				int v = e.getKeyCode() - KeyEvent.VK_0;
				if(model.getPuzzleInfo().getHValues()[hPosition] == v) return;
				model.getPuzzleInfo().getHValues()[hPosition] = v;
				fire();
			}
		} else if(state == STATE_V) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				vPosition++;
				if(vPosition >= model.getField().getWidth()) vPosition = 0;
				fire();
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				vPosition--;
				if(vPosition < 0) vPosition = model.getField().getWidth() - 1;
				fire();
			} else if(e.getKeyChar() == ' ') {
				if(model.getPuzzleInfo().getVValues()[vPosition] < 0) return;
				model.getPuzzleInfo().getVValues()[vPosition] = -1;
				fire();
			} else if(e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
				int v = e.getKeyCode() - KeyEvent.VK_0;
				if(model.getPuzzleInfo().getVValues()[vPosition] == v) return;
				model.getPuzzleInfo().getVValues()[vPosition] = v;
				fire();
			}
		} else if(state == STATE_B) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				jump(0);
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				jump(2);
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				jump(1);
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				jump(3);
			} else if(e.getKeyChar() == ' ') {
				model.getPuzzleInfo().getData()[position] = EMPTY;
				fire();
			} else if(e.getKeyChar() == 'e') {
				model.getPuzzleInfo().getData()[position] = EAST_END;
				fire();
			} else if(e.getKeyChar() == 's') {
				model.getPuzzleInfo().getData()[position] = SOUTH_END;
				fire();
			} else if(e.getKeyChar() == 'w') {
				model.getPuzzleInfo().getData()[position] = WEST_END;
				fire();
			} else if(e.getKeyChar() == 'n') {
				model.getPuzzleInfo().getData()[position] = NORTH_END;
				fire();
			} else if(e.getKeyChar() == 'o') {
				model.getPuzzleInfo().getData()[position] = SINGLE;
				fire();
			} else if(e.getKeyChar() == 'm') {
				model.getPuzzleInfo().getData()[position] = MIDDLE;
				fire();
			} else if(e.getKeyChar() == 'a') {
				model.getPuzzleInfo().getData()[position] = WATER;
				fire();
			}
		} else if(state == STATE_S) {
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				sPosition++;
				if(sPosition >= 6) sPosition = 1;
				fire();
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				sPosition--;
				if(sPosition < 1) sPosition = 5;
				fire();
			} else if(e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
				int v = e.getKeyCode() - KeyEvent.VK_0;
				if(model.getPuzzleInfo().getShipCount(sPosition) == v) return;
				model.getPuzzleInfo().setShipCount(sPosition, v);
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
		if(state == STATE_B & position == p) return;
		state = STATE_B;
		position = p;
		fire();
	}
	
	public int getHPosition() {
		return hPosition;
	}
	
	public void setHPosition(int p) {
		if(state == STATE_H & hPosition == p) return;
		state = STATE_H;
		hPosition = p;
		fire();
	}
	
	public int getVPosition() {
		return vPosition;
	}
	
	public void setVPosition(int p) {
		if(state == STATE_V & vPosition == p) return;
		state = STATE_V;
		vPosition = p;
		fire();
	}
	
	public int getSPosition() {
		return sPosition;
	}
	
	public void setSPosition(int p) {
		if(state == STATE_S && sPosition == p) return;
		state = STATE_S;
		sPosition = p;
		fire();
	}
	
	void fire() {
		component.repaint();
	}

}
