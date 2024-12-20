package com.example.tcp_ip_client_2.ui.usb;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UsbViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UsbViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is usb fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}