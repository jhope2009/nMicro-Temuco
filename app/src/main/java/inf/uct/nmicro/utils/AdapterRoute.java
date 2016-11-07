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

import java.util.ArrayList;

import inf.uct.nmicro.R;
import inf.uct.nmicro.model.Route;


public class AdapterRoute extends BaseAdapter {

    protected Activity activity;
    protected Fragment fragment;
    protected ArrayList<Route> items;

    public AdapterRoute(Activity activity) {
        this.activity = activity;
        items = new ArrayList<Route>();
    }

    public AdapterRoute(Activity activity, ArrayList<Route> items) {
        this.activity = activity;
        this.items = items;
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

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_category, null);
        }

        Route dir = items.get(position);

        TextView title = (TextView) v.findViewById(R.id.category);
        title.setText(dir.getName());

        ImageView imagen = (ImageView) v.findViewById(R.id.imageView4);
        imagen.setImageDrawable(dir.getImg());

        return v;
    }
}