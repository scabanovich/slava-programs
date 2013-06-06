package smallpuzzles.section;

public class Field {
    int width;
    int height;
    int size;
    int[] values;
    int[] x, y;
    int[][] jp;
    private int[][] point;

    public Field() {}

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        size = width * height;
        values = new int[size];
        for (int i = 0; i < size; i++) values[i] = -1;
        x = new int[size];
        y = new int[size];
        point = new int[width][height];
        for (int i = 0; i < size; i++) {
            x[i] = (i % width);
            y[i] = (i / width);
            point[x[i]][y[i]] = i;
        }
        jp = new int[4][size];
        for (int i = 0; i < size; i++) {
            jp[0][i] = (x[i] == width - 1) ? -1 : i + 1;
            jp[1][i] = (y[i] == height - 1) ? -1 : i + width;
            jp[2][i] = (x[i] == 0) ? -1 : i - 1;
            jp[3][i] = (y[i] == 0) ? -1 : i - width;
        }
    }

    public int point(int ix, int iy) {
        return (ix < 0 || ix >= width || iy < 0 || iy >= height) ? -1 : point[ix][iy];
    }

}
