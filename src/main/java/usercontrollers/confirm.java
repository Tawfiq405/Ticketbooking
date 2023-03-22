package usercontrollers;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import managers.Tools;
import service.DBmanager;
import user.Customer;

/**
 * Servlet implementation class confirm
 */
@WebServlet("/cinimas/confirm")
public class confirm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public confirm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append(""+((Integer.parseInt(""+request.getAttribute("Id")))!=2));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("confirm-----------------");
		String str = request.getParameter("email");
		int i= DBmanager.insert(new Customer("",str,"0000000000", null));
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
		
			try {
				PreparedStatement updatesession = Tools.DB.prepareStatement("insert into role values (?,?)");
				updatesession.setInt(1, i);
				updatesession.setString(2, "C");
				result = updatesession.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
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
