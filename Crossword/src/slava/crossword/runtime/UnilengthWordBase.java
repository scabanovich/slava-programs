package slava.crossword.runtime;

import java.util.*;

public class UnilengthWordBase {
    int length;
    byte[][] words = new byte[0][];
    Map patterns = new HashMap();

    public UnilengthWordBase(int length) {
        this.length = length;
    }

    public void init(byte[][] ws, int letterCount) {
        int size = 0;
        for (int i = 0; i < ws.length; i++) if(ws[i].length == length) {
            ++size;
        }
        words = new byte[size][];
        int j = 0;
        for (int i = 0; i < ws.length; i++) if(ws[i].length == length) {
            words[j] = ws[i];
            ++j;
        }
    }

    public int size() {
        return words.length;
    }

    public void statistics() {
        //System.out.println(" " + length + " " + size());
    }

    public static String toString(byte[] pattern) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < pattern.length; i++) sb.append(pattern[i]);
        return sb.toString();
    }

    public byte[][] getWordsForPattern(byte[] pattern) {
        byte[][] ws = (byte[][])patterns.get(toString(pattern));
        if(ws != null) return ws;
        int p = getPosition(pattern);
        if(p < 0) {
            patterns.put(toString(pattern), words);
            return words;
        }
        byte b = pattern[p];
        pattern[p] = (byte)255;
        ws = getWordsForPattern(pattern);
        pattern[p] = b;
        int size = 0;
        for (int i = 0; i < ws.length; i++) if(ws[i][p] == b) ++size;
        byte[][] wsc = new byte[size][];
        size = 0;
        for (int i = 0; i < ws.length; i++) if(ws[i][p] == b) {
            wsc[size] = ws[i];
            ++size;
        }
        if(size > 20) {
            patterns.put(toString(pattern), wsc);
        } else if(size > 1) {
            int i = (int)(size * Math.random());
            byte[] k = wsc[i];
            wsc[i] = wsc[0];
            wsc[0] = k;
        }
        return wsc;

    }

    private int getPosition(byte[] pattern) {
        for (int i = 0; i < pattern.length; i++) if(pattern[i] != (byte)255) return i;
        return -1;
    }

    public void clear() {
        words = new byte[0][];
        patterns.clear();
    }

}
