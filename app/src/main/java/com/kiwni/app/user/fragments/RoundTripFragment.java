package com.kiwni.app.user.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

public class RoundTripFragment extends Fragment implements OnMapReadyCallback {


    AppCompatButton viewCabsRoundTrip;
    GoogleMap mMap;
    String direction = "",serviceType = "";

    static RoundTripFragment myInstance;
    public synchronized static RoundTripFragment getInstance() {
        if (myInstance == null) {
            myInstance = new RoundTripFragment();
        }
        return myInstance;
    }

    public RoundTripFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_round_trip, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {

        });



        //final NavController navController = Navigation.findNavController(view);
        viewCabsRoundTrip = view.findViewById(R.id.btnViewCabRoundTrip);

        viewCabsRoundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serviceType = "Outstation";
                direction = "two-way";

                PreferencesUtils.putPreferences(getActivity(), SharedPref.SERVICE_TYPE, serviceType);
                PreferencesUtils.putPreferences(getActivity(), SharedPref.DIRECTION,direction);

                Intent intent = new Intent(getActivity(), FindCarActivity.class);
               // intent.putExtra("serviceType", "Outstation");
                //intent.putExtra("direction", direction);
                startActivity(intent);

            }


        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d("TAG", "OnMapReady");
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