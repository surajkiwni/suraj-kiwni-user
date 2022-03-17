package com.kiwni.app.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.UpcomingAdapter;


public class UpcomingFragment extends Fragment {

    View view;
    RecyclerView recyclerView1;
    UpcomingAdapter upcomingAdapter;


    String CarType[] ={"Luxury"};

    static UpcomingFragment myInstance;
    public synchronized static UpcomingFragment getInstance() {
        if (myInstance == null) {
            myInstance = new UpcomingFragment();
        }
        return myInstance;
    }

    public UpcomingFragment() {
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
        view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        recyclerView1 = view.findViewById(R.id.upcomingRecyclerView);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingAdapter =new UpcomingAdapter(getContext(),CarType);
        recyclerView1.setAdapter(upcomingAdapter);


        return view;
    }


}