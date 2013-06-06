package smallpuzzles.spiders;

import java.util.*;

public class Spiders {
    //grid of spiders
    int width;
    int height;
    int size;

    //grid of leg-poins
    int g_width;
    int g_height;
    int g_size;

    // first index - spider index, second - leg index
    int[][] spiders;

    int[][][] compatibility = new int[][][]{
        {{0, 4}, {1, 3}, {7, 5}},
        {{1, 5}},
        {{2, 6}, {1, 7}, {3, 5}},
        {{3, 7}},
        {{4, 0}, {3, 1}, {5, 7}},
        {{5, 1}},
        {{6, 2}, {5, 3}, {7, 1}},
        {{7, 3}}
    };

    int extraRemovedLegsCount = 0;

    //coordinates and navigation on grid of spiders
    int[] x, y;
    int[][] jp;
    //map grid of spiders to grid of leg-points
    int[] sg;

    //coordinates and navigation on grid of leg-poins
    int[] xg, yg;
    int[][] jpg;

    int[] nodes;
    int[][] matrix;

    int[] pstate;
    int[] state;
    int[] occupation;
    int emptyNodeCount;
    int t;
    int[] wayCount;
    int[] way;
    int[][] ways;
    int variantCount;
    int solutionCount;
    ArrayList solutions = new ArrayList();

    public Spiders() {
        setSize(3, 3);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        size = width * height;
        g_width = 2 * width + 1;
        g_height = 2 * height + 1;
        g_size = g_width * g_height;
        nodes = new int[g_size];
        matrix = new int[g_size][g_size];
    }

    public void setSpiders(int[][] spiders) {
        if(spiders == null) throw new NullPointerException("Parameter spiders cannot be null.");
        if(spiders.length != size) throw new RuntimeException("Incorrect number of spiders: " + spiders.length);
        this.spiders = spiders;
    }

    public void initField() {
        x = new int[size];
        y = new int[size];
        jp = new int[8][size];
        for (int i = 0; i < size; i++) {
            x[i] = i % width;
            y[i] = i / width;
        }
        for (int i = 0; i < size; i++) {
            jp[0][i] = (x[i] == width - 1                      ) ? -1 : i + 1;
            jp[1][i] = (x[i] == width - 1 || y[i] == height - 1) ? -1 : i + 1 + width;
            jp[2][i] = (                     y[i] == height - 1) ? -1 : i     + width;
            jp[3][i] = (x[i] == 0         || y[i] == height - 1) ? -1 : i - 1 + width;
            jp[4][i] = (x[i] == 0                              ) ? -1 : i - 1;
            jp[5][i] = (x[i] == 0         || y[i] == 0         ) ? -1 : i - 1 - width;
            jp[6][i] = (                     y[i] == 0         ) ? -1 : i     - width;
            jp[7][i] = (x[i] == width - 1 || y[i] == 0         ) ? -1 : i + 1 - width;
        }

        xg = new int[g_size];
        yg = new int[g_size];
        jpg = new int[8][g_size];
        for (int i = 0; i < g_size; i++) {
            xg[i] = i % g_width;
            yg[i] = i / g_width;
        }
        for (int i = 0; i < g_size; i++) {
            jpg[0][i] = (xg[i] == g_width - 1                     ) ? -1 : i + 1;
            jpg[1][i] = (xg[i] == g_width - 1 || yg[i] == g_height - 1) ? -1 : i + 1 + g_width;
            jpg[2][i] = (                        yg[i] == g_height - 1) ? -1 : i     + g_width;
            jpg[3][i] = (xg[i] == 0           || yg[i] == g_height - 1) ? -1 : i - 1 + g_width;
            jpg[4][i] = (xg[i] == 0                                   ) ? -1 : i - 1;
            jpg[5][i] = (xg[i] == 0           || yg[i] == 0           ) ? -1 : i - 1 - g_width;
            jpg[6][i] = (                        yg[i] == 0           ) ? -1 : i     - g_width;
            jpg[7][i] = (xg[i] == g_width - 1 || yg[i] == 0           ) ? -1 : i + 1 - g_width;
        }

        sg = new int[size];
        for (int i = 0; i < size; i++) {
            int gx = 2 * x[i] + 1;
            int gy = 2 * y[i] + 1;
            sg[i] = gy * g_width + gx;
        }
    }

    public void initAnalizer() {
        t = 0;
        state = new int[size];
        pstate = new int[size];
        for (int i = 0; i < size; i++) state[i] = -1;
        for (int i = 0; i < size; i++) pstate[i] = -1;
        occupation = new int[g_size + 1];
        for (int i = 0; i < g_size; i++) occupation[i] = 0;
        way = new int[size + 1];
        wayCount = new int[size + 1];
        ways = new int[size + 1][size];

        variantCount = 0;
        solutionCount = 0;
        solutions.clear();

        extraRemovedLegsCount = g_size - 1;
        for (int i = 0; i < size; i++) for (int d = 0; d < 8; d++) {
            if(spiders[i][d] != 0) --extraRemovedLegsCount;
        }
        emptyNodeCount = 0;
    }

