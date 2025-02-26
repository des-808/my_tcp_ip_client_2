package com.example.tcp_ip_client_2.ui.tcpip.tcp_ip;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.TCPCommunicator;
import com.example.tcp_ip_client_2.adapter.ChatMessageAdapter;
import com.example.tcp_ip_client_2.classs.ServerAdress;
import com.example.tcp_ip_client_2.classs.ChatModel;
import com.example.tcp_ip_client_2.classs.MessageTime;
import com.example.tcp_ip_client_2.databinding.FragmentTcpIpBinding;
import com.example.tcp_ip_client_2.db.DBChatAdapter;
import com.example.tcp_ip_client_2.db.DBChatHelper;
import com.example.tcp_ip_client_2.interfaces.TCPListener;
import com.example.tcp_ip_client_2.interfaces.onFragment_TCP_IP_Init;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TCP_IP_Fragment extends Fragment
implements  TCPListener,
            onFragment_TCP_IP_Init{
    private OnFragmentInteractionListener mListener;
    private onFragment_TCP_IP_Init mFragmentTCPIPInit;
    //View view;
    private static final String PREFS_FILE = "com.example.tcp_ip_client_2_preferences";
    private static final String KEY_ANDROMEDA = "EditMode";
    private static final int PREFS_MODE = MODE_PRIVATE;//Context.MODE_PRIVATE;
    private static final String FRAGMENT_NAME = "TCP_IP_Fragment";
   // SharedPreferences sharedPreferencesMain;
    private FragmentTcpIpBinding binding;
    View root;
    private static final String LOG_TAG = "LOG_TAG" ;
    private List<ChatModel> chatsList = new ArrayList<>();
    private RecyclerView recyclerView;//создаем переменную для отображения сообщений
    private Bundle savedInstanceState;

    DBChatAdapter db_chat_Adapter; //создаем переменную для работы с базой данных
    ChatMessageAdapter chatMessageAdapter;//создаем переменную для работы с clickable
    private Vibrator vibrator;
    MenuItem connection_menu_item;

    private final boolean IN_MSG = false;
    private final boolean OUT_MSG = true;
    final int MENU_RENAME = 1;
    final int MENU_DELETE = 2;
    //final int MENU_CANCEL = 3;
    final int ACK = 6;
    final int NACK = 21;
    final int SUCCESSFULLY = 0x14;
    public boolean connectToServer = false;
    public String mMessage;
    public Toast toast;

    private TCPIPViewModel tcpIpViewModel;
    //public String table_name, ipAdr, port;
    private AlertDialog dialog;
    private final Handler UIHandler = new Handler();
   // public MenuItem menu_switch_btn;
    //public MenuItem menu_clearChat = menu.findItem(R.id.clearChat);

    public MenuItem menu_switch_btn_;
    public MenuItem menu_clearChat_;


    public EditText object,clas,razd,schs;
    public ImageButton btnSend_tx;
    SharedPreferences settings;
    public TCP_IP_Fragment() {}


    // TODO: Rename and change types and number of parameters
    //public static TCP_IP_Fragment newInstance(String param1, String param2, String param3) {
    public static TCP_IP_Fragment newInstance(int number) {
        Bundle args = new Bundle();
        TCP_IP_Fragment fragment = new TCP_IP_Fragment();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        /*if (context instanceof onFragment_TCP_IP_Init){
            mFragmentTCPIPInit = (onFragment_TCP_IP_Init) context;
        }*/
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context + " must implement OnFragmentInteractionListener" );
        }*/
        //Log.d(LOG_TAG, "onAttach FragmentTCP_IP");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate( savedInstanceState );
        //setRetainInstance(true);
        setHasOptionsMenu(true);
        //if (getArguments() != null) {}
    }

    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tcpIpViewModel = new ViewModelProvider(this).get(TCPIPViewModel.class);
        binding = FragmentTcpIpBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        settings = Objects.requireNonNull(getActivity()).getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        db_chat_Adapter = new DBChatAdapter(getActivity());
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        return root;
    }



    @Override
    public void onStart() {
        super.onStart();
        on_FragmentTCP_IP_Init();
        chatMessageOrChatAndromedaLayout();
        //Log.d(LOG_TAG, "onStart FragmentTCP_IP");
        //========================
        ImageButton btn_sendChatTx = getView().findViewById(R.id.sendChatTx);
        if(btn_sendChatTx != null){
            btn_sendChatTx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    sendChatTx();//отправляем сообщение на сервер
                }
            });
        }
        ImageButton btn_sendTx = getView().findViewById(R.id.btnSendTx);
        if(btn_sendTx != null){
            btn_sendTx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    sendTx();//отправляем сообщение на сервер
                }
            });
        }
        //========================
    }

    @Override
    public void on_FragmentTCP_IP_Init() {
        //сработает когда запустится фрагмент fragment_tcp_ip
        String xparam = ServerAdress.getName() +"  |  "+ ServerAdress.getIp() + ":" + ServerAdress.getPort();
        TextView x = getActivity().findViewById( R.id.connect_text );
        x.setText( xparam );
        object = getActivity().findViewById(R.id.editObjekt);
        clas = getActivity().findViewById(R.id.editClass);
        razd = getActivity().findViewById(R.id.editRazd);
        schs = getActivity().findViewById(R.id.editSchs);
        btnSend_tx = getActivity().findViewById(R.id.btnSendTx);
        // начальная инициализация списка
        initRecyclerView();
        clearRecyclerView();
        tcpIpViewModel.addChatMessages(chatsList);
        tcpIpViewModel.addChatMessages(db_chat_Adapter.getMessages());//добавляем сообщения из БД в список
        if(menu_clearChat_ !=null) menu_clearChat_.setVisible(true);
        ConnectToServer();
        if (!db_chat_Adapter.getMessages().isEmpty()) {
            //chatsList.addAll(db_chat_Adapter.getMessages());
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                }
            });
        }
