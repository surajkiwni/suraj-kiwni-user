package com.kiwni.app.user.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.FindsCarRecyclerAdapter;
import com.kiwni.app.user.adapter.HourPackageAdapter;
import com.kiwni.app.user.datamodels.FindCarModel;
import com.kiwni.app.user.datamodels.HourPackageModel;
import com.kiwni.app.user.interfaces.FindCarItemClickListener;
import com.kiwni.app.user.pref.PreferencesUtils;
import com.kiwni.app.user.pref.SharedPref;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class FindCarActivity extends AppCompatActivity implements OnMapReadyCallback{


    GoogleMap mMap1;
    RecyclerView recyclerView,findsCarsRecyclerView;
    List<HourPackageModel> hourPackageModelList;
    FindsCarRecyclerAdapter findsCarRecyclerAdapter;
    FindCarItemClickListener findCarItemClickListener;

    String names[]={"sedan"};
    ImageView imageBack;
    TextView toolbarText,viewDetailsText;
    BottomSheetDialog bottomSheetDialog;
    List<FindCarModel> findCarModelList;

   // boolean isLast = false;

    ConstraintLayout constraintLayoutPack;
    String direction = "",serviceType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finds_cars);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this

        );

        direction = PreferencesUtils.getPreferences(getApplicationContext(),SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");

        Log.d("find car Activity",serviceType);

        findCarItemClickListener = new FindCarItemClickListener() {
            @Override
            public void onFindCarItemClick(View v, int position, List<FindCarModel> findCarModels) {

               /* direction = toolbarText.getText().toString();
                PreferencesUtils.putPreferences(getApplicationContext(),SharedPref.DIRECTION,direction);*/

                Intent intent = new Intent(FindCarActivity.this,CarListTypeActivity.class);
                startActivity(intent);
            }
        };


        constraintLayoutPack = findViewById(R.id.constraintLayout1);
        recyclerView = findViewById(R.id.recyclerView);
        viewDetailsText = findViewById(R.id.viewDetailsText);

        findCarModelList = new ArrayList<>();

        findCarModelList.add(new FindCarModel(R.drawable.sedan,"Sedan"));
        findCarModelList.add(new FindCarModel(R.drawable.suv,"SUV"));
        findCarModelList.add(new FindCarModel(R.drawable.tempo_traveller,"Tempo Traveller"));

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager1);

       FindsCarRecyclerAdapter findsCarRecyclerAdapter = new FindsCarRecyclerAdapter(getApplicationContext(),findCarModelList,
               findCarItemClickListener);
       recyclerView.setAdapter(findsCarRecyclerAdapter);



        findsCarsRecyclerView = findViewById(R.id.findsCarsRecyclerView);
        hourPackageModelList = new ArrayList<>();

        hourPackageModelList.add(new HourPackageModel("2km","25 km"));
        hourPackageModelList.add(new HourPackageModel("4km","40 km"));
        hourPackageModelList.add(new HourPackageModel("6km","60 km"));
        hourPackageModelList.add(new HourPackageModel("8km","80 km"));
        hourPackageModelList.add(new HourPackageModel("10km","100 km"));
        hourPackageModelList.add(new HourPackageModel("12km","120 km"));

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

        toolbarText= findViewById(R.id.toolbarText);


        if(serviceType.equals("Outstation"))
        {
            if(direction.equals("two-way"))
            {
                toolbarText.setText(serviceType + "(Round Trip)");
            }
            else
            {
                toolbarText.setText(serviceType + "(One Way)");
            }

        }
        else if(serviceType.equals("Airport"))
        {
            if(direction.equals("airport-pickup"))
            {
                toolbarText.setText(serviceType + " Pickup");
            }
            else
            {
                toolbarText.setText(serviceType + " Drop");
            }
        }
        /*else
        {
            Toast.makeText(getApplicationContext(), "No Options", Toast.LENGTH_SHORT).show();
        }*/
        else if(serviceType.equals("Rental"))
        {
            if(direction.equals("current-booking"))
            {
                toolbarText.setText(serviceType + "(Current Booking)");
                constraintLayoutPack.setVisibility(View.VISIBLE);
            }
            else
            {
                toolbarText.setText(serviceType + "(Schedule Trip)");
                constraintLayoutPack.setVisibility(View.VISIBLE);
            }

        }





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

        finish();
    }
}