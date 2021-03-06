package com.kiwni.app.user.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.kiwni.app.user.R;
import com.kiwni.app.user.activity.VehicleTypeListActivity;
import com.kiwni.app.user.adapter.AutoCompleteAdapter;
import com.kiwni.app.user.adapter.FavoriteAdapter;
import com.kiwni.app.user.adapter.TimeAdapter;
import com.kiwni.app.user.database.SqliteDatabaseHelper;
import com.kiwni.app.user.datamodels.ErrorDialog;
import com.kiwni.app.user.interfaces.FavoriteOnClickListener;
import com.kiwni.app.user.models.KeyValue;
import com.kiwni.app.user.models.SqliteModelClass;
import com.kiwni.app.user.network.ApiInterface;
import com.kiwni.app.user.network.ConnectivityHelper;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.ui.home.HomeFragment;
import com.kiwni.app.user.utils.PreferencesUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@SuppressLint("StaticFieldLeak")
public class OutstationFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,ConnectivityHelper.NetworkStateReceiverListener {

    /*public static OutstationFragment instance;*/

    public static OutstationFragment instance;
    String TAG = this.getClass().getSimpleName();
    View view;
    String currentLat = "", currentLng = "";
    Bundle bundle;

    AppCompatButton btnViewCabRoundTrip, btnConfirm;// btnCurrentLocation, btnLocationOnMap,
    ImageView imageMarker;
    ConstraintLayout layoutPickupDatePicker, layoutDropDatePicker;

    /* google map */
    GoogleMap mMap;
    Marker pickupMarker, dropMarker;
    private Polyline mPolyline;

    /*auto complete view*/
    public static AutoCompleteTextView autoCompleteTextViewPickup, autoCompleteTextViewDrop;
    AutoCompleteAdapter adapter;

    LinearLayout  linearBtnConfirm;//linearFooterButtons,
    ArrayList<LatLng> pickupLocationList = new ArrayList<>();
    ArrayList<LatLng> dropLocationList = new ArrayList<>();
    PlacesClient placesClient;
    GoogleApiClient mGoogleApiClient;
    private LatLng mCenterLatLong, mOrigin, mDestination;
    boolean isPickup = false, isDrop = false, isCurrent = false, isDDSelected = false, isLocated = false, isCameraMove = false,isNetworkAvailable = false;
    Context mContext;
    AutocompletePrediction item;
    String direction = "", distanceTextFromApi = "", distanceValueFromApi = "",
            durationTextFromApi = "", durationInTrafficFromApi = "",
            curr_converted_date = "", strDropDate = "", strPickupDate = "", convertedDateFormat = "",
            convertedDropDateFormat = "", concatDateTime = "", sendToApiPickupTime = "",
            sendToApiDropTime = "", changeStartDateFormat = "";
    String address = "", city = "", pickup_city = "", drop_city = "", pickup_address = "", drop_address = "";
    Spinner pickup_spinner_time;
    ArrayList<KeyValue> time = new ArrayList<>();
    TimeAdapter timeAdapter;
    boolean isCurrentDateBooking = false;
    TextView txtPickupDatePicker, txtDropDatePicker;
    double convertedDistance = 0.0,
             drop_lat = 0.0, drop_lng = 0.0;
    int mYear, mMonth, mDay, mHour, mMinute;

    ErrorDialog errorDialog;
    private ConnectivityHelper connectivityHelper;          // Receiver that detects network state changes

    private final static int RESULT_OK = 2;


    AppCompatButton btnRoundTrip, btnOneWay;
    TextView txtReturn;

    //declaration

    Dialog dialogAddress;

    SqliteDatabaseHelper sqliteDatabaseHelper;
    String dBAddress = "", dBFavoriteLat = "", dBFavoriteLng = "";

    public static String Address = "" , favoriteLat = "", favoriteLng = "";
    public static Boolean isConnectedCurrentLocation = false;
    public static double pickup_lat = 0.0, pickup_lng = 0.0, currentLatitude = 0.0, currentLongitude = 0.0;
    SqliteModelClass sqliteModelClassResp;
    FavoriteOnClickListener favoriteOnClickListener;
    List<SqliteModelClass> arrayListDb = new ArrayList<SqliteModelClass>();


