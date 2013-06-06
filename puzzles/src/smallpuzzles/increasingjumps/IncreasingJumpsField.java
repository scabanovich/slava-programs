package smallpuzzles.increasingjumps;

public class IncreasingJumpsField {
    int width;
    int height;
    int size;

    int[] status; // 1 - cell belongs to field, 0 - does not.
    int[][] distances;
    int[][] neighbours;
    int maximumDistance = 0;

    public IncreasingJumpsField() {}

    public void setData(int width, int height) {
        this.width = width;
        this.height = height;
        size = width * height;
        status = new int[size];
        for (int i = 0; i < size; i++) status[i] = 1;
    }

    public void setStatus(int i, boolean status) {
        this.status[i] = (status) ? 1 : 0;
    }

    public void build() {
        int maxd = width * width + height * height + 1;
        int[] ds = new int[maxd];
        for (int i = 0; i < maxd; i++) ds[i] = 0;
        distances = new int[size][size];
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) distances[i][j] = -1;
        for (int i = 0; i < size; i++) {
            if (!isInField(i)) continue;
            for (int j = 0; j < size; j++) {
                if (i == j || !isInField(j)) continue;
                int dx = (i % width) - (j % width);
                int dy = (i / width) - (j / width);
                int d = dx * dx + dy * dy;
                ds[d] = 1;
                distances[i][j] = d;
            }
        }
        for (int i = 1; i < maxd; i++) ds[i] += ds[i - 1];
        maximumDistance = ds[maxd - 1];
        for (int i = 0; i < size; i++) for (int j = 0; j < size; j++) {
            if(distances[i][j] == -1) continue;
            distances[i][j] = ds[distances[i][j]];
        }
        buildNeighbours();
    }

    private void buildNeighbours() {
        neighbours = new int[size][0];
        for (int i = 0; i < size; i++) {
            if(!isInField(i)) continue;
            int c = 0;
            for (int j = 0; j < size; j++) if(i != j && isInField(j)) ++c;
            neighbours[i] = new int[c];
            c = 0;
            for (int j = 0; j < size; j++) if(i != j && isInField(j)) {
                neighbours[i][c] = j;
                ++c;
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean isInField(int i) {
        return i >= 0 && i < size && status[i] == 1;
    }

    public int getDistance(int i, int j) {
        return distances[i][j];
    }

    public int[] getNeighbours(int i) {
        return neighbours[i];
    }

    public int getMaxDistance() {
        return maximumDistance;
    }

}