//package:mine
    }

    private  void initRecyclerView(){
        recyclerView = root.findViewById(R.id.list_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(tcpIpViewModel.getChatMessageAdapter());
    }
    private void clearRecyclerView() {
        tcpIpViewModel.clearList();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            tost("onButtonPressed");
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        menu_clearChat_.setVisible(false);
        //Log.d(LOG_TAG, "onDeath FragmentTCP_IP");
        menu_clearChat_.setVisible(false);//скрываем кнопку удаления чата
        DisconnectToServer();
    }

    @Override
    public void on_FragmentTCP_IP_Disconnect(){}


    /*TODO
     * создаём или добавляем таблицу в базу
     * */
    public void ConnectToTable(){
        DBChatHelper.setTableName(ServerAdress.getName());
        db_chat_Adapter.createTableIfNotExists();
    }



    @Override
    public void onTCPMessageRecievedInt(Integer inMsgInt) {}
    @Override
    public void onTCPMessageRecievedChar(char inMsgChar) {}
    @Override
    public void onTCPMessageRecievedByteBuffer(char[] inMsgByteBuffer, int count, int len) {}

    @Override
    public void onTCPMessageRecieved(String message) {
        // TODO Auto-generated method stub
        final String theMessage = message;
        mMessage = message;
        getActivity().runOnUiThread(() -> {
            //tost("onTCPMessageRecieved: "+theMessage);
            addChatMessage(theMessage,IN_MSG);
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void chatMessageOrChatAndromedaLayout(){
        View newView = null;
        ViewGroup fragmentLayout = (ViewGroup) getView().findViewById(R.id.fragmentLayout);
        ViewGroup.LayoutParams params = root.getLayoutParams();
        fragmentLayout.removeAllViews();
        // Проверяем, существует ли ключ KEY_ANDROMEDA в файле PREFS_FILE
        if (settings.contains(KEY_ANDROMEDA)) {
            // Если ключ существует
            Log.d(LOG_TAG, MessageFormat.format("{0}.{1} -> {2}", FRAGMENT_NAME, KEY_ANDROMEDA, settings.getString(KEY_ANDROMEDA, "text")));
            String EditMode = settings.getString(KEY_ANDROMEDA, "text");
            if(EditMode.equals("text") )
            {// режим текст
                newView = getLayoutInflater().inflate(R.layout.row_chat_send, null);
            }
            else if(EditMode.equals("hex") )
            {// режим hex
                newView = getLayoutInflater().inflate(R.layout.row_chat_send, null);
            }
            else if(EditMode.equals("andromeda") )
            {// режим андромеда
                newView = getLayoutInflater().inflate(R.layout.row_andromeda_send, null);
            }
        } else {
            newView = getLayoutInflater().inflate(R.layout.row_andromeda_send, null);
            // Если ключ не существует, выводим сообщение об ошибке
            Log.e(LOG_TAG, "Ключ " + KEY_ANDROMEDA + " не найден в файле " + PREFS_FILE);
            //return;
        }
        newView.setLayoutParams(params);
        fragmentLayout.addView(newView);
    }

    /*TODO
     *  String i - текст сообщения
     *  boolean in_out - true - исходящие сообщения output = true
     *  boolean in_out - false - входящие сообщения input = false
     * */
    /*public void addChatMessage(String i ,boolean in_out){
        ChatModel chat = new ChatModel(in_out);//создаем переменную для отображения сообщений в бд
        chat.setMessage_time(MessageTime.getTime());
        if (!in_out){//true - входящие сообщения
            chat.setMessage_in(i);
        }else{//false - исходящие сообщения
            chat.setMessage_out(i);
        }
        long insert = db_chat_Adapter.addDBMessage(chat);//сохраняем сообщение в БД
        chat.setId((int) insert);
        //Log.d(LOG_TAG, "AddMessage id = " + insert);
        chatsList.add(chat);//выводим сообщение в список
        refreshChatListView();
    }*/



    private void setupDialog() {//в место устаревшего ProgressDialog
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        ProgressBar progressBar = new ProgressBar(getActivity());
        layout.setPadding(50,50,0,40);
        progressBar.setIndeterminate(true);
        layout.addView(progressBar);

        TextView textView = new TextView(getActivity());
        textView.setText(R.string.please_wait);
        textView.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
        textView.setTextSize(14);
        textView.setPadding(50,50,0,40);
        layout.addView(textView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.connecting);
        builder.setCancelable(true);
        builder.setView(layout);

        dialog = builder.create();
        dialog.show();
    }

    /*TODO
     *  String i - текст сообщения
     *  boolean in_out - true - исходящие сообщения output = true
     *  boolean in_out - false - входящие сообщения input = false
     * */
    public void addChatMessage(String i ,boolean in_out){
        ChatModel chatModel = new ChatModel(in_out);//создаем переменную для отображения сообщений в бд
        chatModel.setMessage_time(MessageTime.getTime());
        if (!in_out){//true - входящие сообщения
            chatModel.setMessage_in(i);
        }else{//false - исходящие сообщения
            chatModel.setMessage_out(i);
        }
        long insert = db_chat_Adapter.addDBMessage(chatModel);//сохраняем сообщение в БД
        chatModel.setId((int) insert);
        //Log.d(LOG_TAG, "AddMessage id = " + insert);
        tcpIpViewModel.addChatMessage(chatModel);//выводим сообщение в список
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        });
    }

    @SuppressLint("UseRequireInsteadOfGet")
    public void sendTx()  {
        vibrator.vibrate(100);
        object = Objects.requireNonNull(getActivity()).findViewById(R.id.editObjekt);
        clas = getActivity().findViewById(R.id.editClass);
        razd = getActivity().findViewById(R.id.editRazd);
        schs = getActivity().findViewById(R.id.editSchs);
        String e_object = object.getText().toString();
        String e_clas = clas.getText().toString();
        String e_razd = razd.getText().toString();
        String e_schs = schs.getText().toString();
        String E_text = e_object + e_clas + e_razd + e_schs;

        String x = "5032 18";
        char i = (char) 20;
        E_text = x + E_text + i;//
        if (TCPCommunicator.writeToSocket(E_text, UIHandler, getActivity()) == TCPCommunicator.TCPWriterErrors.OK) {
            addChatMessage(E_text,OUT_MSG);
        } else {
            tostShort("Ошибка передачи сообщения");
        }
    }

    public void sendChatTx()  {
        vibrator.vibrate(100);
        EditText E_Send = getActivity().findViewById( R.id.EChat_Send );
        String E_text = E_Send.getText().toString();
        if(E_text.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter text", Toast.LENGTH_SHORT).show();
            return;
        }
        E_Send.setText( "" );
        if (TCPCommunicator.writeToSocket(E_text, UIHandler, getActivity()) == TCPCommunicator.TCPWriterErrors.OK) {
            addChatMessage(E_text,OUT_MSG);
        } else {
            tostShort("Ошибка передачи сообщения");
        }
    }

    //###################################################################################################
    public void tost(String msg) {
        toast = Toast.makeText( getActivity(), msg, Toast.LENGTH_LONG);
        toast.show();
    }
    public void tostShort(String msg) {
        toast = Toast.makeText( getActivity(), msg, Toast.LENGTH_SHORT );
        toast.show();
    }
    //###################################################################################################
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tcp_ip, menu);
        menu_clearChat_ = menu.findItem(R.id.clearChat);
        menu_switch_btn_ = menu.findItem(R.id.action_Connect_Disconnect_TCP_IP);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }


    @Override
    public void onTCPConnectionStatusChanged(boolean isConnectedNow) {
        if(isConnectedNow)
        {
            getActivity().runOnUiThread(() -> {
                dialog.hide();
                connectToServer = true;
                //invalidateOptionsMenu();
                menu_switch_btn_.setIcon(R.drawable.ic_podkl);
            });
        }
    }

    private void ConnectToServer() {
        if(!connectToServer) {
            setupDialog();
            TCPCommunicator tcpClient = TCPCommunicator.getInstance();
            TCPCommunicator.addListener( this );
            if ( tcpClient.init(
                    ServerAdress.getIp(),
                    ServerAdress.getPortParseInt())== TCPCommunicator.TCPWriterErrors.UnknownHostException ) {
                DisconnectToServer();
            }
        }
    }

    private void DisconnectToServer(){
        if(TCPCommunicator.getInstance()!= null){
            TCPCommunicator.closeStreams();
            TCPCommunicator.removeAllListeners();
            connectToServer = false;
            menu_switch_btn_.setIcon(R.drawable.ic_otkl);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int clearChat = R.id.clearChat;
        final int connect_disconnect = R.id.action_Connect_Disconnect_TCP_IP;
        int id = item.getItemId();
        //final int action_settings = R.id.action_settings;
        /*if(id==action_settings) {
            DisconnectToServer();
            //Navigation.findNavController(root).navigate(R.id.root_preference);
        }*/
        if(id==connect_disconnect) {
            if(connectToServer) {
                DisconnectToServer();
            }else{
                ConnectToServer();
            }
        }
        if( clearChat == id) {
            db_chat_Adapter.deleteMesagesAllChat();
            db_chat_Adapter.createTableIfNotExists();
            clearRecyclerView();
           // return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
