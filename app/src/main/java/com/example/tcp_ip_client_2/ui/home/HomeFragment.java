package com.example.tcp_ip_client_2.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.tcp_ip_client_2.databinding.FragmentHomeBinding;
import com.example.tcp_ip_client_2.fragments.fragment_exit;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //fragment_exit dialogFragment;
    /*@NonNull
    private void alertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setTitle("Так ты точно хочешь выйти??");
        alertDialogBuilder.setTitle(msg);
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("Да", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        //tostShort("Правильный выбор!!");
                        finish();
                    }
                })
                .setNegativeButton("Нет",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/
    public void doPositiveClick() {/*finish();*/}
    public void doNegativeClick() {}

   /* public void doExitDialogStart() {
        dialogFragment = new fragment_exit( "Так ты точно хочешь выйти???" );
        FragmentTransaction fManager = getFragmentManager().beginTransaction();
        dialogFragment.show( fManager, "dialog" );
    }*/
}