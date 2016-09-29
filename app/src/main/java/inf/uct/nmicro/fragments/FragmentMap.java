package inf.uct.nmicro.fragments;

/**
 * Created by jairo on 9/28/2016.
 */
import android.os.Bundle;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import inf.uct.nmicro.R;
import inf.uct.nmicro.utils.AdapterCategory;
import inf.uct.nmicro.utils.Category;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class FragmentMap extends Fragment {

    private MapController   mapController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ArrayList<Category> category = new ArrayList<Category>();

        Category cat = new Category("Recorrido","1C 1A","micro que va al centro",getResources().getDrawable(R.drawable.ic_1a));
        category.add(cat);
        cat = new Category("Recorrido","1C 1A","micro que va al centro",getResources().getDrawable(R.drawable.ic_1a));
        category.add(cat);
        cat = new Category("Recorrido","1C 1A","micro que va al centro",getResources().getDrawable(R.drawable.ic_1a));
        category.add(cat);

        View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);


        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        MapView map = (MapView) rootView.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = (MapController) map.getController();
        mapController.setZoom(14);
        //-38.7392,-72.6087?z=14
        GeoPoint Temuco = new GeoPoint(-38.7392, -72.6087);
        mapController.setCenter(Temuco);
        final MyLocationNewOverlay myLocationoverlay = new MyLocationNewOverlay(this.getActivity(), map);
        myLocationoverlay.enableMyLocation();
        map.getOverlays().add(myLocationoverlay); //No añadir si no quieres una marca

        myLocationoverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapController.animateTo(myLocationoverlay.getMyLocation());
            }
        });


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
}

