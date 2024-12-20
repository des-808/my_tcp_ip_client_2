package com.example.tcp_ip_client_2.ui.preferences;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.databinding.FragmentSettingsBinding;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private View root;
    private SettingsViewModel mViewModel;
    private Context context;
    private ViewPager2 pager;
    private TabLayout tabLayout;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        tabLayout = binding.tabLayout;
        context = getContext();
        pager = binding.pager;
        FragmentStateAdapter pageAdapter = new PageAdapter(getActivity());
        pager.setAdapter(pageAdapter);
        TabLayoutMediator tabLayoutMediator= new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy(){
            @Override
            public void onConfigureTab(TabLayout.Tab tab, int position) {
                PageId.setIdPage(position);//Заглушка.Необходима для того, чтобы педать в другой фрагмент
                tab.setText(Pages.values()[position].getName());
                SpannableString spannableString = new SpannableString(Pages.values()[position].getName());
                spannableString.setSpan(new RelativeSizeSpan(0.8f), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tab.setText(spannableString);
            }
        });
        tabLayoutMediator.attach();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



}