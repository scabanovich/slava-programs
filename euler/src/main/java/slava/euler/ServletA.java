package slava.euler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletA
 */
@WebServlet("/problems/*")
public class ServletA extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ServletA() {
    }
    

    protected MySession getMySession(HttpServletRequest request) {
    	MySession mySession = (MySession)request.getSession().getAttribute("mySession");
    	if (mySession == null) {
    		mySession = new MySession();
    		request.getSession().setAttribute("mySession", mySession);
    	}
    	return mySession;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = getSubResourcePath(request);
		if (uri != null) {
			MySession mySession = getMySession(request);

			InputStream is = //getClass().getClassLoader().getResourceAsStream("/test/" + uri);
					request.getServletContext().getResourceAsStream("/resources/" + uri);
			StringBuilder text = readAll(is);

			String ext = uri.substring(uri.lastIndexOf(".") + 1);
			String type = ext.contentEquals("js") ? "text/javascript"
					: ext.contentEquals("css") ? "text/css" : "text/html";
//			System.out.println("type=" + type);

			if ("text/html".equals(type) && mySession.isSolvable(uri)) {
				boolean solved = mySession.isSolved(uri);
				text = MyRenderer.cutTokens(text, "<!--SolvedStart", "SolvedEnd-->", solved);
				text = MyRenderer.cutTokens(text, "<!--UnsolvedStart", "UnsolvedEnd-->", !solved);
			}

			response.getWriter().append(text);
		} else {
			response.getWriter().append("Served at: ").append(request.getContextPath() + " " + request.getRequestURI());
		}
	}
	
	String getSubResourcePath(HttpServletRequest request) {
		String uri = request.getRequestURI();
		int i = uri.indexOf("/euler/");
		if (i >= 0) {
			return uri.substring(i + 7);
		}
		return null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MySession mySession = getMySession(request);
		String answer = (String)request.getParameter("answer");
		if (answer != null) {
			String uri = getSubResourcePath(request);
			mySession.submit(uri, answer);
		}
		doGet(request, response);
	}

	static StringBuilder readAll(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String ln = null;
		while((ln = br.readLine()) != null) {
			sb.append(ln).append('\n');
		}
		return sb;
	}

}
