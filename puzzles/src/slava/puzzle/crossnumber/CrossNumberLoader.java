package slava.puzzle.crossnumber;

import java.util.*;
import java.io.*;
import slava.puzzle.template.model.PuzzleLoader;
import slava.puzzle.template.undo.UndoManager;

public class CrossNumberLoader extends PuzzleLoader {
	
	public CrossNumberLoader() {
		setRoot("/data/crossnumber");
		initName("cross");
	}
	
	public CrossNumberModel getCrossNumberModel() {
		return (CrossNumberModel)getModel();
	}
	
	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		CrossNumberField field = getCrossNumberModel().getField();
		int width = 11;
			try { 
				width = Integer.parseInt(p.getProperty("width"));
			} catch (Exception e) {
				//ignore unset width
			}
		int height = 11; 
		try { 
			height = Integer.parseInt(p.getProperty("height"));
		} catch (Exception e) {
			//ignore unset height
		}
		if(width != field.getWidth() && height != field.getHeight()) {
			field.setSize(width, height);
		}
		String mask = p.getProperty("mask");
		if(mask != null) field.setMask(deserialize(mask));
		String hsum = p.getProperty("hsum");
		if(hsum != null) field.setHSum(deserialize(hsum));
		String vsum = p.getProperty("vsum");
		if(vsum != null) field.setVSum(deserialize(vsum));
		UndoManager.getInstance().clean();
		
		String s = p.getProperty("differentThroughRow");
		boolean d = ("true".equals(s));
		getCrossNumberModel().setThroughRow(d);
	}
	
	public void save() throws Exception {
		CrossNumberField field = getCrossNumberModel().getField();
		Properties p = new Properties();
		p.setProperty("width", "" + field.getWidth());
		p.setProperty("height", "" + field.getHeight());
		p.setProperty("mask", serialize(field.getMask()));
		p.setProperty("hsum", serialize(field.getHSum()));
		p.setProperty("vsum", serialize(field.getVSum()));
		p.setProperty("differentThroughRow", "" + getCrossNumberModel().isThroughRow());
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
		CrossNumberField field = getCrossNumberModel().getField();
		boolean hasSolution = getCrossNumberModel().isShowingSolution();
		b.print("<html>\n");
		b.print("<p>Condition</p>\n");
		writeTable(b, field, false);
		if(hasSolution) {
			b.print("<p>Solution</p>\n");
			writeTable(b, field, true);
		}
		b.print("</html>\n");
		b.close();
	}
	
	void writeTable(PrintWriter b, CrossNumberField field, boolean saveSolution) {
		b.print("  <table>");
		for (int h = 0; h < field.getHeight(); h++) {
			b.print("\n    <tr>\n");
			for (int v = 0; v < field.getWidth(); v++) {
				b.print("<td width=\"30\" height=\"30\" align=\"center\"");
				int p = h * field.getWidth() + v;
				int state = field.getStatus(p);
				String color = (state == 0) ? "black" : // outside
				               (state == 4) ? "ffffff" : // number
				               "blue";                  // sums  
				b.print(" bgcolor=\"" + color + "\"");
				b.print(">");
				String ch = null;
				if(saveSolution && state == 4) {
					ch = "" + field.getValue(p);
				} else {
					ch = getSettingValueB(p, field, state);
				}
				if(ch != null) b.print(ch);
				b.print("</td>\n");
			}
			b.print("    </tr>");
		}
		b.print("\n  </table>\n");
	}
	
	private String getSettingValueA(int p, CrossNumberField field, int state) {
		String ch = null;
		if(state == 1) {
			ch = "&nbsp;";
			ch += (field.getHSum(p) > 0) ? "" + field.getHSum(p) : "";
			ch += "<br>";
			ch += "&nbsp;";
		} else if(state == 2) {
			ch = "&nbsp;<br>";
			ch += (field.getVSum(p) > 0) ? "" + field.getVSum(p) : "";
			ch += "&nbsp;";
		} else if(state == 3) {
			ch = "&nbsp;";
			ch += (field.getHSum(p) > 0) ? "" + field.getHSum(p) : "-";
			ch += "<br>";
			ch += (field.getVSum(p) > 0) ? "" + field.getVSum(p) : "-";
			ch += "&nbsp;";
		}
		return ch;
	}

	private String getSettingValueB(int p, CrossNumberField field, int state) {
		String ch = null;
		if(state == 1) {
			ch = (field.getHSum(p) > 0) ? "" + field.getHSum(p) : "";
		} else if(state == 2) {
			ch = (field.getVSum(p) > 0) ? "" + field.getVSum(p) : "";
		} else if(state == 3) {
			ch = (field.getHSum(p) > 0) ? "  " + field.getHSum(p) : "  -";
			ch += "|";
			ch += (field.getVSum(p) > 0) ? "" + field.getVSum(p) + "  " : "-  ";
		}
		return ch;
	}
}
