package com.kiwni.app.user.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.ui.home.HomeFragment;

@SuppressLint("StaticFieldLeak")
public class OutstationFragment extends Fragment {
    private TabLayout tabLayout;
    String serviceType = "";
    Fragment roundTripFragment, oneWayFragment;
    /*public static OutstationFragment instance;*/

    public static OutstationFragment instance;
    String TAG = this.getClass().getSimpleName();
    View view;
    String currentLat = "", currentLng = "";
    Bundle bundle;

    public OutstationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_outstation, container, false);
        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instance = this;
        tabLayout = view.findViewById(R.id.tabLayout);

        /* get bundle value here */
        bundle = this.getArguments();
        if (bundle != null)
        {
            currentLat = bundle.getString(SharedPref.USER_CURRENT_LAT, "");
            currentLng = bundle.getString(SharedPref.USER_CURRENT_LNG, "");
        }
        Log.d(TAG, "Location = " + currentLat + " " + currentLng);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0 :
                        replaceFragment(roundTripFragment);
                        break;
                    case 1 :
                        replaceFragment(oneWayFragment);
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

        roundTripFragment = new RoundTripFragment();
        oneWayFragment = new OneWayFragment();
        tabLayout.addTab(tabLayout.newTab().setText("Round Trip"), true);
        tabLayout.addTab(tabLayout.newTab().setText("One Way"));
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.outstationFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public static OutstationFragment getInstance() {
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();

        bundle = this.getArguments();
        if (bundle != null)
        {
            currentLat = bundle.getString(SharedPref.USER_CURRENT_LAT, "");
            currentLng = bundle.getString(SharedPref.USER_CURRENT_LNG, "");
        }
        Log.d(TAG, "Location = " + currentLat + " " + currentLng);

    }
}
