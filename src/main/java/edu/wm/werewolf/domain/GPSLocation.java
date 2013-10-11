package edu.wm.werewolf.domain;

public class GPSLocation {

	private double lat;
	private double lon;
	
	public GPSLocation(double lat, double lon){
		this.lat = lat;
		this.lon = lon;
	}
	
	public GPSLocation()
	{
		
	}
	
	public double getLon() {
		return lon;
	}
	public void setLon(int lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(int lat) {
		this.lat = lat;
	}
	public String toString()
	{
		return this.lat + ", " + this.lon;
	}
}
