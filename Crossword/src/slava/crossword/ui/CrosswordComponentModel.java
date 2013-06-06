package slava.crossword.ui;

import slava.crossword.runtime.*;
import slava.crossword.undo.UndoManager;
import slava.crossword.undo.UndoableChange;
import slava.crossword.undo.ValueSetChange;
import slava.crossword.preference.*;
import slava.crossword.dialog.statistics.*;

public class CrosswordComponentModel {
    int width = 0;
    int height = 0;
    int size = 0;
    public static final int NET_BLACK = 0;
    public static final int NET_WHITE = 1;
    public static final int NET_GREY = 2;
    int[] net = new int[0];
    char[] content = new char[0];
    int[] x;
    int[] y;
    int[][] jp;
    CrosswordComponent listener;
    int colorMode = 0;
    StorageModel storage = new StorageModel();

    public CrosswordComponentModel() {}

    public void addListener(CrosswordComponent listener) {
        this.listener = listener;
    }

    public void setDefaultDimensions() {
        CrosswordPreference p = CrosswordPreference.getInstance();
        int w = p.getInt("net.width", 10);
        int h = p.getInt("net.height", 15);
        setDimensions(w, h);
    }

    public void setDimensions(int width, int height) {
        if(this.width == width && this.height == height) return;
        this.width = width;
        this.height = height;
        init();
        fireSizeChanged();
    }

    public void setNetAt(int i, int value) {
        if(value == getNetAt(i)) return;
        net[i] = value;
        listener.paintCell(i);
    }

    public void setCharAt(int i, byte b) {
        content[i] = WordBase.instance.getLetterCoder().getWindowsChar(b);
    }

    public int getNetAt(int i) {
        return net[i];
    }

    public boolean processKey(int i, char c) {
        if(c == ' ') {
            content[i] = c;
            listener.paintCell(i);
            return true;
        } else {
            if(WordBase.instance.getLetterCoder().getWindowsCharIndex(c) == (byte)255) return false;
            content[i] = c;
            listener.paintCell(i);
            return true;
        }
    }
    
    public boolean canProcessKey(int i, char c) {
    	if(i < 0 || i >= content.length) return false;
    	if(content[i] == c) return false;
    	if(c == ' ') return true;
		if(WordBase.instance.getLetterCoder().getWindowsCharIndex(c) == (byte)255) return false;
		return true;
    }
    
    public int getSize() {
    	return size;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void init() {
        size = width * height;
        net = new int[size];
        content = new char[size];
        x = new int[size];
        y = new int[size];
        for (int i = 0; i < size; i++) {
            net[i] = 1;
            content[i] = ' ';
            x[i] = (i % width);
            y[i] = (i / width);
        }
        jp = Util.getJumps(width, height);
    }

    public void fireSizeChanged() {
        if(listener != null) listener.updateSize();
    }

    public char[] getCharContent() {
        return content;
    }

    public void run() {
        byte[] b = getRuntimeCharacters();
        int[] n = getRuntimeNet();
        CrosswordSolveDialog d = new CrosswordSolveDialog();
        d.execute(jp, n, b);
        int solutionCount = d.getSolver().getSolutionCount();
		if(d.getSolver().getSolutionLimit() > 1) {
			System.out.println("solutionCount=" + solutionCount);
		}
        if(solutionCount > 0) {
             byte[] bs = d.getSolver().getSolution();
             UndoableChange change = new ValueSetChange(this, bs);
             change.redo();
             UndoManager.getInstance().addChange(change);
//             for (int i = 0; i < content.length; i++) {
//                 if(net[i] == 1) content[i] = WordBase.instance.getLetterCoder().getWindowsChar(bs[i]);
//             }
//             listener.repaint();
        }
    }

    public void excludeWords() {
        ExtractWordsHelper h = new ExtractWordsHelper();
        h.execute(jp, getRuntimeNet(), getRuntimeCharacters());
    }
    
    public void limitWordBase() {
        ExtractWordsHelper h = new ExtractWordsHelper();
        h.limitWordBase(jp, getRuntimeNet(), getRuntimeCharacters());
    }

    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int c) {
        colorMode = c;
    }

    public void nextColorMode() {
        colorMode++;
        if(colorMode > 2) colorMode = 0;
    }

    public StorageModel getStorage() {
        return storage;
    }

    public int[] getRuntimeNet() {
        int[] n = new int[net.length];
        for (int i = 0; i < net.length; i++) {
            n[i] = (net[i] == 1) ? 1 : 0;
        }
        return n;
    }

    public byte[] getRuntimeCharacters() {
        byte[] b = new byte[net.length];
        for (int i = 0; i < net.length; i++) {
            byte e = WordBase.instance.getLetterCoder().getWindowsCharIndex(content[i]);
            b[i] = (net[i] == 1) ? e : (byte)255;
        }
        return b;
    }

    public void cleanLetters() {
        for (int i = 0; i < size; i++)
          if(net[i] == 1) content[i] = ' ';
        listener.repaint();
    }

    public void statistics() {
        byte[] b = getRuntimeCharacters();
        int[] n = getRuntimeNet();
        StatisticsDialog d = new StatisticsDialog();
        d.execute(jp, n, b);
    }

}