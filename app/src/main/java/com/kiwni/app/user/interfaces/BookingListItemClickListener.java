package com.kiwni.app.user.interfaces;

import android.view.View;

import com.kiwni.app.user.datamodels.BookingModel;

import java.util.List;

public interface BookingListItemClickListener {

        void onItemClick(View v, int position, List<BookingModel> bookingModels);


}
