package slava.puzzles.seabattlef.gui;

import java.awt.event.*;

import slava.puzzles.seabattlef.model.*;

public class SeaBattleFirstPuzzleInput extends KeyAdapter implements SeaBattleFirstConstants {
	static int STATE_A = 4;
	static int STATE_B0 = 0;
	static int STATE_B1 = 1;
	static int STATE_B2 = 2;
	static int STATE_B3 = 3;
	static int STATE_S = 5;
	SeaBattleFirstModel model;
	SeaBattleFirstComponent component;
	
	int position = 0;
	int[] bPosition = new int[4];
	int sPosition = 1;
	
	int state = STATE_A;
	
	public void setModel(SeaBattleFirstModel model) {
		this.model = model;
		validatePosition();
	}
	
	void validatePosition() {
		if(position >= model.getField().getSize()) {
			position = model.getField().getSize() - 1;
		}
		for (int d = 0; d < 4; d++) {
			int mp = (d % 2 == 0) ? model.getField().getHeight() : model.getField().getWidth();
			if(bPosition[d] >= mp) bPosition[d] = mp - 1;
		}
		if(sPosition >= 6) {
			sPosition = 5;
		}
	}
	
	public void setComponent(SeaBattleFirstComponent component) {
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
		} else if(state == STATE_B0 || state == STATE_B2) {
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				bPosition[state]++;
				if(bPosition[state] >= model.getField().getHeight()) bPosition[state] = 0;
				fire();
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				bPosition[state]--;
				if(bPosition[state] < 0) bPosition[state] = model.getField().getHeight() - 1;
				fire();
			} else if(e.getKeyChar() == ' ') {
				if(model.getPuzzleInfo().getBValues(state)[bPosition[state]] < 0) return;
				model.getPuzzleInfo().getBValues(state)[bPosition[state]] = -1;
				fire();
			} else if(e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
				int v = e.getKeyCode() - KeyEvent.VK_0;
				if(model.getPuzzleInfo().getBValues(state)[bPosition[state]] == v) return;
				model.getPuzzleInfo().getBValues(state)[bPosition[state]] = v;
				fire();
			}
		} else if(state == STATE_B1 || state == STATE_B3) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				bPosition[state]++;
				if(bPosition[state] >= model.getField().getWidth()) bPosition[state] = 0;
				fire();
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				bPosition[state]--;
				if(bPosition[state] < 0) bPosition[state] = model.getField().getWidth() - 1;
				fire();
			} else if(e.getKeyChar() == ' ') {
				if(model.getPuzzleInfo().getBValues(state)[bPosition[state]] < 0) return;
				model.getPuzzleInfo().getBValues(state)[bPosition[state]] = -1;
				fire();
			} else if(e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
				int v = e.getKeyCode() - KeyEvent.VK_0;
				if(model.getPuzzleInfo().getBValues(state)[bPosition[state]] == v) return;
				model.getPuzzleInfo().getBValues(state)[bPosition[state]] = v;
				fire();
			}
		} else if(state == STATE_A) {
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
		if(state == STATE_A & position == p) return;
		state = STATE_A;
		position = p;
		fire();
	}
	
	public int getBPosition(int d) {
		return bPosition[d];
	}
	
	public void setBPosition(int p, int d) {
		if(state == d & bPosition[d] == p) return;
		state = d;
		bPosition[d] = p;
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
