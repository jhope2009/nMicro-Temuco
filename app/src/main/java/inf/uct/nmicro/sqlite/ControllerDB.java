package inf.uct.nmicro.sqlite;

/**
 * Created by Javier on 28-09-2016.
 */

import android.content.Context;

/**
 * Clase auxiliar que implementa ConnectionDB para llevar a cabo el CRUD
 * sobre las entidades existentes.
 */
public final class ControllerDB {

    private static ConnectionDB connectionDB;

    private static ControllerDB controllerDB = new ControllerDB();

    private ControllerDB() {}

    public static ControllerDB getControllerDB(Context contexto) {
        if (connectionDB == null) {
            connectionDB = new ConnectionDB(contexto);
        }
        return controllerDB;
    }
}
