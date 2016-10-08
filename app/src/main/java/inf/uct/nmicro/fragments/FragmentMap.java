package inf.uct.nmicro.fragments;

/**
 * Created by jairo on 9/28/2016.
 */
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import inf.uct.nmicro.R;
import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.sqlite.DataBaseHelper;
import inf.uct.nmicro.utils.AdapterCategory;
import inf.uct.nmicro.utils.Category;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class FragmentMap extends Fragment {

    private MapController   mapController;
    private ArrayList<Category> category = new ArrayList<Category>();
    private List<Company> Lineas;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);

        //seccion que carga reccorridos de la base de datos
        DataBaseHelper myDbHelper = new DataBaseHelper(this.getActivity());
        try {
            myDbHelper.NoCheckCreateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Lineas = myDbHelper.findCompanies();

        for(Company linea : Lineas){
            Category cat = new Category("Recorrido",linea.getName(),"micro que va al centro",getResources().getDrawable(R.drawable.ic_1a));
            category.add(cat);
        }

        //seccion que carga el mapa y lo configura
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        MapView map = (MapView) rootView.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        //map.setUseDataConnection(false);

        //logica control del mapa
        mapController = (MapController) map.getController();
        mapController.setZoom(14);
        GeoPoint Temuco = new GeoPoint(-38.7392, -72.6087);
        mapController.setCenter(Temuco);
        //final MyLocationNewOverlay myLocationoverlay = new MyLocationNewOverlay(this.getActivity(), map);
        //myLocationoverlay.enableMyLocation();
        //map.getOverlays().add(myLocationoverlay); //No añadir si no quieres una marca

        //myLocationoverlay.runOnFirstFix(new Runnable() {
        //    public void run() {
        //        mapController.animateTo(myLocationoverlay.getMyLocation());
        //    }
        //});



        Overlay touchOverlay = new Overlay(this.getContext()){
            ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = null;
            @Override
            protected void draw(Canvas arg0, MapView arg1, boolean arg2) {

            }
            @Override
            public boolean onSingleTapConfirmed(final MotionEvent e, final MapView mapView) {

                final Drawable marker = getActivity().getResources().getDrawable(R.drawable.marker_default);
                Projection proj = mapView.getProjection();
                GeoPoint loc = (GeoPoint) proj.fromPixels((int)e.getX(), (int)e.getY());
                String longitude = Double.toString(((double)loc.getLongitudeE6())/1000000);
                String latitude = Double.toString(((double)loc.getLatitudeE6())/1000000);

                Toast toast = Toast.makeText(getActivity(), "Latitud = " + latitude + ", Longitud = " + longitude , Toast.LENGTH_SHORT);
                toast.show();

                ArrayList<OverlayItem> overlayArray = new ArrayList<OverlayItem>();
                OverlayItem mapItem = new OverlayItem("", "", new GeoPoint((((double)loc.getLatitudeE6())/1000000), (((double)loc.getLongitudeE6())/1000000)));
                mapItem.setMarker(marker);
                overlayArray.add(mapItem);
                if(anotherItemizedIconOverlay==null){
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getActivity(), overlayArray,null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                    mapView.invalidate();
                }else{
                    mapView.getOverlays().remove(anotherItemizedIconOverlay);
                    mapView.invalidate();
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getActivity(), overlayArray,null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                }
                //      dlgThread();
                return true;
            }
        };

        map.getOverlays().add(touchOverlay);


        ListView lv = (ListView) rootView.findViewById(R.id.ListView);
        AdapterCategory adapter = new AdapterCategory(this.getActivity(), category);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                //CODIGO AQUI

            }
        });
        lv.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

