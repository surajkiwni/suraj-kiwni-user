package com.kiwni.app.user.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import com.kiwni.app.user.R;
import com.kiwni.app.user.activity.FindCarActivity;
import com.kiwni.app.user.adapter.AutoCompleteAdapter;
import com.kiwni.app.user.adapter.TimeAdapter;
import com.kiwni.app.user.datamodels.ErrorDialog;
import com.kiwni.app.user.models.DirectionsJSONParser;
import com.kiwni.app.user.models.KeyValue;
import com.kiwni.app.user.network.ApiInterface;
import com.kiwni.app.user.sharedpref.SharedPref;
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

public class RoundTripFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener

{
    AppCompatButton btnViewCabRoundTrip, btnCurrentLocation, btnLocationOnMap, btnConfirm;
    ImageView imageMarker;
    String TAG = this.getClass().getSimpleName();
    ConstraintLayout layoutPickupDatePicker, layoutDropDatePicker;


    public double currentLatitude = 0.0, currentLongitude = 0.0;
    GoogleMap mMap;
    Marker pickupMarker, dropMarker;
    private Polyline mPolyline;
    AutoCompleteTextView autoCompleteTextViewPickup, autoCompleteTextViewDrop;
    AutoCompleteAdapter adapter;
    LinearLayout linearFooterButtons, linearBtnConfirm;
    ArrayList<LatLng> pickupLocationList = new ArrayList<>();
    ArrayList<LatLng> dropLocationList = new ArrayList<>();
    PlacesClient placesClient;
    String address = "", city = "", pickup_city = "", drop_city = "", pickup_address = "", drop_address = "";
    private GoogleApiClient mGoogleApiClient;
    private LatLng mCenterLatLong, mOrigin, mDestination;
    double pickup_lat = 0.0, pickup_lng = 0.0, drop_lat = 0.0, drop_lng = 0.0;
    boolean isPickup = false, isDrop = false, isCurrent = false, isDDSelected = false, isLocated = false;
    Context mContext;
    AutocompletePrediction item;
    String direction = "", distanceTextFromApi = "", distanceValueFromApi = "",
            durationTextFromApi = "", durationInTrafficFromApi = "",
            curr_converted_date = "", strDropDate = "", strPickupDate = "", convertedDateFormat = "",
            convertedDropDateFormat = "";

    Spinner pickup_spinner_time;
    ArrayList<KeyValue> time = new ArrayList<>();
    TimeAdapter timeAdapter;
    boolean isCurrentDateBooking = false;
    TextView txtPickupDatePicker, txtDropDatePicker;
    Double convertedDistance;

    String concatDateTime = "", sendToApiPickupTime = "",
            sendToApiDropTime = "", changeStartDateFormat ="";
    int mYear, mMonth, mDay, mHour, mMinute;

