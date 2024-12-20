package com.example.tcp_ip_client_2.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.tcp_ip_client_2.MainActivity;
import com.example.tcp_ip_client_2.ui.home.HomeFragment;


@SuppressLint("ValidFragment")
public class fragment_exit extends DialogFragment {

    @SuppressLint("ValidFragment")
    public fragment_exit(String title) {
        Bundle args = new Bundle(  );
        args.putString( "title", title );
        setArguments( args );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString( "title" );
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        return builder.setTitle(title).setPositiveButton( "OK", (dialog, whichButton) -> {
            ((HomeFragment) getParentFragment()).doPositiveClick();
        })
                .setNegativeButton( "Cancel", (dialog, whichButton) -> {
                    ((HomeFragment) getParentFragment()).doNegativeClick();
                }).create();



        /*return new AlertDialog.Builder( getActivity() )
                //.setIcon( R.drawable.android3d)
                .setTitle( title )
                .setPositiveButton( "OK", (dialog, whichButton) -> {
                    ((MainActivity) getActivity()).doPositiveClick();
                })
                .setNegativeButton( "Cancel", (dialog, whichButton) -> {
                    ((MainActivity) getActivity()).doNegativeClick();
                }).create();*/
    }
    }





