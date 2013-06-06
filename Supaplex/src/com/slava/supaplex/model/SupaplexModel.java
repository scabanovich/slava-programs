package com.slava.supaplex.model;

import java.util.Properties;

public class SupaplexModel {
	SupaplexLoader loader = new SupaplexLoader();
	SupaplexField field = new SupaplexField();
	SupaplexLevel[] levels = new SupaplexLevel[0];
	int selectedLevel = -1;
	byte selectedToolItem = (byte)255;
	int selectedGravityMode = -1;
	SupaplexModelListener[] listeners = new SupaplexModelListener[0];
	
	public SupaplexModel() {
		loader.setModel(this);		
	}
	
	public void addListener(SupaplexModelListener l) {
		SupaplexModelListener[] ls = new SupaplexModelListener[listeners.length + 1];
		System.arraycopy(listeners, 0, ls, 0, listeners.length);
		ls[listeners.length] = l;
		listeners = ls;
	}
	
	public SupaplexField getField() {
		return field;
	}
	
	public int getSelectedLevelIndex() {
		return selectedLevel;
	}
	
	public void setSelectedLevelIndex(int i) {
		if(i >= levels.length) i = -1;
		selectedLevel = i;
	}

	public SupaplexLevel getLevel(int index) {
		return (index < 0 || index >= levels.length) ? null : levels[index];
	}
	
	public SupaplexLoader getLoader() {
		return loader;
	}
	
	void setLevels(SupaplexLevel[] levels) {
		this.levels = levels;
		if(getLevel(getSelectedLevelIndex()) == null) {
			selectedLevel = -1;
		}
	}
	
	public SupaplexLevel[] getLevels() {
		return levels;
	}
	
	public void setSelectedToolItem(byte b, int gravityMode) {
		selectedToolItem = b;
		selectedGravityMode = gravityMode;
	}
	
	public void applySelectedToolItem(int place) {
		if(field.isOnBorder(place)) return;
		if(selectedToolItem == (byte)255) return;
		SupaplexLevel l = getLevel(getSelectedLevelIndex());
		if(l == null) return;
		l.applySelectedToolItem(place, selectedToolItem, selectedGravityMode);
	}
	
	public void addLevel(String name) {
		SupaplexLevel level = new SupaplexLevel();
		level.setModel(this);
		level.setName(name);
		addLevel(level);
	}
	
	public void insertLevel(String name) {
		int s = getSelectedLevelIndex();
		if(s < 0) {
			addLevel(name);
			return;
		}
		SupaplexLevel level = new SupaplexLevel();
		level.setModel(this);
		level.setName(name);
		insertLevel(level, s);
	}
	
	public void addLevel(SupaplexLevel level) {
		SupaplexLevel[] ls = new SupaplexLevel[levels.length + 1];
		System.arraycopy(levels, 0, ls, 0, levels.length);
		ls[levels.length] = level;
		levels = ls;
		fireLevelAdded(level);		
	}
	
	public void insertLevel(SupaplexLevel level, int index) {
		SupaplexLevel[] ls = new SupaplexLevel[levels.length + 1];
		System.arraycopy(levels, 0, ls, 0, index);
		ls[index] = level;
		System.arraycopy(levels, index, ls, index + 1, levels.length - index);
		levels = ls;
		fireLevelInserted(level, index);		
	}
	
	void fireLevelAdded(SupaplexLevel level) {
		Properties p = new Properties();
		p.setProperty("kind", "levelAdded");
		p.put("level", level);
		fire(p);
	}
	
	void fireLevelInserted(SupaplexLevel level, int index) {
		Properties p = new Properties();
		p.setProperty("kind", "levelInserted");
		p.put("level", level);
		p.put("index", new Integer(index));
		fire(p);
	}
	
	public void fire(Properties p) {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].modelChanged(p);
		}
	}
	
	public void removeLevel() {
		int s = getSelectedLevelIndex();
		if(s < 0) return;
		SupaplexLevel[] ls = new SupaplexLevel[levels.length - 1];
		SupaplexLevel level = levels[s];
		System.arraycopy(levels, 0, ls, 0, s);
		System.arraycopy(levels, s + 1, ls, s, levels.length - s - 1);
		levels = ls;
		fireLevelRemoved(level, s);		
	}
	
	void fireLevelRemoved(SupaplexLevel level, int index) {
		Properties p = new Properties();
		p.setProperty("kind", "levelRemoved");
		p.put("level", level);
		p.put("index", new Integer(index));
		fire(p);
	}
	
	public String paintAllWithSelection() {
		if(selectedToolItem < 0) {
			return "Please select an item in tool.";
		} 
		int s = getSelectedLevelIndex();
		if(s < 0) return "Please select a level.";
		SupaplexLevel level = levels[s];
		level.paintAllWith(selectedToolItem);

		Properties p = new Properties();
		p.setProperty("kind", "fill");
		p.put("level", level);
		fire(p);
		return null;		
	}
	
}
