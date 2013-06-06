package slava.algebra;

public class PaintGraphProblemGenerator {
	int[][] graph;
	int[] coloring;
	PaintGraphAnalysis g = new PaintGraphAnalysis();
	
	public void setGraph(int[][] graph) {
		this.graph = graph;
		g.setGraph(graph);
		coloring = new int[graph.length];
		for (int i = 0; i < graph.length; i++) coloring[i] = -1;
	}
	
	public void setColorCount(int c) {
		g.setColorCount(c);
	}
	
	public int[] getColoring() {
		return coloring;
	}
	
	public void generate() {
		for (int i = 0; i < graph.length; i++) coloring[i] = -1;
		g.setInitialColoring(coloring);
		g.setSolutionLimit(1);
		System.out.println("begin");
		g.solve();
		System.out.println("end");
		if(g.getSolutionCount() == 0) return;
		coloring = g.getSolution();
		g.setSolutionLimit(2);
		int[] q = getRundomQueue();
		for (int i = 0; i < q.length; i++) {
			int c = coloring[q[i]];
			coloring[q[i]] = -1;
			g.setInitialColoring(coloring);
			g.solve();
			if(g.getSolutionCount() != 1) {
				coloring[q[i]] = c;
			}
		}
	}
	
	int[] getRundomQueue() {
		int[] q = new int[graph.length];
		for (int i = 0; i < q.length; i++) q[i] = i;
		for (int i = q.length - 1; i > 0; i--) {
			int j = (int)((i + 1) * Math.random());
			int c = q[i];
			q[i] = q[j];
			q[j] = c;
		}
		return q;
	}
	
	public static void main(String[] args) {
		int[][] graph = new int[][]{};
		PaintGraphProblemGenerator g = new PaintGraphProblemGenerator();
		g.setColorCount(6);
		g.setGraph(graph);
		g.generate();
		System.out.println("emptyCount=" + g.g.sizeToColor);
		int[] problem = g.getColoring();
		for (int i = 0; i < problem.length; i++) {
			System.out.print(" " + problem[i]);
		}
		System.out.println("");
	}

}
