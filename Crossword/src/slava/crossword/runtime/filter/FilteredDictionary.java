package slava.crossword.runtime.filter;

import java.io.*;
import slava.crossword.runtime.*;

public class FilteredDictionary {
	WordBase base;
	IWordFilter filter;
	File f;
	
	public FilteredDictionary() {
		base = WordBase.instance;
	}
	
	public void setFilter(IWordFilter filter) {
		base.setWordFilter(filter);
		base.update();
	}
	
	public void setOutput(String path) {
		f = new File(path);
	}
	
	public void execute() {
		byte[][] words = base.getWords();
		byte[] rs = new byte[100000];
		int length = 0;
		for (int i = 0; i < words.length; i++) {
			for (int j = 0; j < words[i].length; j++) {
				byte b = base.getLetterCoder().getSmallChar(words[i][j]);
				rs[length] = b;
				length++;
			}
			rs[length] = (byte)'\r';
			length++;
			rs[length] = (byte)'\n';
			length++;
		}
		try {
			FileOutputStream w = new FileOutputStream(f);
			w.write(rs, 0, length);
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FilteredDictionary d = new FilteredDictionary();
		d.setFilter(new WordFilter());
		d.setOutput("filtered.txt");
		d.execute();
	}

}
