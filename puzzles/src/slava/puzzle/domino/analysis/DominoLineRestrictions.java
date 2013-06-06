package slava.puzzle.domino.analysis;

public class DominoLineRestrictions {
    int[][] hLines;
    int[][] vLines;

    int max;
    boolean[][] allowed;

    public DominoLineRestrictions() {}

    public void setRestrictions(int max, int[][] hLines, int[][] vLines) {
        this.max = max;
        this.hLines = hLines;
        this.vLines = vLines;
        int size = vLines.length * hLines.length;
        allowed = new boolean[max][size];
        int[] w = new int[max];
        for (int x = 0; x < vLines.length; x++) {
            for (int y = 0; y < hLines.length; y++) {
                int i = y * vLines.length + x;
                for (int q = 0; q < max; q++) w[q] = 0;
                for (int k = 0; k < vLines[x].length; k++) w[vLines[x][k]]++;
                for (int k = 0; k < hLines[y].length; k++) w[hLines[y][k]]++;
                boolean vr = isVerticalRestricted(x);
                boolean hr = isHorizontalRestricted(y);
                if(!vr && !hr) {
                    for (int q = 0; q < max; q++) allowed[q][i] = true;
                } else if(vr && hr) {
                    for (int q = 0; q < max; q++) allowed[q][i] = (w[q] == 2);
                } else {
                    for (int q = 0; q < max; q++) allowed[q][i] = (w[q] == 1);
                }
            }
        }
    }

    public boolean isVerticalRestricted(int x) {
        return vLines[x].length > 0;
    }

    public boolean isHorizontalRestricted(int y) {
        return hLines[y].length > 0;
    }

    public boolean isValueAllowed(int v, int i) {
        return allowed[v][i];
    }

    private int[] temp = new int[10];

    public boolean isVerticalWrong(int x, DominoField field) {
        if(!isVerticalRestricted(x)) return false;
        for (int q = 0; q < max; q++) temp[q] = 0;
        for (int y = 0; y < field.height; y++) {
            int i = y * field.width + x;
            int v = field.getValue(i);
            if(v >= 0) temp[v]++;
        }
        for (int k = 0; k < vLines[x].length; k++) {
            int q = vLines[x][k];
            if(temp[q] == 0) return true;
            temp[q] = 0;
        }
        for (int q = 0; q < max; q++) if(temp[q] > 0) return true;
        return false;
    }

    public boolean isHorizontalWrong(int y, DominoField field) {
        if(!isHorizontalRestricted(y)) return false;
        for (int q = 0; q < max; q++) temp[q] = 0;
        for (int x = 0; x < field.width; x++) {
            int i = y * field.width + x;
            int v = field.getValue(i);
            if(v >= 0) temp[v]++;
        }
        for (int k = 0; k < hLines[y].length; k++) {
            int q = hLines[y][k];
            if(temp[q] == 0) return true;
            temp[q] = 0;
        }
        for (int q = 0; q < max; q++) if(temp[q] > 0) return true;
        return false;
    }

    public boolean isWrong(DominoField field) {
        for (int x = 0; x < field.width; x++)
          if(isVerticalWrong(x, field)) return true;
        for (int y = 0; y < field.height; y++)
          if(isHorizontalWrong(y, field)) return true;
        return false;
    }

}