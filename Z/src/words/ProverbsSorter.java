package words;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ProverbsSorter {
	List<ProverbEntry> proverbs = new ArrayList<ProverbEntry>();

	static class ProverbEntry {
		String english;
		List<String> russian = new ArrayList<String>();

		ProverbEntry(String english) {
			this.english = english;
		}
	
		public void print() {
			System.out.println(english);
			for (String r: russian) {
				System.out.println("   " + r);
			}
		}

		public void printTo(StringBuilder sb) {
			sb.append(english).append("\n");
			for (String r: russian) {
				sb.append("   ").append(r).append("\n");
			}
		}

		public boolean matchesEnglish(String filter) {
			return english.toLowerCase().indexOf(filter) >= 0;
		}

		public boolean matchesRussian(String filter) {
			for (String r: russian) {
				if(r.toLowerCase().indexOf(filter) >= 0) return true;
			}
			return false;
		}
	}

	public void addFile(String filename) {
		String text = FileUtil.readFile(new File(filename), "Windows-1251");
		for (int i = 0; i < 100; i++) {
			
		}
		StringTokenizer st = new StringTokenizer(text, "\r\n");
		while(st.hasMoreTokens()) {
			String line = st.nextToken().trim();
			int dot1 = line.indexOf('.');
			if(dot1 < 0) continue;
			int dot2 = line.indexOf('.', dot1 + 1);
			if(dot2 < 0) continue;
			String english = line.substring(dot1 + 1, dot2 + 1).trim();
			if(english.length() == 0) continue;
			ProverbEntry entry = new ProverbEntry(english);
			proverbs.add(entry);
			int sr = line.indexOf("\u0421\u0440.", dot2 + 1);
			if(sr >= 0) {
				String seq = line.substring(sr + 3).trim();
				StringTokenizer st2 = new StringTokenizer(seq, ".");
				while(st2.hasMoreTokens()) {
					String r = st2.nextToken().trim();
					if(r.length() > 0) {
						entry.russian.add(r + ".");
					}
				}
			}
//			entry.print();
		}
	}

	public static void main(String[] args) {
//		ProverbsSorter p = new ProverbsSorter();
//		p.addFile("/home/slava/Private/English/proverbs.txt");
		new ProverbsVisualizer();
		
	}
}
