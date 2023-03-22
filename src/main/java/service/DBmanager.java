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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import enums.Experience;
import enums.Rating;
import managers.Tools;
import models.Impresariomovie;
import models.Movie;
import user.Customer;



public class DBmanager {
	
		
	
		public static  boolean insert(Movie movie) {
			String sql = "INSERT INTO distributormovies (name, rating, releasedate, hours, min, hero, heroine, director, musicdirector, villan, sysnopsis, basepricepershow, capacity, bigpath, ratings, experience) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt;
			try {
				pstmt = Tools.DB.prepareStatement(sql);
				pstmt.setString(1, movie.getName());
				pstmt.setDouble(2, 5.0);
				pstmt.setDate(3, movie.getReleaseDate());
				pstmt.setInt(4, movie.getHours());
				pstmt.setInt(5, movie.getMin());
				pstmt.setString(6, movie.getHero());
				pstmt.setString(7, movie.getHeroine());
				pstmt.setString(8, movie.getDirector());
				pstmt.setString(9, movie.getMusicDirector());
				pstmt.setString(10, movie.getVillan());
				pstmt.setString(11, movie.getSynopsis());
				pstmt.setDouble(12, movie.getBasePricePerScreenPerDay());
				pstmt.setInt(13, movie.getSeatPerScreen());
				pstmt.setString(14, movie.getBigimgpath());
				pstmt.setString(15, movie.getRatings().toString());
				pstmt.setString(16, new StringBuilder(movie.getExperience().toString()).reverse().toString());
				int rowsInserted = pstmt.executeUpdate();
				pstmt = Tools.DB.prepareStatement("select LAST_INSERT_ID()");
				System.out.println("-------------");
				ResultSet rs = pstmt.executeQuery();
				if (rowsInserted > 0 && rs.next()) {
					insertgenre(rs.getInt(1),new ArrayList<>(movie.getGenres().keySet()));
					System.out.println("----------");
					insertlanguage(rs.getInt(1), new ArrayList<>(movie.getLanguages().keySet()),movie.getSmallimgpaths());
				} else {
				    System.out.println("Failed to insert a new movie.");
				}
				System.out.println("dfghjkl");
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

			
		}
		
		
		
	



		
		
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
		
		public static int insertlanguage(int id,List<Integer> languages,Map<Integer, String> images) {
			for(Integer g : languages) {
				System.out.println(images);
				PreparedStatement psmt;
				try {
					psmt = Tools.DB.prepareStatement("INSERT INTO languagerelations (movieId, lId) values (?, ?)");
					psmt.setInt(1,id);
					psmt.setInt(2, g);
					psmt.execute();
					psmt = Tools.DB.prepareStatement("select LAST_INSERT_ID()");
					ResultSet rs = psmt.executeQuery();
					if(rs.next()) {
						psmt = Tools.DB.prepareStatement("INSERT INTO smallimg values (?, ?) ;");
						psmt.setInt(1,rs.getInt(1));
						psmt.setString(2, images.get(g));
						psmt.execute();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return 0;
				}                         	
			}
			return 1;
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
					}

				}
				return 200;
			}catch(Exception ex) {
				ex.printStackTrace();
				return 500;
			}
			
		}







		public static int insert(Customer cus) {
			try {
			    PreparedStatement ps = Tools.DB.prepareStatement("insert into user (name,email,mobile) values (?,?,?)");
			    ps.setString(1, cus.getName());
			    ps.setString(2, cus.getEmail());
			    ps.setString(3, cus.getMobile());
			    ps.execute();
			    ps = Tools.DB.prepareStatement("insert into role values (?,?)");
			    ps = Tools.DB.prepareStatement("select LAST_INSERT_ID()");
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					return rs.getInt(1);	
				}
			    return 0;
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
			return 0;

		}
}
