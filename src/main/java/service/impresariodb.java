package service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import models.Screen;
import models.Seat;
import models.Show;
import models.Theatre;
import enums.SeatStatus;
import managers.Tools;

public class impresariodb {
	@SuppressWarnings("unchecked")
	public static synchronized JSONObject addtheatre(Theatre theatre) {
		JSONObject res = new JSONObject();
		try {
			PreparedStatement ps=  Tools.DB.prepareStatement("select count(*)+1 from theatre where userId=?");
			int uid = theatre.getImpresario().getUserId();
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int thid = rs.getInt(1);
				ps = Tools.DB.prepareStatement("insert into theatre values (?,?,?,?,?,?,?,?,5.0,?,?)");
				ps.setString(1, theatre.getTheatreName());
				ps.setInt(2,thid);
				ps.setString(3, theatre.getAddress());
				ps.setString(4, theatre.getCity());
				ps.setString(5, theatre.getState());
				ps.setString(6, theatre.getCountry());
				ps.setDouble(7, theatre.getLat());
				ps.setDouble(8, theatre.getLon());
				ps.setInt(9, uid);
				ps.setString(10, theatre.getLandmark());
				ps.execute();
				
				System.out.println(theatre.getAmenities());
				for(Integer aid : theatre.getAmenities()) {
					ps = Tools.DB.prepareStatement("insert into amenitiesrelations values (?,?,?)");
					ps.setInt(1, aid);
					ps.setInt(2, uid);
					ps.setInt(3, thid);
					ps.execute();
				}
				res.put("statusCode", "200");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			res.put("statusCode", "500");
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray mytheatres(int Id) {
		JSONArray theatres = new JSONArray();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from theatre where userId = ?");
			ps.setInt(1, Id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				JSONObject theatre = new JSONObject();
				theatre.put("name",rs.getString(1));
				theatre.put("thId", rs.getInt(2));
				theatre.put("address", rs.getString(3));
				theatre.put("city", rs.getString(4));
				theatre.put("state", rs.getString(5));
				theatre.put("country", rs.getString(6));
				theatre.put("lat", rs.getDouble(7));
				theatre.put("lon", rs.getDouble(8));
				theatre.put("rating", rs.getDouble(9));
				theatre.put("landmark", rs.getString(11));
				theatres.add(theatre);
			}
			
				return theatres;
		}catch(Exception ex) {
			return new JSONArray();
		}
		
	}
	
	@SuppressWarnings("unchecked")
//	public static JSONObject insertscreen(Screen screen) {
//		JSONObject res = new JSONObject();
//		int num=0;
//		int uid = screen.getTheatre().getImpresario().getUserId();
//		int thid = screen.getTheatre().getTheatreId();
//		int sid = screen.getScId();
//		try {
//			PreparedStatement ps = Tools.DB.prepareStatement("insert into screen values (?,?,?,?,?,?,?)");
//			ps.setInt(1, uid);
//			ps.setInt(2,thid);
//			ps.setInt(3, sid);
//			ps.setString(4, screen.getName());
//			ps.setString(5, (screen.isRGB())?"Y":"N");
//			ps.setInt(6,screen.getSound());
//			ps.setString(7, screen.getResolution());
//			ps.execute();
//			for(Map.Entry<List<Integer>, List<List<Integer>>> s : screen.getScreenseating().entrySet()) {
//				int gp = s.getKey().get(0);
//				int seid = s.getKey().get(1);
//				for(List<Integer> d : s.getValue()) {
//					ps = Tools.DB.prepareStatement("insert into screenseatingrelation values (?,?,?,?,?,?,?)");
//					ps.setInt(1, uid);
//					ps.setInt(2,thid);
//					ps.setInt(3, sid);
//					ps.setInt(4, gp);
//					ps.setInt(5,seid);
//					
//					int rowno =d.get(0);
//					int columncount = d.get(1);
//					ps.setInt(6, rowno);
//					ps.setInt(7, columncount);
//					ps.execute();
//					String name = seatname(rowno);
//					for(int i=1;i<=columncount;i++) {
//						ps = Tools.DB.prepareStatement("insert into screenstructure values (?,?,?,?,?,?,?,?,'Active')");
//						ps.setInt(1, uid);
//						ps.setInt(2,thid);
//						ps.setInt(3, sid);
//						ps.setInt(4, gp);
//						ps.setInt(5,seid);
//						ps.setInt(6, rowno);
//						ps.setInt(7, i);
//						ps.setString(8, name+i);
//						ps.execute();
//					}
//					
//				}
//			}
//			res.put("statusCode", "200");
//		}catch(Exception ex) {
//			res.put("statusCode", "500");
//			res.put("Message", ex.getMessage());
//		}
//		return res;
//	}
	
	private static String seatname(int num) {
		int num1 = (num%26==0)?26:num%26;
		int num2 = (num<=26)?-32:(num%26==0)?(num/26)-1:num/26;
		String x = (num2==-32)?"":""+(char)(num2+64);
		return x+(char)(num1+64)+"-".trim();
	}

	@SuppressWarnings("unchecked")
	public static JSONArray myscreen(int count, Integer id, Integer thid) {
		JSONArray screens = new JSONArray();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from screen,soundsystem where userId = ? and thId=? and screen.soundsystemId=soundsystem.soundsystemId");
			ps.setInt(1, id);
			ps.setInt(2, thid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				JSONObject screen = new JSONObject();
				screen.put("name",rs.getString(4));
				screen.put("scId", rs.getInt(3));
				screen.put("sound", rs.getString(8));
				screen.put("resolutions", rs.getString(7));
				screens.add(screen);
			}
			if(screens.size()>count) {
				return screens;
			}else {
				return new JSONArray();
			}
		}catch(Exception ex) {
			return new JSONArray();
		}
	}

