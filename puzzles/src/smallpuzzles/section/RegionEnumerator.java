package smallpuzzles.section;

public class RegionEnumerator {
    public interface RegionEnumeratorListener {
        public void regionBuilt();
    }
    RegionEnumeratorListener listener;
    Field field = new Field();

    int regionSize;

    int cursorT;
    int cursorDirection;

    int currentSize;
    int[] cursorPlace;

    int t;
    int[][] ways;
    int[] wayCount;
    int[] way;
    int[] place;

    int solutionCount;

    public RegionEnumerator() {}

    public void setRegionSize(int size) {
        regionSize = size;
    }

    int Q = 9;

    public void initField() {
        field.setSize(2 * Q, Q);
        for (int i = 0; i < Q; i++) field.values[i] = -2;
        field.values[Q] = 0;
        ways = new int[field.size][2];
        place = new int[field.size];
        wayCount = new int[field.size];
        way = new int[field.size];
        cursorPlace = new int[regionSize + 1];
    }

    public void run() {
        initAnalyzer();
        anal();
        System.out.println("solutionCount=" + solutionCount + ":" + this.currentSize);
    }

    void initAnalyzer() {
        t = 0;
        place[t] = Q;
        currentSize = 1;
        cursorT = 0;
        cursorDirection = 0;
        cursorPlace[cursorT] = Q;
    }

    void srch() {
        wayCount[t] = 0;
        if(currentSize >= regionSize) return;
        cursorT = 0;
        cursorDirection = 0;
        while(cursorT < currentSize) {
            int p = field.jp[cursorDirection][cursorPlace[cursorT]];
            if(p < 0 || field.values[p] != -1) {
                nextCursor();
            } else {
                wayCount[t] = 2;
                ways[t][0] = 0;
                ways[t][1] = -2;
                place[t] = p;
                return;
            }
        }
    }

    private void nextCursor() {
        cursorDirection++;
        if(cursorDirection == 4) {
            cursorDirection = 0;
            cursorT++;
        }
    }

    void move() {
        int v = ways[t][way[t]];
        field.values[place[t]] = v;
        if(v == 0) {
            cursorPlace[currentSize] = place[t];
            currentSize++;
        }
        ++t;
        srch();
        way[t] = -1;
    }

    void back() {
        --t;
        int v = ways[t][way[t]];
        field.values[place[t]] = -1;
        if(v == 0) {
            currentSize--;
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
            if(currentSize == regionSize) {
                ++solutionCount;
                if(solutionCount == 1) {
                    SectionUtil.printSolution(field);
                }
                if(listener != null) listener.regionBuilt();
            }
        }
    }

}
