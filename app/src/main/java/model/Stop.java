package model;

/**
 * @author Javier
 *
 */
public class Stop {

	private int idStop;
	private String address;
	private Point point;
	
	public Stop(){}
	
	/**
	 * @param idStop
	 * @param address
	 * @param point
	 */
	public Stop(int idStop, String address, Point point) {
		super();
		this.idStop = idStop;
		this.address = address;
		this.point = point;
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

	/**
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(Point point) {
		this.point = point;
	}
	
}