	@SuppressWarnings("unchecked")
	public static JSONArray myscreenstructure(Integer id, int thId, int scId) {
//		List<JSONObject> seattype = new ArrayList<>();
		Map<Integer,List<JSONObject>> map = new HashMap<>();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from screenseatingrelation,seatingtype where userId=? and thId=? and scId=? and screenseatingrelation.seId = seatingtype.seId;");
			ps.setInt(1, id);
			ps.setInt(2,thId);
			ps.setInt(3, scId);
			ResultSet rs= ps.executeQuery();
			while(rs.next()) {
				if(map.containsKey(rs.getInt(4))) {
					List<JSONObject> seat = map.get(rs.getInt(4));
					List<JSONObject> seat1 = new ArrayList<>();
					boolean v = true;
					for(JSONObject s : seat) {
						if(s.get(rs.getInt(5))!=null) {
							v=false;
							JSONObject json1 = (JSONObject) s.get(rs.getInt(5));
							JSONObject json2 = new JSONObject();
							json1.put(rs.getInt(6), rs.getInt(7));
							json2.put(rs.getInt(5), json1);
							json2.put(Integer.MAX_VALUE, rs.getString(9));
							json2.put(Integer.MAX_VALUE-1, rs.getInt(4));
							json2.put(Integer.MAX_VALUE-2, rs.getInt(5));
							seat1.add(json2);
						}else {
							seat1.add(s);
						}
					}
					if(v) {
						JSONObject json1 = new JSONObject();
						JSONObject json2 = new JSONObject();
						json1.put(rs.getInt(6), rs.getInt(7));
						json2.put(rs.getInt(5), json1);
						json2.put(Integer.MAX_VALUE, rs.getString(9));
						seat1.add(json2);
						map.put(rs.getInt(4), seat1);
					}else {
						map.put(rs.getInt(4), seat1);
					}
				}else {
					List<JSONObject> seat = new ArrayList<>();
					JSONObject json1 = new JSONObject();
					JSONObject json2 = new JSONObject();
					json1.put(rs.getInt(6), rs.getInt(7));
					json2.put(rs.getInt(5), json1);
					json2.put(Integer.MAX_VALUE, rs.getString(9));
					seat.add(json2);
					map.put(rs.getInt(4), seat);
				}
			}
			List<Entry<Integer, List<JSONObject>>> listofmap = new ArrayList<>(map.entrySet());
			Collections.sort(listofmap, (Entry<Integer, List<JSONObject>> o1, Entry<Integer, List<JSONObject>> o2) ->  o1.getValue().size() - o2.getValue().size());
			JSONArray finallist = new JSONArray();
			for(Entry<Integer, List<JSONObject>> s : listofmap) {
				List<JSONObject> m = s.getValue();
				Collections.sort(m,new Comparator<JSONObject>() {

					@Override
					public int compare(JSONObject o1, JSONObject o2) {
						List<Integer> a = new ArrayList<>(o1.keySet());
						List<Integer> b = new ArrayList<>(o1.keySet());
//						b.c
//						System.out.println(Integer.parseInt(a.get(0)) - Integer.parseInt(b.get(0)));
						return a.get(0) - b.get(0);
					}
					
				});
	            for(JSONObject v : m) {
	            	finallist.add(v);
	            }
	            
	            
			}
			System.out.println(finallist.toString());
			return finallist;
		}catch(Exception ex) {
			ex.printStackTrace();
			return new JSONArray();
		}
		
	}

