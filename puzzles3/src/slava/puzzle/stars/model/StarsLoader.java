package slava.puzzle.stars.model;

import java.io.*;
import java.util.*;

import slava.puzzle.pentaletters.model.PentaLettersField;
import slava.puzzle.template.model.*;

public class StarsLoader extends PuzzleLoader {
	
	public StarsLoader() {
		init();
	}

	protected void init() {
		setRoot("/data/stars");
		initName("stars_");
	}

	public StarsModel getStarsModel() {
		return (StarsModel)getModel();
	}
	
	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		StarsSetsField field = (StarsSetsField)getStarsModel().getField();
		String size = p.getProperty("size");
		int[] sz = deserialize(size);
		getStarsModel().changeFieldSize(sz[0]);
		String letters = p.getProperty("sets");
		int[] ls = deserialize(letters);
		for (int i = 0; i < field.getSize(); i++) field.setSetAt(i, ls[i]);
		String solution = p.getProperty("solution");
		ArrayList solutions = new ArrayList();
		if(solution != null) {
			solutions.add(deserialize(solution));
			getStarsModel().setSolutionInfo("Loaded " + f.getName());
		}
		getStarsModel().setSolutions(solutions);
	}
	
	public void save() throws Exception {
		StarsSetsField field = (StarsSetsField)getStarsModel().getField();
		Properties p = new Properties();
		p.setProperty("size", serialize(new int[]{field.getWidth(), field.getHeight()}));
		p.setProperty("sets", serialize(field.getSets()));
		int[] solution = getStarsModel().getSelectedSolution();
		if(solution != null) {
			p.setProperty("solution", serialize(solution));
		}
		FileOutputStream os = new FileOutputStream(getFile());
		p.store(os, null);
		os.close();
		saveHTML();
		print(field);
	}

	public void saveHTML() throws Exception {
		String path = getFile().getAbsolutePath();
		int dot = path.lastIndexOf('.');
		if(dot >= 0) path = path.substring(0, dot);
		path = path + ".html"; 
		File f = new File(path);
		FileWriter os = new FileWriter(f);
		PrintWriter b = new PrintWriter(os);		
		StarsSetsField field = getStarsModel().getField();
		int[] solution = getStarsModel().getSelectedSolution();
		int[] groups = (int[])field.getSets().clone();
		color(groups, field);
		color(groups, field);
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < groups.length; i++) {
			if(field.x(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			if(groups[i] < 0) b.print(" bgcolor=\"7f7f7f\"");
			else b.print(" bgcolor=\"" + getColor(groups[i]) + "\"");
			b.print(">");
			if(solution != null && solution[i] > 0) {
				String ch = "x";
				b.print(ch);
			}
			b.print("</td>\n");
			if(field.x(i) == field.getWidth() - 1) b.print("    </tr>");
		}
		b.print("\n  </table>\n");
		b.print("</html>\n");
		b.close();
	}
	
	protected char toChar(int letter) {
		return (char)(65 + letter);
	}
	
	private String getColor(int q) {
		q = q % 5;
		String s = "";
		if(q == 0) return "7fffff";
		if(q == 1) return "7f7fff";
		if(q == 2) return "7fff7f";
		if(q == 3) return "ff7f7f";
		if(q == 4) return "ffff7f";
		return "7f7f7f";
	}
	
	private void color(int[] groups, StarsSetsField field) {
		int gm = field.getSize() / 5 - 1;
		for (int g = gm; g >= 0; g--) {
			int gn = getNewColor(g, groups, field);
			if(gn == g) continue;
			for (int i = 0; i < field.getSize(); i++) {
				if(groups[i] == g) groups[i] = gn;
			}
		}
	}
	
	private int getNewColor(int g, int[] groups, StarsSetsField field) {
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
	
	protected void print(StarsSetsField field) {
		int[] solution = getStarsModel().getSelectedSolution();
		int[] groups = field.getSets();
		for (int p = 0; p < groups.length; p++) {
			String s = "";
			if(solution != null) {
				s += solution[p] == 1 ? "S" : "x";
			}
			int q = field.jump(p, 0);
			if(q >= 0 && groups[q] != groups[p]) s += "r";
			q = field.jump(p, 1);
			if(q >= 0 && groups[q] != groups[p]) s += "d";
			while(s.length() < 3) s += " ";
			System.out.print(" " + s);
			if(field.x[p] == field.getWidth() - 1) {
				System.out.println("");
			}
		}		
		System.out.println("");
	}

}
