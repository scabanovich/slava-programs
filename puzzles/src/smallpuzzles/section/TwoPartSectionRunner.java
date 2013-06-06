package smallpuzzles.section;

public class TwoPartSectionRunner {
    static int[] field = new int[] {
        -1,  0,  0,  0,  0, -1, -1, -1,
        -1,  0, -1, -1,  0, -1, -1, -1,
        -1,  0, -1,  0,  0, -1, -1, -1,
        -1, -1, -1,  0,  0,  0, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1,
    };

    public TwoPartSectionRunner() {}

    public void runThreePartSection() {
        Field f = new Field();
        f.setSize(9, 9);
//        for (int i = 0; i < field.length; i++) {
        for (int i = 0; i < f.size; i++) {
            f.values[i] = 0;//field[i];
        }
        ThreePartSection section = new ThreePartSection();
        section.setField(f);
        section.solve();
    }

    public void runRegionEnumerator() {
        RegionEnumerator e = new RegionEnumerator();
        e.setRegionSize(12);
        e.initField();
        e.run();
    }

    int goodRegionCount = 0;

    public void runRegionEnumerator2() {
        RegionEnumerator e = new RegionEnumerator();
        final Field f = e.field;
        e.setRegionSize(9);
        e.initField();
        final ThreePartSection section = new ThreePartSection();
        final Field f2 = new Field();
        f2.setSize(f.width, f.height);
        section.setField(f2);
        goodRegionCount = 0;
        e.listener = new RegionEnumerator.RegionEnumeratorListener() {
            public void regionBuilt() {
                for (int i = 0; i < f.size; i++) f2.values[i] = f.values[i];
                section.solve();
                if(section.solutionCount == 0) {
                  ++goodRegionCount;
                }
            }
        };
        e.run();
        System.out.println("goodRegionCount=" + goodRegionCount);
    }

    public static void main(String[] args) {
        TwoPartSectionRunner runner = new TwoPartSectionRunner();
        runner.runRegionEnumerator2();
    }

}
