package com.kiwni.app.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.kiwni.app.user.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {

        });


        ImageView imageBack = findViewById(R.id.imageBack);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, VehicleListByTypeActivity.class);
                startActivity(intent);
                finish();
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
    }
}