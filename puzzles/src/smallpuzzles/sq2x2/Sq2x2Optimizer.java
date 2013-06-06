package smallpuzzles.sq2x2;

public class Sq2x2Optimizer {
    int width = 10;
    int height = 10;
    int perimeter = width + height;
    int[] xSumLimits = new int[]{9, 10, 1, 13, 13, 3, 5, 2, 17, 17};
    int[] ySumLimits = new int[]{14, 21, 7, 2, 6, 10, 14, 8, 4, 4};

    Sq2x2 analyzer = new Sq2x2();
    int[] permutation = new int[perimeter];

    public Sq2x2Optimizer() {
        analyzer.init();
        analyzer.setSumLimits(xSumLimits, ySumLimits);
        analyzer.setSolutionCountLimit(2);
    }

    public void init() {
        for (int i = 0; i < perimeter; i++) permutation[i] = i;
        for (int i = perimeter - 1; i >= 1; i--) {
            int ic = (int)(i * Math.random());
            int k = permutation[i];
            permutation[i] = permutation[ic];
            permutation[ic] = k;
        }
    }

    public void run() {
        analyzer.initAnal();
        analyzer.anal();
        if(analyzer.getSolutionCount() != 1) return;
        for (int i = 0; i < perimeter; i++) {
            int k = permutation[i];
            if(k < height) {
                int m = xSumLimits[k];
                if(m == -1) continue;
                xSumLimits[k] = -1;
                analyzer.initAnal();
                analyzer.anal();
                if(analyzer.getSolutionCount() > 1) {
                    xSumLimits[k] = m;
                } else System.out.println("horizontal: " + k);
            } else {
                int v = k - height;
                int m = ySumLimits[v];
                if(m == -1) continue;
                ySumLimits[v] = -1;
                analyzer.initAnal();
                analyzer.anal();
                if(analyzer.getSolutionCount() > 1) {
                    ySumLimits[v] = m;
                } else System.out.println("vertical: " + v);
            }
        }
    }

    public static void main(String[] args) {
        Sq2x2Optimizer s = new Sq2x2Optimizer();
        s.init();
        s.run();
    }

}