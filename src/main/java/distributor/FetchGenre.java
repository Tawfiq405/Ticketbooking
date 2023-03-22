package distributor;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import managers.Tools;



/**
 * Servlet implementation class FetchGenre
 */
@WebServlet("/cinimas/distributor/FetchGenre")
public class FetchGenre extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchGenre() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("fetch genre");
		JSONArray genres = new JSONArray();
		JSONObject myresponse = new JSONObject();
		try {
			PreparedStatement psmt = Tools.DB.prepareStatement("select * from genre");
			ResultSet rs = psmt.executeQuery();
			while(rs.next()) {
				JSONObject json = new JSONObject();
				json.put("name", rs.getString(2));
				json.put("id", rs.getInt(1));
				genres.add(json);
			}
			myresponse.put("statusCode", 200);
			myresponse.put("genre",genres);
			System.out.println(genres);
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
