package com.kiwni.app.user.ui.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class aboutViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public aboutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is About fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}