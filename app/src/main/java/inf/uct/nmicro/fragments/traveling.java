package inf.uct.nmicro.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.api.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import inf.uct.nmicro.MainActivity;
import inf.uct.nmicro.R;
import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.model.Stop;
import inf.uct.nmicro.sqlite.DataBaseHelper;

/**
 * Created by Esteban Campos A on 02-11-2016.
 */

/*
Clase creada con el objetivo de mostrar la informacion del viaje del usuarios al momento de seleccionar una ruta simple
o compuesta.
 */

public class traveling extends Activity {

    org.osmdroid.views.MapView map;
    private MapController mMapController;
    int mIncr = 10000;
    ArrayList<Integer> getRoutes;
    List<Company> companias;
    private DataBaseHelper myDbHelper;
    List<Route> rutas;
    List<Stop> paradas;
    DrawInMap DrawinMap = new DrawInMap();
    PathOverlay pathO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //recibo los datos del main_activity
        pathO = new PathOverlay(Color.BLACK, 10, this);
        Intent intent = getIntent();
        getRoutes = intent.getIntegerArrayListExtra(MainActivity.rutasSeleccionadas);
        companias = myDbHelper.findCompanies();
        rutas=getAll(companias,getRoutes);
        paradas=myDbHelper.findStopByidRoute(rutas.get(0).getIdRoute());
        setContentView(R.layout.traveling);
        final MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        GeoPoint startPoint = new GeoPoint(-38.733738, -72.600148);
        IMapController mapController = map.getController();
        mapController.setZoom(16);
        mapController.setCenter(startPoint);
        List<CustomMarker> p2 = new ArrayList<CustomMarker>();
        DrawinMap.DrawRoute(map,rutas.get(0),pathO);
        for(Stop st : paradas){
                GeoPoint gp = new GeoPoint(st.getLatitude(), st.getLongitude());
                CustomMarker p1 = new CustomMarker(map);
              //  p1.setIcon(dra);
                p1.setPosition(gp);
                p1.setTitle(st.getAddress());
                p1.setIdMarker(st.getIdStop());
                p2.add(p1);
                map.getOverlays().add(p1);
            }
            map.invalidate();


        }


    public List<Route> getAll(List<Company> cp, ArrayList<Integer> idro) {
        List<Route> routes = new ArrayList<Route>();
        for (Company c : cp) {
            for (Route r : c.getRoutes()) {
                for (int i : idro) {
                    if (r.getIdRoute() == i) {
                        routes.add(r);
                    }
                }
            }
        }
        return routes;
    }
}