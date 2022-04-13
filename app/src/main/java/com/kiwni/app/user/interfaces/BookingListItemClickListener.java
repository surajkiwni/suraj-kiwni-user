package com.kiwni.app.user.interfaces;

import android.view.View;

import com.kiwni.app.user.models.ScheduleMapResp;

import java.util.List;

public interface BookingListItemClickListener {
        void onItemClick(View v, int position, List<ScheduleMapResp> bookingModels);
}

