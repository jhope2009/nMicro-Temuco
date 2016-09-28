package inf.uct.nmicro.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;
import inf.uct.nmicro.sqlite.ITablesDB.Buses;
import inf.uct.nmicro.sqlite.ITablesDB.Companies;
import inf.uct.nmicro.sqlite.ITablesDB.Points;
import inf.uct.nmicro.sqlite.ITablesDB.Routes;
import inf.uct.nmicro.sqlite.ITablesDB.RoutesPoints;
import inf.uct.nmicro.sqlite.ITablesDB.RoutesStops;
import inf.uct.nmicro.sqlite.ITablesDB.Stops;

/**
 * @author Javier
 *
 */
/**
 * Clase que administra la conexion de la base de datos y su estructuracion
 */
public class ConnectionDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "pedidos.db";
    private static final int ACTUAL_VERSION = 1;
    private final Context context;

    interface Tables {
        String ROUTE = "route";
        String BUS = "bus";
        String STOP = "stop";
        String COMPANY = "company";
        String POINT = "point";
        String ROUTE_POINT = "route_point";
        String ROUTE_STOP = "route_stop";
    }

    interface Referencies {

        String ID_ROUTE = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                Tables.ROUTE, Routes.ID_ROUTE);

        String ID_BUS = String.format("REFERENCES %s(%s)",
                Tables.BUS, Buses.ID_BUS);

        String ID_STOP = String.format("REFERENCES %s(%s)",
                Tables.STOP, Stops.ID_STOP);

        String ID_COMPANY = String.format("REFERENCES %s(%s)",
                Tables.COMPANY, Companies.ID_COMPANY);
        
        String ID_POINT = String.format("REFERENCES %s(%s)",
                Tables.POINT, Points.ID_POINT);        
    }

    public ConnectionDB(Context context) {
        super(context, DB_NAME, null, ACTUAL_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER UNIQUE NOT NULL,%s TEXT NOT NULL," +
                        "%s INTEGER NOT NULL %s)",
                Tables.ROUTE, BaseColumns._ID,
                Routes.ID_ROUTE, Routes.NAME, 
                Routes.ID_COMPANY, Referencies.ID_COMPANY));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER UNIQUE NOT NULL,%s INTEGER NOT NULL %s,%s INTEGER NOT NULL %s," +
                        "%s TEXT NOT NULL)",
                Tables.BUS, BaseColumns._ID,
                Buses.ID_BUS, Buses.ID_ROUTE_GOING, Referencies.ID_ROUTE,
                Buses.ID_ROUTE_RETURN, Referencies.ID_ROUTE, Buses.REGISTER));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER NOT NULL UNIQUE,%s TEXT NOT NULL,%s TEXT NOT NULL)",
                Tables.COMPANY, BaseColumns._ID,
                Companies.ID_COMPANY, Companies.NAME, Companies.RUT));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER NOT NULL UNIQUE,%s TEXT NOT NULL,%s INTEGER NOT NULL %s)",
                Tables.STOP, BaseColumns._ID,
                Stops.ID_STOP, Stops.ADDRESS, Stops.ID_POINT, Referencies.ID_POINT));

        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER NOT NULL UNIQUE,%s REAL NOT NULL,%s REAL NOT NULL)",
                Tables.POINT, BaseColumns._ID,
                Points.ID_POINT, Points.LATITUDE, Points.LONGITUDE));
        
        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                		"%s INTEGER NOT NULL %s,%s INTEGER NOT NULL %s)",
		        Tables.ROUTE_STOP, BaseColumns._ID,
		        RoutesStops.ID_ROUTE, Referencies.ID_ROUTE,
		        RoutesStops.ID_STOP, Referencies.ID_STOP));
        
        db.execSQL(String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                		"%s INTEGER NOT NULL %s,%s INTEGER NOT NULL %s)",
		        Tables.ROUTE_POINT, BaseColumns._ID,
		        RoutesPoints.ID_ROUTE, Referencies.ID_ROUTE,
		        RoutesPoints.ID_POINT, Referencies.ID_POINT));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tables.ROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.BUS);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.POINT);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.STOP);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.ROUTE_POINT);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.ROUTE_STOP);

        onCreate(db);
    }


}
