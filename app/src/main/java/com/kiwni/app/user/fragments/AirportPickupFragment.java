package com.kiwni.app.user.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiwni.app.user.R;
import com.kiwni.app.user.activity.FindCarActivity;
import com.kiwni.app.user.pref.PreferencesUtils;
import com.kiwni.app.user.pref.SharedPref;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class AirportPickupFragment extends Fragment implements OnMapReadyCallback {


    GoogleMap mMap;
    AppCompatButton airportPickupViewCabOneWay;
    String direction = "",serviceType = "";
    static AirportPickupFragment myInstance;

    public synchronized static AirportPickupFragment getInstance() {
        if (myInstance == null) {
            myInstance = new AirportPickupFragment();
        }
        return myInstance;
    }

    public AirportPickupFragment() {
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
        View view = inflater.inflate(R.layout.fragment_airport_pickup, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {

        });







        airportPickupViewCabOneWay = view.findViewById(R.id.airportPickupViewCabOneWay);

        airportPickupViewCabOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                serviceType = "Airport";
                direction = "airport-pickup";
                PreferencesUtils.putPreferences(getActivity(),SharedPref.DIRECTION,direction);
                PreferencesUtils.putPreferences(getActivity(), SharedPref.SERVICE_TYPE,serviceType);


                Intent intent = new Intent(getActivity(), FindCarActivity.class);
               // intent.putExtra("serviceType", "Airport");
                //intent.putExtra("direction", direction);
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