package inf.uct.nmicro.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import inf.uct.nmicro.fragments.CustomMarker;

/**
 * Created by Esteban Campos A on 02-11-2016.
 */

/*
Clase creada con el objetivo de mostrar la informacion del viaje del usuarios al momento de seleccionar una ruta simple
o compuesta.
 */

public class traveling extends Activity {

    //org.osmdroid.views.MapView map;
    private MapController mMapController;
    private MapController mapController;
    int mIncr = 10000;
    ArrayList<Integer> getRoutes;
    private List<Company> companias;
    private DataBaseHelper myDbHelper;
    List<Route> rutas;
    List<Stop> paradas;
    DrawInMap DrawinMap = new DrawInMap();
    PathOverlay pathO;
    MapView map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.NoCheckCreateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //recibo los datos del main_activity
        pathO = new PathOverlay(Color.BLACK, 10, this);

        setContentView(R.layout.traveling);
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(android.support.v4.BuildConfig.APPLICATION_ID);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        mapController = (MapController) map.getController();
        mapController.setZoom(15);
        GeoPoint Temuco = new GeoPoint(-38.7392, -72.6087);
        mapController.setCenter(Temuco);

        Intent intent = getIntent();
        getRoutes = intent.getIntegerArrayListExtra(MainActivity.rutasSeleccionadas);
        List<Company> companies = myDbHelper.findCompanies();
        List<Stop> paradas;
        List<Route> rutas=FindRoutes(companies,getRoutes);

        for(Route r : rutas) {
            if(!r.getStops().isEmpty()) {
                for (Stop st : r.getStops()) {
                    CustomMarker p1=new CustomMarker(map);
                    GeoPoint gp = new GeoPoint(st.getLatitude(), st.getLongitude());
                    p1.setPosition(gp);
                    p1.setIcon(this.getResources().getDrawable(R.drawable.ic_bustop));
                    p1.setPosition(gp);
                    p1.setTitle(st.getAddress());
                   // p1.setIdMarker(st.getIdStop());
                    map.getOverlays().add(p1);
                }

            }
              DrawinMap.DrawRoute(map,r,pathO);
        }
        map.invalidate();
    }


    public List<Route> FindRoutes(List<Company> co, List<Integer> ru){
        List<Route> rutas=new ArrayList<>();
        for(Company c :co){
            for(Route r : c.getRoutes()){
                for(Integer i :ru){
                    if(i==r.getIdRoute()){rutas.add(r);}}
            }
        }
    return rutas;
    }

}
