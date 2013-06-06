package smallpuzzles.shoppingroute;

public class ShoppingRoutAnalizer {
    Node[] nodes;

    int t;
    int[] wayCount;
    int[][] ways;
    int[] way;

    Node[] route;

    int markets;
    int parkings;

    int solutionCount;
    int treeSize;

    public ShoppingRoutAnalizer() {}

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
        int mx = nodes.length + 1;
        wayCount = new int[mx];
        ways = new int[mx][10];
        way = new int[mx];
        route = new Node[mx];
    }

    public void solve() {
        markets = 0;
        parkings = 0;
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].setPassed(false);
            changeObjects(nodes[i].getKind(), 1);
            Transition[] ts = nodes[i].getTransitions();
            for (int j = 0; j < ts.length; j++) {
                ts[j].initRuntime();
                if(ts[j].getSource().getId() > ts[j].getTarget().getId()) continue;
                changeObjects(ts[j].getKind(), 1);
            }
        }
        t = 0;
        solutionCount = 0;
        treeSize = 0;
        anal();
    }

    void changeObjects(int kind, int delta) {
        if(kind == 1) parkings += delta; else if(kind == 2) markets += delta;
    }

    void srch() {
        wayCount[t] = 0;
        if(parkings == 0) return;
        if(t == 0) {
            for (int i = 0; i < nodes.length; i++) {
                if(nodes[i].getKind() != 1) continue;
                ways[t][wayCount[t]] = i;
                ++wayCount[t];
            }
        } else {
            int wc = 0;
            int force = -1;
            Transition[] ts = route[t - 1].getTransitions();
            for (int i = 0; i < ts.length; i++) {
                if(!ts[i].isEnabled()) continue;
                if(ts[i].getTarget().isPassed()) {
                    if(t > 1 && ts[i].getTarget() == route[t - 2]) continue;
                    if(ts[i].getKind() != 0) return;
                    continue;
                } else if(ts[i].getKind() != 0) {
                    if(force >= 0) return;
                    force = i;
                } else {
                    ways[t][wc] = i;
                    ++wc;
                }
            }
            if(force >= 0) {
                wc = 1;
                ways[t][0] = force;
            }
            wayCount[t] = wc;
        }
    }

    void move() {
        int w = ways[t][way[t]];
        if(t == 0) {
            route[t] = nodes[w];
            changeObjects(route[t].getKind(), -1);
            route[t].setPassed(true);
        } else {
            Node n = route[t - 1];
            Transition tr = n.getTransitions()[w];
            route[t] = tr.getTarget();
            changeObjects(tr.getKind(), -1);
            changeObjects(route[t].getKind(), -1);
            route[t].setPassed(true);
        }
        ++t;
        srch();
        way[t] = -1;
    }

    void back() {
        --t;
        int w = ways[t][way[t]];
        if(t == 0) {
            changeObjects(route[t].getKind(), 1);
            route[t].setPassed(false);
        } else {
            Transition tr = route[t - 1].getTransitions()[w];
            changeObjects(tr.getKind(), 1);
            changeObjects(route[t].getKind(), 1);
            route[t].setPassed(false);
        }
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
            if(parkings == 0 && markets == 0) {
                ++solutionCount;
                onSolutionFound();
                if(solutionCount == 1) {
                    printSolution();
                }
            }
        }
    }

    public void printTransitionStatistic() {
        for (int i = 0; i < nodes.length; i++) {
            Transition[] ts = nodes[i].getTransitions();
            for (int j = 0; j < ts.length; j++) {
                int k = ts[j].getSolutions();
                if(k == 0) continue;
                System.out.print(" " + Util.getPresentation(ts[j]) + ":" + k);
            }
        }
        System.out.println("");
    }

    private void printSolution() {
        for (int i = 0; i < t; i++)
            System.out.print(Util.getPresentation(route[i]));
        System.out.println("");
    }

    private void onSolutionFound() {
        for (int i = 1; i < t; i++) {
            int w = ways[i][way[i]];
            Node src = route[i - 1];
            src.getTransitions()[w].solutionFound();
        }
    }

    public int getSolutionCount() {
        return solutionCount;
    }

}
