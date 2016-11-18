package inf.uct.nmicro.utils;

/**
 * Created by jairo on 9/28/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import inf.uct.nmicro.R;
import inf.uct.nmicro.model.Route;


public class AdapterRoute extends BaseAdapter {

    protected Activity activity;
    protected Fragment fragment;
    protected ArrayList<Route> items;
    protected GeoPoint p;

    public AdapterRoute(Activity activity) {
        this.activity = activity;
        items = new ArrayList<Route>();
    }

    public AdapterRoute(Activity activity, ArrayList<Route> items, GeoPoint p) {
        this.activity = activity;
        this.items = items;
        this.p = p;
    }

    public AdapterRoute(Fragment fragment, ArrayList<Route> items) {
        this.fragment = fragment;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Route> route) {
        for (int i = 0; i < route.size(); i++) {
            items.add(route.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getIdRoute();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        Route dir = items.get(position);
        //aqui llamo a la clase que realiza la coneccion con el WS y le paso parametros concatenados por coma


        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_category, null);
        }



        TextView title = (TextView) v.findViewById(R.id.category);
        title.setText(dir.getName());

        TextView title1 = (TextView) v.findViewById(R.id.textView4);
        TextView title2 = (TextView) v.findViewById(R.id.textView5);
        TextView title3 = (TextView) v.findViewById(R.id.textView6);

        ImageView imagen = (ImageView) v.findViewById(R.id.imageView4);
        imagen.setImageDrawable(dir.getImg());


        new ConnectWS(activity, title1, title2, title3, 0).execute(dir.getIdRoute()+","+p.getLatitude()+","+p.getLongitude());
        return v;
    }
}