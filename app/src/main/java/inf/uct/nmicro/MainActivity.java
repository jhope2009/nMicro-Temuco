package inf.uct.nmicro;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.model.Point;
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.sqlite.DataBaseHelper;
import inf.uct.nmicro.utils.AdapterRoute;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int route = -1;
    private int[] tabIcons={
            R.drawable.ic_maps_map,
            R.drawable.ic_recorrido,
            R.drawable.ic_toggle_star,
            R.drawable.ic_rutas2
    };
    private MapController mapController;
    private ArrayList<Route> category = new ArrayList<Route>();
    private List<Company> Lineas;
    private List<Route> routes;
    private final int POSITION_DIAMETER = 150;

    private View rootView;
    private DataBaseHelper myDbHelper;
    private ListView lv;
    private AdapterRoute adapter;
    private Route cat;
    private List<Point> points;
    private List<Route> allRoutes;
    MapView map;
    private FABToolbarLayout morph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsin879gToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        morph = (FABToolbarLayout) findViewById(R.id.fabtoolbar);

        morph.hide();
        fab.setOnClickListener(this);


        myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.NoCheckCreateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //seccion que carga el mapa y lo configura
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(android.support.v4.BuildConfig.APPLICATION_ID);
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        mapController = (MapController) map.getController();
        mapController.setZoom(15);
        GeoPoint Temuco = new GeoPoint(-38.7392, -72.6087);
        mapController.setCenter(Temuco);

        /*
        //logica control del mapa
        mapController = (MapController) map.getController();
        mapController.setZoom(14);
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
*/

        Overlay touchOverlay = new Overlay(this) {
            ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = null;

            @Override
            protected void draw(Canvas arg0, MapView arg1, boolean arg2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongPress(final MotionEvent e, final MapView mapView) {

                final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.marker_default);
                Projection proj = mapView.getProjection();
                GeoPoint loc = (GeoPoint) proj.fromPixels((int) e.getX(), (int) e.getY());
                String longitude = Double.toString(((double) loc.getLongitudeE6()) / 1000000);
                String latitude = Double.toString(((double) loc.getLatitudeE6()) / 1000000);

                GeoPoint geoPointUser = new GeoPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
                findNearRoutes(geoPointUser);

                createListWithAdapter();

                mapController.animateTo(geoPointUser);
                mapController.zoomTo(16);

                ArrayList<OverlayItem> overlayArray = new ArrayList<OverlayItem>();
                OverlayItem mapItem = new OverlayItem("", "", new GeoPoint((((double) loc.getLatitudeE6()) / 1000000), (((double) loc.getLongitudeE6()) / 1000000)));
                mapItem.setMarker(marker);
                overlayArray.add(mapItem);
                if (anotherItemizedIconOverlay == null) {
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                    mapView.invalidate();
                } else {
                    mapView.getOverlays().remove(anotherItemizedIconOverlay);
                    mapView.invalidate();
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray, null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                }
                //      dlgThread();
                return true;
            }
        };
        map.getOverlays().add(touchOverlay);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


}

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            morph.show();
        }

        morph.hide();
    }

    public boolean isRouteInArea(Route route, GeoPoint geoPoint) {
        for (Point p : route.getPoints()) {
            int distance = new GeoPoint(p.getLatitude(), p.getLongitude()).distanceTo(geoPoint);
            if (distance < POSITION_DIAMETER) return true;
        }
        return false;
    }

    public void findNearRoutes(GeoPoint geoPointUser) {
        List<Company> companies = myDbHelper.findCompanies();
        routes = new ArrayList<Route>();

        for (Company c : companies) {
            for (Route r : c.getRoutes()) {
                if (isRouteInArea(r, geoPointUser)) {
                    routes.add(r);
                }
            }
        }
    }

    public void findAllRoutes() {
        List<Company> companies = myDbHelper.findCompanies();
        allRoutes = new ArrayList<Route>();

        for (Company c : companies) {
            for (Route r : c.getRoutes()) {
                allRoutes.add(r);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createListWithAdapter() {
        category = new ArrayList<Route>();
        if (routes != null && !routes.isEmpty()) {

            for (Route route : routes) {
                cat = new Route(route.getIdRoute(), route.getName(), route.getStops(), route.getPoints(), getDrawable(R.drawable.ic_1a));
                category.add(cat);
            }
            morph.show();
        }
        ListView lv = (ListView) findViewById(R.id.ListView);
        ListView lv1 = (ListView) findViewById(R.id.ListView1);
        AdapterRoute adapter = new AdapterRoute(this, category);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                morph.hide();
                String nombres = routes.get(pos).getName();

                //DrawRoute(routes.get(pos));

                Toast.makeText(getApplicationContext(), nombres, Toast.LENGTH_SHORT).show();
            }
        });
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                morph.hide();
                String nombres = routes.get(pos).getName();

                //DrawRoute(routes.get(pos));

                Toast.makeText(getApplicationContext(), nombres, Toast.LENGTH_SHORT).show();
            }
        });
        lv.setAdapter(adapter);
        lv1.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (MainActivity.route != -1) {
            findAllRoutes();
            allRoutes.get(MainActivity.route);
            //DrawRoute(allRoutes.get(MainActivity.route));
        }
        Toast.makeText(this, "Now onStart() calls", Toast.LENGTH_LONG).show(); //onStart
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
