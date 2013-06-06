package smallpuzzles.increasingjumps;

public class IncreasingJumpsAnalyzer {
    IncreasingJumpsField field;
    int jumpedAreaSize;

    int[] state;
    int[] path;

    int t;
    int[] wayCount;
    int[][] ways;
    int[] way;

    int solutionCount;
    int[] solution; //path
    int treeSize;

    int solutionLimit = -1;
    int printedSolutionLimit = 7;
    boolean randomize = false;

    public IncreasingJumpsAnalyzer() {}

    public void setRandomising(boolean b) {
        randomize = b;
    }

    public void setSolutionLimit(int k) {
        solutionLimit = k;
    }

    public void setPrintedSolutionLimit(int k) {
        printedSolutionLimit = k;
    }

    public void setField(IncreasingJumpsField field) {
        this.field = field;
    }

    public void setJumpedAreaSize(int jumpedAreaSize) {
        this.jumpedAreaSize = jumpedAreaSize;
    }

    public void solve() {
        init();
        anal();
    }

    void init() {
        state = new int[field.size()];
        for (int i = 0; i < field.size(); i++) state[i] = -1;
        wayCount = new int[jumpedAreaSize + 1];
        ways = new int[jumpedAreaSize + 1][field.size()];
        way = new int[jumpedAreaSize + 1];
        path = new int[jumpedAreaSize + 1];

        t = 0;
        solutionCount = 0;
        treeSize = 0;
    }

    void srch() {
        wayCount[t] = 0;
        if(t == jumpedAreaSize) return;
        if(t == 0) {
            for (int i = 0; i < field.size(); i++) {
                if(!field.isInField(i)) continue;
                ways[t][wayCount[t]] = i;
                wayCount[t]++;
            }
        } else {
            int p = path[t - 1];
            int lastDistance = (t == 1) ? 10000 : field.getDistance(p, path[t - 2]);
            int[] ns = field.getNeighbours(p);
            for (int i = 0; i < ns.length; i++) {
                if(state[ns[i]] != -1) continue;
                int d = field.getDistance(p, ns[i]);
                if(d >= lastDistance) continue;
                if(d + t < jumpedAreaSize) continue;
                ways[t][wayCount[t]] = ns[i];
                wayCount[t]++;
            }
        }
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

    void move() {
        path[t] = ways[t][way[t]];
        state[path[t]] = t;
        ++t;
        srch();
        way[t] = -1;
    }

    void back() {
        --t;
        state[path[t]] = -1;
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
            if(wayCount[t] == 0) ++treeSize;
            if(t == jumpedAreaSize) {
                ++solutionCount;
                if(solutionCount == 1) {
                    solution = (int[])path.clone();
                }
                if(solutionCount <= printedSolutionLimit) {
                    printSolution();
                }
                if(solutionCount == solutionLimit) return;
            }
        }
    }

    void printSolution() {
//        for (int i = 0; i < t; i++) System.out.print(" " + path[i]);
//        System.out.println("");
        for (int i = 0; i < field.size(); i++) {
            if(i % field.width == 0) System.out.println("");
            System.out.print(" ");
            if(state[i] < 0) System.out.print('-'); else System.out.print((char)(state[i] + 97));
        }
        System.out.println("");
    }

    public int getSolutionCount() {
        return solutionCount;
    }

    public int[] getSolution() {
        return (solutionCount < 1) ? null : solution;
    }

}
