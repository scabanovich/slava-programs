package smallpuzzles.increasingjumps;

public class IncreasingJumpsGenerator {
    IncreasingJumpsField field = new IncreasingJumpsField();
    IncreasingJumpsAnalyzer analyzer = new IncreasingJumpsAnalyzer();

    public IncreasingJumpsGenerator() {
        analyzer.setField(field);
    }

    public void setData(int width, int height, int jumpedAreaSize) {
        field.setData(width, height);
        analyzer.setJumpedAreaSize(jumpedAreaSize);
    }

    public boolean generate() {
        while(true) {
            clearField();
            if(field.getMaxDistance() + 1 < analyzer.jumpedAreaSize) throw new RuntimeException("Too small field.");
            analyzer.setRandomising(true);
            analyzer.setPrintedSolutionLimit(0);
            analyzer.setSolutionLimit(1);
            analyzer.solve();
            if(analyzer.getSolutionCount() == 0) return false;
            restrictFieldToPath(analyzer.getSolution(), analyzer.jumpedAreaSize);
            analyzer.setRandomising(false);
            analyzer.setSolutionLimit(10);
            analyzer.setPrintedSolutionLimit(1);
            analyzer.solve();
            System.out.println("solutionCount=" + analyzer.getSolutionCount());
            if(analyzer.getSolutionCount() == 1) {
                System.out.println("treeSize=" + analyzer.treeSize);
                break;
            }
        }
        return true;
    }

    private void clearField() {
        for (int i = 0; i < field.size(); i++) field.setStatus(i, true);
        field.build();
    }

    private void restrictFieldToPath(int[] path, int jumpedAreaSize) {
        for (int i = 0; i < field.size(); i++) field.setStatus(i, false);
        for (int i = 0; i < jumpedAreaSize; i++) field.setStatus(path[i], true);
        field.build();
    }

}
