package com.slava.supaplex.ui.action;

import java.awt.event.*;

import javax.swing.*;

import com.slava.supaplex.SupaplexRunner;
import com.slava.supaplex.model.SupaplexModel;

public class ActionManager {
	JMenuBar bar = new JMenuBar();
	SupaplexModel model;
	
	public void setModel(SupaplexModel model) {
		this.model = model;
		init();
	}

	public void init() {
		JMenu menu = new JMenu("File");
		bar.add(menu);

		JMenuItem item = new JMenuItem("Open");
		item.setActionCommand("open");
		item.addActionListener(new AL());
		menu.add(item);

		item = new JMenuItem("Save");
		item.setActionCommand("save");
		item.addActionListener(new AL());
		menu.add(item);
		
		menu = new JMenu("Edit");
		bar.add(menu);
		
		JMenu menu1 = new JMenu("Add New Level");
		menu.add(menu1);
		item = new JMenuItem("to the end");
		item.setActionCommand("add level");
		item.addActionListener(new AL());
		menu1.add(item);
		
		item = new JMenuItem("before selection");
		item.setActionCommand("insert level");
		item.addActionListener(new AL());
		menu1.add(item);

		item = new JMenuItem("Remove level");
		item.setActionCommand("remove level");
		item.addActionListener(new AL());
		menu.add(item);
		menu.add(new JSeparator());

		item = new JMenuItem("Fill");
		item.setActionCommand("fill");
		item.addActionListener(new AL());
		menu.add(item);
	}
	
	public JComponent getMenuBar() {
		return bar;
	}
	
	class AL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			String error = null;
			if(command.equals("save")) {
				model.getLoader().save();
			} else if(command.equals("add level")) {
				String s = JOptionPane.showInputDialog(bar, "Name", "");
				if(s == null) return;
				model.addLevel(s);
			} else if(command.equals("insert level")) {
				String s = JOptionPane.showInputDialog(bar, "Name", "");
				if(s == null) return;
				model.insertLevel(s);
			} else if(command.equals("remove level")) {
				model.removeLevel();
			} else if(command.equals("fill")) {
				error = model.paintAllWithSelection();
			} else if(command.equals("open")) {
				String location = SupaplexRunner.selectLocation(model.getLoader().getLocation());
				if(location != null) SupaplexRunner.run(location);
			}
			if(error != null) {
				JOptionPane.showMessageDialog(bar, error, "Warning", JOptionPane.ERROR_MESSAGE);
				
			}
		}		
	}
	
}
