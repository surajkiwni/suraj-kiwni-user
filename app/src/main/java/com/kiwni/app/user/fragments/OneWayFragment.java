package com.kiwni.app.user.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.activity.FindCarActivity;
import com.kiwni.app.user.pref.PreferencesUtils;
import com.kiwni.app.user.pref.SharedPref;


public class OneWayFragment extends Fragment {


    AppCompatButton viewCabsOneWay;
    TextView oneWayText;
    String direction = "",serviceType = "";

    static OneWayFragment myInstance;

    public synchronized static OneWayFragment getInstance() {
        if (myInstance == null) {
            myInstance = new OneWayFragment();
        }
        return myInstance;
    }


    public OneWayFragment() {
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
        return inflater.inflate(R.layout.fragment_one_way, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewCabsOneWay = view.findViewById(R.id.btnViewCabOneWay);




       // final NavController navController = Navigation.findNavController(view);

       AppCompatButton viewCabsOneWay= view.findViewById(R.id.btnViewCabOneWay);

        viewCabsOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                direction = "one-way";
                serviceType = "Outstation";
                PreferencesUtils.putPreferences(getActivity(), SharedPref.DIRECTION,direction);
                PreferencesUtils.putPreferences(getActivity(), SharedPref.SERVICE_TYPE, serviceType);



                Intent intent = new Intent(getActivity(), FindCarActivity.class);
                //intent.putExtra("serviceType", "Outstation");
               // intent.putExtra("direction", direction);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}