package slava.puzzle.template.action;

import java.awt.event.*;
import javax.swing.JOptionPane;
import slava.puzzle.template.model.PuzzleModel;

public class PuzzleActionSolve extends PuzzleAction {
	
	public boolean isEnabled() {
		return manager != null && !manager.model.isRunning() && getClass() != PuzzleActionSolve.class;
	}

	public void actionPerformed(ActionEvent e) {
		PuzzleModel model = manager.getModel();
		if(model.isRunning()) return;
		model.setRunning(true);
		model.setSolutionInfo(null);
		manager.component.repaint();
		manager.setThread(new R());
		manager.getThread().start();
	}
	
	class R extends Thread {
		
		public void run() {
			try {
				execute();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(manager.component, e.getMessage());
				e.printStackTrace();
			} catch (ThreadDeath e) {
				
			} finally {
				manager.model.setRunning(false);
				manager.component.repaint();			
			}
		}
	}
	
	protected void execute() {
	}

}
