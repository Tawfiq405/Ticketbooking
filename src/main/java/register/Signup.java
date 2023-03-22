package register;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import managers.Tools;
import service.DBmanager;
import user.Customer;

/**
 * Servlet implementation class Signup
 */
@WebServlet("/Signup")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signup() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsoninput =(String) request.getAttribute("json");
		JSONObject resobj = new JSONObject();
		
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(jsoninput);
			System.out.println(json.toString());
		}catch(ParseException pe) {
			resobj.put("statusCode","500");
			resobj.put("Message","error occured plz give valid input !!");
			response.getWriter().append(resobj.toString());			
		}
		int i= DBmanager.insert(new Customer((String)json.get("name"),(String)json.get("email"),(String)json.get("number"), null));
		System.out.println(i);
		int result=0;
		try {
			PreparedStatement updatesession = Tools.DB.prepareStatement("insert into sessions values (?,?)");
			String sessionId = getsessionId();
			updatesession.setString(2, sessionId);
			updatesession.setInt(1, i);
			result = updatesession.executeUpdate();
			response.addCookie(new Cookie("sessionId",sessionId));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result>0) {
			resobj.put("statusCode","200");
			resobj.put("Message","successfully logged in !");
		}
		System.out.println();
		if((boolean) json.get("Imp")) {
			try {
				PreparedStatement updatesession = Tools.DB.prepareStatement("insert into request values (?,?,?)");
				updatesession.setInt(1, i);
				updatesession.setString(2, "unseen");
				updatesession.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				result = updatesession.executeUpdate();
				json.put("role", "O");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				PreparedStatement updatesession = Tools.DB.prepareStatement("insert into role values (?,?)");
				updatesession.setInt(1, i);
				updatesession.setString(2, ((boolean) json.get("Imp"))?"O":"C");
				result = updatesession.executeUpdate();
				if((boolean) json.get("Imp")) {
					updatesession = Tools.DB.prepareStatement("insert into accountbalance values (?,?) where  userId = ? ");
					updatesession.setDouble(1, 0.0);
					updatesession.setInt(2, i);
					updatesession.execute();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		response.getWriter().append(json.toString());
	}
	
	
	
	public  String getsessionId() {
		UUID uuid = UUID.randomUUID();
		ResultSet rs2=null;
		PreparedStatement impobj;
		try {
			impobj = Tools.DB.prepareStatement("select * from sessions where session = ?;");
			impobj.setString(1, uuid.toString());
			rs2 = impobj.executeQuery();
			if(rs2.next()) {
				return getsessionId();
			}else {
				return uuid.toString();
			}
		} catch (SQLException e) {
			return getsessionId();
		}	    
	}
}
