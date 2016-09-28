package inf.uct.nmicro.model;

import java.util.List;

/**
 * @author Javier
 *
 */
public class Route {
	
	private int idRoute;
	private Company company;
	private String name;
	private List<Stop> stops;
	private List<Point> points;
	
	public Route(){}

	/**
	 * @param idRoute
	 * @param company
	 * @param name
	 * @param stops
	 * @param points
	 */
	public Route(int idRoute, Company company, String name, List<Stop> stops, List<Point> points) {
		super();
		this.idRoute = idRoute;
		this.company = company;
		this.name = name;
		this.stops = stops;
		this.points = points;
	}

	/**
	 * @return the idRoute
	 */
	public int getIdRoute() {
		return idRoute;
	}

	/**
	 * @param idRoute the idRoute to set
	 */
	public void setIdRoute(int idRoute) {
		this.idRoute = idRoute;
	}

	/**
	 * @return the company
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the stops
	 */
	public List<Stop> getStops() {
		return stops;
	}

	/**
	 * @param stops the stops to set
	 */
	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	/**
	 * @return the points
	 */
	public List<Point> getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(List<Point> points) {
		this.points = points;
	}
		
}
