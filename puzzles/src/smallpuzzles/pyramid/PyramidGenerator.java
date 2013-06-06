package smallpuzzles.pyramid;

public class PyramidGenerator {
    PyramidAnalyzer analyzer = new PyramidAnalyzer();

    public PyramidGenerator() {}

    public void generateRandomField() {
        int[] field = analyzer.getField();
        for (int i = 0; i < field.length; i++) {
          do {
            field[i] = (int) (10 * Math.random());
          } while(!isFieldOk(field, i));
        }
    }

    private boolean isFieldOk(int[] field, int p) {
        if(p == 0) return true;
        if(field[p - 1] == field[p]) return false;
        if(p > 9 && field[p - 9] == field[p]) return false;
        if(p > 10 && field[p - 10] == field[p]) return false;
        if(p > 11 && field[p - 11] == field[p]) return false;
        return true;
    }

    public void findUniqueProblem() {
        int q = 0;
        do {
            generateRandomField();
            analyzer.initAnalyzer();
            analyzer.anal();
            ++q;
        } while(analyzer.solutionCount != 1);
        System.out.println("Attempts count = " + q);
        analyzer.printPyramid();
///        s.printSolutions();
   }



    public static void main(String[] args) {
        PyramidGenerator g = new PyramidGenerator();
        g.findUniqueProblem();
    }

}
