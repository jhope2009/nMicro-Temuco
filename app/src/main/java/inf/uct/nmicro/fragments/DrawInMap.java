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

    public void Draw_Stops(MapView map, DataBaseHelper myDbHelper, Drawable dra){
        ArrayList<Stop> stops = myDbHelper.findAllStops();
        for (Stop st : stops) {
            GeoPoint gp = new GeoPoint(st.getLatitude(), st.getLongitude());
            Marker p1 = new Marker(map);
            p1.setIcon(dra);
            p1.setPosition(gp);
            String title="- ";
            for(Route r : st.getRoutes()){title = title + r.getName() + "- ";}
            p1.setTitle(st.getAddress() + " : "+title);
            map.getOverlays().add(p1);
        }
        map.invalidate();
    }

    public List<Address> findLocationByAddress(String text, Geocoder geocoder){
        List<Address> direcciones;
        try{
            direcciones = geocoder.getFromLocationName(text,3);
            if(direcciones ==null){
                Toast.makeText(getApplicationContext(),"No se encontro Direccion", Toast.LENGTH_LONG).show();
            }
            else{
                return direcciones;
            }
            return direcciones;
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"NO se cargo direccion:" +text, Toast.LENGTH_LONG).show();
            return direcciones=null;
        }
    }

    //metodo que pinta las rutas.
    //se le pasa como parametro el mapa, la ruta y el Pathoverlay
    public void DrawRoute(MapView map, Route route, PathOverlay routesDraw) {
            for (Point pto : route.getPoints()) {
            GeoPoint gp = new GeoPoint(pto.getLatitude(), pto.getLongitude());
            routesDraw.addPoint(gp);
        }
        map.getOverlayManager().add(routesDraw);
        map.invalidate();
    }

    public void DrawFindLocation(MapView map, List<Address> ub1, List<Address> ub2){
        if(ub1!=null) {
            Address ad = ub1.get(0);
            Log.i(String.valueOf(ad.getLatitude()),"datos recibidos: "+ String.valueOf(ad.getLatitude()) + String.valueOf(ad.getLongitude()));
            System.out.print(ad.getLatitude() + ":" + ad.getLongitude());
            GeoPoint marcado = new GeoPoint(ad.getLatitude(), ad.getLongitude());
            Marker sele = new Marker(map);
            //sele.setTitle("la ubicacion 1: " + inicio);
            sele.setPosition(marcado);
            sele.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlayManager().add(sele);
        }
        if(ub2 !=null){
            Address ad1 = ub2.get(0);
            GeoPoint ma2 = new GeoPoint(ad1.getLatitude(), ad1.getLongitude());
            Marker sele2 = new Marker(map);
            sele2.setPosition(ma2);
            sele2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlayManager().add(sele2);
            map.invalidate();
        }
        else
            if(ub2==null){
                Toast.makeText(getApplicationContext(),"Debes introducir una direccion valida", Toast.LENGTH_LONG).show();
            }
    }

}//final de la clase