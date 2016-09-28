package inf.uct.nmicro.model;

/**
 * @author Javier
 *
 */
public class Company {

	private int idCompany;
	private String name;
	private String rut;
	
	public Company(){}

	/**
	 * @param idCompany
	 * @param name
	 * @param rut
	 */
	public Company(int idCompany, String name, String rut) {
		this.idCompany = idCompany;
		this.name = name;
		this.rut = rut;
	}

	/**
	 * @return the idCompany
	 */
	public int getIdCompany() {
		return idCompany;
	}

	/**
	 * @param idCompany the idCompany to set
	 */
	public void setIdCompany(int idCompany) {
		this.idCompany = idCompany;
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
	 * @return the rut
	 */
	public String getRut() {
		return rut;
	}

	/**
	 * @param rut the rut to set
	 */
	public void setRut(String rut) {
		this.rut = rut;
	}
	
}
