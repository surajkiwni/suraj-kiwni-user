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
import com.kiwni.app.user.adapter.OutstationViewPagerAdapter;

public class OutstationFragment extends Fragment {
    private OutstationViewPagerAdapter outstationViewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    String serviceType = "";

    View view;

    static OutstationFragment myInstance;
    public synchronized static OutstationFragment getInstance() {
        if (myInstance == null) {
            myInstance = new OutstationFragment();
        }
        return myInstance;
    }

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

        // initialize view Pager
        viewPager = view.findViewById(R.id.viewPager);

        //setting up adaptor with fragment
        outstationViewPagerAdapter = new OutstationViewPagerAdapter(this);

        //adaptor set to the view Pager
        viewPager.setAdapter(outstationViewPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager.setCurrentItem(0, true);

        tabLayout.addTab(tabLayout.newTab().setText("Round Trip"));
        tabLayout.addTab(tabLayout.newTab().setText("One Way"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
