package year2013;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class LongestEqualSubstringsFinder {
	String text;
	Slice longestSlice = null;

	public LongestEqualSubstringsFinder() {}

	public void setText(String text) {
		this.text = text;
	}

	public Slice run() {
		longestSlice = new Slice(0);
		for (int i = 0; i < text.length(); i++) {
			longestSlice.positions.add(i);
		}
		longestSlice.run();
		System.out.println(longestSlice.toString());
		return longestSlice;
	}

	class Slice {
		int length;
		ArrayList<Integer> positions;
		
		public Slice(int length) {
			this.length = length;
			positions = new ArrayList<Integer>();
		}
	
		boolean areAllNextLettersSame() {
			if(positions.size() < 2) {
				return false;
			}
			if(positions.get(positions.size() - 1) + length >= text.length()) {
				return false;
			}
			char ch = text.charAt(positions.get(0) + length);
			for (int i = 1; i < positions.size(); i++) {
				if(ch != text.charAt(positions.get(i) + length)) return false;
			}
			return true;
		}

		public void run() {
			if(positions.size() < 2) {
				return;
			} else if(positions.size() == 2) {
				int p1 = positions.get(0);
				int p2 = positions.get(1);
				if(p1 > 0 && text.charAt(p1 - 1) == text.charAt(p2 - 1)) {
					return; //Longer substrings exist.
				}
				while(p2 + length < text.length()) {
					char c1 = text.charAt(p1 + length);
					char c2 = text.charAt(p2 + length);
					if(c1 == c2) {
						length++;
						if(longestSlice.length < this.length) {
							longestSlice = this;
						}
					} else {
						break;
					}
				}
				return;
			}
			Map<Character, Slice> map = new HashMap<Character, Slice>();
			for (int i = 0; i < positions.size(); i++) {
				int p = positions.get(i);
				if(p + length >= text.length()) continue;
				char ch = text.charAt(p + length);
				Slice s = map.get(ch);
				if(s == null) {
					s = new Slice(this.length + 1);
					map.put(ch, s);
				}
				s.positions.add(p);
			}
			Iterator<Character> cs = map.keySet().iterator();
			while(cs.hasNext()) {
				Character ch = cs.next();
				Slice s = map.get(ch);
				if(s.positions.size() < 2) {
					cs.remove();
				} else if(longestSlice.length < s.length) {
					longestSlice = s;
				}
			}
			for (Slice s: map.values()) {
				while(s.areAllNextLettersSame()) {
					s.length++;
				}
				if(s.length > longestSlice.length) {
					longestSlice = s;
				}
				s.run();
			}
		}

		public String toString() {
			int s = positions.size();
			int p1 = positions.get(0);
			StringBuilder sb = new StringBuilder();
			sb.append("Length=").append(length);
			if(length < 200) {
				sb.append("  text=").append(text.substring(p1, p1 + length));
			} else {
				sb.append("  text=").append(text.substring(p1, p1 + 200)).append("...");
			}
			sb.append("  occurances_number=").append(s);
			sb.append("  first_occurance_at=").append(p1);
			if(s > 1) {
				sb.append("  second_occurance_at=").append(positions.get(1));
			}
			return sb.toString();
		}
		
	}

	public static String generateRandomText(int length, int distinctSymbols) {
		Random seed = new Random();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			char c = (char)('0' + seed.nextInt(distinctSymbols));
			sb.append(c);
		}
		return sb.toString();//+ sb.toString();
	}

	public static String generateRandomText(int length, int rLength, int repeat, int maxRRepeat, int distinctSymbols) {
		String s = generateRandomText(rLength, distinctSymbols);
		Random seed = new Random();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < repeat; i++) {
			sb.append(generateRandomText(length, distinctSymbols));
			int q = seed.nextInt(maxRRepeat) + 1;
			for (int j = 0; j < maxRRepeat; j++) {
				sb.append(s);
			}
		}
//		sb.append(generateRandomText(length, distinctSymbols));
		return sb.toString();//+ sb.toString();
	}

	public static void main(String[] args) {
		SubsequentEqualSubstringsFinder f = new SubsequentEqualSubstringsFinder();
//		f.find(generateRandomText(4000000, 9, 2, 2, 28));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 100000; i++) sb.append("abc");
		String s = sb.toString();
		f.find(s + "x" + s + "yy");
		f.printResults();
		
//		LongestEqualSubstringsFinder p = new LongestEqualSubstringsFinder();
//		String s = generateRandomText(60, 3);
//		p.setText(generateRandomText(4000000, 2) + s + generateRandomText(4000000, 2) + s + generateRandomText(4000000, 2));
//		p.run();
	}
}
