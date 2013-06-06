package com.slava.supaplex.ui;

import java.awt.*;
import javax.swing.*;
import com.slava.supaplex.model.*;
import com.slava.supaplex.ui.dnd.SupaplexLevelDnd;

public class SupaplexLevelList {
	SupaplexModel model;
	JPanel panel;
	DefaultListModel listModel = new DefaultListModel();
	JList list = new JList(listModel);
	
	SupaplexLevelDnd dnd = new SupaplexLevelDnd(this);
	
	public void setModel(SupaplexModel model) {
		this.model = model;
	}
	
	public SupaplexModel getModel() {
		return model;
	}
	
	public JList getList() {
		return list;
	}

	public JPanel createPanel() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JScrollPane p = new JScrollPane(list);
		p.setPreferredSize(new Dimension(200, 500));
		panel.add(p, BorderLayout.CENTER);
		list.setCellRenderer(new CR());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Serif", 0, 10));
		dnd.activate();
		update();
		return panel;
	}
	
	public void update() {
		SupaplexLevel[] levels = model.getLevels();
		for (int i = 0; i < levels.length; i++) {
			if(listModel.size() <= i) {
				listModel.addElement(levels[i]);
			} else if(listModel.get(i) != levels[i]){
				listModel.set(i, levels[i]);
			}
		}
	}
	
	class CR extends DefaultListCellRenderer {
	    public Component getListCellRendererComponent(
            JList list,	Object value, int index, boolean isSelected, boolean cellHasFocus) {
    		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    		SupaplexLevel level = (SupaplexLevel)value;
    		String t = "" + (index + 1) + ". ";
    		while(t.length() < 5) t = " " + t;
    		setText(t + level.getName());
    		return this;
	     }		
	}
	
	public int getIndex(Point p) {
		for (int i = 0; i < listModel.size(); i++) {
			Rectangle r = list.getCellBounds(i, i);
			if(r.y < p.y && r.y + r.height > p.y) return i;
		}
		return -1;
	}

}
