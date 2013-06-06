package com.slava.supaplex;

import java.io.File;

import javax.swing.JFileChooser;

import com.slava.supaplex.model.SupaplexModel;
import com.slava.supaplex.ui.SupaplexFrame;

public class SupaplexRunner {
	public static void main(String[] args) {
		String location = null;
		if(args.length > 0) {
			location = args[0];
		} else {
			location = selectLocation("C:/");
		}
		run(location);
	}
	
	public static String selectLocation(String initial) {
		JFileChooser c = new JFileChooser();
		if(initial != null) {
			c.setCurrentDirectory(new File(initial));
		}
		c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int i = c.showOpenDialog(null);
		if(i != JFileChooser.APPROVE_OPTION) return null;
		File f = c.getSelectedFile();
		return (f == null) ? null : f.getAbsolutePath();
	}
	
	public static void run(String location) {
		if(location == null) return;
		SupaplexModel model = new SupaplexModel();
		model.getLoader().setLocation(location);
		SupaplexFrame frame = new SupaplexFrame();
		frame.setModel(model);
		frame.init();
	}

}
