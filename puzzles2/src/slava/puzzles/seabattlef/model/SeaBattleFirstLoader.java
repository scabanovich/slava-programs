package slava.puzzles.seabattlef.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import com.slava.common.RectangularField;
import slava.puzzle.template.model.PuzzleLoader;

public class SeaBattleFirstLoader extends PuzzleLoader implements SeaBattleFirstConstants {

	public SeaBattleFirstLoader() {
		init();
	}

	protected void init() {
		setRoot("/data/seafirst");
		initName("seafirst_");
	}
	
	SeaBattleFirstModel getSeaBattleModel() {
		return (SeaBattleFirstModel)model;
	}
	
	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		RectangularField field = getSeaBattleModel().getField();
		String size = p.getProperty("size");
		int[] sz = deserialize(size);
		getSeaBattleModel().changeFieldSize(sz[0], sz[1]);
		SeaBattleFirstPuzzle puzzle = getSeaBattleModel().getPuzzleInfo();
		String s = p.getProperty("data");
		int[] ls = deserialize(s);
		for (int i = 0; i < field.getSize(); i++) puzzle.getData()[i] = ls[i];
		for (int d = 0; d < 4; d++) {
			s = p.getProperty("bValues" + d);
			ls = deserialize(s);
			for (int i = 0; i < field.getHeight(); i++) {
				puzzle.getBValues(d)[i] = ls[i];
			}
		}
		s = p.getProperty("ships");
		if(s != null) {
			ls = deserialize(s);
		} else {
			ls = DEFAULT_SHIPS;
		}
		puzzle.setShips(ls);
		String solution = p.getProperty("solution");
		ArrayList solutions = new ArrayList();
		if(solution != null) {
			solutions.add(deserialize(solution));
			getSeaBattleModel().setSolutionInfo("Loaded " + f.getName());
		}
		getSeaBattleModel().setSolutions(solutions);
	}
	
	public void save() throws Exception {
		RectangularField field = getSeaBattleModel().getField();
		Properties p = new Properties();
		p.setProperty("size", serialize(new int[]{field.getWidth(), field.getHeight()}));
		SeaBattleFirstPuzzle puzzle = getSeaBattleModel().getPuzzleInfo();
		p.setProperty("data", serialize(puzzle.getData()));
		for (int d = 0; d < 4; d++) {
			p.setProperty("bValues" + d, serialize(puzzle.getBValues(d)));
		}
		p.setProperty("ships", serialize(puzzle.getShips()));
		int[] solution = getSeaBattleModel().getSelectedSolution();
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
		RectangularField field = getSeaBattleModel().getField();
		SeaBattleFirstPuzzle puzzle = getSeaBattleModel().getPuzzleInfo();

		int[] solution = getSeaBattleModel().getSelectedSolution();
		int[][] bValues = puzzle.getBValues();
		int[] data = puzzle.getData();

		printCondition(b, field, bValues, data);
		b.print("<hr>");
		if(solution != null) printSolution(b, field, bValues, solution);

		b.print("</html>\n");
		b.close();
	}
	
	String getBgColor(int data) {
		if(data == EMPTY) return "a0a0a0";
		if(data == WATER) return "blue";
		return "6f6f6f";
	}
	
	String[] LETTERS = {"a", "e", "s", "w", "n", "m", "o"}; 
	
	void printCondition(PrintWriter b, RectangularField field, int[][] bValues, int[] data) {
		b.print("<html>\n");
		b.print("  <table>");

		printUpValues(b, bValues);

		for (int i = 0; i < field.getSize(); i++) {
			if(field.getX(i) == 0) {
				b.print("\n    <tr>\n");
				b.print("<td width=\"30\" height=\"30\" align=\"center\"");
				b.print(">");
				if(bValues[2][field.getY(i)] >= 0) {
					b.print("" + bValues[2][field.getY(i)]);
				}
				b.print("</td>\n");
			}
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String bgc = getBgColor(data[i]);
			b.print(" bgcolor=\"" + bgc + "\"");
			b.print(">");
			if(data[i] != EMPTY) b.print(LETTERS[data[i]]);
			b.print("</td>\n");
			if(field.isRightBorder(i)) {
				b.print("<td width=\"30\" height=\"30\" align=\"center\"");
				b.print(">");
				if(bValues[0][field.getY(i)] >= 0) {
					b.print("" + bValues[0][field.getY(i)]);
				}
				b.print("</td>\n");
				b.print("    </tr>");
			}
		}

		printDownValues(b, bValues);

		b.print("\n  </table>\n");
	}

	void printSolution(PrintWriter b, RectangularField field, int[][] bValues, int[] solution) {
		b.print("<html>\n");
		b.print("  <table>");

		printUpValues(b, bValues);

		for (int i = 0; i < field.getSize(); i++) {
			if(field.getX(i) == 0) {
				b.print("\n    <tr>\n");
				b.print("<td width=\"30\" height=\"30\" align=\"center\"");
				b.print(">");
				if(bValues[2][field.getY(i)] >= 0) {
					b.print("" + bValues[2][field.getY(i)]);
				}
				b.print("</td>\n");
			}
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String bgc = solution[i] <= WATER ? "blue" : "black";
			b.print(" bgcolor=\"" + bgc + "\"");
			b.print(">");
			b.print("</td>\n");
			if(field.isRightBorder(i)) {
				b.print("<td width=\"30\" height=\"30\" align=\"center\"");
				b.print(">");
				if(bValues[0][field.getY(i)] >= 0) {
					b.print("" + bValues[0][field.getY(i)]);
				}
				b.print("</td>\n");
				b.print("    </tr>");
			}
		}
		
		printDownValues(b, bValues);

		b.print("\n  </table>\n");
	}
	
	void printUpValues(PrintWriter b, int[][] bValues) {
		b.print("\n    <tr>\n");
		b.print("<td width=\"30\" height=\"30\" align=\"center\"");
		b.print(">");
		for (int ix = 0; ix < bValues[3].length; ix++) {
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			b.print(">");
			if(bValues[3][ix] >= 0) {
				b.print("" + bValues[3][ix]);
			}
			b.print("</td>\n");
		}
		b.print("    </tr>");
	}

	void printDownValues(PrintWriter b, int[][] bValues) {
		b.print("\n    <tr>\n");
		b.print("<td width=\"30\" height=\"30\" align=\"center\"");
		b.print(">");
		for (int ix = 0; ix < bValues[1].length; ix++) {
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			b.print(">");
			if(bValues[1][ix] >= 0) {
				b.print("" + bValues[1][ix]);
			}
			b.print("</td>\n");
		}
		b.print("    </tr>");
	}

}
