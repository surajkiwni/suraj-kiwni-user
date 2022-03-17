package com.kiwni.app.user.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kiwni.app.user.fragments.AirportFragment;
import com.kiwni.app.user.fragments.OutstationFragment;
import com.kiwni.app.user.fragments.RentalFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final int NUM_TABS = 3;
    String serviceType = "";
    Context context;

    public ViewPagerAdapter(Fragment fragment) {
        super(fragment);
        context = fragment.getActivity();
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){

            return OutstationFragment.getInstance();
        }else if(position ==1){

            return AirportFragment.getInstance();
        }else {

            return RentalFragment.getInstance();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }

}