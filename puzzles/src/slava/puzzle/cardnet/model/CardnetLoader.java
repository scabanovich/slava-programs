package slava.puzzle.cardnet.model;

import java.io.*;
import java.util.*;
import slava.puzzle.template.model.PuzzleLoader;

public class CardnetLoader extends PuzzleLoader {
	
	public CardnetLoader() {
		setRoot("/data/cardnets");
		initName("cardnets_");
	}
	
	public CardnetModel getCardnetModel() {
		return (CardnetModel)getModel();
	}

	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		CardnetPuzzleInfo puzzle = (CardnetPuzzleInfo)getCardnetModel().getPuzzleInfo();
		//CardnetField field = puzzle.getField();
		puzzle.clearProblem();
		getCardnetModel().getGeneratorInfo().clear();
		loadFilter(p, puzzle);
		String s = p.getProperty("hasProblem");
		if("true".equals(s)) {
			puzzle.setProblem();
			loadHNumbers(p, puzzle);
			loadVNumbers(p, puzzle);
			loadHSuits(p, puzzle);
			loadVSuits(p, puzzle);
		}
	}

	public void save() throws Exception {
		CardnetPuzzleInfo puzzle = (CardnetPuzzleInfo)getCardnetModel().getPuzzleInfo();
		CardnetField field = puzzle.getField();
		Properties p = new Properties();
		p.setProperty("width", "" + field.getWidth());
		p.setProperty("height", "" + field.getHeight());
		saveFilter(p, puzzle);
		if(puzzle.hasProblem()) {
			p.setProperty("hasProblem", "true");
			saveHNumbers(p, puzzle);
			saveVNumbers(p, puzzle);
			saveHSuits(p, puzzle);
			saveVSuits(p, puzzle);
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
		CardnetPuzzleInfo puzzle = (CardnetPuzzleInfo)getCardnetModel().getPuzzleInfo();
		//CardnetField field = puzzle.getField();
		b.print("<html>\n");
		b.print("  <table>");
		CardnetHTML html = new CardnetHTML();
		CardnetHTML.Cell[][] cells = html.getTable(puzzle);
		if(cells != null) for (int h = 0; h < cells.length; h++) {
			b.print("\n    <tr>\n");
			for (int v = 0; v < cells[h].length; v++) {
				b.print("<td width=\"30\" height=\"30\" align=\"center\"");
				int state = cells[h][v].state;
				String color = (state == -2) ? "999999" : // outside
				               (state == -1) ? "777777" : // number
				               (state == 0) ? "000000" :  // empty
				               "ffffff";                  // card  
				b.print(" bgcolor=\"" + color + "\"");
				b.print(">");
				String ch = cells[h][v].text;
				b.print(ch);
				b.print("</td>\n");
			}
			b.print("    </tr>");
		}
		b.print("\n  </table>\n");
		b.print("</html>\n");
		b.close();
	}


	private void loadFilter(Properties p, CardnetPuzzleInfo puzzle) {
		String s = p.getProperty("filter");
		int[] f = deserialize(s);
		for (int i = 0; i < puzzle.getField().getSize(); i++) {
			puzzle.setFilterValue(i, f[i]);
		}
	}
	
	private void saveFilter(Properties p, CardnetPuzzleInfo puzzle) {
		int[] f = new int[puzzle.getField().getSize()];
		for (int i = 0; i < puzzle.getField().getSize(); i++) {
			f[i] = puzzle.getFilterValue(i);
		}
		String s = serialize(f);
		p.setProperty("filter", s);
	}
	
	private void loadHNumbers(Properties p, CardnetPuzzleInfo puzzle) {
		String s = p.getProperty("hNumbers");
		int[] f = deserialize(s);
		int height = puzzle.getField().getHeight();
		int nc = puzzle.getCardSet().getNumberCount();
		for (int h = 0; h < height; h++) {
			int[] hn = puzzle.getHNumbers(h);
			System.arraycopy(f, nc * h, hn, 0, nc);
		}
		s = p.getProperty("hNumbersSet");
		f = deserialize(s);
		for (int h = 0; h < height; h++) {
			puzzle.setHNumberSet(h, f[h] == 1);
		}
	}
	private void saveHNumbers(Properties p, CardnetPuzzleInfo puzzle) {
		int height = puzzle.getField().getHeight();
		int nc = puzzle.getCardSet().getNumberCount();
		int[] f = new int[nc * height];
		for (int h = 0; h < height; h++) {
			int[] hn = puzzle.getHNumbers(h);
			System.arraycopy(hn, 0, f, nc * h, nc);
		}
		String s = serialize(f);
		p.setProperty("hNumbers", s);
		f = new int[height];
		for (int h = 0; h < height; h++) {
			f[h] = (puzzle.isHNumberSet(h)) ? 1 : 0;
		}
		s = serialize(f);
		p.setProperty("hNumbersSet", s);
	}

	private void loadVNumbers(Properties p, CardnetPuzzleInfo puzzle) {
		String s = p.getProperty("vNumbers");
		int[] f = deserialize(s);
		int width = puzzle.getField().getWidth();
		int nc = puzzle.getCardSet().getNumberCount();
		for (int c = 0; c < width; c++) {
			int[] vn = puzzle.getVNumbers(c);
			System.arraycopy(f, nc * c, vn, 0, nc);
		}
		s = p.getProperty("vNumbersSet");
		f = deserialize(s);
		for (int c = 0; c < width; c++) {
			puzzle.setVNumberSet(c, f[c] == 1);
		}
	}
	private void saveVNumbers(Properties p, CardnetPuzzleInfo puzzle) {
		int width = puzzle.getField().getWidth();
		int nc = puzzle.getCardSet().getNumberCount();
		int[] f = new int[nc * width];
		for (int c = 0; c < width; c++) {
			int[] vn = puzzle.getVNumbers(c);
			System.arraycopy(vn, 0, f, nc * c, nc);
		}
		String s = serialize(f);
		p.setProperty("vNumbers", s);
		f = new int[width];
		for (int h = 0; h < width; h++) {
			f[h] = (puzzle.isVNumberSet(h)) ? 1 : 0;
		}
		s = serialize(f);
		p.setProperty("vNumbersSet", s);
	}

	private void loadHSuits(Properties p, CardnetPuzzleInfo puzzle) {
		String s = p.getProperty("hSuits");
		int[] f = deserialize(s);
		int height = puzzle.getField().getHeight();
		int nc = puzzle.getCardSet().getSuitCount();
		for (int h = 0; h < height; h++) {
			int[] hn = puzzle.getHSuits(h);
			System.arraycopy(f, nc * h, hn, 0, nc);
		}
		s = p.getProperty("hSuitsSet");
		f = deserialize(s);
		for (int h = 0; h < height; h++) {
			puzzle.setHSuitSet(h, f[h] == 1);
		}
	}
	private void saveHSuits(Properties p, CardnetPuzzleInfo puzzle) {
		int height = puzzle.getField().getHeight();
		int nc = puzzle.getCardSet().getSuitCount();
		int[] f = new int[nc * height];
		for (int h = 0; h < height; h++) {
			int[] hn = puzzle.getHSuits(h);
			System.arraycopy(hn, 0, f, nc * h, nc);
		}
		String s = serialize(f);
		p.setProperty("hSuits", s);
		f = new int[height];
		for (int h = 0; h < height; h++) {
			f[h] = (puzzle.isHSuitSet(h)) ? 1 : 0;
		}
		s = serialize(f);
		p.setProperty("hSuitsSet", s);
	}

	private void loadVSuits(Properties p, CardnetPuzzleInfo puzzle) {
		String s = p.getProperty("vSuits");
		int[] f = deserialize(s);
		int width = puzzle.getField().getWidth();
		int nc = puzzle.getCardSet().getSuitCount();
		for (int c = 0; c < width; c++) {
			int[] vn = puzzle.getVSuits(c);
			System.arraycopy(f, nc * c, vn, 0, nc);
		}
		s = p.getProperty("vSuitsSet");
		f = deserialize(s);
		for (int c = 0; c < width; c++) {
			puzzle.setVSuitSet(c, f[c] == 1);
		}
	}
	private void saveVSuits(Properties p, CardnetPuzzleInfo puzzle) {
		int width = puzzle.getField().getWidth();
		int nc = puzzle.getCardSet().getSuitCount();
		int[] f = new int[nc * width];
		for (int c = 0; c < width; c++) {
			int[] vn = puzzle.getVSuits(c);
			System.arraycopy(vn, 0, f, nc * c, nc);
		}
		String s = serialize(f);
		p.setProperty("vSuits", s);
		f = new int[width];
		for (int h = 0; h < width; h++) {
			f[h] = (puzzle.isVSuitSet(h)) ? 1 : 0;
		}
		s = serialize(f);
		p.setProperty("vSuitsSet", s);
	}
}
