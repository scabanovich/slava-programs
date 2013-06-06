package slava.puzzles.seabattle.model;

public class SeaBattlePreferences implements SeaBattleConstants {
	
	int solutionLimit = 10;
	int generatorMode = 0;
	
	public SeaBattlePreferences() {}
	
	public int getSolutionLimit() {
		return solutionLimit;
	}
	
	public void setSolutionLimit(int s) {
		solutionLimit = s;
	}
	
	public int getGeneratorMode() {
		return generatorMode;
	}
	
	public void setGeneratorMode(int s) {
		generatorMode = s;
	}

}
