package com.kiwni.app.user.ui.refer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReferEarnViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public ReferEarnViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is refer and earn fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
