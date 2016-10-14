package inf.uct.nmicro.model;

/**
 * @author Javier
 *
 */
public class Point {
	
	private int idPoint;
	private double latitude;
	private double longitude;
	
	public Point(){}

	public Point(int idPoint, double latitude, double longitude) {
		this.idPoint = idPoint;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getIdPoint() {
		return idPoint;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setIdPoint(int idPoint) {
		this.idPoint = idPoint;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
