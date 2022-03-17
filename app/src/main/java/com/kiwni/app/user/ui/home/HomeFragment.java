package com.kiwni.app.user.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;


import com.kiwni.app.user.R;

import com.kiwni.app.user.adapter.ViewPagerAdapter;
import com.kiwni.app.user.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment implements   OnMapReadyCallback {

    private HomeViewModel homeViewModel;

    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FragmentHomeBinding binding;
    Fragment homeFragment;

    GoogleMap mMap;

    String serviceType = "";
    View root;

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

        // initialize view Pager
        viewPager2 = view.findViewById(R.id.viewPager);

        //setting up adaptor with fragment
        viewPagerAdapter = new ViewPagerAdapter(this);


        //adaptor set to the view Pager
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager2.setCurrentItem(0, true);


        tabLayout.addTab(tabLayout.newTab().setText("Outstation"));
        tabLayout.addTab(tabLayout.newTab().setText("Airport"));
        tabLayout.addTab(tabLayout.newTab().setText("Rental"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });




    }


    @Override
    public void onDestroy() {
        super.onDestroy();

       /* FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        HomeFragment homeFragment = (HomeFragment) fm.findFragmentByTag("tag");

        if(homeFragment == null) {  // not added
            homeFragment = new HomeFragment();
            ft.add(R.id., f, "tag");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        } else {  // already added

            ft.remove(f);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        }*/
        FragmentManager fm = getParentFragmentManager();
        HomeFragment homeFragment = (HomeFragment) fm.findFragmentByTag("fragment_tag");
        if (homeFragment != null) {
            fm.beginTransaction().remove(homeFragment).commit();
        }


    }

    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Log.d("TAG", "OnMapReady");
        if (mMap != null) {
            mMap.clear();
        }
        mMap = googleMap;
    }



}