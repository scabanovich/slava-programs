package slava.puzzle.domino.analysis;

public class LineRestrictedDominoGenerator {
    int max;
    int width;
    int height;
    int[] form;
    int[][] vLines;
    int[][] hLines;
    int[][] vLinesCopy;
    int[][] hLinesCopy;

    boolean[] attempts;

    Dominoes dominoes = new Dominoes(max);
    DominoField field = new DominoField();
    DominoLineRestrictions restrictions = new DominoLineRestrictions();

    LineRestrectedDominoAnalyzer analyzer = new LineRestrectedDominoAnalyzer();
    boolean debug = true;

    public LineRestrictedDominoGenerator() {}
    
    public void setDebugging(boolean b) {
    	debug = b;
    	if(!debug) analyzer.setPrintedSolutionLimit(0);
    }

    public void setData(int max, int width, int height, int[] form) {
        this.max = max;
        this.width = width;
        this.height = height;
        this.form = form;
        if(dominoes.max != max) dominoes.setMaximum(max);
        if(form.length != width * height) throw new RuntimeException("Inconsistent data: Field Size= " + form.length + ", Width * Height = " + (width * height));
        field.init(width, height, form);
        analyzer.setData(dominoes, field, restrictions);
    }

    private void resetRestrictions() {
        vLines = new int[width][0];
        hLines = new int[height][0];
        restrictions.setRestrictions(max, hLines, vLines);
    }

    public boolean generateRandomState() {
        resetRestrictions();
        analyzer.setRandomising(true);
		if(debug) analyzer.setPrintedSolutionLimit(2);
        analyzer.setSolutionLimit(1);
        analyzer.init();
        analyzer.anal();
        analyzer.setRandomising(false);
        analyzer.setSolutionLimit(2);
        createRestrictions();
        return (analyzer.solutionCount > 0);
    }
    
    public boolean generateRandomStateWithUniqueSolution() {
    	int i = 0;
    	while(++i < 50) {
	    	boolean b = generateRandomState();
	    	if(!b) return false;
			analyzer.setRandomising(false);
			analyzer.setSolutionLimit(2);
			analyzer.setPrintedSolutionLimit(0);
			restrictions.setRestrictions(max, hLines, vLines);
			analyzer.init();
			analyzer.anal();
			if(analyzer.solutionCount == 1) return true;
	    	if(debug) System.out.println("solutionCount > 1");
    	}
    	return false;
    }

    private void createRestrictions() {
        int[] vs = new int[max];
        for (int x = 0; x < width; x++) {
            for (int q = 0; q < max; q++) vs[q] = 0;
            for(int y = 0; y < height; y++) {
                int p = y * width + x;
                if(field.isInField(p) && field.getValue(p) >= 0) vs[field.getValue(p)]++;
            }
            vLines[x] = getLineRestrictions(vs);
        }
        for (int y = 0; y < height; y++) {
            for (int q = 0; q < max; q++) vs[q] = 0;
            for(int x = 0; x < width; x++) {
                int p = y * width + x;
                if(field.isInField(p) && field.getValue(p) >= 0) vs[field.getValue(p)]++;
            }
            hLines[y] = getLineRestrictions(vs);
        }
        hLinesCopy = (int[][])hLines.clone();
        vLinesCopy = (int[][])vLines.clone();
    }

    private int[] getLineRestrictions(int[] vs) {
        int c = 0;
        for (int q = 0; q < vs.length; q++) if(vs[q] > 0) ++c;
        int[] rs = new int[c];
        c = 0;
        for (int q = 0; q < vs.length; q++) if(vs[q] > 0) {
            rs[c] = q;
            ++c;
        }
        return rs;
    }

