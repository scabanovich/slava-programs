package slava.puzzle.cardnet.analysis;

import slava.puzzle.cardnet.model.CardnetGeneratorInfo;
import slava.puzzle.cardnet.model.CardnetPuzzleInfo;

public class CardnetGenerator {
	CardnetPuzzleInfo puzzle;
	CardnetAnalyzer analyzer = new CardnetAnalyzer();
	CardnetAnalyzer analyzer2 = new CardnetAnalyzer();
	
	public CardnetGenerator() {
		analyzer.setSolutionLimit(1);
		analyzer.setRandomizing(true);
		analyzer.setWayCountLimit(4);
		analyzer.setConsideringLineInfo(false);
		
		analyzer2.setSolutionLimit(10);
		analyzer2.setRandomizing(false);
		analyzer2.setConsideringLineInfo(true);
	}
	
	public void setPuzzleInfo(CardnetPuzzleInfo puzzle) {
		this.puzzle = puzzle;
		analyzer.setPuzzleInfo(puzzle);
		analyzer2.setPuzzleInfo(puzzle);
	}

	public void setGeneratorInfo(CardnetGeneratorInfo generatorInfo) {
		analyzer.setRestrictions(generatorInfo.getNumbers(), generatorInfo.getSuits());
	}
	
	public void execute() {
		while(true) {
			puzzle.clearProblem();
			analyzer.solve();
			if(analyzer.getSolutionCount() == 0) {
				System.out.println("Cannot make a problem");
				return;
			} 
			int[] values = analyzer.getSolution();
			puzzle.makeProblemForValues(values);
			analyzer2.solve();
			System.out.println("-->" + analyzer2.getSolutionCount());
			if(analyzer2.getSolutionCount() == 1) break;
		}
	}

}
