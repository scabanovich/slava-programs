package smallpuzzles;

public class StainedChain {
    int width = 5;
    int height = 5;
    int size = width * height;

    int[] state = new int[size];
    StainedChainCondition condition = new StainedChainCondition();

    int[] x = new int[size];
    int[] y = new int[size];
    int[][] jump = new int[size][4];

    int[] wn = new int[size + 1];
    int[] mv = new int[size + 1];
    int[][] ws = new int[size + 1][size];
    int t = 0;

    int solutionCount = 0;
    int[] solutionState = new int[size];

    //options
    int printedSolutionsCount = 2;

    int[] st = new int[size];

    public StainedChain() {}

    public void initField() {
        for (int i = 0; i < size; i++) {
            x[i] = (i % width);
            y[i] = (i / width);
        }
        for (int i = 0; i < size; i++) {
            jump[i][0] = (x[i] == width - 1) ? -1 : i + 1;
            jump[i][1] = (y[i] == height - 1) ? -1 : i + width;
            jump[i][2] = (x[i] == 0) ? -1 : i - 1;
            jump[i][3] = (y[i] == 0) ? -1 : i - width;
        }
    }

    public void init() {
        initField();
        condition.setSize(size);
        for (int i = 0; i < size; i++) state[i] = -1;
        t = 0;
        solutionCount = 0;
    }

    void srch() {
        if(t == 0) {
            wn[t] = size;
            for (int i = 0; i < size; i++) {
                ws[t][i] = i;
            }
            return;
        }
        wn[t] = 0;
        int p = ws[t - 1][mv[t - 1]];
        if(!condition.accept(t - 1, p)) return;
        if(!checkContinuity()) return;
        for (int d = 0; d < 4; d++) {
            int p1 = jump[p][d];
            if(p1 < 0 || state[p1] >= 0) continue;
            ws[t][wn[t]] = p1;
            ++wn[t];
        }
    }

    void move() {
        int p = ws[t][mv[t]];
        state[p] = t;
        ++t;
        srch();
        mv[t] = -1;
    }

    void back() {
        --t;
        int p = ws[t][mv[t]];
        state[p] = -1;
    }

    public void analyze() {
        srch();
        mv[t] = -1;
        while(true) {
            while(mv[t] == wn[t] - 1) {
                if(t == 0) return;
                back();
            }
            ++mv[t];
            move();
            if(t == size) onSolutionFound();
/*
            if(t == 4) {
                System.out.println(getFulfillment());
            }
*/
        }
    }

    private void onSolutionFound() {
        if(solutionCount < printedSolutionsCount) {
            for (int i = 0; i < size; i++) solutionState[i] = state[i];
            printSolution(state);
        }
        ++solutionCount;
    }

    private double getFulfillment() {
        double k = 0;
        double p = 1;
        for (int tc = 0; tc < t; tc++) {
            p = p/wn[tc];
            k += p * mv[tc];
        }
        return k;
    }

    void printSolution(int[] _state) {
        for (int i = 0; i < size; i++) {
            String s = " " + _state[i];
            if(s.length() == 2) s = " " + s;
            System.out.print(s);
            if(((i + 1) % width) == 0) System.out.println("");
        }
        System.out.println("");
    }

    public int getSolutionCount() {
        return solutionCount;
    }

    int[] qp = new int[size];
    int qs = 0, qc = 0;
    private boolean checkContinuity() {
        for (int i = 0; i < size; i++) st[i] = state[i];
        int p = ws[t - 1][mv[t - 1]];
        qp[0] = p;
        qs = 1;
        qc = 0;
        while(qc < qs) {
            p = qp[qc];
            for (int d = 0; d < 4; d++) {
                int pn = jump[p][d];
                if(pn < 0 || st[pn] >= 0) continue;
                st[pn] = size;
                qp[qs] = pn;
                ++qs;
            }
            ++qc;
        };

        return t + qs == size + 1;
    }

    public static void main(String[] args) {
        StainedChain c = new StainedChain();
        c.init();
        c.analyze();
        System.out.println("Solution count = " + c.getSolutionCount());
    }

}