    private void srch() {
        wayCount[t] = 0;
        if(t == size) return;
        if(!checkOccupation()) return;
        for (int i = 0; i < size; i++) {
            if(state[i] >= 0) continue;
            if(!checkCompatibility(i, 4)) continue;
            if(!checkCompatibility(i, 6)) continue;
            ways[t][wayCount[t]] = i;
            wayCount[t]++;
        }
    }

    private boolean checkCompatibility(int i0, int d) {
        int p1 = jp[d][t];
        if(p1 < 0) return true;
        int i1 = pstate[p1];
        int s = 0;
        for (int k = 0; k < compatibility[d].length; k++) {
            int d0 = compatibility[d][k][0];
            int d1 = compatibility[d][k][1];
            if(spiders[i0][d0] > 0 && spiders[i1][d1] > 0) ++s;
        }
        return (s < 2);
    }

    private boolean checkOccupation() {
        return (extraRemovedLegsCount >= emptyNodeCount);
    }

    private void move() {
        int i = ways[t][way[t]];
        state[i] = t;
        pstate[t] = i;
        for (int d = 0; d < 8; d++) if(spiders[i][d] != 0) ++occupation[jpg[d][sg[t]]];
        updateEmptyNodeCountOnMove();
        ++t;
        srch();
        way[t] = -1;
    }

    private void updateEmptyNodeCountOnMove() {
        for (int d = 0; d < 8; d++) {
            int kn = jpg[d][sg[t]];
            if((d == 0 || d == 7) && xg[kn] == g_width - 1) {
                if(occupation[kn] == 0) emptyNodeCount++;
            } else if(d == 1 && xg[kn] == g_width - 1 && yg[kn] == g_height - 1) {
                if(occupation[kn] == 0) emptyNodeCount++;
            } else if((d == 2 || d == 3) && yg[kn] == g_height - 1) {
                if(occupation[kn] == 0) emptyNodeCount++;
            } else if(d == 4 || d == 5 || d == 6) {
                if(occupation[kn] == 0) emptyNodeCount++;
            }
        }
    }

    private void back() {
        --t;
        int i = ways[t][way[t]];
        updateEmptyNodeCountOnBack();
        for (int d = 0; d < 8; d++) if(spiders[i][d] != 0) --occupation[jpg[d][sg[t]]];
        state[i] = -1;
        pstate[t] = -1;
    }

    private void updateEmptyNodeCountOnBack() {
        for (int d = 0; d < 8; d++) {
            int kn = jpg[d][sg[t]];
            if((d == 0 || d == 7) && xg[kn] == g_width - 1) {
                if(occupation[kn] == 0) emptyNodeCount--;
            } else if(d == 1 && xg[kn] == g_width - 1 && yg[kn] == g_height - 1) {
                if(occupation[kn] == 0) emptyNodeCount--;
            } else if((d == 2 || d == 3) && yg[kn] == g_height - 1) {
                if(occupation[kn] == 0) emptyNodeCount--;
            } else if(d == 4 || d == 5 || d == 6) {
                if(occupation[kn] == 0) emptyNodeCount--;
            }
        }
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
              if(variantCount % 1000000 == 999999) System.out.print(solutionCount);
              ++variantCount;
              checkMatrix();
          }
      }
    }

    private void checkMatrix() {
        for (int i = 0; i < g_size; i++) for (int j = 0; j < g_size; j++) matrix[i][j] = 0;
        for (int i = 0; i < g_size; i++) nodes[i] = 0;

        int nodeCount = 0;
        int bindCount = 0;
        for (int i = 0; i < size; i++) {
            int g = sg[state[i]];
            for (int d = 0; d < 8; d++) {
                if(spiders[i][d] != 1) continue;
                int g2 = jpg[d][g];
                matrix[g][g2] = 1;
                matrix[g2][g] = 1;
                nodes[g]++;
                nodes[g2]++;
                if(nodes[g] == 1) ++nodeCount;
                if(nodes[g2] == 1) ++nodeCount;
                ++bindCount;
            }
        }
        if(nodeCount == bindCount + 1 && GraphUtil.isConnectedGraph(matrix)) {
            int[] sn = new int[size];
            for (int i = 0; i < size; i++) {
//                System.out.print(" " + state[i]);
                sn[i] = state[i];
            }
            solutions.add(sn);
//            System.out.println("");
            solutionCount++;
        }
    }

    public void printSpiders() {
      System.out.println("Spiders:");
      for (int c = 0; c < size; c++) {
          for (int d = 0; d < 8; d++) {
            System.out.print(" " + spiders[c][d]);
          }
          System.out.print(", ");
      }
      System.out.println();
    }

    public void printSolutions() {
        System.out.println("solutionCount = " + solutionCount + " (variantCount = " + variantCount + ")");
        for (int i = 0; i < solutions.size(); i++) {
            int[] sn = (int[])solutions.get(i);
            for (int c = 0; c < size; c++) System.out.print(" " + sn[c]);
            System.out.println("");
        }
    }

}
