package com.example.tcp_ip_client_2.ui.preferences;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.databinding.FragmentPreferencePageBinding;


public class Preference_Page_Fragment extends Fragment {

    public Preference_Page_Fragment() {}
    private int pageNumber;
    FragmentPreferencePageBinding fragmentBinding;
    FragmentContainerView fragmentContainerView;
    TextView pageHeader;

    public static Preference_Page_Fragment newInstance(int page) {
        Preference_Page_Fragment fragment = new Preference_Page_Fragment();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentPreferencePageBinding.inflate(inflater, container, false);
        pageHeader = fragmentBinding.displayText;
        fragmentContainerView = fragmentBinding.fragmentPreferencePageContainer;
        setPageNumber( pageNumber);
        return fragmentBinding.getRoot();
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        String header = Pages.values()[pageNumber].getName();
        pageHeader.setText(header);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentPreferencePage_container, new SharedPreferenceFragment());
        transaction.commit();
    }

}
