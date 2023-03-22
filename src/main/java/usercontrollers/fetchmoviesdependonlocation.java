package usercontrollers;

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

import models.Movie;
import managers.Tools;

/**
 * Servlet implementation class fetchmoviesdependonlocation
 */
@WebServlet("/cinimas/fetchmoviesdependonlocation")
public class fetchmoviesdependonlocation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fetchmoviesdependonlocation() {
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
		double lat = Double.parseDouble(""+request.getParameter("lat"));
		double lon = Double.parseDouble(""+request.getParameter("lon"));
		ResultSet rs = null;
		List<Integer> thIds = new ArrayList<>();
		JSONObject res = new JSONObject();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from theatre");
			rs = ps.executeQuery();
			while(rs.next()) {
				double dis = distance(lat,lon,rs.getDouble(7),rs.getDouble(8));
				System.out.println(dis);
				if(dis < 1005) {
					thIds.add(rs.getInt(2));
				}
			}
			System.out.println(thIds+"--------------------------------------------");
			String str = "select * from myshow where thId in ("+"?,".repeat(thIds.size()).substring(0,(thIds.size()*2)-1)+") and showdate>=?;";
			System.out.println(str);
			ps = Tools.DB.prepareStatement(str);
			for(int i=1;i<=thIds.size();i++) {
				ps.setInt(i, thIds.get(i-1));
			}
			ps.setDate(thIds.size()+1, new Date(System.currentTimeMillis()));
			rs = ps.executeQuery();
			List<Integer> movieId = new ArrayList<>();
			while(rs.next()) {
				movieId.add(rs.getInt(2));
				movieId.add(rs.getInt(3));
			}
//			System.out.println(movieId);
			str = " select name,distributormovies.movieId,language.lId,language.language,ratings,imgpath,bigpath from distributormovies,languagerelations,language,smallimg\n"
					+ "where distributormovies.movieId=languagerelations.movieId and languagerelations.lId=language.lId and languagerelations.movielId=smallimg.movielId and  (distributormovies.movieId,language.lId)  in ("+"(?,?),".repeat(movieId.size()).substring(0,(movieId.size()*3)-1)+");";
			System.out.println(str);
			ps = Tools.DB.prepareStatement(str);
			for(int i=1;i<=movieId.size();i++) {
				ps.setInt(i, movieId.get(i-1));
			}
			rs = ps.executeQuery();
			JSONArray bigimgsrcs = new JSONArray();
			JSONArray jsarr = new JSONArray();
			while(rs.next()) {
				JSONObject js = new JSONObject();
				js.put("movieId", rs.getInt(2));
				js.put("lId",rs.getInt(3));
				js.put("language", rs.getString(4));
				js.put("name", rs.getString(1));
				js.put("ratings", rs.getString(5));
				js.put("smallimg",Movie.pathToBase64(rs.getString(6)));
				bigimgsrcs.add(Movie.pathToBase64(rs.getString(7)));
				jsarr.add(js);
			}
			res.put("statusCode", "200");
			res.put("movie", jsarr);
			res.put("img", bigimgsrcs);
			response.getWriter().append(res.toString());
		}catch(Exception ex) {
			res.put("statusCode", "500");
			res.put("Message",ex.getMessage());
			response.getWriter().append(res.toString());
		}
		
		
	}
	
	public  double distance(double lat1, double lon1, double lat2, double lon2) {
  	  double earthRadius = 6371;

  	  double dLat = Math.toRadians(lat2 - lat1);
  	  double dLon = Math.toRadians(lon2 - lon1);
  	  lat1 = Math.toRadians(lat1);
  	  lat2 = Math.toRadians(lat2);

  	  double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
  	             Math.cos(lat1) * Math.cos(lat2) *
  	             Math.sin(dLon / 2) * Math.sin(dLon / 2);
  	  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  	  return earthRadius * c;
  	}

}
