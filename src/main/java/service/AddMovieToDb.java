package service;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import models.Impresariomovie;
import models.Movie;
import enums.Experience;
import enums.Rating;
import managers.Tools;



public class AddMovieToDb {
	
//		public static Movie createmovieobj(JSONObject json) {
//			JSONArray genrearr = (JSONArray) json.get("genre");
//			List<Integer> genres = new ArrayList<>();
//			for(Object g : genrearr) {
//				genres.add(Integer.parseInt((String)g));
//			}
//			
//			JSONArray languageobj =(JSONArray) json.get("language");
//			List<Integer> languages = new ArrayList<>();
//			for(Object g : languageobj) {
//				languages.add(Integer.parseInt((String)g));
//			}
//			
//
//
////			private String bigimgpath;
////			private String smallimgpath;
//			
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			
//			java.util.Date date = null;
//			try {
//				date = sdf.parse((String)json.get("date"));
//			} catch (java.text.ParseException e) {
//				date = new java.util.Date();
//			}
//			java.sql.Date releasedate = new java.sql.Date(date.getTime());
//			Movie movie = new Movie(0,
//					(String)json.get("name"),
//					Rating.valueOf((String)json.get("rating")),
//					genres,
//					languages,
//					releasedate,
//					Byte.parseByte(((String)json.get("hours"))),
//					Byte.parseByte(((String)json.get("min"))),
//					(String)json.get("hero"),
//					(String)json.get("heroine"),
//					(String)json.get("director"),
//					(String)json.get("musicDirector"),
//					(String)json.get("villan"),
//					(String)json.get("synopsis"),
//					Experience.valueOf((String)json.get("experience")),
//					Double.parseDouble((String)json.get("basePricePerScreenPerDay")),
//					Integer.parseInt((String)json.get("seatPerScreen")),
//					(String)json.get("bigpath"),
//					(String)json.get("smallpath"),
//					5.0
//					);
//			return movie;
//		}
	
//		public static  int addmovie(Movie movie) {
//			ResultSet rs=null;
//			PreparedStatement psmt;
//			StringBuilder sb = new StringBuilder(String.valueOf(movie.getExperience()));
//			sb.reverse();
//			String reversedStr = sb.toString();
//			System.out.println(reversedStr);
//			try {
//				psmt = Tools.DB.prepareStatement("INSERT INTO distributormovies (name, rating, releasedate, hours, min, hero, heroine, director, musicdirector, villan, sysnopsis, basepricepershow, capacity, bigpath, smallpath,ratings, experience) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);");                         
//				psmt.setString(1, movie.getName());
//				psmt.setDouble(2, 5.0);
//				psmt.setDate(3, movie.getReleaseDate());
//				psmt.setInt(4,movie.getHours());
//				psmt.setInt(5, movie.getMin());
//				psmt.setString(6, movie.getHero());
//				psmt.setString(7, movie.getHeroine());
//				psmt.setString(8, movie.getDirector());
//				psmt.setString(9, movie.getMusicDirector());
//				psmt.setString(10, movie.getVillan());
//				psmt.setString(11, movie.getSynopsis());
//				
//				psmt.setDouble(12, movie.getBasePricePerScreenPerDay());
//				psmt.setInt(13,movie.getSeatPerScreen());
//				psmt.setString(14, movie.getBigimgpath());
//				psmt.setString(15, movie.getSmallimgpath());
//				psmt.setString(16, String.valueOf(movie.getRating()));
//				psmt.setString(17, reversedStr);
//				int s = psmt.executeUpdate();
//				int id = getId(movie);
//				if(s>0) {
//					int c = insertgenre(id,movie.getGenres());
//					int v = insertlanguage(id,movie.getLanguages());
//					return c+v+1;					
//				}
//				
//			} catch (SQLException e) {
//				e.printStackTrace();
//				return -1;
//			}
//			return -1;
//		}
		
//		@SuppressWarnings("unchecked")
//		public static JSONObject movietojson(Movie movie,int id) throws IOException {
//			JSONObject json = new JSONObject();
//			json.put("Id", id);
//			json.put("name", movie.getName());
//			json.put("ratings", String.valueOf(movie.getRating()));
//			json.put("genres", getgenres(id));
//			json.put("languages", getlanuages(id));
//			Date date = movie.getReleaseDate();
//	        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//	        String dateString = formatter.format(date);
//	        json.put("date", dateString);
//	        json.put("hours", movie.getHours());
//	        json.put("min", movie.getMin());
//	        json.put("hero", movie.getHero());
//	        json.put("heroine", movie.getHeroine());
//	        json.put("director", movie.getDirector());
//	        json.put("musicdirector", movie.getMusicDirector());
//	        json.put("villan", movie.getVillan());
//	        json.put("synopsis", movie.getSynopsis());
//	        StringBuilder sb = new StringBuilder(String.valueOf(movie.getExperience()));
//			sb.reverse();
//			String reversedStr = sb.toString();
//	        json.put("experience", reversedStr);
//	        json.put("baseprice", movie.getBasePricePerScreenPerDay());
//	        json.put("capacity", movie.getSeatPerScreen());
//	        json.put("bigpath", Movie.pathToBase64(movie.getBigimgpath()));
//	        json.put("smallpath", Movie.pathToBase64(movie.getSmallimgpath()));
//			return json;
//		}
		
