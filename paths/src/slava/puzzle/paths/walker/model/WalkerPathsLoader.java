package slava.puzzle.paths.walker.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import com.slava.common.RectangularField;

import slava.puzzle.template.model.PuzzleLoader;

public class WalkerPathsLoader extends PuzzleLoader {

	public WalkerPathsLoader() {
		init();
	}

	protected void init() {
		setRoot("/data/walkerpath");
		initName("walkerpath_");
	}

	WalkerPathsModel getWalkerPathsModel() {
		return (WalkerPathsModel)model;
	}
	
	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		RectangularField field = getWalkerPathsModel().getField();
		String size = p.getProperty("size");
		int[] sz = deserialize(size);
		getWalkerPathsModel().changeFieldSize(sz[0], sz[1]);
		WalkerPathsPuzzle puzzle = getWalkerPathsModel().getPuzzleInfo();
		String s = p.getProperty("parts");
		int[][] ls2 = deserialize2(s);
		for (int i = 0; i < field.getSize(); i++) {
			for (int d = 0; d < 4; d++) {
				puzzle.getParts()[i][d] = ls2[i][d];
			}
		}
		s = p.getProperty("filter");
		int[] ls = deserialize(s);
		for (int i = 0; i < field.getSize(); i++) {
			puzzle.getFilter()[i] = ls[i];
		}
		
		s = p.getProperty("turns");
		if(s != null && s.length() > 0) {
			ls = deserialize(s);
			for (int i = 0; i < field.getSize(); i++) {
				puzzle.getTurns()[i] = ls[i];
			}
		}

		String solution = p.getProperty("solution");
		ArrayList solutions = new ArrayList();
		if(solution != null) {
			solutions.add(deserialize2(solution));
			getWalkerPathsModel().setSolutionInfo("Loaded " + f.getName());
		}
		getWalkerPathsModel().setSolutions(solutions);
	}
	
	public void save() throws Exception {
		RectangularField field = getWalkerPathsModel().getField();
		Properties p = new Properties();
		p.setProperty("size", serialize(new int[]{field.getWidth(), field.getHeight()}));
		WalkerPathsPuzzle puzzle = getWalkerPathsModel().getPuzzleInfo();
		p.setProperty("parts", serialize(puzzle.getParts()));
		p.setProperty("filter", serialize(puzzle.getFilter()));
		p.setProperty("turns", serialize(puzzle.getTurns()));

		int[][] solution = getWalkerPathsModel().getSelectedSolution();
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
		RectangularField field = getWalkerPathsModel().getField();
		WalkerPathsPuzzle puzzle = getWalkerPathsModel().getPuzzleInfo();

//		int[][] solution = getWalkerPathsModel().getSelectedSolution();
		int[][] parts = puzzle.getParts();
		int[] filter = puzzle.getFilter();

		printCondition(b, field, filter, parts);
//		b.print("<hr>");
//		if(solution != null) printSolution(b, field, filter, solution);

		b.print("</html>\n");
		b.close();
	}
	
	void printCondition(PrintWriter b, RectangularField field, int[] filter, int[][] parts) {
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < field.getSize(); i++) {
			if(field.getX(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String bgc = filter[i] == 1 ? "black" : "gray";
			b.print(" bgcolor=\"" + bgc + "\"");
			b.print(">");
//			if(filter[i] == 0 && parts[i] > 0) {
//				b.print("" + parts[i]);
//			}
			b.print("</td>\n");
			if(field.isRightBorder(i)) {
				b.print("    </tr>");
			}
		}
		b.print("\n  </table>\n");
	}

	public static String serialize(int[][] array) {
		int[] a = new int[array.length * array[0].length];
		int k = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				a[k] = array[i][j];
				k++;
			}
		}
		return "" + array.length + ":" + serialize(a);
	}
	
	public static int[][] deserialize2(String s) {
		int q = s.indexOf(":");
		int l = 0;
		try {
			l = Integer.parseInt(s.substring(0, q));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		s = s.substring(q + 1);
		int[] a = deserialize(s);
		int[][] array = new int[l][a.length / l];
		int k = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				array[i][j] = a[k];
				k++;
			}
		}
		return array;
	}
}
