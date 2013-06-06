package match2005;

import java.io.*;

public class SudokuGenerator {
	SudokuSolver solver = new SudokuSolver();
	SudokuSolver solver2 = new SudokuSolver();
	int[] problem;
	int[] solution;
	int treeCount;

	int expectedResult;

	public void setField(SudokuField field) {
	  solver.setField(field);
	  solver.setRandomizing(true);
	  solver.setSolutionLimit(1);
	  solver2.setField(field);
	  solver2.setSolutionLimit(2);
	}

	public void setExpectedResult(int c) {
	  expectedResult = c;
	}

	public int[] generate() {
	  problem = null;
	  while(problem == null) {
	   solver.solve();
	   problem = solver.solution;
	  }
	  solution = (int[])problem.clone();
	  solver2.setInitialValues(problem);
	  reduce(10);
	  if(expectedResult < getNumberCount() - 1) return problem;
	  reduce(300);
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
	   problem = generate();
	   int c = getNumberCount();
	   if(c == best) bestProblemCount++;
	   else if(c == best + 1) netxToBestCount++;
	   if(attemptCount % 50 == 0) {
	   	try { Thread.sleep(10); } catch (Exception e) {}
	   }
	   if(attemptCount % 1000 == 0) {
		System.out.println("Attempts=" + attemptCount + " Best Problems=" +
	bestProblemCount + "/" + netxToBestCount);
	   }
	   if(c >= best) continue;
	   netxToBestCount = bestProblemCount;
	   bestProblemCount = 1;
	   best = c;
	   StringBuffer sb = new StringBuffer();
	   sb.append("Problem " + c + " (Attempt=" + attemptCount +
	")").append("\n");
	   sb.append(solver.solutionToString(problem));
	   sb.append("Solution " + " Complexity=" + treeCount).append("\n");
	   sb.append(solver.solutionToString(solution));
	   String out = sb.toString();
	   System.out.print(out);
	   printToFile(out);
	  }
	}

	void printToFile(String out) {
	  File f = new File("./sudoku.txt");
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
/*
Problem 14 (Attempt=410226)
 + + + + + + + + +
 + 5 + + + 3 + + +
 + + + 9 1 + 3 + +
 + + + + + 7 + + 3
 + 8 + + + + + + +
 + + + + + + + + +
 + 2 + + + + + 1 +
 + + + + + + + + 7
 6 + + 5 + 2 + + +
Problem 14 (Attempt=103542)
 2 5 + + + + + + +
 + + + + + + + + +
 + + + + + 6 + + +
 4 + + + + + + + +
 + + + + + + + + +
 + + + 2 + + + + +
 + + + 7 3 + 9 + +
 1 7 + 5 + + + + +
 + 9 6 + + + + + 8
Solution  Complexity=2
Problem 14 (Attempt=103542)
 2 5 + + + + + + +
 + + + + + + + + +
 + + + + + 6 + + +
 4 + + + + + + + +
 + + + + + + + + +
 + + + 2 + + + + +
 + + + 7 3 + 9 + +
 1 7 + 5 + + + + +
 + 9 6 + + + + + 8
Solution  Complexity=2
*/
