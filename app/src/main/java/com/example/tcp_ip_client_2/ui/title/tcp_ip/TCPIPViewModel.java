package com.example.tcp_ip_client_2.ui.title.tcp_ip;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tcp_ip_client_2.adapter.ChatMessageAdapter;
import com.example.tcp_ip_client_2.classs.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class TCPIPViewModel extends ViewModel {
    private MutableLiveData<String> mText;


//=================================================================================================
    private List<ChatModel> chatMessageList;
    private ChatMessageAdapter chatMessageAdapter;
    private MutableLiveData<List<ChatModel>> chatMessageListLiveData;

    public TCPIPViewModel() {
        chatMessageList = new ArrayList<>();
        chatMessageAdapter = new ChatMessageAdapter(chatMessageList);
        chatMessageListLiveData = new MutableLiveData<>();
        chatMessageListLiveData.setValue(chatMessageList);
    }

    public LiveData<List<ChatModel>> getChatMessageListLiveData() {
        return chatMessageListLiveData;
    }

    public ChatMessageAdapter getChatMessageAdapter() {
        return chatMessageAdapter;
    }

    public void addChatMessage(ChatModel chatMessageModel) {
        chatMessageList.add(chatMessageModel);
        chatMessageAdapter.notifyItemInserted(chatMessageList.size() - 1);
        chatMessageListLiveData.setValue(chatMessageList);


    }

    @SuppressLint("NotifyDataSetChanged")
    public void addChatMessages(List<ChatModel> chatMessageList) {
        this.chatMessageList.addAll(chatMessageList);
        chatMessageAdapter.notifyDataSetChanged();
        chatMessageListLiveData.setValue(chatMessageList);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearList() {
        this.chatMessageList.clear();
        chatMessageAdapter.notifyDataSetChanged();
        chatMessageListLiveData.setValue(chatMessageList);
    }


//=================================================================================================
//=================================================================================================
   /* public TCPIPViewModel() {
        *//*mText = new MutableLiveData<>();
        mText.setValue("This is title fragment");*//*
    }*/

    public LiveData<String> getText() {
        return mText;
    }
    public void setmText(MutableLiveData<String> mText) {
        this.mText = mText;
    }
    //private
}