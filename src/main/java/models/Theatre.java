package models;

import java.util.List;

import user.Impresario;

public class Theatre {
	private String theatreName;
	private int theatreId;
	private String address;
	private String  city;
	private String state;
	private String country;
	private double lat;
	private double lon;
	private double rating;
	private Impresario impresario;
	private String landmark;
	private List<Integer> amenities;
	public String getTheatreName() {
		return theatreName;
	}
	public int getTheatreId() {
		return theatreId;
	}
	public String getAddress() {
		return address;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getCountry() {
		return country;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public double getRating() {
		return rating;
	}
	public Impresario getImpresario() {
		return impresario;
	}
	public String getLandmark() {
		return landmark;
	}
	public List<Integer> getAmenities() {
		return amenities;
	}
	public Theatre(String theatreName, int theatreId, String address, String city, String state, String country,
			double lat, double lon, double rating, Impresario impresario, String landmark, List<Integer> amenities) {
		this.theatreName = theatreName;
		this.theatreId = theatreId;
		this.address = address;
		this.city = city;
		this.state = state;
		this.country = country;
		this.lat = lat;
		this.lon = lon;
		this.rating = rating;
		this.impresario = impresario;
		this.landmark = landmark;
		this.amenities = amenities;
	}
	@Override
	public String toString() {
		return "Theatre [theatreName=" + theatreName + ", theatreId=" + theatreId + ", address=" + address + ", city="
				+ city + ", state=" + state + ", country=" + country + ", lat=" + lat + ", lon=" + lon + ", rating="
				+ rating + ", impresario=" + impresario + ", landmark=" + landmark + ", amenities=" + amenities + "]";
	}
	
	
}
