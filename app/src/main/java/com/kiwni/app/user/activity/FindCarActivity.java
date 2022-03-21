package com.kiwni.app.user.activity;

import android.content.Intent;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.FindsCarRecyclerAdapter;
import com.kiwni.app.user.adapter.HourPackageAdapter;
import com.kiwni.app.user.interfaces.FindCarItemClickListener;
import com.kiwni.app.user.models.FindCar;
import com.kiwni.app.user.models.HourPackage;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class FindCarActivity extends AppCompatActivity implements OnMapReadyCallback
{
    GoogleMap mMap1;
    RecyclerView recyclerView,findsCarsRecyclerView;
    List<HourPackage> hourPackageModelList;
    FindsCarRecyclerAdapter findsCarRecyclerAdapter;
    FindCarItemClickListener findCarItemClickListener;

    String names[]={"sedan"};
    ImageView imageBack;
    TextView txtTitle,viewDetailsText, txtFromTo, txtStartEndDate, txtStartTime, txtEstimatedKm;
    BottomSheetDialog bottomSheetDialog;
    List<FindCar> findCarModelList;

    ConstraintLayout constraintLayoutPack;
    String direction = "",serviceType = "", fromLocation = "", endLocation = "",
            startDate = "", endDate = "", startTime = "", distanceInKm = "";

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

        direction = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");

        fromLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_CITY, "");
        endLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_CITY, "");

        startDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_DATE_TO_DISPLAY, "");
        endDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_DATE_TO_DISPLAY, "");
        startTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_TO_DISPLAY, "");
        distanceInKm = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DISTANCE_IN_KM, "");

        Log.d("TAG","fromLocation = " + fromLocation
                + " endLocation = " + endLocation);

        txtFromTo.setText(fromLocation + " To " + endLocation);
        txtStartTime.setText(startTime);
        txtStartEndDate.setText(startDate);
        txtEstimatedKm.setText("Est km " + distanceInKm);

        Log.d("TAG","endDate = " + endDate
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

        constraintLayoutPack = findViewById(R.id.constraintLayout1);
        recyclerView = findViewById(R.id.recyclerView);
        viewDetailsText = findViewById(R.id.viewDetailsText);

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


        findsCarsRecyclerView = findViewById(R.id.findsCarsRecyclerView);
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
                Intent intent = new Intent(FindCarActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        viewDetailsText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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
        if (mMap1 != null) {
            mMap1.clear();
        }
        mMap1 = googleMap;
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

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}