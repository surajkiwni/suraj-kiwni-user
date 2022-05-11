package com.kiwni.app.user.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.kiwni.app.user.R;
import com.kiwni.app.user.ui.home.HomeFragment;

@SuppressLint("StaticFieldLeak")
public class AirportFragment extends Fragment
{
    TabLayout tabLayout;
    //ViewPager2 airportViewPager;
    String serviceType = "", direction = "";

    View view;

    public static AirportFragment instance;
    Fragment airportPickupFragment, airportDropFragment;
    String TAG = this.getClass().getSimpleName();

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

        instance = this;
        tabLayout = view.findViewById(R.id.airportTabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0 :
                        replaceFragment(airportPickupFragment);
                        break;
                    case 1 :
                        replaceFragment(airportDropFragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        airportPickupFragment = new AirportPickupFragment();
        airportDropFragment = new AirportDropFragment();

        tabLayout.addTab(tabLayout.newTab().setText("AIRPORT PICKUP"), true);
        tabLayout.addTab(tabLayout.newTab().setText("AIRPORT DROP"));
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.airportFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public static AirportFragment getInstance() {
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}