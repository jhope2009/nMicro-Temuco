package inf.uct.nmicro;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
import inf.uct.nmicro.fragments.DrawInMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int route = -1;
    private int[] tabIcons = {
            R.drawable.ic_maps_map,
            R.drawable.ic_recorrido,
            R.drawable.ic_toggle_star,
            R.drawable.ic_rutas2
    };

    DrawInMap DrawMap=new DrawInMap();

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
    private List<Company> companie;
    public MapView map;
    private FABToolbarLayout morph;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Route>> listDataChild;
    ArrayList<OverlayItem> anotherOverlayItemArray;
    List<Address> ub1;
    List<Address> ub2;
    PathOverlay routesDraw;
    Drawable icon1;
    private LinearLayout layoutAnimado;
    private LinearLayout layoutAnimado1;
    private LinearLayout layoutAnimado2;
    private LinearLayout layoutAnimado3;
    private LinearLayout layoutAnimado4;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutAnimado = (LinearLayout) findViewById(R.id.animado);
        layoutAnimado1 = (LinearLayout) findViewById(R.id.animado1);
        layoutAnimado2 = (LinearLayout) findViewById(R.id.animado2);
        layoutAnimado3 = (LinearLayout) findViewById(R.id.animado3);
        layoutAnimado4 = (LinearLayout) findViewById(R.id.animado4);

        Button btnseach = (Button) findViewById(R.id.button4);
        Button but = (Button) findViewById(R.id.button2);
        Button but1 = (Button) findViewById(R.id.button3);
        Button but2 = (Button) findViewById(R.id.button5);
        Button but3 = (Button) findViewById(R.id.button6);
        Button but4 = (Button) findViewById(R.id.button7);
        but.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {mostrar(v);}});
        but1.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {mostrar1(v);}});
        but2.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {mostrar2(v);}});
        but3.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {mostrar3(v);}});
        but4.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {mostrar4(v);}});


        final EditText Edtxt1=(EditText)findViewById(R.id.editText4);
        final EditText Edtxt2=(EditText)findViewById(R.id.editText3);
        String ini  = Edtxt1.getText().toString();
        String des = Edtxt2.getText().toString();
        icon1=this.getResources().getDrawable(R.drawable.ic_bustop);
        routesDraw = new PathOverlay(Color.BLUE, 10, this);
        ub1 =findLocationByAddressL(ini  +", Temuco, Araucania, Chile");
        ub2 =findLocationByAddressL(des  +", Temuco, Araucania, Chile");

        btnseach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.getOverlayManager().clear();
                Geocoder geocoder= new Geocoder(getApplication(), Locale.getDefault());
                List<Address> ub1 = DrawMap.findLocationByAddress(Edtxt1.getText().toString() + " Temuco, Araucania, Chile",geocoder);
                List<Address> ub2 = DrawMap.findLocationByAddress(Edtxt2.getText().toString() + " Temuco, Araucania, Chile",geocoder);
                if (ub1 != null && ub2 != null) {
                    Address ad = ub1.get(0);
                    Log.i(String.valueOf(ad.getLatitude()), String.valueOf(ad.getLatitude()) + String.valueOf(ad.getLongitude()));
                    System.out.print(ad.getLatitude() + ":" + ad.getLongitude());
                    GeoPoint marcado = new GeoPoint(ad.getLatitude(), ad.getLongitude());
                    Toast.makeText(getApplicationContext(), "La ubicacion 1" + String.valueOf(ad.getLatitude()) + " : " + String.valueOf(ad.getLongitude()), Toast.LENGTH_LONG).show();
                    Marker sele = new Marker(map);
                    sele.setTitle("la ubicacion que querias: " + Edtxt1.getText().toString());
                    sele.setPosition(marcado);
                    sele.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlayManager().add(sele);
                    //agrego el marcador de la segunda ubicacion
                    Address ad1 = ub2.get(0);
                    GeoPoint ma2 = new GeoPoint(ad1.getLatitude(), ad1.getLongitude());
                    Log.i("separador", "las latitudes y longitudes de lo que necesitas");
                    Log.i(String.valueOf(ad1.getLatitude()), String.valueOf(ad1.getLatitude()) + String.valueOf(ad1.getLongitude()));
                    Toast.makeText(getApplicationContext(), String.valueOf(ad1.getLatitude()) + " : " + String.valueOf(ad1.getLongitude()), Toast.LENGTH_LONG).show();
                    Marker sele2 = new Marker(map);
                    sele2.setTitle("la ubicacion 2: " + Edtxt2.getText().toString());
                    sele2.setPosition(ma2);
                    sele2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlayManager().add(sele2);
                    map.invalidate();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(Edtxt2.getWindowToken(), 0);
                }
            }
        });

        CollapsingToolbarLayout collapsinToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
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

        //cargamos los paraderos ahora con metodo haaa si o no? mire que es pillo
        DrawMap.Draw_Stops(map,myDbHelper,icon1=this.getResources().getDrawable(R.drawable.ic_bustop));
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
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

        List<TreeNode> hijos;
        TreeNode padre;
        TreeNode hijo;

        TreeNode root = TreeNode.root();
        TreeNode abuelo = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Recoridos")).setViewHolder(new ProfileHolder(getApplicationContext()));

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Route>>();

        findAllRoutes();
        List<Route> r = new ArrayList<Route>();

        for (Company c : companie) {
            listDataHeader.add(c.getRut());
            r = new ArrayList<Route>();
            hijos = new ArrayList<TreeNode>();
            for (Route route : c.getRoutes()) {
                cat = new Route(route.getIdRoute(), route.getName(), route.getStops(), route.getPoints(), getDrawable(R.drawable.ic_1a), route.getSignLatitude(), route.getSignLongitude());
                r.add(cat);
                hijo = new TreeNode(new PlaceHolderHolder.PlaceItem(route.getName())).setViewHolder(new PlaceHolderHolder(getApplicationContext()));
                hijos.add(hijo);
            }
            padre = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_people, c.getRut())).setViewHolder(new HeaderHolder(getApplicationContext()));

            padre.addChildren(hijos);
            abuelo.addChildren(padre);

            listDataChild.put(c.getRut(),r);
        }

        root.addChild(abuelo);

        AndroidTreeView tView = new AndroidTreeView(getApplicationContext(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        layoutAnimado3.addView(tView.getView());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            morph.show();
        }


        morph.hide();
        switch (v.getId()){
            case R.id.button2:
                Toast.makeText(getApplication(),"desplegado layout 1",Toast.LENGTH_LONG).show();
                mostrar(v);
                break;
            case R.id.button3:
                mostrar1(v);
                break;
            case R.id.button5:
                mostrar2(v);
                break;
            case R.id.button6:
                mostrar3(v);
                break;
            case R.id.button7:
                mostrar4(v);
                break;
        }
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
        companie = new ArrayList<Company>();
        for (Company c : companies) {
            companie.add(c);
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
                cat = new Route(route.getIdRoute(), route.getName(), route.getStops(), route.getPoints(), getDrawable(R.drawable.ic_1a), route.getSignLatitude(), route.getSignLongitude());
                category.add(cat);
            }
            morph.show();
        }
        ListView lv = (ListView) findViewById(R.id.ListView);

        AdapterRoute adapter = new AdapterRoute(this, category);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                morph.hide();
                String nombres = routes.get(pos).getName();
                Route ruta=routes.get(pos);
                //metodo que pinta la ruta en el mapa.
              DrawMap.DrawRoute(map, routes.get(pos), routesDraw);

                Toast.makeText(getApplicationContext(), nombres, Toast.LENGTH_SHORT).show();
            }
        });

        lv.setAdapter(adapter);
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
        /*
        */
        List<TreeNode> hijos;
        TreeNode padre;

        TreeNode root = TreeNode.root();
        TreeNode abuelo = new TreeNode("Recorridos");
        TreeNode hijo;

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Route>>();

        findAllRoutes();
        List<Route> r = new ArrayList<Route>();

        for (Company c : companie) {

            listDataHeader.add(c.getRut());
            r = new ArrayList<Route>();
            hijos = new ArrayList<TreeNode>();

            for (Route route : c.getRoutes()) {
                cat = new Route(route.getIdRoute(), route.getName(), route.getStops(), route.getPoints(), getDrawable(R.drawable.ic_1a), route.getSignLatitude(), route.getSignLongitude());
                r.add(cat);
                hijo = new TreeNode(route.getName());
                hijos.add(hijo);
            }
            padre = new TreeNode(c.getRut());

            padre.addChildren(hijos);
            abuelo.addChildren(padre);

            listDataChild.put(c.getRut(),r);
        }
        root.addChild(abuelo);

        AndroidTreeView tView = new AndroidTreeView(this, root);
        layoutAnimado2.addView(tView.getView());
    }


    public void mostrar(View button) {
                if (layoutAnimado.getVisibility() == View.GONE) {
                    animar(true,1);
                    layoutAnimado.setVisibility(View.VISIBLE);
                    layoutAnimado1.setVisibility(View.GONE);
                    layoutAnimado2.setVisibility(View.GONE);
                    layoutAnimado3.setVisibility(View.GONE);
                    layoutAnimado4.setVisibility(View.GONE);
                } else {
                    animar(false,1);
                    layoutAnimado.setVisibility(View.GONE);
                }
    }
    public void mostrar1(View button) {
                if (layoutAnimado1.getVisibility() == View.GONE) {
                    animar(true, 2);
                    layoutAnimado1.setVisibility(View.VISIBLE);
                    layoutAnimado.setVisibility(View.GONE);
                    layoutAnimado2.setVisibility(View.GONE);
                    layoutAnimado3.setVisibility(View.GONE);
                    layoutAnimado4.setVisibility(View.GONE);
                } else {
                    animar(false, 2);
                    layoutAnimado1.setVisibility(View.GONE);
                }
    }
    public void mostrar2(View button) {
                if (layoutAnimado2.getVisibility() == View.GONE) {
                    animar(true, 3);
                    layoutAnimado2.setVisibility(View.VISIBLE);
                    layoutAnimado1.setVisibility(View.GONE);
                    layoutAnimado3.setVisibility(View.GONE);
                    layoutAnimado.setVisibility(View.GONE);
                    layoutAnimado4.setVisibility(View.GONE);
                } else {
                    animar(false, 3);
                    layoutAnimado2.setVisibility(View.GONE);
                }
    }
    public void mostrar3(View button) {
                if (layoutAnimado3.getVisibility() == View.GONE) {
                    animar(true, 4);
                    layoutAnimado3.setVisibility(View.VISIBLE);
                    layoutAnimado1.setVisibility(View.GONE);
                    layoutAnimado2.setVisibility(View.GONE);
                    layoutAnimado.setVisibility(View.GONE);
                    layoutAnimado4.setVisibility(View.GONE);
                } else {
                    animar(false, 4);
                    layoutAnimado3.setVisibility(View.GONE);
                }
    }
    public void mostrar4(View button) {
                if (layoutAnimado4.getVisibility() == View.GONE) {
                    animar(true, 5);
                    layoutAnimado4.setVisibility(View.VISIBLE);
                    layoutAnimado1.setVisibility(View.GONE);
                    layoutAnimado2.setVisibility(View.GONE);
                    layoutAnimado3.setVisibility(View.GONE);
                    layoutAnimado.setVisibility(View.GONE);
                } else {
                    animar(false, 5);
                    layoutAnimado4.setVisibility(View.GONE);
                }
        }

    private void animar(boolean mostrar, int id) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
        {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else
        {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        switch (id){
            case 1:
                Toast.makeText(getApplication(),"desplegado layout "+id,Toast.LENGTH_LONG).show();
                layoutAnimado.setLayoutAnimation(controller);
                layoutAnimado.startAnimation(animation);
                break;
            case 2:
                Toast.makeText(getApplication(),"desplegado layout "+id,Toast.LENGTH_LONG).show();
                layoutAnimado1.setLayoutAnimation(controller);
                layoutAnimado1.startAnimation(animation);
                break;
            case 3:
                Toast.makeText(getApplication(),"desplegado layout "+id,Toast.LENGTH_LONG).show();
                layoutAnimado2.setLayoutAnimation(controller);
                layoutAnimado2.startAnimation(animation);
                break;
            case 4:
                Toast.makeText(getApplication(),"desplegado layout "+id,Toast.LENGTH_LONG).show();
                layoutAnimado3.setLayoutAnimation(controller);
                layoutAnimado3.startAnimation(animation);
                break;
            case 5:
                Toast.makeText(getApplication(),"desplegado layout "+id,Toast.LENGTH_LONG).show();
                layoutAnimado4.setLayoutAnimation(controller);
                layoutAnimado4.startAnimation(animation);
                break;
        }
    }

    public List<Address> findLocationByAddressL(String text){
        List<Address> direcciones;
        Geocoder geocoder= new Geocoder(getApplication(), Locale.getDefault());
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





}
