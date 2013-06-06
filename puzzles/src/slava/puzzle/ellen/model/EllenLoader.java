package slava.puzzle.ellen.model;

import java.io.*;
import java.util.*;
import slava.puzzle.template.model.PuzzleLoader;

public class EllenLoader extends PuzzleLoader {
	
	public EllenLoader() {
		setRoot("/data/ellen");
		initName("ellen_");
	}

	public EllenModel getEllenModel() {
		return (EllenModel)getModel();
	}
	
	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		EllenField field = (EllenField)getEllenModel().getField();
		String size = p.getProperty("size");
		int[] sz = deserialize(size);
		field.setSize(sz[0], sz[1]);		
		String letters = p.getProperty("letters");
		int[] ls = deserialize(letters);
		for (int i = 0; i < field.getSize(); i++) field.setLetterAt(i, ls[i]);
	}
	
	public void save() throws Exception {
		EllenField field = (EllenField)getEllenModel().getField();
		Properties p = new Properties();
		p.setProperty("size", serialize(new int[]{field.getWidth(), field.getHeight()}));
		p.setProperty("letters", serialize(field.getLetters()));
		FileOutputStream os = new FileOutputStream(getFile());
		p.store(os, null);
		os.close();
		saveHTML();
	}

	public void saveHTML() throws Exception {
		String path = getFile().getAbsolutePath();
		int dot = path.lastIndexOf('.');
		if(dot >= 0) path = path.substring(0, dot);
		path = path + ".html"; 
		File f = new File(path);
		FileWriter os = new FileWriter(f);
		PrintWriter b = new PrintWriter(os);		
		EllenField field = getEllenModel().getField();
		int[] letters = field.getLetters();
		int[] groups = field.getGroups();
		color(groups, field);
		color(groups, field);
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < letters.length; i++) {
			if(field.x(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			if(groups[i] < 0) b.print(" bgcolor=\"7f7f7f\"");
			else b.print(" bgcolor=\"" + getColor(groups[i]) + "\"");
			b.print(">");
			if(letters[i] == 1) {
				b.print('0');
			}
			b.print("</td>\n");
			if(field.x(i) == field.getWidth() - 1) b.print("    </tr>");
		}
		b.print("\n  </table>\n");
		b.print("</html>\n");
		b.close();
	}
	
	private String getColor(int q) {
		q = q % 5;
		//String s = "";
		if(q == 0) return "7fffff";
		if(q == 1) return "7f7fff";
		if(q == 2) return "7fff7f";
		if(q == 3) return "ff7f7f";
		if(q == 4) return "ffff7f";
		return "7f7f7f";
	}
	
	private void color(int[] groups, EllenField field) {
		int gm = field.getSize() / 5 - 1;
		for (int g = gm; g >= 0; g--) {
			int gn = getNewColor(g, groups, field);
			if(gn == g) continue;
			for (int i = 0; i < field.getSize(); i++) {
				if(groups[i] == g) groups[i] = gn;
			}
		}
	}
	
	private int getNewColor(int g, int[] groups, EllenField field) {
		int[] c = new int[50];
		for (int i = 0; i < c.length; i++) c[i] = 0;
		for (int i = 0; i < field.getSize(); i++) {
			if(groups[i] != g) continue;
			for (int d = 0; d < 4; d++) {
				int n = field.jump(i, d);
				if(n < 0 || groups[n] == g) continue;
				c[groups[n]]++;
			}
		}
		for (int i = 0; i < c.length; i++) if(c[i] == 0) return i;
		return g;
	}
	
}