    private void printRestrictions() {
        for (int x = 0; x < width; x++) {
            if(vLines[x].length == 0) continue;
            System.out.print("x= " + x + ": ");
            for (int i = 0; i < vLines[x].length; i++)
              System.out.print(" " + vLines[x][i]);
            System.out.println("");
        }
        for (int y = 0; y < height; y++) {
            if(hLines[y].length == 0) continue;
            System.out.print("y= " + y + ": ");
            for (int i = 0; i < hLines[y].length; i++)
              System.out.print(" " + hLines[y][i]);
            System.out.println("");
        }
    }

    public void restrict() {
        hLines = (int[][])hLinesCopy.clone();
        vLines = (int[][])vLinesCopy.clone();

        analyzer.setRandomising(false);
        analyzer.setSolutionLimit(2);
        analyzer.setPrintedSolutionLimit(0);
        attempts = new boolean[width + height];
        int c = 0;
        for (int x = 0; x < width; x++) {
            attempts[x] = vLines[x].length > 0;
            if(attempts[x]) ++c;
        }
        for (int y = 0; y < height; y++) {
            attempts[width + y] = hLines[y].length > 0;
            if(attempts[width + y]) ++c;
        }
        while(c > 0) {
            int k = -1;
            do { k = (int)(attempts.length * Math.random()); } while(!attempts[k]);
            attempts[k] = false;
            if(k < width) {
                int x = k;
                int[] back = vLines[x];
                vLines[x] = new int[]{};
                restrictions.setRestrictions(max, hLines, vLines);
                analyzer.init();
                analyzer.anal();
                if(analyzer.solutionCount > 1) {
                    vLines[x] = back;
                } else if(analyzer.solutionCount == 0) {
                  throw new RuntimeException("Error on removing x=" + x);
                }
            } else {
                int y = k - width;
                int[] back = hLines[y];
                hLines[y] = new int[]{};
                restrictions.setRestrictions(max, hLines, vLines);
                analyzer.init();
                analyzer.anal();
                if(analyzer.solutionCount > 1) {
                    hLines[y] = back;
                } else if(analyzer.solutionCount == 0) {
                    throw new RuntimeException("Error on removing y=" + y);
                }
            }
            --c;
        }
        if(debug) {
			System.out.println("Restrictions:");
			printRestrictions();
        }

        restrictions.setRestrictions(max, hLines, vLines);
        analyzer.init();
        analyzer.anal();
        if(analyzer.solutionCount != 1) {
            throw new RuntimeException("Error in created problem: solutionCount=" + analyzer.solutionCount);
        }
        if(debug) System.out.println("treeSize=" + analyzer.treeSize);
    }
    
    public int[] getSolution() {
    	return analyzer.getSolution();
    }
    
	public int[][] getHRestrictions() {
		return (int[][])hLines.clone();
	}

	public int[][] getVRestrictions() {
		return (int[][])vLines.clone();
	}

    static int[] my_form = new int[] {
      0, 0, 0, 0, 3, 0, 1, 0, 0, 0,
      0, 0, 0, 2, 3, 0, 1, 0, 0, 0,
      0, 0, 1, 2, 0, 0, 2, 0, 0, 0,
      0, 2, 1, 0, 0, 1, 2, 0, 1, 0,
      0, 2, 0, 0, 0, 1, 0, 0, 1, 0,
      1, 1, 2, 2, 3, 3, 4, 4, 3, 3,
      0, 2, 0, 0, 0, 1, 0, 0, 1, 0,
      0, 2, 0, 0, 0, 1, 2, 0, 1, 0,
      0, 3, 3, 4, 0, 0, 2, 0, 0, 0,
      0, 0, 0, 4, 3, 0, 3, 0, 0, 0,
      0, 0, 0, 0, 3, 0, 3, 0, 0, 0
    };

	public static void main(String[] args) {
		LineRestrictedDominoGenerator runner = new LineRestrictedDominoGenerator();
		runner.setData(6, 10, 11, my_form);
		if(!runner.generateRandomStateWithUniqueSolution()) {
			System.out.println("Cannot generate problem.");
		} else {
			runner.restrict();
		}
	}

}
