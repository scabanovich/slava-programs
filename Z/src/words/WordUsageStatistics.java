package words;

import java.io.*;
import java.util.*;

public class WordUsageStatistics {
	Map statistics;
	int maxOcc = 0;
	
	
	public void init() {
		statistics = new HashMap();
		maxOcc = 0;
	}
	
	public void addFile(String filename) {
		File f = new File(filename);
		String text = "";
		int cursor = 0;
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			StringBuffer sb = new StringBuffer();
			String s = null;
			while((s = r.readLine()) != null) sb.append(s).append('\n');
			text = sb.toString();
		} catch (Exception e) {}
		int length = text.length();
		String word = "";
		while(cursor < length) {
			char c = text.charAt(cursor);
			if(!Character.isLetter(c)) {
				if(word.length() > 2) {
					addWord(word.toLowerCase());
				}
				word = "";
			} else {
				word += c;
			}
			cursor++;
		}
///		checkEndings();
	}
	
	void addWord(String word) {
		Object o = statistics.get(word);
		int q = (o == null) ? 1 : ((Integer)o).intValue() + 1;
		if(q > maxOcc) maxOcc = q;
		statistics.put(word, new Integer(q));
	}
	
	public void checkEndings() {
		String[] ws = (String[])statistics.keySet().toArray(new String[0]);
		for (int i = 0; i < ws.length; i++) {
			for (int j = 0; j < ENDINGS.length; j++) {
				if(ws[i].endsWith(ENDINGS[j].modified)) {
					String w = ws[i].substring(0, ws[i].length() - ENDINGS[j].modified.length()) + ENDINGS[j].unmodified;
					if(statistics.containsKey(w)) {
						int q1 = ((Integer)statistics.get(w)).intValue();
						int q2 = ((Integer)statistics.remove(ws[i])).intValue();
						int q = q1 + q2;
						if(maxOcc < q) maxOcc = q;
						statistics.put(w, new Integer(q));
						break;
					}
				}
			}
		}
	}
	
	public void printStatistics() {
		System.out.println("Total word count=" + statistics.size());
		Set[] distribution = new TreeSet[maxOcc + 1];
		for (int i = 0; i <= maxOcc; i++) distribution[i] = new TreeSet();
		Iterator it = statistics.keySet().iterator();
		while(it.hasNext()) {
			String word = it.next().toString();
			int q = ((Integer)statistics.get(word)).intValue();
			if(q > 0) distribution[q].add(word);
		}
		int wt = 0;
		for (int i = maxOcc; i >= 0 && wt < 6000; i--) {
			Iterator wit = distribution[i].iterator();
			while(wit.hasNext()) {
				System.out.println(wit.next() + " " + i);
				wt++;
			}
		}
	}
	
	static Ending[] ENDINGS = new Ending[]{
		new Ending("", "ed"),
		new Ending("e", "ed"),
		new Ending("", "es"),
		new Ending("y", "ies"),
		new Ending("", "s"),
		new Ending("", "ing"),
		new Ending("e", "ing"),
	};
	
	static class Ending {
		String unmodified;
		String modified;
		public Ending(String unmodified, String modified) {
			this.unmodified = unmodified;
			this.modified = modified;
		}
	}
	
	public static void main(String[] args) {
		WordUsageStatistics wus = new WordUsageStatistics();
		wus.init();
		wus.addFile("C:/slava/books/Haggard/Morning Star_files/morningstar.htm");
		wus.addFile("C:/slava/books/Dumas/The Three Musketeers_files/threemusket.htm");
		wus.addFile("C:/slava/books/Dumas/The Count of Monte Cristo_files/crsto.htm");
		wus.addFile("C:/slava/books/Vern/The Mysterious Island_files/milnd.htm");
		wus.checkEndings();
		wus.printStatistics();
	}

}
