package models;

import java.sql.Date;
import java.time.LocalTime;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;

public class Show {
	
	private Movie movie;
	private Date dateAndTime;
	private Screen screen;
	private Map<Entry<Integer,Integer>,Entry<String,Double>> ticketrate;
	private Integer time;
	public Movie getMovie() {
		return movie;
	}
	public Date getDateAndTime() {
		return dateAndTime;
	}
	public Screen getScreen() {
		return screen;
	}
	public Map<Entry<Integer, Integer>, Entry<String, Double>> getTicketrate() {
		return ticketrate;
	}
	public Integer getTime() {
		return time;
	}
	public Show(Movie movie, Date dateAndTime, Screen screen,
			Map<Entry<Integer, Integer>, Entry<String, Double>> ticketrate, Integer time) {
		super();
		this.movie = movie;
		this.dateAndTime = dateAndTime;
		this.screen = screen;
		this.ticketrate = ticketrate;
		this.time = time;
	}
	
	
	
}
