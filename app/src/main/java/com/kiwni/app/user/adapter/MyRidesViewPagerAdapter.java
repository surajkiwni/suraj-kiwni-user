package com.kiwni.app.user.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kiwni.app.user.fragments.PastFragment;
import com.kiwni.app.user.fragments.UpcomingFragment;

public class MyRidesViewPagerAdapter extends FragmentStateAdapter {

    public MyRidesViewPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return UpcomingFragment.getInstance();
        }else{
            return PastFragment.getInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
