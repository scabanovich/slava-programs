package smallpuzzles;

public class ColoredChain {
    int colorCount = 5;
    int size = 20;
    int[] state;

    int[][] next;
    int[][] middle;

    int t;
    int[] wayCount;
    int[] way;
    int[][] ways;
    int[] place;
    int[][] restrictions;
    int[] variantCount;
    int solutionCount;

    public ColoredChain() {}

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
    }

    void initJumps() {
        next = new int[size][size];
        middle = new int[size][size];
        for (int p1 = 0; p1 < size; p1++) {
          for (int p2 = 0; p2 < size; p2++) {
            next[p1][p2] = (p2 == p1) ? -1 : next(p1, p2);
            if(p1 == p2 || ((p1 % 2) != (p2 % 2))) {
                middle[p1][p2] = -1;
            } else {
                middle[p1][p2] = ((p1 + p2) / 2);
            }
          }
        }
    }

    private int next(int p1, int p2) {
        int p3 = p2 + p2 - p1;
        return (p3 < 0 || p3 >= size) ? -1 : p3;
    }

    private void srch() {
        wayCount[t] = 0;
        if(t == size) return;
        if(t == 0) {
            place[t] = size / 2;
            wayCount[t] = 1;
            ways[t][0] = 0;
            return;
        }
        if(t < 7) {
            place[t] = place[t - 1] + 1;
            wayCount[t] = variantCount[place[t]];
        } else {
            int p = -1;
            int wc = 100;
            for (int i = 0; i < size; i++) {
              if (state[i] != -1) continue;
              if (variantCount[i] == 0) return;
              if (variantCount[i] < wc) {
                wc = variantCount[i];
                p = i;
              }
              if (wc == 1) break;
            }
            place[t] = p;
            wayCount[t] = wc;
        }
        int q = 0;
        for (int i = 0; i < colorCount; i++) {
            if(restrictions[place[t]][i] > 0) continue;
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
            restrict(middle[i][p], c);
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
            unrestrict(middle[i][p], c);
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
                ++solutionCount;
                if(solutionCount == 1) printSolution();
                if(solutionCount % 100000 == 0) System.out.print("x");
                //return;
            }
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
            if(p3 < 0 || p3 == p1 || p3 == p2) continue;
            if(state[p3] == state[p1]) return new int[]{p1, p2, p3};
        }
        return null;
    }

    public static void main(String[] args) {
        ColoredChain q = new ColoredChain();
        q.init(4, 76);
        q.anal();
        System.out.println(q.solutionCount);
    }
    // (4,71)     1 0 1 1 2 1 0 3 3 1 0 3 0 3 2 0 0 3 2 1 2 2 3 2 3 3 1 1 0 2 0 1 3 2 2 0 0 1 0 1 1 2 1 0 3 3 1 0 3 0 3 2 0 0 3 2 1 2 2 3 2 3 3 1 1 0 2 0 1 3 2
    // (4,72)   0 1 0 1 1 2 1 0 3 3 1 0 3 0 3 2 0 0 3 2 1 2 2 3 2 3 3 1 1 0 2 0 1 3 2 2 0 0 1 0 1 1 2 1 0 3 3 1 0 3 0 3 2 0 0 3 2 1 2 2 3 2 3 3 1 1 0 2 0 1 3 2
    // (4,74) 0 0 1 0 1 1 2 1 0 3 3 1 0 3 0 3 2 0 0 3 2 1 2 2 3 2 3 3 1 1 0 2 0 1 3 2 2 0 0 1 0 1 1 2 1 0 3 3 1 0 3 0 3 2 0 0 3 2 1 2 2 3 2 3 3 1 1 0 2 0 1 3 2 2



    // a c a s  b c e s  b e e e  m m m e
    // a a a s  b c d d  d d d w  m m o w
    // k k k k  b c q q  b q q w  m o o o
    // h h h h  b f h w  f f f w  f f o w


}