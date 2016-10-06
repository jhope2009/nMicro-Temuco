package inf.uct.nmicro.sqlite;

import java.util.UUID;

public class ITablesDB {

	interface Tables {
		String ROUTE = "route";
		String BUS = "bus";
		String STOP = "stop";
		String COMPANY = "company";
		String POINT = "point";
	}

	interface ColumnsRoute{
		String ID_ROUTE = "id_route";
		String ID_COMPANY = "id_company";
		String NAME = "name";
	}
	
	interface ColumnsBus{
		String ID_BUS = "id_bus";
		String ID_ROUTE_GOING = "id_route_going";
		String ID_ROUTE_RETURN = "id_route_return";
		String REGISTER = "register";
	}
	
	interface ColumnsStop{
		String ID_STOP = "id_stop";
		String ADDRESS = "address";
		String ID_POINT = "id_point";
	}
	
	interface ColumnsCompany{
		String ID_COMPANY = "id_company";
		String NAME = "name";
		String RUT = "rut";
	}
	
	interface ColumnsPoint{
		String ID_POINT = "id_point";
		String LATITUDE = "latitude";
		String LONGITUDE = "longitude";
		String ID_ROUTE = "id_route";
	}

	public static class Routes implements ColumnsRoute{	}
	public static class Buses implements ColumnsBus{ }
	public static class Stops implements ColumnsStop{ }
	public static class Companies implements ColumnsCompany{ }
	public static class Points implements ColumnsPoint{	}

}
