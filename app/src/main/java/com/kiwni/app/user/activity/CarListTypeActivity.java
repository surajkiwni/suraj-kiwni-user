package com.kiwni.app.user.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.FilterAdapter;
import com.kiwni.app.user.adapter.TitleItemAdapter;
import com.kiwni.app.user.datamodels.BookingModel;
import com.kiwni.app.user.datamodels.DataModels;
import com.kiwni.app.user.interfaces.BookingListItemClickListener;
import com.kiwni.app.user.interfaces.FilterItemClickListener;
import com.kiwni.app.user.models.Filter;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarListTypeActivity extends AppCompatActivity implements BookingListItemClickListener {

    private RecyclerView recyclerView;
    private List<DataModels> mList;
    private TitleItemAdapter adapter;
    String direction = "", serviceType = "", fromLocation = "", endLocation = "",
            startDate = "", endDate = "", startTime = "", distanceInKm = "",mobile = "";

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1001;
    private BookingListItemClickListener bookingListItemClickListener;
    View view;
    ImageView imageBack,imgCallCarLTAct;
    TextView txtTitle, txtFromTo, txtStartEndDate, txtEstimatedKm, txtStartTime;

    ConstraintLayout sortLayout, mapLayout, filterLayout, constraintLayoutForRentalPackage;
    String strBrand = "",strSegment = "",strYear = "",strSpecialReq = "";

    boolean isSelected = false;
    private static int NEXT_ACTIVITY_REQUEST = 1005;
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list_type);

        constraintLayoutForRentalPackage = findViewById(R.id.constraintLayoutForRentalPackage);
        txtTitle = findViewById(R.id.txtTitle);
        txtFromTo = findViewById(R.id.txtFromTo);
        recyclerView = findViewById(R.id.main_recyclerview);
        imageBack = findViewById(R.id.imageBack);
        imgCallCarLTAct = findViewById(R.id.imgCallCarLTAct);
        mapLayout = findViewById(R.id.mapLayout);
        sortLayout = findViewById(R.id.sortLayout);
        filterLayout = findViewById(R.id.filterLayout);
        txtStartEndDate = findViewById(R.id.txtStartEndDate);
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEstimatedKm = findViewById(R.id.txtEstimatedKm);

        bookingListItemClickListener = new BookingListItemClickListener() {
            @Override
            public void onItemClick(View v, int position, List<BookingModel> bookingModels) {
                /*Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();*/
            }
        };

        direction = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");
        Log.d("TAG","data from previous screen - " + direction + " , " + serviceType);

        fromLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_CITY, "");
        endLocation = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_CITY, "");

        startDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_DATE_TO_DISPLAY, "");
        endDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_DATE_TO_DISPLAY, "");
        startTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_TO_DISPLAY, "");
        distanceInKm = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DISTANCE_IN_KM, "");

        txtTitle.setText(serviceType + " ( " + direction + " ) ");
        txtFromTo.setText(fromLocation + " To " + endLocation);
        txtStartTime.setText(startTime);
        txtStartEndDate.setText(startDate);
        txtEstimatedKm.setText("Est km " + distanceInKm);

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
                    constraintLayoutForRentalPackage.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtTitle.setText(serviceType + " ( Schedule Trip ) ");
                    constraintLayoutForRentalPackage.setVisibility(View.VISIBLE);
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "No Options", Toast.LENGTH_SHORT).show();
                break;
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CarListTypeActivity.this));

        mList = new ArrayList<>();

        //list1
        List<BookingModel> nestedList1 = new ArrayList<>();
        nestedList1.add(new BookingModel("2014"));
        nestedList1.add(new BookingModel("2015"));
        nestedList1.add(new BookingModel("2017"));
        nestedList1.add(new BookingModel("2018"));

        List<BookingModel> nestedList2 = new ArrayList<>();
        nestedList2.add(new BookingModel("2014"));
        nestedList2.add(new BookingModel("2015"));
        nestedList2.add(new BookingModel("2017"));
        nestedList2.add(new BookingModel("2018"));
        nestedList2.add(new BookingModel("2019"));
        nestedList2.add(new BookingModel("2020"));

        List<BookingModel> nestedList3 = new ArrayList<>();
        nestedList3.add(new BookingModel("2014"));
        nestedList3.add(new BookingModel("2015"));
        nestedList3.add(new BookingModel("2017"));
        nestedList3.add(new BookingModel("2019"));
        nestedList3.add(new BookingModel("2020"));

        List<BookingModel> nestedList4 = new ArrayList<>();
        nestedList4.add(new BookingModel("2015"));
        nestedList4.add(new BookingModel("2016"));
        nestedList4.add(new BookingModel("2018"));

        mList.add(new DataModels(nestedList1, "Sedan", bookingListItemClickListener));
        mList.add(new DataModels(nestedList2, "Mahindra Verito", bookingListItemClickListener));
        mList.add(new DataModels(nestedList3, "Swift Dezire", bookingListItemClickListener));
        mList.add(new DataModels(nestedList4, "Maruti Suzuki Ciaz", bookingListItemClickListener));

        adapter = new TitleItemAdapter(mList, this);
        recyclerView.setAdapter(adapter);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(CarListTypeActivity.this, FindCarActivity.class));
                finish();

            }
        });
        imgCallCarLTAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = "7057052508";
                Uri call = Uri.parse("tel:" + mobile);
                Intent intent = new Intent(Intent.ACTION_CALL, call);

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(CarListTypeActivity.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(CarListTypeActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

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

        mapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarListTypeActivity.this, MapActivity.class);
                startActivity(intent);
                // finish();
            }
        });

        sortLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(CarListTypeActivity.this, android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.sort_layout);

                dialog.show();

                AppCompatButton btnLatest = dialog.findViewById(R.id.btnLatest);
                AppCompatButton btnPopularity = dialog.findViewById(R.id.btnPopularity);
                AppCompatButton btnLowToHigh = dialog.findViewById(R.id.btnLowToHigh);
                AppCompatButton btnHighToLow = dialog.findViewById(R.id.btnHighToLow);


                ImageView imageBack = dialog.findViewById(R.id.imageBack);

                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnLatest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(), "Clicked On : " + btnLatest.getText().toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                btnPopularity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(), "Clicked On : " + btnPopularity.getText().toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                btnLowToHigh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(), "Clicked On : " + btnLowToHigh.getText().toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                btnHighToLow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(), "Clicked On : " + btnHighToLow.getText().toString(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(CarListTypeActivity.this, android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_screen);

                RecyclerView filterRecyclerView;
                List<Filter> filterModelList;
                FilterItemClickListener filterItemClickListener;
                TextView spinnerText,premiumText,luxuryText,ultraLuxuryText;
                Spinner spinner1;
                CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,checkBox6;

                dialog.show();
                int[] back_background;

                back_background = new int[]{R.drawable.spinner_background, R.drawable.white_background};

                premiumText = dialog.findViewById(R.id.premiumText);
                luxuryText = dialog.findViewById(R.id.luxuryText);
                ultraLuxuryText = dialog.findViewById(R.id.ultraLuxuryText);

                checkBox1 = (CheckBox) dialog.findViewById(R.id.checkBox1);
                checkBox2 = (CheckBox) dialog.findViewById(R.id.checkBox2);
                checkBox3 = (CheckBox) dialog.findViewById(R.id.checkBox3);
                checkBox4 = (CheckBox) dialog.findViewById(R.id.checkBox4);
                checkBox5 = (CheckBox) dialog.findViewById(R.id.checkBox5);
                checkBox6 = (CheckBox) dialog.findViewById(R.id.checkBox6);

                //OnCheckBoxClicked(dialog,checkBox1);
                checkBox1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(checkBox1.isChecked())
                        {
                            Toast.makeText(getApplicationContext(), "checked = " +checkBox1.getText().toString(), Toast.LENGTH_SHORT).show();
                            strSpecialReq = checkBox1.getText().toString();
                        }
                        else{
                            strSpecialReq = "";
                        }
                    }
                });

                checkBox2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(checkBox2.isChecked())
                        {
                            Toast.makeText(getApplicationContext(), "checked = " +checkBox2.getText().toString(), Toast.LENGTH_SHORT).show();
                            strSpecialReq = checkBox2.getText().toString();
                        }
                        else {
                            strSpecialReq = "";
                        }
                    }
                });

                checkBox3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(checkBox3.isChecked())
                        {
                            Toast.makeText(getApplicationContext(), "checked = " +checkBox3.getText().toString(), Toast.LENGTH_SHORT).show();
                            strSpecialReq = checkBox3.getText().toString();
                        }
                        else{
                            strSpecialReq = "";
                        }
                    }
                });

                checkBox4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(checkBox4.isChecked())
                        {
                            Toast.makeText(getApplicationContext(), "checked = " +checkBox4.getText().toString(), Toast.LENGTH_SHORT).show();
                            strSpecialReq = checkBox4.getText().toString();
                        }
                        else{
                            strSpecialReq = "";
                        }
                    }
                });

                checkBox5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(checkBox5.isChecked())
                        {
                            Toast.makeText(getApplicationContext(), "checked = " +checkBox5.getText().toString(), Toast.LENGTH_SHORT).show();
                            strSpecialReq = checkBox5.getText().toString();
                        }
                        else {
                            strSpecialReq = "";
                        }
                    }
                });

                checkBox6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(checkBox6.isChecked())
                        {
                            Toast.makeText(getApplicationContext(), "checked = " +checkBox6.getText().toString(), Toast.LENGTH_SHORT).show();
                            strSpecialReq = checkBox6.getText().toString();
                        }
                        else{
                            strSpecialReq = "";
                        }
                    }
                });

                premiumText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       strSegment = " ";
                        if (strSegment.equals(" "))
                        {

                            strSegment = premiumText.getText().toString();
                            isSelected = true;
                        }
                        else {
                            strSegment = "";
                            isSelected = false;

                        }

                        if(isSelected){
                            //premiumText.setBackgroundColor(Color.WHITE);
                            premiumText.setBackgroundResource(R.drawable.spinner_background);

                        }
                        else {

                           // premiumText.setBackgroundResource(R.drawable.spinner_background);
                            premiumText.setBackgroundColor(Color.WHITE);

                        }

                        // fetching length of array
                        int array_length =back_background.length;

                        // object creation of random class
                        Random random = new Random();

                        // generation of random number
                        int random_number = random.nextInt(array_length);

                        // set background images on screenView
                        // using setBackground() method.
                        premiumText.setBackground(ContextCompat.getDrawable(getApplicationContext(), back_background[random_number]));

                        Toast.makeText(getApplicationContext(), "Clicked On " + strSegment, Toast.LENGTH_SHORT).show();
                    }

                });

                luxuryText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // fetching length of array
                        int array_length =back_background.length;

                        // object creation of random class
                        Random random = new Random();

                        // generation of random number
                        int random_number = random.nextInt(array_length);

                        // set background images on screenView
                        // using setBackground() method.
                        luxuryText.setBackground(ContextCompat.getDrawable(getApplicationContext(), back_background[random_number]));


                        strSegment = luxuryText.getText().toString();
                        //luxuryText.setBackgroundResource(R.drawable.spinner_background);
                        Toast.makeText(getApplicationContext(), "Clicked On " + strSegment, Toast.LENGTH_SHORT).show();
                    }
                });

                ultraLuxuryText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // fetching length of array
                        int array_length =back_background.length;

                        // object creation of random class
                        Random random = new Random();

                        // generation of random number
                        int random_number = random.nextInt(array_length);

                        // set background images on screenView
                        // using setBackground() method.
                        ultraLuxuryText.setBackground(ContextCompat.getDrawable(getApplicationContext(), back_background[random_number]));


                        strSegment = ultraLuxuryText.getText().toString();
                        //ultraLuxuryText.setBackgroundResource(R.drawable.spinner_background);
                        Toast.makeText(getApplicationContext(), "Clicked On" + strSegment, Toast.LENGTH_SHORT).show();
                    }
                });

                filterRecyclerView = dialog.findViewById(R.id.filterRecyclerView);

                filterItemClickListener = new FilterItemClickListener() {
                    @Override
                    public void onFilterItemClick(View v, int position, List<Filter> filterModels) {
                        strBrand = filterModels.get(position).getName();
                        Toast.makeText(getApplicationContext(), "Item Clicked - " + strBrand, Toast.LENGTH_SHORT).show();
                    }
                };


                Spinner spinner = dialog.findViewById(R.id.spinner);
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(dialog.getContext(),
                        R.array.select_year, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        strYear = spinner.getSelectedItem().toString();
                        if(!strYear.equals("Select Year"))
                        {
                            Toast.makeText(getApplicationContext(), "Clicked On " + strYear, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                filterModelList = new ArrayList<>();

                filterModelList.add(new Filter(R.drawable.volkswagenlogo, "Volkswagen"));
                filterModelList.add(new Filter(R.drawable.hondalogo, "Honda"));
                filterModelList.add(new Filter(R.drawable.marutisuzukilogo, "Maruti Suzuki"));
                filterModelList.add(new Filter(R.drawable.mercedesbenzlogo, "Mercedes Benz"));
                filterModelList.add(new Filter(R.drawable.toyotalogo, "Toyota"));
                filterModelList.add(new Filter(R.drawable.audilogo, "Audi"));

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dialog.getContext());
                linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
                filterRecyclerView.setLayoutManager(linearLayoutManager);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(dialog.getContext(), 2, GridLayoutManager.HORIZONTAL, false);
                filterRecyclerView.setLayoutManager(gridLayoutManager);

                FilterAdapter filterAdapter = new FilterAdapter(dialog.getContext(), filterModelList, filterItemClickListener);
                filterRecyclerView.setAdapter(filterAdapter);

                ImageView imageBack = dialog.findViewById(R.id.imageBack);
                imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
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
    public void onItemClick(View v, int position, List<BookingModel> bookingModels)
    {
        Intent intent = new Intent(CarListTypeActivity.this, BookingDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CarListTypeActivity.this, FindCarActivity.class));
        finish();
    }
}

