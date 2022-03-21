package com.kiwni.app.user.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.kiwni.app.user.R;
import com.kiwni.app.user.activity.FindCarActivity;
import com.kiwni.app.user.adapter.HourPackageAdapter;
import com.kiwni.app.user.models.HourPackage;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;


public class CurrentBookingFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap mMap;
    RecyclerView currentBookingRecyclerView;
    List<HourPackage> hourPackageModelList;
    AppCompatButton rentalCurrentViewCabbtn;
    String direction = "";

    static CurrentBookingFragment myInstance;
    public synchronized static CurrentBookingFragment getInstance() {
        if (myInstance == null) {
            myInstance = new CurrentBookingFragment();
        }
        return myInstance;
    }

    public CurrentBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_current_booking, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {

        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentBookingRecyclerView = view.findViewById(R.id.currentBookingRecyclerView);
        rentalCurrentViewCabbtn = view.findViewById(R.id.rentalCurrentViewCabbtn);

        direction = "current-booking";

        hourPackageModelList = new ArrayList<>();

        hourPackageModelList.add(new HourPackage("2km","25 km"));
        hourPackageModelList.add(new HourPackage("4km","40 km"));
        hourPackageModelList.add(new HourPackage("6km","60 km"));
        hourPackageModelList.add(new HourPackage("8km","80 km"));
        hourPackageModelList.add(new HourPackage("10km","100 km"));
        hourPackageModelList.add(new HourPackage("12km","120 km"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        currentBookingRecyclerView.setLayoutManager(linearLayoutManager);

        HourPackageAdapter hourPackageAdapter = new HourPackageAdapter(getContext(),hourPackageModelList);
        currentBookingRecyclerView .setAdapter(hourPackageAdapter);

        rentalCurrentViewCabbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String titleNameRentalCurrent = "Rental (Current Trip)";

                PreferencesUtils.putPreferences(getActivity(), SharedPref.SERVICE_TYPE,"Rental");
                PreferencesUtils.putPreferences(getActivity(), SharedPref.DIRECTION,direction);

                Intent intent = new Intent(getActivity(), FindCarActivity.class);

                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if (mMap != null) {
            mMap.clear();
        }
        mMap = googleMap;
    }
}