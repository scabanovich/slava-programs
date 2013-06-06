package slava.puzzle.template.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import slava.puzzle.template.action.*;
import slava.puzzle.template.model.*;

public class PuzzleFrame {
	protected JFrame frame = new JFrame("");
	protected PuzzleModel model;
	protected PuzzleActionManager manager = new PuzzleActionManager();
	protected PuzzleMenuBar menuBar = new PuzzleMenuBar();
	
	public PuzzleFrame() {
		init();
	}

	public void run() {
		frame.pack();
		center(frame);
		frame.setVisible(true);		 
	}
	
	private void init() {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	public void setModel(PuzzleModel model) {
		this.model = model;
		manager.setModel(model);
		menuBar.init(manager);
	}
	
	public PuzzleMenuBar getMenuBar() {
		return menuBar;
	}
	
	public void setComponent(PuzzleComponent component) {
		component.setModel(model);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(menuBar.getComponent(), BorderLayout.NORTH);
		frame.getContentPane().add(component);
		manager.setComponent(component);
	}
	
	public PuzzleActionManager getActionManager() {
		return manager;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public static void center(Window window) {
		Dimension dw = window.getSize();
		Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation((ds.width - dw.width) / 2, (ds.height - dw.height) / 2);
	}

	public static void main(String[] args) {
		PuzzleFrame frame = new PuzzleFrame();
		frame.getFrame().setTitle("Puzzle");
		PuzzleModel model = new PuzzleModel();
		PuzzleLoader loader = new PuzzleLoader();
		loader.setRoot("/data/template");
		loader.initName("template");
		model.setLoader(loader);
		PuzzleActionManager manager = frame.getActionManager();
		manager.initActions();
		//configureManager... 
		frame.setModel(model);
		frame.setComponent(new PuzzleComponent());
		frame.run();
	}

}
