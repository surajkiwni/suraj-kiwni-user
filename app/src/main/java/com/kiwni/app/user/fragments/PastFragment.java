package com.kiwni.app.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.PastAdapter;

public class PastFragment extends Fragment
{
    RecyclerView pastRecyclerView;
    View view;
    PastAdapter pastAdapter;

    String pickupAdd[] ={"Luxury"};

    static PastFragment myInstance;
    public synchronized static PastFragment getInstance() {
        if (myInstance == null) {
            myInstance = new PastFragment();
        }
        return myInstance;
    }

    public PastFragment() {
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
        view = inflater.inflate(R.layout.fragment_past, container, false);

        pastRecyclerView = view.findViewById(R.id.pastRecyclerView);
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pastAdapter =new PastAdapter(getContext(),pickupAdd);
        pastRecyclerView.setAdapter(pastAdapter);

        return view;
    }
}