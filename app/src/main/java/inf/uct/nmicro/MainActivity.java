package inf.uct.nmicro;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import android.view.LayoutInflater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import inf.uct.nmicro.fragments.FragmentFavorites;
import inf.uct.nmicro.fragments.FragmentMap;
import inf.uct.nmicro.fragments.FragmentRoutes;
import inf.uct.nmicro.fragments.FragmentToplan;
import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.sqlite.DataBaseHelper;


public class MainActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons={
            R.drawable.ic_maps_map,
            R.drawable.ic_recorrido,
            R.drawable.ic_toggle_star,
            R.drawable.ic_rutas2
    };
    /* intentando pasar de fragment y los datos. */

     public void onroute(int position) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article

       FragmentMap articleFrag = (FragmentMap)
                getSupportFragmentManager().findFragmentByTag("FragmentMap");


            // Otherwise, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            FragmentMap newFragment = new FragmentMap();
            Bundle args = new Bundle();
           // args.putInt(FragmentMap.ARG_POSITION, position);
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
          //  transaction.replace(R.id.FragmentRoutes, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
//terminan los dato


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


        DataBaseHelper myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.NoCheckCreateDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Company> Lineas = myDbHelper.findCompanies();

        for(Company linea : Lineas){
            System.out.println(linea.getIdCompany()+" / "+linea.getName()+" / "+linea.getRut());
        }

    }
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Inicio");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0,tabIcons[0] , 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);
        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Recorrido");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Favoritos");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("Mi Ruta");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[3], 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentMap(), "Inicio");
        adapter.addFragment(new FragmentRoutes() , "Recorridos");
        adapter.addFragment(new FragmentFavorites(), "Favoritos");
        adapter.addFragment(new FragmentToplan(), "Mi Ruta");
        viewPager.isHorizontalScrollBarEnabled();
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);

        }
    }



}