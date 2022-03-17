package com.kiwni.app.user.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiwni.app.user.R;
import com.kiwni.app.user.activity.FindCarActivity;
import com.kiwni.app.user.adapter.HourPackageAdapter;
import com.kiwni.app.user.datamodels.HourPackageModel;
import com.kiwni.app.user.pref.PreferencesUtils;
import com.kiwni.app.user.pref.SharedPref;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;


public class ScheduleBookingFragment extends Fragment  implements OnMapReadyCallback {


    GoogleMap mMap;
    RecyclerView scheduleBookingRecyclerView;
    List<HourPackageModel> hourPackageModelList;
    AppCompatButton rentalScheduleViewCabbtn;
    String direction = "",serviceType = "";

    static ScheduleBookingFragment myInstance;
    public synchronized static ScheduleBookingFragment getInstance() {
        if (myInstance == null) {
            myInstance = new ScheduleBookingFragment();
        }
        return myInstance;
    }

    public ScheduleBookingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_schedule_booking, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {

        });

        rentalScheduleViewCabbtn = view.findViewById(R.id.rentalScheduleViewCabbtn);
        scheduleBookingRecyclerView = view.findViewById(R.id.scheduleBookingRecyclerView);

        hourPackageModelList = new ArrayList<>();

        hourPackageModelList.add(new HourPackageModel("2km","25 km"));
        hourPackageModelList.add(new HourPackageModel("4km","40 km"));
        hourPackageModelList.add(new HourPackageModel("6km","60 km"));
        hourPackageModelList.add(new HourPackageModel("8km","80 km"));
        hourPackageModelList.add(new HourPackageModel("10km","100 km"));
        hourPackageModelList.add(new HourPackageModel("12km","120 km"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        scheduleBookingRecyclerView.setLayoutManager(linearLayoutManager);

        HourPackageAdapter hourPackageAdapter = new HourPackageAdapter(getContext(),hourPackageModelList);
        scheduleBookingRecyclerView.setAdapter(hourPackageAdapter);

        rentalScheduleViewCabbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                direction = "schedule-booking";
                serviceType = "Rental";
                PreferencesUtils.putPreferences(getActivity(),SharedPref.DIRECTION,direction);
                PreferencesUtils.putPreferences(getActivity(),SharedPref.SERVICE_TYPE,serviceType);

                Intent intent = new Intent(getActivity(), FindCarActivity.class);
                startActivity(intent);

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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}