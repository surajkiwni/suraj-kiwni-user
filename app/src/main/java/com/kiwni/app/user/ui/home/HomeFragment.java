package com.kiwni.app.user.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import com.kiwni.app.user.R;

import com.kiwni.app.user.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.kiwni.app.user.fragments.AirportFragment;
import com.kiwni.app.user.fragments.OutstationFragment;
import com.kiwni.app.user.fragments.RentalFragment;

public class HomeFragment extends Fragment
{

    HomeViewModel homeViewModel;

    private TabLayout tabLayout;
    private FragmentHomeBinding binding;
    String serviceType = "";
    View root;
    Fragment outstationFragment, airportFragment, rentalFragment;
    public static HomeFragment instance;

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

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

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

        FragmentManager fm = getParentFragmentManager();
        HomeFragment homeFragment = (HomeFragment) fm.findFragmentByTag("fragment_tag");
        if (homeFragment != null) {
            fm.beginTransaction().remove(homeFragment).commit();
        }
    }

    public static HomeFragment getInstance() {
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}