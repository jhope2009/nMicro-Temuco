package inf.uct.nmicro.model;

/**
 * @author Javier
 *
 */
public class Stop {

	private int idStop;
	private String address;
	private double latitude;
	private double longitude;

	
	public Stop(){}
	
	/**
	 * @param idStop
	 * @param address


	 */
	public Stop(int idStop, String address, double lat, double lon) {
		super();
		this.idStop = idStop;
		this.address = address;
		this.latitude=lat;
		this.longitude=lon;
	}

	/**
	 * @return the idStop
	 */
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
}