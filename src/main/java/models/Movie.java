package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import enums.Experience;
import enums.Rating;

public class Movie {
	private int id;
	private String name;
	private Rating ratings;
	private Map<Integer,String> genres;
	private Map<Integer,String> languages;
	private Date releaseDate;
	private byte hours;
	private byte min;
	private String hero;
	private String heroine;
	private String director;
	private String musicDirector;
	private String villan;
	private String synopsis;
	private Experience experience;
	private double basePricePerScreenPerDay;
	private int seatPerScreen;
	private String bigimgpath;
	private Map<Integer,String> smallimgpaths;
	private double rating;
	
	public Movie(int id, String name, Rating ratings, Map<Integer, String> genres, Map<Integer, String> languages,
			Date releaseDate, byte hours, byte min, String hero, String heroine, String director, String musicDirector,
			String villan, String synopsis, Experience experience, double basePricePerScreenPerDay, int seatPerScreen,
			String bigimgpath, Map<Integer, String> smallimgpaths, double rating) {
		this.id = id;
		this.name = name;
		this.rating = rating;
		this.genres = genres;
		this.languages = languages;
		this.releaseDate = releaseDate;
		this.hours = hours;
		this.min = min;
		this.hero = hero;
		this.heroine = heroine;
		this.director = director;
		this.musicDirector = musicDirector;
		this.villan = villan;
		this.synopsis = synopsis;
		this.experience = experience;
		this.basePricePerScreenPerDay = basePricePerScreenPerDay;
		this.seatPerScreen = seatPerScreen;
		this.bigimgpath = bigimgpath;
		this.smallimgpaths = smallimgpaths;
		this.ratings = ratings;
	}

	@Override
	public String toString() {
		return "Movie [id=" + id + ", name=" + name + ", ratings=" + ratings + ", genres=" + genres + ", languages="
				+ languages + ", releaseDate=" + releaseDate + ", hours=" + hours + ", min=" + min + ", hero=" + hero
				+ ", heroine=" + heroine + ", director=" + director + ", musicDirector=" + musicDirector + ", villan="
				+ villan + ", synopsis=" + synopsis + ", experience=" + experience + ", basePricePerScreenPerDay="
				+ basePricePerScreenPerDay + ", seatPerScreen=" + seatPerScreen + ", bigimgpath=" + bigimgpath
				+ ", smallimgpaths=" + smallimgpaths + ", rating=" + rating + "]";
	}

	public static synchronized String pathToBase64(String paths) throws IOException{
		String[] cvb = paths.split("@");
		System.out.println(paths);
		Path path = Paths.get(cvb[1]);
        File file = path.toFile();
        FileInputStream fis;
        byte[] imageData;
		try {
			fis = new FileInputStream(file);
			imageData = new byte[(int) file.length()];
	        fis.read(imageData);
	        fis.close();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("");
		}
        
        return cvb[0]+Base64.getEncoder().encodeToString(imageData);
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Rating getRatings() {
		return ratings;
	}

	public Map<Integer, String> getGenres() {
		return genres;
	}

	public Map<Integer, String> getLanguages() {
		return languages;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public byte getHours() {
		return hours;
	}

	public byte getMin() {
		return min;
	}

	public String getHero() {
		return hero;
	}

	public String getHeroine() {
		return heroine;
	}

	public String getDirector() {
		return director;
	}

	public String getMusicDirector() {
		return musicDirector;
	}

	public String getVillan() {
		return villan;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public Experience getExperience() {
		return experience;
	}

	public double getBasePricePerScreenPerDay() {
		return basePricePerScreenPerDay;
	}

	public int getSeatPerScreen() {
		return seatPerScreen;
	}

	public String getBigimgpath() {
		return bigimgpath;
	}

	public Map<Integer, String> getSmallimgpaths() {
		return smallimgpaths;
	}

	public double getRating() {
		return rating;
	}
	
	
	
}
