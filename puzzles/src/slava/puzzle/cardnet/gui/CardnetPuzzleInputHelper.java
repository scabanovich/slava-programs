package slava.puzzle.cardnet.gui;

import java.awt.event.*;
import slava.puzzle.cardnet.model.*;

public class CardnetPuzzleInputHelper extends KeyAdapter {
	CardnetModel model;
	CardnetComponent component;
	
	boolean isSettingNumber = true;
	boolean isSettingHLine = true;
	int rowIndex = 0;
	
	public CardnetPuzzleInputHelper() {}
	
	public void setModel(CardnetModel model) {
		this.model = model;
	}
	
	public void setComponent(CardnetComponent component) {
		this.component = component;
		component.addKeyListener(this);
	}
	
	boolean isEnabled() {
		return model != null && model.isSettingPuzzleModeOn();
	}

	public void keyPressed(KeyEvent e) {
		if(!isEnabled()) return;
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right();
			component.repaint(); 
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			left();
			component.repaint(); 
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			up();
			component.repaint(); 
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			down();
			component.repaint(); 
		} else if(isSettingNumber) {
			int n = model.getCardSet().getNumber(e.getKeyChar());
			int[] ns = null;
			if(n < 0) {
			} else if(isSettingHLine) {
				ns = model.getPuzzleInfo().getHNumbers(rowIndex);
				ns[n] = 1 - ns[n];
				model.getPuzzleInfo().setHNumberSet(rowIndex, hasValues(ns));
				component.repaint(); 
			} else {
				ns = model.getPuzzleInfo().getVNumbers(rowIndex);
				ns[n] = 1 - ns[n];
				model.getPuzzleInfo().setVNumberSet(rowIndex, hasValues(ns));
				component.repaint(); 
			}
		} else {
			int n = model.getCardSet().getSuit(e.getKeyChar());
			int[] ns = null;
			if(n < 0) {
			} else if(isSettingHLine) {
				ns = model.getPuzzleInfo().getHSuits(rowIndex);
				ns[n] = 1 - ns[n];
				model.getPuzzleInfo().setHSuitSet(rowIndex, hasValues(ns));
				component.repaint(); 
			} else {
				ns = model.getPuzzleInfo().getVSuits(rowIndex);
				ns[n] = 1 - ns[n];
				model.getPuzzleInfo().setVSuitSet(rowIndex, hasValues(ns));
				component.repaint(); 
			}
		}
	}
	
	boolean hasValues(int[] ns) {
		for (int i = 0; i < ns.length; i++) if(ns[i] == 1) return true;
		return false;
	}
	
	void right() {
		if(isSettingHLine && isSettingNumber) {
			isSettingNumber = false;
		} else if(!isSettingHLine && isSettingNumber) {
			rowIndex++;
			if(rowIndex == model.getField().getWidth()) {
				rowIndex = 0;
				isSettingHLine = true;
				isSettingNumber = false;
			}
		} else if(!isSettingHLine && !isSettingNumber) {
			rowIndex++;
			if(rowIndex == model.getField().getWidth()) {
				rowIndex = model.getField().getHeight() - 1;
				isSettingHLine = true;
				isSettingNumber = false;
			}
		}
	}
	
	void left() {
		if(isSettingHLine && !isSettingNumber) {
			isSettingNumber = true;
		} else if(!isSettingHLine && isSettingNumber) {
			rowIndex--;
			if(rowIndex < 0) {
				rowIndex = 0;
				isSettingHLine = true;
				isSettingNumber = true;
			}
		} else if(!isSettingHLine && !isSettingNumber) {
			rowIndex--;
			if(rowIndex < 0) {
				rowIndex = model.getField().getHeight() - 1;
				isSettingHLine = true;
				isSettingNumber = true;
			}
		}
	}

	void up() {
		if(isSettingHLine && isSettingNumber) {
			rowIndex--;
			if(rowIndex < 0) {
				rowIndex = 0;
				isSettingHLine = false;
				isSettingNumber = true;
			}
		} else if(isSettingHLine && !isSettingNumber) {
			rowIndex--;
			if(rowIndex < 0) {
				rowIndex = model.getField().getWidth() - 1;
				isSettingHLine = false;
				isSettingNumber = true;
			}
		} else if(!isSettingHLine && !isSettingNumber) {
			isSettingNumber = true;
		}
	}

	void down() {
		if(isSettingHLine && isSettingNumber) {
			rowIndex++;
			if(rowIndex == model.getField().getHeight()) {
				rowIndex = 0;
				isSettingHLine = false;
				isSettingNumber = false;
			}
		} else if(isSettingHLine && !isSettingNumber) {
			rowIndex++;
			if(rowIndex == model.getField().getHeight()) {
				rowIndex = model.getField().getWidth() - 1;
				isSettingHLine = false;
				isSettingNumber = false;
			}
		} else if(!isSettingHLine && isSettingNumber) {
			isSettingNumber = false;
		}
	}

}
