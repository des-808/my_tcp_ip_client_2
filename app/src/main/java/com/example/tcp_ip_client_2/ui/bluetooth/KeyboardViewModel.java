package com.example.tcp_ip_client_2.ui.bluetooth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KeyboardViewModel extends ViewModel {
    private MutableLiveData<String> keyboardStr1Resource = new MutableLiveData<>();
    private MutableLiveData<String> keyboardStr2Resource = new MutableLiveData<>();
    private MutableLiveData<String> keyboardStr3Resource = new MutableLiveData<>();



    public LiveData<String> getKeyboardStr1() {return keyboardStr1Resource;}
    public void setKeyboardStr1(String text1) {keyboardStr1Resource.setValue(text1);}
    public LiveData<String> getKeyboardStr2() {return keyboardStr2Resource;}
    public void setKeyboardStr2(String text2) {keyboardStr2Resource.setValue(text2);}
    public LiveData<String> getKeyboardStr3() {return keyboardStr3Resource;}
    public void setKeyboardStr3(String text3) {keyboardStr3Resource.setValue(text3);}


}
