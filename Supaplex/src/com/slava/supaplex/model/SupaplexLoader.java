package com.slava.supaplex.model;

import java.io.*;
import java.util.*;

public class SupaplexLoader {
	static String LEVELS_DAT_FILE = "/levels.dat";
	static String LEVELS_LST_FILE = "/level.lst";
	SupaplexModel model;
	String location = null;
	String loadError = null;
	
	public void setModel(SupaplexModel model) {
		this.model = model;
	}
	
	public void setLocation(String location) {
		this.location = location;
		load();
	}
	
	public String getLocation() {
		return location;
	}
	
	public boolean modelExists() {
		if(location == null) return false;
		File folder = new File(location);
		if(!folder.isDirectory()) return false;
		File dat = new File(folder, LEVELS_DAT_FILE);
		return dat.isFile();
	}
	
	public void load() {
		if(!modelExists()) {
			loadError = "Cannot find " + LEVELS_DAT_FILE + ".";
			return;
		}
		File folder = new File(location);
		File dat = new File(folder, LEVELS_DAT_FILE);
		//File lst = new File(folder, LEVELS_LST_FILE);
		if(!dat.isFile()) return;
		ArrayList levels = new ArrayList();
		try {
			FileInputStream reader = new FileInputStream(dat);
			long fileLength = dat.length();
			int length = model.getField().getSize() + 96;
			int readLength = 0;
			byte[] buffer = new byte[length];
			while(fileLength - readLength >= length) {
				int toRead = length;
				while(toRead > 0) {
					toRead -= reader.read(buffer, length - toRead, toRead);
				}
				readLength += length;
				SupaplexLevel level = new SupaplexLevel();
				level.setModel(model);
				level.loadFromBytes(buffer);
				levels.add(level);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SupaplexLevel[] ls = (SupaplexLevel[])levels.toArray(new SupaplexLevel[0]);
		model.setLevels(ls);
	}
	
	public void save() {
		if(location == null) return;
		File folder = new File(location);
		File dat = new File(folder, LEVELS_DAT_FILE);
		File lst = new File(folder, LEVELS_LST_FILE);
		SupaplexLevel[] ls = model.getLevels();
		int levelLength = model.getField().getSize() + 96;
		byte[] bs = new byte[ls.length * levelLength];
		for (int i = 0; i < ls.length; i++) {
			byte[] lb = ls[i].getBytes();
			System.arraycopy(lb, 0, bs, levelLength * i, levelLength);
		}
		write(dat, bs);
		bs = new byte[28 * ls.length];
		for (int i = 0; i < ls.length; i++) {
		   String n = "" + (i + 1);
		   while(n.length() < 3) n = "0" + n;
		   n += ' ';
		   n += "-" + ls[i].getFullName() + "-";
		   n += (char)10;
		   byte[] b = n.getBytes();
		   System.arraycopy(b, 0, bs, 28 * i, 28);
		}
		write(lst, bs);		
	}
	
	private void write(File f, byte[] bs) {
		try {
			FileOutputStream o = new FileOutputStream(f);
			o.write(bs, 0, bs.length);
			o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
