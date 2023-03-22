package impresario;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import managers.Tools;

/**
 * Servlet implementation class fetchamenities
 */
@WebServlet("/cinimas/impresario/fetchamenities")
public class fetchamenities extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fetchamenities() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject amenities = new JSONObject();
		JSONObject myresponse = new JSONObject();
		try {
			PreparedStatement psmt = Tools.DB.prepareStatement("select * from amenities");
			ResultSet rs = psmt.executeQuery();
			while(rs.next()) {
				amenities.put(rs.getInt(2), rs.getString(1));
			}
			myresponse.put("statusCode", 200);
			myresponse.put("amenities",amenities.toString());
			response.getWriter().append(myresponse.toString());
		} catch (SQLException e) {
			myresponse.put("statusCode", 500);
			myresponse.put("Message",e.getMessage());
			response.getWriter().append(myresponse.toString());
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
