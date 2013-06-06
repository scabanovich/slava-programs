package com.slava.maze;

import java.util.Random;

import com.slava.common.RectangularField;

public class MazeGenerator {

	public static MazeProblem getRandomMazeProblem(int size, int wallSize) {
		MazeProblem p = new MazeProblem();
		Random seed = new Random();
		p.walls = new int[size];
		int s = 0;
		while(s < wallSize) {
			int q = seed.nextInt(size);
			if(p.walls[q] == 0) {
				p.walls[q] = 1;
				s++;
			}
		}
		p.start = seed.nextInt(size);
		while(p.walls[p.start] > 0) {
			p.start = seed.nextInt(size);
		}		
		return p;
	}

	public static void generateMazeProblem(int width, int height, int wallSize, int depth) {
		RectangularField f = new RectangularField();
		f.setSize(width, height);
		int best = 5;
		while(true) {
			MazeProblem p = getRandomMazeProblem(f.getSize(), wallSize);
			MazeSolver solver = new MazeSolver();
			solver.setField(f);
			solver.setProblem(p);
			solver.solve();
			int param = solver.getVisited();
			if(param > best) {
				best = param;
				System.out.println("Moves:" + best);
				solver.printSolution();
				if(best >= depth) {
					return;
				}
			}
		}
	}

}
