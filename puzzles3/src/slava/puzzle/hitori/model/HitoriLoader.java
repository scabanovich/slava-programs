package slava.puzzle.hitori.model;

import java.io.*;
import java.util.Properties;
import com.slava.common.RectangularField;
import slava.puzzle.template.model.PuzzleLoader;

public class HitoriLoader extends PuzzleLoader {

	public HitoriLoader() {
		init();
	}

	protected void init() {
		setRoot("/data/hitori");
		initName("hitori_");
	}

	public HitoriModel getHitoriModel() {
		return (HitoriModel)getModel();
	}

	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
//		RectangularField field = getHitoriModel().getField();
		String size = p.getProperty("size");
		int[] sz = deserialize(size);
		getHitoriModel().changeFieldSize(sz[0]);
		String numbers = p.getProperty("numbers");
		int[] ns = deserialize(numbers);
		getHitoriModel().getProblemInfo().setNumbers(ns);
		String solution = p.getProperty("solution");
		int[] s = solution == null ? null : deserialize(solution);
		getHitoriModel().getProblemInfo().setSolution(s);		
	}

	public void save() throws Exception {
		RectangularField field = getHitoriModel().getField();
		Properties p = new Properties();
		p.setProperty("size", serialize(new int[]{field.getWidth(), field.getHeight()}));
		p.setProperty("numbers", serialize(getHitoriModel().getProblemInfo().getNumbers()));
		int[] s = getHitoriModel().getProblemInfo().getSolution();
		if(s != null) p.setProperty("solution", serialize(s));
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
		
		RectangularField field = getHitoriModel().getField();
		int[] solution = getHitoriModel().getProblemInfo().getSolution();
		int[] numbers = getHitoriModel().getProblemInfo().getNumbers();
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < numbers.length; i++) {
			if(field.getX(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			if(solution == null) b.print(" bgcolor=\"ffffff\"");
			else b.print(" bgcolor=\"" + getColor(solution[i]) + "\"");
			b.print(">");
			b.print("" + numbers[i]);
			b.print("</td>\n");
			if(field.getX(i) == field.getWidth() - 1) b.print("    </tr>");
		}
		b.print("\n  </table>\n");
		b.print("</html>\n");
		b.close();
	}

	private String getColor(int q) {
		q = q % 5;
		if(q == -1) return "ff0000";
		if(q == 1) return "7f7f7f";
		return "ffffff";
	}
}
