/*
package com.example.tcp_ip_client_2;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.tcp_ip_client_2.classs.ServerListItem;
import com.example.tcp_ip_client_2.db.DBManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonBroadcastReceiver extends BroadcastReceiver {

    private final DBManager dbManager;
    private List<ServerListItem> list;

    public JsonBroadcastReceiver(Context context) {
        dbManager = new DBManager(context);
        list = new ArrayList<>();
        //Log.d("JsonBroadcastReceiver", Objects.requireNonNull("Запустились"));
    }
    //private static final String CUSTOM_INTENT_ACTION = "com.example.broadcast.MY_NOTIFICATION";
    private static final String CUSTOM_INTENT_ACTION = "com.example.receiver.CUSTOM_ACTION";
    private static final String CUSTOM_INTENT_SEND = "android.intent.action.SEND";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Bingooo Receiver!!!", Objects.requireNonNull("Зашли в onReceive"));
        if (intent.getAction().equals(CUSTOM_INTENT_ACTION)) //{String json = intent.getStringExtra("data");
        //if (intent.getAction().equals(Intent.ACTION_SEND))
        { String json = intent.getStringExtra(Intent.EXTRA_TEXT);
            Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
            Log.d("JsonBroadcastReceiver", Objects.requireNonNull(json));

            try {
                list =  loadTitleListFromJson(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
 for (int i = 0; i < list.size(); i++) {
                dbManager.addContact(list.get(i));
            }

        }
    }

@Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action)) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.d("CustomBroadcastReceiver", "Received text: " + text);
            // Здесь можно обработать полученный текст (json)
        }
    }


    public List<ServerListItem> loadTitleListFromJson(String json) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ServerListItem>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
*/