		public static int insertgenre(int id,List<Integer> genres) {
			for(Integer g : genres) {
				PreparedStatement psmt;
				try {
					psmt = Tools.DB.prepareStatement("INSERT INTO genrerelations values (?, ?) ;");
					psmt.setInt(1,id);
					psmt.setInt(2, g);
					psmt.execute();
				} catch (SQLException e) {
					e.printStackTrace();
					return 0;
				}                         	
			}
			return 1;
		}
		
		public static int insertlanguage(int id,List<Integer> languages) {
			for(Integer g : languages) {
				PreparedStatement psmt;
				try {
					psmt = Tools.DB.prepareStatement("INSERT INTO languagerelations values (?, ?) ;");
					psmt.setInt(1,id);
					psmt.setInt(2, g);
					psmt.execute();
				} catch (SQLException e) {
					e.printStackTrace();
					return 0;
				}                         	
			}
			return 1;
		}
		
		public static int getId(Movie movie) {
			ResultSet rs=null;
			PreparedStatement impobj;
			try {
				impobj = Tools.DB.prepareStatement("select movieId from distributormovies where name like ? and releasedate = ?");
				impobj.setString(1, movie.getName());
				impobj.setDate(2, movie.getReleaseDate());
				rs = impobj.executeQuery();
				if(rs.next()) {
					return rs.getInt(1);
				}
				return 0;
			} catch (SQLException e) {
				e.printStackTrace();
				return 0;
			}
			
		}
		
		public static String reverse(String str) {
			List<String> asd = Arrays.asList(str.split(""));
			Collections.reverse(asd);
			str="";
			for(String s : asd) {
				str+=s;
			}
			return str;
		}
		
		@SuppressWarnings("unchecked")
		public static JSONArray getgenres(int id) {
			JSONArray jsonarr = new JSONArray();
			try {
				PreparedStatement p = Tools.DB.prepareStatement("select genre from genre,genrerelations where genrerelations.gId=genre.gId and movieId= ?");
				p.setInt(1, id);
				ResultSet rs = p.executeQuery();
				
				while(rs.next()) {
					jsonarr.add(rs.getString(1));
					
				}
				return jsonarr;
			} catch (SQLException e) {
				e.printStackTrace();
				return jsonarr;
			}
		}
		
		
		@SuppressWarnings("unchecked")
		public static JSONArray getlanuages(int id) {
			JSONArray jsonarr = new JSONArray();
			try {
				PreparedStatement p = Tools.DB.prepareStatement("select language from language,languagerelations where languagerelations.lId=language.lId and movieId=?;");
				p.setInt(1, id);
				ResultSet rs = p.executeQuery();
				while(rs.next()) {
					jsonarr.add(rs.getString(1));
					
				}
				return jsonarr;
			} catch (SQLException e) {
				e.printStackTrace();
				return jsonarr;
			}
		}

