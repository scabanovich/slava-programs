package smallpuzzles.spiders;

public class SpidersExampleRunner {

    static int[][] spiders = new int[][]{
        {1, 1, 1, 0, 1, 0, 1, 1},
        {1, 0, 1, 0, 1, 1, 1, 0},
        //           x     x
        {1, 0, 1, 0, 1, 0, 0, 1},
        {1, 1, 0, 0, 1, 0, 1, 0},
        {1, 1, 1, 1, 1, 0, 1, 0},
        {0, 1, 1, 0, 1, 1, 1, 1},
        {1, 1, 1, 0, 1, 0, 1, 0},
        {0, 0, 1, 1, 1, 1, 1, 1},
        {1, 0, 1, 0, 1, 1, 1, 1}
    };

    public SpidersExampleRunner() {
    }

    public static void main(String[] args) {
        Spiders s = new Spiders();
        s.setSpiders(spiders);
        s.initField();
        s.initAnalizer();
        s.anal();
        System.out.println("variantCount = " + s.variantCount);
        s.printSolutions();
    }
}