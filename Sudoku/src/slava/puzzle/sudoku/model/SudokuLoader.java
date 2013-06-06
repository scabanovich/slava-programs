package slava.puzzle.sudoku.model;

import java.io.*;
import java.util.*;
import slava.puzzle.template.model.PuzzleLoader;

public class SudokuLoader extends PuzzleLoader {
	
	public SudokuLoader() {
		setRoot("/data/sudoku");
		initName("sudoku_");
	}

	public SudokuModel getSudokuModel() {
		return (SudokuModel)getModel();
	}
	
	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		SudokuDesignField field = (SudokuDesignField)getSudokuModel().getField();
		String size = p.getProperty("size");
		int[] sz = deserialize(size);
		getSudokuModel().setSize(sz[0]);

		String regions = p.getProperty("regions");
		int[] ls = deserialize(regions);
		int[] _regions = getSudokuModel().getProblemInfo().getRegions();
		for (int i = 0; i < field.getSize(); i++) _regions[i] = ls[i];

		String numbers = p.getProperty("numbers");
		int[] ns = deserialize(numbers);
		int[] _numbers = getSudokuModel().getProblemInfo().getNumbers();
		for (int i = 0; i < field.getSize(); i++) _numbers[i] = ns[i];
		
		String s = p.getProperty("addDiagonals");
		if("true".equals(s)) {
			getSudokuModel().getProblemInfo().setDiagonalsOption(SudokuProblemInfo.MAIN_DIAGONALS);
		} else {
			getSudokuModel().getProblemInfo().setDiagonalsOption(SudokuProblemInfo.NO_DIAGONALS);
		}
		s = p.getProperty("diagonalOption");
		int diagonalOption = ("0".equals(s)) ? 0 : ("1".equals(s)) ? 1 : ("2".equals(s)) ? 2 : -1;
		if(diagonalOption >= 0) {
			getSudokuModel().getProblemInfo().setDiagonalsOption(diagonalOption);
		}
		
		s = p.getProperty("notTouchingDiagonally");
		getSudokuModel().getProblemInfo().setNotTouchingDiagonally("true".equals(s));
		
		s = p.getProperty("usingDifferenceOneRestriction");
		getSudokuModel().getProblemInfo().setUsingDifferenceOneRestriction("true".equals(s));
		
		s = p.getProperty("differenceOneData");
		if(s != null) {
			int[][] data = getSudokuModel().getProblemInfo().getDifferenceOneData();
			loadTwoDimensionalArray(data, s);
		}
		
		s = p.getProperty("usingLessOrGreaterRestriction");
		getSudokuModel().getProblemInfo().setUsingLessOrGreaterRestriction("true".equals(s));
		
		s = p.getProperty("lessOrGreaterData");
		if(s != null) {
			int[][] data = getSudokuModel().getProblemInfo().getLessOrGreaterData();
			loadTwoDimensionalArray(data, s);
		}
		
		s = p.getProperty("usingXVRestriction");
		getSudokuModel().getProblemInfo().setUsingXVRestriction("true".equals(s));
		
		s = p.getProperty("xvData");
		if(s != null) {
			int[][] data = getSudokuModel().getProblemInfo().getXVData();
			loadTwoDimensionalArray(data, s);
		}

		s = p.getProperty("usingPartitioningSumRestriction");
		getSudokuModel().getProblemInfo().setUsingPartitioningSumRestriction("true".equals(s));

