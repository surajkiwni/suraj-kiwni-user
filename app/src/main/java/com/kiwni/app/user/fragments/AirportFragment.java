package com.kiwni.app.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.AirportViewPagerAdapter;


public class AirportFragment extends Fragment
{
    AirportViewPagerAdapter airportViewPagerAdapter;
    TabLayout tabLayout;
    ViewPager2 airportViewPager;
    String serviceType = "", direction = "";

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
        airportViewPager = view.findViewById(R.id.airportViewPager);

        //setting up adaptor with fragment
        airportViewPagerAdapter = new AirportViewPagerAdapter(this);

        //adaptor set to the view Pager
        airportViewPager.setAdapter(airportViewPagerAdapter);

        tabLayout = (TabLayout) view.findViewById(R.id.airportTabLayout);

        airportViewPager.setCurrentItem(0, true);

        tabLayout.addTab(tabLayout.newTab().setText("AIRPORT PICKUP"));
        tabLayout.addTab(tabLayout.newTab().setText("AIRPORT DROP"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                airportViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        airportViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}