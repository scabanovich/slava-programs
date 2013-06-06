package slava.puzzles.hexa.rook.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import com.slava.common.RectangularField;
import slava.puzzle.template.model.PuzzleLoader;
import slava.puzzles.hexa.common.HexaField;

public class HexaRookLoader extends PuzzleLoader implements HexaRookConstants {

	public HexaRookLoader() {
		init();
	}

	protected void init() {
		setRoot("/data/hexarook");
		initName("hexarook_");
	}
	
	HexaRookModel getHexaRookModel() {
		return (HexaRookModel)model;
	}
	
	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		RectangularField field = getHexaRookModel().getField();
		String sideLength = p.getProperty("sideLength");
		int[] sz = deserialize(sideLength);
		getHexaRookModel().changeFieldSize(sz[0]);
		HexaRookPuzzle puzzle = getHexaRookModel().getPuzzleInfo();
		String s = p.getProperty("positions");
		int[] ls = deserialize(s);
		for (int i = 0; i < field.getSize(); i++) puzzle.getPositions()[i] = ls[i];
		String solution = p.getProperty("solution");
		ArrayList solutions = new ArrayList();
		if(solution != null) {
			solutions.add(deserialize(solution));
			getHexaRookModel().setSolutionInfo("Loaded " + f.getName());
		}
		getHexaRookModel().setSolutions(solutions);
	}
	
	public void save() throws Exception {
		HexaField field = getHexaRookModel().getField();
		Properties p = new Properties();
		p.setProperty("sideLength", "" + field.getSideLength());
		HexaRookPuzzle puzzle = getHexaRookModel().getPuzzleInfo();
		p.setProperty("positions", serialize(puzzle.getPositions()));
		int[] solution = getHexaRookModel().getSelectedSolution();
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
		HexaField field = getHexaRookModel().getField();
		HexaRookPuzzle puzzle = getHexaRookModel().getPuzzleInfo();

		int[] solution = getHexaRookModel().getSelectedSolution();
		int[] positions = puzzle.getPositions();

		printCondition(b, field, positions);
		b.print("<hr>");
		if(solution != null) printSolution(b, field, solution);

		b.print("</html>\n");
		b.close();
	}
	
	void printCondition(PrintWriter b, HexaField field, int[] positions) {
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < field.getSize(); i++) {
			if(field.getX(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String bgc = field.isInField(i) ? "7f7f7f" : "ffffff";
			b.print(" bgcolor=\"" + bgc + "\"");
			b.print(">");
			String s = (positions[i] == NOT_POSITION) ? "" :
				(positions[i] == SOME_POSITION) ? "o" :
					"" + positions[i];
			b.print(s);
			b.print("</td>\n");
			if(field.isRightBorder(i)) {
				b.print("    </tr>");
			}
		}
		b.print("\n  </table>\n");
	}

	void printSolution(PrintWriter b, HexaField field, int[] solution) {
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < field.getSize(); i++) {
			if(field.getX(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String bgc = field.isInField(i) ? "7f7f7f" : "ffffff";
			b.print(" bgcolor=\"" + bgc + "\"");
			b.print(">");
			String s = (solution[i] == NOT_POSITION) ? "" :
				(solution[i] == SOME_POSITION) ? "o" :
					"" + solution[i];
			b.print(s);
			b.print("</td>\n");
			if(field.isRightBorder(i)) {
				b.print("    </tr>");
			}
		}
		b.print("\n  </table>\n");
	}

}
