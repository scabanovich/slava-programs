package ic2006_2;

import java.util.*;

public class LogodromWords {
	static String[] WORDS = {
		"ARARAT",
		"ABRACADABRA", 
		//"KTITOR", "ROTATOR",
		"TRATTA", 
		"DARTS", "OSTERIA", 
		"ANDORRA",
		"KTITOR", "ROTATOR",
		"ADAMS", "DANCE", "ACCORD",
		"STOSS",
		  
	};
	
	String[] words;

	char[] letters;
	Map numbers = new HashMap();
	
	int sequenceSize;
	int[] numericSequence;
	int[] wordBeginnings;

	int[] statistics;
	
	public LogodromWords() {
		setWords(WORDS);
	}
	
	public void setWords(String[] ws) {
		words = ws;
		sequenceSize = 0;
		for (int i = 0; i < words.length; i++) sequenceSize += words[i].length();
		numericSequence = new int[sequenceSize];
		wordBeginnings = new int[sequenceSize];
		int index = 0;
		for (int i = 0; i < words.length; i++) {
			wordBeginnings[index] = 1;
			for (int k = 0; k < words[i].length(); k++) {
				char c = words[i].charAt(k);
				numericSequence[index] = getCharIndex(c);
				index++;
			}
		}
		letters = new char[numbers.size()];
		Iterator it = numbers.keySet().iterator();
		while(it.hasNext()) {
			Character c = (Character)it.next();
			int i = ((Integer)numbers.get(c)).intValue();
			letters[i] = c.charValue();
		}
		statistics = new int[letters.length];
		for (int i = 0; i < numericSequence.length; i++) {
			statistics[numericSequence[i]]++;
		}
		for (int i = 0; i < statistics.length; i++) {
			System.out.println(" " + letters[i] + " " + statistics[i]);
		}
	}
	
	int getCharIndex(char c) {
		Character ch = new Character(c);
		Integer i = (Integer)numbers.get(ch);
		if(i == null) {
			i = new Integer(numbers.size());
			numbers.put(ch, i);
		}
		return i.intValue();
	}
	


}
