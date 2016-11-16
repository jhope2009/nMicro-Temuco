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

    public AdapterTravel(Activity activity) {
        this.activity = activity;
        items = new ArrayList<Travel>();
    }

    public AdapterTravel(Activity activity, ArrayList<Travel> items) {
        this.activity = activity;
        this.items = items;
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
        String txt="";
        for(Route r : dir.getRoutes()){
            txt=txt+r.getName();
        }
        title.setText(txt);

        //ImageView imagen = (ImageView) v.findViewById(R.id.imageView4);
        //imagen.setImageDrawable(dir.getRoutes().getImg());

        return v;
    }
}
