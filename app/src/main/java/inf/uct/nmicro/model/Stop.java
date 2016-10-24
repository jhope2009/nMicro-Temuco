package inf.uct.nmicro.model;

import java.util.List;

/**
 * @author Javier
 *
 */
public class Stop {

	private int idStop;
	private String address;
	private double latitude;
	private double longitude;
	private List<Route> routes;


	public Stop(){}

	public Stop(int idStop, String address, double lat, double lon) {
		super();
		this.idStop = idStop;
		this.address = address;
		this.latitude=lat;
		this.longitude=lon;

	}

	public Stop(int idStop, String address, double lat, double lon, List<Route> routes) {
		super();
		this.idStop = idStop;
		this.address = address;
		this.latitude=lat;
		this.longitude=lon;
		this.routes = routes;
	}

	public int getIdStop() {
		return idStop;
	}

	/**
	 * @param idStop the idStop to set
	 */
	public void setIdStop(int idStop) {
		this.idStop = idStop;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
}