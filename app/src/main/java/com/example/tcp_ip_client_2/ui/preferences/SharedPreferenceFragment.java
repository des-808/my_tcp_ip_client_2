package com.example.tcp_ip_client_2.ui.preferences;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.tcp_ip_client_2.R;

public class SharedPreferenceFragment extends PreferenceFragmentCompat {
    int pageNumber;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        pageNumber = PageId.getIdPage();
        Log.d("onCreatePreferences", "pageNumber -> " + pageNumber);
        if (pageNumber == 0) {addPreferencesFromResource(R.xml.root_preferences_serial);}
        else if (pageNumber == 1) {addPreferencesFromResource(R.xml.root_preferences_terminal);}
        else if (pageNumber == 2) {addPreferencesFromResource(R.xml.root_preferences_receive);}
        else if (pageNumber == 3) {addPreferencesFromResource(R.xml.root_preferences_send);}
        //else if (pageNumber == 4) {addPreferencesFromResource(R.xml.root_preferences);}
        //else if (pageNumber == 3) {addPreferencesFromResource(R.xml.root_preferences3);}
               else{ addPreferencesFromResource(R.xml.root_preferences);}
    }

    private MultiSelectListPreference multiSelectPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            // Получение ссылки на MultiSelectListPreference
            multiSelectPref = findPreference("TimestampFormat");
            if (multiSelectPref != null) {
                // ListPreference listPref = (ListPreference) multiSelectPref.getDialog().getListView();
                // Делаем второй элемент (Option2) недоступным для выбора
                //listPref.setEnabled(1, false); // Нумерация начинается с нуля
            }
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