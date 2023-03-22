package register;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import managers.Tools;

/**
 * Servlet implementation class Signout
 */
@WebServlet("/cinimas/Signout")
public class Signout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String session=null;
		for(Cookie cookie : request.getCookies()) {
			if(cookie.getName().equals("sessionId")) {
				session=cookie.getValue();
				break;
			}
		}
		if(session!=null) {
			try {
				PreparedStatement psmt = Tools.DB.prepareStatement("delete from sessions where session=?;");
				psmt.setString(1, session);
				psmt.execute();
					JSONObject json = new JSONObject();
					for(Cookie cookie : request.getCookies()) {
						if(cookie.getName().equals("sessionId")) {
							cookie.setValue(null);
							break;
						}
					}
					json.put("statusCode","200");
					json.put("Message", "logged out");
					response.getWriter().append(json.toString());
				
			} catch (SQLException e) {
				JSONObject json = new JSONObject();
				json.put("statusCode","500");
				json.put("Message", e.getMessage());
				response.getWriter().append(json.toString());
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
