package com.example.tcp_ip_client_2.ui.mip;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.classs.MipI;
import com.example.tcp_ip_client_2.classs.ResivedBufferDelegate;
import com.example.tcp_ip_client_2.databinding.FragmentMipIBinding;
import com.example.tcp_ip_client_2.interfaces.OnMessageReceivedListener;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MipIFragment extends Fragment implements OnMessageReceivedListener {
    static byte[]  receive_buffer3 = new byte[]{
            (byte)0x01, (byte)0x03, (byte)0x3C, (byte)0x00, (byte)0x14, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x04, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x03, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x03,
            (byte)0x00, (byte)0x03, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x00, (byte)0x03, (byte)0x3F, (byte)0x1E, (byte)0xB8, (byte)0x52, (byte)0x3F, (byte)0x1E, (byte)0xB8,
            (byte)0x52, (byte)0x3F, (byte)0x1E, (byte)0xB8, (byte)0x52, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x21, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0x00, (byte)0x00, (byte)0xB4, (byte)0x2E};
    private final String ADDRESS_DEVICE = "     Адрес прибора -> ";
    MipI mipi = new MipI();
    ResivedBufferDelegate receiveBuffer;// = new ResivedBufferDelegate()
    private int shs = 1;
    private static String strRxTx = "Tx :  \nRx : \n      :  \n"    ;
    boolean resetAlarmAll = false;
    boolean cicleAllParameters = false;
    boolean start = false;
    private FragmentMipIBinding binding;
    boolean cicleSearchAddress = true;
    View root;
    MipIViewModel mipiViewModel;
    TextView textViewRxTx;
    int count = 0;

    Vibrator vibrator;
    public static MipIFragment newInstance() {
        return new MipIFragment();
    }

    int bytesToInteger(byte[] b) {
        int i = (b[1] & 0xFF) | ((b[0] & 0xFF) << 8);
        return i;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mipiViewModel = new ViewModelProvider(this).get(MipIViewModel.class);
        binding = FragmentMipIBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        TextView textViewAddress = binding.textViewAddress;
        mipiViewModel.set_mText( ADDRESS_DEVICE + bytesToInteger(mipi.device.readRegisterData(1)));
        textViewRxTx = binding.textViewRxTx;

        // Создание ArrayList
        ArrayList<String> spinnerArrayOldAddress = new ArrayList<>();
        for (int i = 1; i < 248; i++) {spinnerArrayOldAddress.add(Integer.toString(i));}
        ArrayList<String> spinnerArrayNewAddress = new ArrayList<>();
        for (int i = 1; i < 248; i++) {spinnerArrayNewAddress.add(Integer.toString(i));}
        Button btnWriteAddress = binding.btnWriteAddress;
        Button btnSearchAddress = binding.btnSearch;
        Spinner spinnerBoudrate =binding.SpinnerBoudrate;
        Button btnBoudRateWrite = binding.btnBoudRateWrite;
        Spinner spinnerOldAddress =binding.SpinnerOldAddress;
        Spinner spinnerNewAddress =binding.SpinnerNewAddress;
        Spinner spinnerTipDevice =binding.SpinnerTipDevice;

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchTermocompensation = binding.termocompensation;
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchYhontPui          = binding.yhontPui;
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchFixLengthAlarm    = binding.fixLengthAlarm;

        RadioGroup radioGroup = binding.rgroup;
        Button btnWriteLength = binding.btnWriteLength;
        Button btnWriteOM = binding.btnWriteOM;
        EditText editTextLengthShS1 = binding.editTextLengthShS1;
        EditText editTextLengthShS2 = binding.editTextLengthShS2;
        EditText editTextLengthShS3 = binding.editTextLengthShS3;
        EditText editTextOhMShS1 = binding.editTextOhMShS1;
        EditText editTextOhMShS2 = binding.editTextOhMShS2;
        EditText editTextOhMShS3 = binding.editTextOhMShS3;

        RadioGroup releShS1 = binding.releShS1;
        RadioGroup releShS2 = binding.releShS2;
        RadioGroup releShS3 = binding.releShS3;

        TextView operating_mode = binding.operatingMode;
        TextView shs1Status = binding.shs1Status;
        TextView shs2Status = binding.shs2Status;
        TextView shs3Status = binding.shs3Status;
        TextView ups1Status = binding.ups1Status;
        TextView ups2Status = binding.ups2Status;
        TextView textViewSearchAaddress = binding.textViewSearchAaddress;

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchResetVolumeAll    = binding.switchResetVolumeAll;
        ImageButton btnResetVolume    = binding.btnResetVolume;
        Button btnResetAlarm     = binding.btnResetAlarm;
        Button btnGetParametersAll     = binding.btnGetParametersAll;
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchGetParametersAllCicle     = binding.switchGetParametersAllCicle;

        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.spinner_boudrate_array, android.R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBoudrate.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<CharSequence> spinnerArrayAdapterTipDevice = ArrayAdapter.createFromResource(requireContext(), R.array.spinner_tip_device, android.R.layout.simple_spinner_item);
        spinnerArrayAdapterTipDevice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipDevice.setAdapter(spinnerArrayAdapterTipDevice);

        ArrayAdapter<String> spinnerArrayAdapterNewAddress = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArrayNewAddress);
        ArrayAdapter<String> spinnerArrayAdapterOldAddress = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArrayOldAddress);
        spinnerArrayAdapterNewAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayAdapterOldAddress.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOldAddress.setAdapter(spinnerArrayAdapterOldAddress);
        spinnerNewAddress.setAdapter(spinnerArrayAdapterNewAddress);

        byte[] pos = mipi.device.readRegisterData(2);
        int positionBoudrate = pos[1];
        spinnerBoudrate.setSelection(positionBoudrate-1);

        spinnerBoudrate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    mipiViewModel.setSpinnerDataBoudrate(selectedItem);
                    mipi.device.writeRegisterData(2, Integer.valueOf(position + 1));
                //set_strRxTx("Tx", mipi.sendWriteBoudRate(position + 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Обработка отсутствия выбора значения

            }
        });

        btnBoudRateWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position = spinnerBoudrate.getSelectedItemPosition();
                //set_strRxTx("Tx",mipi.sendWriteBoudRate(position + 1));
                //или
                byte[] pos = mipi.device.readRegisterData(2);
                int position2 = (byte)pos[1]&0xFF;
                
                set_strRxTx("Tx",mipi.sendWriteBoudRate(position2));

            }
        });

        mipiViewModel.getSpinnerDataBoudrate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление Spinner при изменении LiveData
                spinnerBoudrate.setSelection(spinnerArrayAdapterOldAddress.getPosition(data));
            }
        });
        mipiViewModel.getSpinnerDataOld().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление Spinner при изменении LiveData
                spinnerOldAddress.setSelection(spinnerArrayAdapterOldAddress.getPosition(data));
            }
        });
        mipiViewModel.getSpinnerDataNew().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление Spinner при изменении LiveData
                spinnerNewAddress.setSelection(spinnerArrayAdapterNewAddress.getPosition(data));
            }
        });

        mipiViewModel.getmTextRxTx().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление TextView при изменении LiveData
                textViewRxTx.setText(data);
            }
        });


        mipiViewModel.getOperatingMode().observe(getViewLifecycleOwner(), new Observer<String>(){
            @Override
            public void onChanged(String data) {
                // Обновление TextView при изменении LiveData
                operating_mode.setText(data);
            }
        });
        mipiViewModel.getShs1Status().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление TextView при изменении LiveData
                shs1Status.setText(data);
            }
        });
        mipiViewModel.getShs2Status().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление TextView при изменении LiveData
                shs2Status.setText(data);
            }
        });
        mipiViewModel.getShs3Status().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление TextView при изменении LiveData
                shs3Status.setText(data);
            }
        });
        mipiViewModel.getUps1Status().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление TextView при изменении LiveData
                ups1Status.setText(data);
            }
        });
        mipiViewModel.getUps2Status().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление TextView при изменении LiveData
                ups2Status.setText(data);
            }
        });
        mipiViewModel.getTextViewSearchAaddress().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление TextView при изменении LiveData
                textViewSearchAaddress.setText(data);
            }
        });


        mipiViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                // Обновление TextView при изменении LiveData
                textViewAddress.setText(data);
            }
        });

        spinnerOldAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                mipiViewModel.setSpinnerDataOld(selectedItem);
                mipi.device.writeRegisterData(1,Integer.valueOf(selectedItem));
                //mipi.device.print();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Обработка отсутствия выбора значения
            }
        });

        btnWriteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] registerData = mipi.device.readRegisterData(1);
                int num = (byte)registerData[1]&0xFF;
                set_strRxTx("Tx",mipi.sendWriteAddress(num,Integer.valueOf(spinnerNewAddress.getSelectedItem().toString())));
                /*byte[] data = mipi.device.readRegisterData(1);
                int address = (byte)data[1]&0xFF;
                mipiViewModel.set_mText( ADDRESS_DEVICE + Integer.toString(address));*/
                mipiViewModel.set_mText( ADDRESS_DEVICE + bytesToInteger(mipi.device.readRegisterData(1)));
            }
        });

        spinnerNewAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                mipiViewModel.setSpinnerDataNew(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Обработка отсутствия выбора значения
            }
        });
        String positionAddress = String.valueOf(ByteBuffer.wrap(mipi.device.readRegisterData(1)).getShort());
        //mipiViewModel.setSpinnerDataOld(position);
        spinnerOldAddress.setSelection(spinnerArrayAdapterOldAddress.getPosition(positionAddress));
        spinnerNewAddress.setSelection(spinnerArrayAdapterNewAddress.getPosition(positionAddress));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.rBtnSHS1){shs = 1;}
                    else if(checkedId == R.id.rBtnSHS2){shs = 2;}
                    else if(checkedId == R.id.rBtnSHS3){shs = 3;}
            }
        });

        btnWriteLength.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int length = 0;
                switch (shs){
                    case 1: length = Integer.valueOf(editTextLengthShS1.getText().toString());if(length > 2000){ length = 2000;editTextLengthShS1.setText(String.valueOf(length));  }break;
                    case 2: length = Integer.valueOf(editTextLengthShS2.getText().toString());if(length > 2000){ length = 2000;editTextLengthShS2.setText(String.valueOf(length));  }break;
                    case 3: length = Integer.valueOf(editTextLengthShS3.getText().toString());if(length > 2000){ length = 2000;editTextLengthShS3.setText(String.valueOf(length));  }break;
                }
                set_strRxTx("Tx",mipi.sendWriteZoneLength(shs,length));
            }
        });
        btnWriteOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float resistance = 0.0f;
                switch (shs){
                    case 1: resistance = Float.valueOf(editTextOhMShS1.getText().toString());if(resistance < 0.100f){ resistance = 0.100f;editTextOhMShS1.setText(String.format("%.3f",resistance));}  else if(resistance > 1.000f){ resistance = 1.000f;editTextOhMShS1.setText(String.format("%.3f",resistance));  }break;
                    case 2: resistance = Float.valueOf(editTextOhMShS2.getText().toString());if(resistance < 0.100f){ resistance = 0.100f;editTextOhMShS2.setText(String.format("%.3f",resistance));}  else if(resistance > 1.000f){ resistance = 1.000f;editTextOhMShS2.setText(String.format("%.3f",resistance));  }break;
                    case 3: resistance = Float.valueOf(editTextOhMShS3.getText().toString());if(resistance < 0.100f){ resistance = 0.100f;editTextOhMShS3.setText(String.format("%.3f",resistance));}  else if(resistance > 1.000f){ resistance = 1.000f;editTextOhMShS3.setText(String.format("%.3f",resistance));  }break;
                }
                set_strRxTx("Tx",mipi.sendWriteZonePogonResitance(shs,resistance));
            }
        });

        releShS1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.releShS1NC){set_strRxTx("Tx",mipi.sendWriteOperatingModeRelayOutputsNORMAL(1,false));}
                else if(checkedId == R.id.releShS1NO){set_strRxTx("Tx",mipi.sendWriteOperatingModeRelayOutputsNORMAL(1,true));}
            }
        });
        releShS2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.releShS2NC){set_strRxTx("Tx",mipi.sendWriteOperatingModeRelayOutputsNORMAL(2,false));}
                else if(checkedId == R.id.releShS2NO){set_strRxTx("Tx",mipi.sendWriteOperatingModeRelayOutputsNORMAL(2,true));}
            }
        });
        releShS3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.releShS3NC){set_strRxTx("Tx",mipi.sendWriteOperatingModeRelayOutputsNORMAL(3,false));}
                else if(checkedId == R.id.releShS3NO){set_strRxTx("Tx",mipi.sendWriteOperatingModeRelayOutputsNORMAL(3,true));}
            }
        });



        btnSearchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cicleSearchAddress)
                {
                    timerCicleSearchAddress.postDelayed(runnableCicleSearchAddress, 20);
                    cicleSearchAddress = false;
                    btnSearchAddress.setText("СТОП");
                }
                else
                {
                    timerCicleSearchAddress.removeCallbacks(runnableCicleSearchAddress);
                    cicleSearchAddress = true;
                    count = 0;
                    btnSearchAddress.setText("ПОИСК");
                }
                //Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();

            }
        });

        switchResetVolumeAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resetAlarmAll = isChecked;
            }
        });
        btnResetVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resetAlarmAll){
                    set_strRxTx("Tx",mipi.sendWriteSoundResetAlarmAll());
                }else{
                    set_strRxTx("Tx",mipi.sendWriteSoundResetAlarm());
                }
            }
        });
        btnResetAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    set_strRxTx("Tx",mipi.sendWriteAlarmResetNotices());
                }
            });
        btnGetParametersAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cicleAllParameters){
                        set_strRxTx("Tx",mipi.sendReadAllParametrs());
                        try {
                            // Запуск таймера
                            timerCicleReadAllParameters.postDelayed(runnableCicleReadAllParameters, 20);
                        }catch (Exception e){
                            //e.printStackTrace();
                            Log.e("timerCicleReadAllParameters", e.toString());
                        }
                    }else{
                        set_strRxTx("Tx",mipi.sendReadAllParametrs());
                    }

                }
            });
        switchGetParametersAllCicle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    timerCicleReadAllParameters.removeCallbacks(runnableCicleReadAllParameters);//отключаем таймер
                }
                cicleAllParameters = isChecked;
            }
        });

        switchTermocompensation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                set_strRxTx("Tx",mipi.sendWriteTemperatureCompensation(isChecked));
            }
        });

        switchYhontPui.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                set_strRxTx("Tx",mipi.sendWriteYahontPui(isChecked));
            }
        });

        switchFixLengthAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                set_strRxTx("Tx",mipi.sendWriteRegistrationOfLengthThermalCableAlarmCondition(isChecked));
            }
        });
        return root;

    }

    /**
     * TODO: Вызов функции для отображения сообщения в TextViewRxTx
     * @param message - сообщение
     * RxTx - Принимает строку "Rx","Tx" или Message
     * Rx - Принимаемые сообщения
     * Tx - Отправляемые сообщения
     * Message - если есть ошибка при отправке сообщ или приёме сообщения или если нет сообщений.
     *                А так же при успешной обработке сообщения отвечает "OK"
     * */
    void set_strRxTx(String InOut,String message){
        String[] parts = strRxTx.split("\n");
        switch (InOut) {
            case "Tx": parts[0] = InOut + " : " + message;
                       parts[2] = "      : ";
                try {
                    // Запуск таймера
                    timerRx.postDelayed(runnable, 300);
                }catch (Exception e){
                    //e.printStackTrace();
                    Log.e("timerRx", e.toString());
                }
            break;
            case "Rx": parts[1] = InOut + "  : " + message;break;
            case "Message": parts[2] = "      : " + message;break;
        }
        strRxTx = String.join("\n", parts);
        mipiViewModel.setmTextRxTx((strRxTx));
    }
    byte[] set_strRxTx(String InOut,byte[] message){
        String str = "";
        for (int i = 0; i < message.length; i++) {
           str += String.format("%02X ", (byte) (message[i] & 0xFF));
        }
        set_strRxTx(InOut,str);
        return message;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart(){
        super.onStart();
        start = true;
        /*mipi.parser.getBuffer_Ressive(mipi.buffer_Transmitte,receive_buffer3);
        mipi.device.print();*/
     //  sendData(mipi.sendWriteBoudRate(4));

/*         sendData(mipi.sendWriteSoundResetAlarmAll());//сброс звука общий
        sendData(mipi.sendWriteAlarmResetNotices());//сброс тревоги
        mipi.print(mipi.sendReadAllParametrs());*/
        //receiveBuffer.onMessageReceived(receive_buffer3);
        //mipi.print(mipi.sendReadAllParametrs());
        //set_strRxTx("Tx",mipi.sendReadAllParametrs());
        MyClass myClass = new MyClass(receiveBuffer);
        //myClass.sendData(receive_buffer3);

        //set_strRxTx("Tx", mipi.sendWriteSoundResetAlarm());//сброс звука
        //set_strRxTx("Rx","f7 06 00 02 00 01 C9 A8");
        //set_strRxTx("Message","TIME OUT.");

        mipiViewModel.setOperatingMode(" БЕЗ ФИКСАЦИИ ТРЕВОГИ ");
        mipiViewModel.setShs1Status(" НОРМА ");
        mipiViewModel.setShs2Status(" ТРЕВОГА ");
        mipiViewModel.setShs3Status(" ОБРЫВ ");
        mipiViewModel.setUps1Status(" 12.4 ");
        mipiViewModel.setUps2Status(" 12.3 ");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        receiveBuffer = new ResivedBufferDelegate();
        receiveBuffer.setOnMessageReceivedListener(this);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        receiveBuffer.setOnMessageReceivedListener(null);
    }

    //@Override
    public void onMessageReceived(byte[] message){
        timerRx.removeCallbacks(runnable);//отключаем таймер
        if(mipi.parser.getBuffer_Ressive(mipi.buffer_Transmitte,message)==null){
            set_strRxTx("Rx",message);//для отладки выводим сообщение что бы увидеть что пришло
            set_strRxTx("Message",mipi.parser.getMessageError());
        }
        else{
            set_strRxTx("Rx",message);
            set_strRxTx("Message","OK");
        }
        //mipi.device.print();
    }
    public void sendData(byte[] data) {
            mipi.print(data);
    }

    public byte[] ressiveData() {
        return mipi.buffer_Transmitte;
    }

   // Класс, который будет выдавать данные в делегат
   public class MyClass {
       private ResivedBufferDelegate receiveBuffer;

       public MyClass(ResivedBufferDelegate receiveBuffer3) {
           this.receiveBuffer = receiveBuffer3;
       }

       // Метод, который выдает данные в делегат
       public void sendData(byte[] message) {
           receiveBuffer.onMessageReceived(message);
       }
   }

    /**TODO
     * Таймер ответа. Если таймер сработал то ответ не получен
     */
    Handler timerRx = new Handler();
    Runnable runnable = new Runnable() {//если таймер сработал то ответ не получен
        @Override
        public void run() {
            timerRx.removeCallbacks(runnable);//отключаем таймер
            set_strRxTx("Message","TIME OUT.");
        }
    };


    Handler timerCicleReadAllParameters = new Handler();
    Runnable runnableCicleReadAllParameters = new Runnable() {
        @Override
        public void run() {
           byte[] message = set_strRxTx("Tx",mipi.sendReadAllParametrs());
            String str = "";
            for (int i = 0; i < message.length; i++) {
                str += String.format("%02X ", (byte) (message[i] & 0xFF));
            }
            Log.d("TIMER_Tx",str);
            timerCicleReadAllParameters.postDelayed(this, 2000);
        }
    };

    Handler timerCicleSearchAddress = new Handler();
    Runnable runnableCicleSearchAddress = new Runnable() {
        @Override
        public void run() {
            if (count == 248){
                count = 0;timerCicleSearchAddress.removeCallbacks(runnableCicleSearchAddress);
                set_strRxTx("Message","ПРИБОР <МИП-1И(2И,3И)> НЕ ОБНАРУЖЕН...");
                mipiViewModel.setTextViewSearchAaddress(ADDRESS_DEVICE+count);
                //mipiViewModel.setTextViewSearchAaddress("ПРИБОР <МИП-1И(2И)> НЕ ОБНАРУЖЕН...");
                return;
            }//отключаем таймер
            byte[] message = set_strRxTx("Tx",mipi.searchAddressDevice(searchAddress()));
            mipiViewModel.setTextViewSearchAaddress(ADDRESS_DEVICE+count);
            //Log.d("TIMER_Tx",byteToString(message));
            timerCicleSearchAddress.postDelayed(this, 150);
        }
    };
    String string = "ОБНАРУЖЕН ПРИБОР <МИП-2И - V2> ПО АДРЕСУ 068";

    String byteToString(byte[] message){
        String str = "";
        for (int i = 0; i < message.length; i++) {
            str += String.format("%02X ", (byte) (message[i] & 0xFF));
        }
        return str;
    }

    int searchAddress(){
        count++;
        return count;
    }

    String id_to_string (byte[] id){
      int i = bytesToInteger(id);
      String str = "";
      switch (i){
          case 0x13: str = "ОБНАРУЖЕН ПРИБОР <МИП-1И - V2>"; break;
          case 0x14: str = "ОБНАРУЖЕН ПРИБОР <МИП-2И - V2>"; break;
          case 0x15: str = "ОБНАРУЖЕН ПРИБОР <МИП-3И - V2>"; break;
      }
      return str;
    }


}