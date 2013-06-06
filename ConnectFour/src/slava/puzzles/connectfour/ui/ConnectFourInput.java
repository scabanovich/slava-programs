package slava.puzzles.connectfour.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import slava.puzzles.connectfour.model.ConnectFourModel;

public class ConnectFourInput extends KeyAdapter {
	ConnectFourModel model;
	ConnectFourComponent component;

	public void setModel(ConnectFourModel model) {
		this.model = model;
	}

	public void setComponent(ConnectFourComponent component) {
		this.component = component;
		component.addKeyListener(this);
	}

	boolean isEnabled() {
		return model != null;
	}
	
	void fire() {
		component.repaint();
	}

	public void keyPressed(KeyEvent e) {
		if(!isEnabled()) return;
		if(!model.isRunning()) {
			if(e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
				int x = e.getKeyCode() - KeyEvent.VK_0;
				if(x < model.getField().getWidth() && model.canMove(x)) {
					model.move(x);
					component.repaint();
				}
			} else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				if(model.canBack()) {
					model.back();
					component.repaint();
				}
			}
		}
	}
}
