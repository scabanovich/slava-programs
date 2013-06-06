package slava.crossword.runtime;

public class Cell {
    int position;
    byte content = (byte)255;
    Word contentSource = null;
    Word hword;
    Word vword;

    public Cell(int position) {
        this.position = position;
    }

    public Word getCrossing(Word word) {
        return (word == hword) ? vword : (word == vword) ? hword : null;
    }

    public void setContent(byte i) {
        content = i;
    }

    public void setContent(byte i, Word contentSource) {
        content = i;
        this.contentSource = contentSource;
    }

    public void clean() {
        content = (byte)255;
        this.contentSource = null;
    }

    public byte getContent() {
        return content;
    }

    public boolean isOccupied() {
        return content != (byte)255;
    }

} 