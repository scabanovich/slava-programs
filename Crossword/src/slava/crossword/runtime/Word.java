package slava.crossword.runtime;

public class Word {
    int length;
    boolean occupied = false;
    Cell[] cells;
    byte[] pattern; //tempdata
    byte[][][] variantsStack;
    int variant = -1;

    public Word(Cell[] cells) {
        length = cells.length;
        this.cells = cells;
        variantsStack = new byte[length + 1][][];
        pattern = new byte[length];
    }

    public Cell[] getCells() {
        return cells;
    }

    public byte[][] getVariants() {
        if(variant == -1) updateVariants();
        return variantsStack[variant];
    }

    public boolean isOccupied() {
        return occupied;
    }

    // to be called by initializator only
    public boolean computeOccupied() {
        for (int i = 0; i < cells.length; i++)
          if(!cells[i].isOccupied()) return false;
        return occupied = true;
    }

    public boolean accepts(byte[] word) {
        if(word.length != length) return false;
        for (int i = 0; i < length; i++)
          if(cells[i].isOccupied() && cells[i].getContent() != word[i]) return false;
        return true;
    }

    public void setWord(byte[] word) {
        for (int i = 0; i < length; i++) {
            if(cells[i].isOccupied()) continue;
            cells[i].setContent(word[i], this);
            Word w = cells[i].getCrossing(this);
            if(w != null) w.updateVariants();
        }
        occupied = true;
    }

    public void unset() {
        for (int i = 0; i < length; i++) {
            if(cells[i].contentSource != this) continue;
            Word w = cells[i].getCrossing(this);
            if(w != null && w.isOccupied()) continue;
            cells[i].clean();
            if(w != null) w.variant--;
        }
        occupied = false;
    }

    public void updateVariants() {
        for (int i = 0; i < cells.length; i++) pattern[i] = cells[i].getContent();
        ++variant;
        variantsStack[variant] = WordBase.instance.getWordsForPattern(pattern);
    }

    public byte[] getWord() {
        byte[] b = new byte[cells.length];
        for (int i = 0; i < b.length; i++)
          b[i] = cells[i].getContent();
        return b;
    }

}
