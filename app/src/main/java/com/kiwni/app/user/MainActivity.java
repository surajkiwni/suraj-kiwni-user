package com.kiwni.app.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.kiwni.app.user.databinding.ActivityMainBinding;
import com.kiwni.app.user.global.PermissionRequestConstant;
import com.kiwni.app.user.models.socket.SocketReservationResp;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.network.ConnectivityHelper;
import com.kiwni.app.user.sharedpref.SharedPref;
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
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, ConnectivityHelper.NetworkStateReceiverListener
{
    String TAG = this.getClass().getSimpleName();
    public DrawerLayout drawerLayout;
    NavController navController;
    ConstraintLayout constraintProfile;
    TextView txtHeaderUserMobile, txtHeaderName;
    BottomSheetDialog bottomSheetDialog;
    String mobile = "", userName = "", mobileNumber = "";
    int partyId = 0;
    Handler handler = new Handler();

    /* socket */
    Socket mSocket;
    SocketReservationResp reservationResp = new SocketReservationResp();
    Context context;
    boolean driver_data_updated = false, isValid = true, isRefresh = false;
    byte[] valueDecoded= new byte[0];
    String convertedStartDateTime = "", stringData = "", splittedStr1 ="", splittedStr2 = "",
            splittedStr3 = "", splittedStr4 = "", concatDirection = "", convertedEndDate = "";
    AlertDialog success_alert, data_updated_alert;
    AlertDialog.Builder dialogBuilder;
    List<SocketReservationResp> driverDataRespList = new ArrayList<>();
    List<SocketReservationResp> socketSuccessRespList = new ArrayList<>();
    private ConnectivityHelper connectivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        startNetworkBroadcastReceiver(this);

        /* auto refresh */
        doTheAutoRefresh();

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getSupportActionBar().setTitle("title");

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // R.id.nav_home R.id.nav_myrides
        AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            // Setup NavigationUI here
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }
        //navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);


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

    @SuppressLint({"InflateParams", "NonConstantResourceId"})
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
                            PermissionRequestConstant.MY_PERMISSIONS_REQUEST_CALL_PHONE);

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

                Toast.makeText(getApplicationContext(), "Save Address", Toast.LENGTH_LONG).show();
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
                    public void onClick(View view)
                    {
                        saveButton.setBackgroundColor(Color.TRANSPARENT);
                        saveButton.setTextColor(Color.BLACK);
                        cancelButton.setBackgroundColor(Color.BLACK);
                        cancelButton.setTextColor(Color.WHITE);
                        bottomSheetDialog.dismiss();
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
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }  //super.onBackPressed();


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

    @SuppressLint({"InflateParams", "NonConstantResourceId"})
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

                if(ConnectivityHelper.isConnected){
                    navController.navigate(R.id.action_nav_home_to_nav_myrides);
                }
                else{
                    Snackbar.make(findViewById(android.R.id.content), R.string.no_internet_msg, Snackbar.LENGTH_LONG)
                            .setTextColor(Color.WHITE)
                            .setBackgroundTint(Color.RED)
                            .setDuration(5000)
                            .show();
                }

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

                BottomSheetDialog dialog = new BottomSheetDialog(this);
                View view = getLayoutInflater().inflate(R.layout.feedback_bottom_sheet,null,false);

                dialog.setCancelable(false);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        BottomSheetDialog d = (BottomSheetDialog) dialog;

                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                });
                dialog.setContentView(view);


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

            ListenDriverDataMessage();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private final Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            Log.e("IsConnected", String.valueOf(mSocket.connected()));
            Log.d(TAG, "connected to the server");
        }
    };

    private final Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "connection error");
        }
    };

    private final Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "disconnected from the server");
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

                            driverDataRespList.clear();
                            driverDataRespList.add(reservationResp);
                            Log.d(TAG, "reservationRespList in update msg = " + driverDataRespList.size());

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

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void DisplayDriverDataUpdatedDialog(List<SocketReservationResp> driverDataRespList)
    {
        //create custom dialog here
        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_socket_response, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        data_updated_alert = dialogBuilder.create();

        TextView txtKRNno = dialogView.findViewById(R.id.txtKRNno);
        ImageView imgDriverImg = dialogView.findViewById(R.id.imgDriverImg);
        ImageView imgCallToDriver = dialogView.findViewById(R.id.imgCallToDriver);
        TextView txtDriverName = dialogView.findViewById(R.id.txtDriverName);
        TextView txtOtp = dialogView.findViewById(R.id.txtOtp);
        TextView txtMobile = dialogView.findViewById(R.id.txtMobile);
        TextView txtVehicleNo = dialogView.findViewById(R.id.txtVehicleNo);
        TextView txtServiceType = dialogView.findViewById(R.id.txtServiceType);
        TextView txtStartDateTime = dialogView.findViewById(R.id.txtStartDateTime);
        TextView txtEstimatedFare = dialogView.findViewById(R.id.txtEstimatedFare);
        AppCompatButton btnDone = dialogView.findViewById(R.id.btnDone);

        /* set data to view */
        Log.d(TAG, "reservation id = " + driverDataRespList.get(0).getReservationId());
        String KrnNo = R.string.your_krn_no_is + "<b> " + driverDataRespList.get(0).getReservationId() + "</b>" + ".";
        txtKRNno.setText(Html.fromHtml(KrnNo));

        Log.d(TAG, "name = " + driverDataRespList.get(0).getDriver().getName().trim());
        String driver_name = driverDataRespList.get(0).getDriver().getName();
        String driver_mobile = driverDataRespList.get(0).getDriver().getMobile();

        if(driver_data_updated)
        {
            txtDriverName.setVisibility(View.VISIBLE);
            txtDriverName.setText(driver_name);

            txtMobile.setVisibility(View.VISIBLE);
            txtMobile.setText("Contact : " + driver_mobile);

            imgCallToDriver.setVisibility(View.VISIBLE);

            imgCallToDriver.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Uri call = Uri.parse("tel:" + txtMobile.getText().toString());
                    Intent intent = new Intent(Intent.ACTION_CALL, call);

                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                PermissionRequestConstant.MY_PERMISSIONS_REQUEST_CALL_PHONE);

                        // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    } else {
                        //You already have permission
                        try {
                            startActivity(intent);
                        } catch(SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            if(driverDataRespList.get(0).getDriverImageUrl() != null)
            {
                imgDriverImg.setVisibility(View.VISIBLE);
                Log.d(TAG, "image = " + driverDataRespList.get(0).getDriverImageUrl());

                Glide.with(context)
                        .load(driverDataRespList.get(0).getDriverImageUrl())
                        .into(imgDriverImg);
            }
            else
            {
                imgDriverImg.setVisibility(View.GONE);
            }

            //vehicle no
            if(driverDataRespList.get(0).getVehcileNo() != null)
            {
                txtVehicleNo.setVisibility(View.VISIBLE);
                txtVehicleNo.setText("Vehicle No : " + driverDataRespList.get(0).getVehcileNo());
            }
            else
            {
                txtVehicleNo.setVisibility(View.GONE);
            }
        }

        //estimated fare
        if(driverDataRespList.get(0).getEstimatedPrice() != null)
        {
            txtEstimatedFare.setVisibility(View.VISIBLE);
            Float estimatedFare = Float.parseFloat(String.valueOf(driverDataRespList.get(0).getEstimatedPrice()));
            txtEstimatedFare.setText("Rs. " + String.format("%.2f",estimatedFare));
        }
        else
        {
            txtEstimatedFare.setVisibility(View.GONE);
        }

        //decode otp
        ConvertOtp(driverDataRespList.get(0).getOtp());
        txtOtp.setText("OTP : " + new String(valueDecoded));

        /* split service type */
        GetClassTypeFromServiceType(driverDataRespList.get(0).getServiceType());

        txtServiceType.setText(concatDirection + "-trip" + " To " + driverDataRespList.get(0).getEndlocationCity());

        //convert start date in format
        ConvertDate(driverDataRespList.get(0).getStartTime());
        txtStartDateTime.setText(convertedStartDateTime);

        //convert end date in format
        ConvertDate(driverDataRespList.get(0).getEndTime());
        if(concatDirection.equals("Two-way"))
        {
            txtStartDateTime.setText(txtStartDateTime.getText().toString() + " - " + convertedEndDate);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                data_updated_alert.dismiss();
            }
        });
        data_updated_alert.show();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void DisplaySuccessDialog(List<SocketReservationResp> socketReservationRespList)
    {
        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_socket_response, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        success_alert = dialogBuilder.create();

        TextView txtKRNno = dialogView.findViewById(R.id.txtKRNno);
        TextView txtOtp = dialogView.findViewById(R.id.txtOtp);
        TextView txtServiceType = dialogView.findViewById(R.id.txtServiceType);
        TextView txtStartDateTime = dialogView.findViewById(R.id.txtStartDateTime);
        TextView txtEstimatedFare = dialogView.findViewById(R.id.txtEstimatedFare);
        AppCompatButton btnDone = dialogView.findViewById(R.id.btnDone);

        /* set data to view */
        Log.d(TAG, "reservation id = " + socketReservationRespList.get(0).getReservationId());
        String KrnNo = R.string.your_krn_no_is + "<b> " + socketReservationRespList.get(0).getReservationId() + "</b>" + ". Your ride is scheduled & will send your driver details within few hours.";
        txtKRNno.setText(Html.fromHtml(KrnNo));

        //estimated fare
        if(socketReservationRespList.get(0).getEstimatedPrice() != null)
        {
            txtEstimatedFare.setVisibility(View.VISIBLE);
            Float estimatedFare = Float.parseFloat(String.valueOf(socketReservationRespList.get(0).getEstimatedPrice()));
            txtEstimatedFare.setText("Rs. " + String.format("%.2f",estimatedFare));
        }
        else
        {
            txtEstimatedFare.setVisibility(View.GONE);
        }

        //decode otp
        ConvertOtp(socketReservationRespList.get(0).getOtp());
        txtOtp.setText("OTP : " + new String(valueDecoded));

        /* split service type */
        GetClassTypeFromServiceType(socketReservationRespList.get(0).getServiceType());

        txtServiceType.setText(concatDirection + "-trip" + " To " + socketReservationRespList.get(0).getEndlocationCity());

        //convert date in format
        ConvertDate(socketReservationRespList.get(0).getStartTime());
        txtStartDateTime.setText(convertedStartDateTime);

        //convert end date in format
        ConvertDate(socketReservationRespList.get(0).getEndTime());
        if(concatDirection.equals("Two-way"))
        {
            txtStartDateTime.setText(txtStartDateTime.getText().toString() + " - " + convertedEndDate);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                success_alert.dismiss();
            }
        });
        success_alert.show();
    }

    public void GetClassTypeFromServiceType(String str)
    {
        String[] split = str.split("-");
        for (String s : split)
        {
            System.out.println(s);
            splittedStr1 = split[0];
            splittedStr2 = split[1];
            splittedStr3 = split[2];
            splittedStr4 = split[3];
        }
        Log.d(TAG, "data print from array = " + splittedStr1 + ", " + splittedStr2 +
                ", " + splittedStr3 + ", " + splittedStr4);

        concatDirection = splittedStr1.substring(0, 1).toUpperCase() + splittedStr1.substring(1).toLowerCase() + "-" + splittedStr2;
        Log.d(TAG, "concatDirection = " + concatDirection);
    }

    public void ConvertOtp(String otp)
    {
        //convert using Base64 decoder
        valueDecoded = Base64.decode(otp.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        Log.d(TAG, "valueDecoded = " + new String(valueDecoded));
    }

    @SuppressLint("SimpleDateFormat")
    public void ConvertDate(String start_date)
    {
        Log.d(TAG, "actualDate = " + start_date);

        Date startDate;
        if (start_date.length() == 24)
        {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
                startDate = sdf.parse(start_date);

                //startTime = sdf.parse(time);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM, hh:mm a");
                convertedStartDateTime = sdf2.format(startDate);

                SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM");
                convertedEndDate = sdf3.format(startDate);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "message = " + e.getMessage());
            }
        }
        else if (start_date.length() == 20) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                startDate = sdf.parse(start_date);

                //startTime = sdf.parse(time);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM, hh:mm a");
                convertedStartDateTime = sdf2.format(startDate);

                SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM");
                convertedEndDate = sdf3.format(startDate);
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

    public void GetReservationSuccessData()
    {
        Gson gson = new Gson();
        //String stringData = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SOCKET_RESP_OBJECT, "");
        Intent intent = getIntent();
        if(intent != null)
        {
            stringData = getIntent().getStringExtra(SharedPref.SOCKET_RESP_OBJECT);
            Log.d(TAG, "stringData Data = " + stringData);

            if(stringData != null)
            {
                Type type = new TypeToken<List<SocketReservationResp>>() {
                }.getType();
                socketSuccessRespList = new ArrayList<>();
                socketSuccessRespList = gson.fromJson(stringData, type);

                if(socketSuccessRespList.size() != 0)
                {
                    DisplaySuccessDialog(socketSuccessRespList);
                }
            }

        }
    }

    /* internet connection broadcast receiver */
    public void startNetworkBroadcastReceiver(Context currentContext) {
        connectivityHelper = new ConnectivityHelper();
        connectivityHelper.addListener(this);
        registerNetworkBroadcastReceiver(currentContext);
    }

    /* register receiver */
    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(connectivityHelper,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /* unregister receiver */
    public void unregisterNetworkBroadcastReceiver(Context currentContext) {
        currentContext.unregisterReceiver(connectivityHelper);
    }

    /* internet connection available callback method */
    @Override
    public void networkAvailable()
    {

    }

    @Override
    public void networkUnavailable()
    {

    }

    /* refresh */
    private void doTheAutoRefresh()
    {
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                /* driver data updated */
                Log.d(TAG, "driver data resp list size in on resume = " + driverDataRespList.size());
                if(isRefresh)
                {
                    if(driverDataRespList.size() != 0)
                    {
                        DisplayDriverDataUpdatedDialog(driverDataRespList);
                        isRefresh = false;
                    }
                    else
                    {
                        Log.d(TAG, "driver data resp list size is empty = " + driverDataRespList.size());
                    }
                }

                doTheAutoRefresh();
            }
        }, 5000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        isRefresh = true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "in onResume method ");

        registerNetworkBroadcastReceiver(this);

        /* get list from confirm booking screen and show reservation success dialog */
        if(isValid)
        {
            GetReservationSuccessData();
        }

        /* for driver data updated connect socket and show dialog */
        SocketConnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        isValid = false;
        unregisterNetworkBroadcastReceiver(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
        stringData = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        driverDataRespList.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mSocket.disconnect();
    }
}