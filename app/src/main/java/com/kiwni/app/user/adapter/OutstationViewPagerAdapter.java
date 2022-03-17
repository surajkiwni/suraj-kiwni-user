package com.kiwni.app.user.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.kiwni.app.user.fragments.OneWayFragment;
import com.kiwni.app.user.fragments.RoundTripFragment;

public class OutstationViewPagerAdapter extends FragmentStateAdapter {

    public OutstationViewPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return RoundTripFragment.getInstance();
        }else{
            return OneWayFragment.getInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
