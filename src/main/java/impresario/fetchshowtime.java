package impresario;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import managers.Tools;

/**
 * Servlet implementation class fetchshowtime
 */
@WebServlet("/cinimas/impresario/fetchshowtime")
public class fetchshowtime extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fetchshowtime() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String thId = ""+request.getParameter("thId");
		String scId = ""+request.getParameter("scId");
		Integer Id= (Integer)request.getAttribute("Id");
		String date = ""+request.getParameter("date");
		JSONObject res = new JSONObject();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from showtime where stId not in (select stId from myshow where userId=? and thId=? and scId=? and showdate=?);");
			ps.setInt(1, Id);
			ps.setInt(2,Integer.parseInt(thId));
			ps.setInt(3,Integer.parseInt(scId));
			ps.setDate(4, Date.valueOf(date));
			ResultSet rs = ps.executeQuery();
			JSONArray arr = new JSONArray();
			while(rs.next()) {
				JSONObject js = new JSONObject();
				js.put("time", rs.getString(2));
				js.put("value", rs.getInt(1));
				arr.add(js);
			}
			res.put("statusCode", 200);
			res.put("jsonarr", arr);
		}catch(Exception ex) {
			res.put("statusCode", 500);
			res.put("Message", ex.getMessage());
		}
		response.getWriter().append(res.toString());
//		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
