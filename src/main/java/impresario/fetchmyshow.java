package impresario;

import java.io.IOException;
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

import managers.Tools;

/**
 * Servlet implementation class fetchmyshow
 */
@WebServlet("/cinimas/impresario/fetchmyshow")
public class fetchmyshow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fetchmyshow() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String Id = ""+request.getAttribute("Id");
		JSONObject res = new JSONObject();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("		 select distributormovies.name,showdate,showtime.showtime from myshow,distributormovies,showtime where myshow.stId=showtime.stId and distributormovies.movieId=myshow.movieId and myshow.userId=? and showdate>=date(now());");
			ps.setInt(1, Integer.parseInt(Id));
			ResultSet rs = ps.executeQuery();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm aa");
			JSONArray jsarr = new JSONArray();
			while(rs.next()) {
				JSONObject json = new JSONObject();
				json.put("name", rs.getString(1));
				json.put("showdate", sdf2.format(sdf1.parse(String.valueOf(rs.getDate(2)))));
				json.put("time", sdf4.format(sdf3.parse(String.valueOf(rs.getString(3)))));
				jsarr.add(json);
			}
			res.put("shows", jsarr);
			res.put("statusCode", "200");
			System.out.println(res);
		}catch(Exception ex) {
			ex.printStackTrace();
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
