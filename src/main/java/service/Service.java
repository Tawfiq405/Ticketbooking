package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import enums.Experience;
import enums.Rating;
import models.Movie;
import managers.Tools;

public class Service {
	
	
	@SuppressWarnings("unchecked")
	public static Movie createmovieobj(JSONObject json) {
		JSONObject genre = (JSONObject) json.get("genre");
		Map<Integer, String> genres = new HashMap<>();
		genre.forEach((key, value) -> genres.put(Integer.parseInt(""+key), value.toString()));
		
		JSONObject language = (JSONObject) json.get("language");
		Map<Integer, String> languages = new HashMap<>();
		language.forEach((key, value) -> languages.put(Integer.parseInt(""+key), value.toString()));

		JSONArray imgarr = (JSONArray) json.get("imgarr");
		Map<Integer, String> imgmaps = new HashMap<>();
		imgarr.forEach( img -> imgmaps.put(Integer.parseInt(""+((JSONObject) img).get("languageid")), Service.base64topath((JSONObject) img)));

		Movie movie = new Movie(0,
				(String)json.get("name"),
				Rating.valueOf((String)json.get("ratings")),
				genres,
				languages,
				Date.valueOf(""+json.get("date")),
				Byte.parseByte(((String)json.get("hours"))),
				Byte.parseByte(((String)json.get("min"))),
				(String)json.get("hero"),
				(String)json.get("heroine"),
				(String)json.get("director"),
				(String)json.get("musicdirector"),
				(String)json.get("villan"),
				(String)json.get("synopsis"),
				Experience.valueOf(""+json.get("experience")),
				Double.parseDouble(""+json.get("baseprice")),
				Integer.parseInt((String)json.get("capacity")),
				Service.base64topath((JSONObject) json.get("bigimg")),
				imgmaps,
				5.0
				);
		return movie;
	}
	
	public static String base64topath(JSONObject image) {
		String base64Image = (String)image.get("src");
		String type = (String)image.get("type");
		String fileName = (String)image.get("name");
		String id = ""+image.get("languageid");
		if(!id.equals("null")) {
			fileName+=id;
		}
		fileName+="."+type.split("/")[1];
        String folderPath = "/home/tawfiq-zstk322/eci/MTbooking/";
        String imgpath = "src/main/resources/";
        String[] cvb = base64Image.split(",");
        System.out.println(folderPath+imgpath + fileName);
        try {
            byte[] imageBytes = Base64.getDecoder().decode(cvb[1]);
            Path path = Paths.get(folderPath+imgpath + fileName);
            Files.write(path, imageBytes);
            return cvb[0]+","+"@"+folderPath+imgpath + fileName;
        } catch (IOException e) {
           e.printStackTrace();
           return null;
        }
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized JSONArray getDistributorMovies() throws IOException {
		JSONArray jsonarr = new JSONArray();
		try {
			PreparedStatement p = Tools.DB.prepareStatement("select * from distributormovies ");
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
				JSONObject json = new JSONObject();
				json.put("Id", rs.getInt(1));
				json.put("name", rs.getString(2));
				json.put("rating", rs.getDouble(3));
				
				
				
				
				Date date = rs.getDate(4);
		        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		        String dateString = formatter.format(date);
		        
		        json.put("date", dateString);
		        
		        json.put("hours", rs.getByte(5));
		        json.put("min", rs.getByte(6));
		        json.put("hero", rs.getString(7));
		        json.put("heroine", rs.getString(8));
		        json.put("director", rs.getString(9));
		        json.put("musicdirector", rs.getString(10));
		        json.put("villan", rs.getString(11));
		        json.put("synopsis",rs.getString(12));
		        json.put("baseprice", rs.getDouble(13));
		        json.put("capacity", rs.getInt(14));
		        json.put("bigpath", Movie.pathToBase64(rs.getString(15)));
		        StringBuilder sb = new StringBuilder(rs.getString(17));
				sb.reverse();
				String reversedStr = sb.toString();
		        json.put("experience", reversedStr);
		        json.put("ratings", rs.getString(16));
		        JSONObject jsons = getlanuages(rs.getInt(1));
		        json.put("smallpath", jsons.get("imgmap"));
		        json.put("genre", getgenres(rs.getInt(1)));
				
				
				json.put("language", jsons.get("language"));
//				System.out.println(json);
			jsonarr.add(json);
			}
//			System.out.println(jsonarr);
				return jsonarr;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static synchronized JSONObject getgenres(int id) {
		try {
			PreparedStatement p = Tools.DB.prepareStatement("select genre,genre.gId from genre,genrerelations where genrerelations.gId=genre.gId and movieId= ?");
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			JSONObject json = new JSONObject();
			while(rs.next()) {
				json.put(rs.getInt(2),rs.getString(1));
				
			}
			return json;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static synchronized JSONObject getlanuages(int id) {
		try {
			JSONArray imgarr= new JSONArray();
			PreparedStatement p = Tools.DB.prepareStatement("select language.lId,language,movielId from language,languagerelations where language.lId=languagerelations.lId and movieId=?");
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			JSONObject json = new JSONObject();
			while(rs.next()) {
				json.put(rs.getInt(1),rs.getString(2));
				p = Tools.DB.prepareStatement("select imgpath from smallimg where movielId=?;");
				p.setInt(1, rs.getInt(3));
				ResultSet ps = p.executeQuery();
				if(ps.next()) {
					try {
						JSONObject imgmap = new JSONObject();
						imgmap.put("languageid",rs.getInt(1));
						imgmap.put("src",  Movie.pathToBase64(ps.getString(1)));
						imgarr.add(imgmap);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			JSONObject jsons = new JSONObject();
			jsons.put("language", json);
			jsons.put("imgmap", imgarr);
			return jsons;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
