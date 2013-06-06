package slava.puzzle.pentasets.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import slava.puzzle.pentaletters.gui.PentaLettersComponent;

public class PentaSetsComponent extends PentaLettersComponent {

	protected void paintPentaBorders(Graphics g) {
		doPaintPentaBorders(g);
	}
	
	protected void doKeyReleasedOnSelectedNode(KeyEvent e) {
		if(selectedNode < 0) return;
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			jumpToNeighbourNode(2);
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			jumpToNeighbourNode(0);
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			jumpToNeighbourNode(3);
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			jumpToNeighbourNode(1);
		} else if(e.getKeyChar() == ' ') {
			setLetter(-1);
		} else if(e.getKeyChar() == '1') {
			setLetter(0);
		} else if(e.getKeyChar() == '2') {
			setLetter(1);
		} else if(e.getKeyChar() == '3') {
			setLetter(2);
		} else if(e.getKeyChar() == '4') {
			setLetter(3);
		} else if(e.getKeyChar() == '5') {
			setLetter(4);
		}
	}	

	protected char toChar(int letter) {
		if(letter < 0) return ' ';
		if(letter == 0) return '1'; 
		if(letter == 1) return '2'; 
		if(letter == 2) return '3'; 
		if(letter == 3) return '4'; 
		if(letter == 4) return '5'; 
		return ' ';
	}
	
}
