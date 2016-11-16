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
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.model.Stop;
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

    private DataBaseHelper myDbHelper;
    private LinearLayout animado;
    DrawInMap DrawinMap = new DrawInMap();
    PathOverlay pathO;
    MapView map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recibo los datos del main_activity
        Intent intent = getIntent();
        getRoutes = intent.getIntegerArrayListExtra(MainActivity.rutasSeleccionadas);
        //inicio el asist de base de datos.
        myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.NoCheckCreateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        List<Company> companies = myDbHelper.findCompanies();
        List<Route> rutas = FindRoutes(companies, getRoutes);
        animado = (LinearLayout) findViewById(R.id.fabtoolbar_toolbar);
        Drawable icon = this.getResources().getDrawable(R.drawable.ic_bustop);
        DrawinMap.DrawStopsByRoute(rutas, myDbHelper, icon, map);

        for (CustomMarker mak : Markers_stop) {
            mak.setOnMarkerClickListener(new org.osmdroid.views.overlay.Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(org.osmdroid.views.overlay.Marker marker, MapView mapView) {
                    /*
                    *
                    *
                    String username = usernameField.getText().toString();
                    String password = passwordField.getText().toString();
                    method.setText("Get Method");
                    new SinginActivity(this,status,role,0).execute(username,password);
                    *
                    * */
                    //createListWithAdapter(myDbHelper.findRoutesByStop(mak.getIdMarker()),0);
                    //aqui llamo a la clase que realiza la coneccion con el WS y le paso parametros concatenados por coma
                    new ConnectWS(getApplication(),animado,0).execute(getRoutes.get(0)+","+mak.getPosition().getLatitude()+","+mak.getPosition().getLongitude());
                    return false;
                }
            });
        }
        for(Route r : rutas){
            DrawinMap.DrawRoute(map,r,pathO);
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



