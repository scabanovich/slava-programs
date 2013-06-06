package olia.coloring;

public class ColorProblemRunner {
	Field f = new Field();
	ColorAnalysis a = new ColorAnalysis();
	int[] form;
	
	public void prepare(int width, int height) {
		f.setSize(width, height);
		a.setField(f);
		form = new int[f.getSize()];
		for (int i = 0; i < form.length; i++) form[i] = 1;
		a.setForm(form);
	}
	
	public void runWithoutHoles(int width, int height) {
		prepare(width, height);
		a.solve();
		int solutionsCount = a.getSolutionCount() / 6;
		System.out.println("Q(" + width + "," + height + ")=" + solutionsCount);
	}
	
	public void runWithOneHole(int width, int height) {
		System.out.println("width=" + width + " height=" + height);
		prepare(width, height);
		for (int i = 0; i < form.length; i++) {
			form[i] = 0;
			a.solve();
			System.out.println(" i=" + i + " S=" + (a.getSolutionCount() / 6));
			form[i] = 1;
		}
	}

	public void runWithTwoHoles(int width, int height) {
		System.out.println("width=" + width + " height=" + height);
		prepare(width, height);
		for (int i1 = 0; i1 < form.length; i1++) {
			form[i1] = 0;
			for (int i2 = i1 + 1; i2 < form.length; i2++) {
				form[i2] = 0;
				a.solve();
				System.out.println(" i=" + i1 + "," + i2 + " S=" + (a.getSolutionCount() / 6));
				form[i2] = 1;
			}
			form[i1] = 1;
		}
	}

	public void runWithThreeHoles(int width, int height) {
		System.out.println("width=" + width + " height=" + height);
		prepare(width, height);
		for (int i1 = 0; i1 < form.length; i1++) {
			form[i1] = 0;
			for (int i2 = i1 + 1; i2 < form.length; i2++) {
				form[i2] = 0;
				for (int i3 = i2 + 1; i3 < form.length; i3++) {
					form[i3] = 0;
					a.solve();
					int solutionCount = a.getSolutionCount() / 6;
//					if(solutionCount > 1800 && solutionCount  < 2200) {
						System.out.println(" i=" + i1 + "," + i2 + "," + i3 + " S=" + solutionCount);
//					}
					form[i3] = 1;
				}
				form[i2] = 1;
			}
			form[i1] = 1;
		}
	}
	
	public void runWithRandomHoles(int width, int height, int holesCount) {
		int[] holes = new int[holesCount];
		prepare(width, height);
		int delta = 50000;
		while(true) {
			fillHoles(holes, f.getSize());
			for (int i = 0; i < holesCount; i++) form[holes[i]] = 0;
			a.solve();
			int solutionCount = a.getSolutionCount() / 6;
			if(Math.abs(solutionCount - 2005) < delta) {
				delta = Math.abs(solutionCount - 2005);
				System.out.print("Delta=" + delta);
				System.out.print(" Holes=");
				for (int i = 0; i < holesCount; i++) System.out.print(" " + holes[i]);
				System.out.print(" Solutions=" + solutionCount);
				System.out.println("");
			}
			for (int i = 0; i < holesCount; i++) form[holes[i]] = 1;
		}
	}
	
	void fillHoles(int[] holes, int size) {
		for (int i = 0; i < holes.length; i++) {
			holes[i] = (int)(size * Math.random());
		}
	}

	public static void main(String[] args) {
		ColorProblemRunner runner = new ColorProblemRunner();
		runner.runWithoutHoles(5, 6);
	}
}
