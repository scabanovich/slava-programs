package smallpuzzles.shoppingroute;

public class ShoppingRoutAnalizerRunner {
    ShoppingRoutAnalizer analyzer = new ShoppingRoutAnalizer();

    public ShoppingRoutAnalizerRunner() {}

    public void load() {
        analyzer.setNodes(new GraphLoader().load());
    }

    public void runAnalyzer() {
        analyzer.solve();
        System.out.println("solutionCount=" + analyzer.solutionCount);
        if(analyzer.solutionCount > 1) analyzer.printTransitionStatistic();
    }

    public static void main(String[] args) {
        ShoppingRoutAnalizerRunner runner = new ShoppingRoutAnalizerRunner();
        runner.load();
        runner.runAnalyzer();
    }
}