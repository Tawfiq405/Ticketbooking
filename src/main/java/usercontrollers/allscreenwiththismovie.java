package usercontrollers;

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
 * Servlet implementation class allscreenwiththismovie
 */
@WebServlet("/cinimas/allscreenwiththismovie")
public class allscreenwiththismovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public allscreenwiththismovie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer movieId = Integer.parseInt(""+request.getParameter("movieId"));
		Integer lId = Integer.parseInt(""+request.getParameter("lId"));
//		System.out.println(movieId+" "+lId);
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select myshow.userId,myshow.thId,myshow.scId,myshow.showdate,theatre.lat,theatre.lon,screen.name,theatre.address,theatre.city,theatre.landmark,GROUP_CONCAT(showtime.showtime SEPARATOR ',') as showtime,screen.rgbLaser,soundsystem.sound,screen.resolution from  myshow,showtime, theatre, screen,soundsystem where "
					+ "myshow.thId=theatre.thId and myshow.userId = theatre.userId and myshow.userId = screen.userId  and myshow.thId = screen.thId and myshow.scId = screen.scId and myshow.movieId=? and myshow.lId=? and myshow.stId=showtime.stId and myshow.showdate>= ? and screen.soundsystemId=soundsystem.soundsystemId group by myshow.userId,myshow.thId,myshow.scId,myshow.showdate,theatre.lat,theatre.lon,screen.name,theatre.address,theatre.city,theatre.landmark,screen.rgbLaser,soundsystem.sound,screen.resolution;");
			ps.setInt(1, movieId);
			// and showtime.showtime>=Time(now())
			ps.setInt(2, lId);
//			System.out.println(new Date(System.currentTimeMillis()));
			ps.setDate(3,new Date(System.currentTimeMillis()));
			ResultSet rs = ps.executeQuery();
			JSONArray jsarr = new JSONArray();
			while(rs.next()) {
				JSONObject json = new JSONObject();
				json.put("name", rs.getString(7)+" "+rs.getString(14)+" "+((rs.getString(12).equals("Y"))?"rgbLaser":"")+" "+rs.getString(13));
				String time = rs.getString(11);
				String [] timearr = time.split(",");
				JSONArray times = new JSONArray();
//				json.put("valtime", );
				SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
				SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aa");
				
				for(String tim : timearr) {
					JSONArray js = new JSONArray();
					js.add(tim);
					java.util.Date date = sdf1.parse(tim);
					js.add(sdf2.format(date));
					times.add(js);
				}
				
				json.put("address", rs.getString(8)+", "+rs.getString(9));
				json.put("landmark", rs.getString(10));
				json.put("userId", rs.getInt(1));
				json.put("thId", rs.getInt(2));
				json.put("scId", rs.getInt(3));
				json.put("showtime", times);
//				System.out.println(String.valueOf(rs.getDate(4)));
				json.put("showdate",String.valueOf(rs.getDate(4)));
//				System.out.println(json);
				jsarr.add(json);
			}
			JSONObject movie = new JSONObject();
			ps = Tools.DB.prepareStatement("select * from distributormovies where movieId = ? ");
			ps.setInt(1, movieId);
			rs = ps.executeQuery();
			if(rs.next()) {
					movie.put("name", rs.getString(2));
//					System.out.println(rs.getDate(4));
					movie.put("date", ""+rs.getDate(4));
					movie.put("totaltime", rs.getInt(5)+" hrs "+rs.getInt(6)+" min");
					movie.put("cast ", rs.getString(7)+" | "+rs.getString(8)+" | "+rs.getString(11));
					movie.put("director", rs.getString(9));
					movie.put("musicdirector", rs.getString(10));
					movie.put("sysnopsis", rs.getString(12));
					movie.put("bigsrc", Movie.pathToBase64(rs.getString(15)));
//					movie.put("smallsrc", Movie.pathToBase64(rs.getString(16)));
					movie.put("rating",rs.getString(16));
					movie.put("experience",rs.getString(17));
			}
			ps = Tools.DB.prepareStatement("select language from language where lId = ? ");
			ps.setInt(1, lId);
			rs = ps.executeQuery();
		if(rs.next()){
			movie.put("language", rs.getString(1));
		}
		ps = Tools.DB.prepareStatement("select genre from genrerelations,genre where movieId = ? and genrerelations.gId=genre.gId;");
		ps.setInt(1, movieId);
		rs = ps.executeQuery();
		JSONArray genres = new JSONArray();
		while(rs.next()) {
			genres.add(rs.getString(1));
		}
		movie.put("genres", genres);
//		System.out.println(movie);
		JSONObject res = new JSONObject();
		res.put("movie", movie);
		res.put("statusCode", "200");
		res.put("list", jsarr);
//		System.out.println(res);
		response.getWriter().append(res.toString());
		}catch(Exception ex) {
//			select * from myshow,screen,distributormovies,language where myshow.movieId=2 and myshow.lId=5 and myshow.userId=screen.userId and screen.thId=myshow.thId and screen.scId=myshow.scId and myshow.movieId=distributormovies.movieId and myshow.lId=language.lId;
		ex.printStackTrace();
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
