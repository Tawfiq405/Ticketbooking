package register;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import org.json.simple.JSONObject;

import service.LoginManager;


/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = (String)request.getAttribute("email");
		String otp = ""+request.getAttribute("otp");
		Entry<Boolean,JSONObject> user = LoginManager.checkotp(email,otp);
		JSONObject resobj = new JSONObject();
		if(user.getKey()){
			JSONObject json = user.getValue();
			resobj.put("role",json.get("role"));
			resobj.put("name", json.get("name"));
			response.addCookie(new Cookie("sessionId",""+json.get("sessionId")));
			resobj.put("statusCode", 200);
		}else {
			resobj.put("statusCode", 500);
		}
		resobj.put("statusCode", 200);
		response.getWriter().append(resobj.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Login");
		
		String email = (String)request.getAttribute("email");
		ServletContext contextparams = request.getServletContext();
		String distributormail = contextparams.getInitParameter("distributor");
		Boolean val = false;
		JSONObject resobj = new JSONObject();
		if(!email.equals("null")){
			if(LoginManager.isUser(email)) {
				resobj.put("statusCode", 200);
			}else {
				resobj.put("statusCode", 401);
			}
		}else {
			LoginManager.checkotp("unknown@unknown.com", "0");
		}
//		resobj.put("statusCode", 200);
		response.getWriter().append(resobj.toString());
		
	}
	
	

}
