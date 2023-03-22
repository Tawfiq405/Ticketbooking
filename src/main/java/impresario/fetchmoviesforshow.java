package impresario;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import models.Movie;
import managers.Tools;

/**
 * Servlet implementation class fetchmoviesforshow
 */
@WebServlet("/cinimas/impresario/fetchmoviesforshow")
public class fetchmoviesforshow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fetchmoviesforshow() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String Id = ""+request.getAttribute("Id");
		String count = ""+request.getParameter("count");
		String date =""+ request.getParameter("date");
		JSONObject res = new JSONObject();
		int c = Integer.parseInt(count);
		c= (c+100)-(c%100);
		try {
			PreparedStatement ps =  Tools.DB.prepareStatement("select * from impresariobalanceshows,distributormovies,language where userId =? and maxcapacity>=? and showdate=? and distributormovies.movieId=impresariobalanceshows.movieId and impresariobalanceshows.lId=language.lId;");
			ps.setInt(1, Integer.parseInt(Id));
			ps.setInt(2, c);
			ps.setDate(3, Date.valueOf(date));
			ResultSet rs = ps.executeQuery();
			JSONArray arr = new JSONArray();
			while(rs.next()) {
				JSONObject js = new JSONObject();
				js.put("Id", rs.getInt(1));
				js.put("name", rs.getString(8));
				js.put("movieId", rs.getInt(2));
				js.put("language", rs.getInt(3));
		        js.put("ratings", rs.getString(23));
		        js.put("date", rs.getString(5));
		        js.put("capacity", rs.getInt(4));
		        js.put("smallpath", Movie.pathToBase64(rs.getString(21)));
		        arr.add(js);
			}
			res.put("statusCode", 200);
			res.put("jsonarr", arr);
		}catch(Exception ex) {
			ex.printStackTrace();
			res.put("statusCode", 500);
			res.put("Message", ex.getMessage());
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
