package com.slava.drops;

import java.util.Random;

import com.slava.common.RectangularField;

public class DropsGenerator {
    RectangularField field;
    DropState problemState;
    
    Random seed = new Random();
    
    public void setField(RectangularField f) {
        field = f;
    }
    
    public void generate() {
        int[] state = new int[field.getSize()];
        for (int p = 0; p < state.length; p++) {
            state[p] = seed.nextInt(5);
        }
        problemState = new DropState(null, state, -1);
        problemState.printState(field);

        solve();
    }
    
    public void solve() {
        DropsSolver solver = new DropsSolver();

        solver.setField(field);
        solver.setInitialState(problemState);
        
        solver.hitLimit = 0;
        solver.treeLimit = 300000;

        while(solver.bestSolution == null && solver.hitLimit < 8) {
            solver.hitLimit++;
            solver.solve();
        }

        if(solver.bestSolution != null) return;

        solver.hitLimit = 12;
        solver.treeLimit = 1000;
        solver.solve();
        
        while(solver.isTooComplex()) {
            solver.treeLimit *= 2;
            System.out.println("-->" + solver.treeLimit);
            solver.solve();
        }
        
    }
    
    static void generateRandom() {
        DropsGenerator g = new DropsGenerator();
        RectangularField f = new RectangularField();
        f.setSize(6, 6);
        g.setField(f);
        g.generate();
    }
    
    static int[] STATE = {
        2,2,3,3,2,3,
        0,4,4,4,1,0,
        4,0,2,3,4,3,
        0,2,1,1,0,0,
        3,0,2,4,0,3,
        3,3,2,4,2,0,
    };
    
    static void solveGiven() {
        DropsGenerator g = new DropsGenerator();
        RectangularField f = new RectangularField();
        f.setSize(6, 6);
        g.setField(f);
        g.problemState = new DropState(null, STATE, -1);
        g.solve();        
    }
    
    public static void main(String[] args) {
        solveGiven();
    }

}
