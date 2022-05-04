package com.kiwni.app.user.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    ImageView imageBack,editImage;

    private static final int RESULT_CODE_FOR_GALLERY = 100;
    private static final int RESULT_CODE_FOR_CAMERA = 123;
    Uri imageUri;
    private Bitmap bitmap;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageBack = view.findViewById(R.id.imageBack);
        txtLogout = view.findViewById(R.id.txtLogOut);
        txtEmailAddress = view.findViewById(R.id.txtEmailAddress);
        txtMobileNo = view.findViewById(R.id.txtMobileNo);
        txtName = view.findViewById(R.id.txtName);
        txtEmgContact = view.findViewById(R.id.txtEmgContact);
        editImage = view.findViewById(R.id.imgProfile);

        ((MainActivity) getActivity()).getSupportActionBar().hide();

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

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.camera_profile_dialog, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
                AlertDialog profileShow = dialog.create();


                /*Dialog dialog = new Dialog(ThirdActivity.this, android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);*/

                TextView txtCamera = dialogView.findViewById(R.id.txtCamera);
                TextView txtGallery = dialogView.findViewById(R.id.txtGallery);
                TextView txtCancel = dialogView.findViewById(R.id.txtCancel);

                txtCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCamera();
                        profileShow.dismiss();

                    }
                });

                txtGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGallery();
                        profileShow.dismiss();
                    }
                });

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        profileShow.dismiss();
                    }
                });
                profileShow.show();
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

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, RESULT_CODE_FOR_GALLERY);

    }

    private void openCamera() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, RESULT_CODE_FOR_CAMERA);

        }else{

            Intent camera_intent
                    = new Intent(MediaStore
                    .ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent,RESULT_CODE_FOR_CAMERA);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_FOR_GALLERY){
            imageUri = data.getData();
            editImage.setImageURI(imageUri);
        }
        if (requestCode == RESULT_CODE_FOR_CAMERA) {

            // BitMap is data structure of image file
            // which stor the image in memory
            Bitmap photo = (Bitmap)data.getExtras()
                    .get("data");

            // Set the image in imageview for display
            editImage.setImageBitmap(photo);
        }




    }
}