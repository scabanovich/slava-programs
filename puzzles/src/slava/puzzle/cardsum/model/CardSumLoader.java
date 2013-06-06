package slava.puzzle.cardsum.model;

import java.io.*;
import java.util.*;
import slava.puzzle.template.model.PuzzleLoader;

public class CardSumLoader extends PuzzleLoader {
	
	public CardSumLoader() {
		setRoot("/data/cards");
		initName("cards_");
	}

	public CardSumModel getPentaLettersModel() {
		return (CardSumModel)getModel();
	}
	
	public void load() throws Exception {
		File f = getFile();
		if(!f.isFile()) throw new Exception("File does not exist.");
		Properties p = new Properties();
		p.load(new FileInputStream(f));
		CardSumField field = (CardSumField)getPentaLettersModel().getField();
//		String size = p.getProperty("size");
//		int[] sz = deserialize(size);
//		field.setSize(sz[0], sz[1]);		
		String cards = p.getProperty("cards");
		int[] ls = deserialize(cards);
		for (int i = 0; i < field.size(); i++) field.setCard(i, ls[i]);
		String sums = p.getProperty("sums");
		int[] ss = deserialize(sums);
		for (int i = 0; i < field.getWidth(); i++) field.setSum(i, ss[i]);
	}
	
	public void save() throws Exception {
		CardSumField field = (CardSumField)getPentaLettersModel().getField();
		Properties p = new Properties();
		p.setProperty("width", "" + field.getWidth());
		p.setProperty("height", "" + field.getHeight());
		p.setProperty("cards", serialize(field.cards));
		p.setProperty("sums", serialize(field.sums));
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
		CardSumField field = getPentaLettersModel().getField();
		b.print("<html>\n");
		b.print("  <table>");
		for (int i = 0; i < field.size(); i++) {
			if(field.x(i) == 0) b.print("\n    <tr>\n");
			b.print("<td width=\"30\" height=\"30\" align=\"center\"");
			b.print(" bgcolor=\"ffff00\"");
			b.print(">");
			String ch = field.getVisual(field.getCard(i));
			b.print(ch);
			b.print("</td>\n");
			if(field.x(i) == field.getWidth() - 1) b.print("    </tr>");
		}
		for (int i = 0; i < field.getWidth(); i++) {
			b.print("<td width=\"30\" height=\"30\" align=\"center\">");
			int s = field.getSum(i);
			if(s >= 0) b.print("" + s);
			b.print("</td>\n");
		}
		b.print("\n  </table>\n");
		b.print("</html>\n");
		b.close();
	}
	
}
