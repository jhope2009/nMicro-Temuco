package inf.uct.nmicro.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.api.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import inf.uct.nmicro.MainActivity;
import inf.uct.nmicro.R;
import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.model.Instruction;
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.model.Stop;
import inf.uct.nmicro.model.Travel;
import inf.uct.nmicro.sqlite.DataBaseHelper;
import inf.uct.nmicro.fragments.CustomMarker;
import inf.uct.nmicro.utils.AdapterRoute;
import inf.uct.nmicro.utils.ConnectWS;

/**
 * Created by Esteban Campos A on 02-11-2016.
 */

/*
Clase creada con el objetivo de mostrar la informacion del viaje del usuarios al momento de seleccionar una ruta simple
o compuesta.
 */

public class traveling extends Activity {

    private FABToolbarLayout morph;
    private MapController mMapController;
    private MapController mapController;
    private List<CustomMarker> Markers_stop;
    ArrayList<Integer> getRoutes;
    ArrayList<String> getDirections;
    private DataBaseHelper myDbHelper;
    private LinearLayout animado;
    DrawInMap DrawinMap = new DrawInMap();
    PathOverlay pathO;
    MapView map;
    SearchTraveling Straveling=new SearchTraveling();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recibo los datos del main_activity
        Intent intent = getIntent();
        getRoutes = intent.getIntegerArrayListExtra(MainActivity.rutasSeleccionadas);
        getDirections=intent.getStringArrayListExtra(MainActivity.rutasSeleccionadas2);
        //inicio el asist de base de datos.
        myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.NoCheckCreateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Company> companies=myDbHelper.findCompanies();

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
        //paso los dos rutas desde el activity armo el viaje de nuevo y no ocupo la base  de datos.
        List<Route> NewViaje= FindRoutes(companies,getRoutes);
        Travel viaje=Straveling.GetTheTravel(NewViaje,getDirections);
        Log.i("El nombre del viaje",viaje.getname());
        for(Route r :viaje.getRoutes()){
            Log.i("rutas del viaje",r.getName());
        }
        for(Instruction instruction :viaje.getInstructions()) {
            Log.i("instrucciones del viaje", instruction.getIndication());
        }




        animado = (LinearLayout) findViewById(R.id.fabtoolbar_toolbar);
        Drawable icon = this.getResources().getDrawable(R.drawable.ic_bustop);
        //Markers_stop = DrawinMap.DrawStopsByRoute(rutas, myDbHelper, icon, map);

        for (CustomMarker mak : Markers_stop) {
            mak.setOnMarkerClickListener(new org.osmdroid.views.overlay.Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(org.osmdroid.views.overlay.Marker marker, MapView mapView) {
                    //createListWithAdapter(myDbHelper.findRoutesByStop(mak.getIdMarker()),0);
                    Toast.makeText(getApplication(), ""+getRoutes.toString(), Toast.LENGTH_SHORT).show();
                    //aqui llamo a la clase que realiza la coneccion con el WS y le paso parametros concatenados por coma
                    //new ConnectWS(getApplication(),animado,0).execute(getRoutes.get(0)+","+mak.getPosition().getLatitude()+","+mak.getPosition().getLongitude());
                    return false;
                }
            });
        }

    }
    public List<Route> FindRoutes(List<Company> co, List<Integer> ru) {
        List<Route> rutas = new ArrayList<>();
        for (Company c : co) {
            for (Route r : c.getRoutes()) {
                for (Integer i : ru) {
                    if (i== r.getIdRoute()) {
                        rutas.add(r);
                    }
                }
            }
        }
        return rutas;
    }
}



