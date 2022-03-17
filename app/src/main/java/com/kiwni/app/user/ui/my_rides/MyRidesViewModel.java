package com.kiwni.app.user.ui.my_rides;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyRidesViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<String> mText;

    public MyRidesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is My Rides fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}