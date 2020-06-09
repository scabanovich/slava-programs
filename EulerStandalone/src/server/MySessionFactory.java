package server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class MySessionFactory {
	public static String SESSION_ID = "SESSION_ID";
	static long lastSessionId = 0;

	private Map<Long, MySession> sessions = new HashMap<>();
	
	protected MySession getSession(HttpExchange httpExchange) {
		long sessionId = 0;
		List<String> cookies = httpExchange.getRequestHeaders().get("Cookie");
		if (cookies != null) {
			for (String c: cookies) {
				String[] ck = c.split("=");
				if (ck.length == 2 && ck[0].equals(SESSION_ID)) {
					sessionId = Long.parseLong(ck[1]);
				}
			}
		}
		if (sessionId == 0) {
			lastSessionId++;
			sessionId = lastSessionId;
		}
		if (!sessions.containsKey(sessionId)) {
			sessions.put(sessionId, new MySession(sessionId));
		}
		System.out.println("SessionId=" + sessionId);
		return sessions.get(sessionId);
	}

}
