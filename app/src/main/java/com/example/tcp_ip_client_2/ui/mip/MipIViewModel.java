package com.example.tcp_ip_client_2.ui.mip;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tcp_ip_client_2.classs.ServerListItem;

import java.util.ArrayList;


public class MipIViewModel extends ViewModel {
    private MutableLiveData<String> mText = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ServerListItem>> mListView = new MutableLiveData<>();
    // TODO: Implement the ViewModel

    private MutableLiveData<String> operatingMode = new MutableLiveData<>();
    private MutableLiveData<String> shs1Status = new MutableLiveData<>();
    private MutableLiveData<String> shs2Status = new MutableLiveData<>();
    private MutableLiveData<String> shs3Status = new MutableLiveData<>();
    private MutableLiveData<String> ups1Status = new MutableLiveData<>();
    private MutableLiveData<String> ups2Status = new MutableLiveData<>();
    private MutableLiveData<String> textViewSearchAaddress = new MutableLiveData<>();

    public MipIViewModel() {}

    public MutableLiveData<ArrayList<ServerListItem>> getmListView() {
        return mListView;
    }

    public void setmListView(MutableLiveData<ArrayList<ServerListItem>> mListView) {
        this.mListView = mListView;
    }

    public LiveData<String> getText() {
        return mText;
    }
    public void set_mText(String mText) {
        //this.mText = mText;
        this.mText.setValue(String.valueOf(mText));
    }


    private MutableLiveData<String> spinnerDataBoudrate = new MutableLiveData<>();
    private MutableLiveData<String> spinnerDataOld = new MutableLiveData<>();
    private MutableLiveData<String> spinnerDataNew = new MutableLiveData<>();
    private MutableLiveData<String> mTextRxTx = new MutableLiveData<>();

    public LiveData<String> getSpinnerDataBoudrate() {
        return spinnerDataBoudrate;
    }

    public void setSpinnerDataBoudrate(String data) {
        spinnerDataBoudrate.setValue(data);
    }

    public LiveData<String> getSpinnerDataNew() {
        return spinnerDataOld;
    }
    public LiveData<String> getSpinnerDataOld() {
        return spinnerDataOld;
    }

    public void setSpinnerDataNew(String data) {
        spinnerDataNew.setValue(data);
    }

    public void setSpinnerDataOld(String data) {
        spinnerDataNew.setValue(data);
    }

    public LiveData<String> getmTextRxTx() {
        return mTextRxTx;
    }

    public void setmTextRxTx(String mTextRxTx) {
        this.mTextRxTx.setValue(mTextRxTx);
    }

    public LiveData<String> getOperatingMode() {
        return operatingMode;
    }

    public void setOperatingMode(String operatingMode) {
        this.operatingMode.setValue(operatingMode);
    }

    public LiveData<String> getShs1Status() {
        return shs1Status;
    }

    public void setShs1Status(String shs1Status) {
        this.shs1Status.setValue(shs1Status);
    }

    public LiveData<String> getShs2Status() {
        return shs2Status;
    }

    public void setShs2Status(String shs2Status) {
        this.shs2Status.setValue(shs2Status);
    }

    public LiveData<String> getShs3Status() {
        return shs3Status;
    }

    public void setShs3Status(String shs3Status) {
        this.shs3Status.setValue(shs3Status);
    }

    public LiveData<String> getUps1Status() {
        return ups1Status;
    }

    public void setUps1Status(String ups1Status) {
        this.ups1Status.setValue(ups1Status);
    }

    public LiveData<String> getUps2Status() {
        return ups2Status;
    }

    public void setUps2Status(String ups2Status) {
        this.ups2Status.setValue(ups2Status);
    }

    public LiveData<String> getTextViewSearchAaddress() {
        return textViewSearchAaddress;
    }

    public void setTextViewSearchAaddress(String textViewSearchAaddress) {
        this.textViewSearchAaddress.setValue(textViewSearchAaddress);
    }
}