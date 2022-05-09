package com.kiwni.app.user.ui.FAQs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FaqViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<String> mText;

    public FaqViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("faq fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}