		public static int addmovieforimpresario(List<Impresariomovie> movies) {
			
			try {
				for(Impresariomovie movie : movies) {
					PreparedStatement ps = Tools.DB.prepareStatement("insert into impresariomovies values (?,?,?,?,?,?,?,?,?)");
					ps.setInt(1, movie.getUser().getUserId());
					ps.setInt(2, movie.getMovieId());
					ps.setInt(3, movie.getlId());
					ps.setInt(4,movie.getCapacity());
					ps.setDate(5,movie.getFrom());
					ps.setDate(6,movie.getTo());
					ps.setInt(7,movie.getScreencount());
					ps.setDouble(8, movie.getAmount());
					ps.setInt(9, movie.getShowperday());
					ps.execute();
					for(long i=movie.getFrom().getTime();i<movie.getTo().getTime();i+=86400000) {
						System.out.println( new Date(i));
						try {
							ps = Tools.DB.prepareStatement("insert into impresariobalanceshows values (?,?,?,?,?,?)");
							ps.setInt(1, movie.getUser().getUserId());
							ps.setInt(2, movie.getMovieId());
							ps.setInt(3, movie.getlId());
							ps.setInt(4,movie.getCapacity());
							ps.setDate(5, new Date(i));
							ps.setInt(6, movie.getScreencount() * movie.getShowperday());
							ps.execute();
							ps = Tools.DB.prepareStatement("insert into impresariobuyedmovies values (?,?,?,?,?,?)");
							ps.setInt(1, movie.getUser().getUserId());
							ps.setInt(2, movie.getMovieId());
							ps.setInt(3, movie.getlId());
							ps.setInt(4,movie.getCapacity());
							ps.setDate(5, new Date(i));
							ps.setInt(6, movie.getScreencount() * movie.getShowperday());
							ps.execute();
						}catch(Exception ex) {
							ex.printStackTrace();
							try {
								ps = Tools.DB.prepareStatement("update impresariobalanceshows set showbalance = showbalance + ? where  userId = ? and movieId = ? and lId = ? and showdate like ? and maxcapacity=?");
								ps.setInt(1, movie.getScreencount() * movie.getShowperday());
								ps.setInt(2, movie.getUser().getUserId());
								ps.setInt(3, movie.getMovieId());
								ps.setInt(4, movie.getlId());
								ps.setDate(5, new Date(i));
								ps.setInt(6, movie.getCapacity());
								int v = ps.executeUpdate();
								System.out.println(v);
								ps = Tools.DB.prepareStatement("update impresariobuyedmovies set showbalance = showbalance + ? where userId = ? and movieId = ? and lId = ? and showdate like ? and maxcapacity=?");
								ps.setInt(1, movie.getScreencount() * movie.getShowperday());
								ps.setInt(2, movie.getUser().getUserId());
								ps.setInt(3, movie.getMovieId());
								ps.setInt(4, movie.getlId());
								ps.setDate(5, new Date(i));
								ps.setInt(6, movie.getCapacity());
								ps.execute();
								v = ps.executeUpdate();
								System.out.println(v);
							}catch(Exception x) {
								x.printStackTrace();
							}
						}
						ps = Tools.DB.prepareStatement("update accountbalance set balance = balance + ? where  userId = ? ");
						ps.setDouble(1, movie.getAmount());
						ps.setInt(2, movie.getUser().getUserId());
						ps.execute();
					}

				}
				
				System.out.println(200);
				return 200;
			}catch(Exception ex) {
				ex.printStackTrace();
				return 500;
			}
			
		}
}
