package model;

/**
 * @author Javier
 *
 */
public class Point {
	
	private int idPoint;
	private float latitude;
	private float longitude;
	
	public Point(){}
	
	/**
	 * @param idPoint
	 * @param latitude
	 * @param longitude
	 */
	public Point(int idPoint, float latitude, float longitude) {
		this.idPoint = idPoint;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * @return the idPoint
	 */
	public int getIdPoint() {
		return idPoint;
	}

	/**
	 * @param idPoint the idPoint to set
	 */
	public void setIdPoint(int idPoint) {
		this.idPoint = idPoint;
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
}
