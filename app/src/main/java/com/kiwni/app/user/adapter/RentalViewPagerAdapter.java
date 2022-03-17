package com.kiwni.app.user.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kiwni.app.user.fragments.CurrentBookingFragment;
import com.kiwni.app.user.fragments.RentalFragment;
import com.kiwni.app.user.fragments.ScheduleBookingFragment;

public class RentalViewPagerAdapter extends FragmentStateAdapter {

    public RentalViewPagerAdapter(@NonNull RentalFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return CurrentBookingFragment.getInstance();
        }else{
            return ScheduleBookingFragment.getInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
