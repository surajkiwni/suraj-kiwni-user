package com.kiwni.app.user.fragments;

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


public class RentalFragment extends Fragment {
    TabLayout rentalTabLayout;
    String serviceType = "";
    static RentalFragment instance;
    Fragment currentBookingFragment, scheduleBookingFragment;
    String TAG = this.getClass().getSimpleName();

    public RentalFragment() {
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
        View view = inflater.inflate(R.layout.fragment_rental, container, false);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instance = this;
        rentalTabLayout = view.findViewById(R.id.rentalTabLayout);

        rentalTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0 :
                        replaceFragment(currentBookingFragment);
                        break;
                    case 1 :
                        replaceFragment(scheduleBookingFragment);
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

        currentBookingFragment = new CurrentBookingFragment();
        scheduleBookingFragment = new ScheduleBookingFragment();

        rentalTabLayout.addTab(rentalTabLayout.newTab().setText("CURRENT BOOKING"), true);
        rentalTabLayout.addTab(rentalTabLayout.newTab().setText("SCHEDULE BOOKING"));
    }

    public static RentalFragment getInstance() {
        return instance;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.rentalFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}