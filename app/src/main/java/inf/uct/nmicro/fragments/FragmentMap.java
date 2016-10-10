package inf.uct.nmicro.fragments;

/**
 * Created by jairo on 9/28/2016.
 */
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import inf.uct.nmicro.MainActivity;
import inf.uct.nmicro.R;
import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.model.Point;
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.sqlite.DataBaseHelper;
import inf.uct.nmicro.sqlite.ITablesDB;
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
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class FragmentMap extends Fragment {

    private MapController mapController;
    private ArrayList<Category> category = new ArrayList<Category>();
    private List<Company> Lineas;
    private List<Route> routes;
    private final int POSITION_DIAMETER = 150;
    private Category cat;
    private View rootView;
    private DataBaseHelper myDbHelper;
    private List<Point> points;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_mapa, container, false);

        //seccion que carga reccorridos de la base de datos
        myDbHelper = new DataBaseHelper(this.getActivity());
        try {
            myDbHelper.NoCheckCreateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //seccion que carga el mapa y lo configura
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        MapView map = (MapView) rootView.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
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

                GeoPoint geoPointUser = new GeoPoint(Double.parseDouble(latitude),Double.parseDouble(longitude));
                findNearRoutes(geoPointUser);
                createListWithAdapter();

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
        createListWithAdapter();

        return rootView;
    }

    public boolean isRouteInArea(Route route, GeoPoint geoPoint){
        for(Point p : route.getPoints()){
            int distance = new GeoPoint(p.getLatitude(),p.getLongitude()).distanceTo(geoPoint);
            if(distance<POSITION_DIAMETER) return true;
        }
        return false;
    }

    public void findNearRoutes(GeoPoint geoPointUser){
        List<Company> companies = myDbHelper.findCompanies();
        routes = new ArrayList<Route>();

        for(Company c : companies){
            for(Route r : c.getRoutes()){
                if(isRouteInArea(r, geoPointUser)){
                    routes.add(r);
                }
            }
        }
    }

    private void createListWithAdapter(){
        category = new ArrayList<Category>();
        if(routes!=null && !routes.isEmpty()){
            for(Route route : routes){
                cat = new Category("Recorrido", route.getName() + "", "micro que va al centro", getResources().getDrawable(R.drawable.ic_1a));
                category.add(cat);
            }
        }
        Lineas = myDbHelper.findCompanies();
        ListView lv = (ListView) rootView.findViewById(R.id.ListView);
        AdapterCategory adapter = new AdapterCategory(this.getActivity(), category);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                //CODIGO AQUI
                routes.get(pos);
                String nombres=routes.get(pos).getName();
                points=myDbHelper.findPointsByRoute(routes.get(pos).getIdRoute());
                String colores="IDA";
                PathOverlay ruta=new PathOverlay(Color.BLUE, getContext());
                for(Point pto : points){
                    GeoPoint gp=new GeoPoint(pto.getLatitude(),pto.getLongitude());
                    ruta.addPoint(gp);
                }

                MapView map = (MapView) rootView.findViewById(R.id.map);
                map.getOverlays().add(ruta);
                Toast.makeText(getContext(),nombres, Toast.LENGTH_SHORT).show();

            }
        });
        lv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

