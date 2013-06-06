package slava.crossword.runtime;

import java.util.*;

import slava.crossword.preference.CrosswordPreference;

public class CrosswordSolver {
    PositionFactory factory = new PositionFactory();
    Word[] words;
    int t = 0;
    int wordNumber = 0;
    int[] wayCount, way, target, wayI;
    byte[][][] ways;
    int solutionCount = 0;
    Set used = new HashSet();
    int solutionLimit = 1;
    boolean interrupted = false;
    byte[] solution = null;
    int tmax;
    int moveCount;
    
	ISolutionEstimate estimator;
	int maxEstimate;

    public CrosswordSolver() {
		solutionLimit = CrosswordPreference.getInstance().getInt("solutionLimit", 1);
    }

    public void init(int[][] jp, int[] net, byte[] content) {
        words = factory.create(jp, net, content);
        wordNumber = words.length;
        for (int i = 0; i < words.length; i++) {
			if(words[i].computeOccupied()) {
				--wordNumber;
				byte[][] vs = words[i].getVariants();
				if(vs.length == 1) {
					used.add(vs[0]);
				}
			} 
        }
        wayCount = new int[words.length + 1];
        way = new int[words.length + 1];
        wayI = new int[words.length + 1];
        target = new int[words.length + 1];
        ways = new byte[words.length + 1][][];
        t = 0;
        solutionCount = 0;
        tmax = 0;
        moveCount = 0;
		maxEstimate = 0;
    }
    
    public void setEstimator(ISolutionEstimate estimator) {
    	this.estimator = estimator;
    }

    void srch() {
        wayCount[t] = 0;
        if(t == wordNumber) return;
        byte[][] tways = null;
        int q = 100000;
        for (int i = 0; i < words.length; i++) {
            if(words[i].isOccupied()) continue;
            byte[][] ws = words[i].getVariants();
            if(ws.length >= q) continue;
            q = ws.length;
            target[t] = i;
            tways = ws;
        }
        if(tways == null) return;
        ways[t] = tways;
        wayCount[t] = ways[t].length;
    }

    void initWayIndex() {
        way[t] = -1;
        wayI[t] = (wayCount[t] < 2) ? -1 : (int)(Math.random() * wayCount[t]) - 1;
    }

    void nextWayIndex() {
        ++way[t];
        ++wayI[t];
        if(wayI[t] == wayCount[t]) wayI[t] = 0;
    }

    void move() {
        byte[] w = ways[t][wayI[t]];
        used.add(w);
        words[target[t]].setWord(w);
        ++t;
        srch();
        initWayIndex();
        ++moveCount;
        if(moveCount == 2000) {
            moveCount = 0;
            System.out.print("x");
        }
    }

    void back() {
        --t;
        byte[] w = ways[t][wayI[t]];
        used.remove(w);
        words[target[t]].unset();
    }

    public void analyse() {
        interrupted = false;
        srch();
        initWayIndex();
        while(!interrupted) {
            do {
                while(way[t] >= wayCount[t] - 1) {
                    if(t == 0) return;
                    back();
                }
                nextWayIndex();
            } while(way[t] >= wayCount[t]
                    || used.contains(ways[t][wayI[t]])
                    || restrict()
                    );
            move();
            if(t > tmax) {
                tmax = t;
                System.out.print(" " + tmax);
            }
            if(t == wordNumber) {
            	byte[] sol = readSolution();
                if(estimator != null) {
                	int est = estimator.estimate(sol);
                	if(est > maxEstimate) {
                		maxEstimate = est;
                		solution = sol;
						solutionCount = 1;
                		System.out.println("-maxEstimate---> " + maxEstimate);
                		int requitedEstimate = 200;
                		if(maxEstimate > requitedEstimate) return;
                	}
                } else {
					if(solutionCount == 0) {
						solution = sol;
					}
                	++solutionCount;
                	///write();
                	if(solutionCount >= solutionLimit) return;
                }
            }
        }
    }
    
    boolean restrict() {
    	return solutionLimit == 1 && (moveCount == 1000 && wayCount[t] < 15);
//      return false; //for capitals
    }

    private void write() {
        System.out.print("" + solutionCount + ": ");
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[i].cells.length; j++)
              System.out.write(new byte[]{WordBase.instance.coder.getChar(words[i].cells[j].getContent())}, 0, 1);
            System.out.print(" ");
        }
        System.out.println("");
    }

    public void interrupt() {
        interrupted = true;
    }

    public int getSolutionCount() {
        return solutionCount;
    }
    
    public int getSolutionLimit() {
    	return solutionLimit;
    }

    public byte[] getSolution() {
        return solution;
    }

    private byte[] readSolution() {
        byte[] c = new byte[factory.cells.length];
        for (int i = 0; i < c.length; i++) {
            Cell cell = factory.cells[i];
            c[i] = (cell == null) ? (byte)255 : cell.getContent();
        }
        return c;
    }

}