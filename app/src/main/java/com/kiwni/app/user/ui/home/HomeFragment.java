package com.kiwni.app.user.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;

import com.kiwni.app.user.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.kiwni.app.user.fragments.AirportFragment;
import com.kiwni.app.user.fragments.OutstationFragment;
import com.kiwni.app.user.fragments.RentalFragment;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

public class HomeFragment extends Fragment
{
    HomeViewModel homeViewModel;

    private TabLayout tabLayout;
    private FragmentHomeBinding binding;
    String serviceType = "";
    String TAG = this.getClass().getSimpleName();
    View root;
    Fragment outstationFragment, airportFragment, rentalFragment;
    public HomeFragment instance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instance=this;

        tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0 :
                        replaceFragment(outstationFragment);
                        break;
                    case 1 :
                        replaceFragment(airportFragment);
                        break;
                    case 2:
                        replaceFragment(rentalFragment);
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

        outstationFragment = new OutstationFragment();
        airportFragment = new AirportFragment();
        rentalFragment = new RentalFragment();
        tabLayout.addTab(tabLayout.newTab().setText("Outstation"), true);
        tabLayout.addTab(tabLayout.newTab().setText("Airport"));
        tabLayout.addTab(tabLayout.newTab().setText("Rental"));
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.homeFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }
}