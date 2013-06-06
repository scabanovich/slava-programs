package slava.puzzle.domino.analysis;

public class LineRestrectedDominoAnalyzerRunner {
    int[] f = new int[] {
      0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
      0, 0, 2, 1, 2, 0, 0, 0, 0, 0,
      0, 0, 2, 0, 2, 0, 0, 0, 0, 0,
      4, 4, 3, 0, 3, 4, 4, 0, 0, 0,
      1, 0, 3, 1, 3, 0, 1, 0, 0, 0,
      1, 0, 2, 1, 2, 0, 1, 0, 0, 0,
      2, 0, 2, 0, 2, 0, 2, 0, 0, 0,
      2, 0, 1, 0, 1, 0, 2, 0, 0, 0,
      3, 0, 1, 2, 1, 0, 3, 0, 0, 0,
      3, 0, 3, 2, 3, 0, 3, 0, 0, 0,
      0, 0, 3, 0, 3, 0, 0, 0, 0, 0
    };
    int[][] vLines = new int[][] {
/*x=0*/        {0,2,4},
/*x=1*/        {},
/*x=2*/        {0,1,4,5},
/*x=3*/        {},
/*x=4*/        {},
/*x=5*/        {},
/*x=6*/        {},
/*x=7*/        {},
/*x=8*/        {},
/*x=9*/        {}
	};
	int[][] hLines = new int[][] {
/*y=0*/        {},
/*y=1*/        {},
/*y=2*/        {},
/*y=3*/        {0,1,2,3},
/*y=4*/        {},
/*y=5*/        {},
/*y=6*/        {},
/*y=7*/        {},
/*y=8*/        {},
/*y=9*/        {},
/*y=10*/       {}
	};
    Dominoes dominoes = new Dominoes(6);
    DominoField field = new DominoField();
    DominoLineRestrictions restrictions = new DominoLineRestrictions();

    LineRestrectedDominoAnalyzer analyzer = new LineRestrectedDominoAnalyzer();

    public LineRestrectedDominoAnalyzerRunner() {}

    public void run() {
        field.init(10, 11, f);
        restrictions.setRestrictions(6, hLines, vLines);
        analyzer.setData(dominoes, field, restrictions);
        analyzer.init();
        analyzer.anal();
        System.out.println("solutionCount=" + analyzer.solutionCount + " treeSize=" + analyzer.treeSize);
    }

    public static void main(String[] args) {
        LineRestrectedDominoAnalyzerRunner runner = new LineRestrectedDominoAnalyzerRunner();
        runner.run();
    }

}