		s = p.getProperty("partitioningData");
		if(s != null) {
			int[] data = deserialize(s);
			int[] partitioning = getSudokuModel().getProblemInfo().getPartitioning();
			for (int i = 0; i < data.length; i++) partitioning[i] = data[i];
			
		}
		s = p.getProperty("sumsData");
		if(s != null) {
			int[] data = deserialize(s);
			getSudokuModel().getProblemInfo().setSums(data);
		}
	}
	
	private void loadTwoDimensionalArray(int[][] data, String s) {
		int[] _data = deserialize(s);
		int k = 0;
		for (int i = 0; i < data.length; i++) {
			for (int d = 0; d < 4; d++) {
				data[i][d] = _data[k];
				++k;
			}
		}
	}
	
	public void save() throws Exception {
		SudokuDesignField field = (SudokuDesignField)getSudokuModel().getField();
		Properties p = new Properties();
		p.setProperty("size", serialize(new int[]{field.getWidth()}));
		p.setProperty("regions", serialize(getSudokuModel().getProblemInfo().getRegions()));
		p.setProperty("numbers", serialize(getSudokuModel().getProblemInfo().getNumbers()));
		int diagonalOption = getSudokuModel().getProblemInfo().getDiagonalsOption();
		p.setProperty("diagonalOption", "" + diagonalOption);
		boolean notTouchingDiagonally = getSudokuModel().getProblemInfo().isNotTouchingDiagonally();
		if(notTouchingDiagonally) p.setProperty("notTouchingDiagonally", "true");
		int neighboursDifferNotMoreThan = getSudokuModel().getProblemInfo().getNeighboursDifferNotMoreThan();
		if(neighboursDifferNotMoreThan > 0) p.setProperty("neighboursDifferNotMoreThan", "" + neighboursDifferNotMoreThan);
		boolean usingDifferenceOneRestriction = getSudokuModel().getProblemInfo().isUsingDifferenceOneRestriction();
		if(usingDifferenceOneRestriction) {
			p.setProperty("usingDifferenceOneRestriction", "true");
			int[][] data = getSudokuModel().getProblemInfo().getDifferenceOneData();		
			int[] _data = saveTwoDimensionalArray(data);
			p.setProperty("differenceOneData", serialize(_data));
		}
		boolean usingLessOrGreaterRestriction = getSudokuModel().getProblemInfo().isUsingLessOrGreaterRestriction();
		if(usingLessOrGreaterRestriction) {
			p.setProperty("usingLessOrGreaterRestriction", "true");
			int[][] data = getSudokuModel().getProblemInfo().getLessOrGreaterData();		
			int[] _data = saveTwoDimensionalArray(data);
			p.setProperty("lessOrGreaterData", serialize(_data));
		}
		boolean usingXVRestriction = getSudokuModel().getProblemInfo().isUsingXVRestriction();
		if(usingXVRestriction) {
			p.setProperty("usingXVRestriction", "true");
			int[][] data = getSudokuModel().getProblemInfo().getXVData();	
			int[] _data = saveTwoDimensionalArray(data);
			p.setProperty("xvData", serialize(_data));
		}
		boolean usingPartitioningSumRestriction = getSudokuModel().getProblemInfo().isUsingPartitioningSumRestriction();
		if(usingPartitioningSumRestriction) {
			p.setProperty("usingPartitioningSumRestriction", "true");
			int[] partitioning = getSudokuModel().getProblemInfo().getPartitioning();
			p.setProperty("partitioningData", serialize(partitioning));
			int[] sums = getSudokuModel().getProblemInfo().getSums();
			p.setProperty("sumsData", serialize(sums));
		}

		FileOutputStream os = new FileOutputStream(getFile());
		p.store(os, null);
		os.close();
		saveHTML();
		if(getSudokuModel().getProblemInfo().isUsingPartitioningSumRestriction()) {
			savePartitioningSum();
		}
	}
	
	private int[] saveTwoDimensionalArray(int[][] data) {
		int[] _data = new int[data.length * 4];
		int k = 0;
		for (int i = 0; i < data.length; i++) {
			for (int d = 0; d < 4; d++) {
				_data[k] = data[i][d];
				++k;
			}
		}
		return _data;
	}

	public void saveHTML() throws Exception {
		String path = getFile().getAbsolutePath();
		int dot = path.lastIndexOf('.');
		if(dot >= 0) path = path.substring(0, dot);
		path = path + ".html"; 
		File f = new File(path);
		FileWriter os = new FileWriter(f);
		PrintWriter b = new PrintWriter(os);		
		SudokuDesignField field = getSudokuModel().getField();
		int[] numbers = getSudokuModel().getProblemInfo().getNumbers();
		int[] solution = getSudokuModel().getProblemInfo().getSolution();
		int[] groups = (int[])getSudokuModel().getProblemInfo().getRegions().clone();
		color(groups, field);
		color(groups, field);
		b.print("<html>\n");
		b.print("<h4>Problem</h4>\n");
		printTable(b, groups, numbers, field);

		int diagonalOption = getSudokuModel().getProblemInfo().getDiagonalsOption();
		if(diagonalOption == SudokuProblemInfo.MAIN_DIAGONALS) {
			b.print("<p>Main Diagonals</p>\n");
		} else if(diagonalOption == SudokuProblemInfo.ALL_DIAGONALS) {
			b.print("<p>All Diagonals (from left upper to right down)</p>\n");
		}
		boolean notTouchingDiagonally = getSudokuModel().getProblemInfo().isNotTouchingDiagonally();
		if(notTouchingDiagonally) {
			b.print("<p>Not Touching Diagonally</p>\n");
		}
		int neighboursDifferNotMoreThan = getSudokuModel().getProblemInfo().getNeighboursDifferNotMoreThan();
		if(neighboursDifferNotMoreThan > 0) {
			b.print("<p>Neighbours Differ NotMore Than " + neighboursDifferNotMoreThan + "</p>");
		}
		boolean usingDifferenceOneRestriction = getSudokuModel().getProblemInfo().isUsingDifferenceOneRestriction();
		if(usingDifferenceOneRestriction) {
			b.print("<p>Using Difference One Restriction</p>\n");
		}

		if(solution != null) {
			b.print("<h4>Solution</h4>\n");
			printTable(b, groups, solution, field);
		}

		b.print("</html>\n");
		b.close();
	}
	
	private void printTable(PrintWriter b, int[] groups, int[] values, SudokuDesignField field) {
		b.print("  <table>");
		for (int i = 0; i < values.length; i++) {
			if(field.x(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			if(groups[i] < 0) b.print(" bgcolor=\"7f7f7f\"");
			else b.print(" bgcolor=\"" + getColor(groups[i]) + "\"");
			b.print(">");
			if(values[i] != 0) {
				b.print(values[i]);
			}
			b.print("</td>\n");
			if(field.x(i) == field.getWidth() - 1) b.print("    </tr>");
		}
		b.print("\n  </table>\n");
	}
	
	private String getColor(int q) {
		q = q % 5;
		//String s = "";
		if(q == 0) return "7fffff";
		if(q == 1) return "7f7fff";
		if(q == 2) return "7fff7f";
		if(q == 3) return "ff7f7f";
		if(q == 4) return "ffff7f";
		return "7f7f7f";
	}
	
	private void color(int[] groups, SudokuDesignField field) {
		int gm = field.getSize() / 5 - 1;
		for (int g = gm; g >= 0; g--) {
			int gn = getNewColor(g, groups, field);
			if(gn == g) continue;
			for (int i = 0; i < field.getSize(); i++) {
				if(groups[i] == g) groups[i] = gn;
			}
		}
	}
	
	private int getNewColor(int g, int[] groups, SudokuDesignField field) {
		int[] c = new int[50];
		for (int i = 0; i < c.length; i++) c[i] = 0;
		for (int i = 0; i < field.getSize(); i++) {
			if(groups[i] != g) continue;
			for (int d = 0; d < 4; d++) {
				int n = field.jp(i, d);
				if(n < 0 || groups[n] == g) continue;
				c[groups[n]]++;
			}
		}
		for (int i = 0; i < c.length; i++) if(c[i] == 0) return i;
		return g;
	}

	private void savePartitioningSum() throws IOException {
		String path = getFile().getAbsolutePath();
		int dot = path.lastIndexOf('.');
		if(dot >= 0) path = path.substring(0, dot);
		path = path + "_sums." + extension; 
		File f = new File(path);
		FileWriter os = new FileWriter(f);
		PrintWriter b = new PrintWriter(os);		
		SudokuDesignField field = getSudokuModel().getField();
		int[] solution = getSudokuModel().getProblemInfo().getSolution();
		int[] partitions = (int[])getSudokuModel().getProblemInfo().getPartitioning().clone();
		int[] sums = getSudokuModel().getProblemInfo().getSums();
		int[] usedSums = new int[sums.length];
		
		StringBuffer sb = new StringBuffer();
		//sums
		for (int p = 0; p < field.getSize(); p++) {
			int index = partitions[p];
			if(index < 0 || index >= sums.length || usedSums[index] > 0) {
				sb.append("  +");
			} else {
				usedSums[index] = 1;
				String s = " " + sums[index];
				while(s.length() < 3)  {
					s = " " + s;
				}
				sb.append(s);
			}
			if(field.x(p) == field.getWidth() - 1) {
				sb.append('\n');
			}
		}
		sb.append('\n');
		//partitions
		for (int p = 0; p < field.getSize(); p++) {
			int index = partitions[p];
			if(index < 0) {
				sb.append("   +");
			} else {
				String ds = " ";
				char[] dc = {'r', 'd', 'l', 'u'};
				for (int d = 0; d < 4; d++) {
					int q = field.jp(p, d);
					if(q < 0 || partitions[q] != index) ds += dc[d];
				}
				if(ds.length() == 1) ds += '+'; //deep inside
				while(ds.length() < 4) ds = " " + ds;
				sb.append(ds);
			}
			if(field.x(p) == field.getWidth() - 1) {
				sb.append('\n');
			}
		}
		sb.append('\n');
		//solution
		if(solution != null) for (int p = 0; p < field.getSize(); p++) {
			sb.append(" " + solution[p]);
			if(field.x(p) == field.getWidth() - 1) {
				sb.append('\n');
			}
		}

		b.write(sb.toString());
		b.close();
	}

}
