package smallpuzzles.pyramid;

public class PyramidAnalyzer {
    int depth;
    int size;
    int[] field;
    int[] pathfield;
    int[] x, y;
    int[][] jp;

    int[] state;
    int[][] ways;
    int[] way;
    int[] wayCount;
    int t;
    int place;

    int solutionCount;
    int[] solution;

    public PyramidAnalyzer() {
        setDepth(10);
    }

    public void setDepth(int depth) {
        this.depth = depth;
        onSetDepth();
    }

    private void onSetDepth() {
        size = depth * depth;
        x = new int[size];
        y = new int[size];
        for (int i = 0; i < size; i++) {
            x[i] = (i % depth);
            y[i] = (i / depth);
        }
        jp = new int[2][size];
        for (int i = 0; i < size; i++) {
            jp[0][i] = (x[i] == depth - 1) ? 0 : i + 1;
            jp[1][i] = (y[i] == depth - 1) ? 0 : i + depth;
        }

        field = new int[size];
        pathfield = new int[size];
        for (int i = 0; i < pathfield.length; i++) pathfield[i] = 0;
        state = new int[depth];
        for (int i = 0; i < depth; i++) state[i] = 0;
        ways = new int[depth][2];
        wayCount = new int[depth];
        way = new int[depth];
    }

    public int[] getField() {
        return field;
    }

    public int getDepth() {
        return depth;
    }

    public void initAnalyzer() {
        for (int i = 0; i < depth; i++) state[i] = 0;
        t = 0;
        place = 0;
        solutionCount = 0;
        state[field[place]] = 1;
        pathfield[place] = 1;
    }

    public void srch() {
        wayCount[t] = 0;
        for (int d = 0; d < 2; d++) {
            int p = jp[d][place];
            if(state[field[p]] != 0) continue;
            ways[t][wayCount[t]] = d;
            wayCount[t]++;
        }
    }

    private void move() {
        int d = ways[t][way[t]];
        int p = jp[d][place];
        state[field[p]] = 1;
        place = p;
        pathfield[place] = 1;
        ++t;
        srch();
        way[t] = -1;
    }

    private void back() {
        --t;
        int d = ways[t][way[t]];
        state[field[place]] = 0;
        pathfield[place] = 0;
        int p = (d == 0) ? place - 1 : place - depth;
        place = p;
    }

    public void anal() {
        srch();
        way[t] = -1;
        while(true) {
            while(way[t] == wayCount[t] - 1) {
                if(t == 0) return;
                back();
            }
            ++way[t];
            move();
            if(t == 9) {
                ++solutionCount;
                if(solutionCount == 1) {
                    rememberSolution();
                }
            }
        }
    }

    private void rememberSolution() {
        solution = (int[])pathfield.clone();
    }

    public int getSolutionCount() {
        return solutionCount;
    }

    public void printPyramid() {
        for (int ix = 0; ix < depth; ix++) {
            for (int iy = 0; iy < depth - ix; iy++) {
                int p = iy * depth + ix;
                char c = (solution != null && solution[p] == 1) ? 'x' : ' ';
                System.out.print("" + c + field[p]);
            }
            System.out.println("");
        }
    }


}
