package com.kiwni.app.user.interfaces;

import android.view.View;

import com.kiwni.app.user.datamodels.FindCarModel;

import java.util.List;

public interface FindCarItemClickListener {

    void onFindCarItemClick(View v, int position, List<FindCarModel> findCarModels);
}
