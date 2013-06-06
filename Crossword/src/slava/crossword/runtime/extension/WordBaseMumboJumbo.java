package slava.crossword.runtime.extension;

import java.util.List;

import slava.crossword.preference.CrosswordPreference;
import slava.crossword.runtime.WordBase;

public class WordBaseMumboJumbo extends WordBase {

    public void loadSourceList() {}

    public void saveSourceList() {}

    public void setSourceList(List l) {
    	invalidate();
    }

    public void addSource(String source) {}

    public void readSources() {
        allwords.clear();
//        CrosswordPreference p = CrosswordPreference.getInstance();
        generateWords();
        words = (byte[][])allwords.toArray(new byte[0][]);
    }

    void generateWords() {
    	for (int n = 4; n < 10; n++) generateWords(n);
    }

    void generateWords(int n) {
        double a = n / 2;
        int t = 1;
        for (int i = 0; i < n; i++) t = t * 2;
        for (int k = 0; k < t; k++) {
        	byte[] b = decode(k, n);
        	if(Math.abs(a - getACount(b)) < 0.7d) {
//        		if(Math.random() > 0.33) continue;
        		allwords.add(b);
        	}
        }
    }
    
    byte[] decode(int k, int s) {
    	byte[] b = new byte[s];
    	int i = 0;
    	while(k > 0) {
    		b[i] = (byte)(k % 2);
    		k = k / 2;
    		i++;
    	}
    	return b;
    }
    
    int getACount(byte[] b) {
    	int c = 0;
    	for (int i = 0; i < b.length; i++) {
    		if(b[i] == 0) c++;
    	}
    	return c;
    }
}
