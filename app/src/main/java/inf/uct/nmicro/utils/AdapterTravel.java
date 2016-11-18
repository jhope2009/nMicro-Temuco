package inf.uct.nmicro.utils;

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
import inf.uct.nmicro.model.Travel;

/**
 * Created by Javier on 16-11-2016.
 */

public class AdapterTravel extends BaseAdapter {

    protected Activity activity;
    protected Fragment fragment;
    protected ArrayList<Travel> items;
    protected GeoPoint p;

    public AdapterTravel(Activity activity) {
        this.activity = activity;
        items = new ArrayList<Travel>();

    }

    public AdapterTravel(Activity activity, ArrayList<Travel> items, GeoPoint p) {
        this.activity = activity;
        this.items = items;
        this.p = p;
    }

    public AdapterTravel(Fragment fragment, ArrayList<Travel> items) {
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

    public void addAll(ArrayList<Travel> travel) {
        for (int i = 0; i < travel.size(); i++) {
            items.add(travel.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getIdTravel();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_category, null);
        }

        Travel dir = items.get(position);

        TextView title = (TextView) v.findViewById(R.id.category);

        TextView title1 = (TextView) v.findViewById(R.id.textView4);
        TextView title2 = (TextView) v.findViewById(R.id.textView5);
        TextView title3 = (TextView) v.findViewById(R.id.textView6);

        title.setText(dir.getname());

        ImageView imagen =  (ImageView) v.findViewById(R.id.imageView4);
        ImageView imagen2 = (ImageView) v.findViewById(R.id.imageView5);
        imagen.setImageDrawable(dir.getRoutes().get(0).getImg());
        if(dir.getRoutes().size()>1){
            imagen2.setImageDrawable(dir.getRoutes().get(1).getImg());
        }

        //esto me falta analizar y encontrar solucion puesto que son dos rutas las que debo unir y calcular aproximado
        new ConnectWS(activity, title1, title2, title3, 0).execute(dir.getRoutes().get(0).getIdRoute()+","+p.getLatitude()+","+p.getLongitude());
        return v;
    }
}
