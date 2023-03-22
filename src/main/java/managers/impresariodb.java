package managers;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import enums.Experience;
import enums.Rating;
import enums.SeatStatus;
import models.Movie;
import models.Screen;
import models.Seat;
import models.Show;
import models.Theatre;
import user.Impresario;

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
		System.out.println(Id);
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
			ex.printStackTrace();
			return new JSONArray();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static String insert(Screen screen) {
//		System.out.println(1);
		JSONObject res = new JSONObject();
		AtomicInteger counter = new AtomicInteger(0);
		int uid = screen.getTheatre().getImpresario().getUserId();
		int thid = screen.getTheatre().getTheatreId();
		int sid = screen.getScId();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("insert into screen values (?,?,?,?,?,?,?)");
			ps.setInt(1, uid);
			ps.setInt(2,thid);
			ps.setInt(3, sid);
			ps.setString(4, screen.getName());
			ps.setString(5, (screen.isRGB())?"Y":"N");
			ps.setInt(6,screen.getSound());
			ps.setString(7, screen.getResolution());
			ps.execute();
//			System.out.println(2);
			for(Entry<Entry<Integer, Integer>, Map<Integer, Set<Entry<Integer, Boolean>>>> s : screen.getScreenseating().entrySet()) {
				Entry<Integer, Integer> key = s.getKey();
				int gpId = key.getKey();
				int seId = key.getValue();
				List<Entry<Integer, Set<Entry<Integer, Boolean>>>> map = new ArrayList<>(s.getValue().entrySet());
				map.forEach(e->{
					int rowno = e.getKey();
					
					try {
						PreparedStatement ps1 = Tools.DB.prepareStatement("insert into screenseatingrelation values (?,?,?,?,?,?,?)");
						ps1.setInt(1, uid);
						ps1.setInt(2,thid);
						ps1.setInt(3, sid);
						ps1.setInt(4, gpId);
						ps1.setInt(5,seId);
						ps1.setInt(6,rowno);
						ps1.setInt(7,e.getValue().size());
						ps1.executeUpdate();
//						System.out.println(3);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					String name = seatname(rowno);
					AtomicInteger cnum = new AtomicInteger(0);
					e.getValue().forEach(ety->{
						try {
							PreparedStatement ps1 = Tools.DB.prepareStatement("insert into screenstructure values (?,?,?,?,?,?,?,?,?,?,?)");
							ps1.setInt(1, uid);
							ps1.setInt(2,thid);
							ps1.setInt(3, sid);
							ps1.setInt(4, gpId);
							ps1.setInt(5,seId);
							ps1.setInt(6,rowno);
							ps1.setInt(7,ety.getKey());
							ps1.setString(8, name+"-"+((ety.getValue())?cnum.incrementAndGet():0));
							ps1.setString(9, (ety.getValue())?"T":"F");
							ps1.setString(10, (ety.getValue())?"Active":"InActive");
							ps1.setInt(11, counter.incrementAndGet());
							ps1.executeUpdate();
//							System.out.println(3);
						}catch(Exception ex) {
							ex.printStackTrace();
						}
					});
				});
			}
//			System.out.println(5);
			return "200";
		}catch(Exception ex) {
			return "500";
		}
	}
	
	public static String seatname(int rowNumber) {
	    StringBuilder sb = new StringBuilder();
	    while (rowNumber > 0) {
	        int remainder = (rowNumber - 1) % 26;
	        char letter = (char) ('A' + remainder);
	        sb.insert(0, letter);
	        rowNumber = (rowNumber - 1) / 26;
	    }

	    return sb.toString();
	}

	

	@SuppressWarnings("unchecked")
	public static JSONArray myscreen(Integer id, Integer thid) {
		JSONArray screens = new JSONArray();
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from screen,soundsystem where userId = ? and thId=? and screen.soundsystemId=soundsystem.soundsystemId");
			ps.setInt(1, id);
			ps.setInt(2, thid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				JSONObject screen = new JSONObject();
				screen.put("thId", thid);
				screen.put("name",rs.getString(4));
				screen.put("scId", rs.getInt(3));
				screen.put("sound", rs.getString(8));
				screen.put("resolutions", rs.getString(7));
				screens.add(screen);
			}
				return screens;
		}catch(Exception ex) {
			return new JSONArray();
		}
	}

	@SuppressWarnings("unchecked")
	public static JSONArray myscreenstructure(Integer id, int thId, int scId) {
		
		Map<Entry<String,Entry<Integer,Integer>>,Map<Integer,Set<Entry<Integer,Boolean>>>> screenseating = new LinkedHashMap<>();

		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from screenstructure,seatingtype where userId=? and thId=? and scId=? and screenstructure.seId=seatingtype.seId order by ctno;");
			ps.setInt(1, id);
			ps.setInt(2,thId);
			ps.setInt(3, scId);
			ResultSet rs= ps.executeQuery();
			while (rs.next()) {
			    int userId = rs.getInt("userId");
//			    int thId = rs.getInt("thId");
//			    int scId = rs.getInt("scId");
			    int gpId = rs.getInt("gpId");
			    int seId = rs.getInt("seId");
			    int rowno = rs.getInt("rowno");
			    int columnno = rs.getInt("columnno");
			    String name = rs.getString("name");
			    boolean isseat = rs.getString("isseat").equals("T");
			    boolean isActive = rs.getString("status").equals("Active");
			    int ctno = rs.getInt("ctno");
			    String type = rs.getString("type");
			    
			    
			    Entry<Integer,Integer> key = new AbstractMap.SimpleEntry<>(gpId,seId);
			    Entry<String,Entry<Integer,Integer>> key1 = new AbstractMap.SimpleEntry<>(type,key);
			    Map<Integer,Set<Entry<Integer,Boolean>>> value1 = screenseating.getOrDefault(key1, new LinkedHashMap<>());
			    
			    Set<Entry<Integer,Boolean>> value2 = value1.getOrDefault(rowno, new LinkedHashSet<>());
			    Entry<Integer,Boolean> key2 = new AbstractMap.SimpleEntry<>(columnno, isseat);
			    value2.add(key2);
			    
			    value1.put(rowno, value2);
			    screenseating.put(key1, value1);
			}
			
			JSONArray jsarr = new JSONArray();
			screenseating.forEach((key,value)->{
				JSONArray head = new JSONArray();
				head.add(key.getKey());
				head.add(key.getValue().getKey());
				head.add(key.getValue().getValue());
				
				JSONObject rows = new JSONObject();
				value.forEach((row,seats)->{
					JSONObject seats1 = new JSONObject();
					seats.forEach((seat2)->{
						seats1.put(seat2.getKey(),seat2.getValue());
						
					});
					rows.put(row, seats1);
				});
				JSONArray ele = new JSONArray();
				ele.add(head);
				ele.add(rows);
				jsarr.add(ele);
			});
			
			
//			List<Entry<Integer, List<JSONObject>>> listofmap = new ArrayList<>(map.entrySet());
//			Collections.sort(listofmap, (Entry<Integer, List<JSONObject>> o1, Entry<Integer, List<JSONObject>> o2) ->  o1.getValue().size() - o2.getValue().size());
//			JSONArray finallist = new JSONArray();
//			for(Entry<Integer, List<JSONObject>> s : listofmap) {
//				List<JSONObject> m = s.getValue();
//				Collections.sort(m,new Comparator<JSONObject>() {
//
//					@Override
//					public int compare(JSONObject o1, JSONObject o2) {
//						List<Integer> a = new ArrayList<>(o1.keySet());
//						List<Integer> b = new ArrayList<>(o1.keySet());
////						b.c
////						System.out.println(Integer.parseInt(a.get(0)) - Integer.parseInt(b.get(0)));
//						return a.get(0) - b.get(0);
//					}
//					
//				});
//	            for(JSONObject v : m) {
//	            	finallist.add(v);
//	            }
//	            
//	            
//			}
			System.out.println(jsarr);
			return jsarr;
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
//				
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

	@SuppressWarnings("unchecked")
	public static void createcreateshow(int userId, int movieId, int lId, String showdate, int stId, int thId, int scId, int asd, JSONArray rate) {
			try {
				PreparedStatement ps = Tools.DB.prepareStatement("select * from user where id = ? ");
				ps.setInt(1, userId);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					Impresario impresario = new Impresario(rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(1),0.0);
					
					ps = Tools.DB.prepareStatement("select * from theatre where userId = ? and thId=?");
					ps.setInt(1, userId);
					ps.setInt(2, thId);
					rs = ps.executeQuery();
					if(rs.next()) {
						Theatre theatre = new Theatre(
						        rs.getString("thname"), 
						        rs.getInt("thId"), 
						        rs.getString("address"), 
						        rs.getString("city"), 
						        rs.getString("state"), 
						        rs.getString("country"), 
						        rs.getDouble("lat"), 
						        rs.getDouble("lon"), 
						        rs.getDouble("rating"), 
						        impresario, 
						        rs.getString("landmark"), 
						        null
						    );					
						
						ps = Tools.DB.prepareStatement("SELECT * FROM screen WHERE userId = ? AND thId = ? AND scId = ?");
						ps.setInt(1, userId);
						ps.setInt(2, thId);
						ps.setInt(3, scId);
						rs = ps.executeQuery();
						if(rs.next()) {
							Screen screen = new Screen(
									rs.getString("name"),
									rs.getString("rgbLaser").equals("Y"),
									rs.getInt("soundsystemId"),
									rs.getString("resolution"),
									theatre,
									rs.getInt("scId"),
									null
									);
							
							ps = Tools.DB.prepareStatement("""
									SELECT m.movieId, m.name, m.rating, m.releasedate, m.hours, m.min, m.hero, m.heroine, m.director, m.musicdirector,
							m.villan, m.sysnopsis, m.basepricepershow, m.capacity, m.bigpath, m.ratings, m.experience, l.language ,l.lId
							FROM distributormovies m
							INNER JOIN languagerelations ml ON m.movieId = ml.movieId
							INNER JOIN language l ON ml.lId = l.lId
							WHERE m.movieId = ? and l.lId=?;
									""");
							ps.setInt(1, movieId);
							ps.setInt(2, lId);
							rs = ps.executeQuery();
							if(rs.next()) {
//								int movieId = rs.getInt("movieId");
								String name = rs.getString("name");
								double rating = rs.getDouble("rating");
								Date releaseDate = rs.getDate("releasedate");
								byte hours = rs.getByte("hours");
								byte min = rs.getByte("min");
								String hero = rs.getString("hero");
								String heroine = rs.getString("heroine");
								String director = rs.getString("director");
								String musicDirector = rs.getString("musicdirector");
								String villan = rs.getString("villan");
								String synopsis = rs.getString("sysnopsis");
								String originalString = rs.getString("experience");
								StringBuilder reversedString = new StringBuilder(originalString);
								reversedString = reversedString.reverse();
								Experience experience = Experience.valueOf(reversedString.toString());
								double basePricePerScreenPerDay = rs.getDouble("basepricepershow");
								int seatPerScreen = rs.getInt("capacity");
								String bigimgpath = rs.getString("bigpath");
								String language = rs.getString("language");
								Rating ratingEnum = Rating.valueOf(rs.getString("ratings"));
								Map<Integer, String> languages = new HashMap<>();
								languages.put(rs.getInt("lId"), language);
								Movie movie = new Movie(movieId, name, ratingEnum, null, languages, releaseDate, hours, min, hero, heroine,
										director, musicDirector, villan, synopsis, experience, basePricePerScreenPerDay, seatPerScreen, bigimgpath, null, rating);
							
								Map<Entry<Integer, Integer>, Entry<String, Double>> price = new HashMap<>();
								
								rate.forEach((k)->{
									JSONArray jsarr = (JSONArray)k;
									Entry<Integer, Integer> gands = new SimpleEntry<Integer,Integer>(Integer.parseInt(""+jsarr.get(1)),Integer.parseInt(""+jsarr.get(2)));
									Entry<String, Double> nandr = new SimpleEntry<String,Double>(""+jsarr.get(0),Double.parseDouble(""+jsarr.get(3)));
									price.put(gands, nandr);
								});
								
								
								Show show = new Show(movie,Date.valueOf(showdate),screen,price,stId);
								insert(show);
								
								
							
							}
							
							
						}
						
					}
					
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
	}

	private static void insert(Show show) {
		PreparedStatement pstmt = null;
		try {
			int userId = show.getScreen().getTheatre().getImpresario().getUserId();
			int movieId = show.getMovie().getId();
			int lId= new ArrayList<Integer>(show.getMovie().getLanguages().keySet()).get(0);
			Date showdate = show.getDateAndTime();
//			pstmt.setInt(5, show.getTime());
			int thId = show.getScreen().getTheatre().getTheatreId();
//			pstmt.setInt(7, show.getScreen().getScId());
			pstmt = Tools.DB.prepareStatement("insert INTO myshow (userId, movieId, lId, showdate, stId, thId, scId) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, userId);
			pstmt.setInt(2, movieId);
			pstmt.setInt(3, lId);
			pstmt.setDate(4, showdate);
			pstmt.setInt(5, show.getTime());
			pstmt.setInt(6, thId);
			pstmt.setInt(7, show.getScreen().getScId());
			pstmt.execute();
			
			show.getTicketrate().forEach((k,v)->{
				
				try {
					PreparedStatement ps = Tools.DB.prepareStatement("insert into ticketrate (userId, movieId, lId, showdate, stId, thId, scId, gpId, seId, rate) \n"
						+ "    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
				ps.setInt(1, userId);
				ps.setInt(2, movieId);
				ps.setInt(3, lId);
				ps.setDate(4, showdate);
				ps.setInt(5, show.getTime());
				ps.setInt(6, thId);
				ps.setInt(7, show.getScreen().getScId());
				ps.setInt(8, k.getKey());
				ps.setInt(9, k.getValue());
				System.out.println(v.getValue());
				ps.setDouble(10, v.getValue());
				ps.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			});
			
			List<Seat> seats = new ArrayList<>();
			pstmt = Tools.DB.prepareStatement("select * from screenstructure where userId = ? and thId=? and scId =? order by ctno");
			pstmt.setInt(1, userId);
			pstmt.setInt(2, thId);
			pstmt.setInt(3, show.getScreen().getScId());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				SeatStatus s = (rs.getString(9).equals("F"))?SeatStatus.notseat:(rs.getString(10).equals("Active"))?SeatStatus.Unbooked:SeatStatus.InActive;
				seats.add(new Seat(rs.getInt(1),movieId,lId,rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),show.getDateAndTime(),show.getTime(),rs.getInt(6),rs.getInt(7),s,rs.getString(8)));
			}
			for(Seat seat : seats) {
				pstmt = Tools.DB.prepareStatement("insert into seat values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, seat.getUserId());
				pstmt.setInt(2, seat.getMovieId());
				pstmt.setInt(3, seat.getlId());
				pstmt.setInt(4, seat.getThId());
				pstmt.setInt(5, seat.getScId());
				pstmt.setInt(6, seat.getGpId());
				pstmt.setInt(7, seat.getSeId());
				pstmt.setDate(8, seat.getDate());
				pstmt.setInt(9, seat.getStId());
				pstmt.setInt(10, seat.getRowno());
				pstmt.setInt(11, seat.getColumnno());
				pstmt.setString(12, String.valueOf(seat.getStatus()));
				pstmt.setString(13,seat.getName());
				pstmt.execute();
			}
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public static String addscreen(JSONObject json) {
		Map<Entry<Integer,Integer>,Map<Integer,Set<Entry<Integer,Boolean>>>> screenseating = new LinkedHashMap<>();
		((JSONArray)json.get("strcture")).forEach((I) -> {
			JSONArray jsarr = (JSONArray)I;
			JSONArray head = (JSONArray) jsarr.get(0);
			JSONObject value = (JSONObject)jsarr.get(1);
			Entry<Integer,Integer> heads = new SimpleEntry<>(Integer.parseInt(""+head.get(0)),Integer.parseInt(""+head.get(1)));
			
				Map<Integer,Set<Entry<Integer,Boolean>>> map = new LinkedHashMap<>();
				((JSONObject)value).forEach((k,v)->{
					Integer rowno = Integer.parseInt(""+k);
					TreeSet<Map.Entry<Integer, Boolean>> seats = new TreeSet<>((e1, e2) -> e1.getKey() -  e2.getKey());

					((JSONObject)v).forEach((a,b)->{
						Entry<Integer,Boolean> tails = new SimpleEntry<>(Integer.parseInt(""+a),(Boolean)b);
						seats.add(tails);
					});
					map.put(rowno, seats);
				});
				screenseating.put(heads, map);				
		});
		System.out.println(screenseating);
		Impresario owner = null;
		int uid = Integer.parseInt(""+json.get("id"));
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from user where id=?");
			ps.setInt(1, uid);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				owner = new Impresario(rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(1),0);
			}
			Integer thid=Integer.parseInt(""+json.get("thId"));
			if(thid!=null) {
				Theatre theatre = null;
				try {
					ps = Tools.DB.prepareStatement("select * from theatre where userId=? and thId=?");
					ps.setInt(1, uid);
					ps.setInt(2, thid);
					rs = ps.executeQuery();
					if(rs.next()) {
						theatre = new Theatre(rs.getString(1),thid,rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),0.0,0.0,0.0,owner,rs.getString(11),null);
					}
					ps=  Tools.DB.prepareStatement("select count(*)+1 from screen where userId=? and thId=?");
					ps.setInt(1, uid);
					ps.setInt(2, thid);
					rs = ps.executeQuery();
					int scid=0;
					if(rs.next()) {
						scid=rs.getInt(1);
					}
					Screen screen = new Screen((String)json.get("name"),(boolean)json.get("rgb"),Integer.parseInt((String)json.get("sound")),(String)json.get("Resolution"),theatre,scid,screenseating);
					System.out.println(screen);
					if(impresariodb.insert(screen).equals("200")) {
						return "200";
					}
				}catch(Exception ex) {
					ex.printStackTrace();
					return "500";		
				}
			}else {
				return "500";	
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			return "500";		
		}
		return "500";
	}
	
	
	public static String addtheatre(JSONObject json) {
		Impresario owner = null;
		try {
			PreparedStatement ps = Tools.DB.prepareStatement("select * from user where id=?");
			ps.setInt(1, Integer.parseInt(""+json.get("id")));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				owner = new Impresario(rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(1),0.0);
			}
		}catch(Exception ex) {
			return ex.getMessage();		
		}
		List<Integer> amenities = new ArrayList<>();
		for(Object a : (JSONArray)json.get("amenities")) {
			amenities.add(Integer.parseInt((String)a));
		}
		Theatre theatre = new Theatre((String)json.get("name"),0,(String)json.get("address"),(String)json.get("city"),(String)json.get("state"),(String)json.get("country"),Double.parseDouble((String)json.get("lat")),Double.parseDouble((String)json.get("lon")),5.0,owner,(String)json.get("landmark"),amenities);
		System.out.println(theatre);
		JSONObject res = impresariodb.addtheatre(theatre);
		if(((String)res.get("statusCode")).equals("200")) {
			return "200";
		}else {
			return "500";
		}
	}
	
	
	
	
	
	
}
