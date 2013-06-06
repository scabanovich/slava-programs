package com.slava.supaplex.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import com.slava.supaplex.model.*;

public class SupaplexPalette {
	SupaplexImages images = new SupaplexImages();
	SupaplexModel model;
	JPanel panel;
	DefaultListModel listModel = new DefaultListModel();
	JCheckBox enabling;
	JList list = new JList(listModel);
	
	public SupaplexPalette() {
		init();
	}
	
	public void setModel(SupaplexModel model) {
		this.model = model;
	}

	public JPanel createPanel() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		enabling = new JCheckBox();
		enabling.setText("Enable drawing tool.");
		enabling.setSelected(true);
		panel.add(enabling, BorderLayout.NORTH);
		JScrollPane p = new JScrollPane(list);
		p.setPreferredSize(new Dimension(enabling.getPreferredSize().width, 100));
		panel.add(p, BorderLayout.CENTER);
		list.setCellRenderer(new CR());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Serif", 0, 10));
		for (int i = 0; i < items.length; i++) {
			listModel.addElement(items[i]);
		}		
		enabling.addActionListener(new AL());
		list.addListSelectionListener(new SL());
		panel.setBorder(BorderFactory.createEtchedBorder());
		return panel;
	}
	
	class CR extends DefaultListCellRenderer {
	    public Component getListCellRendererComponent(
            JList list,	Object value, int index, boolean isSelected, boolean cellHasFocus) {
    		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    		PaletteItem item = (PaletteItem)value;
    		setText(item.name);
    		setIcon(item.image);
    		return this;
	     }		
	}
	
	class SL implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			setSelectedToolItem();			
		}
	}
	
	class AL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setSelectedToolItem();			
		}
	}
	
	void setSelectedToolItem() {
		int i = list.getSelectedIndex();
		if(!enabling.isSelected() || i < 0) {
			model.setSelectedToolItem((byte)255, -1);
		} else {
			model.setSelectedToolItem(items[i].b, items[i].gravityMode);
		}
		
	}
	
	class PaletteItem {
		String name;
		ImageIcon image;
		byte b;
		int gravityMode = -1;
	}
	
	PaletteItem[] items;
	
	void init() {
		ArrayList l = new ArrayList();
		addPaletteItem("Empty", images.EMPTY, (byte)0, l);
		addPaletteItem("Zonk", images.ZONK, (byte)1, l);
		addPaletteItem("Base", images.BASE, (byte)2, l);
		addPaletteItem("Murphy", images.MURPHY, (byte)3, l);
		addPaletteItem("Infotron", images.INFOTRON, (byte)4, l);
		addPaletteItem("Chip", images.CHIP, (byte)5, l);
		addPaletteItem("Hardware", images.HARDWARE_06, (byte)6, l);
		addPaletteItem("Exit", images.EXIT, (byte)7, l);
		addPaletteItem("Falling Disk", images.FALLING_DISC, (byte)8, l);
		addPaletteItem("Port right", images.PORT_RIGHT, (byte)9, l);
		addPaletteItem("Port down", images.PORT_DOWN, (byte)10, l);
		addPaletteItem("Port left", images.PORT_LEFT, (byte)11, l);
		addPaletteItem("Port up", images.PORT_UP, (byte)12, l);
		addPaletteItem("Port up-down", images.PORT_UP_DOWN, (byte)21, l);
		addPaletteItem("Port left-right", images.PORT_LEFT_RIGHT, (byte)22, l);
		addPaletteItem("Port all way", images.PORT_ALL_WAY, (byte)23, l);

		addPaletteItem("Snik Snak", images.SNIK_SNAK, (byte)17, l);
		addPaletteItem("Movable Disk", images.MOVABLE_DISC, (byte)18, l);
		addPaletteItem("Termonal", images.TERMINAL, (byte)19, l);
		addPaletteItem("Eatable Disk", images.EATABLE_DISC, (byte)20, l);
		addPaletteItem("Electron", images.ELECTRON, (byte)24, l);
		addPaletteItem("Bug", images.BUG, (byte)25, l);

		addPaletteItem("Port, gravity on", images.PORT_RIGHT, (byte)13, 1, l);
		addPaletteItem("Port, gravity on", images.PORT_DOWN, (byte)14, 1, l);
		addPaletteItem("Port, gravity on", images.PORT_LEFT, (byte)15, 1, l);
		addPaletteItem("Port, gravity on", images.PORT_UP, (byte)16, 1, l);

		addPaletteItem("Port, gravity off", images.PORT_RIGHT, (byte)13, 0, l);
		addPaletteItem("Port, gravity off", images.PORT_DOWN, (byte)14, 0, l);
		addPaletteItem("Port, gravity off", images.PORT_LEFT, (byte)15, 0, l);
		addPaletteItem("Port, gravity off", images.PORT_UP, (byte)16, 0, l);

		addPaletteItem("Chip-2", images.CHIP_1A, (byte)26, l);
		addPaletteItem("Chip-3", images.CHIP_1B, (byte)27, l);
		addPaletteItem("Chip-4", images.CHIP_26, (byte)38, l);
		addPaletteItem("Chip-5", images.CHIP_27, (byte)39, l);

		addPaletteItem("Hardware-2", images.HARDWARE_1C, (byte)28, l);
		addPaletteItem("Hardware-3", images.HARDWARE_1F, (byte)31, l);
		addPaletteItem("Hardware-4", images.HARDWARE_20, (byte)32, l);
		addPaletteItem("Hardware-5", images.HARDWARE_21, (byte)33, l);
		addPaletteItem("Hardware-6", images.HARDWARE_22, (byte)34, l);
		addPaletteItem("Hardware-7", images.HARDWARE_25, (byte)37, l);


		items = (PaletteItem[])l.toArray(new PaletteItem[0]);		
	}
	
	void addPaletteItem(String name, Image image, byte b, ArrayList l) {
		addPaletteItem(name, image, b, -1, l);
	}

	void addPaletteItem(String name, Image image, byte b, int gravityMode, ArrayList l) {
		PaletteItem item = new PaletteItem();
		item.name = name;
		item.image = new ImageIcon(image);
		item.b = b;
		item.gravityMode = gravityMode;
		l.add(item);
	}

}
