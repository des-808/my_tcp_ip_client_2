package com.example.tcp_ip_client_2.ui.bluetooth;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.classs.Nord;
import com.example.tcp_ip_client_2.classs.Swith_Terminal;
import com.hoho.android.usbserial.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KeyboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KeyboardFragment extends Fragment {
    private enum BluetoothPermission { Unknown, Requested, Granted, Denied }
    //private static final String INTENT_ACTION_GRANT_BLUETOOTH = BuildConfig.APPLICATION_ID + ".GRANT_USB";
    private static final String INTENT_ACTION_GRANT_BLUETOOTH = BuildConfig.LIBRARY_PACKAGE_NAME + ".GRANT_BLUETOOTH";
    private static final int WRITE_WAIT_MILLIS = 2000;
    private static final int READ_WAIT_MILLIS = 2000;
    private KeyboardFragment.BluetoothPermission bluetoothPermission = KeyboardFragment.BluetoothPermission.Unknown;
    private final BroadcastReceiver broadcastReceiver;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;

    private String mConnectedDeviceName = null;
    //private ArrayAdapter<String> mConversationArrayAdapter;
    private StringBuffer mOutStringBuffer;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;

    private StringBuilder sb = new StringBuilder();

    public TextView keyboardStr1;
    public TextView keyboardStr2;
    public TextView keyboardStr3;
    public ImageView avariya;
    public ImageView security;
    public String lenghtString = "";
    //==============================================================================================
    //==============================================================================================
    public static boolean nord = false;
    public static boolean migalka_security = false;
    public static boolean migalka_avariya = false;
    public static boolean migalka_security_thread = true;
    public static boolean migalka_avariya_thread = true;
    public static boolean disconnect_to_socket_bool =false;

    public static final byte startInd1 = (byte)0xFE;//-2;//0xFE;дежурный режим клавиатуры
    public static final byte startInd2 = (byte)0xF9;//-7;//0xF9;режим настроек прибора
    //public static final byte stopIndStartPrib = (byte)0x00;//
    public static final byte stopInd1 = (byte)0x2E;
    public static final byte stopInd2 = (byte)0x0C;//когда всё в норме
    public static final byte stopInd3 = (byte)0x04;//снятие с охраны кодом техника когда нету неисправности
    public static final byte stopInd4 = (byte)0x06;//снятие с охраны кодом техника когда есть неисправность
    public static final byte stopInd5 = (byte)0x02;//снятие с охраны кодом техника когда есть неисправность при откл 220 и откл акб. появляется при предпрогр епром
    public static final byte stopInd6 = (byte)0x0E;//при откл акб
    public static final byte stopInd7 = (byte)0x08;//при откл 220
    public static final byte stopInd8 = (byte)0x0A;//при откл 220 и откл акб
    //public static final byte stopInd9 =(byte)0xF8;//-8;//0xF8;

    public static final byte securityLed_postavl_na_ohrany_sost_bez_avarii    = (byte)0x04;//0x04;
    public static final byte securityLed_postavl_na_ohrany_sost_s_avariey     = (byte)0x06;//0x06;
    public static final byte    securityLed_zaderjka_bez_avarii               = (byte)0xCC;//0xCC;
    public static final byte    securityLed_zaderjka_s_avariey                = (byte)0xCE;//0xCE;
    public static final byte    securityLed_end_zaderjka_bez_avarii           = (byte)0xC4;//0xC4;
    public static final byte    securityLed_end_zaderjka_s_avariey            = (byte)0xC6;//0xC6;
    //public static final byte    securityLed_pri_sniatii_s_ohrany_bez_avarii   = (byte)0x80;//0x80;
    //public static final byte    securityLed_pri_sniatii_s_ohrany_s_avariey    = (byte)0x82;//0x82;
    public static final byte    securityLed_sniyato_s_ohrany_bez_avarii       = (byte)0x00;//0x00;
    public static final byte    securityLed_sniyato_s_ohrany_s_avariey        = (byte)0x02;//0x02;
    public static final byte    securityLed_otkaz_vziatiya_bez_avarii         = (byte)0x40;//0x40;
    public static final byte    securityLed_otkaz_vziatiya_s_avariey          = (byte)0x42;//0x42;

    public static final int dlinnaMassiva = 64;
    //public  byte[] bytebuf      = new byte[dlinnaMassiva];
    public static byte comand   = (byte)0x00;
    public static byte data     = (byte)0x01;
    byte[]         byteBuferr   = new byte[dlinnaMassiva];
    public  byte   startBit     = (byte)0x24;//0xF1
    public  byte   stopBit      = (byte)0x3B;//0xF3
    //    public  byte   startBit     = (byte)0xF1;//0x24
//    public  byte   stopBit      = (byte)0xF3;//0x3B
    private Menu menu;
    public  boolean karetkaMig = true;
    public char ch = (byte) 0x23;
    //==============================================================================================
    //==============================================================================================
    byte[] buffer = new byte[128];
    int bytesArray = 0;
    boolean startbit = true;
    Nord nordParser = new Nord();



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MenuItem menu_clearChat;
    private MenuItem menu_bluetooth_search_to_connect_btn;

    /* private Handler mHandler;*/

    public KeyboardFragment() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(INTENT_ACTION_GRANT_BLUETOOTH.equals(intent.getAction())) {
                    bluetoothPermission = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                            ? KeyboardFragment.BluetoothPermission.Granted : KeyboardFragment.BluetoothPermission.Denied;
                    //connect();
                }
            }
        };
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KeyboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KeyboardFragment newInstance(String param1, String param2) {
        KeyboardFragment fragment = new KeyboardFragment();
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
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        nord = true;
        // Если адаптер нулевой, значит Bluetooth не поддерживается
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Swith_Terminal.setNord();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH}, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keyboard, container, false);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // Когда DeviceListActivity возвращается с устройством для подключения
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;

            case REQUEST_ENABLE_BT:
                // Когда запрос на включение Bluetooth возвращается
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth теперь включен, поэтому настройте сеанс чата
                    setupChat();
                } else {
                    // Пользователь не включил Bluetooth или произошла ошибка
                    //.d(TAG, "BT not enabled");
                    //Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    //getActivity().finish();
                }
        }
    }

    private void setupChat() {
//        Log.d(TAG, "setupChat()");

        // Инициализируйте адаптер массива для цепочки бесед
        // mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);
        // mConversationView.setAdapter(mConversationArrayAdapter);
        // Initialize the compose field with a listener for the return key
        //  mOutEditText.setOnEditorActionListener(mWriteListener);
        // Initialize the send button with a listener that for click events
        //   mSendButton.setOnClickListener(new View.OnClickListener() {
        //       public void onClick(View v) {
        //           // Отправить сообщение, используя содержимое виджета редактирования текста
        //           View view = getView();
        //           if (null != view) {
        //               TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
        //               String message = textView.getText().toString();
        //               sendMessage(message);
        //           }
        //       }
        //   });
        //инициализировать BluetoothChatService для выполнения соединений Bluetooth
        mChatService = new BluetoothChatService(getActivity(), mHandler);
        // Инициализировать буфер для исходящих сообщений
        mOutStringBuffer = new StringBuffer("");
    }



    private void sendMessager(String message) {
        // Проверяем, что мы действительно подключены, прежде чем пытаться что-либо
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверяем, действительно ли есть что отправить
        if (message.length() > 0) {
            // Получение байтов сообщения и указание BluetoothChatService написать
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // Если действие является событием нажатия клавиши возврата, отправьте сообщение
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessager(message );
            }
            return true;
        }
    };
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void connectDevice(Intent data) {
        // Получаем MAC-адрес устройства
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Получаем объект BluetoothDevice
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Попытка подключиться к устройству
        mChatService.connect(device);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) { setupChat(); }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* if (mBluetoothAdapter != null)
            mBluetoothAdapter.close();*/
        if (mChatService != null) { mChatService.stop(); }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Выполнение этой проверки в onResume () охватывает случай, когда BT был
        // не включен во время onStart (), поэтому мы были приостановлены, чтобы включить его ...
        // onResume () будет вызываться при возврате активности ACTION_REQUEST_ENABLE.
        if (mChatService != null) {
            // Только если состояние STATE_NONE, мы знаем, что мы еще не начали
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Запускаем службы чата Bluetooth
                mChatService.start();
            }
        }
    }



    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        keyboardStr1 = (TextView) view.findViewById(R.id.keyboardStr1);
        keyboardStr2 = (TextView) view.findViewById(R.id.keyboardStr2);
        keyboardStr3 = (TextView) view.findViewById(R.id.keyboardStr3);

        avariya =        (ImageView)view.findViewById(R.id.alertLed);
        security =     (ImageView)view.findViewById(R.id.securityLed);

        Button btn_0 = (Button) view.findViewById(R.id.btn_0);
        Button btn_1 = (Button) view.findViewById(R.id.btn_1);
        Button btn_2 = (Button) view.findViewById(R.id.btn_2);
        Button btn_3 = (Button) view.findViewById(R.id.btn_3);
        Button btn_4 = (Button) view.findViewById(R.id.btn_4);
        Button btn_5 = (Button) view.findViewById(R.id.btn_5);
        Button btn_6 = (Button) view.findViewById(R.id.btn_6);
        Button btn_7 = (Button) view.findViewById(R.id.btn_7);
        Button btn_8 = (Button) view.findViewById(R.id.btn_8);
        Button btn_9 = (Button) view.findViewById(R.id.btn_9);
        Button btn_zv = (Button) view.findViewById(R.id.btn_zv);
        Button btn_resh = (Button) view.findViewById(R.id.btn_resh);
        Button btn_end = (Button) view.findViewById(R.id.btn_end);
        Button btn_next = (Button) view.findViewById(R.id.btn_next);
        Button btn_back = (Button) view.findViewById(R.id.btn_back);
        Button btn_enter = (Button) view.findViewById(R.id.btn_enter);

        btn_0.setOnClickListener(oclBtn);
        btn_1.setOnClickListener(oclBtn);
        btn_2.setOnClickListener(oclBtn);
        btn_3.setOnClickListener(oclBtn);
        btn_4.setOnClickListener(oclBtn);
        btn_5.setOnClickListener(oclBtn);
        btn_6.setOnClickListener(oclBtn);
        btn_7.setOnClickListener(oclBtn);
        btn_8.setOnClickListener(oclBtn);
        btn_9.setOnClickListener(oclBtn);
        btn_zv.setOnClickListener(oclBtn);
        btn_resh.setOnClickListener(oclBtn);
        btn_end.setOnClickListener(oclBtn);
        btn_next.setOnClickListener(oclBtn);
        btn_back.setOnClickListener(oclBtn);
        btn_enter.setOnClickListener(oclBtn);
    }

    View.OnClickListener oclBtn = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            int id = v.getId();
            // по id определеяем кнопку, вызвавшую этот обработчик
            if (id == R.id.btn_1) {byteBuferr[0] = (byte) 0x01;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_2)      {byteBuferr[0] = (byte) 0x02;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_3)      {byteBuferr[0] = (byte) 0x03;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_4)      {byteBuferr[0] = (byte) 0x04;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_5)      {byteBuferr[0] = (byte) 0x05;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_6)      {byteBuferr[0] = (byte) 0x06;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_7)      {byteBuferr[0] = (byte) 0x07;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_8)      {byteBuferr[0] = (byte) 0x08;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_9)      {byteBuferr[0] = (byte) 0x09;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_0)      {byteBuferr[0] = (byte) 0x0A;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_zv)     {byteBuferr[0] = (byte) 0x0B;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_resh)   {byteBuferr[0] = (byte) 0x0C;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_end)    {byteBuferr[0] = (byte) 0x16;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_next)   {byteBuferr[0] = (byte) 0x18;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_back)   {byteBuferr[0] = (byte) 0x17;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
            else if (id == R.id.btn_enter)  {byteBuferr[0] = (byte) 0x15;sendMessage(bufferOutMessage(data, byteBuferr));vibrates();}
        }
    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_keyboard_bluetooth, menu);
        //menu_clearChat = menu.findItem(R.id.clearChatBt);
        menu_bluetooth_search_to_connect_btn = menu.findItem(R.id.menu_bluetooth_search_to_connect);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_bluetooth_search_to_connect) {
            //tostString("здрасте я терминал");
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        }
        if (id == R.id.stop_connect) {
            //resetEsp32();//отправить команду reset для esp32
            stop_avariya_led();
            stop_security_led();
            disconnect_to_socket_bool = true;
            migalka_avariya_thread = false;
            migalka_security_thread = false;
            if (mChatService != null) {
                mChatService.stop();
            }
            String xz = "";
            keyboardStr3.setText(xz);
            disconnect_to_socket_led_all();
            return true;
        }
        if (id == R.id.nord_on) {//"Nord On Lin Off"
            Swith_Terminal.setNord();
            byteBuferr[0] = (byte)0x30;
            sendMessage(bufferOutMessage(comand, byteBuferr));
            vibrates();
            return true;
        }
        if (id == R.id.lin_on) {//"Nord Off Lin On";
            Swith_Terminal.setLin();
            byteBuferr[0] = (byte)0x31;
            //sendMessage(bufferOutMessage(data,"abrakadabra epta"));
            sendMessage(bufferOutMessage(comand, byteBuferr));
            vibrates();
            return true;
        }
        if (id == R.id.serial1_to_bt) {//"Serial1 to Bluetooth"
//                if (item.isChecked()) {item.setChecked(false);btCheck = false;}
//                else {item.setChecked(true);btCheck = true;}
//                if ((btCheck) && (serialCheck)) {byteBuferr[0] = (byte)0x34;}
//                if ((btCheck) && (!serialCheck)) {byteBuferr[0] = (byte)0x32;}
//                if ((!btCheck) && (!serialCheck)) {byteBuferr[0] = (byte)0x00;}
            byteBuferr[0] = (byte)0x32;
            sendMessage(bufferOutMessage(comand, byteBuferr));
            vibrates();
            return true;
        }
        if (id == R.id.serial1_to_serial) {//"Serial1 to Serial"
//               if (item.isChecked()) {item.setChecked(false);serialCheck = false;}
//               else {item.setChecked(true);serialCheck = true;}
//               if ((serialCheck) && (btCheck)) {byteBuferr[0] = (byte)0x34;}
//               if ((serialCheck) && (!btCheck)) {byteBuferr[0] = (byte)0x33;}
//               if ((!serialCheck) && (!btCheck)) {byteBuferr[0] = (byte)0x00;}

            byteBuferr[0] = (byte)0x33;
            sendMessage(bufferOutMessage(comand,byteBuferr));
            //sendMessage(bufferOutMessage(data,"abrakadabra"));
            vibrates();
            return true;
        }
