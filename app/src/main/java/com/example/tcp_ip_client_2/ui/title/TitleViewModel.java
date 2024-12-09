package com.example.tcp_ip_client_2.ui.title;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tcp_ip_client_2.classs.TitleChatsItems;

import java.util.ArrayList;
import java.util.List;


public class TitleViewModel extends ViewModel {



    private MutableLiveData<String> mText;
    private MutableLiveData<ArrayList<TitleChatsItems>> mListView = new MutableLiveData<>();

    /*public TitleViewModel(MutableLiveData<String> mText, MutableLiveData<ArrayList<TitleChatsItems>> mListView) {
        this.mText = mText;
        this.mListView = mListView;
    }*/



    public MutableLiveData<ArrayList<TitleChatsItems>> getmListView() {
        return mListView;
    }

    public void setmListView(MutableLiveData<ArrayList<TitleChatsItems>> mListView) {
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