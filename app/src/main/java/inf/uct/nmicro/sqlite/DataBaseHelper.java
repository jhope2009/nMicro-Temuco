package inf.uct.nmicro.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.model.Point;
import inf.uct.nmicro.model.Stop;
import inf.uct.nmicro.sqlite.ITablesDB.Tables;
import inf.uct.nmicro.sqlite.ITablesDB.Routes;
import inf.uct.nmicro.sqlite.ITablesDB.Companies;
import inf.uct.nmicro.sqlite.ITablesDB.Points;
import inf.uct.nmicro.sqlite.ITablesDB.StopRoute;
import inf.uct.nmicro.model.Company;

public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/inf.uct.nmicro/databases/";
    private static String DB_NAME = "MicroDB.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void NoCheckCreateDataBase() throws IOException {

        try {
            this.getReadableDatabase();
            copyDataBase();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }

    }
    /*
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.

            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }
    */

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteCantOpenDatabaseException e) {
            throw new Error("database does't exist yet.");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;

    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {
        System.out.println("Creating database");
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        System.out.println("Database Created");

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /*public List<Company> GetAllCompany() {
        System.out.println("Geting companys");
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("select * from company", null);
        oList<Company> Lineas = new ArrayList<Company>();
        cursor.mveToFirst();
        while (!cursor.isAfterLast()) {
            Lineas.add(new Company(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }
        return Lineas;
    }*/

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
    /*
        Obtiene recorridos asociados a un linea (company)
     */
    public List<Route> findRoutesByCompany(int idCompany) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        String sql = String.format("SELECT %s, %s, %s FROM %s WHERE %s=?", Routes.ID_ROUTE, Routes.NAME,
                Routes.ICON, Tables.ROUTE, Routes.ID_COMPANY);
        String[] selectionArgs = {Integer.toString(idCompany)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<Route> routes = new ArrayList<Route>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            routes.add(new Route(cursor.getInt(0), cursor.getString(1), findPointsByRoute(cursor.getInt(0)), cursor.getString(2)));
            cursor.moveToNext();
        }
        return routes;
    }

    /*
        Obtiene los puntos asociados a un recorrido
     */
    public List<Point> findPointsByRoute(int idRoute) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        String sql = String.format("SELECT %s, %s, %s FROM %s WHERE %s=?", Points.ID_POINT,
                Points.LATITUDE, Points.LONGITUDE, Tables.POINT, Points.ID_ROUTE);
        String[] selectionArgs = {Integer.toString(idRoute)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<Point> points = new ArrayList<Point>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            points.add(new Point(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2)));
            cursor.moveToNext();
        }
        return points;
    }

    /*
        Obtiene todos las lineas (companies)
    */
    public List<Company> findCompanies() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        String sql = String.format("SELECT * FROM %s", Tables.COMPANY);
        Cursor cursor = db.rawQuery(sql, null);
        List<Company> companies = new ArrayList<Company>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            companies.add(new Company(cursor.getInt(0), cursor.getString(1), cursor.getString(2), findRoutesByCompany(cursor.getInt(0))));
            cursor.moveToNext();
        }
        return companies;
    }

    /*
        Obtiene todos las lineas (companies)
    */
    public List<Company> findCompaniesById(int idCompany) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        String sql = String.format("SELECT * FROM %s WHERE %s=?", Tables.COMPANY, Companies.ID_COMPANY);
        String[] selectionArgs = {Integer.toString(idCompany)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<Company> companies = new ArrayList<Company>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            companies.add(new Company(cursor.getInt(0), cursor.getString(1), cursor.getString(2), findRoutesByCompany(cursor.getInt(0))));
            cursor.moveToNext();
        }
        return companies;
    }

    public ArrayList<Stop> findAllStops() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        String sql = String.format("SELECT * FROM %s", Tables.STOP);
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<Stop> stops = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            stops.add(new Stop(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3)));
            cursor.moveToNext();
        }
        return stops;
    }

    public List<Stop> findStopByidRoute(int idroute){
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        String sql = String.format("SELECT * FROM %s WHERE %s=?", Tables.STOP);
        String[] selectionArgs = {Integer.toString(idroute)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        ArrayList<Stop> stops = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            stops.add(new Stop(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3)));
            cursor.moveToNext();
        }
        return stops;

    }


    public List<Route> findRoutesByStop(int idStop) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        String sql = String.format("SELECT * FROM %s INNER JOIN %s ON %s = %s WHERE %s = ?",
                Tables.STOP_ROUTE, Tables.ROUTE, Tables.STOP_ROUTE+"."+StopRoute.ID_ROUTE, Tables.ROUTE+"."+Routes.ID_ROUTE, StopRoute.ID_STOP);
        String[] selectionArgs = {Integer.toString(idStop)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<Route> routes = new ArrayList<Route>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            routes.add(new Route(cursor.getInt(3), cursor.getString(5), findPointsByRoute(cursor.getInt(3)), cursor.getString(6)));
            cursor.moveToNext();
        }
        return routes;
    }



}