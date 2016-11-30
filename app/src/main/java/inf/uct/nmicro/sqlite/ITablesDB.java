package inf.uct.nmicro.sqlite;

import java.util.UUID;

public class ITablesDB {

	interface Tables {
		String ROUTE = "route";
		String BUS = "bus";
		String STOP = "stop";
		String COMPANY = "company";
		String POINT = "point";
		String STOP_ROUTE = "stop_routes";
		String TRAVEL = "travel";
        String TRAVEL_ROUTE = "travel_route";
        String INSTRUCTION = "instruction";
	}

	interface ColumnsRoute{
		String ID_ROUTE = "id_route";
		String ID_COMPANY = "id_company";
		String NAME = "name";
		String ICON = "icon";
	}

	interface ColumnsStop{
		String ID_STOP = "id_stop";
		String ADDRESS = "address";
		String LATITUDE = "latitude";
		String LONGITUDE = "longitude";
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

	interface ColumnsStopRoute{
		String ID_STOP_ROUTE="id_stop_route";
		String ID_STOP="id_stop";
		String ID_ROUTE="id_route";
	}

	interface ColumnsTravel{
		String ID_TRAVEL="id_travel";
		String NAME="name";
		String PRICE="price";
		String START_STOP="start_stop";
		String END_STOP="end_stop";
	}

    interface ColumnsTravelRoute{
        String ID_TRAVEL="id_travel";
        String ID_ROUTE="id_route";
    }

    interface ColumnsInstruction{
        String INDICATION="indication";
        String STOP="stop";
        String HOUR="hour";
        String ID_TRAVEL="id_travel";
    }

	public static class Routes implements ColumnsRoute{	}
	public static class Stops implements ColumnsStop{ }
	public static class Companies implements ColumnsCompany{ }
	public static class Points implements ColumnsPoint{	}
	public static class StopRoute implements ColumnsStopRoute{ }
	public static class Travels implements ColumnsTravel{ }
    public static class TravelRoutes implements ColumnsTravelRoute{}
    public static class Instructions implements ColumnsInstruction{}

}
