package server;

import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class MySession {
	long id;
	
	Map<String, Boolean> solved = new HashMap<>();

	static Map<String, String> solutions = new HashMap<>();
	static {
		solutions.put("P31_CoinSums.html", "73682");
	}
	
	public MySession(long id) {
		this.id = id;
	}

	public void writeToCookies(HttpExchange httpExchange) {
		httpExchange.getResponseHeaders().add("Set-Cookie", MySessionFactory.SESSION_ID + "=" + id);
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
