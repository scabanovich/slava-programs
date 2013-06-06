package slava.puzzle.pentasets.model;

import java.util.*;
import java.io.*;
import slava.puzzle.pentaletters.model.*;

public class PentaSetsLoader extends PentaLettersLoader {

	protected void init() {
		setRoot("/data/pentasets");
		initName("pentasets");
	}

	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		PentaLettersField field = (PentaLettersField)getPentaLettersModel().getField();
		String size = p.getProperty("size");
		int[] sz = deserialize(size);
		getPentaLettersModel().setSize(sz[0], sz[1]);
		String letters = p.getProperty("letters");
		int[] ls = deserialize(letters);
		for (int i = 0; i < field.getSize(); i++) field.setLetterAt(i, ls[i]);
		String groups = p.getProperty("groups");
		ls = deserialize(groups);
		for (int i = 0; i < field.getSize(); i++) field.setGroupAt(i, ls[i]);
	}
	
	public void save() throws Exception {
		PentaLettersField field = (PentaLettersField)getPentaLettersModel().getField();
		Properties p = new Properties();
		p.setProperty("size", serialize(new int[]{field.getWidth(), field.getHeight()}));
		p.setProperty("letters", serialize(field.getLetters()));
		p.setProperty("groups", serialize(field.getGroups()));
		FileOutputStream os = new FileOutputStream(getFile());
		p.store(os, null);
		os.close();
		saveHTML();
		print(field);
	}

	protected char toChar(int letter) {
		if(letter < 0) return ' ';
		if(letter == 0) return '1'; 
		if(letter == 1) return '2'; 
		if(letter == 2) return '3'; 
		if(letter == 3) return '4'; 
		if(letter == 4) return '5'; 
		return ' ';
	}

}
