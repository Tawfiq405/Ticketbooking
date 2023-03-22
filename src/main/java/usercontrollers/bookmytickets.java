package usercontrollers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.PseudoColumnUsage;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import managers.Tools;

/**
 * Servlet implementation class bookmytickets
 */
@WebServlet("/cinimas/bookmytickets")
public class bookmytickets extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public bookmytickets() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(""+request.getAttribute("Id"));
		JSONObject json = new JSONObject();
		if(id==1) {
			json.put("statusCode", 500);
		}else {
			json.put("statusCode", 200);
		}
		response.getWriter().append(json.toString());
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
			System.out.println(json);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		int id = Integer.parseInt(""+request.getAttribute("Id"));
		if(id==1) {
			try {
				PreparedStatement ps  = Tools.DB.prepareStatement("insert into user (name,email,password,mobile) values ('unknown',?,?,0000000000);");
				ps.setString(1, ""+json.get("email"));
				ps.setString(2, ""+json.get("pass"));
				ps.execute();
				ps = Tools.DB.prepareStatement("select Last_insert_id();");
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					id = rs.getInt(1);
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		Map<String,Integer> map = new HashMap<>();
		map.put("05:00:00", 1);
		map.put("08:15:00", 2);
		map.put("11:30:00", 3);
		map.put("14:45:00", 4);
		map.put("18:30:00", 5);
		map.put("22:00:00", 6);
		map.put("02:00:00", 7);
		
		JSONArray jsarr = (JSONArray)json.get("seats");
		System.out.println(json.toString());
		for(int i=0;i<jsarr.size();i++) {
			try {
				JSONObject js = (JSONObject) jsarr.get(i); 
				PreparedStatement psmt = Tools.DB.prepareStatement("update seat set status='Booked' where userId=? and thId=? and scId=? and showdate=? and stId =? and gpId=? and seId=? and rowno=? and columnno=?");
				psmt.setInt(1, Integer.parseInt(""+js.get("userId")));
				psmt.setInt(2, Integer.parseInt(""+js.get("thId")));
				psmt.setInt(3, Integer.parseInt(""+js.get("scId")));
				psmt.setDate(4, Date.valueOf(""+js.get("showdate")));
				psmt.setInt(5, map.get(""+js.get("showtime")));
				psmt.setInt(6, Integer.parseInt(""+js.get("groupid")));
				psmt.setInt(7, Integer.parseInt(""+js.get("seId")));
				psmt.setInt(8, Integer.parseInt(""+js.get("rowno")));
				psmt.setInt(9, Integer.parseInt(""+js.get("columnno")));
				psmt.execute();
//				psmt = 
//				psmt = Tools.DB.prepareStatement("insert into ticket values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//				psmt.setInt(1, Integer.parseInt(""+js.get("userId")));
//				psmt.setInt(2,Integer.parseInt(""+json.get("movieId")));
//				psmt.setInt(3,Integer.parseInt(""+json.get("lId")));				
//				psmt.setInt(4, Integer.parseInt(""+js.get("thId")));
//				psmt.setInt(5, Integer.parseInt(""+js.get("scId")));
//				psmt.setInt(6, Integer.parseInt(""+js.get("groupid")));
//				psmt.setInt(7, Integer.parseInt(""+js.get("seId")));
//				
//				psmt.setDate(8, Date.valueOf(""+js.get("showdate")));
//				psmt.setInt(9, map.get(""+js.get("showtime")));
//				
//				psmt.setInt(10, Integer.parseInt(""+js.get("rowno")));
//				psmt.setInt(11, Integer.parseInt(""+js.get("columnno")));
//				psmt.setString(12, "active");
//				psmt.setString(13, ""+json.get("zx"));
//				psmt.setInt(14, id);
//				psmt.setString(15, UUID.randomUUID().toString());
//				psmt.setString(16, "dfghj");
//				psmt.execute();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		JSONObject res = new JSONObject();
		res.put("statusCode", "200");
		response.getWriter().append(res.toString());
	}

}
