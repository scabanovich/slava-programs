package slava.puzzle.domino.analysis;

public class Dominoes {
    int max;
    int count;
    int[] less;
    int[] greater;
    int[][] index;

    public Dominoes(int max) {
        setMaximum(max);
    }

    public void setMaximum(int max) {
        this.max = max;
        build();
    }

    private void build() {
        count = max * (max + 1) / 2;
        less = new int[count];
        greater = new int[count];
        index = new int[count][count];
        int k = 0;
        for (int i = 0; i < max; i++) for (int j = i; j < max; j++) {
            less[k] = i;
            greater[k] = j;
            index[i][j] = k;
            index[j][i] = k;
            ++k;
        }
    }

    public int getCount() {
        return count;
    }

    public int getLess(int index) {
        return less[index];
    }

    public int getGreater(int index) {
        return greater[index];
    }

    public int getIndex(int v1, int v2) {
        return index[v1][v2];
    }

}
