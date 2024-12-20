package com.example.tcp_ip_client_2.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.tcp_ip_client_2.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {
    private WebView webView;
    private FragmentGalleryBinding binding;
    GalleryViewModel galleryViewModel;
    TextView textView;
    String url = "https://metanit.com/java/android/13.1.php";

    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        webView = binding.web;
        webView.getSettings().setJavaScriptEnabled(true);
        textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //webView.loadUrl("https://www.gwars.io");
        galleryViewModel.setText(url);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        webView.loadUrl("https://metanit.com/java/android/13.1.php");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       /* String url = webView.getUrl();
        galleryViewModel.setText(url);*/
    }
}