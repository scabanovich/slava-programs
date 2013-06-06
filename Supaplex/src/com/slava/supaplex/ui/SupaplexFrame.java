package com.slava.supaplex.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.slava.supaplex.model.SupaplexModel;
import com.slava.supaplex.ui.action.*;

public class SupaplexFrame {
	static int frameCount = 0;
	SupaplexModel model;	
	JFrame frame;
	ActionManager actionManager;
	SupaplexComponent component;
	
	public void setModel(SupaplexModel model) {
		this.model = model;
	}

	public void init() {
		frame = new JFrame("Supapplex - " + model.getLoader().getLocation());
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	try {
		    		frameCount--;
		    		if(frameCount == 0) System.exit(0);
		    	} catch (Exception exc) {		    		
		    	}
		    }
		});
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		
		pane.add(createToolsPanel(), BorderLayout.NORTH);
		component = new SupaplexComponent();
		component.setModel(model);

		pane.add(component.createPanel(), BorderLayout.CENTER);
		frame.setContentPane(pane);
		frame.pack();
		toScreenCenter(frame, new Dimension(frameCount * 30, frameCount * 30));
		frame.setVisible(true);
		frameCount++;
	}
	
	JComponent createToolsPanel() {
		actionManager = new ActionManager();
		actionManager.setModel(model);
		return actionManager.getMenuBar();
	}
	
	public void updateFrame() {
		frame.setTitle("Supapplex - " + model.getLoader().getLocation());
	}
	
	public static void toScreenCenter(Component component, Dimension move) {
		Rectangle bounds = component.getBounds();
		int componentWidth = bounds.width;
		int componentHeight = bounds.height;
		Dimension screenSize = getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		component.setBounds(
			(screenWidth-componentWidth)/2 + move.width, 
			(screenHeight-componentHeight)/2 + move.height,
				componentWidth,	componentHeight);
	}

	static public Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

}
