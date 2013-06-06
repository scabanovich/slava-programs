package smallpuzzles.section;

public class TwoPartSection {
    Field field;

    int firstOrigin;
    int secondOrigin;
    int transform;

    int solutionCount;

    int t;
    int partSize;
    int[][] ways;
    int[] place;
    int[] wayCount;
    int[] way;

    public TwoPartSection() {}

    public void setField(Field field) {
        this.field = field;
    }

    public void solve() {
        solutionCount = 0;
        if(!computePartSize()) return;
        for (int i = 0; i < field.size; i++) {
            if(field.values[i] < 0) continue;
            firstOrigin = i;
            break;
        }
        for (int i = 0; solutionCount == 0 && i < field.size; i++) {
            if(field.values[i] < 0 || i == firstOrigin) continue;
            secondOrigin = i;
            for (transform = 0; solutionCount == 0 && transform < 8; transform++) {
                solveForPlacedOrigins();
            }
        }
        System.out.println(solutionCount);
    }

    boolean computePartSize() {
      partSize = 0;
      for (int i = 0; i < field.size; i++) {
          if(field.values[i] >= 0) ++partSize;
      }
      if(partSize % 2 == 1) return false;
      partSize = partSize / 2;
      wayCount = new int[partSize + 1];
      ways = new int[partSize + 1][2];
      way = new int[partSize + 1];
      place = new int[partSize + 1];
      return true;
    }

    void solveForPlacedOrigins() {
        for (int i = 0; i < field.size; i++) {
            if(field.values[i] > 0) field.values[i] = 0;
        }
        field.values[firstOrigin] = 1;
        field.values[secondOrigin] = 2;
        t = 0;
        this.anal();
    }

    void srch() {
        wayCount[t] = 0;
        int q = 3;
        for (int i = 0; i < field.size; i++) {
            if(field.values[i] != 0) continue;
            boolean a1 = isFirstPartAccepted(i);
            boolean a2 = isSecondPartAccepted(i);
            int q1 = 0;
            if(a1) ++q1;
            if(a2) ++q1;
            if(q <= q1) continue;
            q = q1;
            place[t] = i;
            if(q1 == 0) {
                return;
            } else if(q1 == 1) {
                ways[t][0] = (a1) ? 1 : 2;
            } else {
                ways[t][0] = 1;
                ways[t][1] = 2;
            }
        }
        if(q < 3) wayCount[t] = q;
    }

    void move() {
        int w = ways[t][way[t]];
        int pa = place[t];
        int pb = (w == 1) ? getSecondPartPoint(pa) : getFirstPartPoint(pa);
        field.values[pa] = w;
        field.values[pb] = 3 - w;
        ++t;
        srch();
        way[t] = -1;
    }

    void back() {
        --t;
        int w = ways[t][way[t]];
        int pa = place[t];
        int pb = (w == 1) ? getSecondPartPoint(pa) : getFirstPartPoint(pa);
        field.values[pa] = 0;
        field.values[pb] = 0;
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
            if(t == partSize - 1 && SectionUtil.isConnectedPart(field)) {
                ++solutionCount;
                if(solutionCount == 1) {
                    //rememberSolution();
                    SectionUtil.printSolution(field);
///                    return;
                }
            }
        }
    }

    boolean isFirstPartAccepted(int i) {
        int p = getSecondPartPoint(i);
        return p >= 0 && p != i && field.values[p] == 0;
    }

    int getSecondPartPoint(int firstPartPoint) {
        return SectionUtil.getPoint(firstPartPoint, firstOrigin, transform, secondOrigin, field);
    }

    boolean isSecondPartAccepted(int i) {
        int p = getFirstPartPoint(i);
        return p >= 0 && p != i && field.values[p] == 0;
    }

    int getFirstPartPoint(int secondPartPoint) {
        return SectionUtil.getPoint(secondPartPoint, secondOrigin, SectionUtil.backTransform[transform], firstOrigin, field);
    }


}
