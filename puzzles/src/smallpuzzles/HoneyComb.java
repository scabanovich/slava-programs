package smallpuzzles;

public class HoneyComb {
    int delta = 3;
    int size = 0;
    int[][] bonds = new int[0][];

    int[] state;
    int[][] vacancies;
    int[] vacanciesCount;

    int[] wayCount;
    int[][] ways;
    int[] place;
    int[] way;
    int t;

    int solutionCount = 0;

    int place_1, place_2, place_3;
    int[][][] distributionCount;
    int[][][][] distributionState;

    public HoneyComb() {}

    public void setBonds(int[][] bonds) {
        this.bonds = bonds;
        size = bonds.length;
    }

    public void setProblemPlaces(int place_1, int place_2, int place_3) {
        this.place_1 = place_1;
        this.place_2 = place_2;
        this.place_3 = place_3;
    }

    public void init() {
        wayCount = new int[size + 1];
        ways = new int[size + 1][size];
        place = new int[size + 1];
        way = new int[size + 1];
        state = new int[size];
        for (int i = 0; i < size; i++) state[i] = -1;
        vacanciesCount = new int[size];
        vacancies = new int[size][size];
        for (int i = 0; i < size; i++) {
            vacanciesCount[i] = size;
            for (int j = 0; j < size; j++) vacancies[i][j] = 0;
        }
        t = 0;
        solutionCount = 0;
        distributionCount = new int[size][size][size];
        distributionState = new int[size][size][size][size];
    }

    protected void srch() {
        wayCount[t] = 0;
        if(t == size) return;
        place[t] = getMostRestrictedPlace();
        if(place[t] < 0) return;
        for (int i = 0; i < size; i++) {
            if(vacancies[place[t]][i] > 0) continue;
            ways[t][wayCount[t]] = i;
            ++wayCount[t];
        }
    }

    private int getMostRestrictedPlace() {
        int v = size + 1;
        int p = -1;
        for (int i = 0; i < size; i++) {
            if(state[i] >= 0 || vacanciesCount[i] >= v) continue;
            v = vacanciesCount[i];
            p = i;
        }
        return p;
    }

    protected void move() {
        int p = place[t];
        int v = ways[t][way[t]];
        state[p] = v;
        for (int i = 0; i < bonds[p].length; i++) {
            int p2 = bonds[p][i];
            for (int j = v - delta + 1; j < v + delta; j++) {
                if(j < 0 || j >= size) continue;
                if(vacancies[p2][j] == 0) --vacanciesCount[p2];
                ++vacancies[p2][j];
            }
        }
        for (int i = 0; i < size; i++) {
            if(vacancies[i][v] == 0) --vacanciesCount[i];
            ++vacancies[i][v];
        }
        ++t;
        srch();
        way[t] = -1;

    }

    protected void back() {
        --t;
        int p = place[t];
        int v = ways[t][way[t]];
        state[p] = -1;
        for (int i = 0; i < bonds[p].length; i++) {
            int p2 = bonds[p][i];
            for (int j = v - delta + 1; j < v + delta; j++) {
                if(j < 0 || j >= size) continue;
                --vacancies[p2][j];
                if(vacancies[p2][j] == 0) ++vacanciesCount[p2];
            }
        }
        for (int i = 0; i < size; i++) {
            --vacancies[i][v];
            if(vacancies[i][v] == 0) ++vacanciesCount[i];
        }
    }

    public void anal() {
        init();
        srch();
        way[t] = -1;
        while(true) {
            while(way[t] >= wayCount[t] - 1) {
                if(t == 0) return;
                back();
            }
            ++way[t];
            move();
            if(t == size) {
              ++solutionCount;
              processSolution();
// progress
              if(solutionCount % 10000 == 0) System.out.print("x");
            }
        }
    }

    private void processSolution() {
        ++distributionCount[state[place_1]][state[place_2]][state[place_3]];
        if(distributionCount[state[place_1]][state[place_2]][state[place_3]] > 1) return;
        for (int k = 0; k < size; k++)
          distributionState[state[place_1]][state[place_2]][state[place_3]][k] = state[k];
    }

    public int getUniqueProblemCount() {
        int result = 0;
        for (int v1 = 0; v1 < size; v1++)
        for (int v2 = 0; v2 < size; v2++)
        for (int v3 = 0; v3 < size; v3++) {
            if(distributionCount[v1][v2][v3] != 1) continue;
            ++result;
        }
        return result;
    }

    public int visual(int i) {
        return i + 1;
    }

    public void writeUniqueProblems() {
        System.out.println("Places = " + visual(place_1) + ", " + visual(place_2) + ", " + visual(place_3));
        for (int v1 = 0; v1 < size; v1++)
        for (int v2 = 0; v2 < size; v2++)
        for (int v3 = 0; v3 < size; v3++) {
            if(distributionCount[v1][v2][v3] != 1) continue;
            System.out.println("Condition: " + visual(v1) + " " + visual(v2) + " " + visual(v3));
            System.out.println("Solution:");
            for (int i = 0; i < size; ++i)
              System.out.print(" " + visual(distributionState[v1][v2][v3][i]));
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        HoneyComb h = new HoneyComb();
        h.setBonds(Combs.comb3);
        h.setProblemPlaces(0, 6, 9);
        h.anal();
        System.out.println("Total state count = " + h.solutionCount);
        System.out.println("Unique problem count = " + h.getUniqueProblemCount());
        h.writeUniqueProblems();
    }

}
