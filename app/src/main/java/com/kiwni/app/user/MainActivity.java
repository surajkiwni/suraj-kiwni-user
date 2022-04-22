package com.kiwni.app.user;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.kiwni.app.user.R;
import com.kiwni.app.user.databinding.ActivityMainBinding;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.kiwni.app.user.models.socket.SocketReservationResp;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.socket.SocketSingletonClass;
import com.kiwni.app.user.ui.FAQs.FaqFragment;
import com.kiwni.app.user.ui.about.AboutFragment;
import com.kiwni.app.user.ui.my_rides.MyRidesFragment;
import com.kiwni.app.user.ui.offers.OffersFragment;
import com.kiwni.app.user.ui.payment.PaymentFragment;
import com.kiwni.app.user.ui.refer.ReferEarnFragment;
import com.kiwni.app.user.ui.safety.SafetyFragment;
import com.kiwni.app.user.ui.support.SupportFragment;
import com.kiwni.app.user.utils.PreferencesUtils;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    String TAG = this.getClass().getSimpleName();
    public DrawerLayout drawerLayout;
    NavController navController;
    ConstraintLayout constraintProfile;
    TextView txtHeaderUserMobile, txtHeaderName;
    BottomSheetDialog bottomSheetDialog;
    String mobile = "", userName = "", mobileNumber = "";
    int partyId = 0;
    MenuItem action_like;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1001;

    /* socket */
    Socket mSocket;
    SocketReservationResp reservationResp = new SocketReservationResp();
    Context context;
    boolean driver_data_updated = false;
    byte[] valueDecoded= new byte[0];
    String convertedDateTime = "";
    AlertDialog b;
    AlertDialog.Builder dialogBuilder;
    List<SocketReservationResp> reservationRespList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getSupportActionBar().setTitle("title");

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // R.id.nav_home R.id.nav_myrides
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)

                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtHeaderName = headerView.findViewById(R.id.txtHeaderName);
        txtHeaderUserMobile = headerView.findViewById(R.id.txtHeaderUserMobile);
        constraintProfile = headerView.findViewById(R.id.constraintProfile);

        constraintProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                navController.navigate(R.id.action_nav_home_to_profileFragment);
                drawerLayout.close();
            }
        });

        /* get data from pref */
        partyId = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.partyId, 0);
        Log.d(TAG, "partyId = " + partyId);

        userName = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_USERNAME, "");
        mobileNumber = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_MOBILE_NO, "");

        txtHeaderName.setText(userName);
        if(mobileNumber.contains("+91"))
        {
            mobileNumber = mobileNumber.replaceAll("\\+","").replaceFirst("91","");
            txtHeaderUserMobile.setText(mobileNumber);
        }

        context = getApplicationContext();

        SocketConnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_call:

                mobile = "7057052508";
                Uri call = Uri.parse("tel:" + mobile);
                Intent intent = new Intent(Intent.ACTION_CALL, call);

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(intent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
                return true;

            case R.id.action_like:

                Toast.makeText(getApplicationContext(), "Item 2 Selected", Toast.LENGTH_LONG).show();
                bottomSheetDialog = new BottomSheetDialog(this);
                View view = getLayoutInflater().inflate(R.layout.favorite_bottom_sheet, null, false);

                bottomSheetDialog.show();
                bottomSheetDialog.setCancelable(false);


                AppCompatButton cancelButton = view.findViewById(R.id.cancelButton);
                AppCompatButton saveButton = view.findViewById(R.id.saveButton);

                saveButton.setBackgroundColor(Color.BLACK);
                saveButton.setTextColor(Color.WHITE);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveButton.setBackgroundColor(Color.TRANSPARENT);
                        saveButton.setTextColor(Color.BLACK);
                        cancelButton.setBackgroundColor(Color.BLACK);
                        cancelButton.setTextColor(Color.WHITE);
                    }
                });

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(view);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the phone call

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            /* return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();*/

        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }


        if (MyRidesFragment.backKeyPressedListener!= null){

            MyRidesFragment.backKeyPressedListener.onBackPressed();
        }

        if (PaymentFragment.backKeyPressedListener!= null){

            PaymentFragment.backKeyPressedListener.onBackPressed();
        }

        if (OffersFragment.backKeyPressedListener!= null){

            OffersFragment.backKeyPressedListener.onBackPressed();
        }
        if (SafetyFragment.backKeyPressedListener!= null){

            SafetyFragment.backKeyPressedListener.onBackPressed();
        }
        if (FaqFragment.backKeyPressedListener!= null){

            FaqFragment.backKeyPressedListener.onBackPressed();
        }
        if (ReferEarnFragment.backKeyPressedListener!= null){

            ReferEarnFragment.backKeyPressedListener.onBackPressed();
        }
        if (SupportFragment.backKeyPressedListener!= null){

            SupportFragment.backKeyPressedListener.onBackPressed();
        }
        if (AboutFragment.backKeyPressedListener!= null){

            AboutFragment.backKeyPressedListener.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        drawerLayout.closeDrawers();

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_shareapp:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;

            case R.id.nav_myrides:

                navController.navigate(R.id.action_nav_home_to_nav_myrides);

                break;

            case R.id.nav_payment:

                navController.navigate(R.id.action_nav_home_to_nav_payment);
                break;

            case R.id.nav_offers:

                navController.navigate(R.id.action_nav_home_to_nav_offers);
                break;

            case R.id.nav_safety:

                navController.navigate(R.id.action_nav_home_to_nav_safety);
                break;

            case R.id.nav_faq:
                navController.navigate(R.id.action_nav_home_to_nav_faq);

                break;

            case R.id.nav_feedback:

                Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.feedback_bottom_sheet);

                final ImageView imageClose = dialog.findViewById(R.id.imageClose);

                imageClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();

                break;

            case R.id.nav_refer:

                navController.navigate(R.id.action_nav_home_to_nav_refer);
                break;

            case R.id.nav_support:

                navController.navigate(R.id.action_nav_home_to_nav_support);
                break;

            case R.id.nav_about:

                navController.navigate(R.id.action_nav_home_to_nav_about);
                break;
        }

        return true;
    }

    /* socket */
    public void SocketConnect()
    {
        //Socket Listen
        try {
            mSocket = IO.socket(AppConstants.SOCKET_BASE_URL);
            mSocket.connect();

            mSocket.on(Socket.EVENT_CONNECT,onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR,onConnectError);

            EmitData();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void EmitData()
    {
        //{"partyId": 274}
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("partyId", partyId);
            mSocket.emit("join", obj);
            Log.d("join", obj.toString());

            /* reservation success */
            ListenReservationMessage();

            //ListenDriverDataMessage();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            Log.e("IsConnected", String.valueOf(mSocket.connected()));
            Log.d(TAG, "connected to the server");
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "connection error");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "disconnected from the server");
        }
    };

    private final Emitter.Listener onNewMessage = new Emitter.Listener()
    {
        @Override
        public void call(final Object... args)
        {
            runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    Log.d(TAG, "in new msg");
                    Log.d(TAG, "data in new msg = " + Arrays.toString(args));

                    if (args.length == 0)
                    {
                        //Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        try {
                            JSONObject jsonObject = (JSONObject) args[0];

                            Gson gson = new Gson();
                            reservationResp = gson.fromJson(jsonObject.toString(), SocketReservationResp.class);

                            Log.d(TAG, "reservationResp = " + reservationResp.toString());
                            Log.d(TAG, "reservationResp id = " + reservationResp.getId());

                            reservationRespList.clear();
                            reservationRespList.add(reservationResp);
                            Log.d(TAG, "reservationRespList in new msg = " + reservationRespList.size());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };

    private final Emitter.Listener onUpdatedMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    Log.d(TAG, "in updated msg");
                    Log.d(TAG, "data = " + Arrays.toString(args));

                    driver_data_updated = true;

                    if (args.length == 0)
                    {
                        //Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        try {
                            JSONObject jsonObject = (JSONObject) args[0];

                            Gson gson = new Gson();
                            reservationResp = gson.fromJson(jsonObject.toString(), SocketReservationResp.class);

                            Log.d(TAG, "reservationResp = " + reservationResp.toString());
                            Log.d(TAG, "reservationResp id = " + reservationResp.getId());

                            reservationRespList.clear();
                            reservationRespList.add(reservationResp);
                            Log.d(TAG, "reservationRespList in update msg = " + reservationRespList.size());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };

    public void ListenDriverDataMessage()
    {
        mSocket.on(AppConstants.WEBSOCKET_DRIVER_DATA_EVENT, onUpdatedMessage);
    }

    public void ListenReservationMessage()
    {
        mSocket.on(AppConstants.WEBSOCKET_RESERVATION_EVENT, onNewMessage);
    }

    public void DisplaySuccessDialog(List<SocketReservationResp> reservationRespList)
    {
        //create custom dialog here
        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reservation_confirmation_krn_no_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();

        TextView txtKRNno = dialogView.findViewById(R.id.txtKRNno);
        ImageView imgDriverImg = dialogView.findViewById(R.id.imgDriverImg);
        TextView txtDriverName = dialogView.findViewById(R.id.txtDriverName);
        TextView txtOtp = dialogView.findViewById(R.id.txtOtp);
        TextView txtMobile = dialogView.findViewById(R.id.txtMobile);
        TextView txtVehicleNo = dialogView.findViewById(R.id.txtVehicleNo);
        TextView txtServiceType = dialogView.findViewById(R.id.txtServiceType);
        TextView txtStartDateTime = dialogView.findViewById(R.id.txtStartDateTime);
        TextView txtEstimatedFare = dialogView.findViewById(R.id.txtEstimatedFare);
        AppCompatButton btnDone = dialogView.findViewById(R.id.btnDone);

        /* set data to view */
        Log.d(TAG, "reservation id = " + reservationRespList.get(0).getReservationId());
        String KrnNo = "Your KRN number is" + "<b> " + reservationRespList.get(0).getReservationId() + "</b>" + ". Your ride is scheduled & will send your driver details within few hours.";
        txtKRNno.setText(Html.fromHtml(KrnNo));

        Log.d(TAG, "name = " + reservationRespList.get(0).getDriver().getName().trim());
        String driver_name = reservationRespList.get(0).getDriver().getName();
        String driver_mobile = reservationRespList.get(0).getDriver().getMobile();

        Log.d(TAG, "driver_data_updated before check= " + driver_data_updated);
        if(driver_data_updated)
        {
            Log.d(TAG, "driver_data_updated true = " + driver_data_updated);
            Log.d("TAG", "image = " + reservationRespList.get(0).getDriverImageUrl());

            txtDriverName.setVisibility(View.VISIBLE);
            txtDriverName.setText(driver_name);

            txtMobile.setVisibility(View.VISIBLE);
            txtMobile.setText("Contact : " + driver_mobile);

            if(reservationRespList.get(0).getDriverImageUrl() != null)
            {
                imgDriverImg.setVisibility(View.VISIBLE);
                Log.d(TAG, "image = " + reservationRespList.get(0).getDriverImageUrl());

                Glide.with(context)
                        .load(reservationRespList.get(0).getDriverImageUrl())
                        .into(imgDriverImg);
            }
            else
            {
                imgDriverImg.setVisibility(View.GONE);
            }

            //vehicle no
            if(reservationRespList.get(0).getVehcileNo() != null)
            {
                txtVehicleNo.setVisibility(View.VISIBLE);
                txtVehicleNo.setText("Vehicle No : " + reservationRespList.get(0).getVehcileNo());
            }
            else
            {
                txtVehicleNo.setVisibility(View.GONE);
            }
        }
        else
        {
            driver_data_updated = false;
            Log.d(TAG, "driver_data_updated false = " + driver_data_updated);

            if(driver_name.equals("null") || driver_name.equals(""))
            {
                txtDriverName.setVisibility(View.GONE);

                if(driver_mobile.equals("null") || driver_name.equals(""))
                {
                    txtMobile.setVisibility(View.GONE);
                }
                else
                {
                    txtMobile.setVisibility(View.VISIBLE);
                    txtMobile.setText("Contact : " + driver_mobile);

                    if(reservationRespList.get(0).getDriverImageUrl() != null)
                    {
                        imgDriverImg.setVisibility(View.VISIBLE);
                        Log.d(TAG, "image = " + reservationRespList.get(0).getDriverImageUrl());
                    }
                    else
                    {
                        imgDriverImg.setVisibility(View.GONE);
                    }
                }
            }
            else
            {
                txtDriverName.setVisibility(View.VISIBLE);
                txtDriverName.setText(driver_name);
            }
        }

        //estimated fare
        if(reservationRespList.get(0).getEstimatedPrice() != null)
        {
            txtEstimatedFare.setVisibility(View.VISIBLE);
            Float estimatedFare = Float.parseFloat(String.valueOf(reservationRespList.get(0).getEstimatedPrice()));
            txtEstimatedFare.setText("Rs. " + String.format("%.2f",estimatedFare));
        }
        else
        {
            txtEstimatedFare.setVisibility(View.GONE);
        }

        //decode otp
        ConvertOtp(reservationRespList.get(0).getOtp());
        txtOtp.setText("OTP: " + new String(valueDecoded));

        txtServiceType.setText(reservationRespList.get(0).getServiceType() + " To " + reservationRespList.get(0).getEndlocationCity());

        //convert date in format
        ConvertDate(reservationRespList.get(0).getStartTime());
        txtStartDateTime.setText(convertedDateTime);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                b.dismiss();
            }
        });
        b.show();
    }

    public void ConvertOtp(String otp)
    {
        //convert using Base64 decoder
        try {
            valueDecoded = Base64.decode(otp.getBytes("UTF-8"), Base64.DEFAULT);
            Log.d(TAG, "valueDecoded = " + new String(valueDecoded));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    public void ConvertDate(String actualdate)
    {
        Log.d(TAG, "actualDate = " + actualdate);

        Date startDate = null;
        if (actualdate.length() == 24)
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
                startDate = sdf.parse(actualdate);

                //startTime = sdf.parse(time);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM, hh:mm a");
                convertedDateTime = sdf2.format(startDate);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "message = " + e.getMessage());
            }
        }
        else if (actualdate.length() == 20) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                startDate = sdf.parse(actualdate);

                //startTime = sdf.parse(time);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM, hh:mm a");
                convertedDateTime = sdf2.format(startDate);
                Log.d(TAG, "convertedDateTime = " + convertedDateTime);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.d(TAG, "message = " + e.getMessage());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Given date is not in correct format.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        //SocketConnect();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "in onResume method ");

        SocketConnect();
        //Toast.makeText(getApplicationContext(), "on Resume()", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "reservation resp list size in on resume = " + reservationRespList.size());
        if(reservationRespList.size() != 0)
        {
            DisplaySuccessDialog(reservationRespList);
        }
        else
        {
            Log.d(TAG, "reservation resp list size is empty = " + reservationRespList.size());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        mSocket.disconnect();
    }
}