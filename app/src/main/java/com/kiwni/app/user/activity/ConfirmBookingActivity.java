package com.kiwni.app.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;
import com.kiwni.app.user.sharedpref.SharedPref;
import com.kiwni.app.user.utils.PreferencesUtils;

public class ConfirmBookingActivity extends AppCompatActivity {

    AppCompatRadioButton radioBusiness, radioPersonal;
    AppCompatButton confirmButton, doneButton;
    String TAG = this.getClass().getSimpleName();
    ConstraintLayout constraintBusinessInput;
    ImageView imageBack;
    TextView txtTitle, txtStartTime, txtStartEndDate, txtEstimatedKm, txtTitleType;

    String direction = "", startDate = "", endDate = "", startTime = "", serviceType = "", distanceInKm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        radioBusiness = findViewById(R.id.radioBusiness);
        radioPersonal = findViewById(R.id.radioPersonal);
        confirmButton = findViewById(R.id.confirmButton);
        constraintBusinessInput = findViewById(R.id.constraintBusinessInput);
        imageBack = findViewById(R.id.imageBack);

        txtTitle = findViewById(R.id.txtTitle);
        txtStartEndDate = findViewById(R.id.txtStartEndDate);
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEstimatedKm = findViewById(R.id.txtEstimatedKm);
        txtTitleType = findViewById(R.id.txtTitleType);

        //pref data
        direction = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DIRECTION,"");
        serviceType = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.SERVICE_TYPE,"");
        Log.d("TAG","data from previous screen - " + direction + " , " + serviceType);

        startDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_DATE_TO_DISPLAY, "");
        endDate = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DROP_DATE_TO_DISPLAY, "");
        startTime = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.PICKUP_TIME_TO_DISPLAY, "");
        distanceInKm = PreferencesUtils.getPreferences(getApplicationContext(), SharedPref.DISTANCE_IN_KM, "");

        txtTitle.setText("Confirm Booking");
        txtStartTime.setText(startTime);
        txtStartEndDate.setText(startDate);
        txtEstimatedKm.setText("Est km " + distanceInKm);
        txtTitleType.setText(serviceType + " ( " + direction + " ) ");

        radioPersonal.setChecked(true);

        radioPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                radioBusiness.setChecked(false);
                constraintBusinessInput.setVisibility(View.GONE);
            }
        });

        radioBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                radioPersonal.setChecked(false);

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConfirmBookingActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.business_view, null);

                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                AlertDialog b = dialogBuilder.create();

                doneButton = (AppCompatButton) dialogView.findViewById(R.id.doneButton);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();
                        constraintBusinessInput.setVisibility(view.VISIBLE);
                    }
                });

                b.show();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConfirmBookingActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.payment_screen, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                AlertDialog b = dialogBuilder.create();

                AppCompatButton btnPay = dialogView.findViewById(R.id.btnPay);

                btnPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConfirmBookingActivity.this);

                        LayoutInflater inflater = LayoutInflater.from(ConfirmBookingActivity.this);
                        View dialogView = inflater.inflate(R.layout.thank_you_screen, null);
                        dialogBuilder.setView(dialogView);
                        dialogBuilder.setCancelable(false);
                        AlertDialog b1 = dialogBuilder.create();

                        Button okButton = dialogView.findViewById(R.id.okButton);

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(ConfirmBookingActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });

                        b1.show();
                    }
                });

                b.show();
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfirmBookingActivity.this, BookingDetailsActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ConfirmBookingActivity.this, BookingDetailsActivity.class));
        finish();
    }
}

