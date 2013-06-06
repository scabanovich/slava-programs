package diogen2005;

import java.io.File;
import java.io.FileWriter;

public class TickTackToeGenerator {
	TickTackToeSolver solver = new TickTackToeSolver();
	TickTackToeSolver solver2 = new TickTackToeSolver();
	int[] problem;
	int[] solution;
	int treeCount;
	
	int expectedResult = 19;

	
	public void setField(TickTackToeField field) {
		int crossCount =
			-1;
			//field.size / 2;
		solver.setField(field);
		solver.setCrossCount(crossCount);
		solver.setRandomizing(true);
		solver.setTreeCountLimit(20);
		solver.setSolutionLimit(1);
		solver2.setField(field);
		solver2.setCrossCount(crossCount);
		solver2.setSolutionLimit(2);
			solver2.setRandomizing(true);
	}

	public int[] generate() {
		problem = null;
		int q = 0;
		while(problem == null) {
			solver.solve();
			problem = solver.solution;
			++q;
		}
		solution = (int[])problem.clone();
		solver2.setInitialValues(problem);
		
//      leave only 0-values in problem
//		  for (int i = 0; i < problem.length; i++) {
//			  if(problem[i] == 1) problem[i] = -1;
//		  }

		reduce(20);
//		reduce(50);
//		reduce(500);
		if(expectedResult < getNumberCount() - 1) return problem;
		reduce(2000);
		return problem;
	}
	
	void reduce(int complexity) {
		solver2.setTreeCountLimit(complexity);
		int[] check = getRandomOrder();
		for (int i = 0; i < check.length; i++) {
			int p = check[i];
			int v = problem[p];
			if(v < 0) continue;
			problem[p] = -1;
			solver2.solve();
			if(solver2.solutionCount != 1) {
				problem[p] = v;
			} else {
				treeCount = solver2.treeCount;
			}
		}
	}
	
	int[] getRandomOrder() {
		int[] check = new int[problem.length];
		for (int i = 0; i < check.length; i++) check[i] = i;
		for (int i = check.length - 1; i >= 0; i--) {
			int j = (int) ((i + 1) * Math.random());
			if(i != j) {
				int k = check[i];
				check[i] = check[j];
				check[j] = k;
			}
		}
		return check;
	}
	
	public void generateMinimum() {
		int attemptCount = 0;
		int bestProblemCount = 0; 
		int netxToBestCount = 0;
		int best = 100;
		while(true) {
			attemptCount++;
			if(attemptCount % 100 == 0) {
				try { Thread.sleep(20); } catch (Exception e) {}
			}
			problem = generate();
			int c = getNumberCount();
			if(c == best) bestProblemCount++;
			else if(c == best + 1) netxToBestCount++;
			if(attemptCount % 1000 == 0) {
				System.out.println("Attempts=" + attemptCount + " Best Problems=" + bestProblemCount + "/" + netxToBestCount);
			}
			if(c >= best) continue;
			netxToBestCount = bestProblemCount;
			bestProblemCount = 1;
			best = c;
			printResults(attemptCount);
		}
	}

	public int[] generateUp(int k) {
		problem = null;
		int attemptCount = 0;
		int ms = 900;
		solver2.setSolutionLimit(ms);
		int[] bg = {0,1,8,9,10,17};
		while(problem == null) {
			++attemptCount;
			if(attemptCount % 10000 == 0) System.out.println("Attempts=" + attemptCount);
			int[] initialValues = new int[solver.field.size];
			for (int i = 0; i < initialValues.length; i++) initialValues[i] = -1;
			solver.setInitialValues(null);
			solver.init();
			int i = k;
			for (int j = 0; j < bg.length; j++) {
				int p = bg[j] + 18;
				solver.add(p, 0);
				initialValues[p] = 0;
				i--;
			}
			while(i > 0) {
				int p = (int)(initialValues.length * Math.random());
				if(initialValues[p] >= 0) continue;
				int v = Math.random() < 0.7 ? 0 : 1;
				if(!solver.canPut(p, v)) continue;
				solver.add(p, v);
				initialValues[p] = v;
				--i;
			}
			solver2.setInitialValues(initialValues);
			solver2.setMakeDistribution(true);
			solver2.solve();
			if(solver2.solutionCount == 1) {
				problem = (int[])initialValues.clone();
				solution = solver2.getSolution();
				solver2.printSolution(solution);
			} else if(solver2.solutionCount > 0 && solver2.solutionCount < ms) {
				int[] ns = solver2.getNarrowestPlaceInfo();
				if(solver2.solutionCount > 1000) {
					ms = solver2.solutionCount;
					solver2.setSolutionLimit(solver2.solutionCount);
				}
				System.out.println("sc=" + solver2.solutionCount + ":" + ns[2]);
				if(ns[2] == 1) {
					initialValues[ns[0]] = ns[1];
					problem = (int[])initialValues.clone();
					solver2.setInitialValues(initialValues);
					solver2.solve();
					solution = solver2.getSolution();
				}
			}
		}
		printResults(attemptCount);
		return problem;
	}
	
	void printResults(int attemptCount) {
		StringBuffer sb = new StringBuffer();
		sb.append("Problem " + getNumberCount() + " (Attempt=" + attemptCount + ")").append("\n");
		sb.append(solver.solutionToString(problem));
		sb.append("Solution " + " Complexity=" + treeCount).append("\n");
		sb.append(solver.solutionToString(solution));
		String out = sb.toString();
		System.out.print(out);
		printToFile(out);		
	}

	void printToFile(String out) {
		File f = new File("./ticktacktoe.txt");
		System.out.println(f.getAbsolutePath());
		try {
			if(!f.isFile()) f.createNewFile();
			FileWriter fw = new FileWriter(f);
			fw.write(out);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Saved to file " + f.getAbsolutePath());
	}

	int getNumberCount() {
		int c = 0;
		for (int i = 0; i < problem.length; i++) if(problem[i] >= 0) ++c;
		return c;
	}

}