//            case R.id.bytes_to_hex: {
//                String s = "byte_to_hex";
//                //sendMessage(s);
//                return true;
//            }
//            case R.id.bytes_to_string: {
//                String s = "byte_to_string";
//                //sendMessage(s);
//                return true;
//            }
        return false;
    }


    private void sendMessage(byte[] bytebuf) {
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // Убедитесь, что действительно есть что отправить
        //if (bytebuf[] == 0) {
        // Получите байты сообщения и скажите BluetoothChatService написать
        //byte[] send = message.getBytes();
        mChatService.write(bytebuf);
        //}
    }

    public byte[] bufferOutMessage(byte commandElseDataFlag, byte[] byteBufIn ) {
        byte[] bytebuf  = new byte[dlinnaMassiva];  // максимальная длинна 64 байта
        int dlinnaByteBuf = 0;//byteBufIn.length;
        for (int x = 0;x < 64; ++x){
            if ((byteBufIn[x] == 0)&&(byteBufIn[x+1] == 0)&&(byteBufIn[x+2] == 0)&&(byteBufIn[x+3] == 0)&&(byteBufIn[x+4] == 0)){
                dlinnaByteBuf=x;
                break;
            }
        }
        int dlinnaSoobsheniya = 3+dlinnaByteBuf+2+1;
        //Charset cset = Charset.forName("CP866");
        //String readMessage = new String(readBuf, cset);// построить строку из допустимых байтов в буфере в кодировке cp866
        bytebuf[0] = startBit;
        bytebuf[1] = (byte)dlinnaSoobsheniya;
        bytebuf[2] = commandElseDataFlag;
        int z = 3;
        int crc = 32;
        for(int i = 0; i<dlinnaByteBuf; ++i){
            bytebuf[z] = byteBufIn[i]  ;
            z++;
        }
        bytebuf[z] = (byte)crc;z++;
        bytebuf[z] = (byte)crc;z++;
        bytebuf[z] = stopBit;
        //z = 0;
        return bytebuf;
    }

    public byte[] bufferOutMessage(byte commandElseDataFlag, String stringBufIn) {
        byte[] bytebuf  = new byte[dlinnaMassiva];  // максимальная длинна 64 байта
        int dlinnaStroki = stringBufIn.length();
        int dlinnaSoobsheniya = 3+dlinnaStroki+2+1;
        //Charset cset = Charset.forName("CP866");
        bytebuf[0] = startBit;
        bytebuf[1] = (byte)dlinnaSoobsheniya;
        bytebuf[2] = commandElseDataFlag;
        int j = 0;
        int z = 3;
        int crc = 32;
        byte[] buter = stringBufIn.getBytes();
        for(int i = 0;i<dlinnaStroki; ++i){
            bytebuf[z] = buter[j]  ;
            j++;
            z++;
        }
        bytebuf[z] = (byte)crc;z++;
        bytebuf[z] = (byte)crc;z++;
        bytebuf[z] = stopBit;z++;
        j = 0;z = 0;
        return bytebuf;
    }

    public void vibrates() { vibrate(60); }
    public void vibrate(long duration) {
        Vibrator vibs = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibs.vibrate(duration);
    }

    public static byte IntToByte(int i){
        return (byte) (i & 0x000000ff);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String changeCharInPosition(int position, char ch, String str){
        char[] charArray = str.toCharArray();
        charArray[position]= ch;
        return new String(charArray);
    }

    public static String hexadecimal(String input, String charsetName) throws UnsupportedEncodingException {
        if (input == null) throw new NullPointerException();
        return bytesToHex(input.getBytes(charsetName));
    }

    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] buf) {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            //int in =  (buf[i] & 0xff);
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
            //chars[2 * i] = HEX_CHARS[(in& 0xF0) >>> 4];
            //chars[2 * i + 1] = HEX_CHARS[in & 0x0F];
        }
        return new String(chars);
    }

    public static byte[] hexStringToByteArray(String s) {
        try {

            int len = s.length();
            if(len>1) {
                byte[] data = new byte[len / 2];
                for (int i = 0 ; i < len ; i += 2) {
                    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                            + Character.digit(s.charAt(i + 1), 16));
                }
                return data;
            }
            else

            {
                return  null;
            }
        }catch (Exception e)
        {
            throw e;
        }
    }

    // Global Charset Encoding
    public static Charset encodingType = StandardCharsets.UTF_8;

    // Text To Hex
    public static String textToHex(String text)
    {
        byte[] buf = null;
        buf = text.getBytes(encodingType);
        char[] HEX_CHARSS = "0123456789ABCDEF".toCharArray();
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARSS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARSS[buf[i] & 0x0F];
        }
        return new String(chars);
    }
    public static String hexToText(String hex)
    {
        int l = hex.length();
        byte[] data = new byte[l / 2];
        for (int i = 0; i < l; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        String st = new String(data, encodingType);
        return st;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public void migalka_security_led() {
        if (migalka_security) {
            security.setImageResource(android.R.drawable.presence_online);
            migalka_security = false;
        } else {
            security.setImageResource(android.R.drawable.presence_invisible);
            migalka_security = true;
        }
    }
    public void migalka_avariya_led() {
        if (migalka_avariya) {
            avariya.setImageResource(android.R.drawable.ic_notification_overlay);
            migalka_avariya = false;
        } else {
            avariya.setImageResource(android.R.drawable.presence_invisible);
            migalka_avariya = true;
        }
    }

    public void disconnect_to_socket_led_all(){
        if(disconnect_to_socket_bool) {
            security.setImageResource(android.R.drawable.presence_invisible);
            avariya.setImageResource(android.R.drawable.presence_invisible);
            disconnect_to_socket_bool= false;
        }
    }
    private boolean isRunningAvariya = false;
    private final Handler ha_avariya_led = new Handler();

    private void start_avariya_led() {
        isRunningAvariya = true;
        ha_avariya_led.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                migalka_avariya_led();
                if (isRunningAvariya) {
                    ha_avariya_led.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void stop_avariya_led() {
        isRunningAvariya = false;
        ha_avariya_led.removeCallbacksAndMessages(null);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean isRunningSecurity = false;
    private final Handler ha_security_led = new Handler();

    private void start_security_led() {
        isRunningSecurity = true;
        ha_security_led.postDelayed(new Runnable() {
            @Override
            public void run() {
                //call function
                migalka_security_led();
                if (isRunningSecurity) {
                    ha_security_led.postDelayed(this, 500);
                }
            }
        }, 500);
    }

    private void stop_security_led() {
        isRunningSecurity = false;
        ha_security_led.removeCallbacksAndMessages(null);
    }



    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to));
                            //Toast.makeText(getContext(), msg.arg2, Toast.LENGTH_SHORT).show();
                            //mConversationArrayAdapter.clear();

                            keyboardStr1.setText(" Connect ");
                            keyboardStr2.setText(" to socket ");
                            keyboardStr3.setText("");
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            //setStatus(getString(R.string.title_connecting));
                            disconnect_to_socket_bool= false;
                            migalka_avariya_thread = true;
                            migalka_security_thread = true;
                            //connectToScan.setIcon(getResources().getDrawable(R.drawable.ic_action_bluetooth_connected));
                            //toolbar.getMenu().findItem(R.id.connect_to_scan).setIcon(R.drawable.ic_action_bluetooth_connected);
//                            byteBuferr[0] = (byte)0x32;
//                            sendMessage(bufferOutMessage(comand, byteBuferr));
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            break;
                        case BluetoothChatService.STATE_NONE:
                            String no_connect = "Disconnect to socket";
                            String xxz = "";
                            keyboardStr1.setText(xxz);
                            keyboardStr2.setText(xxz);
                            keyboardStr3.setText(xxz);
                            //tostString( no_connect);
                            stop_avariya_led();
                            stop_security_led();
                            security.setImageResource(android.R.drawable.presence_invisible);
                            avariya.setImageResource(android.R.drawable.presence_invisible);
                            //connectToScan.setIcon(getResources().getDrawable(R.drawable.ic_action_device_access_bluetooth_searching));
                            //toolbar.getMenu().findItem(R.id.connect_to_scan).setIcon(R.drawable.ic_action_device_access_bluetooth_searching);
                            break;
                    }
                    break;

                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);// построить строку из буфера
                    break;

                case Constants.MESSAGE_READ:
                    byte[] readBuf = new byte[128];
                    // Send the obtained bytes to the UI Activity
                    if(nordParser.ressiveBuffer((byte[]) msg.obj,msg.arg1)){break;}
                    else{readBuf = nordParser.getBuffer();}
                    /*if (startbit) {
                        if ((buffer[bytesArray] == startInd1) || (buffer[bytesArray] == startInd2)) {
                            bytesArray = 1;
                            startbit = false;
                        }
                    } else {
                        if (//((buffer[bytesArray] == stopInd)) &&
                                (bytesArray == 40)) {
                            mHandler.obtainMessage(Constants.MESSAGE_READ, bytesArray, -1, buffer).sendToTarget();
                            readBuf = buffer;
                            bytesArray = 0;
                            startbit = true;
                        } else
                        {
                            bytesArray++;
                            break;
                        }
                    }*/

                   /* if(Swith_Terminal.isLin()){
                        Charset cset = Charset.forName("CP866");
                        String readMessage = new String(readBuf, cset);// построить строку из допустимых байтов в буфере в кодировке cp866
                        // readBuf
                        //String readMessage = new String(readBuf, 0, msg.arg1);
                        keyboardStr3.setText(readMessage);
                        break;
                    }*/
                    Charset cset = Charset.forName("CP866");
                    String readMessage = new String(readBuf, cset);// построить строку из допустимых байтов в буфере в кодировке cp866
                    //String readMessage = new String(readBuf, 0, msg.arg1);// построить строку из допустимых байтов в буфере
                    //Log.d(TAG, readMessage);
                    sb.append(readMessage); // формируем строку
                    lenghtString = "";
                    //Log.d(TAG, "мы тут бываем");
                    lenghtString = sb.substring(0, 41);

                    String simwol_sicurity = lenghtString.substring(33,34);
                    String polojenie_karetki = lenghtString.substring(34,35);
                    String simwol_oblPost = lenghtString.substring(39,40);
                    String simwol_neispravnosti = lenghtString.substring(40,41);
                    String sicurityAlarmStr = simwol_sicurity+polojenie_karetki+simwol_oblPost+simwol_neispravnosti;
                    byte simvolSicurity      = readBuf[33];
                    byte simvolKaretki       = readBuf[34];
                    byte simvolOblPost       = readBuf[39];
                    byte simvolNeispravnosti = readBuf[40];

                    if (simvolKaretki<((byte)0x21)) {
                        if (karetkaMig){
                            lenghtString = changeCharInPosition((byte)(simvolKaretki+1), ch, lenghtString);
                            karetkaMig = false;}
                        else{
                            karetkaMig = true;}
                    }
                    int z = 0;for (int x = 0;x < 64; ++x){ readBuf[x] = (byte) z; }
                    /////////////////////////////////////////////
                    //String str1 = lenghtString.substring(1, 17);keyboardStr1.setText(str1);
                    //String str2 = lenghtString.substring(17, 33);keyboardStr2.setText(str2);
                    keyboardStr1.setText(lenghtString.substring(1, 17));
                    keyboardStr2.setText(lenghtString.substring(17, 33));

                    if((simvolSicurity == securityLed_postavl_na_ohrany_sost_bez_avarii) || (simvolSicurity == securityLed_postavl_na_ohrany_sost_s_avariey) ){
                        security.setImageResource(android.R.drawable.presence_online);
                    }
                    if((simvolSicurity == securityLed_sniyato_s_ohrany_bez_avarii) || (simvolSicurity == securityLed_sniyato_s_ohrany_s_avariey) ){
                        security.setImageResource(android.R.drawable.presence_invisible);
                        stop_security_led();
                        migalka_security_thread = true;
                    }

                    if((simvolSicurity == securityLed_end_zaderjka_bez_avarii)|| (simvolSicurity == securityLed_end_zaderjka_s_avariey)){
                        //security.setImageResource(android.R.drawable.presence_offline);
                        //migalka_security_thread = true;
                        stop_security_led();
                        migalka_security_thread = true;
                    }

                    if(migalka_security_thread){
                        if(((simvolSicurity == securityLed_zaderjka_bez_avarii) || (simvolSicurity == securityLed_zaderjka_s_avariey))){
                            migalka_security_thread = false;
                            start_security_led();
                        }
                    }

                    if(migalka_avariya_thread){
                        if (((simvolNeispravnosti==stopInd1)||(simvolNeispravnosti==stopInd4)||(simvolNeispravnosti==stopInd5)||
                                (simvolNeispravnosti==stopInd6)||(simvolNeispravnosti==stopInd7)||(simvolNeispravnosti==stopInd8)||
                                (simvolNeispravnosti==stopInd8))){//&&(migalka_avariya_thread)

                            migalka_avariya_thread = false;
                            start_avariya_led();
                        }
                    }
                    if((simvolNeispravnosti==stopInd2)||
                            (simvolNeispravnosti==stopInd3)){
                        migalka_avariya_thread = true;
//                        avariya.setImageResource(android.R.drawable.presence_invisible);
                        stop_avariya_led();
                    }
                    //alert.setImageResource(android.R.drawable.ic_notification_overlay);
                    //security.setImageResource(android.R.drawable.presence_online);
                    //alert.setImageResource(android.R.drawable.presence_offline);
                    //security.setImageResource(android.R.drawable.presence_offline);
                    //String sicurityAlarmStr = String.valueOf(simvolSicurity+simvolAlarm);

                    try { sicurityAlarmStr = hexadecimal(sicurityAlarmStr, "CP866");}
                    catch (UnsupportedEncodingException e) { e.printStackTrace(); }
                    /////////////////////////////////////////////
                    String  StringToHexStart = lenghtString.substring(0, 1);
                    try { StringToHexStart = hexadecimal(StringToHexStart, "CP866");}
                    catch (UnsupportedEncodingException e) { e.printStackTrace();}
                    String  StringToHexStop = lenghtString.substring(33, 41);
                    try { StringToHexStop = hexadecimal(StringToHexStop, "CP866");}
                    catch (UnsupportedEncodingException e) { e.printStackTrace(); }
                    //Log.v(TAG, StringToHexStop);
                    String StringToHexNew = StringToHexStart + "__" + StringToHexStop+"<<>>"+sicurityAlarmStr;

                    keyboardStr3.setText(StringToHexNew);                    // обновляем TextView3*/
                    sb.delete(0, sb.length());                                      // и очищаем sb

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // сохранить имя подключенного устройства
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != getActivity()) {
                        Toast.makeText(getActivity(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != getActivity()) {
                        Toast.makeText(getActivity(), msg.getData().getString(Constants.TOAST), Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };



}