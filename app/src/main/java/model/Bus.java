package model;

/**
 * @author Javier
 *
 */
public class Bus {
	
	private int idBus;
	private Route routeGoing;
	private Route routeReturn;
	private String register;
	
	public Bus(){}

	/**
	 * @param idBus
	 * @param routeGoing
	 * @param routeReturn
	 * @param register
	 */
	public Bus(int idBus, Route routeGoing, Route routeReturn, String register) {
		super();
		this.idBus = idBus;
		this.routeGoing = routeGoing;
		this.routeReturn = routeReturn;
		this.register = register;
	}

	/**
	 * @return the idBus
	 */
	public int getIdBus() {
		return idBus;
	}

	/**
	 * @param idBus the idBus to set
	 */
	public void setIdBus(int idBus) {
		this.idBus = idBus;
	}

	/**
	 * @return the routeGoing
	 */
	public Route getRouteGoing() {
		return routeGoing;
	}

	/**
	 * @param routeGoing the routeGoing to set
	 */
	public void setRouteGoing(Route routeGoing) {
		this.routeGoing = routeGoing;
	}

	/**
	 * @return the routeReturn
	 */
	public Route getRouteReturn() {
		return routeReturn;
	}

	/**
	 * @param routeReturn the routeReturn to set
	 */
	public void setRouteReturn(Route routeReturn) {
		this.routeReturn = routeReturn;
	}

	/**
	 * @return the register
	 */
	public String getRegister() {
		return register;
	}

	/**
	 * @param register the register to set
	 */
	public void setRegister(String register) {
		this.register = register;
	}	
	
}
