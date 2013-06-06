package slava.crossword.dialog.statistics;

import java.util.*;
import slava.crossword.runtime.*;

public class StatisticsProvider {
    String[][] statistics = new String[0][4];

    public void init(int[][] jp, int[] net, byte[] content) {
        PositionFactory factory = new PositionFactory();
        Word[] ws = factory.create(jp, net, content);
        int[][] is = new int[30][3];
        for(int i = 0; i < is.length; i++) is[i][0] = i + 2;
        for (int i = 0; i < ws.length; i++) {
            Word word = ws[i];
            Cell[] cs = word.getCells();
            if(cs.length < 2) continue;
            int q = cs.length - 2;
            is[q][1]++;
            for (int j = 0; j < cs.length; j++)
                if(cs[j].getCrossing(word) != null) ++is[q][2];
        }
        ArrayList l = new ArrayList();
        int total = 0;
        for (int i = is.length - 1; i >= 0; i--) {
            if(is[i][1] <= 0) continue;
            total += is[i][1];
            String wl = "" + is[i][0];
            String n = "" + is[i][1];
            String c = "" + ((float)is[i][2]) / is[i][1];
            byte[] b = new byte[i + 2];
            for (int j = 0; j < b.length; j++) b[j] = (byte)255;
            String wc = "" + WordBase.instance.getWordsForPattern(b).length;
            l.add(new String[]{wl, n, c, wc});
        }
        l.add(new String[]{"total", "" + total, "", ""});
        statistics = (String[][])l.toArray(new String[0][]);
    }

    public String[][] getStatistics() {
        return statistics;
    }

    public String[] getHeaders() {
        return new String[]{"Length", "Number", "Crossings", "Dictionary"};
    }

}
