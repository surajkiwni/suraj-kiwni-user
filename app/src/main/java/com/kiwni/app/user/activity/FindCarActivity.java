package com.kiwni.app.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.FindsCarRecyclerAdapter;
import com.kiwni.app.user.adapter.HourPackageAdapter;
import com.kiwni.app.user.interfaces.FindCarItemClickListener;
import com.kiwni.app.user.models.DirectionsJSONParser;
import com.kiwni.app.user.models.FindCar;
import com.kiwni.app.user.models.HourPackage;
import com.kiwni.app.user.network.ApiInterface;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindCarActivity extends AppCompatActivity implements OnMapReadyCallback
{
    GoogleMap mMap;
    RecyclerView recyclerView,findsCarsRecyclerView;
    List<HourPackage> hourPackageModelList;
    FindCarItemClickListener findCarItemClickListener;
    String TAG = this.getClass().getSimpleName();

    String names[]={"sedan"};
    ImageView imageBack;
    TextView txtTitle,viewDetailsText, txtFromTo, txtStartEndDate, txtStartTime, txtEstimatedKm;
    BottomSheetDialog bottomSheetDialog;
    List<FindCar> findCarModelList;

    ConstraintLayout constraintLayoutPack;
    String direction = "",serviceType = "", fromLocation = "", endLocation = "",
            startDate = "", endDate = "", startTime = "", distanceInKm = "";
    String pickupLocation = "", dropLocation = "", pickup_city = "", drop_city = "";
    String[] latlong;
    double pickup_latitude = 0.0, pickup_longitude = 0.0, drop_latitude = 0.0, drop_longitude = 0.0;
    Polyline mPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finds_cars);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this

        );

        txtTitle= findViewById(R.id.txtTitle);
        txtFromTo = findViewById(R.id.txtFromTo);
        txtStartEndDate = findViewById(R.id.txtStartEndDate);
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEstimatedKm = findViewById(R.id.txtEstimatedKm);
        constraintLayoutPack = findViewById(R.id.constraintLayout1);
        recyclerView = findViewById(R.id.recyclerView);
        viewDetailsText = findViewById(R.id.viewDetailsText);
        findsCarsRecyclerView = findViewById(R.id.findsCarsRecyclerView);

        direction = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");

        fromLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_CITY, "");
        endLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_CITY, "");

        pickupLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_LOCATION, "");
        dropLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_LOCATION, "");
        pickup_city = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_CITY, "");
        drop_city = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_CITY, "");

        Log.d(TAG,"pickupLocation = " + pickupLocation
                + " dropLocation = " + dropLocation);

        startDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_DATE_TO_DISPLAY, "");
        endDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_DATE_TO_DISPLAY, "");
        startTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_TO_DISPLAY, "");
        distanceInKm = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DISTANCE_IN_KM, "");

        Log.d(TAG,"fromLocation = " + fromLocation
                + " endLocation = " + endLocation);

        txtFromTo.setText(fromLocation + " To " + endLocation);
        txtStartTime.setText(startTime);
        txtStartEndDate.setText(startDate);
        txtEstimatedKm.setText("Est km " + distanceInKm);

        Log.d(TAG,"endDate = " + endDate
                + " direction = " + direction);

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

        findCarItemClickListener = new FindCarItemClickListener() {
            @Override
            public void onFindCarItemClick(View v, int position, List<FindCar> findCarModels) {
                Intent intent = new Intent(FindCarActivity.this,CarListTypeActivity.class);
                startActivity(intent);
            }
        };


        findCarModelList = new ArrayList<>();

        findCarModelList.add(new FindCar(R.drawable.sedan,"Sedan"));
        findCarModelList.add(new FindCar(R.drawable.suv,"SUV"));
        findCarModelList.add(new FindCar(R.drawable.tempo_traveller,"Tempo Traveller"));

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager1);

        FindsCarRecyclerAdapter findsCarRecyclerAdapter = new FindsCarRecyclerAdapter(getApplicationContext(),findCarModelList,
               findCarItemClickListener);
        recyclerView.setAdapter(findsCarRecyclerAdapter);

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

        imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Log.d("TAG", "OnMapReady");
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
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickup_latitude, pickup_longitude), 7.0f));

            latlong =  dropLocation.split(" ");
            drop_latitude = Double.parseDouble(latlong[0]);
            drop_longitude = Double.parseDouble(latlong[1]);

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(drop_latitude, drop_longitude))
                    .title(drop_city)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(drop_latitude, drop_longitude), 7.0f));

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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}