package smallpuzzles.cube;

public class SetsSifter {
    int[] filter;
    int[][] sets;
    int[][] compatibility;

    int[] state;
    int[] used;

    int[] wayCount;
    int[] selected;
    int[] way;
    int[][] ways;
    int limit;

    int t;
    int solutionCount;
    int[] solution;

    public SetsSifter() {}

    public void setData(int[] filter, int[][] sets) {
        this.filter = filter;
        this.sets = sets;
        computeCompatibility();
        init();
        anal();
    }

    public int getSolutionCount() {
        return solutionCount;
    }

    /*
     * For each element in filter returns number of set in array of sets.
     */
    public int[] getSolution() {
        return solution == null ? null : (int[])solution.clone();
    }

    private void computeCompatibility() {
        compatibility = new int[filter.length][filter.length];
        for (int p = 0; p < filter.length; p++) for (int c = 0; c < filter.length; c++)
            compatibility[p][c] = (_isCompatible(p, c)) ? 1 : 0;
    }

    private boolean _isCompatible(int p, int c) {
        for (int i = 0; i < filter.length; i++)
            if(sets[c][i] == filter[p]) return true;
        return false;
    }

    private boolean isCompatible(int p, int c) {
        return compatibility[p][c] == 1;
    }

    void init() {
        limit = filter.length;
        wayCount = new int[limit + 1];
        way = new int[limit + 1];
        selected = new int[limit + 1];
        ways = new int[limit + 1][limit];
        state = new int[limit];
        for (int i = 0; i < limit; i++) state[i] = -1;
        used = new int[limit];
        for (int i = 0; i < limit; i++) used[i] = -1;
        t = 0;
        solutionCount = 0;
    }

    private void srch() {
        wayCount[t] = 0;
        if(t == limit) return;
        narrowPlace.compute();
        if(narrowPlace.q == 1000 || narrowPlace.q < 1) return;
        selected[t] = narrowPlace.place;
        for (int c = 0; c < limit; c++) {
            if(used[c] > -1) continue;
            if(isCompatible(selected[t], c)) {
                ways[t][wayCount[t]] = c;
                ++wayCount[t];
            }
        }
    }

    NarrowPlace narrowPlace = new NarrowPlace();

    class NarrowPlace {
        int place;
        int q;

        void compute() {
            place = -1;
            q = 1000;
            for (int p = 0; p < limit; p++) {
                if(state[p] > -1) continue;
                int qc = getWayCount(p);
                if(qc < q) {
                    q = qc;
                    place = p;
                }
            }
        }

    }

    private int getWayCount(int place) {
        int q = 0;
        for (int c = 0; c < limit; c++) {
            if(used[c] > -1) continue;
            if(isCompatible(place, c)) ++q;
        }
        return q;
    }

    private void move() {
        int p = selected[t];
        int c = ways[t][way[t]];
        state[p] = c;
        used[c] = p;
        ++t;
        srch();
        way[t] = -1;
    }

    private void back() {
        --t;
        int p = selected[t];
        int c = ways[t][way[t]];
        state[p] = -1;
        used[c] = -1;
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
            if(t == limit) {
                ++solutionCount;
                if(solutionCount == 1) {
                    solution = new int[limit];
                    for (int i = 0; i < limit; i++) solution[i] = state[i];
                }
            }
        }
    }

    public static void main(String[] args) {
        SetsSifter s = new SetsSifter();
        int[] filter = {5, 9, 3};
        int[][] sets = {{2, 5, 3},
                        {4, 3, 9},
                        {3, 9, 4},
        };
        s.setData(filter, sets);
        System.out.println(s.solutionCount);
    }

}
