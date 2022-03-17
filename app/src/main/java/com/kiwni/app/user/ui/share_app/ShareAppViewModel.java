package com.kiwni.app.user.ui.share_app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareAppViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<String> mText;

    public ShareAppViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share App fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}