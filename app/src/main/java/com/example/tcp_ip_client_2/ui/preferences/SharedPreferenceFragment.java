package com.example.tcp_ip_client_2.ui.preferences;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import com.example.tcp_ip_client_2.R;

public class SharedPreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        int pageNumber = PageId.getIdPage();
        Log.d("onCreatePreferences", "pageNumber -> " + pageNumber);
        if (pageNumber == 0) {addPreferencesFromResource(R.xml.root_preferences_serial);}
        else if (pageNumber == 1) {addPreferencesFromResource(R.xml.root_preferences_terminal);}
        else if (pageNumber == 2) {addPreferencesFromResource(R.xml.root_preferences_receive);}
        else if (pageNumber == 3) {addPreferencesFromResource(R.xml.root_preferences_send);}
        //else if (pageNumber == 4) {addPreferencesFromResource(R.xml.root_preferences);}
        //else if (pageNumber == 3) {addPreferencesFromResource(R.xml.root_preferences3);}
               else{ addPreferencesFromResource(R.xml.root_preferences);}
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}