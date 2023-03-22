package impresario;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import models.Impresariomovie;
import service.AddMovieToDb;
import managers.Tools;
import user.Impresario;

/**
 * Servlet implementation class addmovieforimpresario
 */
@WebServlet("/cinimas/impresario/addmovieforimpresario")
public class addmovieforimpresario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addmovieforimpresario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String inp="";
		String jsoninput ="";
		BufferedReader reader = request.getReader();
		JSONObject resobj = new JSONObject();
		while((inp = reader.readLine())!=null) {
			jsoninput+=inp;
		}
		JSONParser parser = new JSONParser();
		JSONArray jsons = null;
		Impresario owner = null;
		int uid = (Integer)request.getAttribute("Id");
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from user where id=?");
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				owner = new Impresario(rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(1),0);
			}
		}catch(Exception ex) {
				ex.printStackTrace();
			}
		try {
			jsons = (JSONArray) parser.parse(jsoninput);
			System.out.println(jsons);
			List<Impresariomovie> movies = new ArrayList<>();
			for(Object json : jsons) {
				JSONObject js = (JSONObject)json;
				long milli = (Date.valueOf(""+js.get("endday")).getTime() - Date.valueOf(""+js.get("startday")).getTime());
				long days = milli /(1000 * 60 * 60 * 24);
				
				System.out.println(Double.parseDouble(""+js.get("per1")));
				System.out.println(Integer.parseInt(""+js.get("capacity")));
				System.out.println(Integer.parseInt((""+js.get("showperday"))));
				System.out.println(Integer.parseInt((""+js.get("screencount"))));
				System.out.println(days);
				double amount  = Double.parseDouble(""+js.get("per1")) *   Integer.parseInt(""+js.get("capacity")) * Integer.parseInt((""+js.get("showperday"))) * Integer.parseInt((""+js.get("screencount"))) * days;
				System.out.println("amount "+amount);
				Impresariomovie movie = new Impresariomovie(
						owner,
						Integer.parseInt(""+js.get("movieId")),
						Integer.parseInt(""+js.get("lId")),
						Integer.parseInt(""+js.get("capacity")),
						Date.valueOf((String)js.get("startday")),
						Date.valueOf((String)js.get("endday")),
						Integer.parseInt(""+js.get("screencount")),
						amount,
						Integer.parseInt(""+js.get("showperday"))
						);
				movies.add(movie);
			}
			int res = AddMovieToDb.addmovieforimpresario(movies);
			if(res==200) {
				resobj.put("statusCode", "200");
			}else {
				resobj.put("Message", "erroroccored");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		response.getWriter().append(resobj.toString());
	}

}
