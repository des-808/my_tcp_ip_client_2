package com.example.tcp_ip_client_2.ui.usb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tcp_ip_client_2.databinding.FragmentUsbBinding;

public class UsbFragment extends Fragment {

    private FragmentUsbBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UsbViewModel usbViewModel =
                new ViewModelProvider(this).get(UsbViewModel.class);

        binding = FragmentUsbBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textUsb;
        usbViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}