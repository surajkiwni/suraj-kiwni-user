package com.kiwni.app.user.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kiwni.app.user.fragments.AirportDropFragment;
import com.kiwni.app.user.fragments.AirportFragment;
import com.kiwni.app.user.fragments.AirportPickupFragment;

public class AirportViewPagerAdapter extends FragmentStateAdapter {


    public AirportViewPagerAdapter(@NonNull AirportFragment fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position==0){
            return AirportPickupFragment.getInstance();
        }else{
            return AirportDropFragment.getInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
