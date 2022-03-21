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
import com.kiwni.app.user.adapter.RentalViewPagerAdapter;


public class RentalFragment extends Fragment {

    RentalViewPagerAdapter rentalViewPagerAdapter;
    TabLayout rentalTabLayout;
    ViewPager2 rentalViewPager2;
    String serviceType = "";

    static RentalFragment myInstance;
    public synchronized static RentalFragment getInstance() {
        if (myInstance == null) {
            myInstance = new RentalFragment();
        }
        return myInstance;
    }

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

        // initialize view Pager
        rentalViewPager2 = view.findViewById(R.id.rentalViewPager2);

        //setting up adaptor with fragment
        rentalViewPagerAdapter = new RentalViewPagerAdapter(this);


        //adaptor set to the view Pager
        rentalViewPager2.setAdapter(rentalViewPagerAdapter);
        rentalTabLayout = (TabLayout) view.findViewById(R.id.rentalTabLayout);
        rentalViewPager2.setCurrentItem(0, true);

        rentalViewPager2.setSaveEnabled(false);
        rentalViewPager2.setSaveFromParentEnabled(false);

        rentalTabLayout.addTab(rentalTabLayout.newTab().setText("CURRENT BOOKING"));
        rentalTabLayout.addTab(rentalTabLayout.newTab().setText("SCHEDULE BOOKING"));


        rentalTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                rentalViewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        rentalViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                rentalTabLayout.selectTab(rentalTabLayout.getTabAt(position));
            }
        });


    }
}