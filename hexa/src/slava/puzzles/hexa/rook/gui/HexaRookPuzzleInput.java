package slava.puzzles.hexa.rook.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import slava.puzzles.hexa.rook.model.HexaRookConstants;
import slava.puzzles.hexa.rook.model.HexaRookModel;

public class HexaRookPuzzleInput extends KeyAdapter implements HexaRookConstants {
	HexaRookModel model;
	HexaRookComponent component;
	int fieldCursor = 0;

	public HexaRookPuzzleInput() {}
	
	public void setModel(HexaRookModel model) {
		this.model = model;
	}
	
	public void setComponent(HexaRookComponent component) {
		this.component = component;
		component.addKeyListener(this);
	}

	boolean isEnabled() {
		return model != null;
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
		} else if(model.getField().isInField(fieldCursor)) {
			if(e.getKeyCode() == KeyEvent.VK_DELETE) {
				setPuzzlePosition(NOT_POSITION);
			} else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				setPuzzlePosition(SOME_POSITION);
			} else if(e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_9) {
				setPuzzlePosition(e.getKeyCode() - KeyEvent.VK_0);
			} else if(e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z) {
				setPuzzlePosition(e.getKeyCode() - KeyEvent.VK_A + 1);
			} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				int q = model.getField().jump(fieldCursor, 0);
				if(q >= 0) setFieldCursor(q);
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				int q = model.getField().jump(fieldCursor, 3);
				if(q >= 0) setFieldCursor(q);
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				int q = model.getField().jump(fieldCursor, 2);
				if(q < 0) q = model.getField().jump(fieldCursor, 1);
				if(q >= 0) setFieldCursor(q);
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				int q = model.getField().jump(fieldCursor, 5);
				if(q < 0) q = model.getField().jump(fieldCursor, 4);
				if(q >= 0) setFieldCursor(q);
			}
		}		
	}

	void fire() {
		component.repaint();
	}

	public int getFieldCursor() {
		return fieldCursor;
	}
	
	public void setFieldCursor(int p) {
		fieldCursor = p;
		fire();
	}
	
	void setPuzzlePosition(int v) {
		if(!model.getField().isInField(fieldCursor)) return;
		model.getPuzzleInfo().getPositions()[fieldCursor] = v;
		fire();
	}
	
	
}
