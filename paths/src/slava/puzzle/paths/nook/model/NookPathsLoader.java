package slava.puzzle.paths.nook.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import com.slava.common.RectangularField;

import slava.puzzle.template.model.PuzzleLoader;

public class NookPathsLoader extends PuzzleLoader {

	public NookPathsLoader() {
		init();
	}

	protected void init() {
		setRoot("/data/nookpath");
		initName("nookpath_");
	}
	
	NookPathsModel getNookPathsModel() {
		return (NookPathsModel)model;
	}
	
	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		RectangularField field = getNookPathsModel().getField();
		String size = p.getProperty("size");
		int[] sz = deserialize(size);
		getNookPathsModel().changeFieldSize(sz[0], sz[1]);
		NookPathsPuzzle puzzle = getNookPathsModel().getPuzzleInfo();
		String s = p.getProperty("data");
		int[] ls = deserialize(s);
		for (int i = 0; i < field.getSize(); i++) puzzle.getData()[i] = ls[i];
		s = p.getProperty("filter");
		ls = deserialize(s);
		for (int i = 0; i < field.getSize(); i++) puzzle.getFilter()[i] = ls[i];

		String solution = p.getProperty("solution");
		ArrayList solutions = new ArrayList();
		if(solution != null) {
			solutions.add(deserialize(solution));
			getNookPathsModel().setSolutionInfo("Loaded " + f.getName());
		}
		getNookPathsModel().setSolutions(solutions);
	}
	
	public void save() throws Exception {
		RectangularField field = getNookPathsModel().getField();
		Properties p = new Properties();
		p.setProperty("size", serialize(new int[]{field.getWidth(), field.getHeight()}));
		NookPathsPuzzle puzzle = getNookPathsModel().getPuzzleInfo();
		p.setProperty("data", serialize(puzzle.getData()));
		p.setProperty("filter", serialize(puzzle.getFilter()));

		int[] solution = getNookPathsModel().getSelectedSolution();
		if(solution != null) {
			p.setProperty("solution", serialize(solution));
		}
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
		RectangularField field = getNookPathsModel().getField();
		NookPathsPuzzle puzzle = getNookPathsModel().getPuzzleInfo();

		int[] solution = getNookPathsModel().getSelectedSolution();
		int[] data = puzzle.getData();
		int[] filter = puzzle.getFilter();

		printCondition(b, field, filter, data);
		b.print("<hr>");
		if(solution != null) printSolution(b, field, filter, solution);

		b.print("</html>\n");
		b.close();
	}
	
	void printCondition(PrintWriter b, RectangularField field, int[] filter, int[] data) {
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < field.getSize(); i++) {
			if(field.getX(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String bgc = filter[i] == 1 ? "black" : "gray";
			b.print(" bgcolor=\"" + bgc + "\"");
			b.print(">");
			if(filter[i] == 0 && data[i] > 0) {
				b.print("" + data[i]);
			}
			b.print("</td>\n");
			if(field.isRightBorder(i)) {
				b.print("    </tr>");
			}
		}
		b.print("\n  </table>\n");
	}

	void printSolution(PrintWriter b, RectangularField field, int[] filter, int[] solution) {
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < field.getSize(); i++) {
			if(field.getX(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String bgc = filter[i] == 1 ? "black" : "gray";
			b.print(" bgcolor=\"" + bgc + "\"");
			b.print(">");
			if(filter[i] == 0 && solution[i] > 0) {
				b.print("" + solution[i]);
			}
			b.print("</td>\n");
			if(field.isRightBorder(i)) {
				b.print("    </tr>");
			}
		}
		b.print("\n  </table>\n");
	}


}
