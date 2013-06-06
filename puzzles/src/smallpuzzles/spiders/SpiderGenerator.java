package smallpuzzles.spiders;

public class SpiderGenerator {
    int width = 3;
    int height = 3;
    int size = width * height;
    int spiderLegMinimum = 4;
    int extraRemovedLegsCount = 0;
    int removedLegsCount = size * 8 - (2 * width + 1) * (2 * height + 1) + 1 + extraRemovedLegsCount;

    int[][] spiders = new int[size][8];

    public SpiderGenerator() {}

    public void generate() {
        for (int c = 0; c < size; c++) for (int d = 0; d < 8; d++) spiders[c][d] = 1;
        int[] legs = new int[size];
        for (int i = 0; i < size; i++) legs[i] = 8;
        int k = 0;
        while (k < removedLegsCount) {
            int c = (int)(size * Math.random());
            if(legs[c] <= spiderLegMinimum) continue;
            int d = (int)(8 * Math.random());
            if(spiders[c][d] == 1) {
                spiders[c][d] = 0;
                legs[c]--;
                ++k;
            }
        }
    }

    private boolean hasEqual() {
        for (int i = 0; i < spiders.length; i++) {
          for (int j = i + 1; j < spiders.length; j++) {
              boolean b = true;
              for (int d = 0; d < 8; d++) {
                  if(spiders[i][d] != spiders[j][d]) b = false;
              }
              if(b) return true;
          }
        }
        return false;
    }

    public void findUniqueProblem() {
        Spiders s = new Spiders();
        s.setSize(width, height);
        s.initField();
        int q = 0;
        do {
            do {
              generate();
            } while(hasEqual());
            s.setSpiders(spiders);
            s.initAnalizer();
            s.anal();
            ++q;
        } while(s.solutionCount != 1);
        System.out.println("Attempts count = " + q);
        s.printSpiders();
        s.printSolutions();

    }

    public static void main(String[] args) {
        SpiderGenerator g = new SpiderGenerator();
        g.findUniqueProblem();
    }

}
