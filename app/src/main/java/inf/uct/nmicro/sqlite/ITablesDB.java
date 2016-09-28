package inf.uct.nmicro.sqlite;

import java.util.UUID;

/**
 * @author Javier
 *
 *
 * Clase que establece los nombres a usar en la base de datos
 */

public class ITablesDB {
	
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
	}
	
	interface ColumnsRouteStop{
		String ID_ROUTE = "id_route";
		String ID_STOP = "id_stop";
	}
	
	interface ColumnsRoutePoint{
		String ID_ROUTE = "id_route";
		String ID_POINT = "id_point";
	}
	
	public static class Routes implements ColumnsRoute{
		public static String generateIdRoute(){
			return "R-" + UUID.randomUUID().toString();
		}
	}
	
	public static class Buses implements ColumnsBus{
		public static String generateIdBus(){
			return "B-" + UUID.randomUUID().toString();
		}
	}
	
	public static class Stops implements ColumnsStop{
		public static String generateIdStop(){
			return "R-" + UUID.randomUUID().toString();
		}
	}
	
	public static class Companies implements ColumnsCompany{
		public static String generateIdCompany(){
			return "R-" + UUID.randomUUID().toString();
		}
	}
	
	public static class Points implements ColumnsPoint{
		public static String generateIdPoint(){
			return "R-" + UUID.randomUUID().toString();
		}
	}
	
	public static class RoutesStops implements ColumnsRouteStop{}
	
	public static class RoutesPoints implements ColumnsRoutePoint{}
	
}
