package olia;

import java.util.Arrays;
import com.slava.common.RectangularField;

public class ChangeableCroswordGenerator {
	int width;
	int height;
	
	public ChangeableCroswordGenerator() {}
	
	public void generate(int width, int height) {
		this.width = width;
		this.height = height;
		while(true) {
			String[] parts = generateParts();
			String[] words = getWords(parts);
			ChangeableCroswordSolver solver = new ChangeableCroswordSolver();
			solver.setProblem(words, width, height);
			solver.solve();
			System.out.println("Solutions=" + solver.getSolutionCount());
			if(solver.getSolutionCount() == 1) {
				System.out.println("Problem");
				for (int i = 0; i < words.length; i++) {
					System.out.println(words[i]);
				}
				System.out.println("Solution");
				solver.printSolution(solver.getSolution());
				break;
			}
		}
	}
	
	String[] generateParts() {
		String[] parts = new String[width * height];
		for (int i = 0; i < parts.length; i++) parts[i] = "";
		int totalLength = (int) (2.1 * width * height);
		int l = 0;
		while(l < totalLength) {
			int i = (int)(parts.length * Math.random());
			if(parts[i].length() == 3) continue;
			int v = (int)(2 * Math.random()) + 1;
			parts[i] += v;
			l++;
		}		
		return parts;
	}
	
	String[] getWords(String[] parts) {
		RectangularField f = new RectangularField();
		f.setSize(width, height);
		String[] words = new String[width + height];
		for (int i = 0; i < words.length; i++) words[i] = "";
		for (int p = 0; p < parts.length; p++) {
			int x = f.getX(p);
			int y = f.getY(p);
			words[y] += parts[p];
			words[f.getHeight() + x] += parts[p];
		}
		Arrays.sort(words);
		return words;
	}

}
