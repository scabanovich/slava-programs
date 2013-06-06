package slava.crossword.runtime;

import java.io.*;
import java.util.*;
import slava.crossword.preference.*;
import slava.crossword.runtime.extension.WordBaseMumboJumbo;
//import slava.crossword.runtime.filter.WordFilter;

public class WordBase {
    public static final WordBase instance;
    
    static {
        CrosswordPreference p = CrosswordPreference.getInstance();
    	String wordBase = p.getString("wordBase", null);
    	if(wordBase != null && wordBase.startsWith("MumboJumbo")) {
    		instance = new WordBaseMumboJumbo();
    	} else {
    		instance = new WordBase();
    	}
    }

    protected LetterCoder coder;
    protected List sources = new ArrayList();
    protected Set allwords = new HashSet();
    protected Set excludedPatterns = new HashSet();
    protected UnilengthWordBase[] unilength = new UnilengthWordBase[25];
    protected boolean updated = false;
    protected IWordFilter filter;

    protected byte[][] words;

    public WordBase() {
        for (int i = 0; i < unilength.length; i++) unilength[i] = new UnilengthWordBase(i);
        setLetterCoder(new LetterCoder());
        loadSourceList();
        /*
         * Temporal!!! 
         */
//        setWordFilter(new WordFilter());
    }
    
    public void setWordFilter(IWordFilter filter) {
    	if(this.filter == filter) return;
    	this.filter = filter;
    	invalidate();
    }
    
    public IWordFilter getWordFilter() {
    	return filter;
    }

    public void loadSourceList() {
        CrosswordPreference p = CrosswordPreference.getInstance();
        String ds = p.getString("dictionaries", null);
        if(ds == null) return;
        StringTokenizer st = new StringTokenizer(ds, ";");
        while(st.hasMoreTokens()) {
            String t = st.nextToken().trim();
            File f = new File(t);
            if(f.isFile()) sources.add(t);
        }
    }

    public void saveSourceList() {
        CrosswordPreference p = CrosswordPreference.getInstance();
        String[] ps = (String[])sources.toArray(new String[0]);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ps.length; i++) sb.append(ps[i]).append(';');
        p.setString("dictionaries", sb.toString());
    }

    public List getSourceList() {
        return (List)((ArrayList)sources).clone();
    }

    public void setSourceList(List l) {
        if(equal(sources, l)) return;
        sources.clear();
        sources.addAll(l);
        invalidate();
    }

    private boolean equal(List a, List b) {
        if(a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++)
          if(!a.get(i).equals(b.get(i))) return false;
        return true;
    }

    public void setLetterCoder(LetterCoder coder) {
        this.coder = coder;
    }

    public LetterCoder getLetterCoder() {
        return coder;
    }

    public void addSource(String source) {
        if(sources.contains(source)) return;
        sources.add(source);
        invalidate();
    }

    public void invalidate() {
        updated = false;
    }

    public void update() {
        if(updated) return;
        updated = true;
        readSources();
        updateIndices();
    }

    public void readSources() {
        allwords.clear();
        Set cwords = readFiles();
        allwords.addAll(cwords);
/*        byte[][] swords = (byte[][])cwords.toArray(new byte[cwords.size()][]);
        for (int i = 0; i < swords.length; i++) {
            byte[] w = coder.toNumbers(swords[i]);
            if(coder.isCorrectWord(w)) allwords.add(w);
        }*/
        words = (byte[][])allwords.toArray(new byte[0][]);
    }

    Set readFiles() {
        Set set = new HashSet();
        Set sss = new HashSet();
        for (int i = 0; i < sources.size(); i++) {
            String filename = (String)sources.get(i);
            File f = new File(filename);
            if(!f.exists()) continue;
            byte[] bs = null;
            try {
              InputStream is = new FileInputStream(f);
              int length = is.available();
              bs = new byte[length];
              is.read(bs);
            } catch (Exception e) {}
            int k = 0;
            for (int j = 0; j < bs.length; j++) {
              if(bs[j] == (byte)'\n') {
                int wl = j - k - 1;
                byte[] w = new byte[wl];
                System.arraycopy(bs, k, w, 0, wl);
                if(w.length > 1 && w.length <= unilength.length) {
                    byte[] b = coder.toNumbers(w);
                    String s = UnilengthWordBase.toString(b);
                    if(coder.isCorrectWord(b) && !excludedPatterns.contains(s) && !sss.contains(s)) {
                    	if(checkFilter(b)) {
                        	set.add(b);
                        	sss.add(s);
                    	}
                    }
                }
                k = j + 1;
              }
            }
        }
        System.out.println("--->" + set.size());
        return set;
    }
    
    boolean checkFilter(byte[] w) {
    	return filter == null || filter.accept(w);
    }

    public byte[][] getWords() {
        return words;
    }

    public void updateIndices() {
        for (int i = 2; i < unilength.length; i++) {
            unilength[i].clear();
            unilength[i].init(getWords(), coder.getLetterCount());
            unilength[i].statistics();
        }
    }

    public byte[][] getWordsForPattern(byte[] pattern) {
        return unilength[pattern.length].getWordsForPattern(pattern);
    }

    public void cleanExcludedPatterns() {
        if(excludedPatterns.size() == 0) return;
        excludedPatterns.clear();
        invalidate();
    }

    public void addExcludedPattern(String s) {
        if(excludedPatterns.contains(s)) return;
        excludedPatterns.add(s);
        invalidate();
    }
    
    public void limitTo(Set set) {
    	allwords.clear();
    	allwords.addAll(set);
        words = (byte[][])allwords.toArray(new byte[0][]);
        updateIndices();
    }

    public static void main(String[] s) {
    }
}
