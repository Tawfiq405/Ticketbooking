package models;

import java.sql.Date;

import enums.SeatStatus;

public class Seat {

	private int userId;
	private int movieId;
	private int lId;
	private int thId;
	private int scId;
	private int gpId;
	private int seId;
	private Date date;
	public int getUserId() {
		return userId;
	}
	public int getMovieId() {
		return movieId;
	}
	public int getlId() {
		return lId;
	}
	public int getThId() {
		return thId;
	}
	public int getScId() {
		return scId;
	}
	public int getGpId() {
		return gpId;
	}
	public int getSeId() {
		return seId;
	}
	public Date getDate() {
		return date;
	}
	public int getStId() {
		return stId;
	}
	public int getRowno() {
		return rowno;
	}
	public int getColumnno() {
		return columnno;
	}
	public SeatStatus getStatus() {
		return status;
	}
	public String getName() {
		return name;
	}
	private int stId;
	private int rowno;
	private int columnno;
	SeatStatus status;
	private String name;
	public Seat(int userId, int movieId, int lId, int thId, int scId, int gpId, int seId, Date date, int stId,
			int rowno, int columnno, SeatStatus status, String name) {
		super();
		this.userId = userId;
		this.movieId = movieId;
		this.lId = lId;
		this.thId = thId;
		this.scId = scId;
		this.gpId = gpId;
		this.seId = seId;
		this.date = date;
		this.stId = stId;
		this.rowno = rowno;
		this.columnno = columnno;
		this.status = status;
		this.name = name;
	}
	
	

}
