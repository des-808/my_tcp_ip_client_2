package com.example.tcp_ip_client_2.ui.preferences;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import com.example.tcp_ip_client_2.R;

public class SharedPreferenceFragment extends PreferenceFragmentCompat {
    //private OnSettingsFragment mFragmentSettings;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.root_preferences);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /*if (context instanceof OnSettingsFragment){
            mFragmentSettings = (OnSettingsFragment) context;
        }
        else {
            throw new RuntimeException( context + " must implement OnFragmentInteractionListener" );
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mFragmentSettings.on_SettingsFragment();
    }
}