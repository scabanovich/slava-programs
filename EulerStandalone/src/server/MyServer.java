package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.sun.net.httpserver.*;

public class MyServer {
	static MySessionFactory factory = new MySessionFactory();
	
	private static abstract class MyAbstractHttpHandler implements HttpHandler {
		
		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
			MySession session = factory.getSession(httpExchange);
			if("GET".equals(httpExchange.getRequestMethod())) {				 
				handleResponse(httpExchange, readParams(httpExchange), null, session);
			} else if("POST".equals(httpExchange.getRequestMethod())) {
				String s = readAll(httpExchange.getRequestBody());
				handleResponse(httpExchange, readParams(s), s, session);
			} else {
				System.out.println("Supports only GET method.");
			}
			
		}
		
		static String readAll(InputStream is) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String ln = null;
			while((ln = br.readLine()) != null) {
				sb.append(ln).append('\n');
			}
			return sb.toString();
		}

		protected void handleResponse(HttpExchange httpExchange, Map<String, String> requestParams, String requestBody, MySession session)  throws  IOException {
		}
		
		protected Map<String, String> readParams(HttpExchange httpExchange) {
			String s = httpExchange.getRequestURI().toString();
			int q = s.indexOf('?');
			if (q >= 0) {
				return readParams(s.substring(q + 1));
			}				
			return new HashMap<>();
		}
		
		protected Map<String, String> readParams(String params) {
			Map<String, String> result = new HashMap<>();
			for (String p: params.split("&")) {
				String[] kv = p.split("=");
				if (kv.length == 2) {
					result.put(kv[0], kv[1]);
				}
			}
			return result;
		}
	}

	private static class MyHttpHandler extends MyAbstractHttpHandler {

		@Override
		protected void handleResponse(HttpExchange httpExchange, Map<String, String> requestParams, String requestBody, MySession session)  throws  IOException {
			String uri = httpExchange.getRequestURI().toString().substring("/euler/".length());
			if (uri.startsWith("projecteuler.net")) {
				handleProjectEuler(httpExchange, requestParams, requestBody, session);
				return;
			}
			if (uri.length() == 0) {
				uri = "Euler.html";
			}
			if (uri.indexOf("?") >= 0) {
				uri = uri.substring(0, uri.indexOf("?"));
			}
			File f = new File("../euler/src/main/webapp/resources/problems/" + uri);
			
			System.out.println(uri + " " + f.isFile() + f.getAbsolutePath());
			OutputStream outputStream = httpExchange.getResponseBody();
			
			StringBuilder r = new StringBuilder();
			Files.readAllLines(f.toPath()).forEach((line) -> r.append(line).append("\n"));
			StringBuilder response = r;
			
			String ext = uri.substring(uri.lastIndexOf(".") + 1);
			String type = ext.contentEquals("js") ? "text/javascript"
					: ext.contentEquals("css") ? "text/css" : "text/html";
//			System.out.println("type=" + type);
			httpExchange.getResponseHeaders().add("Content-Type", type);

			if ("text/html".equals(type) && session.isSolvable(uri)) {
				if (requestParams.containsKey("answer")) {
					session.submit(uri, requestParams.get("answer"));
				}
				boolean solved = session.isSolved(uri);
				response = MyRenderer.cutTokens(response, "<!--SolvedStart", "SolvedEnd-->", solved);
				response = MyRenderer.cutTokens(response, "<!--UnsolvedStart", "UnsolvedEnd-->", !solved);
			}

			session.writeToCookies(httpExchange);
			httpExchange.sendResponseHeaders(200, response.length());
			outputStream.write(response.toString().getBytes());
//			System.out.println(response);
			outputStream.flush();
			outputStream.close();			
		}
		
		private void handleProjectEuler(HttpExchange httpExchange, Map<String, String> requestParams, String requestBody, MySession session) throws IOException {
			StringBuilder response = new StringBuilder();

			OutputStream outputStream = httpExchange.getResponseBody();

			String uri = httpExchange.getRequestURI().toString().substring("/euler/".length());
			URL url = new URL("https://" + uri);
			URLConnection conn = url.openConnection();
			Headers hs = httpExchange.getRequestHeaders();
			System.out.println(hs.keySet());
			for (String s : hs.keySet()) {
				System.out.println(s + "  =  " + hs.getFirst(s));
//				conn.setRequestProperty(s, hs.getFirst(s)); 
			}
			conn.setRequestProperty( "Cookie", hs.get("Cookie").stream().collect(Collectors.joining(";")));
			if ("POST".equals(httpExchange.getRequestMethod())) {
				conn.setDoOutput( true );
//				((HttpURLConnection)conn).setInstanceFollowRedirects( false );
				((HttpURLConnection)conn).setRequestMethod("POST");
				conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
				conn.setRequestProperty( "charset", "utf-8");
				System.out.println("-->" + hs.get("Cookie").stream().collect(Collectors.joining(";")));
				conn.setRequestProperty( "Cookie", hs.get("Cookie").stream().collect(Collectors.joining(";")));
				conn.setRequestProperty("Content-Length", "" + requestBody.trim().getBytes().length);
				System.out.println(requestBody);
				System.out.println(hs.get("Content-Type"));
				try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
					wr.write(requestBody.trim().getBytes()  );
				}
				
			}
			conn.connect();
			if (conn.getHeaderField("Set-Cookie") != null) {
				httpExchange.getResponseHeaders().add("Set-Cookie", conn.getHeaderField("Set-Cookie"));
			}
			InputStream is = conn.getInputStream();

			httpExchange.sendResponseHeaders(200, response.length());
			if (uri.endsWith(".png") || uri.contains("captcha")) {
				if (uri.endsWith(".png")) {
					httpExchange.getResponseHeaders().add("Content-Type", "image/png");
				}
				byte[] b = new byte[2048];
				while (true) {
					int l = is.read(b);
					if (l < 0) break;
					outputStream.write(b, 0, l);
				}
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line = null;
				while ((line = br.readLine()) != null) {
					response.append(line).append('\n');
				}
				br.close();
				outputStream.write(response.toString().getBytes());
			}
			
			//TODO
			outputStream.flush();
			outputStream.close();			
		}

	}

	private static class MyHttpHandler2 extends MyAbstractHttpHandler {

		@Override
		protected void handleResponse(HttpExchange httpExchange, Map<String, String> requestParams, String requestBody, MySession session)  throws  IOException {
			URI uri = httpExchange.getRequestURI();
			System.out.println(uri);
			OutputStream outputStream = httpExchange.getResponseBody();
			
			String response = "<html><body><h2>Hello Yury!</h2></body></html>";
			
			httpExchange.sendResponseHeaders(200, response.length());
			outputStream.write(response.getBytes());
			outputStream.flush();
			outputStream.close();			
		}			       

	}

	public static void main(String[] args) throws IOException {
		InetSocketAddress address = new InetSocketAddress("localhost", 21000);
		HttpServer server = HttpServer.create(address, 0);
		server.createContext("/euler/", new MyHttpHandler());
		server.createContext("/euler2/", new MyHttpHandler2());
		
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
		server.setExecutor(threadPoolExecutor);

		
		server.start();
		System.out.println("Server started");
	}

}
