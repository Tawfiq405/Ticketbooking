package usercontrollers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import managers.Tools;

/**
 * Servlet implementation class fetchseatsforshow
 */
@WebServlet("/cinimas/fetchseatsforshow")
public class fetchseatsforshow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fetchseatsforshow() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		
		
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select seat.gpId,seat.seId,seat.rowno,seat.columnno,seat.status,seat.name,ticketrate.rate,seatingtype.type from seat,ticketrate,seatingtype,showtime where seat.stId=showtime.stId and seat.userId=? and seat.thId=? and seat.scId=? and seat.showdate = ? and showtime.showtime=? and seat.userId=ticketrate.userId and seat.thId=ticketrate.thId and seat.scId = ticketrate.scId and seat.showdate=ticketrate.showdate and seat.stId=ticketrate.stId and seat.gpId=ticketrate.gpId and seat.seId=ticketrate.seId and seatingtype.seId=seat.seId;");
			ps.setInt(1, Integer.parseInt(""+json.get("userId")));
			ps.setInt(2, Integer.parseInt(""+json.get("thId")));
			ps.setInt(3, Integer.parseInt(""+json.get("scId")));
			ps.setDate(4, Date.valueOf(""+json.get("showdate")));
			ps.setString(5, ""+json.get("showtime"));
			ResultSet rs = ps.executeQuery();
			System.out.println("--------------");
			JSONObject seats = new JSONObject();
			while(rs.next()) {
				if(seats.get(rs.getInt(1))!=null) {
					JSONArray a = new JSONArray();
					a.add(rs.getInt(2));
					a.add(rs.getDouble(7));
					a.add(rs.getString(8));
					JSONObject json1 = (JSONObject) seats.get(rs.getInt(1));
					if(json1.get(a)!=null) {
						JSONArray jsarr = (JSONArray) json1.get(a);
						JSONObject json2 = new JSONObject();
						json2.put("rowno", rs.getInt(3));
						json2.put("columnno", rs.getInt(4));
						json2.put("groupid", rs.getInt(1));
						json2.put("status", rs.getString(5));
						json2.put("zx", rs.getString(6));
						json2.put("seId", rs.getInt(2));
						jsarr.add(json2);
						json1.put(a, jsarr);
						seats.put(rs.getInt(1), json1);
					}else {
						JSONObject json11 = (JSONObject) seats.get(rs.getInt(1));
						JSONArray jsarr = new JSONArray();
						JSONObject json2 = new JSONObject();
						json2.put("rowno", rs.getInt(3));
						json2.put("columnno", rs.getInt(4));
						json2.put("status", rs.getString(5));
						json2.put("zx", rs.getString(6));
						json2.put("groupid", rs.getInt(1));
						json2.put("seId", rs.getInt(2));
						jsarr.add(json2);
						json11.put(a, jsarr);
						seats.put(rs.getInt(1), json11);
					}
					
				}else {
					JSONObject json1 = new JSONObject();
					JSONArray a = new JSONArray();
					a.add(rs.getInt(2));
					a.add(rs.getDouble(7));
					a.add(rs.getString(8));
					JSONArray jsarr = new JSONArray();
					JSONObject json2 = new JSONObject();
					json2.put("rowno", rs.getInt(3));
					json2.put("columnno", rs.getInt(4));
					json2.put("status", rs.getString(5));
					json2.put("zx", rs.getString(6));
					json2.put("groupid", rs.getInt(1));
					json2.put("seId", rs.getInt(2));
					jsarr.add(json2);
					json1.put(a, jsarr);
					seats.put(rs.getInt(1), json1);
					
				}
			}
			List<Map.Entry<Integer, JSONObject>> seatss = new ArrayList<>(seats.entrySet());
			List<JSONObject> m = new ArrayList<>();
			for(int i=0;i<seatss.size();i++) {
				m.add((JSONObject)seatss.get(i).getValue());
			}
			Collections.sort(m,(JSONObject o1, JSONObject o2) ->  ((JSONArray)(((Map.Entry<JSONArray, JSONArray>)(new ArrayList<>(o1.entrySet()).get(0))).getValue())).size() - ((JSONArray)(((Map.Entry<JSONArray, JSONArray>)(new ArrayList<>(o2.entrySet()).get(0))).getValue())).size());
			JSONArray finalarr = new JSONArray();
			for(JSONObject d : m) {
				List<Map.Entry<JSONArray, JSONArray>> x = new ArrayList<>(d.entrySet());
				List<JSONArray> y = new ArrayList<>();
				for(Map.Entry<JSONArray, JSONArray> f : x) {
					JSONArray n = new JSONArray();
					n.add(f.getKey());
					n.add(f.getValue());
					y.add(n);
				}
				Collections.sort(y,(JSONArray o1, JSONArray o2) -> (Integer)((JSONArray)o1.get(0)).get(0)- (Integer)((JSONArray)o2.get(0)).get(0));
				for(JSONArray g : y) {
					finalarr.add(g);
				}
			}
			JSONObject res = new JSONObject();
			res.put("statusCode", "200");
			res.put("arr", finalarr);
			response.getWriter().append(res.toString());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
