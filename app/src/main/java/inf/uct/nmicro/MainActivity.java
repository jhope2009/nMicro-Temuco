package inf.uct.nmicro;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.location.Location;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.osmdroid.google.wrapper.MyLocationOverlay;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import inf.uct.nmicro.fragments.CustomMarker;
import inf.uct.nmicro.fragments.DrawInMap;
import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.model.Point;
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.model.Stop;
import inf.uct.nmicro.sqlite.DataBaseHelper;
import inf.uct.nmicro.utils.AdapterRoute;
import inf.uct.nmicro.utils.ExpandableListAdapter;
import inf.uct.nmicro.utils.HeaderHolder;
import inf.uct.nmicro.utils.IconTreeItemHolder;
import inf.uct.nmicro.utils.PlaceHolderHolder;
import inf.uct.nmicro.utils.ProfileHolder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    public static int route = -1;
    Drawable icon1;

    private MapController mapController;
    private ArrayList<Route> category = new ArrayList<Route>();
    private final int POSITION_DIAMETER = 150;

    private DataBaseHelper myDbHelper;
    private Route cat;
    private List<Route> allRoutes;
    private List<Company> companie;
    MapView map;
    private FABToolbarLayout morph;
    DrawInMap DrawinMap = new DrawInMap();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Route>> listDataChild;
    PathOverlay routesDraw;
    PathOverlay MarkerDraw;

    private LinearLayout layout_menu;

    private AppBarLayout bar;
    private DrawerLayout drawer;
    private AdapterRoute adapter;
    private List<CustomMarker> Markers_stop;
    LocationManager locationManager;
    String mprovider;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText inicio = (EditText) findViewById(R.id.editText4);
        final EditText destino = (EditText) findViewById(R.id.editText3);
        Geocoder geocoder = new Geocoder(getApplication(), Locale.getDefault());

        layout_menu = (LinearLayout) findViewById(R.id.layout_menu);

        adapter = new AdapterRoute(this);

        myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.NoCheckCreateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prepareListData();
        Button butS = (Button) findViewById(R.id.button4);
        MarkerDraw = new PathOverlay(Color.BLACK, 10, this);


        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Route> compa= new ArrayList<Route>();
                if (destino.equals("Destino")) {
                    Toast.makeText(getApplicationContext(), "Debes ingresar destino.", Toast.LENGTH_LONG).show();
                }
                if (inicio.equals("Ubicación Actual")) {
                    Location loc= GetCurrentLocation();
                    try {
                        List<Address> ub0=geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(),1);
                        //List<Address> ub1 = DrawinMap.findLocationByAddress(inicio.getText().toString() + " Temuco, Araucania, Chile", geocoder);
                        List<Address> ub2 = DrawinMap.findLocationByAddress(destino.getText().toString() + " Temuco, Araucania, Chile", geocoder);
                        DrawinMap.DrawFindLocation(ub0, ub2, map, MarkerDraw);
                        GeoPoint pto1 = new GeoPoint(ub0.get(0).getLatitude(), ub0.get(0).getLongitude());
                        GeoPoint pto2 = new GeoPoint(ub2.get(0).getLatitude(), ub2.get(0).getLongitude());
                        List<Company> companies = myDbHelper.findCompanies();
                        //pregunto por punto
                        for (Company c : companies) {
                            for (Route r : c.getRoutes()) {
                                if (isRouteInArea(r, pto1) && isRouteInArea(r, pto2)) {
                                    int a = isRouteInArea2(r, pto1);
                                    int b = isRouteInArea2(r, pto2);
                                    if (a < b) {
                                        compa.add(r);
                                        createListWithAdapter(compa);
                                        Toast.makeText(getApplicationContext(), r.getName() + " Pasa cerca de los 2 puntos " + "orientacion del recorrido: ", Toast.LENGTH_LONG).show();
                                    }
                                }

                            }}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    List<Address> ub1 = DrawinMap.findLocationByAddress(inicio.getText().toString() + " Temuco, Araucania, Chile", geocoder);
                    List<Address> ub2 = DrawinMap.findLocationByAddress(destino.getText().toString() + " Temuco, Araucania, Chile", geocoder);
                    DrawinMap.DrawFindLocation(ub1, ub2, map, MarkerDraw);
                    GeoPoint pto1 = new GeoPoint(ub1.get(0).getLatitude(), ub1.get(0).getLongitude());
                    GeoPoint pto2 = new GeoPoint(ub2.get(0).getLatitude(), ub2.get(0).getLongitude());
                    List<Company> companies = myDbHelper.findCompanies();
                    //pregunto por punto
                    for (Company c : companies) {
                        for (Route r : c.getRoutes()) {
                            if (isRouteInArea(r, pto1) && isRouteInArea(r, pto2)) {
                                int a = isRouteInArea2(r, pto1);
                                int b = isRouteInArea2(r, pto2);
                                if (a < b) {
                                    compa.add(r);
                                    createListWithAdapter(compa);
                                    Toast.makeText(getApplicationContext(), r.getName() + " Pasa cerca de los 2 puntos " + "orientacion del recorrido: ", Toast.LENGTH_LONG).show();
                                }
                            }

                        }

                    }
                }
            }
        });


        bar = (AppBarLayout) findViewById(R.id.MyAppbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        morph = (FABToolbarLayout) findViewById(R.id.fabtoolbar);

        morph.hide();
        fab.setOnClickListener(this);
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

        //evita caer en un null Pointer Exeption
        routesDraw = new PathOverlay(Color.BLUE, 10, this);
        map.getOverlays().add(routesDraw);
        Markers_stop = DrawinMap.Draw_Stops(map, myDbHelper, icon1 = this.getResources().getDrawable(R.drawable.ic_bustop));
        for (CustomMarker mak : Markers_stop) {
            mak.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    createListWithAdapter(myDbHelper.findRoutesByStop(mak.getIdMarker()));
                    return false;
                }
            });
        }


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


                createListWithAdapter(findNearRoutes(geoPointUser));

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
                return true;
            }
        };

        map.getOverlays().add(touchOverlay);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            if (adapter.getCount() > 0) {
                morph.show();
            } else { Toast.makeText(getApplication(), "Presione un Punto en el Mapa", Toast.LENGTH_LONG).show();            }
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

    public int isRouteInArea2(Route route, GeoPoint geoPoint) {
        int aux = 0;
        for (Point p : route.getPoints()) {
            aux = aux + 1;
            int distance = new GeoPoint(p.getLatitude(), p.getLongitude()).distanceTo(geoPoint);
            if (distance < POSITION_DIAMETER) {
                return aux;

            }
        }
        return -1;
    }

    public List<Route> findNearRoutes(GeoPoint geoPointUser) {
        List<Company> companies = myDbHelper.findCompanies();
        List<Route> routes = new ArrayList<Route>();
        routes.removeAll(routes);
        for (Company c : companies) {
            for (Route r : c.getRoutes()) {
                if (isRouteInArea(r, geoPointUser)) {
                    routes.add(r);
                }
            }
        }
        return routes;
    }

    public List<Company> findAllRoutes() {
        List<Company> companies = myDbHelper.findCompanies();
        allRoutes = new ArrayList<Route>();
        companie = new ArrayList<Company>();
        for (Company c : companies) {
            companie.add(c);
            for (Route r : c.getRoutes()) {
                allRoutes.add(r);
            }
        }
        return companies;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createListWithAdapter(List<Route> routes) {
        if (routes.size() > 0) {
            ArrayList<Route> category = new ArrayList<Route>();
            Route cat;
            if (routes != null && !routes.isEmpty()) {
                for (Route route : routes) {
                    cat = new Route(route.getIdRoute(), route.getName(), route.getStops(), route.getPoints(), getDrawable(getResources().getIdentifier(route.getIcon(),"drawable", getPackageName())));
                    category.add(cat);
                }
            }
            ListView lv = (ListView) findViewById(R.id.ListView);
            adapter = new AdapterRoute(this, category);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final int pos = position;
                    System.out.println(category.get(pos).getPoints());
                    DrawinMap.DrawRoute(map, category.get(pos), routesDraw);
                    morph.hide();
                }
            });
            morph.show();
            lv.setAdapter(adapter);
        }
        routes.clear();
        morph.hide();
        bar.setExpanded(false);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void prepareListData() {
        List<TreeNode> hijos;
        TreeNode padre;
        TreeNode hijo;

        TreeNode root = TreeNode.root();
        TreeNode abuelo = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Recoridos")).setViewHolder(new ProfileHolder(getApplicationContext()));

        List<Route> r;

        for (Company c : findAllRoutes()) {
            r = new ArrayList<Route>();
            hijos = new ArrayList<TreeNode>();

            for (Route route : c.getRoutes()) {
                cat = new Route(route.getIdRoute(), route.getName(), route.getStops(), route.getPoints(), getDrawable(getResources().getIdentifier(route.getIcon(),"drawable", getPackageName())));
                r.add(cat);
                hijo = new TreeNode(new PlaceHolderHolder.PlaceItem(route.getName())).setViewHolder(new PlaceHolderHolder(getApplicationContext()));
                hijo.setClickListener(new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        drawer.closeDrawer(GravityCompat.START);
                        bar.setExpanded(false);
                        DrawinMap.DrawRoute(map, route, routesDraw);
                    }
                });
                hijos.add(hijo);
            }
            padre = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_people, c.getRut())).setViewHolder(new HeaderHolder(getApplicationContext()));

            padre.addChildren(hijos);
            abuelo.addChildren(padre);
        }

        root.addChild(abuelo);
        AndroidTreeView tView = new AndroidTreeView(getApplicationContext(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        layout_menu.addView(tView.getView());
    }
    public Location GetCurrentLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 15000, 1, (LocationListener) this);

            if (location != null)
                return location;
            else
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
        }
        Location loc = null;
        return loc;
    }
}//fin del main
