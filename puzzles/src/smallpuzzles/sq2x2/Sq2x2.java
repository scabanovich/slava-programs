package smallpuzzles.sq2x2;

import java.util.*;

public class Sq2x2 {
    int width = 10;
    int height = 10;
    int pieceCount = 9;
    int size = width * height;

    int[] pieceWeight;

//  int[] xSumLimits = new int[]{9, 10, 1, 13, 13, 3, 5, 2, 17, 17};
//  int[] ySumLimits = new int[]{14, 21, 7, 2, 6, 10, 14, 8, 4, 4};
    int[] xSumLimits = new int[]{-1, 10, 1, -1, -1, -1, 5, 2, -1, -1};
    int[] ySumLimits = new int[]{14, -1, -1, 2, -1, 10, 14, -1, -1, 4};

    int[] x, y;
    int[][] xy;

    int[] used;
    // numbers
    int[] state;
    int[] exclusions;
    int[] xSum;
    int[] ySum;

    int t;
    int[] wayCount;
    int[] way;
    int[][] ways;
    int variantCount;
    int solutionCount;
    ArrayList solutions = new ArrayList();
    int solutionCountLimit = 1000;

    public Sq2x2() {}

    public void setSumLimits(int[] xSum, int[] ySum) {
        xSumLimits = xSum;
        ySumLimits = ySum;
    }

    public void setSolutionCountLimit(int c) {
        solutionCountLimit = c;
    }

    public void init() {
        x = new int[size];
        y = new int[size];
        xy = new int[width][height];
        for (int i = 0; i < size; i++) {
            x[i] = i % width;
            y[i] = i / width;
            xy[x[i]][y[i]] = i;
        }
    }

    public void initAnal() {
        pieceWeight = new int[pieceCount];
        for (int i = 0; i < pieceCount; i++) pieceWeight[i] = pieceCount - i;
        used = new int[pieceCount];
        for (int i = 0; i < pieceCount; i++) used[i] = 0;
        state = new int[size];
        for (int i = 0; i < size; i++) state[i] = 0;
        exclusions = new int[size];
        for (int i = 0; i < size; i++) {
            exclusions[i] = (x[i] < width - 1 && y[i] < height - 1) ? 0 : 1;
        }
        xSum = new int[height];
        for (int i = 0; i < height; i++) xSum[i] = 0;
        ySum = new int[width];
        for (int i = 0; i < width; i++) ySum[i] = 0;
        t = 0;
        ways = new int[pieceCount + 1][size];
        wayCount = new int[pieceCount + 1];
        way = new int[pieceCount + 1];
        solutionCount = 0;
        solutions.clear();
    }

    public void srch() {
        wayCount[t] = 0;
        if(!checkXSum() || !checkYSum()) return;
        for (int i = 0; i < size; i++) {
            if(exclusions[i] != 0) continue;
            if(xSumLimits[y[i]] != -1 && xSum[y[i]] + pieceWeight[t] > xSumLimits[y[i]]) continue;
            if(xSumLimits[y[i] + 1] != -1 && xSum[y[i] + 1] + pieceWeight[t] > xSumLimits[y[i] + 1]) continue;
            if(ySumLimits[x[i]] != -1 && ySum[x[i]] + pieceWeight[t] > ySumLimits[x[i]]) continue;
            if(ySumLimits[x[i] + 1] != -1 && ySum[x[i] + 1] + pieceWeight[t] > ySumLimits[x[i] + 1]) continue;
            ways[t][wayCount[t]] = i;
            wayCount[t]++;
        }
    }

    private boolean checkXSum() {
        int s = 0;
        for (int i = 0; i < height; i++)
          if(xSumLimits[i] >= 0) s = s + xSumLimits[i] - xSum[i];
        for (int i = t; i < pieceCount; i++) s = s - pieceWeight[i] - pieceWeight[i];
        return (s <= 0);
    }
    private boolean checkYSum() {
        int s = 0;
        for (int i = 0; i < width; i++)
          if(ySumLimits[i] >= 0) s = s + ySumLimits[i] - ySum[i];
        for (int i = t; i < pieceCount; i++) s = s - pieceWeight[i] - pieceWeight[i];
        return (s <= 0);
    }

    public void move() {
        int i = ways[t][way[t]];
        for (int dx = 0; dx < 2; dx++) for (int dy = 0; dy < 2; dy++) {
            state[xy[x[i] + dx][y[i] + dy]] = pieceWeight[t];
        }
        xSum[y[i]] += pieceWeight[t];
        xSum[y[i] + 1] += pieceWeight[t];
        ySum[x[i]] += pieceWeight[t];
        ySum[x[i] + 1] += pieceWeight[t];
        int minX = (x[i] - 2 < 0) ? 0 : x[i] - 2;
        int maxX = (x[i] + 3 > width) ? width : x[i] + 3;
        int minY = (y[i] - 2 < 0) ? 0 : y[i] - 2;
        int maxY = (y[i] + 3 > height) ? height : y[i] + 3;
        for (int ix = minX; ix < maxX; ix++) for (int iy = minY; iy < maxY; iy++) {
            exclusions[xy[ix][iy]]++;
        }
        ++t;
        srch();
        way[t] = -1;
    }

    public void back() {
        --t;
        int i = ways[t][way[t]];
        for (int dx = 0; dx < 2; dx++) for (int dy = 0; dy < 2; dy++) {
            state[xy[x[i] + dx][y[i] + dy]] = 0;
        }
        xSum[y[i]] -= pieceWeight[t];
        xSum[y[i] + 1] -= pieceWeight[t];
        ySum[x[i]] -= pieceWeight[t];
        ySum[x[i] + 1] -= pieceWeight[t];
        int minX = (x[i] - 2 < 0) ? 0 : x[i] - 2;
        int maxX = (x[i] + 3 > width) ? width : x[i] + 3;
        int minY = (y[i] - 2 < 0) ? 0 : y[i] - 2;
        int maxY = (y[i] + 3 > height) ? height : y[i] + 3;
        for (int ix = minX; ix < maxX; ix++) for (int iy = minY; iy < maxY; iy++) {
            exclusions[xy[ix][iy]]--;
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
            if(t == pieceCount) {
                if(isSolution()) {
                    solutions.add(state.clone());
                    ++solutionCount;
                    if(solutionCount >= solutionCountLimit) return;
                }
            }
        }
    }

    private boolean isSolution() {
        for (int ix = 0; ix < width; ix++) {
            if(ySumLimits[ix] != -1 && ySum[ix] != ySumLimits[ix]) return false;
        }
        for (int iy = 0; iy < height; iy++) {
            if(xSumLimits[iy] != -1 && xSum[iy] != xSumLimits[iy]) return false;
        }
        return true;
    }

    public int getSolutionCount() {
        return solutionCount;
    }

    public void printSolutions() {
        System.out.println("solutionCount = " + solutionCount);
        if(solutions.size() == 0) return;
        int[] state = (int[])solutions.get(0);
        for (int i = 0; i < size; i++) {
            System.out.print(state[i]);
            if(x[i] == width - 1) System.out.println("");
        }
    }

    public static void main(String[] args) {
        Sq2x2 s = new Sq2x2();
        s.init();
        s.initAnal();
        s.anal();
        s.printSolutions();
    }

}
