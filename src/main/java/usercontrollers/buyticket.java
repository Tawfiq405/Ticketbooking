package usercontrollers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import enums.SeatStatus;
import managers.Tools;
import models.Seat;

/**
 * Servlet implementation class buyticket
 */
@WebServlet("/cinimas/buyticket")
public class buyticket extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public buyticket() {
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
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(jsoninput);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		Map<String,Integer> time= new HashMap<>();
		 time.put("05:00:00", 1);
		 time.put("08:15:00", 2);
		 time.put("11:30:00", 3);
		 time.put("14:45:00", 4);
		 time.put("18:30:00", 5);
		 time.put("22:00:00", 6);
		 time.put("02:00:00", 7);
		JSONObject details = (JSONObject) json.get("details");
		JSONArray tickets = (JSONArray) json.get("tickets");
		List<Seat> seats = new ArrayList<>();
		JSONObject movie = (JSONObject) details.get("movie");
		Integer movieId = Integer.parseInt(""+movie.get("movieId"));
		Integer lId = Integer.parseInt(""+movie.get("lId"));
		int userId = Integer.parseInt(details.get("userId")+"");
		 int thId =Integer.parseInt(details.get("thId")+"");
		int scId = Integer.parseInt(details.get("scId")+"");

Date date = Date.valueOf(details.get("showdate")+"");
 int stId = time.get(details.get("showtime")+"");
 
SeatStatus status = SeatStatus.Booked;
String name = ""+details.get("name");
		tickets.forEach(k->{
			String[] seat = (""+k).split("-");
			int gpId = Integer.parseInt(seat[2]);
			 int seId = Integer.parseInt(seat[3]);
			 int rowno = Integer.parseInt(seat[5]);
			 int columnno = Integer.parseInt(seat[4]);
			Seat s = new Seat(userId, movieId, lId, thId, scId, gpId, seId,date,stId , rowno, columnno, status, name);
			
			seats.add(s);
		});
		System.out.println(seats);
		bookseat( seats);
		
		response.getWriter().append("success");
	}
	
	public  void bookseat(List<Seat> seats) {
		for(Seat s : seats) {
			try {
				PreparedStatement ps  =  Tools.DB.prepareStatement("update seat set status = 'booked' where userId = ? and movieId = ? and lId = ? and thId = ? and scId = ? and gpId = ? and seId = ? and showdate = ? and stId = ? and rowno =? and columnno = ? ;");
				ps.setInt(1, s.getUserId());
				ps.setInt(2, s.getMovieId());
				ps.setInt(3, s.getlId());
				ps.setInt(4, s.getThId());
				ps.setInt(5, s.getScId());
				ps.setInt(6, s.getGpId());
				ps.setInt(7, s.getSeId());
				ps.setDate(8, s.getDate());
				ps.setInt(9, s.getStId());
				ps.setInt(10, s.getRowno());
				ps.setInt(11, s.getColumnno());
				System.out.println(ps.executeUpdate());
				
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
