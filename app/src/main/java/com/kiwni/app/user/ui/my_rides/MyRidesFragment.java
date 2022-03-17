package com.kiwni.app.user.ui.my_rides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.MyRidesViewPagerAdapter;
import com.kiwni.app.user.databinding.FragmentMyRidesBinding;
import com.kiwni.app.user.interfaces.BackKeyPressedListener;
import com.kiwni.app.user.ui.home.HomeFragment;
import com.google.android.material.tabs.TabLayout;


public class MyRidesFragment extends Fragment implements BackKeyPressedListener {

    private MyRidesViewModel myRidesViewModel;
    private FragmentMyRidesBinding binding;

    public static BackKeyPressedListener backKeyPressedListener;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MyRidesViewPagerAdapter myRidesViewPagerAdapter;

    HomeFragment homeFragment;



    View view;
    ImageView imageBack;
    TextView toolbarText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myRidesViewModel =
                new ViewModelProvider(this).get(MyRidesViewModel .class);

        binding = FragmentMyRidesBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //final TextView textView = binding.textMyRides;
        myRidesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });




        //set title to the support action bar

        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Your Title");




        return view;
    }

    @Override
    public void onBackPressed() {

        //Toast.makeText(getContext(), "Back Pressed", Toast.LENGTH_SHORT).show();

       // Navigation.findNavController(requireView()).navigate(R.id.action_nav_myrides_to_mainActivity);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        imageBack = view.findViewById(R.id.imageBack);


        // initialize view Pager
        ViewPager2 viewPager = (ViewPager2) view.findViewById(R.id.myRidesViewPager);

        //private ViewPagerAdapter viewPagerAdapter;
        myRidesViewPagerAdapter = new MyRidesViewPagerAdapter(this);




        viewPager.setAdapter(myRidesViewPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.myRidesTabLayout);
        viewPager.setCurrentItem(0, true);


        //Add Fragment


        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Past"));


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


        ///

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Navigation.findNavController(view).navigate(R.id.action_nav_myrides_to_mainActivity);
                Navigation.findNavController(requireView()).navigate(R.id.action_nav_myrides_to_mainActivity);

            }
        });

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
}