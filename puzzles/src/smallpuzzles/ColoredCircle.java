package smallpuzzles;

public class ColoredCircle {
    int colorCount = 4;
    int size = 10;
    int[] state;

    int[][] next;
    int[][] middleA, middleB;

    int t;
    int[] wayCount;
    int[] way;
    int[][] ways;
    int[] place;
    int[][] restrictions;
    int[] variantCount;
    int solutionCount;

    int optimumValue = 0;
    int[] optimumState;


    public ColoredCircle() {}

    public void init(int colorCount, int size) {
        this.colorCount = colorCount;
        this.size = size;
        state = new int[size];
        for (int i = 0; i < size; i++) state[i] = -1;
        t = 0;
        place = new int[size + 1];
        wayCount = new int[size + 1];
        way = new int[size + 1];
        ways = new int[size + 1][colorCount];
        restrictions = new int[size][colorCount];
        for (int i = 0; i < size; i++) for (int j = 0; j < colorCount; j++)
            restrictions[i][j] = 0;
        variantCount = new int[size];
        for (int i = 0; i < size; i++) variantCount[i] = colorCount;
        initJumps();
        solutionCount = 0;
        optimumState = new int[size];
    }

    void initJumps() {
        next = new int[size][size];
        middleA = new int[size][size];
        middleB = new int[size][size];
        for (int p1 = 0; p1 < size; p1++) {
          for (int p2 = 0; p2 < size; p2++) {
            next[p1][p2] = (p2 == p1) ? -1 : next(p1, p2);
            if(p1 == p2 || ((p1 % 2) != (p2 % 2))) {
                middleA[p1][p2] = -1;
            } else {
                middleA[p1][p2] = ((p1 + p2) / 2);
            }
            int p3 = p1 + size;
            if(p3 == p2 || ((p3 % 2) != (p2 % 2))) {
                middleB[p1][p2] = -1;
            } else {
                middleB[p1][p2] = ((p3 + p2) / 2);
                if(middleB[p1][p2] >= size) middleB[p1][p2] -= size;
            }
          }
        }
    }

    private int next(int p1, int p2) {
        int p3 = p2 + p2 - p1;
        return (p3 < 0) ? p3 + size : (p3 >= size) ? p3 - size : p3;
    }

    private void srch() {
        wayCount[t] = 0;
        if(t == size) return;
        if(t == 0) {
            place[t] = 0;
            wayCount[t] = 1;
            ways[t][0] = 0;
            return;
        }
        int p = -1;
        int wc = 100;
        for (int i = 0; i < size; i++) {
            if(state[i] != -1) continue;
            if(variantCount[i] == 0) return;
            if(variantCount[i] < wc) {
              wc = variantCount[i];
              p = i;
            }
            if(wc == 1) break;
        }
        place[t] = p;
        wayCount[t] = wc;
        int q = 0;
        for (int i = 0; i < colorCount; i++) {
            if(restrictions[p][i] > 0) continue;
            ways[t][q] = i;
            ++q;
        }

    }

    private void move() {
        int p = place[t];
        int c = ways[t][way[t]];
        state[p] = c;
        for (int i = 0; i < size; i++) {
            if(i == p || state[i] != c) continue;
            restrict(next[p][i], c);
            restrict(next[i][p], c);
            restrict(middleA[i][p], c);
            restrict(middleB[i][p], c);
        }
        ++t;
        srch();
        way[t] = -1;
    }


    private void restrict(int p, int c) {
        if(p < 0 || state[p] != -1) return;
        ++restrictions[p][c];
        if(restrictions[p][c] == 1) --variantCount[p];
    }

    private void unrestrict(int p, int c) {
        if(p < 0 || state[p] != -1) return;
        --restrictions[p][c];
        if(restrictions[p][c] == 0) ++variantCount[p];
    }

    private void back() {
        --t;
        int p = place[t];
        int c = ways[t][way[t]];
        for (int i = 0; i < size; i++) {
            if(i == p || state[i] != c) continue;
            unrestrict(next[p][i], c);
            unrestrict(next[i][p], c);
            unrestrict(middleA[i][p], c);
            unrestrict(middleB[i][p], c);
        }
        state[p] = -1;
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
            if(t == size) {
                checkOptimumSolution();
                ++solutionCount;
                if(solutionCount == 1) printSolution();
                if(solutionCount % 1000 == 0) System.out.print("x");
                //return;
            }
        }
    }

    private void checkOptimumSolution() {
        int k = 0;
        for (int i = 0; i < size; i++) if(state[i] == 0) ++k;
        if(k > optimumValue) {
            optimumValue = k;
            for (int i = 0; i < size; i++) optimumState[i] = state[i];
            System.out.println("\n" + k);
            printSolution();
        }
    }

    private void printSolution() {
        for (int i = 0; i < size; i++) {
            System.out.print(" " + state[i]);
        }
        System.out.println("");
        int[] error = checkSolution();
        if(error != null) {
            System.out.println("Error: " + error[0] + " " + error[1] + " " + error[2]);
        }
    }

    private int[] checkSolution() {
        for (int p1 = 0; p1 < size; p1++) for (int p2 = 0; p2 < size; p2++) {
            if(p1 == p2 || state[p1] != state[p2]) continue;
            int p3 = next(p1, p2);
            if(p3 == p1 || p3 == p2) continue;
            if(state[p3] == state[p1]) return new int[]{p1, p2, p3};
        }
        return null;
    }

    public void printOptimumSolution() {
      System.out.println("Optimum:");
      for (int i = 0; i < size; i++)
        System.out.print(" " + optimumState[i]);
    }

    public static void main(String[] args) {
        ColoredCircle q = new ColoredCircle();
        q.init(4, 56);
        q.anal();
        System.out.println(q.solutionCount);
        q.printOptimumSolution();
    }

}