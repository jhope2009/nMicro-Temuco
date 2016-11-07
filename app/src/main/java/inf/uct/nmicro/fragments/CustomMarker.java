package inf.uct.nmicro.fragments;

import android.content.Context;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by Javier on 25-10-2016.
 */

public class CustomMarker extends Marker {

    private int idMarker;

    public CustomMarker(MapView mapView) {
        super(mapView);
    }

    public CustomMarker(MapView mapView, Context resourceProxy) {
        super(mapView, resourceProxy);
    }

    public int getIdMarker() {
        return idMarker;
    }

    public void setIdMarker(int idMArker) {
        this.idMarker = idMArker;
    }
}
