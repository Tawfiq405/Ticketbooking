package models;


import java.sql.Date;

import user.Impresario;

public class Impresariomovie {
	private Impresario user;
	private int movieId;
	private int lId;
	private int capacity;
	private Date from;
	private Date to;
	private int screencount;
	private double amount;
	private int showperday;
	public Impresario getUser() {
		return user;
	}
	public int getMovieId() {
		return movieId;
	}
	public int getlId() {
		return lId;
	}
	public int getCapacity() {
		return capacity;
	}
	public Date getFrom() {
		return from;
	}
	public Date getTo() {
		return to;
	}
	public int getScreencount() {
		return screencount;
	}
	public double getAmount() {
		return amount;
	}
	public int getShowperday() {
		return showperday;
	}
	public Impresariomovie(Impresario user, int movieId, int lId, int capacity, Date from, Date to, int screencount,
			double amount, int showperday) {
		this.user = user;
		this.movieId = movieId;
		this.lId = lId;
		this.capacity = capacity;
		this.from = from;
		this.to = to;
		this.screencount = screencount;
		this.amount = amount;
		this.showperday = showperday;
	}
	
}
