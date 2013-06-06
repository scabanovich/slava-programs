package slava.puzzle.domino.analysis;

public class DominoField {
    int width;
    int height;
    int size;

    int[] mask;

    int[] x, y;
    int[][] jp;
    int[] neighbour;
    int[] next;

    int[] values;

    public DominoField() {}

    public void init(int width, int height, int[] mask) {
        this.width = width;
        this.height = height;
        size = width * height;
        this.mask = mask;
        build();
    }

    private void build() {
        x = new int[size];
        y = new int[size];
        neighbour = new int[size];
        jp = new int[4][size];
        for (int i = 0; i < size; i++) {
            x[i] = (i % width);
            y[i] = (i / width);
            neighbour[i] = -1;
        }
        for (int i = 0; i < size; i++) {
            jp[0][i] = (x[i] < width - 1) ? i + 1 : -1;
            jp[1][i] = (y[i] < height - 1) ? i + width : -1;
            jp[2][i] = (x[i] > 0) ? i - 1 : -1;
            jp[3][i] = (y[i] > 0) ? i - width : -1;
            for (int d = 0; d < 4; d++) {
                if(!isInField(jp[d][i]) || !isInField(i)) jp[d][i] = -1;
                if(jp[d][i] >= 0 && mask[jp[d][i]] == mask[i]) neighbour[i] = jp[d][i];
            }
        }
        next = new int[size];
        next[size - 1] = -1;
        for (int i = size - 2; i >= 0; i--) {
            next[i] = (isInField(i + 1)) ? i + 1 : next[i + 1];
        }
        values = new int[size];
        for (int i = 0; i < size; i++) values[i] = -1;
    }

    public boolean isInField(int i) {
        return i >= 0 && i < size && mask[i] > 0;
    }

    public int getNextCell(int i) {
        return next[i];
    }

    public int x(int i) {
        return x[i];
    }

    public int y(int i) {
        return y[i];
    }

    public int getNeighbour(int i) {
        return neighbour[i];
    }

    public int getValue(int i) {
        return values[i];
    }

    public void setValue(int i, int v) {
        values[i] = v;
    }

}
