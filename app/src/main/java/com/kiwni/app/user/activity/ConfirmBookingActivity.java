package com.kiwni.app.user.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.kiwni.app.user.MainActivity;
import com.kiwni.app.user.R;

public class ConfirmBookingActivity extends AppCompatActivity {

    RadioButton bookingButton,personalButton;
    AppCompatButton confirmButton,doneButton;
    View view1;
    ConstraintLayout constraintLayout5;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        personalButton = findViewById(R.id.radioButton1);
        bookingButton =findViewById(R.id.radioButton2);
        confirmButton = findViewById(R.id.confirmButton);
        constraintLayout5 =findViewById(R.id.constraintLayout5);
        imageBack = findViewById(R.id.imageBack);

       // final NavController navController = Navigation.findNavController(view1);

        personalButton.setChecked(true);

        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConfirmBookingActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.business_view, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                AlertDialog b = dialogBuilder.create();

                personalButton.setChecked(false);

                b.show();
                doneButton = (AppCompatButton) dialogView.findViewById(R.id.doneButton);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        b.dismiss();

                        constraintLayout5.setVisibility(view.VISIBLE);

                    }
                });
            }

        });

        personalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingButton.setChecked(false);
                constraintLayout5.setVisibility(View.GONE);
            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConfirmBookingActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.payment_screen, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                AlertDialog b = dialogBuilder.create();

                b.show();

                AppCompatButton bookingSuccessfulButton = dialogView.findViewById(R.id.bookingSuccessfulButton);

                bookingSuccessfulButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConfirmBookingActivity.this);

                        LayoutInflater inflater = LayoutInflater.from(ConfirmBookingActivity.this);
                        View dialogView = inflater.inflate(R.layout.thank_you_screen, null);
                        dialogBuilder.setView(dialogView);
                        dialogBuilder.setCancelable(false);
                        AlertDialog b1 = dialogBuilder.create();

                        b1.show();

                        Button okButton = dialogView.findViewById(R.id.okButton);

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(ConfirmBookingActivity.this, MainActivity.class);
                               // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //this will always start your activity as a new task
                                /*ConfirmBookingActivity.this.startActivity(intent);
                                ConfirmBookingActivity.this.finishAffinity();*/
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                    }
                });
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

