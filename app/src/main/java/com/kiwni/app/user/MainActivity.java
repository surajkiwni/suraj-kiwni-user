package com.kiwni.app.user;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.kiwni.app.user.R;
import com.kiwni.app.user.databinding.ActivityMainBinding;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.kiwni.app.user.models.socket.SocketReservationResp;
import com.kiwni.app.user.socket.SocketSingletonClass;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    String TAG = this.getClass().getSimpleName();
    public DrawerLayout drawerLayout;
    NavController navController;
    ConstraintLayout constraintProfile;
    BottomSheetDialog bottomSheetDialog;
    MenuItem action_like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getSupportActionBar().setTitle("title");

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // R.id.nav_home R.id.nav_myrides
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)

                //R.id.nav_refer,R.id.nav_myrides,R.id.nav_about,
                //R.id.nav_payment,R.id.nav_offers,R.id.nav_safety,R.id.nav_faq,R.id.nav_feedback,R.id.nav_shareapp,R.id.nav_support
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        constraintProfile =headerView.findViewById(R.id.constraintProfile);
        constraintProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_nav_home_to_profileFragment);
                drawerLayout.close();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_like:

                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                bottomSheetDialog = new BottomSheetDialog(this);
                View view = getLayoutInflater().inflate(R.layout.favorite_bottom_sheet,null,false);

                bottomSheetDialog.show();
                bottomSheetDialog.setCancelable(false);

                //setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);
                //bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                AppCompatButton cancelButton = view.findViewById(R.id.cancelButton);
                AppCompatButton saveButton = view.findViewById(R.id.saveButton);

                saveButton.setBackgroundColor(Color.BLACK);
                saveButton.setTextColor(Color.WHITE);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveButton.setBackgroundColor(Color.TRANSPARENT);
                        saveButton.setTextColor(Color.BLACK);
                        cancelButton.setBackgroundColor(Color.BLACK);
                        cancelButton.setTextColor(Color.WHITE);
                    }
                });

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.setContentView(view);

                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
       /* return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();*/

      return  NavigationUI.navigateUp(navController, drawerLayout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to exit from app..")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        finish();
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        drawerLayout.closeDrawers();

        int id = item.getItemId();

            switch (id)
            {
                case R.id.nav_shareapp:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                    break;

                case R.id.nav_myrides:

                    navController.navigate(R.id.action_nav_home_to_nav_myrides);

                    break;

                case R.id.nav_payment:

                    navController.navigate(R.id.action_nav_home_to_nav_payment);
                    break;

                case R.id.nav_offers:

                    navController.navigate(R.id.action_nav_home_to_nav_offers);
                    break;

                case R.id.nav_safety:

                    navController.navigate(R.id.action_nav_home_to_nav_safety);
                    break;

                case R.id.nav_faq:
                    navController.navigate(R.id.action_nav_home_to_nav_faq);

                    break;

                case R.id.nav_feedback:

                    Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Light);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.feedback_bottom_sheet);

                    final ImageView imageClose = dialog.findViewById(R.id.imageClose);

                    imageClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });


                    dialog.show();

                    break;

                case R.id.nav_refer:

                    navController.navigate(R.id.action_nav_home_to_nav_refer);
                    break;

                case R.id.nav_support:

                    navController.navigate(R.id.action_nav_home_to_nav_support);
                    break;

                case R.id.nav_about:

                    navController.navigate(R.id.action_nav_home_to_nav_about);
                    break;
            }

            return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}