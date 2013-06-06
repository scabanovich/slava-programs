package slava.puzzle.hitori.model;

public class HitoriPreferences {
	public static HitoriPreferences instance = new HitoriPreferences();
	
	int suggestionsLimit = 10;
	
	public HitoriPreferences() {}
	
	public int getSuggestionsLimit() {
		return suggestionsLimit;
	}
	
	public void setSuggestionsLimit(int s) {
		suggestionsLimit = s;
	}

}
