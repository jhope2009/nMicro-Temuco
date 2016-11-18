package inf.uct.nmicro;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.location.Location;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import inf.uct.nmicro.fragments.CustomMarker;
import inf.uct.nmicro.fragments.DrawInMap;
import inf.uct.nmicro.fragments.SearchTraveling;
import inf.uct.nmicro.fragments.traveling;
import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.model.Instruction;
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.model.Travel;
import inf.uct.nmicro.sqlite.DataBaseHelper;
import inf.uct.nmicro.utils.AdapterRoute;
import inf.uct.nmicro.utils.AdapterTravel;
import inf.uct.nmicro.utils.ConnectWS;
import inf.uct.nmicro.utils.HeaderHolder;
import inf.uct.nmicro.utils.IconTreeItemHolder;
import inf.uct.nmicro.utils.PlaceHolderHolder;
import inf.uct.nmicro.utils.ProfileHolder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    public static int route = -1;
    Drawable icon1;
    private MapController mapController;
    private final int POSITION_DIAMETER = 150;
    private DataBaseHelper myDbHelper;
    private Route cat;
    private List<Route> allRoutes;
    private List<Company> companie;
    MapView map;
    private FABToolbarLayout morph;
    DrawInMap DrawinMap = new DrawInMap();
    PathOverlay routesDraw;
    private LinearLayout layout_menu;
    private LinearLayout animado;
    private AppBarLayout bar;
    private DrawerLayout drawer;
    private AdapterRoute adapter;
    private EditText inicio;
    private EditText destino;
    private AdapterTravel adapterTravel;
    private List<CustomMarker> Markers_stop;
    LocationManager locationManager;
    String mprovider;
    public final static String rutasSeleccionadas = "traveling";
    public ArrayList<Integer> ParaActivityTraveler=new ArrayList<>();
    SearchTraveling travel=new SearchTraveling();
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    public ProgressDialog pDialog;
    public List<Travel> compa;
    public List<Travel> travels;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicio = (EditText) findViewById(R.id.editText3);
        destino = (EditText) findViewById(R.id.editText4);

        inicio.setText("");
        destino.setText("");

        Geocoder geocoder = new Geocoder(getApplication(), Locale.getDefault());
        routesDraw = new PathOverlay(Color.BLUE, 10, this);
        layout_menu = (LinearLayout) findViewById(R.id.layout_menu);
        animado = (LinearLayout) findViewById(R.id.fabtoolbar_toolbar);
        adapter = new AdapterRoute(this);

        myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.NoCheckCreateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prepareListData();
        Button butS = (Button) findViewById(R.id.button4);

        butS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Company> companies = myDbHelper.findCompanies();
                for(Company c :companies){
                  for(Route r :c.getRoutes()){
                      r.setImg(getDrawable(getResources().getIdentifier(r.getIcon(), "drawable", getPackageName())));

                  }
                }
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(destino.getWindowToken(), 0);
                compa = new ArrayList<Travel>();
                CustomTask buttonTask = new CustomTask();

                String ori = inicio.getText().toString();
                String des = destino.getText().toString();

                if (!des.isEmpty()) {
                    if (ori.isEmpty()) {
                        Location loc = GetCurrentLocation();
                        try {
                            List<Address> ub0 = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                            List<Address> ub2 = DrawinMap.findLocationByAddress(des + " Temuco, Araucania, Chile", geocoder,getApplication());
                            DrawinMap.DrawFindLocation(ub0, ub2, map, routesDraw,getApplication());
                            GeoPoint pto1 = new GeoPoint(ub0.get(0).getLatitude(), ub0.get(0).getLongitude());
                            GeoPoint pto2 = new GeoPoint(ub2.get(0).getLatitude(), ub2.get(0).getLongitude());
                            //Ejecuta tarea asincronica
                            buttonTask.execute(companies, pto1, pto2);

                        } catch (IOException e) {e.printStackTrace();}
                    }else {
                        List<Address> ub1 = DrawinMap.findLocationByAddress(ori + " Temuco, Araucania, Chile", geocoder, getApplication());
                        List<Address> ub2 = DrawinMap.findLocationByAddress(des + " Temuco, Araucania, Chile", geocoder, getApplication());
                        DrawinMap.DrawFindLocation(ub1, ub2, map, routesDraw, getApplication());

                        if (ub1.size() > 0 && ub2.size() >0) {
                            GeoPoint pto1 = new GeoPoint(ub1.get(0).getLatitude(), ub1.get(0).getLongitude());
                            GeoPoint pto2 = new GeoPoint(ub2.get(0).getLatitude(), ub2.get(0).getLongitude());
                            //Ejecuta tarea asincronica
                            buttonTask.execute(companies, pto1, pto2);

                        }else{
                            Toast.makeText(getApplicationContext(), "No se encontraron Direcciones", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Debes ingresar destino.", Toast.LENGTH_LONG).show();
                }
            }
        });

        bar = (AppBarLayout) findViewById(R.id.MyAppbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        morph = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        morph.hide();
        fab.setOnClickListener(this);
        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(android.support.v4.BuildConfig.APPLICATION_ID);
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        mapController = (MapController) map.getController();
        mapController.setZoom(15);
        GeoPoint Temuco = new GeoPoint(-38.7392, -72.6087);
        mapController.setCenter(Temuco);

        map.getOverlays().add(routesDraw);
        Markers_stop = DrawinMap.Draw_Stops(map, myDbHelper, icon1 = this.getResources().getDrawable(R.drawable.ic_bustop));
        for (CustomMarker mak : Markers_stop) {
            mak.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    createListWithAdapterRoute(myDbHelper.findRoutesByStop(mak.getIdMarker()),0,marker.getPosition());
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

                createListWithAdapterRoute(findNearRoutes(geoPointUser),0, geoPointUser);

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
    public List<Route> findNearRoutes(GeoPoint geoPointUser) {
        List<Company> companies = myDbHelper.findCompanies();
        List<Route> routes = new ArrayList<Route>();
        routes.removeAll(routes);
        for (Company c : companies) {
            for (Route r : c.getRoutes()) {
                if (DrawinMap.isRouteInArea(r, geoPointUser)) {
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
    public void createListWithAdapterRoute(List<Route> routes, int a, GeoPoint p) {
        //si se llama al metodo desde el evento del boton.
        if(a==1) {
            if (routes.size() > 0) {
                ArrayList<Route> category = new ArrayList<Route>();
                Route cat;
                if (routes != null && !routes.isEmpty()) {
                    for (Route route : routes) {
                        cat = new Route(route.getIdRoute(), route.getName(), route.getStops(), route.getPoints(), getDrawable(getResources().getIdentifier(route.getIcon(), "drawable", getPackageName())));
                        category.add(cat);
                    }
                }
                ListView lv = (ListView) findViewById(R.id.ListView);
                adapter = new AdapterRoute(this, category, p);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final int pos = position;
                        System.out.println(category.get(pos).getPoints());
                       // DrawinMap.DrawRoute(map, category.get(pos), routesDraw);
                       // morph.hide();
                        ParaActivityTraveler.add(category.get(pos).getIdRoute());
                        Intent intent=new Intent(getApplicationContext(),traveling.class);
                        intent.putExtra(rutasSeleccionadas,ParaActivityTraveler);
                        startActivity(intent);
                    }
                });
                morph.show();
                lv.setAdapter(adapter);
            }
            routes.clear();
            morph.hide();
            bar.setExpanded(false);
        }
        else{
            if (routes.size() > 0) {
                ArrayList<Route> category = new ArrayList<Route>();
                Route cat;
                if (routes != null && !routes.isEmpty()) {
                    for (Route route : routes) {
                        cat = new Route(route.getIdRoute(), route.getName(), route.getStops(), route.getPoints(), getDrawable(getResources().getIdentifier(route.getIcon(), "drawable", getPackageName())));
                        category.add(cat);
                    }
                }
                ListView lv = (ListView) findViewById(R.id.ListView);
                adapter = new AdapterRoute(this, category, p);
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
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createListWithAdapterTravel(List<Travel> travels, int a) {
        //si se llama al metodo desde el evento del boton.
        if(a==1) {
            if (travels.size() > 0) {
                ArrayList<Travel> category = new ArrayList<Travel>();
                Travel cat;
                if (travels != null && !travels.isEmpty()) {
                    for (Travel travel : travels) {
                        cat = new Travel(travel.getIdTravel(),travel.getname(), travel.getRoutes()) ;
                        category.add(cat);
                    }
                }
                ListView lv = (ListView) findViewById(R.id.ListView);
                adapterTravel = new AdapterTravel(this, category);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final int pos = position;

                        ParaActivityTraveler.add(category.get(pos).getIdTravel());
                        Intent intent=new Intent(getApplicationContext(),traveling.class);
                        intent.putExtra(rutasSeleccionadas,ParaActivityTraveler);
                        startActivity(intent);
                    }
                });
                morph.show();
                lv.setAdapter(adapterTravel);
            }
            travels.clear();
            morph.hide();
            bar.setExpanded(false);
        }
        else{
            if (travels.size() > 0) {
                ArrayList<Travel> category = new ArrayList<Travel>();
                Travel cat;
                if (travels != null && !travels.isEmpty()) {
                    for (Travel travel : travels) {
                        cat = new Travel(travel.getIdTravel(), travel.getRoutes(), travel.getPrice(), travel.getStartStop(), travel.getEndStop(), travel.getTotalTime(), travel.getStartHour(), travel.getEndHour(),travel.getInstructions());
                        category.add(cat);
                    }
                }
                ListView lv = (ListView) findViewById(R.id.ListView);
                adapterTravel = new AdapterTravel(this, category);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final int pos = position;

                        morph.hide();
                    }
                });
                morph.show();
                lv.setAdapter(adapterTravel);
            }
            travels.clear();
            morph.hide();
            bar.setExpanded(false);


        }
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
        TreeNode abuelo = new TreeNode(new IconTreeItemHolder.IconTreeItem(getDrawable(R.drawable.bus), "Recoridos")).setViewHolder(new ProfileHolder(getApplicationContext()));

        List<Route> r;

        for (Company c : findAllRoutes()) {
            hijos = new ArrayList<TreeNode>();

            for (Route route : c.getRoutes()) {
                cat = new Route(route.getIdRoute(), route.getName(), route.getStops(), route.getPoints(), getDrawable(getResources().getIdentifier(route.getIcon(),"drawable", getPackageName())));
                hijo = new TreeNode(new PlaceHolderHolder.PlaceItem(route.getName(),cat.getImg())).setViewHolder(new PlaceHolderHolder(getApplicationContext()));
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
            padre = new TreeNode(new IconTreeItemHolder.IconTreeItem(getDrawable(R.drawable.marker_default), c.getRut())).setViewHolder(new HeaderHolder(getApplicationContext()));

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
    // tarea asincornica para la buueda de rutas por origen y destino
    class CustomTask extends AsyncTask{
        Geocoder geocoder2 = new Geocoder(getApplication(), Locale.getDefault());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage("Procesando...");
            pDialog.setCancelable(true);
            pDialog.setMax(100);
            pDialog.show();
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Object doInBackground(Object[] params) {

            List<Company> companies = (ArrayList<Company>)params[0];
            GeoPoint pto1 = (GeoPoint)params[1];
            GeoPoint pto2 = (GeoPoint)params[2];
            List<Route> rutas=new ArrayList<>();
            for (Company c : companies) {
                for (Route r : c.getRoutes()) {
                    if (DrawinMap.isRouteInArea(r, pto1) && DrawinMap.isRouteInArea(r, pto2)) {
                        int a = DrawinMap.isRouteInArea2(r, pto1);
                        int b = DrawinMap.isRouteInArea2(r, pto2);
                        if (a < b) {
                            rutas.add(new Route(r.getIdRoute(),r.getName(),r.getStops(),r.getPoints(),getDrawable(getResources().getIdentifier(r.getIcon(), "drawable", getPackageName()))));
                            Travel tr=new Travel(rutas.get(0).getIdRoute(),r.getName().toString(),rutas);
                            compa.add(tr);
                        }
                    }
                }
            }
            travels=travel.GetTravel(companies,pto1,pto2,geocoder2);
            Log.i("Datos de los viajes obtenidos","si no pasa gg"+String.valueOf(travels.size()));
            for(Travel tr :travels){
                Log.i("Mostarndo el nombre de los paraderos",tr.getname());
                for(Instruction ins : tr.getInstructions()){
                    Log.i("las instrucciones",ins.getIndication());
                    break;
                }
                for(Route r: tr.getRoutes()){
                    Log.i("las rutas",r.getName());
                }


            }
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pDialog.dismiss();
            createListWithAdapterTravel(compa,1);
            if(compa.size()==0){
                createListWithAdapterTravel(travels,1);
            }
        }
    }

}//fin del main
