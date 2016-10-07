package inf.uct.nmicro.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import inf.uct.nmicro.R;
import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.sqlite.DataBaseHelper;
import inf.uct.nmicro.utils.AdapterCategory;
import inf.uct.nmicro.utils.Category;

/**
 * Created by Esteban Campos A on 28-09-2016.
 */
public class FragmentRoutes extends Fragment {


    private ListView lv;
    private AdapterCategory adapter;
    private Category cat;

    public FragmentRoutes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<Category> category = new ArrayList<Category>();

        cat = new Category("Recorrido","1C 1A","micro que va al centro",getResources().getDrawable(R.drawable.ic_1a));
        category.add(cat);

        View rootView = inflater.inflate(R.layout.fragment_routes, container, false);
        lv = (ListView) rootView.findViewById(R.id.ListView);


        DataBaseHelper myDbHelper = new DataBaseHelper(this.getActivity());
        try {
            myDbHelper.NoCheckCreateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<Company> Lineas = myDbHelper.findCompanies();


        for(Company linea : Lineas){
            List<Route> rutas = linea.getRoutes();
            for(Route ruta : rutas) {
                cat = new Category("Recorrido", ruta.getName() + "", "micro que va al centro", getResources().getDrawable(R.drawable.ic_1a));
                category.add(cat);
            }
        }

        adapter = new AdapterCategory(this.getActivity(), category);
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
