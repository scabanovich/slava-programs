package slava.puzzles.hexa.fence.model;

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

public class HexaFenceLoader extends PuzzleLoader implements HexaFenceConstants {

	public HexaFenceLoader() {
		init();
	}

	protected void init() {
		setRoot("/data/hexafence");
		initName("hexafence_");
	}
	
	HexaFenceModel getHexaFenceModel() {
		return (HexaFenceModel)model;
	}

	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		RectangularField field = getHexaFenceModel().getField();
		String sideLength = p.getProperty("sideLength");
		int[] sz = deserialize(sideLength);
		getHexaFenceModel().changeFieldSize(sz[0]);

		HexaFencePuzzle puzzle = getHexaFenceModel().getPuzzleInfo();
		String s = p.getProperty("data");
		int[] ls = deserialize(s);
		for (int i = 0; i < field.getSize(); i++) puzzle.getData()[i] = ls[i];
		s = p.getProperty("form");
		ls = deserialize(s);
		for (int i = 0; i < field.getSize(); i++) puzzle.getForm()[i] = ls[i];

		String solution = p.getProperty("solution");
		ArrayList solutions = new ArrayList();
		if(solution != null) {
			solutions.add(deserialize(solution));
			getHexaFenceModel().setSolutionInfo("Loaded " + f.getName());
		}
		getHexaFenceModel().setSolutions(solutions);
	}
	
	public void save() throws Exception {
		HexaField field = getHexaFenceModel().getField();
		Properties p = new Properties();
		p.setProperty("sideLength", "" + field.getSideLength());
		HexaFencePuzzle puzzle = getHexaFenceModel().getPuzzleInfo();
		p.setProperty("data", serialize(puzzle.getData()));
		p.setProperty("form", serialize(puzzle.getForm()));
		int[] solution = getHexaFenceModel().getSelectedSolution();
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
		HexaField field = getHexaFenceModel().getField();
		HexaFencePuzzle puzzle = getHexaFenceModel().getPuzzleInfo();

		int[] solution = getHexaFenceModel().getSelectedSolution();
		int[] data = puzzle.getData();
		int[] form = puzzle.getForm();

		printConditionAndSolution(b, field, form, data, solution);

		b.print("</html>\n");
		b.close();
	}
	
	void printConditionAndSolution(PrintWriter b, HexaField field, int[] form, int[] data, int[] solution) {
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < field.getSize(); i++) {
			if(field.getX(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String bgc = !field.isInField(i) ? "ffffff" 
					: (form[i] == 0) ? "000000" : "7f7f7f";
			if(solution != null && solution[i] == 1) {
				bgc = "9f0000";
			}
			b.print(" bgcolor=\"" + bgc + "\"");
			b.print(">");
			String s = (data[i] == NO_VALUE) ? "" :
					"" + data[i];
			b.print(s);
			b.print("</td>\n");
			if(field.isRightBorder(i)) {
				b.print("    </tr>");
			}
		}
		b.print("\n  </table>\n");
	}

}
