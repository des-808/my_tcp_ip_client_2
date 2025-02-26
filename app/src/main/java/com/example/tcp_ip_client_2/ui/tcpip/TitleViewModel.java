package com.example.tcp_ip_client_2.ui.tcpip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tcp_ip_client_2.classs.ServerListItem;

import java.util.ArrayList;


public class TitleViewModel extends ViewModel {



    private MutableLiveData<String> mText;
    private MutableLiveData<ArrayList<ServerListItem>> mListView = new MutableLiveData<>();

    /*public TitleViewModel(MutableLiveData<String> mText, MutableLiveData<ArrayList<ServerListItem>> mListView) {
        this.mText = mText;
        this.mListView = mListView;
    }*/



    public MutableLiveData<ArrayList<ServerListItem>> getmListView() {
        return mListView;
    }

    public void setmListView(MutableLiveData<ArrayList<ServerListItem>> mListView) {
        this.mListView = mListView;
    }

    public TitleViewModel() {
        /*mText = new MutableLiveData<>();
        mText.setValue("This is title fragment");*/
    }

    public LiveData<String> getText() {
        return mText;
    }
    public void setmText(MutableLiveData<String> mText) {
        this.mText = mText;
    }
    //private
}