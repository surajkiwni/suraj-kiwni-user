package com.kiwni.app.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.activity.LoginActivity;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProfileFragment extends Fragment
{
    TextView txtLogout, txtEmailAddress, txtMobileNo, txtName, txtEmgContact;
    ImageView imageBack;

    String TAG = this.getClass().getSimpleName();

    String userName = "", mobileNumber = "", emailAddress = "", emergencyContact = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        ((MainActivity) requireActivity()).getSupportActionBar().hide();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageBack = view.findViewById(R.id.imageBack);
        txtLogout = view.findViewById(R.id.txtLogOut);
        txtEmailAddress = view.findViewById(R.id.txtEmailAddress);
        txtMobileNo = view.findViewById(R.id.txtMobileNo);
        txtName = view.findViewById(R.id.txtName);
        txtEmgContact = view.findViewById(R.id.txtEmgContact);

        //pref values
        userName = PreferencesUtils.getPreferences(getActivity(), SharedPref.FIREBASE_USERNAME, "");
        mobileNumber = PreferencesUtils.getPreferences(getActivity(), SharedPref.FIREBASE_MOBILE_NO, "");
        emailAddress = PreferencesUtils.getPreferences(getActivity(), SharedPref.FIREBASE_EMAIL, "");

        Log.d(TAG, "pref data = " + mobileNumber +": " + userName + ": " + emailAddress);

        txtName.setText(userName);
        txtEmailAddress.setText(emailAddress);

        if(!mobileNumber.equals(""))
        {
            txtMobileNo.setText(mobileNumber);
        }
        else
        {
            txtMobileNo.setText("");
            Toast.makeText(getActivity(), "not getting mobile no", Toast.LENGTH_SHORT).show();
        }

        txtEmgContact.setText(mobileNumber);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_mainActivity);
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to logout..")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog)
                            {
                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                PreferencesUtils.putPreferences(getActivity(), SharedPref.hasLoggedIn, false);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.FIREBASE_TOKEN, "");
                                FirebaseAuth.getInstance().signOut();

                                startActivity(i);
                                getActivity().finish();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });

        return view;
    }
}