    public OutstationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_outstation, container, false);

        mContext = getActivity();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* for gps */
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()) // Pass context here
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        favoriteOnClickListener = this:: onFavoriteOnClickListener;
        sqliteDatabaseHelper = new SqliteDatabaseHelper(getActivity());

        HomeFragment.isOutStation = true;

        /* start receiver for network state */
        startNetworkBroadcastReceiver(getActivity());

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnRoundTrip = view.findViewById(R.id.btnRoundTrip);
        btnOneWay = view.findViewById(R.id.btnOneWay);
        layoutDropDatePicker = view.findViewById(R.id.layoutDropDatePicker);
        txtReturn = view.findViewById(R.id.Return);


        /**/
        btnViewCabRoundTrip = view.findViewById(R.id.btnViewCabRoundTrip);
        /*linearFooterButtons = view.findViewById(R.id.linearFooterButtons);
        btnCurrentLocation = view.findViewById(R.id.btnCurrentLocation);
        btnLocationOnMap = view.findViewById(R.id.btnLocationOnMap);*/
        imageMarker = view.findViewById(R.id.imageMarker);
        linearBtnConfirm = view.findViewById(R.id.linearBtnConfirm);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        txtDropDatePicker = view.findViewById(R.id.txtDropDatePicker);
        pickup_spinner_time = view.findViewById(R.id.pickup_spinner_time);
        txtPickupDatePicker = view.findViewById(R.id.txtPickupDatePicker);

        layoutPickupDatePicker = view.findViewById(R.id.layoutPickupDatePicker);
        layoutDropDatePicker = view.findViewById(R.id.layoutDropDatePicker);


        btnRoundTrip.setBackgroundResource(R.drawable.tab_outstation_color);
        btnRoundTrip.setTextColor(Color.WHITE);

        btnRoundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                direction = "two-way";

                btnRoundTrip.setBackgroundResource(R.drawable.tab_outstation_color);
                btnRoundTrip.setTextColor(Color.WHITE);
                btnOneWay.setBackgroundResource(R.drawable.border_black_for_tab);
                btnOneWay.setTextColor(getActivity().getResources().getColor(R.color.button_color_dark));
                txtReturn.setVisibility(View.VISIBLE);
                layoutDropDatePicker.setVisibility(View.VISIBLE);
            }
        });

        btnOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                direction = "one-way";
                btnOneWay.setBackgroundResource(R.drawable.tab_outstation_color);
                btnOneWay.setTextColor(Color.WHITE);
                btnRoundTrip.setBackgroundResource(R.drawable.border_black_for_tab);
                btnRoundTrip.setTextColor(getActivity().getResources().getColor(R.color.button_color_dark));
                txtReturn.setVisibility(View.GONE);
                layoutDropDatePicker.setVisibility(View.GONE);
            }
        });

        /*initialized place sdk*/
        if (!Places.isInitialized()){
            Log.d(TAG, "key = " + R.string.google_maps_key);
            Places.initialize(getActivity(), ApiInterface.GOOGLE_MAP_API_KEY);
        }

        /*create new place client instance*/
        placesClient = Places.createClient(getActivity());
        adapter = new AutoCompleteAdapter(getActivity(), placesClient);

        // in below line we are initializing our array list.
        pickupLocationList = new ArrayList<>();
        dropLocationList = new ArrayList<>();

        /* autocomplete fields with adpater */
        autoCompleteTextViewPickup = view.findViewById(R.id.auto_pickup);
       autoCompleteTextViewPickup.setOnItemClickListener(autocompleteClickListener);

        //set adapter
        autoCompleteTextViewPickup.setAdapter(adapter);

        autoCompleteTextViewDrop = view.findViewById(R.id.auto_destination);
       // autoCompleteTextViewDrop.setOnItemClickListener(autocompleteClickListener);

        //set adapter
        //autoCompleteTextViewDrop.setAdapter(adapter);

        direction = "two-way";

        /*get current pickup date in format and set to UI*/
        GetCurrentPickupDate();

        /*get current end date in format and set to UI*/
        GetCurrentDropDate();

        //compare two dates are same or not
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM");
        try {
            Date currentDate = inputFormat.parse(curr_converted_date);
            Date pickerDate = inputFormat.parse(txtPickupDatePicker.getText().toString());

            if (currentDate.compareTo(pickerDate) == 0) {
                time.clear();

                KeyValue keyValue = new KeyValue("0", "select time");
                time.add(0, keyValue);
                isCurrentDateBooking = true;
            } else {
                isCurrentDateBooking = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* 1 hr time prior
        Time Picker */
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String[] hour_min = currentTime.split(":");
        String hour = hour_min[0];
        String min = hour_min[1];

        int i_hour = Integer.parseInt(hour);
        int i_mint = Integer.parseInt(min);

        if (isCurrentDateBooking) {
            if (i_hour >= 23) {
                //Toast.makeText(getActivity(), "For Outstation, We are catering Advance booking. Kindly Select the date accordingly.", Toast.LENGTH_SHORT).show();
            } else {
                SetTimeForCurrentDate(i_hour, i_mint);
            }
        } else {
            SetTimeForFutureDate();
        }

        timeAdapter = new TimeAdapter(getActivity(), R.layout.spinner_list_item, time);
        pickup_spinner_time.setAdapter(timeAdapter);

        /* Date picker */
        layoutPickupDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SimpleDateFormat")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar c = Calendar.getInstance();
                                c.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                strPickupDate = simpleDateFormat.format(c.getTime());
                                Log.d(TAG, strPickupDate);

                                //converted format
                                Date date = null;
                                try {
                                    date = simpleDateFormat.parse(strPickupDate);
                                    SimpleDateFormat sdfOutputDateFormat = new SimpleDateFormat("EEE, dd MMM");
                                    convertedDateFormat = sdfOutputDateFormat.format(date);
                                    Log.d(TAG, "convertedDateFormat - " + convertedDateFormat);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                txtPickupDatePicker.setText(convertedDateFormat);

                                //compare two dates are same or not
                                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM");
                                try {
                                    Date currentDate = inputFormat.parse(curr_converted_date);
                                    Date pickerDate = inputFormat.parse(convertedDateFormat);

                                    if (currentDate.compareTo(pickerDate) == 0) {
                                        time.clear();

                                        KeyValue keyValue = new KeyValue("0", "select time");
                                        time.add(0, keyValue);
                                        isCurrentDateBooking = true;
                                    } else {
                                        isCurrentDateBooking = false;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //Time Picker
                                GetPriorTime();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        layoutDropDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SimpleDateFormat")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar c = Calendar.getInstance();
                                c.set(year, monthOfYear, dayOfMonth);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                strDropDate = simpleDateFormat.format(c.getTime());
                                Log.d(TAG, strDropDate);

                                /*converted format*/
                                Date date = null;
                                try {
                                    date = simpleDateFormat.parse(strDropDate);
                                    SimpleDateFormat sdfOutputDateFormat = new SimpleDateFormat("EEE, dd MMM");
                                    convertedDropDateFormat = sdfOutputDateFormat.format(date);
                                    Log.d(TAG, "convertedDropDateFormat - " + convertedDropDateFormat);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                txtDropDatePicker.setText(convertedDropDateFormat);

                                /*compare two dates are same or not*/
                                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM");
                                try {
                                    Date currentDate = inputFormat.parse(curr_converted_date);
                                    Date pickerDate = inputFormat.parse(convertedDropDateFormat);

                                    isCurrentDateBooking = currentDate.compareTo(pickerDate) == 0;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        /*click events of autocomplete pickup */
        autoCompleteTextViewPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isPickup = true;
                isDrop = false;
                isLocated = false;

                String pickupText = String.valueOf(autoCompleteTextViewPickup.getText());

                Log.d("TAG", isPickup + " " + isDrop);

                dialogAddress = new Dialog(getActivity(), android.R.style.Theme_Light);
                dialogAddress.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAddress.setContentView(R.layout.dialog_address);

                AutoCompleteTextView autoPickup = dialogAddress.findViewById(R.id.dialogAutoPickup);
                ImageView imageBack = dialogAddress.findViewById(R.id.imgBack);
                AppCompatButton btnCurrentLocation = dialogAddress.findViewById(R.id.btnCurrentLocation);
                AppCompatButton btnLocationOnMap = dialogAddress.findViewById(R.id.btnLocationOnMap);
                RecyclerView recyclerView = dialogAddress.findViewById(R.id.recyclerView);

                autoPickup.setText(pickupText);

                autoPickup.setOnItemClickListener(autocompleteClickListener);
                autoPickup.setAdapter(adapter);

                dialogAddress.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


                arrayListDb = sqliteDatabaseHelper.getAllData();
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                FavoriteAdapter favoriteAdapter = new FavoriteAdapter(getActivity(),arrayListDb,favoriteOnClickListener);
                recyclerView.setAdapter(favoriteAdapter);

                //Buttons Clicks

                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAddress.dismiss();
                    }
                });
                autoPickup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        autoPickup.selectAll();
                    }
                });

                btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isCurrent = true;
                        isLocated = true;
                       // linearFooterButtons.setVisibility(View.GONE);

                        dialogAddress.dismiss();

                        //get current location
                        if (currentLatitude != 0.0 && currentLongitude != 0.0)
                        {
                            getAddressFromCurrentLocation(currentLatitude, currentLongitude);

                            pickupMarker.remove();
                            DrawMarker(currentLatitude, currentLongitude, pickup_city);

                            if (pickupLocationList.size() > 0)
                            {
                                pickupLocationList.set(0, new LatLng(currentLatitude, currentLongitude));
                                Log.d("TAG", "size update pickup = " + pickupLocationList.size());
                            }
                            else {
                                Log.d("TAG", "pickup size = " + pickupLocationList.size());
                            }

                            AddMarker(pickup_city);
                            Log.d("TAG", "size 2 = " + pickupLocationList.size());
                        }

                        if (!autoCompleteTextViewPickup.getText().toString().equals("")
                                && !autoCompleteTextViewDrop.getText().toString().equals("")) {
                            Log.d("TAG", "enable find car button");
                            linearBtnConfirm.setVisibility(View.GONE);
                            btnViewCabRoundTrip.setVisibility(View.VISIBLE);
                        } else {
                            linearBtnConfirm.setVisibility(View.GONE);
                        }
                    }
                });

                btnLocationOnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageMarker.setVisibility(View.VISIBLE);
                       // linearFooterButtons.setVisibility(View.GONE);
                        linearBtnConfirm.setVisibility(View.VISIBLE);

                        dialogAddress.dismiss();

                        if (isPickup)
                        {
                            isCameraMove = true;
                            hideKeyboardFrom(getActivity(), autoCompleteTextViewPickup);
                        } else {
                            hideKeyboardFrom(getActivity(), autoCompleteTextViewDrop);
                        }

                        if (!isLocated) {
                            CameraChange();
                        }
                    }
                });

                autoCompleteTextViewPickup.selectAll();
                dialogAddress.show();

                //autoCompleteTextViewPickup.setSelectAllOnFocus(true);
                // autoCompleteTextViewPickup.selectAll();

                // linearFooterButtons.setVisibility(View.VISIBLE);
                btnCurrentLocation.setVisibility(View.VISIBLE);
                linearBtnConfirm.setVisibility(View.GONE);
                btnViewCabRoundTrip.setVisibility(View.GONE);

            }
        });

        autoCompleteTextViewPickup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 15) {
                    autoCompleteTextViewPickup.dismissDropDown();
                }
            }
        });

        /*click events of autocomplete drop */
        autoCompleteTextViewDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isDrop = true;
                isPickup = false;
                isLocated = false;

                //autoCompleteTextViewDrop.setSelectAllOnFocus(true);
                // autoCompleteTextViewDrop.selectAll();

                dialogAddress = new Dialog(getActivity(), android.R.style.Theme_Light);
                dialogAddress.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogAddress.setContentView(R.layout.dialog_address);

                String dropText = String.valueOf(autoCompleteTextViewDrop.getText());


                AutoCompleteTextView autoPickup = dialogAddress.findViewById(R.id.dialogAutoPickup);
                ImageView imageBack = dialogAddress.findViewById(R.id.imgBack);
                AppCompatButton btnCurrentLocation = dialogAddress.findViewById(R.id.btnCurrentLocation);
                AppCompatButton btnLocationOnMap = dialogAddress.findViewById(R.id.btnLocationOnMap);
                RecyclerView recyclerView = dialogAddress.findViewById(R.id.recyclerView);
                TextView txtTitle = dialogAddress.findViewById(R.id.txtTitle);
                autoPickup.setText(dropText);

                dialogAddress.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                arrayListDb = sqliteDatabaseHelper.getAllData();
                txtTitle.setText("Drop");

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                FavoriteAdapter favoriteAdapter = new FavoriteAdapter(getActivity(),arrayListDb,favoriteOnClickListener);
                recyclerView.setAdapter(favoriteAdapter);


                autoPickup.setOnItemClickListener(autocompleteClickListener);
                autoPickup.setAdapter(adapter);

                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAddress.dismiss();
                        Toast.makeText(getActivity(), "imageBack", Toast.LENGTH_SHORT).show();
                    }
                });

                autoPickup.setSelectAllOnFocus(true);
                autoPickup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        autoPickup.selectAll();
                    }
                });

                btnLocationOnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageMarker.setVisibility(View.VISIBLE);
                        //linearFooterButtons.setVisibility(View.GONE);
                        linearBtnConfirm.setVisibility(View.VISIBLE);

                        dialogAddress.dismiss();

                        if (isPickup)
                        {
                            isCameraMove = true;
                            hideKeyboardFrom(getActivity(), autoCompleteTextViewPickup);
                        } else {
                            hideKeyboardFrom(getActivity(), autoCompleteTextViewDrop);
                        }

                        if (!isLocated) {
                            CameraChange();
                        }
                    }
                });

                dialogAddress.show();

                //linearFooterButtons.setVisibility(View.VISIBLE);
                btnCurrentLocation.setVisibility(View.GONE);
                linearBtnConfirm.setVisibility(View.GONE);
                btnViewCabRoundTrip.setVisibility(View.GONE);

            }
        });


        autoCompleteTextViewDrop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 15) {
                    autoCompleteTextViewDrop.dismissDropDown();
                }
            }
        });

        /* confirm location manage on this click and hide footer buttons too*/
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocated = true;

                if (isPickup) {
                    //not change camera position
                    imageMarker.setVisibility(View.GONE);
                    linearBtnConfirm.setVisibility(View.GONE);

                    //set current location
                    pickupMarker.remove();

                    DrawMarker(pickup_lat, pickup_lng, pickup_city);
                    Log.d(TAG, "new Latlng = " + pickup_lat + " " + pickup_lng);

                    if (pickupLocationList.size() > 0) {
                        pickupLocationList.set(0, new LatLng(pickup_lat, pickup_lng));
                        Log.d("TAG", "size update pickup = " + pickupLocationList.size());
                    } else {
                        Log.d("TAG", "pickup size = " + pickupLocationList.size());
                    }

                    AddMarker(pickup_city);
                    Log.d("TAG", "size 2 = " + pickupLocationList.size());
                } else {
                    //drop
                    //for unnamed location
                    Log.d(TAG, "city = " + pickup_city + " - " + drop_city);
                    if (pickup_city == null || drop_city == null)
                    {
                        autoCompleteTextViewDrop.setText("");
                        imageMarker.setVisibility(View.GONE);
                        linearBtnConfirm.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Please select proper location.!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (pickup_city.equals(drop_city))
                        {
                            autoCompleteTextViewDrop.setText("");
                            Toast.makeText(getActivity(), "Please Select Different City..!", Toast.LENGTH_SHORT).show();
                            linearBtnConfirm.setVisibility(View.GONE);

                            imageMarker.setVisibility(View.GONE);
                            if (dropMarker != null) {
                                dropMarker.remove();
                                mPolyline.remove();
                            }
                        } else {
                            if (drop_lat != 0.0 && drop_lng != 0.0) {
                                imageMarker.setVisibility(View.GONE);
                                linearBtnConfirm.setVisibility(View.GONE);

                                if (dropLocationList.size() == 0) {
                                    DrawMarker(drop_lat, drop_lng, drop_city);
                                    dropLocationList.add(new LatLng(drop_lat, drop_lng));
                                    Log.d("TAG", "size default = " + dropLocationList.size());
                                } else {
                                    dropMarker.remove();
                                    DrawMarker(drop_lat, drop_lng, drop_city);
                                    dropLocationList.set(0, new LatLng(drop_lat, drop_lng));
                                    Log.d("TAG", "size update drop = " + dropLocationList.size());
                                }

                                AddMarker(drop_city);
                                Log.d("TAG", "size 2 = " + dropLocationList.size());
                            }
                        }
                    }
                }

                if (!autoCompleteTextViewPickup.getText().toString().equals("")
                        && !autoCompleteTextViewDrop.getText().toString().equals("")) {
                    Log.d("TAG", "enable find car button");
                    linearBtnConfirm.setVisibility(View.GONE);
                   // linearFooterButtons.setVisibility(View.GONE);
                    btnViewCabRoundTrip.setVisibility(View.VISIBLE);
                } /*else {
                    linearFooterButtons.setVisibility(View.GONE);
                }*/
            }
        });

        /* view cab button click with validations and navigate to next screen */
        btnViewCabRoundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //round-trip
                isPickup = false;
                isDrop = false;
                if(ConnectivityHelper.isConnected) {
                    if (autoCompleteTextViewPickup.getText().toString().equals("")) {
                        errorDialog = new ErrorDialog(getActivity(), "Pickup location field cannot be empty.!");
                        errorDialog.show();
                    } else if (autoCompleteTextViewDrop.getText().toString().equals("")) {
                        errorDialog = new ErrorDialog(getActivity(), "Drop location field cannot be empty.!");
                        errorDialog.show();
                    } else if (txtPickupDatePicker.getText().toString().equals("")) {
                        errorDialog = new ErrorDialog(getActivity(), "Pickup Date field cannot be empty.!");
                        errorDialog.show();
                    } else if (pickup_spinner_time.getSelectedItem().toString().isEmpty() || pickup_spinner_time.getSelectedItem().toString() == "") {
                        errorDialog = new ErrorDialog(getActivity(), "Pickup Time field cannot be empty.!");
                        errorDialog.show();
                    } else if (txtDropDatePicker.getText().toString().equals("")) {
                        errorDialog = new ErrorDialog(getActivity(), "Destination Date field cannot be empty.!");
                        errorDialog.show();
                    } else {
                        /* check distance should not be null or empty */
                        if (distanceValueFromApi.equals("") || distanceValueFromApi.equals(null)) {
                            errorDialog = new ErrorDialog(getActivity(), "Please Wait..!");
                            errorDialog.show();
                            autoCompleteTextViewDrop.setText("");
                            dropMarker.remove();
                        }
                        else if (pickup_city.equals("") || pickup_city.equals(null) || drop_city.equals("") || drop_city.equals(null)){
                            errorDialog = new ErrorDialog(getActivity(), "Please select different Address.!");
                            errorDialog.show();
                        }
                        else {
                            try {
                                Date currentDate = inputFormat.parse(curr_converted_date);
                                Date pickerDate = inputFormat.parse(txtPickupDatePicker.getText().toString());

                                if (currentDate.compareTo(pickerDate) == 0 && pickup_spinner_time.getSelectedItem().toString() == "select time") {
                                    errorDialog = new ErrorDialog(getActivity(), "For Outstation, We are catering Advance booking. Kindly Select the date accordingly.");
                                    errorDialog.show();
                                } else {

                                    if(direction.equals("two-way")){
                                        concatDateTime = strPickupDate + " " + pickup_spinner_time.getSelectedItem().toString();
                                        Log.d(TAG, "concat pickup date time = " + concatDateTime);

                                        /* convert selected pickup date from picker to format to send api */
                                        getCurrentDateToSendApiInFormat(concatDateTime);

                                        /*convert date format and send to next screen for display */
                                        getStartDateInFormat(concatDateTime);

                                        /*Calculate drop time from start date and 23:59:59 */
                                        CalculateDropTimeForRoundTrip(strDropDate + " 23:59:59");
                                        Log.d(TAG, "dropDateTime = " + sendToApiDropTime);

                                        /* check end date is greater than start date */
                                        if (isDateAfter(sendToApiPickupTime, sendToApiDropTime))
                                        {
                                            //true condition

                                            Intent i = new Intent(getActivity(), VehicleTypeListActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            //send data to next screen
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_CITY, pickup_city);
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_CITY, drop_city);
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_ADDRESS, autoCompleteTextViewPickup.getText().toString());
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_ADDRESS, autoCompleteTextViewDrop.getText().toString());
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_LOCATION, mOrigin.latitude + " " + mOrigin.longitude);
                                            Log.d("TAG","Pass value to next"+mOrigin.longitude+","+mOrigin.longitude);
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_LOCATION, mDestination.latitude + " " + mDestination.longitude);
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_DATE, concatDateTime);
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.START_DATE_WITH_MONTH_DAY, changeStartDateFormat);
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.DIRECTION, direction);
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.SERVICE_TYPE, "Outstation");
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.DISTANCE, String.valueOf(convertedDistance));
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.DURATION_IN_TRAFFIC, durationInTrafficFromApi);
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_TIME_FOR_API, sendToApiPickupTime);
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_TIME_FOR_API, sendToApiDropTime);
                                            Log.d("TAG","drop time"+ sendToApiDropTime);
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_DATE_TO_DISPLAY, txtPickupDatePicker.getText().toString());
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_TIME_TO_DISPLAY, pickup_spinner_time.getSelectedItem().toString());
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_DATE_TO_DISPLAY, txtDropDatePicker.getText().toString());
                                            PreferencesUtils.putPreferences(getActivity(), SharedPref.DISTANCE_IN_KM, distanceTextFromApi);

                                            startActivity(i);
                                            //getActivity().finish();

                                        } else {
                                            //false condition
                                            errorDialog = new ErrorDialog(getActivity(), "Kindly select proper return date..!");
                                            errorDialog.show();
                                        }
                                    }else {

                                        // one way

                                        concatDateTime = strPickupDate + " " + pickup_spinner_time.getSelectedItem().toString();
                                        Log.d(TAG, "concatDateTime = " + concatDateTime);

                                        /* convert selected pickup date from picker to format to send api */
                                        getCurrentDateToSendApiInFormat(concatDateTime);

                                        /*convert date format and send to next screen for display */
                                        getStartDateInFormat(concatDateTime);

                                        /* Calculate drop_time from start_date and duration */
                                        CalculateDropTime(concatDateTime, durationInTrafficFromApi);

                                        Intent i = new Intent(getActivity(), VehicleTypeListActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_CITY, pickup_city);
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_CITY, drop_city);
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_ADDRESS, autoCompleteTextViewPickup.getText().toString());
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_ADDRESS, autoCompleteTextViewDrop.getText().toString());
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_LOCATION, mOrigin.latitude + " " + mOrigin.longitude);
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_LOCATION, mDestination.latitude + " " + mDestination.longitude);
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_DATE, txtPickupDatePicker.getText().toString());
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.START_DATE_WITH_MONTH_DAY, changeStartDateFormat);
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.DIRECTION, direction);
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.SERVICE_TYPE, "Outstation");
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.DISTANCE, String.valueOf(convertedDistance));
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.DURATION_IN_TRAFFIC, durationInTrafficFromApi);
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_TIME_FOR_API, sendToApiPickupTime);
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_TIME_FOR_API, sendToApiDropTime);
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_DATE_TO_DISPLAY, txtPickupDatePicker.getText().toString());
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_TIME_TO_DISPLAY, pickup_spinner_time.getSelectedItem().toString());
                                        PreferencesUtils.putPreferences(getActivity(), SharedPref.DISTANCE_IN_KM, distanceTextFromApi);

                                        startActivity(i);
                                    }


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else{
                    /* no internet message */
                    Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.no_internet_msg, Snackbar.LENGTH_LONG)
                            .setTextColor(Color.WHITE)
                            .setBackgroundTint(Color.RED)
                            .setDuration(5000)
                            .show();
                }
            }
        });
    }

    private void onFavoriteOnClickListener(View v, int position, String addressType, String address) {

        Log.d("TAG","adapterAddressType :" +addressType);

        favoriteAddressSelect(address);

    }

    public void favoriteAddressSelect(String address) {


        if (isPickup) {

            for(int i = 0; i< arrayListDb.size(); i++){
                sqliteModelClassResp =arrayListDb.get(i);

                //dBFavoriteLat = modelsClassResp.getFavoriteLat();
                dBAddress = sqliteModelClassResp.getAddress();


                if(dBAddress.equals(address)){
                    dBFavoriteLat = sqliteModelClassResp.getFavoriteLat();
                    dBFavoriteLng = sqliteModelClassResp.getFavoriteLng();

                    Log.d("TAG","Values Lat Lng "+dBFavoriteLat +","+ dBFavoriteLng);
                }


            }



            Log.d("Tag", "BOTTOM_SHEET_LAT" + favoriteLat);
            Log.d("Tag", "BOTTOM_SHEET_LNG" + favoriteLng);
            Log.d("Tag", "FAVORITE_HOME_ADDRESS" + pickup_address);

            pickup_lat = Double.parseDouble(dBFavoriteLat);
            pickup_lng = Double.parseDouble(dBFavoriteLng);
            pickup_address = address;

            Log.d("TAG","pickup_addressDb"+pickup_address);

            dialogAddress.dismiss();
            autoCompleteTextViewPickup.setText(address);

           /* if (!isDDSelected) {
                item = adapter.getItem(i);
            }*/

            isDDSelected = true;
            getAddressFromCurrentLocation(pickup_lat, pickup_lng);
        } else {

            for(int i = 0; i< arrayListDb.size(); i++) {
                sqliteModelClassResp = arrayListDb.get(i);

                //dBFavoriteLat = modelsClassResp.getFavoriteLat();
                dBAddress = sqliteModelClassResp.getAddress();


                if (dBAddress.equals(address)) {
                    dBFavoriteLat = sqliteModelClassResp.getFavoriteLat();
                    dBFavoriteLng = sqliteModelClassResp.getFavoriteLng();

                    Log.d("TAG", "Values Lat Lng " + dBFavoriteLat + "," + dBFavoriteLng);
                }
            }


            drop_lat = Double.parseDouble(dBFavoriteLat);
            drop_lng = Double.parseDouble(dBFavoriteLng);


            isDDSelected = true;
            getAddressFromCurrentLocation(drop_lat, drop_lng);

            dialogAddress.dismiss();
            autoCompleteTextViewDrop.setText(address);
        }

        if(autoCompleteTextViewPickup.getText().toString().equals(autoCompleteTextViewDrop.getText().toString()))
        {
            autoCompleteTextViewDrop.setText("");
            Toast.makeText(getActivity(), "Please Select Different City..!", Toast.LENGTH_SHORT).show();
            linearBtnConfirm.setVisibility(View.GONE);

            if(dropMarker != null)
            {
                dropMarker.remove();
                mPolyline.remove();
            }
        }

        if (!autoCompleteTextViewPickup.getText().toString().equals("")
                && !autoCompleteTextViewDrop.getText().toString().equals("")) {
            linearBtnConfirm.setVisibility(View.GONE);
            //linearFooterButtons.setVisibility(View.GONE);
            btnViewCabRoundTrip.setVisibility(View.VISIBLE);
        }

    }


    /* autocompletetextview listener for drop down places list display */
    public AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

                /* To specify which data types to return, pass an array of Place.
                Fields in your FetchPlaceRequest
                Use only those fields which are required.*/

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnCompleteListener(new OnCompleteListener<FetchPlaceResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<FetchPlaceResponse> task)
                        {
                            if(task.getResult() != null)
                            {
                                FetchPlaceResponse places = task.getResult();
                                final Place place = places.getPlace();
                                Log.d("TAG", "places = " + place.getAddress());

                                /* if pickup flag is true (selected from drop down location) */
                                if (isPickup)
                                {
                                    pickup_lat = places.getPlace().getLatLng().latitude;
                                    pickup_lng = places.getPlace().getLatLng().longitude;
                                    pickup_address = places.getPlace().getAddress();

                                    dialogAddress.dismiss();
                                    autoCompleteTextViewPickup.setText(pickup_address);

                                    if (!isDDSelected) {
                                        item = adapter.getItem(i);
                                    }

                                    isDDSelected = true;
                                    getAddressFromCurrentLocation(pickup_lat, pickup_lng);
                                }
                                else
                                {
                                    drop_lat = places.getPlace().getLatLng().latitude;
                                    drop_lng = places.getPlace().getLatLng().longitude;
                                    drop_address = places.getPlace().getAddress();

                                    isDDSelected = true;
                                    getAddressFromCurrentLocation(drop_lat, drop_lng);

                                    dialogAddress.dismiss();
                                    autoCompleteTextViewDrop.setText(drop_address);
                                }

                                /* enable view cab btn and disable other buttons */
                                if (!autoCompleteTextViewPickup.getText().toString().equals("")
                                        && !autoCompleteTextViewDrop.getText().toString().equals("")) {
                                    linearBtnConfirm.setVisibility(View.GONE);
                                    //linearFooterButtons.setVisibility(View.GONE);
                                    btnViewCabRoundTrip.setVisibility(View.VISIBLE);
                                } /*else {
                                    linearFooterButtons.setVisibility(View.GONE);
                                }*/
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Please check your internet connection.!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            autoCompleteTextViewPickup.setText(e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /* get current location */
    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null)
        {
            /* current location */
            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();
            isConnectedCurrentLocation = true;

            /* internet connected true*/
            if(ConnectivityHelper.isConnected)
            {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                        (new LatLng(currentLatitude, currentLongitude), 10.0f));
                Log.d(TAG, "current location = " + currentLatitude + " " + currentLongitude);

                /*get current location and draw marker on map*/
                if (currentLatitude != 0.0 && currentLongitude != 0.0)
                {
                    pickupMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(currentLatitude, currentLongitude))
                            .title(city)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                            (new LatLng(currentLatitude, currentLongitude), 10.0f));


                    getAddressFromCurrentLocation(currentLatitude, currentLongitude);
                    autoCompleteTextViewDrop.setText("");


                    /* check list size and add or update value in list */
                    if(pickupLocationList.size() == 0)
                    {
                        pickupLocationList.add(new LatLng(currentLatitude, currentLongitude));
                    }
                    else
                    {
                        pickupLocationList.set(0, new LatLng(currentLatitude, currentLongitude));
                    }

                    AddMarker(pickup_city);
                    Log.d(TAG, "size 0 for current loc = " + pickupLocationList.size());
                } else {
                    Log.d(TAG, "Not getting co-ordinates");
                }
            }
            else            /* internet connected false*/
            {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                        (new LatLng(currentLatitude, currentLongitude), 10.0f));
            }
        } else
            try
            {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        /*try {
            if (location != null)
                //changeMap(location);

            *//*Log.d(TAG, "Location = " + location.getLatitude() + " " + location.getLongitude());
            Toast.makeText(getActivity(), "Location change", Toast.LENGTH_SHORT).show();*//*
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        this. mMap = googleMap;
        if (mMap != null) {
            mMap.clear();
            autoCompleteTextViewDrop.setText("");
        }
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    /* after moving marker on map location changes and then display that location on textview */
    public void CameraChange()
    {
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener()
        {
            @Override
            public void onCameraChange(@NonNull CameraPosition cameraPosition)
            {
                if(!isLocated)
                {
                    Log.d("Camera position change " + "", cameraPosition + "");
                    mCenterLatLong = cameraPosition.target;

                    isCameraMove = false;

                    try
                    {
                        Location mLocation = new Location("");
                        mLocation.setLatitude(mCenterLatLong.latitude);
                        mLocation.setLongitude(mCenterLatLong.longitude);

                        if(isPickup)
                        {
                            pickup_lat = mCenterLatLong.latitude;
                            pickup_lng = mCenterLatLong.longitude;

                            if(pickup_lat != 0.0 && pickup_lng != 0.0)
                            {
                                getAddressFromCurrentLocation(pickup_lat, pickup_lng);
                            }
                        }
                        else
                        {
                            drop_lat = mCenterLatLong.latitude;
                            drop_lng = mCenterLatLong.longitude;

                            if(drop_lat != 0.0 && drop_lng != 0.0)
                            {
                                getAddressFromCurrentLocation(drop_lat, drop_lng);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Log.d(TAG, "is located true");
                }
            }
        });
    }

    /* get address from co-ordinates according to flag */
    public void getAddressFromCurrentLocation(Double latitude, Double longitude)
    {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();

            Log.d("TAG", "address : " + address);
            Log.d("TAG", "city : " + city);



            //pickup
            if(isPickup)
            {
                pickup_city = city;

                //pickup + drop-down selected
                if(isDDSelected)
                {
                    isDDSelected = false;
                    isLocated = true;
                    autoCompleteTextViewPickup.setSelection(autoCompleteTextViewPickup.getText().length());
                    hideKeyboardFrom(getActivity(), autoCompleteTextViewPickup);

                    //pickup
                    imageMarker.setVisibility(View.GONE);
                    linearBtnConfirm.setVisibility(View.GONE);

                    pickupMarker.remove();
                    DrawMarker(pickup_lat, pickup_lng, pickup_city);

                    if(pickupLocationList.size() > 0)
                    {
                        pickupLocationList.set(0, new LatLng(pickup_lat,pickup_lng));
                        Log.d("TAG", "size update pickup = " + pickupLocationList.size());
                    }
                    else
                    {
                        Log.d("TAG", "pickup size = " + pickupLocationList.size());
                    }

                    AddMarker(pickup_city);
                    Log.d("TAG", "size 2 = " + pickupLocationList.size());
                }
                else    //pickup + locate on map selected
                {
                    pickup_address = address;
                    autoCompleteTextViewPickup.setText(pickup_address);
                    autoCompleteTextViewPickup.setSelection(autoCompleteTextViewPickup.getText().length());
                    hideKeyboardFrom(getActivity(), autoCompleteTextViewPickup);
                }
            }
            else if(isDrop)         //drop
            {
                drop_city = city;

                //drop + drop-down selected
                if(isDDSelected)
                {
                    isDDSelected = false;
                    isLocated = true;
                    autoCompleteTextViewDrop.setSelection(autoCompleteTextViewDrop.getText().length());
                    hideKeyboardFrom(getActivity(), autoCompleteTextViewDrop);

                    /* drop-down selected
                     check same city throws error else display location */
                    if(pickup_city.equals(drop_city))
                    {
                        autoCompleteTextViewDrop.setText("");
                        Toast.makeText(getActivity(), "Please Select Different City..!", Toast.LENGTH_SHORT).show();
                        linearBtnConfirm.setVisibility(View.GONE);

                        if(dropMarker != null)
                        {
                            dropMarker.remove();
                            mPolyline.remove();
                        }
                    }
                    else
                    {
                        if(drop_lat != 0.0 && drop_lng != 0.0)
                        {
                            imageMarker.setVisibility(View.GONE);
                            linearBtnConfirm.setVisibility(View.GONE);

                            if(dropLocationList.size() == 0)
                            {
                                DrawMarker(drop_lat, drop_lng, drop_city);
                                dropLocationList.add(new LatLng(drop_lat, drop_lng));
                                Log.d("TAG", "size default = " + dropLocationList.size());
                            }
                            else
                            {
                                dropMarker.remove();
                                DrawMarker(drop_lat, drop_lng, drop_city);
                                dropLocationList.set(0, new LatLng(drop_lat,drop_lng));
                                Log.d("TAG", "size update drop = " + dropLocationList.size());
                            }

                            AddMarker(drop_city);
                            Log.d("TAG","size 2 = " + dropLocationList.size());
                        }
                    }
                }
                else   //drop + locate on map selected
                {
                    autoCompleteTextViewDrop.setText(address);

                    autoCompleteTextViewDrop.setSelection(autoCompleteTextViewDrop.getText().length());
                    hideKeyboardFrom(getActivity(), autoCompleteTextViewDrop);
                }
            }
            else
            {
                //current location
                autoCompleteTextViewPickup.setText(address);
                pickup_city = city;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* draw marker depends on flag */
    public void DrawMarker(Double latitude, Double longitude, String city)
    {
        if(isPickup)
        {
            pickupMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(city)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                    (new LatLng(latitude, longitude), 10.0f));
        }
        else if(isDrop)
        {
            dropMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(city)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                    (new LatLng(latitude, longitude), 10.0f));
        }
        else
        {
            Log.d(TAG, "current location");
        }
    }

    /* add marker in list */
    public void AddMarker(String title)
    {
        Log.d("TAG","pickupLocationList"+pickupLocationList.toString());
        if(pickupLocationList.size() == 1)
        {
            Log.d("TAG","pickupLocationList"+pickupLocationList.toString());
            mOrigin = pickupLocationList.get(0);
            Log.d(TAG, "mOrigin = " + mOrigin);
        }
        else
        {
            Log.d(TAG, "size of add marker 1 = " + pickupLocationList.size());
        }

        if (dropLocationList.size() == 1)
        {
            mDestination = dropLocationList.get(0);
            Log.d(TAG, "mDestination = " + mDestination);
        }
        else
        {
            Log.d("TAG", "size of add marker 1 = " + pickupLocationList.size());
        }

        if(pickupLocationList.size() == 1 && dropLocationList.size() == 1)
        {
            Log.d("TAG", "size of add marker 1 = " + pickupLocationList.size());
            Log.d("TAG", "size of add marker 2 = " + dropLocationList.size());

            drawRoute();
        }
    }

    /* draw route between two points if pickup-list size is one and drop-list size is one */
    private void drawRoute()
    {
        Log.d("TAG", "mOrigin = " + mOrigin);
        Log.d("TAG", "mDestination = " + mDestination);

        /*Getting URL to the Google Directions API*/
        String directionUrl = getDirectionsUrl(mOrigin, mDestination);
        Log.d("TAG", "directionUrl = " + directionUrl);

        /* Start downloading json data from Google Directions API */
        DownloadDirectionTask downloadTask = new DownloadDirectionTask();
        downloadTask.execute(directionUrl);
    }

    /**
     * Getting URL to the Google Directions API
     */
    private String getDirectionsUrl(LatLng origin, LatLng dest)
    {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        Log.d("TAG", "str_origin = " + str_origin);
        //String str_origin = "origin=18.5204,73.8567";
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        Log.d("TAG", "str_dest = " + str_dest);
        //String str_dest = "destination=19.0760,72.8777";
        // Key
        //String key = "key=" + getString(R.string.google_maps_key);
        String key = "key=" + ApiInterface.GOOGLE_MAP_API_KEY;
        Log.d("TAG", "api key = " + key);
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d("TAG", "url = " + url);

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();
        } catch (Exception e) {
            Log.d("Exception on download", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class to download data from Google Directions URL
     */
    @SuppressLint("StaticFieldLeak")
    private class DownloadDirectionTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask", "DownloadTask : " + data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        /*Executes in UI thread, after the execution of
        doInBackground()*/
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Invokes the thread for parsing the JSON data
            DirectionParseTask parserTask = new DirectionParseTask();
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Directions in JSON format
     */
    @SuppressLint("StaticFieldLeak")
    private class DirectionParseTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        /* Executes in UI thread, after the parsing process*/
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            double lat, lng;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    lat = Double.parseDouble(point.get("lat"));
                    lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.BLACK);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                if (mPolyline != null) {
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            } else
                Toast.makeText(getActivity(), "No route is found", Toast.LENGTH_LONG).show();

            /*get distance from distance matrix api google map*/
            GetDistance(mOrigin, mDestination);

            /* zoom map */
            List<LatLng> latLngList = new ArrayList<>();
            latLngList.add(mOrigin);
            latLngList.add(mDestination);
            zoomRoute(mMap, latLngList);
        }
    }

    /* calculate distance and get from distance matrix api */
    private void GetDistance(LatLng mOrigin, LatLng mDestination) {
        String url = getDistanceMatrixUrl(mOrigin, mDestination);

        DownloadAndDistanceParseTask distanceTask = new DownloadAndDistanceParseTask();
        distanceTask.execute(url);
    }

    private String getDistanceMatrixUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origins=" + origin.latitude + "," + origin.longitude;
        Log.d(TAG, "str_origin = " + str_origin);
        //String str_origin = "origin=18.5204,73.8567";
        // Destination of route
        String str_dest = "destinations=" + dest.latitude + "," + dest.longitude;
        Log.d(TAG, "str_dest = " + str_dest);
        //String str_dest = "destination=19.0760,72.8777";
        // Key
        //String key = "key=" + getString(R.string.google_maps_key);
        String key = "key=" + ApiInterface.GOOGLE_MAP_API_KEY;
        Log.d(TAG, "api key = " + key);
        //departure_time
        String departure_time = "departure_time=now";
        // Building the parameters to the web service
        String parameters = departure_time + "&" + str_origin + "&" + str_dest + "&" + key;
        // Output format
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + parameters;
        Log.d(TAG, "url = " + url);

        return url;
    }

    /**
     * A class to download data from Google Distance Matrix URL
     */
    @SuppressLint("StaticFieldLeak")
    public class DownloadAndDistanceParseTask extends AsyncTask<String, Void, String> {
        String data = "";

        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "result = " + result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("elements");
                    for (int i1 = 0; i1 < jsonArray1.length(); i1++) {
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(i1);

                        JSONObject distance = jsonObject2.getJSONObject("distance");
                        distanceTextFromApi = distance.getString("text");
                        distanceValueFromApi = distance.getString("value");

                        Log.d(TAG, "distanceValueFromApi = " + distanceValueFromApi);

                        JSONObject duration = jsonObject2.getJSONObject("duration");
                        durationTextFromApi = duration.getString("text");

                        JSONObject duration_in_traffic = jsonObject2.getJSONObject("duration_in_traffic");
                        durationInTrafficFromApi = duration_in_traffic.getString("text");

                        Log.d(TAG, "distanceTextFromApi = " + distanceTextFromApi + "distanceValueFromApi = " + distanceValueFromApi
                                + ", durationTextFromApi = " + durationTextFromApi
                                + ", durationInTrafficFromApi = " + durationInTrafficFromApi);

                        /* calculate distance */
                        distanceValueFromApi = String.valueOf(Double.parseDouble(distanceValueFromApi) / 1000);
                        Log.d(TAG, "distanceValueFromApi = " + distanceValueFromApi.trim());

                        convertedDistance = Double.valueOf(distanceValueFromApi.trim());
                        Log.d(TAG, "convertedDistance = " + convertedDistance);

                        /* for round trip multiply by 2 */
                        convertedDistance = convertedDistance * 2;
                        Log.d(TAG, "convertedDistance = " + convertedDistance);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /* network available */
    @SuppressLint("ResourceAsColor")
    @Override
    public void networkAvailable()
    {
        if(isNetworkAvailable)
        {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.internet_msg, Snackbar.LENGTH_LONG)
                    .setTextColor(Color.WHITE)
                    .setBackgroundTint(Color.GREEN)
                    .setDuration(5000)
                    .show();
        }

        //draw current location and draw marker on map
        /*if (currentLatitude != 0.0 && currentLongitude != 0.0)
        {
            getAddressFromCurrentLocation(currentLatitude, currentLongitude);

            if(pickupMarker != null)
            {
                pickupMarker.remove();
            }

            *//* current location marker *//*
            pickupMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(currentLatitude, currentLongitude))
                    .title(city)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                    (new LatLng(currentLatitude, currentLongitude), 10.0f));

            *//* check pickup-list size and add or update data in list*//*
            if(pickupLocationList.size() > 0)
            {
                pickupLocationList.set(0, new LatLng(currentLatitude, currentLongitude));
                Log.d("TAG", "size update pickup = " + pickupLocationList.size());
            }
            else
            {
                pickupLocationList.add(new LatLng(currentLatitude, currentLongitude));
                Log.d("TAG", "pickup size = " + pickupLocationList.size());
            }

            AddMarker(pickup_city);
            Log.d(TAG, "size 0 for current loc = " + pickupLocationList.size());
        } else {
            Log.d(TAG, "Not getting co-ordinates");
        }*/
    }

    /* no internet connection */
    @Override
    public void networkUnavailable() {
        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.no_internet_msg, Snackbar.LENGTH_LONG)
                .setTextColor(Color.WHITE)
                .setBackgroundTint(Color.RED)
                .setDuration(5000)
                .show();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(currentLatitude, currentLongitude), 10.0f));

        isNetworkAvailable = true;
    }

    /* register broadcast receiver*/
    public void startNetworkBroadcastReceiver(Context currentContext) {
        connectivityHelper = new ConnectivityHelper();
        connectivityHelper.addListener(this);
        registerNetworkBroadcastReceiver(currentContext);
    }

    /* compare two dates end date is not less than start date */
    @SuppressLint("SimpleDateFormat")
    public static boolean isDateAfter(String startDate,String endDate)
    {
        try
        {
            String myFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; // for example
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date end_date = df.parse(endDate);
            Date start_date = df.parse(startDate);

            return end_date.after(start_date);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /* set current pickup date in format for display on ui */
    @SuppressLint("SimpleDateFormat")
    public void GetCurrentPickupDate()
    {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        strPickupDate = simpleDateFormat.format(c.getTime());
        Log.d(TAG, "strPickupDate = " + strPickupDate);

        //converted format
        Date date = null;
        try {
            date = simpleDateFormat.parse(strPickupDate);
            SimpleDateFormat sdfOutputDateFormat = new SimpleDateFormat("EEE, dd MMM");
            curr_converted_date = sdfOutputDateFormat.format(date);
            Log.d(TAG, "curr_converted_date - " + curr_converted_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtPickupDatePicker.setText(curr_converted_date);
    }

    /* set current drop date in format for display on ui */
    @SuppressLint("SimpleDateFormat")
    public void GetCurrentDropDate()
    {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        strDropDate = simpleDateFormat.format(c.getTime());
        Log.d(TAG, "strDropDate = " + strDropDate);

        //converted format
        Date date = null;
        try {
            date = simpleDateFormat.parse(strDropDate);
            SimpleDateFormat sdfOutputDateFormat = new SimpleDateFormat("EEE, dd MMM");
            curr_converted_date = sdfOutputDateFormat.format(date);
            Log.d(TAG, "curr_converted_date - " + curr_converted_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtDropDatePicker.setText(curr_converted_date);
    }

    @SuppressLint("SimpleDateFormat")
    private void CalculateDropTime(String startDateFromLabel, String totalDuration)
    {
        Log.d(TAG, "start = " + startDateFromLabel);

        SimpleDateFormat simpleDateFormat;
        //total_duration = "1 day 1 hour 3 minutes";
        String[] strArray = totalDuration.split("\\D+");

        Date start_date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            start_date = sdf.parse(startDateFromLabel);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //String start_date = sdf.format();
        //display in 24hrs format
        Log.d(TAG, "start_date = " + start_date);

        Calendar calendar = Calendar.getInstance();

        assert start_date != null;
        if (strArray.length == 3)
        {
            Log.d(TAG, "array value 0 = " + strArray[0]);
            Log.d(TAG, "array value 1 = " + strArray[1]);
            Log.d(TAG, "array value 2 = " + strArray[2]);

            calendar.setTime(start_date);
            calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(strArray[0]));
            calendar.add(Calendar.HOUR, Integer.parseInt(strArray[1]));
            calendar.add(Calendar.MINUTE, Integer.parseInt(strArray[2]));

            //Log.v("sample date --- ",""+calendar.getTime());
            Log.v(TAG, "calender time = " + calendar.getTime());

            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sendToApiDropTime = simpleDateFormat.format(calendar.getTime());
            Log.v(TAG, "calender time = " + calendar.getTime());
            Log.v(TAG, "sendToApiDropTime = " + sendToApiDropTime);
        }
        else if (strArray.length == 2)
        {
            calendar.setTime(start_date);
            Log.d(TAG, "array value 0 = " + strArray[0]);
            Log.d(TAG, "array value 1 = " + strArray[1]);

            calendar.add(Calendar.HOUR, Integer.parseInt(strArray[0]));
            calendar.add(Calendar.MINUTE, Integer.parseInt(strArray[1]));
            //calendar.add(Calendar.SECOND, seconds);
            //Log.v("sample date --- ",""+calendar.getTime());

            Log.v(TAG, "calender time = " + calendar.getTime());

            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sendToApiDropTime = simpleDateFormat.format(calendar.getTime());
            Log.v(TAG, "sendToApiDropTime = " + sendToApiDropTime);
        }
        else if (strArray.length == 1)
        {
            calendar.setTime(start_date);
            Log.d(TAG, "array value 0 = " + strArray[0]);

            calendar.add(Calendar.MINUTE, Integer.parseInt(strArray[0]));
            //calendar.add(Calendar.SECOND, seconds);
            //Log.v("sample date --- ",""+calendar.getTime());

            Log.v(TAG, "calender time = " + calendar.getTime());

            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sendToApiDropTime = simpleDateFormat.format(calendar.getTime());
            Log.v(TAG, "sendToApiDropTime = " + sendToApiDropTime);
        }

    }

    /* time */
    private void SetTimeForCurrentDate(int current_hour, int current_time){
        time = new ArrayList<>();
        String s_time="";
        String s_mint="";
        String am_pm="";
        int skip_hour=0;
        int skip_00 =0;
        int skip_30 =0;
        if(current_time<=15){
            skip_hour =1;
            skip_00 =1;
        }else if(current_time>15 && current_time<=30){
            skip_hour =1;
            skip_00 = 1;
        }else if(current_time>30 && current_time<=45){
            skip_hour =1;
            skip_00 = 1;
            skip_30 = 1;
        }else{
            skip_hour =2;
        }
        s_time = UpdateTime(current_hour+1);
        if(current_hour+1>=12){
            am_pm = "pm";
        }else{
            am_pm = "am";
        }
        time.add(new KeyValue("0",s_time+":"+current_time+" "+am_pm));


        for(int i=current_hour+skip_hour; i < 24 ; i++){
            for(int j=0+skip_00;j<2-skip_30;j++){
                if(j%2!=0){
                    s_mint = ":30";
                }else{
                    s_mint = ":00";
                }
                s_time = UpdateTime(i);
                if(i>=12){
                    am_pm = "pm";
                }else{
                    am_pm = "am";
                }
                time.add(new KeyValue("0",s_time+s_mint+" "+am_pm));
            }
            skip_00 = 0;
            skip_30 = 0;

        }
    }

    /* time for future date */
    private void SetTimeForFutureDate()
    {
        time = new ArrayList<>();
        String s_time="";
        String s_mint="";
        String am_pm="";

        for(int i=0; i < 24 ; i++){
            for(int j=0;j<2;j++){
                if(j%2!=0){
                    s_mint = ":30";
                }else{
                    s_mint = ":00";
                }
                s_time = UpdateTime(i);
                if(i>=12){
                    am_pm = "pm";
                }else{
                    am_pm = "am";
                }
                time.add(new KeyValue("0", s_time + s_mint +" "+am_pm));
            }
        }
    }

    /* update time */
    private String UpdateTime(int t)
    {
        String s_hour="";
        switch (t){
            case 0:
                s_hour = "00";
                break;
            case 12:
                s_hour = "12";
                break;
            case 1:
            case 13:
                s_hour = "01";
                break;
            case 2:
            case 14:
                s_hour = "02";
                break;
            case 3:
            case 15:
                s_hour = "03";
                break;
            case 4:
            case 16:
                s_hour = "04";
                break;
            case 5:
            case 17:
                s_hour = "05";
                break;
            case 6:
            case 18:
                s_hour = "06";
                break;
            case 7:
            case 19:
                s_hour = "07";
                break;
            case 8:
            case 20:
                s_hour = "08";
                break;
            case 9:
            case 21:
                s_hour = "09";
                break;
            case 10:
            case 22:
                s_hour = "10";
                break;
            case 11:
            case 23:
                s_hour = "11";
                break;
        }
        return s_hour;
    }

    /* get prior time */
    @SuppressLint("SimpleDateFormat")
    public void GetPriorTime()
    {
        //Time Picker
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String[] hour_min = currentTime.split(":");
        String hour = hour_min[0];
        String min = hour_min[1];

        int i_hour = Integer.parseInt(hour);
        int i_mint = Integer.parseInt(min);

        if(isCurrentDateBooking)
        {
            if(i_hour >= 23)
            {
                //Toast.makeText(getActivity(), "For Outstation, We are catering Advance booking. Kindly Select the date accordingly.", Toast.LENGTH_SHORT).show();
                errorDialog = new ErrorDialog(getActivity(), "For Outstation, We are catering Advance booking. Kindly Select the date accordingly.");
                errorDialog.show();
            }
            else
            {
                SetTimeForCurrentDate(i_hour,i_mint);
            }
        }
        else
        {
            SetTimeForFutureDate();
        }

        timeAdapter = new TimeAdapter(getActivity(), R.layout.spinner_list_item,time);
        pickup_spinner_time.setAdapter(timeAdapter);
    }

    /*Calculate drop time for round trip. get date and add 23:59:59 time*/
    @SuppressLint("SimpleDateFormat")
    public void CalculateDropTimeForRoundTrip(String drop_date)
    {
        SimpleDateFormat sdf;
        Log.d(TAG, "start = " + drop_date);

        Date start_date = null;
        try {
            sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            start_date = sdf.parse(drop_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //String start_date = sdf.format();
        //display in 24hrs format
        Log.d(TAG, "start_date = " + start_date);

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sendToApiDropTime = sdf.format(start_date);

        Log.d(TAG, "sendToApiDropTime in round trip = " + sendToApiDropTime);
    }

    /* current date convert in different format to send api */
    @SuppressLint("SimpleDateFormat")
    public void getCurrentDateToSendApiInFormat(String pickupDateFromTextbox)
    {
        //current format
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

        //converted format
        Date date = null;
        try {
            date = inputDateFormat.parse(pickupDateFromTextbox);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sendToApiPickupTime = sdf2.format(date);
            Log.d(TAG, "sendToApiPickupTime - " + sendToApiPickupTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Sun, 01 May
    @SuppressLint("SimpleDateFormat")
    public void getStartDateInFormat(String current_date)
    {
        //Convert to date format
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        Date date = null;
        try {
            date = inputDateFormat.parse(current_date);
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, dd MMM");
            changeStartDateFormat = sdf2.format(date);
            Log.d(TAG, "changeStartDateFormat - " + changeStartDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zooms a Route (given a List of LalLng) at the greatest possible zoom level.
     *
     * @param googleMap: instance of GoogleMap
     * @param lstLatLngRoute: list of LatLng forming Route
     */
    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();
        int left, right,bottom, top;
        //googleMap.setPadding(left = 10, top = 30, right = 10, bottom = 10);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    /* hide keyboard */
    public static void hideKeyboardFrom(Context context, View view)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /* register broadcast receiver */
    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(connectivityHelper,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    /* unregister broadcast receiver */
    public void unregisterNetworkBroadcastReceiver(Context currentContext) {
        currentContext.unregisterReceiver(connectivityHelper);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
        try {
            if (mGoogleApiClient != null)
            {
                mGoogleApiClient.connect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG,"onStop");
        isDrop = false;
        isNetworkAvailable = false;

        /*if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");

        mGoogleApiClient.connect();
        //isCurrent = true;

        unregisterNetworkBroadcastReceiver(getActivity());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        isDrop = false;
        isNetworkAvailable = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerNetworkBroadcastReceiver(getActivity());
    }
}
