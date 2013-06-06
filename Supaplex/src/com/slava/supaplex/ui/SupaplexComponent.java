package com.slava.supaplex.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Properties;

import javax.swing.*;
import javax.swing.event.*;
import com.slava.supaplex.model.*;

public class SupaplexComponent {	
	SupaplexLevelList list = new SupaplexLevelList();
	SupaplexLevelComponent levelComponent = new SupaplexLevelComponent();
	SupaplexLevelOptions levelOptions = new SupaplexLevelOptions();
	SupaplexPalette palette = new SupaplexPalette();
	SupaplexModel model;
	JPanel panel;
	
	public void setModel(SupaplexModel model) {
		this.model = model;
		list.setModel(model);
		levelComponent.setModel(model);
		palette.setModel(model);
		model.addListener(new SML());
	}
	
	public JPanel createPanel() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(list.createPanel(), BorderLayout.WEST);
		list.list.addListSelectionListener(new LSL());
		panel.add(createLevelPanel(), BorderLayout.CENTER);
		return panel;
	}
	
	JPanel createLevelPanel() {
		JPanel p = new JPanel(new BorderLayout());
		JScrollPane sp = new JScrollPane(levelComponent);
		sp.setPreferredSize(new Dimension(400, 300));
		p.add(sp, BorderLayout.CENTER);
		JPanel p1 = new JPanel(new BorderLayout());
		p.add(p1, BorderLayout.SOUTH);
		JPanel p2 = new JPanel(new BorderLayout());
		JComponent oc = levelOptions.createPanel();
		p2.add(oc, BorderLayout.NORTH);
		p2.add(new JPanel(), BorderLayout.CENTER);
		p1.add(p2, BorderLayout.CENTER);
		JComponent pc = palette.createPanel();
		p1.add(pc, BorderLayout.EAST);
		return p;
	}
	
	class LSL implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if(e.getValueIsAdjusting()) return;
			onSelectionChanged();
		}		
	}
	
	void onSelectionChanged() {
		int i = list.list.getSelectedIndex();
		SupaplexLevel level = model.getLevel(i);
		levelOptions.setLevel(level);
		levelComponent.setLevel(level);
		model.setSelectedLevelIndex(i);
	}
	
	class SML implements SupaplexModelListener {
		public void modelChanged(Properties p) {
			String kind = p.getProperty("kind");
			if("levelAdded".equals(kind)) {
				processLevelAdded((SupaplexLevel)p.get("level"));
			} else if("levelRenamed".equals(kind)) {
				list.list.validate();
				list.list.repaint();
			} else if("levelInserted".equals(kind)) {
				processLevelInserted((SupaplexLevel)p.get("level"), ((Integer)p.get("index")).intValue());
			} else if("levelRemoved".equals(kind)) {
				processLevelRemoved((SupaplexLevel)p.get("level"), ((Integer)p.get("index")).intValue());
			} else if("fill".equals(kind)) {
				levelComponent.repaint();
			}
		}
	}
	
	void processLevelAdded(SupaplexLevel level) {
		list.listModel.addElement(level);
		list.list.setSelectedValue(level, true);
		onSelectionChanged();
	}

	void processLevelInserted(SupaplexLevel level, int index) {
		list.listModel.insertElementAt(level, index);
		list.list.setSelectedValue(level, true);
		onSelectionChanged();
	}

	void processLevelRemoved(SupaplexLevel level, int index) {
		list.listModel.removeElement(level);
		list.list.setSelectedValue(level, true);
		if(index >= list.listModel.size()) index--;
		if(index >= 0) {
			list.list.setSelectedIndex(index);
		}
	}

}
