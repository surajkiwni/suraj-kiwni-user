package com.kiwni.app.user.interfaces;

import android.view.View;

import com.kiwni.app.user.datamodels.FilterModel;

import java.util.List;

public interface FilterItemClickListener {

    void onFilterItemClick(View v, int position, List<FilterModel> filterModels);
}
