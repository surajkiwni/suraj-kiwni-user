package com.kiwni.app.user.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;


public class ProfileFragment extends Fragment {


    TextView txtLogout;
    ImageView imageBack;

    public ProfileFragment() {
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

        ((MainActivity) requireActivity()).getSupportActionBar().hide();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageBack = view.findViewById(R.id.imageBack);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //  Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_mainActivity);
                // Create new fragment and transaction


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment profileFragment = new ProfileFragment();
                fragmentTransaction.add(R.id.profileFragment, profileFragment);
                fragmentTransaction.commit();



            }
        });

        return view;
    }
}