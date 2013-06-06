package slava.puzzles.hexa.fence.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import slava.puzzles.hexa.fence.model.HexaFenceConstants;
import slava.puzzles.hexa.fence.model.HexaFenceModel;

public class HexaFencePuzzleInput extends KeyAdapter implements HexaFenceConstants {
	HexaFenceModel model;
	HexaFenceComponent component;
	int fieldCursor = 0;

	public HexaFencePuzzleInput() {}

	public void setModel(HexaFenceModel model) {
		this.model = model;
	}

	public void setComponent(HexaFenceComponent component) {
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
				setPuzzleData(-2);
			} else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				setPuzzleData(NO_VALUE);
			} else if(e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_6) {
				setPuzzleData(e.getKeyCode() - KeyEvent.VK_0);
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
	
	void setPuzzleData(int v) {
		if(!model.getField().isInField(fieldCursor)) return;
		if(v > -2) {
			model.getPuzzleInfo().getForm()[fieldCursor] = 1;
			model.getPuzzleInfo().getData()[fieldCursor] = v;
		} else {
			model.getPuzzleInfo().getForm()[fieldCursor] = 0;
			model.getPuzzleInfo().getData()[fieldCursor] = NO_VALUE;
		}
		fire();
	}
	
}
