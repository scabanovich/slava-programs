package slava.puzzle.cardnet.gui;

import java.awt.event.*;
import slava.puzzle.cardnet.model.*;

public class CardnetGeneratorInfoInputHelper extends KeyAdapter {
	CardnetModel model;
	CardnetComponent component;
	int position = 0;
	CardnetGeneratorInfo generatorInfo;
	
	public CardnetGeneratorInfoInputHelper() {}
	
	public void setModel(CardnetModel model) {
		this.model = model;
		generatorInfo = model.getGeneratorInfo();
	}
	
	public void setComponent(CardnetComponent component) {
		this.component = component;
		component.addKeyListener(this);
	}
	
	public boolean isEnabled() {
		return model != null && model.isSettingGeneratorInfoModeOn();
	}
	
	public CardnetGeneratorInfo getGeneratorInfo() {
		return generatorInfo;
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
		} else if(Character.isLetterOrDigit(e.getKeyChar()) && model.getPuzzleInfo().getFilterValue(position) == 1) {
			String cs = "" + e.getKeyChar();
			for (int n = 0; n < model.getCardSet().getNumberCount(); n++) {
				if(model.getCardSet().getNumberAsString(n).equalsIgnoreCase(cs)) {
					generatorInfo.setNumber(position, n);
					fire();
					return;
				}
			}
			for (int s = 0; s < model.getCardSet().getSuitCount(); s++) {
				if(model.getCardSet().getSuitAsString(s).equalsIgnoreCase(cs)) {
					generatorInfo.setSuit(position, s);
					fire();
					return;
				}
			}
		} else if(e.getKeyCode() == KeyEvent.VK_SPACE && model.getPuzzleInfo().getFilterValue(position) == 1) {
			generatorInfo.setNumber(position, -1);
			generatorInfo.setSuit(position, -1);
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
