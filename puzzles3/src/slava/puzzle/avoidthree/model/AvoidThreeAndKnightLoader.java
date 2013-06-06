package slava.puzzle.avoidthree.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;

import com.slava.common.TwoDimField;

import slava.puzzle.template.model.PuzzleLoader;

public class AvoidThreeAndKnightLoader extends PuzzleLoader {

	public AvoidThreeAndKnightLoader() {
		setRoot("/data/avoid3");
		initName("avoid3_");
	}
	
	public AvoidThreeAndKnightModel getAvoidThreeModel() {
		return (AvoidThreeAndKnightModel)getModel();
	}

	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		AvoidThreeAndKnightPuzzle puzzle = (AvoidThreeAndKnightPuzzle)getAvoidThreeModel().getPuzzleInfo();
		TwoDimField field = getAvoidThreeModel().getField();
		int width = Integer.parseInt(p.getProperty("width"));
		int height = Integer.parseInt(p.getProperty("height"));
		field.setSize(width, height);
		puzzle.init();
		int mode = Integer.parseInt(p.getProperty("mode"));
		if(mode == AvoidThreeAndKnightModel.DRAWING_FIELD_MODE) {
			getAvoidThreeModel().setDrawingFieldMode();
		} else if(mode == AvoidThreeAndKnightModel.SETTING_PUZZLE_MODE) {
			getAvoidThreeModel().setSettingPuzzleMode();
		}
		loadFilter(p, puzzle);
		if(mode == AvoidThreeAndKnightModel.SETTING_PUZZLE_MODE) {
			loadPuzzle(p, puzzle);
		}
	}

	public void save() throws Exception {
		AvoidThreeAndKnightPuzzle puzzle = (AvoidThreeAndKnightPuzzle)getAvoidThreeModel().getPuzzleInfo();
		TwoDimField field = getAvoidThreeModel().getField();
		Properties p = new Properties();
		p.setProperty("width", "" + field.getWidth());
		p.setProperty("height", "" + field.getHeight());
		p.setProperty("mode", "" + getAvoidThreeModel().getMode());
		saveFilter(p, puzzle);
		if(getAvoidThreeModel().isSettingPuzzleMode()) {
			savePuzzle(p, puzzle);
		}
		FileOutputStream os = new FileOutputStream(getFile());
		p.store(os, null);
		os.close();
		saveHTML();
	}
	
	void loadFilter(Properties p, AvoidThreeAndKnightPuzzle puzzle) throws Exception {
		int[] f = deserialize(p.getProperty("filter"));
		int[] filter = puzzle.getFilter();
		System.arraycopy(f, 0, filter, 0, filter.length);
	}	
	void loadPuzzle(Properties p, AvoidThreeAndKnightPuzzle puzzle) throws Exception {
		int[] f = deserialize(p.getProperty("places"));
		int[] v = deserialize(p.getProperty("values"));
		for (int i = 0; i < f.length; i++) {
			puzzle.doMove(f[i], v[i]);
		}
	}

	void saveFilter(Properties p, AvoidThreeAndKnightPuzzle puzzle) throws Exception {
		int[] filter = puzzle.getFilter();
		int[] f = new int[filter.length];
		System.arraycopy(filter, 0, f, 0, filter.length);
		p.setProperty("filter", serialize(f));
	}	
	void savePuzzle(Properties p, AvoidThreeAndKnightPuzzle puzzle) throws Exception {
		int moveCount = puzzle.getMoveCount();
		int[] f = new int[moveCount];
		int[] v = new int[moveCount];
		TwoDimField field = getAvoidThreeModel().getField();
		for (int i = 0; i < field.getSize(); i++) {
			if(puzzle.getCondition()[i] == 1) {
				int t = puzzle.getHistory()[i];
				f[t] = i;
				v[t] = puzzle.getValue(i);
			}
		}
		p.setProperty("places", serialize(f));
		p.setProperty("values", serialize(v));
	}

	public void saveHTML() throws Exception {
		String path = getFile().getAbsolutePath();
		int dot = path.lastIndexOf('.');
		if(dot >= 0) path = path.substring(0, dot);
		path = path + ".html"; 
		File f = new File(path);
		FileWriter os = new FileWriter(f);
		PrintWriter b = new PrintWriter(os);		
		AvoidThreeAndKnightPuzzle puzzle = (AvoidThreeAndKnightPuzzle)getAvoidThreeModel().getPuzzleInfo();
		TwoDimField field = getAvoidThreeModel().getField();
		b.print("<html>\n");
		b.print("  <table>");
		for (int p = 0; p < field.getSize(); p++) {
			int h = field.getY(p);
			int v = field.getX(p);
			if(v == 0) b.print("\n    <tr>\n");
			boolean fv = puzzle.isField(p);
			int value = puzzle.getValue(p);
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			String color = (fv) ? "bbbbbb" : "ffffff";                   
			b.print(" bgcolor=\"" + color + "\"");
			b.print(">");
			String ch = (value == AvoidThreeAndKnightPuzzle.A_VALUE) ? "A" :
			            (value == AvoidThreeAndKnightPuzzle.B_VALUE) ? "B" : 
			            (value == AvoidThreeAndKnightPuzzle.C_VALUE) ? "C" : 
			            (value == AvoidThreeAndKnightPuzzle.D_VALUE) ? "D" : 
			           " ";
			boolean set = puzzle.isCondition(p);
			if(set) b.print("<font color=\"0000ff\">");
			b.print(ch);
			if(set) b.print("</font>");
			b.print("</td>\n");
			if(v == field.getWidth() - 1) b.print("    </tr>");
		}
		b.print("\n  </table>\n");
		b.print("</html>\n");
		b.close();
	}


}
