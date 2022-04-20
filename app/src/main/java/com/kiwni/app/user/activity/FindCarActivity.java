package com.kiwni.app.user.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.FindsCarRecyclerAdapter;
import com.kiwni.app.user.adapter.GridLayoutWrapper;
import com.kiwni.app.user.adapter.HourPackageAdapter;
import com.kiwni.app.user.datamodels.ErrorDialog;
import com.kiwni.app.user.global.PermissionRequestConstant;
import com.kiwni.app.user.interfaces.FindCarItemClickListener;
import com.kiwni.app.user.models.DirectionsJSONParser;
import com.kiwni.app.user.models.FindCar;
import com.kiwni.app.user.models.HourPackage;
import com.kiwni.app.user.models.vehicle_details.ProjectionScheduleErrorResp;
import com.kiwni.app.user.models.vehicle_details.ScheduleDates;
import com.kiwni.app.user.models.vehicle_details.ScheduleMapResp;
import com.kiwni.app.user.network.ApiClient;
import com.kiwni.app.user.network.ApiInterface;
import com.kiwni.app.user.network.AppConstants;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindCarActivity extends AppCompatActivity implements OnMapReadyCallback,FindCarItemClickListener
{
    GoogleMap mMap;
    RecyclerView recyclerView,findsCarsRecyclerView;
    List<HourPackage> hourPackageModelList;
    FindCarItemClickListener findCarItemClickListener;
    String TAG = this.getClass().getSimpleName();


    ImageView imageBack,imgCallFindCarAct;
    TextView txtTitle,viewDetailsText, txtFromTo, txtStartEndDate, txtStartTime, txtEstimatedKm;
    BottomSheetDialog bottomSheetDialog;
    List<FindCar> findCarModelList;

    ConstraintLayout constraintLayoutPack;
    String direction = "",serviceType = "",
            startDate = "", endDate = "", startTime = "", distanceInKm = "", pickupLocation = "",
            dropLocation = "", pickup_city = "", drop_city = "", mobile = "", pickup_time = "",
            drop_time = "", durationInTraffic = "", idToken = "";
    String[] latlong;
    double pickup_latitude = 0.0, pickup_longitude = 0.0, drop_latitude = 0.0, drop_longitude = 0.0,
            convertedDistance = 0.0;

    Polyline mPolyline;

    FindCarItemClickListener listener;
    ApiInterface apiInterface;
    ScheduleMapResp scheduleMapResp;

    List<ScheduleMapResp> finalScheduleList = new ArrayList<>();
    Map<String, Map<String, Map<String, List<ScheduleMapResp>>>> listMap = new HashMap<String, Map<String, Map<String, List<ScheduleMapResp>>>>();
    FindsCarRecyclerAdapter findsCarRecyclerAdapter;
    List<String> listOfVehicleType = new ArrayList<String>();
    Map<String, Map<String, List<ScheduleMapResp>>> outerMap;
    Map<String, List<ScheduleMapResp>> innerMap;

    List<ScheduleMapResp> selectedByVehicleTypeList = new ArrayList<>();
    List<ScheduleMapResp> tempList = new ArrayList<>();
    List<ScheduleMapResp> tempList1 = new ArrayList<>();
    List<ScheduleMapResp> remainingList = new ArrayList<>();

    boolean isEqual = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finds_cars);

        Log.d(TAG, "onCreate");

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this

        );

        imageBack = findViewById(R.id.imageBack);
        imgCallFindCarAct = findViewById(R.id.imgCallFindAct);
        txtTitle= findViewById(R.id.txtTitle);
        txtFromTo = findViewById(R.id.txtFromTo);
        txtStartEndDate = findViewById(R.id.txtStartEndDate);
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEstimatedKm = findViewById(R.id.txtEstimatedKm);
        constraintLayoutPack = findViewById(R.id.constraintLayout1);
        recyclerView = findViewById(R.id.recyclerView);
        viewDetailsText = findViewById(R.id.viewDetailsText);
        findsCarsRecyclerView = findViewById(R.id.findsCarsRecyclerView);

        /* shared pref values */
        direction = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");
        pickupLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_LOCATION, "");
        dropLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_LOCATION, "");
        pickup_city = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_CITY, "");
        drop_city = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_CITY, "");
        startDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_DATE_TO_DISPLAY, "");
        endDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_DATE_TO_DISPLAY, "");
        startTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_TO_DISPLAY, "");
        distanceInKm = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DISTANCE_IN_KM, "");
        pickup_time = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_FOR_API, "");
        drop_time = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_TIME_FOR_API, "");
        idToken = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_TOKEN, "");
        convertedDistance = Double.parseDouble(PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DISTANCE, ""));
        durationInTraffic = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DURATION_IN_TRAFFIC, "");
        idToken = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.FIREBASE_TOKEN, "");

        Log.d(TAG,"pickupLocation = " + pickupLocation
                + " dropLocation = " + dropLocation);
        Log.d(TAG,"fromLocation = " + pickup_city
                + " endLocation = " + drop_city);
        Log.d(TAG,"endDate = " + endDate
                + " convertedDistance = " + convertedDistance);

        /* set data to ui */
        txtFromTo.setText(pickup_city + " To " + drop_city);
        txtStartTime.setText(startTime);
        txtStartEndDate.setText(startDate);
        txtEstimatedKm.setText("Est km " + Math.round(Double.parseDouble(distanceInKm)));

        switch (serviceType)
        {
            case "Outstation":
                if (direction.equals("two-way"))
                {
                    txtTitle.setText(serviceType + "(Round Trip)");
                    txtStartEndDate.setText(startDate + " - " + endDate);
                }
                else
                {
                    txtTitle.setText(serviceType + "(One Way)");
                }
                break;
            case "Airport":
                if (direction.equals("airport-pickup"))
                {
                    txtTitle.setText(serviceType + " Pickup");
                }
                else
                {
                    txtTitle.setText(serviceType + " Drop");
                }
                break;
            case "Rental":
                if (direction.equals("current-booking"))
                {
                    txtTitle.setText(serviceType + " ( Current Booking ) ");
                    constraintLayoutPack.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtTitle.setText(serviceType + " ( Schedule Trip ) ");
                    constraintLayoutPack.setVisibility(View.VISIBLE);
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "No Options", Toast.LENGTH_SHORT).show();
                break;
        }

        /* rental static data */
        hourPackageModelList = new ArrayList<>();

        hourPackageModelList.add(new HourPackage("2km","25 km"));
        hourPackageModelList.add(new HourPackage("4km","40 km"));
        hourPackageModelList.add(new HourPackage("6km","60 km"));
        hourPackageModelList.add(new HourPackage("8km","80 km"));
        hourPackageModelList.add(new HourPackage("10km","100 km"));
        hourPackageModelList.add(new HourPackage("12km","120 km"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        findsCarsRecyclerView.setLayoutManager(linearLayoutManager);

        HourPackageAdapter hourPackageAdapter = new HourPackageAdapter(this,hourPackageModelList);
        findsCarsRecyclerView.setAdapter(hourPackageAdapter);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        imgCallFindCarAct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                mobile = "7057052508";
                Uri call = Uri.parse("tel:" + mobile);
                Intent intent = new Intent(Intent.ACTION_CALL, call);

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(FindCarActivity.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(FindCarActivity.this,
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

        viewDetailsText.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                bottomSheetDialog = new BottomSheetDialog(FindCarActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.view_details_bottom_sheet,null,false);

               // TextView textPara1,textPara2, textPara3, textPara4, textPara5;

                TextView textPara1 = view1.findViewById(R.id.textPara1);
                TextView textPara2 = view1.findViewById(R.id.textPara2);
                TextView textPara3 = view1.findViewById(R.id.textPara3);
                TextView textPara4 = view1.findViewById(R.id.textPara4);
                TextView textPara5 = view1.findViewById(R.id.textPara5);

                textPara1.setText(Html.fromHtml("<p>Rental can be used for local travels only.</p>"+
                                "Package cannot be changed after booking is \n"
                        +"confirmed </p>", Html.FROM_HTML_MODE_COMPACT));

                textPara2.setText(Html.fromHtml("<p>For usage beyond selected package, additional</p>"+
                        " fare will be applicable as per rates above.</p>", Html.FROM_HTML_MODE_COMPACT));

                textPara3.setText(Html.fromHtml("<p>Additional GST applicable on fare. Toll will be added</p>"+
                        "<p>in the final bill if applicable, please pay parking fee</p>"+
                        "when required.</p>", Html.FROM_HTML_MODE_COMPACT));

                textPara4.setText(Html.fromHtml("<p>For Ride Later booking , Advance Booking fee</p>"+
                        "of Rs 100 will be added to Total Fare.</p>", Html.FROM_HTML_MODE_COMPACT));

                textPara5.setText(Html.fromHtml("<p>Base fare amount is the minimum bill amount a</p>"+
                        " Customer has to pay for the package.</p>", Html.FROM_HTML_MODE_COMPACT));

                AppCompatButton doneButton = view1.findViewById(R.id.doneButton);



                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
                bottomSheetDialog.setCancelable(false);
            }
        });

        /* vehicle type recyclerview */
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutWrapper(getApplicationContext(), 1));

        listener = this::onFindCarItemClick;

        if(!isNetworkConnected())
        {
            Toast.makeText(getApplicationContext(), "No internet. Connect to wifi or cellular network.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            getProjectionScheduleMap(pickup_time,drop_time,
                    pickup_city, direction,serviceType,
                    "","",convertedDistance , idToken,true);
        }
    }

    public void getProjectionScheduleMap(String startTime, String endTime, String startLocation,
                                         String direction, String serviceType,
                                         String classType, String vehicleType, Double distance, String idToken, boolean matchExactTime){

        ScheduleDates scheduleDates = new ScheduleDates(startTime, endTime, startLocation,
                direction, serviceType, classType,vehicleType, distance, matchExactTime);

        apiInterface = ApiClient.getClient(AppConstants.BASE_URL).create(ApiInterface.class);
        Call<Map<String, Map<String, Map<String, List<ScheduleMapResp>>>>> listCall = apiInterface.getProjectionScheduleDates(scheduleDates,idToken);

        // Set up progress before call
        Dialog lovelyProgressDialog = new LovelyProgressDialog(FindCarActivity.this)
                .setIcon(R.drawable.ic_cast_connected_white_36dp)
                .setTitle(R.string.connecting_to_server)
                .setMessage(R.string.your_request_is_processing)
                .setTopColorRes(R.color.teal_200)
                .show();

        listCall.enqueue(new Callback<Map<String, Map<String, Map<String, List<ScheduleMapResp>>>>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Map<String, Map<String, Map<String, List<ScheduleMapResp>>>>> call, Response<Map<String, Map<String, Map<String, List<ScheduleMapResp>>>>> response) {
                lovelyProgressDialog.dismiss();
                int statusCode = response.code();
                if(statusCode == 200)
                {
                    listMap = response.body();

                    if(listMap.size() == 0)
                    {
                        ErrorDialog errorDialog = new ErrorDialog(getApplicationContext(), "No Data Found");
                        errorDialog.show();
                        //Toast.makeText(FindCarActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        finalScheduleList = new ArrayList<ScheduleMapResp>();

                        //Map<String, Map<String, Map<String, List<ScheduleMapResp>>>> listMap
                        // = new HashMap<String, Map<String, Map<String, List<ScheduleMapResp>>>>();
                        for(String outerKey : listMap.keySet())
                        {
                            Log.d(TAG,"outerKey = " + outerKey);
                            listOfVehicleType.add(outerKey);

                            outerMap = listMap.get(outerKey);
                            for(String innerKey : outerMap.keySet())
                            {
                                Log.d(TAG,"innerKey = " + innerKey);
                                innerMap = outerMap.get(innerKey);
                                for(String model1 : innerMap.keySet())
                                {
                                    Log.d(TAG,"model1 = " + model1);

                                    List<ScheduleMapResp> tempScheduleList = innerMap.get(model1);
                                    Log.d(TAG,"list size = " + tempScheduleList.size());
                                    Log.d(TAG,"list data = " + tempScheduleList.toString());

                                    for (int i = 0; i < tempScheduleList.size(); i++)
                                    {
                                        scheduleMapResp = tempScheduleList.get(i);
                                        //scheduleMapResp.setTotalAvailable(tempScheduleList.size());
                                        finalScheduleList.add(scheduleMapResp);
                                    }
                                    Log.d(TAG, "tempScheduleList size in inner model = " + tempScheduleList.size());
                                    Log.d(TAG, "finalScheduleList size in inner model = " + finalScheduleList.size());
                                }
                            }
                        }

                        Log.d(TAG, "finalScheduleList data outer model = " + listOfVehicleType.toString());
                        findsCarRecyclerAdapter = new FindsCarRecyclerAdapter(getApplicationContext(), listOfVehicleType, finalScheduleList, listener);
                        recyclerView.setAdapter(findsCarRecyclerAdapter);
                        findsCarRecyclerAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    Gson gson = new GsonBuilder().create();
                    ProjectionScheduleErrorResp mError = new ProjectionScheduleErrorResp();
                    try {
                        mError= gson.fromJson(response.errorBody().string(), ProjectionScheduleErrorResp.class);
                        if(!mError.getError().equals("") || !response.message().equals(""))
                        {
                            ErrorDialog errorDialog = new ErrorDialog(getApplicationContext(), mError.getError());
                            errorDialog.show();
                            //Toast.makeText(getApplicationContext(), mError.getError(), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            ErrorDialog errorDialog = new ErrorDialog(getApplicationContext(), "Something Went Wrong.");
                            errorDialog.show();
                            //Toast.makeText(getApplicationContext(), "Something Went Wrong.", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        // handle failure to read error
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Map<String, Map<String, List<ScheduleMapResp>>>>> call, Throwable t) {
                lovelyProgressDialog.dismiss();
                Log.d(TAG, "error: " + t.toString());
            }
        });
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionRequestConstant.MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay! Do the phone call
                }
                else
                {

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
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Log.d(TAG, "OnMapReady");
        if (mMap != null) {
            mMap.clear();
        }
        mMap = googleMap;

        if(!pickupLocation.equals("0.0") && !dropLocation.equals("0.0"))
        {
            latlong = pickupLocation.split(" ");
            pickup_latitude = Double.parseDouble(latlong[0]);
            pickup_longitude = Double.parseDouble(latlong[1]);

            /*TextIconGenerator tc = new TextIconGenerator(this);
            Bitmap bmp = tc.makeIcon(pickup_city);*/

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pickup_latitude, pickup_longitude))
                    .title(pickup_city)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickup_latitude, pickup_longitude), 6.0f));

            latlong =  dropLocation.split(" ");
            drop_latitude = Double.parseDouble(latlong[0]);
            drop_longitude = Double.parseDouble(latlong[1]);

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(drop_latitude, drop_longitude))
                    .title(drop_city)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(drop_latitude, drop_longitude), 6.0f));

            DrawRoute(new LatLng(pickup_latitude, pickup_longitude), new LatLng(drop_latitude, drop_longitude));
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Not getting location", Toast.LENGTH_SHORT).show();
        }
    }

    private void DrawRoute(LatLng origin, LatLng destination)
    {
        /*Getting URL to the Google Directions API*/
        String directionUrl = getDirectionsUrl(origin, destination);
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
        // Key
        //String key = "key=" + getString(R.string.google_maps_key);
        String key = "key=" + ApiInterface.GOOGLE_MAP_API_KEY;
        Log.d(TAG, "api key = " + key);
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d(TAG, "url = " + url);

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException
    {
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

    /** A class to download data from Google Directions URL */
    @SuppressLint("StaticFieldLeak")
    private class DownloadDirectionTask extends AsyncTask<String, Void, String>
    {
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

    /** A class to parse the Google Directions in JSON format */
    @SuppressLint("StaticFieldLeak")
    private class DirectionParseTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
    {
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
                Toast.makeText(getApplicationContext(), "No route is found", Toast.LENGTH_LONG).show();

            /* zoom */
            List<LatLng> latLngList = new ArrayList<>();
            latLngList.add(new LatLng(pickup_latitude, pickup_longitude));
            latLngList.add(new LatLng(drop_latitude, drop_longitude));
            zoomRoute(mMap, latLngList);
        }
    }

    @Override
    public void onFindCarItemClick(View v, int position, List<String> scheduleMapRespList, String labelName, String seatCapacity)
    {
        /* on click on recyclerview item */
        Log.d(TAG,"label Value = " + labelName);
        Log.d(TAG,"label Value = " + seatCapacity);

        /* get selected vehicle_type and add into another list */
        for (int i = 0; i < finalScheduleList.size();i++)
        {
            // Log.d(TAG,"finalschedulelist i "+i);
            if (finalScheduleList.get(i).getVehicleType().equals(labelName))
            {
                selectedByVehicleTypeList.add(finalScheduleList.get(i));
                //Log.d(TAG, "selectedByVehicleTypeList " + selectedByVehicleTypeList.toString());
            }
        }

        /* sort by model */
        Collections.sort(selectedByVehicleTypeList, orderModel);
        //Log.d("sorted list = ", selectedByVehicleTypeList.toString());

        /*sorting by model and classType same and different */
        sortByModelAndClassType();
        //Log.d(TAG,"selected by vehicle type = " + tempList.toString());

        Gson gson = new Gson();
        Type type = new TypeToken<List<ScheduleMapResp>>() {}.getType();
        String jsonForData = gson.toJson(tempList, type);
        String jsonForDuplicateData = gson.toJson(remainingList, type);

        Intent intent = new Intent(FindCarActivity.this, CarListTypeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra(SharedPref.SELECTED_VEHICLE_TYPE_OBJECT, jsonForData);
        //intent.putExtra(SharedPref.DUPLICATE_VEHICLE_OBJECT, jsonForDuplicateData);
        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.SELECTED_VEHICLE_TYPE_OBJECT, jsonForData);
        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.DUPLICATE_VEHICLE_OBJECT, jsonForDuplicateData);
        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.VEHICLE_TYPE_FOR_DISPLAY, labelName);
        PreferencesUtils.putPreferences(getApplicationContext(), SharedPref.VEHICLE_SEAT_CAPACITY_FOR_DISPLAY, seatCapacity);
        startActivity(intent);
        //finish();

        //Log.d(TAG,"scheduleDatesResp Data to send next Activity tempList = " + tempList.toString());
        //Log.d(TAG,"scheduleDatesResp Data to send next Activity remainingList = " + remainingList.toString());
    }

    public void sortByModelAndClassType()
    {
        int i =0;
        while (i<=selectedByVehicleTypeList.size())
        {
            for(int j = i+1; j < selectedByVehicleTypeList.size(); j++)
            {
                if(selectedByVehicleTypeList.get(i).getClassType().equals(selectedByVehicleTypeList.get(j).getClassType()))
                {
                    if (selectedByVehicleTypeList.get(i).getModel().equals(selectedByVehicleTypeList.get(j).getModel()))
                    {
                        //System.out.println("model compare i = " + mList.get(i).getModel());
                        tempList.add(selectedByVehicleTypeList.get(i));
                        Log.d(TAG, "templist size" +tempList.size());

                        remainingList.add(selectedByVehicleTypeList.get(j));
                        Log.d(TAG,"remainingList = " +remainingList);

                        isEqual = true;
                    }
                }
                else
                {
                    if (selectedByVehicleTypeList.get(i).getModel().equals(selectedByVehicleTypeList.get(j).getModel()))
                    {
                        //System.out.println("class type not same i " + selectedByVehicleTypeList.get(i).getModel());
                        //System.out.println("class type not same  j " + selectedByVehicleTypeList.get(j).getModel());

                        tempList.add(selectedByVehicleTypeList.get(i));
                        tempList1.add(selectedByVehicleTypeList.get(j));

                        tempList1.removeAll(tempList);
                        tempList.addAll(tempList1);
                    }
                }
            }

            if (isEqual)
            {
                i= i+2;
                isEqual = false;
            }else{
                i++;
            }
        }
    }

    public static Comparator<ScheduleMapResp> orderModel = new Comparator<ScheduleMapResp>() {
        @Override
        public int compare(ScheduleMapResp o1, ScheduleMapResp o2)
        {
            return o1.getVehicle().getModel().compareTo(o2.getVehicle().getModel());
        }
    };

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

        int routePadding = 200;
        LatLngBounds latLngBounds = boundsBuilder.build();
        int left, right,bottom, top;
        //googleMap.setPadding(left = 10, top = 30, right = 10, bottom = 10);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}