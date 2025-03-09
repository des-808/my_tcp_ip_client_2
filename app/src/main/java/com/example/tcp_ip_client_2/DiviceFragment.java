package com.example.tcp_ip_client_2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Vibrator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiviceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout linearLayout;
    NavController navController;
    Vibrator vibrator;

    public DiviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiviceFragment newInstance(String param1, String param2) {
        DiviceFragment fragment = new DiviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        //return inflater.inflate(R.layout.fragment_bluetooth, container, false);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
          return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayout = view.findViewById(R.id.linearLayout_FragmentBluetooth);
        navController = NavHostFragment.findNavController(this);
        Button button1 = new Button(getActivity());
        button1.setText("Терминал");
        button1.setTextSize(24);
        button1.setWidth(100);
        button1.setTextColor(getResources().getColor(R.color.colorSendText));
        button1.setPadding(0, 30, 0, 30);
        button1.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                navController.navigate(R.id.nav_tcpIpFragment);
                // Обработчик нажатия для button1
            }
        });
        linearLayout.addView(button1);

        Button button2 = new Button(getActivity());
        button2.setText("Норд");
        button2.setTextSize(24);
        button2.setWidth(100);
        button2.setTextColor(getResources().getColor(R.color.colorSendText));
        button2.setPadding(0, 30, 0, 30);
        button2.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработчик нажатия для textView2
                vibrator.vibrate(50);
                navController.navigate(R.id.nav_keyboard);
            }
        });
        linearLayout.addView(button2);

        Button button3 = new Button(getActivity());
        button3.setText("Лин");
        button3.setTextSize(24);
        button3.setWidth(100);
        button3.setTextColor(getResources().getColor(R.color.colorSendText));
        button3.setPadding(0, 30, 0, 30);
        button3.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(50);
                // Обработчик нажатия для textView3
            }
        });
        linearLayout.addView(button3);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

}