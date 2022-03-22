package com.kiwni.app.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiwni.app.user.R;
import com.kiwni.app.user.adapter.BookingAdapter;
import com.kiwni.app.user.adapter.NestedAdapter;
import com.kiwni.app.user.adapter.TitleItemAdapter;
import com.kiwni.app.user.datamodels.BookingModel2;

import java.util.ArrayList;
import java.util.List;

public class BookingDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView1 ,recyclerView2;
    List<BookingModel2> bookingModelList;
    List<BookingModel2> bookingModelList1;
    ConstraintLayout constraintLayout;

    NestedAdapter nestedAdapter;
    TitleItemAdapter titleItemAdapter;

    ImageView imageBack;
    AppCompatButton proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        recyclerView1 =findViewById(R.id.recyclerView1);
        recyclerView2=findViewById(R.id.recyclerView2);

        bookingModelList = new ArrayList<>();
        bookingModelList1 = new ArrayList<>();

        bookingModelList.add(new BookingModel2(R.drawable.gps,"GPS Tracking"));
        bookingModelList.add(new BookingModel2(R.drawable.otp,"OTP Pickup & Drops"));
        bookingModelList.add(new BookingModel2(R.drawable.tube_less_tier,"Tube Less Tier"));
        bookingModelList.add(new BookingModel2(R.drawable.sos_button,"SOS Button"));
        bookingModelList.add(new BookingModel2(R.drawable.group_6313,"Vaccination Driver"));
        bookingModelList.add(new BookingModel2(R.drawable.first_aid_box,"First Aid Box"));
        bookingModelList.add(new BookingModel2(R.drawable.break_down,"Break Down"));
        bookingModelList.add(new BookingModel2(R.drawable.group_6320,"Fire Extinguisher"));
        bookingModelList.add(new BookingModel2(R.drawable.experience_driver,"Experience driver"));
        bookingModelList.add(new BookingModel2(R.drawable.sharing_location,"Sharing Location"));
        bookingModelList.add(new BookingModel2(R.drawable.gps_speed_limit,"GPS Speed Limit"));

        bookingModelList1.add(new BookingModel2(R.drawable.umbrella,"Umbrella"));
        bookingModelList1.add(new BookingModel2(R.drawable.wifi,"WIFI"));
        bookingModelList1.add(new BookingModel2(R.drawable.news_paper,"News Paper"));
        bookingModelList1.add(new BookingModel2(R.drawable.water_bottel,"Water Bottle"));
        bookingModelList1.add(new BookingModel2(R.drawable.sanitizer,"Sanitizer"));
        bookingModelList1.add(new BookingModel2(R.drawable.chocolate,"Chocolate"));
        bookingModelList1.add(new BookingModel2(R.drawable.shoes_cleaner,"Shoes Cleaner"));
        bookingModelList1.add(new BookingModel2(R.drawable.mask_group_26,"Eyes Mask"));
        bookingModelList1.add(new BookingModel2(R.drawable.notebook_pencil,"Notebook Pencil"));
        bookingModelList1.add(new BookingModel2(R.drawable.laptop_charger,"Laptop Charger"));
        bookingModelList1.add(new BookingModel2(R.drawable.mobile_charger,"Mobile Charger"));

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getApplicationContext(),3,GridLayoutManager.HORIZONTAL,false);
        recyclerView1.setLayoutManager(gridLayoutManager1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getApplicationContext(),3,GridLayoutManager.HORIZONTAL,false);
        recyclerView2.setLayoutManager(gridLayoutManager2);

        BookingAdapter bookingAdapter = new BookingAdapter(getApplicationContext(),bookingModelList);
        recyclerView1.setAdapter(bookingAdapter);

        BookingAdapter bookingAdapter1 = new BookingAdapter(getApplicationContext(),bookingModelList1);
        recyclerView2.setAdapter(bookingAdapter1);

        imageBack =findViewById(R.id.imageBack);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(BookingDetailsActivity.this, CarListTypeActivity.class));
                finish();
            }
        });
        proceedButton =findViewById(R.id.proceedButton);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // navController.navigate(R.id.action_bookingDetailsFragment_to_bookingDetailsSecondFragment);
                Intent intent = new Intent(BookingDetailsActivity.this, ConfirmBookingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //startActivity(new Intent(BookingDetailsActivity.this, CarListTypeActivity.class));
        finish();

    }
}