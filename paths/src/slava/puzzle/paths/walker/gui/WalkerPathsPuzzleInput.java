package slava.puzzle.paths.walker.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import slava.puzzle.paths.walker.model.WalkerPathsModel;

public class WalkerPathsPuzzleInput extends KeyAdapter {
	static int STATE_A = 4;
	WalkerPathsModel model;
	WalkerPathsComponent component;
	int position = 0;
	int state = STATE_A;

	public WalkerPathsPuzzleInput() {}

	public void setModel(WalkerPathsModel model) {
		this.model = model;
	}
	
	public void setComponent(WalkerPathsComponent component) {
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
				int[][] parts = model.getPuzzleInfo().getParts();
				int[] ds = parts[position];
				for (int d = 0; d < 4; d++) {
					int db = (d + 2) % 4;
					ds[d] = -1;
					int q = model.getField().jump(position, d);
					if(q >= 0) parts[q][db] = -1;
				}
				model.getPuzzleInfo().getTurns()[position] = 0;
				fire();
			} else if(e.getKeyChar() == ' ') {
				model.getPuzzleInfo().getFilter()[position] = 0;
				fire();
			} else if(e.getKeyChar() == KeyEvent.VK_R) {
				shiftPart(0);
			} else if(e.getKeyChar() == KeyEvent.VK_D) {
				shiftPart(1);
			} else if(e.getKeyChar() == KeyEvent.VK_L) {
				shiftPart(2);
			} else if(e.getKeyChar() == KeyEvent.VK_U) {
				shiftPart(3);
			} else if(e.getKeyChar() == KeyEvent.VK_T) {
				shiftTurn();
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
	
	void shiftPart(int d) {
		if(position < 0) return;
		int[] filter = model.getPuzzleInfo().getFilter();
		if(filter[position] == 1) return;
		int[][] parts = model.getPuzzleInfo().getParts();
		int q = model.getField().jump(position, d);
		if(q < 0 || filter[q] == 1) return;
		int db = (d + 2) % 4;
		parts[position][d]++;
		if(parts[position][d] > 1) parts[position][d] = -1;
		parts[q][db] = parts[position][d];
		fire();
	}
	
	void shiftTurn() {
		if(position < 0) return;
		int[] filter = model.getPuzzleInfo().getFilter();
		if(filter[position] == 1) return;
		int[] turns = model.getPuzzleInfo().getTurns();
		turns[position]++;
		if(turns[position] > 2) turns[position] = 0;
		fire();
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
