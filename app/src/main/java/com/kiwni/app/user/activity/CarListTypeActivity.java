package com.kiwni.app.user.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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

import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.FilterAdapter;
import com.kiwni.app.user.adapter.HourPackageAdapter;
import com.kiwni.app.user.adapter.TitleItemAdapter;
import com.kiwni.app.user.datamodels.BookingModel;
import com.kiwni.app.user.datamodels.DataModels;
import com.kiwni.app.user.datamodels.FilterModel;
import com.kiwni.app.user.datamodels.HourPackageModel;
import com.kiwni.app.user.interfaces.BookingListItemClickListener;
import com.kiwni.app.user.interfaces.FilterItemClickListener;
import com.kiwni.app.user.pref.PreferencesUtils;
import com.kiwni.app.user.pref.SharedPref;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarListTypeActivity extends AppCompatActivity implements BookingListItemClickListener {

    private RecyclerView recyclerView,findsCarsListsRecyclerView;
    private List<DataModels> mList;
    private TitleItemAdapter adapter;
    ConstraintLayout constraintLayoutPack ;
    List<HourPackageModel> hourPackageModelList;
    BottomSheetDialog bottomSheetDialog;
    String direction = "",serviceType = "";


    private BookingListItemClickListener bookingListItemClickListener;
    View view;
    ImageView imageBack;
    TextView filterText, sortText, mapText,viewDetailsText,toolbarText2;

    ConstraintLayout constraintLayout;
    String strBrand = "",strSegment = "",strYear = "",strSpecialReq = "";

    boolean isSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list_type);

        viewDetailsText = findViewById(R.id.viewDetailsText);
        constraintLayoutPack = findViewById(R.id.constraintLayout1);
        recyclerView = findViewById(R.id.main_recyclerview);
        findsCarsListsRecyclerView = findViewById(R.id.findsCarsListsRecyclerView);
        filterText = findViewById(R.id.filterText);
        sortText = findViewById(R.id.sortText);
        imageBack = findViewById(R.id.imageBack);
        mapText = findViewById(R.id.mapText);
        toolbarText2 = findViewById(R.id.toolbarText2);


        bookingListItemClickListener = new BookingListItemClickListener() {
            @Override
            public void onItemClick(View v, int position, List<BookingModel> bookingModels) {
                /*Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();*/
            }
        };


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

        hourPackageModelList = new ArrayList<>();

        hourPackageModelList.add(new HourPackageModel("2km","25 km"));
        hourPackageModelList.add(new HourPackageModel("4km","40 km"));
        hourPackageModelList.add(new HourPackageModel("6km","60 km"));
        hourPackageModelList.add(new HourPackageModel("8km","80 km"));
        hourPackageModelList.add(new HourPackageModel("10km","100 km"));
        hourPackageModelList.add(new HourPackageModel("12km","120 km"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        findsCarsListsRecyclerView.setLayoutManager(linearLayoutManager);

        HourPackageAdapter hourPackageAdapter = new HourPackageAdapter(this,hourPackageModelList);
        findsCarsListsRecyclerView.setAdapter(hourPackageAdapter);


        direction = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");


        if(serviceType.equals("Outstation"))
        {
            if(direction.equals("two-way"))
            {
                toolbarText2.setText(serviceType + " (Round Trip)");
            }
            else
            {
                toolbarText2.setText(serviceType + " (One Way)");
            }

        }
        else if(serviceType.equals("Airport"))
        {
            if(direction.equals("airport-pickup"))
            {
                toolbarText2.setText(serviceType + " Pickup");
            }
            else
            {
                toolbarText2.setText(serviceType + " Drop");
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
                toolbarText2.setText(serviceType + "(Current Booking)");
                constraintLayoutPack.setVisibility(View.VISIBLE);
            }
            else
            {
                toolbarText2.setText(serviceType + "(Schedule Trip)");
                constraintLayoutPack.setVisibility(View.VISIBLE);
            }

        }







        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(CarListTypeActivity.this, FindCarActivity.class);
                /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
                startActivity(intent);
                /*intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                finish();*/

            }
        });




        mapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CarListTypeActivity.this, MapActivity.class);
                startActivity(intent);
                // finish();
            }
        });


        sortText.setOnClickListener(new View.OnClickListener() {
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




        filterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Dialog dialog = new Dialog(CarListTypeActivity.this, android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_screen);

                RecyclerView filterRecyclerView;
                List<FilterModel> filterModelList;
                FilterItemClickListener filterItemClickListener;
                TextView spinnerText,premiumText,luxuryText,ultraLuxuryText;
                Spinner spinner1;
                CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5,checkBox6;


                dialog.show();
                int[] back_background;

                back_background = new int[]{R.drawable.spinner_background,R.drawable.white_background};



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

                        premiumText.requestFocus();

                      /* strSegment = " ";
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

                        }*/

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
                    public void onFilterItemClick(View v, int position, List<FilterModel> filterModels) {
                        Toast.makeText(getApplicationContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
                        strBrand = filterModels.get(position).getName();
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
                        Toast.makeText(getApplicationContext(), "Clicked On"+strYear, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                filterModelList = new ArrayList<>();

                filterModelList.add(new FilterModel(R.drawable.volkswagenlogo, "Volkswagen"));
                filterModelList.add(new FilterModel(R.drawable.hondalogo, "Honda"));
                filterModelList.add(new FilterModel(R.drawable.marutisuzukilogo, "Maruti Suzuki"));
                filterModelList.add(new FilterModel(R.drawable.mercedesbenzlogo, "Mercedes Benz"));
                filterModelList.add(new FilterModel(R.drawable.toyotalogo, "Toyota"));
                filterModelList.add(new FilterModel(R.drawable.audilogo, "Audi"));


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

        viewDetailsText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(CarListTypeActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.view_details_bottom_sheet,null,false);
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
        //startActivity(new Intent(CarListTypeActivity.this, FindCarActivity.class));
        finish();
    }

    @Override
    public void onItemClick(View v, int position, List<BookingModel> bookingModels) {

        // Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(CarListTypeActivity.this, BookingDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}