//	public static void addshowtodb(Show show) {
//		try {
//			PreparedStatement ps = Tools.DB.prepareStatement("insert into myshow values(?,?,?,?,?,?,?)");
//			ps.setInt(1, show.getUserId());
//			ps.setInt(2, show.getMovieId());
//			ps.setInt(3, show.getlId());
//			System.out.println(show.getDate());
//			System.out.println(Date.valueOf(show.getDate()));
//			ps.setDate(4, Date.valueOf(show.getDate()));
//			ps.setInt(5, show.getStId());
//			ps.setInt(6, show.getThId());
//			ps.setInt(7, show.getScId());
//			ps.execute();
//			ps = Tools.DB.prepareStatement("update impresariobalanceshows set showbalance=showbalance-1 where userId=? and movieId=? and lId=? and maxcapacity=? and showdate=?;");
//			ps.setInt(1, show.getUserId());
//			ps.setInt(2, show.getMovieId());
//			ps.setInt(3, show.getlId());
//			ps.setInt(4, show.getCapacity());
//			ps.setDate(5, Date.valueOf(show.getDate()));
//			JSONArray jsarr = show.getJsonarr();
//			for(int i=0;i<jsarr.size();i++) {
//				JSONArray js = (JSONArray) jsarr.get(i);
//				ps = Tools.DB.prepareStatement("insert into ticketrate values (?,?,?,?,?,?,?,?,?,?)");
//				ps.setInt(1, show.getUserId());
//				ps.setInt(2, show.getMovieId());
//				ps.setInt(3, show.getlId());
//				ps.setDate(4, Date.valueOf(show.getDate()));
//				ps.setInt(5, show.getStId());
//				ps.setInt(6, show.getThId());
//				ps.setInt(7, show.getScId());
//				ps.setInt(8, Integer.parseInt(""+js.get(1)));
//				ps.setInt(9, Integer.parseInt(""+js.get(2)));
//				ps.setDouble(10, Double.parseDouble(""+js.get(3)));	
//				ps.execute();
//			}
//			List<Seat> seats = new ArrayList<>();
//			ps = Tools.DB.prepareStatement("select * from screenstructure where userId = ? and thId=? and scId =?");
//			ps.setInt(1, show.getUserId());
//			ps.setInt(2, show.getThId());
//			ps.setInt(3, show.getScId());
//			ResultSet rs = ps.executeQuery();
//			while(rs.next()) {
//				seats.add(new Seat(rs.getInt(1),show.getMovieId(),show.getlId(),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),Date.valueOf(show.getDate()),show.getStId(),rs.getInt(6),rs.getInt(7),(rs.getString(9).equals("Active"))?SeatStatus.Unbooked:SeatStatus.Damaged,rs.getString(8)));
//			}
//			for(Seat seat : seats) {
//				ps = Tools.DB.prepareStatement("insert into seat values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
//				ps.setInt(1, seat.getUserId());
//				ps.setInt(2, seat.getMovieId());
//				ps.setInt(3, seat.getlId());
//				ps.setInt(4, seat.getThId());
//				ps.setInt(5, seat.getScId());
//				ps.setInt(6, seat.getGpId());
//				ps.setInt(7, seat.getSeId());
//				ps.setDate(8, seat.getDate());
//				ps.setInt(9, seat.getStId());
//				ps.setInt(10, seat.getRowno());
//				ps.setInt(11, seat.getColumnno());
//				ps.setString(12, String.valueOf(seat.getStatus()));
//				ps.setString(13,seat.getName());
//				ps.execute();
//			}
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
//	}
}
