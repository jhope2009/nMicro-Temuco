package inf.uct.nmicro.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

import inf.uct.nmicro.R;
import inf.uct.nmicro.utils.AdapterCategory;
import inf.uct.nmicro.utils.Category;

/**
 * Created by Esteban Campos A on 28-09-2016.
 */
public class FragmentRoutes extends Fragment {

    ArrayList<Category> category = new ArrayList<Category>();
    public FragmentRoutes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //important! set your user agent to prevent getting banned from the osm server

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_routes, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.ListView);

        AdapterCategory adapter = new AdapterCategory(this.getActivity(), category);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                //CODIGO AQUI

            }
        });
        lv.setAdapter(adapter);

        return rootView;
    }
}
