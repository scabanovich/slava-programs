package smallpuzzles.dool;

public class DoolAnalizer {
    int width = 18;
    int height = 16;
    int size = width * height;

    int[] field = {
        9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9,
        9, 9, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 9, 0, 0, 0, 1, 0,
        0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 9, 9, 9, 9, 0, 0, 0, 0,
        9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 0, 0, 0, 0
    };

    int[] reciprocal = new int[]{2, 3, 0, 1};

    int[] x, y;
    int[][] jp;

    int store;
    int[][] state;

    int t;
    int place;
    int[] wayCount = new int[500];
    int[] way = new int[500];
    int[][] waysD = new int[500][4];
    int[][] waysF = new int[500][4];

    int result;
    int maxResult;

    public DoolAnalizer() {}

    public void init() {
        x = new int[size];
        y = new int[size];
        jp = new int[size][4];
        for (int i = 0; i < size; i++) {
            x[i] = (i % width);
            y[i] = (i / width);
        }
        for (int i = 0; i < size; i++) {
            jp[i][0] = (x[i] == width - 1) ? -1 : i + 1;
            jp[i][1] = (y[i] == height - 1) ? -1 : i + width;
            jp[i][2] = (x[i] == 0) ? -1 : i - 1;
            jp[i][3] = (y[i] == 0) ? -1 : i - width;
        }
        state = new int[size][4];
        for (int i = 0; i < size; i++) for (int d = 0; d < 4; d++) state[i][d] = 0;
        t = 0;
        place = 12 * width + 11;
        store = 3;
        result = 0;
        maxResult = 0;
    }

    void srch() {
        wayCount[t] = 0;
        if(t == 0) {
            for (int d = 0; d < 4; d++) {
                int p = jp[place][d];
                if(p < 0 || field[p] != 0) continue;
                waysD[t][wayCount[t]] = d;
                waysF[t][wayCount[t]] = -1;
                wayCount[t]++;
            }
        } else {
            int df = waysD[t - 1][way[t - 1]];
            int pf = jp[place][df];
            boolean canTurn = (pf < 0 || field[pf] != 0);
            boolean acceptStone = acceptStone(pf);
            for (int d = 0; d < 4; d++) {
                if(d == reciprocal[df]) continue;
                if(d != df && !canTurn && !acceptStone) continue;
                int p = jp[place][d];
                if(p < 0 || field[p] != 0) continue;
                if(state[p][d] == 1 || state[p][reciprocal[d]] == 1) continue;
                waysD[t][wayCount[t]] = d;
                waysF[t][wayCount[t]] = (d == df || !acceptStone) ? -1 : pf;
                wayCount[t]++;
            }
        }
    }

    private boolean acceptStone(int p) {
        if(store <= 0) return false;
        if(p < 0 || field[p] != 0) return false;
        for (int d = 0; d < 4; d++) if(state[p][d] == 1) return false;
        return true;
    }

    void move() {
        int df = waysD[t][way[t]];
        int fp = waysF[t][way[t]];
        if(fp >= 0) {
            field[fp] = 2;
            --store;
        }
        state[place][df] = 1;
        place = jp[place][df];
        state[place][reciprocal[df]] = 1;
        if(state[place][0] == 1 || state[place][2] == 1) ++result;
        if(state[place][1] == 1 || state[place][3] == 1) ++result;
        ++t;
        srch();
        way[t] = -1;
    }

    void back() {
        --t;
        if(state[place][0] == 1 || state[place][2] == 1) --result;
        if(state[place][1] == 1 || state[place][3] == 1) --result;
        int df = waysD[t][way[t]];
        int fp = waysF[t][way[t]];
        if(fp >= 0) {
            field[fp] = 0;
            ++store;
        }
        state[place][reciprocal[df]] = 0;
        place = jp[place][reciprocal[df]];
        state[place][df] = 0;
    }

    void anal() {
      srch();
      way[t] = -1;
      while(true) {
          while(way[t] == wayCount[t] - 1) {
              if(t == 0) return;
              back();
          }
          ++way[t];
          move();
          if(result > maxResult) {
              maxResult = result;
              printSolution();
          }
      }
    }

    void printSolution() {
        System.out.println(maxResult);
        for (int i = 0; i < t; i++) {
            int fp = waysF[i][way[i]];
            if(fp >= 0) System.out.print(" " + x[fp] + ":" + y[fp]);
        }
        System.out.println("");
        int dc = -1;
        for (int i = 0; i < t; i++) {
            int d = waysD[i][way[i]];
            if(d == dc) continue;
            dc = d;
            System.out.print(" " + dc);
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        DoolAnalizer d = new DoolAnalizer();
        d.init();
        d.anal();
    }

}
