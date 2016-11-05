package inf.uct.nmicro.fragments;

/**
 * Created by Esteban Campos A on 24-10-2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import inf.uct.nmicro.MainActivity;
import inf.uct.nmicro.R;
import inf.uct.nmicro.model.Point;
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.model.Stop;
import inf.uct.nmicro.sqlite.DataBaseHelper;

public class DrawInMap extends Activity {

    public List<CustomMarker> Draw_Stops(MapView map, DataBaseHelper myDbHelper, Drawable dra){
        ArrayList<Stop> stops = myDbHelper.findAllStops();
        List<CustomMarker> p2 = new ArrayList<CustomMarker>();
        for (Stop st : stops) {
            GeoPoint gp = new GeoPoint(st.getLatitude(), st.getLongitude());
            CustomMarker p1 = new CustomMarker(map);
            p1.setIcon(dra);
            p1.setPosition(gp);
            p1.setTitle(st.getAddress());
            p1.setIdMarker(st.getIdStop());
            p2.add(p1);
            map.getOverlays().add(p1);
        }
        map.invalidate();
        return p2;
    }
    public List<Address> findLocationByAddress(String text, Geocoder geocoder, Context contexto){
        List<Address> direcciones = new ArrayList<Address>();
        try{
            direcciones = geocoder.getFromLocationName(text,3);
            if(direcciones ==null){
                Toast.makeText(contexto,"No se encontro Direccion", Toast.LENGTH_LONG).show();
            }
            else{
                return direcciones;
            }
            return direcciones;
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(contexto,"NO se cargo direccion:" +text, Toast.LENGTH_LONG).show();
            return direcciones;
        }
    }

    public void DrawRoute(MapView map, Route route, PathOverlay routesDraw) {
        routesDraw.clearPath();
            for (Point pto : route.getPoints()) {
            GeoPoint gp = new GeoPoint(pto.getLatitude(), pto.getLongitude());

                routesDraw.addPoint(gp);

        }
        map.getOverlayManager().add(routesDraw);
        map.invalidate();
    }

    public void DrawFindLocation(List<Address> ub1, List<Address> ub2, MapView map, PathOverlay routesDraw, Context contexto){
        if(ub1!=null && ub2 !=null) {
            routesDraw.clearPath();
            Address ad = ub1.get(0);
            Address ad1 = ub2.get(0);
            Log.i(String.valueOf(ad.getLatitude()),"datos recibidos: "+ String.valueOf(ad.getLatitude()) + String.valueOf(ad.getLongitude()));
            GeoPoint punto1 = new GeoPoint(ad.getLatitude(), ad.getLongitude());
            GeoPoint punto2 = new GeoPoint(ad1.getLatitude(), ad1.getLongitude());
            routesDraw.addPoint(punto1);
            routesDraw.addPoint(punto2);
            map.getOverlayManager().add(routesDraw);
            map.invalidate();
        }
        else
            if(ub2==null){
                Toast.makeText(contexto, "Debes introducir una direccion valida", Toast.LENGTH_LONG).show();
            }
    }
    public void drawStopsSelected(List<Stop> a){

    }



}//final de la clase