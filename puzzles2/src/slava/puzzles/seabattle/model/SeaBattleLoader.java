package slava.puzzles.seabattle.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import com.slava.common.RectangularField;
import slava.puzzle.template.model.PuzzleLoader;

public class SeaBattleLoader extends PuzzleLoader implements SeaBattleConstants {

	public SeaBattleLoader() {
		init();
	}

	protected void init() {
		setRoot("/data/sea");
		initName("sea_");
	}
	
	SeaBattleModel getSeaBattleModel() {
		return (SeaBattleModel)model;
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
		SeaBattlePuzzle puzzle = getSeaBattleModel().getPuzzleInfo();
		String s = p.getProperty("data");
		int[] ls = deserialize(s);
		for (int i = 0; i < field.getSize(); i++) puzzle.getData()[i] = ls[i];
		s = p.getProperty("hValues");
		ls = deserialize(s);
		for (int i = 0; i < field.getHeight(); i++) puzzle.getHValues()[i] = ls[i];
		s = p.getProperty("vValues");
		ls = deserialize(s);
		for (int i = 0; i < field.getWidth(); i++) puzzle.getVValues()[i] = ls[i];
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
		SeaBattlePuzzle puzzle = getSeaBattleModel().getPuzzleInfo();
		p.setProperty("data", serialize(puzzle.getData()));
		p.setProperty("hValues", serialize(puzzle.getHValues()));
		p.setProperty("vValues", serialize(puzzle.getVValues()));
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
		SeaBattlePuzzle puzzle = getSeaBattleModel().getPuzzleInfo();

		int[] solution = getSeaBattleModel().getSelectedSolution();
		int[] hValues = puzzle.getHValues();
		int[] vValues = puzzle.getVValues();
		int[] data = puzzle.getData();

		printCondition(b, field, hValues, vValues, data);
		b.print("<hr>");
		if(solution != null) printSolution(b, field, hValues, vValues, solution);

		b.print("</html>\n");
		b.close();
	}
	
	String getBgColor(int data) {
		if(data == EMPTY) return "a0a0a0";
		if(data == WATER) return "blue";
		return "6f6f6f";
	}
	
	String[] LETTERS = {"a", "e", "s", "w", "n", "m", "o"}; 
	
	void printCondition(PrintWriter b, RectangularField field, int[] hValues, int[] vValues, int[] data) {
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < field.getSize(); i++) {
			if(field.getX(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String bgc = getBgColor(data[i]);
			b.print(" bgcolor=\"" + bgc + "\"");
			b.print(">");
			if(data[i] != EMPTY) b.print(LETTERS[data[i]]);
			b.print("</td>\n");
			if(field.isRightBorder(i)) {
				b.print("<td width=\"30\" height=\"30\" align=\"center\"");
				b.print(">");
				if(hValues[field.getY(i)] >= 0) {
					b.print("" + hValues[field.getY(i)]);
				}
				b.print("</td>\n");
				b.print("    </tr>");
			}
		}
		b.print("\n    <tr>\n");
		for (int ix = 0; ix < vValues.length; ix++) {
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			b.print(">");
			if(vValues[ix] >= 0) {
				b.print("" + vValues[ix]);
			}
			b.print("</td>\n");
		}
		b.print("    </tr>");
		b.print("\n  </table>\n");
	}

	void printSolution(PrintWriter b, RectangularField field, int[] hValues, int[] vValues, int[] solution) {
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < field.getSize(); i++) {
			if(field.getX(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String bgc = solution[i] <= WATER ? "blue" : "black";
			b.print(" bgcolor=\"" + bgc + "\"");
			b.print(">");
			b.print("</td>\n");
			if(field.isRightBorder(i)) {
				b.print("<td width=\"30\" height=\"30\" align=\"center\"");
				b.print(">");
				if(hValues[field.getY(i)] >= 0) {
					b.print("" + hValues[field.getY(i)]);
				}
				b.print("</td>\n");
				b.print("    </tr>");
			}
		}
		b.print("\n    <tr>\n");
		for (int ix = 0; ix < vValues.length; ix++) {
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			b.print(">");
			if(vValues[ix] >= 0) {
				b.print("" + vValues[ix]);
			}
			b.print("</td>\n");
		}
		b.print("    </tr>");
		b.print("\n  </table>\n");
	}

}
