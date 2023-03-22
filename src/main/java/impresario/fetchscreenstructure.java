package impresario;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import managers.impresariodb;
import managers.Tools;

/**
 * Servlet implementation class fetchscreenstructure
 */
@WebServlet("/cinimas/impresario/fetchscreenstructure")
public class fetchscreenstructure extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fetchscreenstructure() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String thId = (String)request.getParameter("thId");
		String scId = (String)request.getParameter("scId");
		Integer id= (Integer)request.getAttribute("Id");
		String mvId = (String)request.getParameter("mvId");
		JSONArray js = impresariodb.myscreenstructure(id,Integer.parseInt(thId),Integer.parseInt(scId));
		JSONObject res = new JSONObject();
		res.put("statusCode", 200);
		res.put("jsonarr", js);
		if(mvId!=null) {
			try {
				PreparedStatement ps = Tools.DB.prepareStatement("select basepricepershow/capacity from distributormovies where movieId=?;");
				ps.setInt(1, Integer.parseInt(mvId));
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					res.put("per", rs.getDouble(1));
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println(res);
		response.getWriter().append(res.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
