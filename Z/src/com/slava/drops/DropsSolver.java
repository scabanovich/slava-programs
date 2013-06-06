package com.slava.drops;

import java.util.Random;

import com.slava.common.RectangularField;

public class DropsSolver {
    RectangularField field;
    DropState initialState;
    
    int treeLimit = 1000;
    
    int hitLimit = 10;
    DropState bestSolution;
    
    DropState current;

    int t;
    int[] hits;
    int[] wayCount;
    int[] way;
    int[][] ways;
    
    int treeSize;
    

    public DropsSolver() {}

    public void setField(RectangularField f) {
        field = f;
    }
    
    public void setInitialState(DropState s) {
        initialState = s;
    }
    
    public void solve() {
        init();
        anal();
    }
    
    void init() {
        hits = new int[20];
        wayCount = new int[20];
        way = new int[20];
        ways = new int[20][field.getSize()];
        current = initialState;
        
        t = 0;
        bestSolution = null;
        treeSize = 0;
    }
    
    void srch() {
        wayCount[t] = 0;
        for (int p = 0; p < current.state.length; p++) {
            if(current.state[p] == 0 || hits[t] + current.state[p] > hitLimit) continue;
            ways[t][wayCount[t]] = p;
            wayCount[t]++;
        }
        if(t < 3) randomize();
    }
    
    Random seed = new Random();

    void randomize() {
        if(wayCount[t] < 2) return;
        for (int i = 0; i < wayCount[t]; i++) {
            int j = i + seed.nextInt(wayCount[t] - i);
            if(j == i) continue;
            int c = ways[t][i];
            ways[t][i] = ways[t][j];
            ways[t][j] = c;
        }
    }
    
    void move() {
        int p = ways[t][way[t]];
        hits[t + 1] = hits[t] + current.state[p];
        current = current.hit(field, p);
        ++t;
        srch();
        way[t] = -1;
    }
    
    void back() {
        --t;
        current = current.parent;        
    }
    
    void anal() {
        srch();
        way[t] = -1;
        while(true) {
            while(way[t] == wayCount[t] - 1) {
                if(t == 0) return;
                back();
            }
            way[t]++;
            move();
            if(current.getVolume() == 0) {
                if(hits[t] < hitLimit) {
                    hitLimit = hits[t];
                    bestSolution = current;
                    System.out.println("Hits " + hitLimit + " solution=" + bestSolution.getSolution());
                }
            }
            if(wayCount[t] == 0) treeSize++;
            if(isTooComplex()) return;
        }
    }
    
    public boolean isTooComplex() {
        return treeSize >= treeLimit;
    }
    
}


