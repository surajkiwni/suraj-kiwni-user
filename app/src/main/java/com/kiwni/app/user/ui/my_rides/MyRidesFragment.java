package com.kiwni.app.user.ui.my_rides;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.databinding.FragmentMyRidesBinding;
import com.kiwni.app.user.fragments.PastFragment;
import com.kiwni.app.user.fragments.UpcomingFragment;
import com.kiwni.app.user.interfaces.BackKeyPressedListener;
import com.google.android.material.tabs.TabLayout;
import com.kiwni.app.user.network.ConnectivityHelper;


public class MyRidesFragment extends Fragment implements BackKeyPressedListener
{
    String TAG = this.getClass().getSimpleName();
    public static MyRidesFragment instance;
    public static BackKeyPressedListener backKeyPressedListener;
    TabLayout tabLayout;
    View view;
    ImageView imgBack;
    TextView txtTitle;
    Fragment upcomingFragment, pastFragment;

    public MyRidesFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_my_rides, container, false);

        ((MainActivity) requireActivity()).getSupportActionBar().hide();

        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instance = this;
        tabLayout = (TabLayout) view.findViewById(R.id.myRidesTabLayout);
        imgBack = view.findViewById(R.id.imgBack);
        txtTitle = view.findViewById(R.id.txtTitle);

        txtTitle.setText("My Ride");


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0 :
                        replaceFragment(upcomingFragment);
                        break;
                    case 1 :
                        replaceFragment(pastFragment);
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

        upcomingFragment = new UpcomingFragment();
        pastFragment = new PastFragment();

        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"), true);
        tabLayout.addTab(tabLayout.newTab().setText("Past"));



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigation.findNavController(view).navigate(R.id.action_nav_myrides_to_mainActivity);
                Navigation.findNavController(requireView()).navigate(R.id.action_nav_myrides_to_mainActivity);
            }
        });

    }



    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.homeFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public static MyRidesFragment getInstance() {
        return instance;
    }


    @Override
    public void onPause() {
        backKeyPressedListener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        backKeyPressedListener = this;
    }

    @Override
    public void onBackPressed() {
        Navigation.findNavController(requireView()).navigate(R.id.action_nav_myrides_to_mainActivity);
    }


}