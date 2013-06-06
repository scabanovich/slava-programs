package match2008;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class WordReader {
	String[] sourceFiles;
	Set words = new TreeSet();
	
	public void setSourceFiles(String[] sourceFiles) {
		this.sourceFiles = sourceFiles;
	}

	public void load() {
		for (int i = 0; i < sourceFiles.length; i++) {
			try {
				FileReader fr = new FileReader(sourceFiles[i]);
				BufferedReader br = new BufferedReader(fr);
				String ln = null;
				while((ln = br.readLine()) != null) {
					String s = ln.trim();
					if(s.length() == 0) continue;
					StringTokenizer st = new StringTokenizer(s, " \t\r\n");
					while(st.hasMoreTokens()) {
						String t = st.nextToken();
						if(t.length() > 4) {
							words.add(t);
						}
					}
				}
			} catch (IOException e) {
				
			}
		}
		System.out.println(words.size());		
	}

	public void save(String file, int minLength) {
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			String[] ws = asArray();
			for (int i = 0; i < ws.length; i++) {
				if(ws[i].length() < minLength) continue;
				bw.write(ws[i]);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			
		}
	}

	public void filterOutWordsWithRepeatingLetters() {
		Iterator it = words.iterator();
		while(it.hasNext()) {
			String s = (String)it.next();
			if(hasRepeatingLetters(s)) {
				it.remove();
			}
		}
		System.out.println(words.size());
		int[] distr = new int[20];
		String[] ws = asArray();
		for (int i = 0; i < ws.length; i++) {
			distr[ws[i].length()]++;
			if(ws[i].length() > 12) {
				System.out.println(ws[i]);
			}
		}
		for (int i = 4; i < distr.length; i++) {
			if(distr[i] > 0) {
				System.out.println("i=" + i + " n=" + distr[i]);
			}
		}
	}
	
	String[] asArray() {
		return (String[])words.toArray(new String[0]);
	}

	boolean hasRepeatingLetters(String s) {
		for (int i = 0; i < s.length(); i++) {
			for (int j = i + 1; j < s.length(); j++) {
				if(s.charAt(i) == s.charAt(j)) return true;
			}
		}
		return false;
	}

	boolean areOfDifferentLetters(String s1, String s2) {
		for (int i = 0; i < s1.length(); i++) {
			char c1 = s1.charAt(i);
			for (int j = 0; j < s2.length(); j++) {
				char c2 = s2.charAt(j);
				if(c1 == c2) return false;
			}
		}
		return true;
	}

	void collectNoncrossingWords() throws IOException {
//		String f = "f:/slava/dictionaries/norepeat_x.txt";
//		FileWriter fw = new FileWriter(f);
//		BufferedWriter bw = new BufferedWriter(fw);
		String[] ws = asArray();
		for (int i = 0; i < ws.length; i++) {
			if(ws[i].length() < 10) continue;
			Set set = new TreeSet();
			for (int j = 0; j < ws.length; j++) {
//				if(ws[j].length() < 8) continue;
				if(i == j || !areOfDifferentLetters(ws[i], ws[j])) continue;
				set.add(ws[j]);
			}
			if(set.size() > 0) {
				System.out.println(ws[i] + " " + ws[i].length() + " - " + set.size());
			}
//			if(set.size() > 59) {
//				bw.write(ws[i] + " " + set.size());
//				bw.newLine();
//			}
		}
//		bw.close();
	}

	int filter(int min) throws IOException {
		String file = "f:/slava/dictionaries/filter.txt";
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String f =  br.readLine();
		String f1 ="";
		String f2 = "";
		for (int i = 0; i < f.length(); i++) {
			if((Math.random() < 0.59
//				|| i == 4
				) //&& (i != 0)
					) {
				f1 += f.charAt(i); 
			} else {
				f2 += f.charAt(i); 
			}
		}
		
		String[] ws = asArray();
		Set[] c1 = new TreeSet[17];
		Set[] c2 = new TreeSet[17];
		Set r1 = new TreeSet();
		Set r2 = new TreeSet();
		for (int i = 0; i < ws.length; i++) {
			int length = ws[i].length();
			if(ws[i].charAt(length - 1) == f.charAt(28)) continue;
			if(areOfDifferentLetters(f1, ws[i])) {
				if(c1[length] == null) c1[length] = new TreeSet();
				c1[length].add(ws[i]);
			}
			if(areOfDifferentLetters(f2, ws[i])) {
				if(c2[length] == null) c2[length] = new TreeSet();
				c2[length].add(ws[i]);
			}
		}
		int sz = 0;
		int wc = 0;
		for (int i = c1.length - 1; i >= 5; i--) {
			if(c1[i] == null || c1[i].size() == 0) continue;
			int dc = c1[i].size();
			if(wc + dc > 8) dc = 8 - wc;
			sz += i * dc;
			wc += dc;
			Iterator i1 = c1[i].iterator();
			for (int k1 = 0; k1 < dc; k1++) r1.add(i1.next());
			if(wc >= 8) break;
		}
		if(wc < 8) return 0;
		wc = 0;
		for (int i = c2.length - 1; i >= 5; i--) {
			if(c2[i] == null || c2[i].size() == 0) continue;
			int dc = c2[i].size();
			if(wc + dc > 8) dc = 8 - wc;
			sz += i * dc;			
			wc += dc;
			Iterator i2 = c2[i].iterator();
			for (int k2 = 0; k2 < dc; k2++) r2.add(i2.next());
			if(wc >= 8) break;
		}
		if(wc < 8) return 0;
		if(sz < min) return sz;
		
		
		if(sz >= min) {
			file = "f:/slava/dictionaries/result.txt";
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			Iterator it = r1.iterator();
			while(it.hasNext()) {
				bw.write(it.next().toString());
				bw.newLine();
			}
			bw.newLine();
			it = r2.iterator();
			while(it.hasNext()) {
				bw.write(it.next().toString());
				bw.newLine();
			}
			bw.close();
		}
		return sz;
	}

	public static void makeNorepeat() {
		String[] sourceFiles = {
			"f:/slava/dictionaries/long/lexi1.enc",
			"f:/slava/dictionaries/long/lexi2.enc",
			"f:/slava/dictionaries/long/lexi3.enc",
			"f:/slava/dictionaries/long/lexi4.enc",
			"f:/slava/dictionaries/long/lexi5.enc",
			"f:/slava/dictionaries/long/lexi6.enc",
		};
		WordReader p = new WordReader();
		p.setSourceFiles(sourceFiles);
		p.load();
		p.filterOutWordsWithRepeatingLetters();
		p.save("f:/slava/dictionaries/norepeat7.txt", 5);
	}

	static void process() {
		String[] sourceFiles = {
			"f:/slava/dictionaries/norepeat7.txt" 
		};
		WordReader p = new WordReader();
		p.setSourceFiles(sourceFiles);
		p.load();
		try {
//			p.collectNoncrossingWords();
			int min = 100;
			while(min < 160) {
				int sz = p.filter(min);
				if(sz > min) {
					min = sz;
					System.out.println("-->" + sz);
				}
			}
		} catch (IOException e) {
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		makeNorepeat();
		process();
	}

}
