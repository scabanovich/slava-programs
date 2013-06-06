package slava.crossword.runtime;

import java.util.*;

public class ExtractWordsHelper {

    public void execute(int[][] jp, int[] net, byte[] content) {
        PositionFactory factory = new PositionFactory();
        Word[] ws = factory.create(jp, net, content);
        WordBase base = WordBase.instance;
        for (int i = 0; i < ws.length; i++) {
            if(!ws[i].computeOccupied()) continue;
            byte[] b = ws[i].getWord();
            String pattern = UnilengthWordBase.toString(b);
            base.addExcludedPattern(pattern);
        }
    }

    public void limitWordBase(int[][] jp, int[] net, byte[] content) {
        PositionFactory factory = new PositionFactory();
        Word[] ws = factory.create(jp, net, content);
        WordBase base = WordBase.instance;
        Set set = new HashSet();
        Set s2 = new HashSet();
        for (int i = 0; i < ws.length; i++) {
            if(!ws[i].computeOccupied()) continue;
            byte[] b = ws[i].getWord();
            String pattern = UnilengthWordBase.toString(b);
            if(s2.contains(pattern)) continue;
            s2.add(pattern);
            set.add(b);
        }
        base.limitTo(set);
    }

}
