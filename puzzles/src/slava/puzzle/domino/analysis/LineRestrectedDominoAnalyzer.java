package slava.puzzle.domino.analysis;

public class LineRestrectedDominoAnalyzer {
    Dominoes dominoes;
    DominoField field;
    DominoLineRestrictions restrictions;

    int[] used;
    int unusedCount;

    int t;
    int[] wayCount;
    int[][] ways;
    int[] way;
    int[] place;

    int solutionCount;
    int treeSize;

    int solutionLimit = -1;
    int printedSolutionLimit = 7;
    boolean randomize = false;
    
    int[] solution = null;

    public LineRestrectedDominoAnalyzer() {}

    public void setData(Dominoes dominoes, DominoField field, DominoLineRestrictions restrictions) {
        this.dominoes = dominoes;
        this.field = field;
        this.restrictions = restrictions;
        checkData();
    }

    void checkData() {
        int n = 2 * dominoes.getCount();
        for (int i = 0; i < field.size; i++) {
            if(field.isInField(i)) n--;
        }
        if(n != 0) throw new RuntimeException("Wrong field size: difference=" + n);
    }

    public void setRandomising(boolean b) {
        randomize = b;
    }

    public void setSolutionLimit(int k) {
        solutionLimit = k;
    }

    public void setPrintedSolutionLimit(int k) {
        printedSolutionLimit = k;
    }

    void init() {
        used = new int[dominoes.getCount()];
        for (int i = 0; i < used.length; i++) used[i] = 0;
        unusedCount = used.length;
        int tm = unusedCount * 2 + 1;
        wayCount = new int[tm];
        ways = new int[tm][dominoes.max];
        way = new int[tm];
        place = new int[tm];
        for (int i = 0; i < field.size; i++) field.setValue(i, -1);
        solutionCount = 0;
        treeSize = 0;
        t = 0;
    }

    int[] temp_ways = new int[10];
    void srch() {
        wayCount[t] = 0;
        if(unusedCount == 0) return;
        int wc = 10;
        for (int p = 0; p < field.size; p++) {
            if(!field.isInField(p) || field.getValue(p) >= 0) continue;
            int adj = getAdjacentValue(p);
            if(adj == -2) {
              return;
            }
            if(adj >= 0) {
                wc = 1;
                place[t] = p;
                ways[t][0] = adj;
            }
            if(wc == 1) continue;
            int ww = fillTempWays(p);
            if(ww >= wc) continue;
            wc = ww;
            place[t] = p;
            for (int k = 0; k < wc; k++) ways[t][k] = temp_ways[k];
            if(wc == 0) return;
        }
        if(wc < 10) wayCount[t] = wc;
        if(randomize) randomizeWays();
    }

    private void randomizeWays() {
      if(wayCount[t] > 1) {
          for (int i = wayCount[t] - 1; i >= 1; i--) {
              int j = (int)((i + 1) * Math.random());
              if(j == i) continue;
              int b = ways[t][i];
              ways[t][i] = ways[t][j];
              ways[t][j] = b;
          }
      }
    }

    private int getAdjacentValue(int p) {
        int n = field.getNeighbour(p);
        int nv = field.getValue(n);
        int q = -1;
        for (int d = 0; d < 4; d++) {
            int a = field.jp[d][p];
            if(n == a || a < 0 || field.getValue(a) < 0) continue;
            if(q == -1) q = field.getValue(a); else if(q != field.getValue(a)) return -2;
            if(nv >= 0) {
                if(used[dominoes.getIndex(nv, q)] > 0) return -2;
            }
        }
        return q;
    }

    private int fillTempWays(int p) {
        int n = field.getNeighbour(p);
        int nv = field.getValue(n);
        int k = 0;
        for (int q = 0; q < dominoes.max; q++) {
            if(!restrictions.isValueAllowed(q, p)) continue;
            if(nv >= 0) {
                if(used[dominoes.getIndex(nv, q)] > 0) continue;
            }
            temp_ways[k] = q;
            ++k;
        }
        return k;
    }

    void move() {
        int p = place[t];
        int q = ways[t][way[t]];
        int n = field.getNeighbour(p);
        int nv = field.getValue(n);
        if(nv >= 0) {
            int index = dominoes.getIndex(nv, q);
            used[index]++;
            unusedCount--;
        }
        field.setValue(p, q);
        ++t;
        srch();
        way[t] = -1;
    }

    void back() {
        --t;
        int p = place[t];
        int q = ways[t][way[t]];
        int n = field.getNeighbour(p);
        int nv = field.getValue(n);
        if(nv >= 0) {
            int index = dominoes.getIndex(nv, q);
            used[index]--;
            unusedCount++;
        }
        field.setValue(p, -1);
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
          if(wayCount[t] == 0) treeSize++;
          if(unusedCount == 0) {
              if(restrictions.isWrong(field)) {
                  continue;
              }
              ++solutionCount;
              if(solutionCount < printedSolutionLimit) {
                  //rememberSolution();
                  printSolution(field);
              }
              if(solutionCount == 1) solution = (int[])field.values.clone();
              if(solutionCount == solutionLimit) return;
          }
      }
    }

    public void printSolution(DominoField field) {
        for (int i = 0; i < field.size; i++) {
            int x = field.x(i);
            if(x == 0) System.out.println("");
            int v = field.getValue(i);
            if(v < 0) System.out.print("- ");
            else System.out.print("" + v + " ");
        }
        System.out.println("");
    }
    
    public int[] getSolution() {
    	return solution;
    }
    
}