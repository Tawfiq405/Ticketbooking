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
 * Servlet implementation class fetchseatcount
 */
@WebServlet("/cinimas/impresario/fetchseatcount")
public class fetchseatcount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fetchseatcount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String Id = ""+request.getAttribute("Id");
		String thId =""+ request.getParameter("thId");
		String scId = ""+request.getParameter("scId");
		JSONObject res = new JSONObject();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select count(*) from screenstructure where userId =? and thId=? and scId =? and isseat='T' ;");
			ps.setInt(1, Integer.parseInt(Id));
			ps.setInt(2, Integer.parseInt(thId));
			ps.setInt(3, Integer.parseInt(scId));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				res.put("count", rs.getInt(1));
				res.put("statusCode", 200);
			}
		}catch(Exception ex) {
			res.put("Message", ex.getMessage());
			res.put("statusCode", 500);
		}
		response.getWriter().append(res.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
