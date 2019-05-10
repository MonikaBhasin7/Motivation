package com.techsdm.motivation;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.Toast;

import com.techsdm.motivation.Common.Common;

public class Tabbed_Activity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener{



    android.support.v7.widget.SearchView search_view;
    android.app.FragmentTransaction fragmentTransaction;
    public android.app.FragmentManager fragmentManager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    TabHost tHost;
    private ViewPager mViewPager;
    public TabLayout tabLayout;
    /*private int[] tabIcons = {
           R.drawable.ic_category,
            R.drawable.ic_recent,
            R.drawable.ic_favorite
    };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_);

        fragmentManager=getFragmentManager();





        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        search_view= (android.support.v7.widget.SearchView) findViewById(R.id.search_view);
        search_view.setOnQueryTextListener(this);
        search_view.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) search_view.getLayoutParams();
                param.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                //set layout params to cause layout update
                search_view.setLayoutParams(param);
                return false;
            }
        });

        search_view.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) search_view.getLayoutParams();
                param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                //set layout params to cause layout update
                search_view.setLayoutParams(param);
            }
        });





        //
        // setupTabIcons();

        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null).commit();


    }


    /*private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Common.search_query=query;
        Intent intent=new Intent(getApplicationContext(),FindActivity.class);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed_, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new Tab1();


                    break;

                case 1:
                    fragment = new Tab2();
                    break;

                /*case 2:
                    fragment = new Tab3();
                    break;*/

            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
           // return 3;
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Popular";
                case 1:
                    return "Collection";
                /*case 2:
                    return "Favorites";*/
            }
            return null;
        }
    }
}
