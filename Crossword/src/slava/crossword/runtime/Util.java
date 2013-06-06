package slava.crossword.runtime;

public class Util {

    public Util() {}

    public static int[][] getJumps(int width, int height) {
        int length = width * height;
        int[] x = new int[length], y = new int[length];
        for (int i = 0; i < length; i++) {
            x[i] = (i % width);
            y[i] = (i / width);
        }
        int[][] jp = new int[length][4];
        for (int i = 0; i < length; i++) {
            jp[i][0] = (x[i] == width - 1) ? -1 : i + 1;
            jp[i][1] = (y[i] == height - 1) ? -1 : i + width;
            jp[i][2] = (x[i] == 0) ? -1 : i - 1;
            jp[i][3] = (y[i] == 0) ? -1 : i - width;
        }
        return jp;
    }

} 