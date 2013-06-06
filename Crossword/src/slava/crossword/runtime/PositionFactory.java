package slava.crossword.runtime;

import java.util.*;

public class PositionFactory {
    Cell[] cells;
    public PositionFactory() {}

    public Word[] create(int[][] jp, int[] net, byte[] content) {
        ArrayList words = new ArrayList();
        cells = new Cell[net.length];
        for (int i = 0; i < net.length; i++) if(net[i] == 1) {
            cells[i] = new Cell(i);
            if(content[i] != (byte)255) {
                cells[i].setContent(content[i]);
            }
        }
        for (int i = 0; i < net.length; i++) if(net[i] == 1) {
            int ia = jp[i][2];
            if(ia < 0 || net[ia] == 0) {
                int l = getWordLength(jp, net, i, 0);
                if(l > 1) {
                    Cell[] cs = getCells(cells, jp, i, 0, l);
                    Word w = new Word(cs);
                    words.add(w);
                    for (int j = 0; j < cs.length; j++) cs[j].hword = w;
                }
            }
            int ib = jp[i][3];
            if(ib < 0 || net[ib] == 0) {
                int l = getWordLength(jp, net, i, 1);
                if(l > 1) {
                    Cell[] cs = getCells(cells, jp, i, 1, l);
                    Word w = new Word(cs);
                    words.add(w);
                    for (int j = 0; j < cs.length; j++) cs[j].vword = w;
                }
            }
        }
        return (Word[])words.toArray(new Word[0]);
    }

    int getWordLength(int[][] jp, int[] net, int i, int d) {
        int l = 0;
        while(i >= 0 && net[i] == 1) {
            ++l;
            i = jp[i][d];
        }
        return l;
    }

    Cell[] getCells(Cell[] cells, int[][] jp, int i, int d, int l) {
        Cell[] cs = new Cell[l];
        for (int k = 0; k < l; k++) {
            cs[k] = cells[i];
            i = jp[i][d];
        }
        return cs;
    }

}