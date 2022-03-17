package com.kiwni.app.user.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.AirportViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class AirportFragment extends Fragment {


    private AirportViewPagerAdapter airportViewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager2 airportViewPager2;
    String serviceType = "",direction = "";

    View view;

    static AirportFragment myInstance;

    public synchronized static AirportFragment getInstance() {
        if (myInstance == null) {
            myInstance = new AirportFragment();
        }
        return myInstance;
    }

    public AirportFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_airport, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // initialize view Pager
        airportViewPager2 = view.findViewById(R.id.airportViewPager);


        //Log.d("airport Fragment",PreferencesUtils.getPreferences(getActivity(),SharedPref.SERVICE_TYPE,serviceType));

        //setting up adaptor with fragment
        airportViewPagerAdapter = new AirportViewPagerAdapter(this);


        //adaptor set to the view Pager
        airportViewPager2.setAdapter(airportViewPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.airportTabLayout);
        airportViewPager2.setCurrentItem(0, true);

        tabLayout.addTab(tabLayout.newTab().setText("AIRPORT PICKUP"));
        tabLayout.addTab(tabLayout.newTab().setText("AIRPORT DROP"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                airportViewPager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                airportViewPager2.setCurrentItem(tab.getPosition());

            }
        });

        airportViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });




    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}