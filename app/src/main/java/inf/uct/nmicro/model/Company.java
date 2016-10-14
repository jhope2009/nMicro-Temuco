package inf.uct.nmicro.model;

import java.util.List;

/**
 * @author Javier
 *
 */
public class Company {

	private int idCompany;
	private String name;
	private String rut;
	private List<Route> routes;
	
	public Company(){}

	public Company(int idCompany, String name, String rut, List<Route> routes) {
		this.idCompany = idCompany;
		this.name = name;
		this.rut = rut;
		this.routes=routes;
	}

	public int getIdCompany() {
		return idCompany;
	}

	public String getName() {
		return name;
	}

	public String getRut() {
		return rut;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setIdCompany(int idCompany) {
		this.idCompany = idCompany;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
}
