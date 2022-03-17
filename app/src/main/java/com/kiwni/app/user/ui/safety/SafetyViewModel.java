package com.kiwni.app.user.ui.safety;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SafetyViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<String> mText;

    public SafetyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("safety fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}