    public RoundTripFragment() {
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
        View view = inflater.inflate(R.layout.fragment_round_trip, container, false);

        mContext = getActivity();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        btnViewCabRoundTrip = view.findViewById(R.id.btnViewCabRoundTrip);
        linearFooterButtons = view.findViewById(R.id.linearFooterButtons);
        btnCurrentLocation = view.findViewById(R.id.btnCurrentLocation);
        btnLocationOnMap = view.findViewById(R.id.btnLocationOnMap);
        //txtMarkerText = view.findViewById(R.id.txtMarkerText);
        imageMarker = view.findViewById(R.id.imageMarker);
        linearBtnConfirm = view.findViewById(R.id.linearBtnConfirm);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        txtDropDatePicker = view.findViewById(R.id.txtDropDatePicker);
        pickup_spinner_time = view.findViewById(R.id.pickup_spinner_time);
        txtPickupDatePicker = view.findViewById(R.id.txtPickupDatePicker);

        layoutPickupDatePicker = view.findViewById(R.id.layoutPickupDatePicker);
        layoutDropDatePicker = view.findViewById(R.id.layoutDropDatePicker);

        /*initialized place sdk*/
        if (!Places.isInitialized()) {
            //initialized the sdk
            Log.d(TAG, "key = " + R.string.google_maps_key);
            Places.initialize(getActivity(), ApiInterface.GOOGLE_MAP_API_KEY);
        }
        /*create new place client instance*/
        placesClient = Places.createClient(getActivity());
        adapter = new AutoCompleteAdapter(getActivity(), placesClient);

        // in below line we are initializing our array list.
        pickupLocationList = new ArrayList<>();
        dropLocationList = new ArrayList<>();

        autoCompleteTextViewPickup = (AutoCompleteTextView) view.findViewById(R.id.auto_pickup);
        autoCompleteTextViewPickup.setOnItemClickListener(autocompleteClickListener);
        //set adapter
        autoCompleteTextViewPickup.setAdapter(adapter);

        autoCompleteTextViewDrop = (AutoCompleteTextView) view.findViewById(R.id.auto_destination);
        autoCompleteTextViewDrop.setOnItemClickListener(autocompleteClickListener);
        //set adapter
        autoCompleteTextViewDrop.setAdapter(adapter);

        direction = "two-way";
        //get current location and draw marker on map
        currentLatitude = Double.parseDouble(PreferencesUtils.getPreferences(getActivity(), SharedPref.USER_CURRENT_LAT, ""));
        currentLongitude = Double.parseDouble(PreferencesUtils.getPreferences(getActivity(), SharedPref.USER_CURRENT_LNG, ""));
        Log.d(TAG, currentLatitude + ", " + currentLongitude);

        if(currentLatitude != 0.0 && currentLongitude != 0.0)
        {
            getAddressFromCurrentLocation(currentLatitude, currentLongitude);
            //DrawMarker(currentLatitude, currentLongitude, pickup_city);
            pickupLocationList.add(new LatLng(currentLatitude, currentLongitude));
            AddMarker(pickup_city);
            Log.d(TAG, "size 0 for current loc = " + pickupLocationList.size());
        }
        else
        {
            Log.d(TAG, "Not getting co-ordinates");
        }

        //get current date in format and set to UI
        GetCurrentPickupDate();
        GetCurrentDropDate();

        //compare two dates are same or not
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM");
        try {
            Date currentDate = inputFormat.parse(curr_converted_date);
            Date pickerDate = inputFormat.parse(txtPickupDatePicker.getText().toString());

            if(currentDate.compareTo(pickerDate) == 0)
            {
                time.clear();

                KeyValue keyValue = new KeyValue("0", "select time");
                time.add(0, keyValue);
                isCurrentDateBooking = true;
            }
            else {
                isCurrentDateBooking = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //1 hr time prior
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

        //Date picker
        layoutPickupDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
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
                                //c.set(mYear,mMonth,mDay);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                strPickupDate = simpleDateFormat.format(c.getTime());
                                Log.d(TAG, strPickupDate);

                                //converted format
                                Date date = null;
                                try {
                                    date = (Date) simpleDateFormat.parse(strPickupDate);
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

                                    if(currentDate.compareTo(pickerDate) == 0)
                                    {
                                        time.clear();

                                        KeyValue keyValue = new KeyValue("0", "select time");
                                        time.add(0, keyValue);
                                        isCurrentDateBooking = true;
                                    }
                                    else {
                                        isCurrentDateBooking = false;
                                    }
                                }
                                catch (Exception e)
                                {
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
                                //c.set(mYear,mMonth,mDay);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                strDropDate = simpleDateFormat.format(c.getTime());
                                Log.d(TAG, strDropDate);

                                //converted format
                                Date date = null;
                                try {
                                    date = (Date) simpleDateFormat.parse(strDropDate);
                                    SimpleDateFormat sdfOutputDateFormat = new SimpleDateFormat("EEE, dd MMM");
                                    convertedDropDateFormat = sdfOutputDateFormat.format(date);
                                    Log.d(TAG, "convertedDropDateFormat - " + convertedDropDateFormat);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                txtDropDatePicker.setText(convertedDropDateFormat);

                                //compare two dates are same or not
                                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM");
                                try {
                                    Date currentDate = inputFormat.parse(curr_converted_date);
                                    Date pickerDate = inputFormat.parse(convertedDropDateFormat);

                                    if(currentDate.compareTo(pickerDate) == 0)
                                    {
                                        isCurrentDateBooking = true;
                                    }
                                    else {
                                        isCurrentDateBooking = false;
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        //click events of autocomplete
        autoCompleteTextViewPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isLocated = false;
                autoCompleteTextViewPickup.setSelectAllOnFocus(true);
            }
        });

        autoCompleteTextViewPickup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                isPickup = true;
                isDrop = false;
                isLocated = false;

                Log.d(TAG, isPickup + " " + isDrop);

                autoCompleteTextViewPickup.setSelectAllOnFocus(true);
                autoCompleteTextViewPickup.selectAll();

                linearFooterButtons.setVisibility(View.VISIBLE);
                btnCurrentLocation.setVisibility(View.VISIBLE);
                linearBtnConfirm.setVisibility(View.GONE);
                btnViewCabRoundTrip.setVisibility(View.GONE);

                return false;
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
            public void afterTextChanged(Editable s)
            {
                if(s.length() > 15)
                {
                    autoCompleteTextViewPickup.dismissDropDown();
                }
            }
        });

        autoCompleteTextViewDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextViewDrop.setSelectAllOnFocus(true);
            }
        });

        autoCompleteTextViewDrop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                isDrop = true;
                isPickup = false;
                isLocated = false;

                autoCompleteTextViewDrop.setSelectAllOnFocus(true);
                //autoCompleteTextViewDrop.requestFocus();
                autoCompleteTextViewDrop.selectAll();

                linearFooterButtons.setVisibility(View.VISIBLE);
                btnCurrentLocation.setVisibility(View.GONE);
                linearBtnConfirm.setVisibility(View.VISIBLE);
                btnViewCabRoundTrip.setVisibility(View.GONE);

                return false;
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
                if(s.length() > 15)
                {
                    autoCompleteTextViewDrop.dismissDropDown();
                }
            }
        });

        btnLocationOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //txtMarkerText.setVisibility(View.VISIBLE);
                imageMarker.setVisibility(View.VISIBLE);
                linearFooterButtons.setVisibility(View.GONE);
                linearBtnConfirm.setVisibility(View.VISIBLE);

                if (isPickup)
                {
                    hideKeyboardFrom(getActivity(), autoCompleteTextViewPickup);
                }
                else
                {
                    hideKeyboardFrom(getActivity(), autoCompleteTextViewDrop);
                }

                if(!isLocated)
                {
                    CameraChange();
                }
            }
        });

        btnCurrentLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                isCurrent = true;
                isLocated = true;
                linearFooterButtons.setVisibility(View.GONE);
                //linearBtnDone.setVisibility(View.VISIBLE);

                //get current location
                if(currentLatitude != 0.0 && currentLongitude != 0.0)
                {
                    getAddressFromCurrentLocation(currentLatitude, currentLongitude);

                    pickupMarker.remove();
                    DrawMarker(currentLatitude, currentLongitude, pickup_city);

                    if(pickupLocationList.size() > 0)
                    {
                        pickupLocationList.set(0, new LatLng(currentLatitude, currentLongitude));
                        Log.d(TAG, "size update pickup = " + pickupLocationList.size());
                    }
                    else
                    {
                        Log.d(TAG, "pickup size = " + pickupLocationList.size());
                    }

                    AddMarker(pickup_city);
                    Log.d(TAG, "size 2 = " + pickupLocationList.size());

                    if(pickupLocationList.size() == 1 && dropLocationList.size() == 1)
                    {
                        drawRoute();
                    }
                }

                if(!autoCompleteTextViewPickup.getText().toString().equals("")
                        && !autoCompleteTextViewDrop.getText().toString().equals(""))
                {
                    Log.d(TAG,"enable find car button");
                    linearBtnConfirm.setVisibility(View.GONE);
                    btnViewCabRoundTrip.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearBtnConfirm.setVisibility(View.GONE);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                isLocated = true;

                if(isPickup)
                {
                    //pickup
                    if(isDDSelected)
                    {
                        isDDSelected = false;
                        linearBtnConfirm.setVisibility(View.GONE);

                        if(pickupLocationList.size() == 1 && dropLocationList.size() == 1)
                        {
                            drawRoute();
                        }
                    }
                    else
                    {
                        imageMarker.setVisibility(View.GONE);
                        linearBtnConfirm.setVisibility(View.GONE);

                        pickupMarker.remove();
                        DrawMarker(pickup_lat, pickup_lng, pickup_city);

                        if(pickupLocationList.size() > 0)
                        {
                            pickupLocationList.set(0, new LatLng(pickup_lat,pickup_lng));
                            Log.d(TAG, "size update pickup = " + pickupLocationList.size());
                        }
                        else
                        {
                            Log.d(TAG, "pickup size = " + pickupLocationList.size());
                        }

                        AddMarker(pickup_city);
                        Log.d(TAG, "size 2 = " + pickupLocationList.size());

                        if(pickupLocationList.size() == 1 && dropLocationList.size() == 1)
                        {
                            drawRoute();
                        }
                    }
                }
                else
                {
                    //drop
                    if(pickup_city.equals(drop_city))
                    {
                        autoCompleteTextViewDrop.setText("");
                        Toast.makeText(getActivity(), "Please Select Different City..!", Toast.LENGTH_SHORT).show();
                        linearBtnConfirm.setVisibility(View.GONE);
                        //mMap.clear();

                        //txtMarkerText.setVisibility(View.GONE);
                        imageMarker.setVisibility(View.GONE);

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
                            Log.d(TAG,"isDDSelected = " + isDDSelected);
                            if(isDDSelected)
                            {
                                isDDSelected = false;
                                linearBtnConfirm.setVisibility(View.GONE);

                                Log.d(TAG,"dropList Size = " + dropLocationList.size());
                                Log.d(TAG,"pickupList Size = " + pickupLocationList.size());

                                if(pickupLocationList.size() == 1 && dropLocationList.size() == 1)
                                {
                                    drawRoute();
                                }
                            }
                            else
                            {
                                imageMarker.setVisibility(View.GONE);
                                linearBtnConfirm.setVisibility(View.GONE);

                                if(dropLocationList.size() == 0)
                                {
                                    DrawMarker(drop_lat, drop_lng, drop_city);
                                    dropLocationList.add(new LatLng(drop_lat, drop_lng));
                                    Log.d(TAG, "size default = " + dropLocationList.size());
                                }
                                else
                                {
                                    dropMarker.remove();
                                    DrawMarker(drop_lat, drop_lng, drop_city);
                                    dropLocationList.set(0, new LatLng(drop_lat,drop_lng));
                                    Log.d(TAG, "size update drop = " + dropLocationList.size());
                                }

                                AddMarker(drop_city);
                                Log.d(TAG,"size 2 = " + dropLocationList.size());

                                if(pickupLocationList.size() == 1 && dropLocationList.size() == 1)
                                {
                                    drawRoute();
                                }
                            }
                        }
                    }
                }

                if(!autoCompleteTextViewPickup.getText().toString().equals("")
                        && !autoCompleteTextViewDrop.getText().toString().equals(""))
                {
                    Log.d(TAG,"enable find car button");
                    linearBtnConfirm.setVisibility(View.GONE);
                    linearFooterButtons.setVisibility(View.GONE);
                    btnViewCabRoundTrip.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearFooterButtons.setVisibility(View.GONE);
                }
            }
        });

        btnViewCabRoundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!isNetworkConnected())
                {
                    //Toast.makeText(getActivity(), "No internet. Connect to wifi or cellular network.", Toast.LENGTH_SHORT).show();
                    ErrorDialog errorDialog = new ErrorDialog(getActivity(), "No internet. Connect to wifi or cellular network.");
                    errorDialog.show();
                }
                else
                {
                    //round-trip
                    isPickup = false;
                    isDrop = false;
                    if (autoCompleteTextViewPickup.getText().toString().equals(""))
                    {
                        //txtPickupLocation.setError("Pickup location field cannot be empty.!");
                        //Toast.makeText(getActivity(), "Pickup location field cannot be empty.!", Toast.LENGTH_SHORT).show();
                        ErrorDialog errorDialog = new ErrorDialog(getActivity(), "Pickup location field cannot be empty.!");
                        errorDialog.show();
                    } else if (autoCompleteTextViewDrop.getText().toString().equals("")) {
                        //txtDropLocation.setError("Drop location field cannot be empty.!");
                        //Toast.makeText(getActivity(), "Drop location field cannot be empty.!", Toast.LENGTH_SHORT).show();
                        ErrorDialog errorDialog = new ErrorDialog(getActivity(), "Drop location field cannot be empty.!");
                        errorDialog.show();
                    } else if (txtPickupDatePicker.getText().toString().equals("")) {
                        //txtDropLocation.setError("Drop location field cannot be empty.!");
                        //Toast.makeText(getActivity(), "Pickup Date field cannot be empty.!", Toast.LENGTH_SHORT).show();
                        ErrorDialog errorDialog = new ErrorDialog(getActivity(), "Pickup Date field cannot be empty.!");
                        errorDialog.show();
                    }
                    else if(pickup_spinner_time.getSelectedItem().toString().isEmpty() || pickup_spinner_time.getSelectedItem().toString() == "")
                    {
                        //Toast.makeText(getActivity(), "Pickup Time field cannot be empty.!", Toast.LENGTH_SHORT).show();
                        ErrorDialog errorDialog = new ErrorDialog(getActivity(), "Pickup Time field cannot be empty.!");
                        errorDialog.show();
                    }
                    else if(txtDropDatePicker.getText().toString().equals(""))
                    {
                        //Toast.makeText(getActivity(), "Destination Date field cannot be empty.!", Toast.LENGTH_SHORT).show();
                        ErrorDialog errorDialog = new ErrorDialog(getActivity(), "Destination Date field cannot be empty.!");
                        errorDialog.show();
                    }
                    else
                    {
                        try
                        {
                            Date currentDate = inputFormat.parse(curr_converted_date);
                            Date pickerDate = inputFormat.parse(txtPickupDatePicker.getText().toString());

                            if (currentDate.compareTo(pickerDate) == 0 && pickup_spinner_time.getSelectedItem().toString() == "select time")
                            {
                                //Toast.makeText(getActivity(), "For Outstation, We are catering Advance booking. Kindly Select the date accordingly.", Toast.LENGTH_SHORT).show();
                                ErrorDialog errorDialog = new ErrorDialog(getActivity(), "For Outstation, We are catering Advance booking. Kindly Select the date accordingly.");
                                errorDialog.show();
                            }
                            else
                            {
                                concatDateTime = strPickupDate + " " + pickup_spinner_time.getSelectedItem().toString();
                                Log.d(TAG,"concat pickup date time = " + concatDateTime);

                                getCurrentDateToSendApiInFormat(concatDateTime);

                                distanceValueFromApi = String.valueOf(Double.parseDouble(distanceValueFromApi) / 1000);
                                Log.d(TAG, "distanceValueFromApi = " + distanceValueFromApi.trim());
                                convertedDistance = Double.valueOf(distanceValueFromApi.trim());
                                Log.d(TAG, "convertedDistance = " + convertedDistance);
                                convertedDistance = convertedDistance * 2;
                                Log.d(TAG, "convertedDistance = " + convertedDistance);

                                //change date format and send to next screen
                                getStartDateInFormat(concatDateTime);

                                //Calculate droptime from startdate and 23:59:59
                                Log.d(TAG, "field time = " + txtDropDatePicker.getText().toString());

                                CalculateDropTimeForRoundTrip(strDropDate + " 23:59:59");
                                Log.d(TAG, "dropDateTime = " + sendToApiDropTime);

                                // call next activity
                                Log.d(TAG, "mOrigin = " + mOrigin + ", " + pickup_city + ", " + drop_city);
                                Log.d(TAG, "mDestination = " + mDestination);

                                Intent i = new Intent(getActivity(), FindCarActivity.class);

                                //send data to next screen
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_CITY, pickup_city);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_CITY, drop_city);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_ADDRESS, autoCompleteTextViewPickup.getText().toString());
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_ADDRESS, autoCompleteTextViewDrop.getText().toString());
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_LOCATION, mOrigin.latitude + " " + mOrigin.longitude);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_LOCATION, mDestination.latitude + " " + mDestination.longitude);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_DATE, concatDateTime);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.START_DATE_WITH_MONTH_DAY, changeStartDateFormat);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.DIRECTION, direction);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.SERVICE_TYPE, "Outstation");
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.DISTANCE, String.valueOf(convertedDistance));
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.DURATION_IN_TRAFFIC, durationInTrafficFromApi);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_TIME_FOR_API, sendToApiPickupTime);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_TIME_FOR_API, sendToApiDropTime);
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_DATE_TO_DISPLAY, txtPickupDatePicker.getText().toString());
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.PICKUP_TIME_TO_DISPLAY, pickup_spinner_time.getSelectedItem().toString());
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.DROP_DATE_TO_DISPLAY, txtDropDatePicker.getText().toString());
                                PreferencesUtils.putPreferences(getActivity(), SharedPref.DISTANCE_IN_KM, distanceTextFromApi);

                                startActivity(i);
                                //getActivity().finish();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    //autocompletetextview listener
    public AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            try
            {
                item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                Log.d(TAG, "places id with list = " + placeID + ", " + placeFields.toString());

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null)
                {
                    placesClient.fetchPlace(request).addOnCompleteListener(new OnCompleteListener<FetchPlaceResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<FetchPlaceResponse> task)
                        {
                            FetchPlaceResponse places = task.getResult();
                            final Place place = places.getPlace();
                            Log.d(TAG, "places = " + place.getAddress());

                            if(isPickup)
                            {
                                pickup_lat = places.getPlace().getLatLng().latitude;
                                pickup_lng = places.getPlace().getLatLng().longitude;
                                pickup_address = places.getPlace().getAddress();

                                if(!isDDSelected)
                                {
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
                            }

                            if(!autoCompleteTextViewPickup.getText().toString().equals("")
                                    && !autoCompleteTextViewDrop.getText().toString().equals(""))
                            {
                                Log.d(TAG,"enable view cabs button");
                                linearBtnConfirm.setVisibility(View.VISIBLE);
                                linearFooterButtons.setVisibility(View.GONE);
                                btnViewCabRoundTrip.setVisibility(View.GONE);
                            }
                            else
                            {
                                linearFooterButtons.setVisibility(View.GONE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        if (mMap != null) {
            mMap.clear();
            autoCompleteTextViewDrop.setText("");
        }
        mMap = googleMap;

        pickupMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentLatitude, currentLongitude))
                .title(city)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(currentLatitude, currentLongitude), 9.0f));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if (mLastLocation != null) {
            //changeMap(mLastLocation);
            Log.d(TAG, "ON connected");

        } else
            try {
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
    public void onLocationChanged(Location location) {
        try
        {
            if (location != null)
                //changeMap(location);
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

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

                    try
                    {
                        Location mLocation = new Location("");
                        mLocation.setLatitude(mCenterLatLong.latitude);
                        mLocation.setLongitude(mCenterLatLong.longitude);

                        if(isPickup)
                        {
                            pickup_lat = mCenterLatLong.latitude;
                            pickup_lng = mCenterLatLong.longitude;

                            //txtMarkerText.setText("Lat1 : " + pickup_lat + "," + "Long1 : " + pickup_lng);
                            if(pickup_lat != 0.0 && pickup_lng != 0.0)
                            {
                                getAddressFromCurrentLocation(pickup_lat, pickup_lng);
                            }
                        }
                        else
                        {
                            drop_lat = mCenterLatLong.latitude;
                            drop_lng = mCenterLatLong.longitude;

                            //txtMarkerText.setText("Lat1 : " + drop_lat + "," + "Long1 : " + drop_lng);
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

    public void DrawMarker(Double latitude, Double longitude, String city)
    {
        if(isPickup)
        {
            pickupMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(city)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                    (new LatLng(latitude, longitude), 9.0f));
        }
        else if(isDrop)
        {
            dropMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(city)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                    (new LatLng(latitude, longitude), 9.0f));
        }
        else
        {
            /*mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(city)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                    (new LatLng(latitude, longitude), 10.0f));*/
        }
    }

    public void AddMarker(String title)
    {
        if(pickupLocationList.size() == 1)
        {
            mOrigin = pickupLocationList.get(0);
            Log.d(TAG, "mOrigin = " + mOrigin);
        }
        else
        {
            mOrigin = null;
            Log.d(TAG, "size of add marker 1 = " + pickupLocationList.size());
        }

        if (dropLocationList.size() == 1)
        {
            mDestination = dropLocationList.get(0);
            Log.d(TAG, "mDestination = " + mDestination);
        }
        else
        {
            mDestination = null;
            Log.d(TAG, "size of add marker 1 = " + pickupLocationList.size());
            Log.d(TAG, "size of add marker 2 = " + dropLocationList.size());
        }
    }

    public void drawRoute()
    {
        Log.d(TAG, "mOrigin = " + mOrigin);
        Log.d(TAG, "mDestination = " + mDestination);

        /*Getting URL to the Google Directions API*/
        String directionUrl = getDirectionsUrl(mOrigin, mDestination);
        Log.d(TAG, "directionUrl = " + directionUrl);

        DownloadDirectionTask downloadTask = new DownloadDirectionTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(directionUrl);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest)
    {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        Log.d(TAG, "str_origin = " + str_origin);
        //String str_origin = "origin=18.5204,73.8567";
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        Log.d(TAG, "str_dest = " + str_dest);
        //String str_dest = "destination=19.0760,72.8777";
        String mode = "driving";
        // Key
        //String key = "key=" + getString(R.string.google_maps_key);
        String key = "key=" + ApiInterface.GOOGLE_MAP_API_KEY;
        Log.d(TAG, "api key = " + key);
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&" + key;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d(TAG, "url = " + url);

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

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            DirectionParseTask parserTask = new DirectionParseTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Directions in JSON format
     */
    @SuppressWarnings("unchecked")
    @SuppressLint("StaticFieldLeak")
    private class DirectionParseTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
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

                    //getAddressFromLocation(lat, lng);

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
        }
    }

    //calculate distance and get from distance matrix api
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

                        /*//Calculate droptime from startdate and duration
                        Log.d(TAG, "date with duration = " + concatDateTime + "\n " + durationInTrafficFromApi);
                        CalculateDropTime(concatDateTime, durationInTrafficFromApi);*/
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void getAddressFromCurrentLocation(Double latitude, Double longitude)
    {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();

            Log.d(TAG, "address : " + address);
            Log.d(TAG, "city : " + city);

            if(isPickup)
            {
                //pickup
                pickup_city = city;

                if(isDDSelected)
                {
                    isDDSelected = true;
                    isLocated = true;
                    autoCompleteTextViewPickup.setSelection(autoCompleteTextViewPickup.getText().length());
                    hideKeyboardFrom(getActivity(), autoCompleteTextViewPickup);

                    //pickup
                    //txtMarkerText.setVisibility(View.GONE);
                    imageMarker.setVisibility(View.GONE);
                    linearBtnConfirm.setVisibility(View.GONE);

                    pickupMarker.remove();
                    DrawMarker(pickup_lat, pickup_lng, pickup_city);

                    if(pickupLocationList.size() > 0)
                    {
                        pickupLocationList.set(0, new LatLng(pickup_lat,pickup_lng));
                        Log.d(TAG, "size update pickup = " + pickupLocationList.size());

                        if(mPolyline != null)
                        {
                            mPolyline.remove();
                        }
                    }
                    else
                    {
                        Log.d(TAG, "pickup size = " + pickupLocationList.size());
                    }

                    AddMarker(pickup_city);
                    Log.d(TAG, "size 2 = " + pickupLocationList.size());
                }
                else
                {
                    pickup_address = address;
                    autoCompleteTextViewPickup.setText(pickup_address);
                    autoCompleteTextViewPickup.setSelection(autoCompleteTextViewPickup.getText().length());
                    hideKeyboardFrom(getActivity(), autoCompleteTextViewPickup);
                }
            }
            else if(isDrop)
            {
                //drop
                drop_city = city;

                if(isDDSelected)
                {
                    //autoCompleteTextViewDrop.setText(drop_address);
                    isDDSelected = true;
                    isLocated = true;
                    autoCompleteTextViewDrop.setSelection(autoCompleteTextViewDrop.getText().length());
                    hideKeyboardFrom(getActivity(), autoCompleteTextViewDrop);

                    //drop
                    if(pickup_city.equals(drop_city))
                    {
                        autoCompleteTextViewDrop.setText("");
                        //Toast.makeText(getActivity(), "Please Select Different City..!", Toast.LENGTH_SHORT).show();

                        ErrorDialog errorDialog = new ErrorDialog(getActivity(), "Please Select Different City..!");
                        errorDialog.show();

                        linearBtnConfirm.setVisibility(View.GONE);
                        imageMarker.setVisibility(View.GONE);
                        //mMap.clear();
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
                            //txtMarkerText.setVisibility(View.GONE);
                            imageMarker.setVisibility(View.GONE);
                            linearBtnConfirm.setVisibility(View.GONE);

                            if(dropLocationList.size() == 0)
                            {
                                DrawMarker(drop_lat, drop_lng, drop_city);
                                dropLocationList.add(new LatLng(drop_lat, drop_lng));
                                Log.d(TAG, "size default = " + dropLocationList.size());
                            }
                            else
                            {
                                dropMarker.remove();
                                DrawMarker(drop_lat, drop_lng, drop_city);
                                dropLocationList.set(0, new LatLng(drop_lat,drop_lng));
                                Log.d(TAG, "size update drop = " + dropLocationList.size());

                                if(mPolyline != null)
                                {
                                    mPolyline.remove();
                                }
                            }

                            AddMarker(drop_city);
                            Log.d(TAG,"size 2 = " + dropLocationList.size());
                        }
                    }
                }
                else
                {
                    autoCompleteTextViewDrop.setText(address);

                    autoCompleteTextViewDrop.setSelection(autoCompleteTextViewDrop.getText().length());
                    hideKeyboardFrom(getActivity(), autoCompleteTextViewDrop);
                }
            }
            else
            {
                //current
                pickup_city = city;
                autoCompleteTextViewPickup.setText(address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //set current date in format
    public void GetCurrentPickupDate()
    {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        strPickupDate = simpleDateFormat.format(c.getTime());
        Log.d(TAG, "strPickupDate = " + strPickupDate);

        //converted format
        Date date = null;
        try {
            date = (Date) simpleDateFormat.parse(strPickupDate);
            SimpleDateFormat sdfOutputDateFormat = new SimpleDateFormat("EEE, dd MMM");
            curr_converted_date = sdfOutputDateFormat.format(date);
            Log.d(TAG, "curr_converted_date - " + curr_converted_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtPickupDatePicker.setText(curr_converted_date);
    }

    public void GetCurrentDropDate()
    {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        strDropDate = simpleDateFormat.format(c.getTime());
        Log.d(TAG, "strDropDate = " + strDropDate);

        //converted format
        Date date = null;
        try {
            date = (Date) simpleDateFormat.parse(strDropDate);
            SimpleDateFormat sdfOutputDateFormat = new SimpleDateFormat("EEE, dd MMM");
            curr_converted_date = sdfOutputDateFormat.format(date);
            Log.d(TAG, "curr_converted_date - " + curr_converted_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtDropDatePicker.setText(curr_converted_date);
    }

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
                ErrorDialog errorDialog = new ErrorDialog(getActivity(), "For Outstation, We are catering Advance booking. Kindly Select the date accordingly.");
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

    //Calculate drop time for round trip. get date and add 23:59:59 time
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

    public void getCurrentDateToSendApiInFormat(String pickupDateFromTextbox)
    {
        //current format
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

        //converted format
        Date date = null;
        try {
            date = (Date) inputDateFormat.parse(pickupDateFromTextbox);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sendToApiPickupTime = sdf2.format(date);
            Log.d(TAG, "sendToApiPickupTime - " + sendToApiPickupTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //06 Dec
    public void getStartDateInFormat(String current_date)
    {
        //Convert to date format
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        Date date = null;
        try {
            date = (Date) inputDateFormat.parse(current_date);
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, dd MMM");
            changeStartDateFormat = sdf2.format(date);
            Log.d(TAG, "changeStartDateFormat - " + changeStartDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboardFrom(Context context, View view)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG,"onStop");

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");

        /*autoCompleteTextViewPickup.setText("");
        autoCompleteTextViewDrop.setText("");*/

        isCurrent = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG,"onResume");
    }
}