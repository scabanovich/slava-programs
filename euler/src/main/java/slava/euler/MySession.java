package slava.euler;

import java.util.HashMap;
import java.util.Map;

public class MySession {
	
	Map<String, Boolean> solved = new HashMap<>();

	static Map<String, String> solutions = new HashMap<>();
	static {
		solutions.put("problems/P31_CoinSums.html", "73682");
	}
	
	public MySession() {
	}

	public void submit(String path, String answer) {
		if (solutions.containsKey(path)) {
			solved.put(path, solutions.get(path).equals(answer));
		}
	}

	public boolean isSolvable(String path) {
		return solutions.containsKey(path);
	}
	

	public boolean isSolved(String path) {
		return isSolvable(path) && solved.containsKey(path) && solved.get(path);
	}

}
