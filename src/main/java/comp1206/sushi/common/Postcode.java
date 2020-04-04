package comp1206.sushi.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Postcode extends Model {

	private String name;
	private Map<String, Double> latLong;
	private Number distance;

	public Postcode(String code) {
		this.name = code;
		calculateLatLong();
		this.distance = Integer.valueOf(0);
	}

	public Postcode(String code, Restaurant restaurant) {
		this.name = code;
		calculateLatLong();
		calculateDistance(restaurant);
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Number getDistance() {
		return this.distance;
	}

	public Map<String, Double> getLatLong() {
		return this.latLong;
	}

	protected void calculateDistance(Restaurant restaurant) {
		//This function needs implementing
		Postcode destination = restaurant.getLocation();
		Map<String, Double> map = destination.getLatLong();
		Double distanceRestaurant = distance(this.latLong.get("lat"), this.latLong.get("lon"), map.get("lat"), map.get("lon"));

		this.distance = distanceRestaurant;
	}

	protected void calculateLatLong() {

		String[] splitLatLong = retrieveLatLong().replaceAll("[{}\"]", "").split("[,:]");

		this.latLong = new HashMap<String, Double>();
		latLong.put("lat", Double.parseDouble(splitLatLong[3]));
		latLong.put("lon", Double.parseDouble(splitLatLong[5]));
		this.distance = new Integer(0);
	}

	private String retrieveLatLong() {
		String latlong = "";
		String postcode = this.name.replaceAll("\\s", "");

		try {

			URL url = new URL("https://www.southampton.ac.uk/~ob1a12/postcode/postcode.php?postcode=" +
					postcode);

			try {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
				latlong = bufferedReader.readLine();
				bufferedReader.close();
			} catch (IOException e) {
				System.out.println(e);
			}

		} catch (MalformedURLException e) {
			System.out.println(e);
		}

		return latlong;
	}


	// Calculated distance between two sets of lat/long values.
	// Taken from https://dzone.com/articles/distance-calculation-using-3
	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return dist;
	}
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

}