package register;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.mysql.cj.protocol.Resultset;

import managers.Tools;

/**
 * Servlet implementation class FindName
 */
@WebServlet("/cinimas/FindName")
public class FindName extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindName() {
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		try {
			System.out.println("----------------");
			int id = Integer.parseInt(""+request.getAttribute("Id"));
			System.out.println(id);
			if(id!=2) {
				PreparedStatement ps = Tools.DB.prepareStatement("select * from user,role where id=? and id=userId");
				ps.setInt(1, id);
//				System.out.println(request.getParameter("Id"));
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					json.put("name", rs.getString(2));
					json.put("statusCode", 200);
					json.put("role", rs.getString(6));
					json.put("Id", id);
					response.getWriter().append(json.toString());
				}
			}else {
				json.put("name",0);
				json.put("statusCode", 500);
				response.getWriter().append(json.toString());
			}
			System.out.println(json);
		}catch(Exception ex) {
			ex.printStackTrace();
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
