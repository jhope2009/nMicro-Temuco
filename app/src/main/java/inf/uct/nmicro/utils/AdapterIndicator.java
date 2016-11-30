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

public class AdapterIndicator extends BaseAdapter {

    protected Activity activity;
    protected Fragment fragment;
    protected ArrayList<String> items;
    protected GeoPoint p;

    public AdapterIndicator(Activity activity) {
        this.activity = activity;
        items = new ArrayList<String>();
    }

    public AdapterIndicator(Activity activity, ArrayList<String> items, GeoPoint p) {
        this.activity = activity;
        this.items = items;
        this.p = p;
    }

    public AdapterIndicator(Fragment fragment, ArrayList<String> items) {
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

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        //aqui llamo a la clase que realiza la coneccion con el WS y le paso parametros concatenados por coma
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_info, null);
        }
        return v;
    }
}