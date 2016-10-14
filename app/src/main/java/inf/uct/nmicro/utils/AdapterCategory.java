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


public class AdapterCategory extends BaseAdapter {

    protected Activity activity;
    protected Fragment fragment;
    protected ArrayList<Category> items;

    public AdapterCategory (Activity activity, ArrayList<Category> items) {
        this.activity = activity;
        this.items = items;
    }

    public AdapterCategory (Fragment fragment, ArrayList<Category> items) {
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

    public void addAll(ArrayList<Category> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
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

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_category, null);
        }

        Category dir = items.get(position);

        TextView title = (TextView) v.findViewById(R.id.category);
        title.setText(dir.getTitle());

        TextView description = (TextView) v.findViewById(R.id.texto);
        description.setText(dir.getDescription());

        ImageView imagen = (ImageView) v.findViewById(R.id.imageView4);
        imagen.setImageDrawable(dir.getImage());

        return v;
    }
}