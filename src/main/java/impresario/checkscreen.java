package impresario;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import managers.Tools;

/**
 * Servlet implementation class checkscreen
 */
@WebServlet("/cinimas/impresario/checkscreen")
public class checkscreen extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public checkscreen() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String capacity = ""+request.getParameter("cap");
		Integer id= (Integer)request.getAttribute("Id");
		JSONObject js = new JSONObject();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select count(*) from (select thId,scId,count(*) from screenstructure where userId= ? group by thId,scId having count(*) < ? and count(*) > ? ) as screencount;");
			ps.setInt(1, id);
			ps.setInt(2, Integer.parseInt(capacity));
			ps.setInt(3, Integer.parseInt(capacity)-100);
			ResultSet rs = ps.executeQuery();
			JSONObject ks =  new JSONObject();
			if(rs.next()) {
				ks.put("count", rs.getInt(1));
			}
			js.put("statusCode", 200);
			js.put("count", ks.toString());
		}catch(Exception ex) {
			js.put("statusCode", 500);
			js.put("Message", ex.getMessage());
		}
		response.getWriter().append(js.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
