package smallpuzzles.increasingjumps;

public class IncreasingJumpsRunner {

    public IncreasingJumpsRunner() {}

    public void generate(int width, int height, int jumpedAreaSize) {
        IncreasingJumpsGenerator generator = new IncreasingJumpsGenerator();
        generator.setData(width, height, jumpedAreaSize);
        boolean b = generator.generate();
        if(!b) System.out.println("Cannot generate a problem.");
    }

    public void countPaths(int width, int height, int jumpedAreaSize) {
        IncreasingJumpsField field = new IncreasingJumpsField();
        field.setData(width, height);
        field.build();
        IncreasingJumpsAnalyzer analyzer = new IncreasingJumpsAnalyzer();
        analyzer.setField(field);
        analyzer.setSolutionLimit(-1);
        analyzer.setPrintedSolutionLimit(0);
        analyzer.setJumpedAreaSize(jumpedAreaSize);
        analyzer.solve();
        System.out.println("solutionCount=" + analyzer.getSolutionCount());
    }

    public static void main(String[] args) {
        IncreasingJumpsRunner runner = new IncreasingJumpsRunner();
        runner.generate(6, 4, 13);
//        runner.generate(5, 5, 13);
///        runner.countPaths(8, 8, 32);
    }
}