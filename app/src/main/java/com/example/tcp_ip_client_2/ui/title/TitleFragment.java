package com.example.tcp_ip_client_2.ui.title;




import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.adapter.ChatsTitleAdapter;
import com.example.tcp_ip_client_2.adapter.SearchPortAdapter;
import com.example.tcp_ip_client_2.classs.Adress;
import com.example.tcp_ip_client_2.classs.SearchPortItems;
import com.example.tcp_ip_client_2.classs.TitleChatsItems;
import com.example.tcp_ip_client_2.databinding.FragmentTitleBinding;
import com.example.tcp_ip_client_2.db.DBChatAdapter;
import com.example.tcp_ip_client_2.db.DBChatHelper;
import com.example.tcp_ip_client_2.db.DBManager;
import com.example.tcp_ip_client_2.interfaces.EnumsAndStatics;
import com.example.tcp_ip_client_2.interfaces.onListViewFragmentTitle;
import com.example.tcp_ip_client_2.interfaces.onStartFragmentTcpIp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TitleFragment extends Fragment {
    private onListViewFragmentTitle ONListViewFragmentTitle;
    private static final String LOG_TAG = "LOG_TAG" ;
    private Bundle savedInstanceState;
    private FragmentTitleBinding binding;
   // private MyClickCallback mClickCallback;
    //private OnFragmentItemClickListener mClickCallback;
    // private OnFragmentInteractionListener mListener;
    private onStartFragmentTcpIp OnStartFragmentTcpIp;
    //private static final String LOG_TAG = "LOG_TAG" ;

    private ChatsTitleAdapter C_Adapter;
    final int MENU_RENAME = 1;
    final int MENU_DELETE = 2;
    final int MENU_SEARCH_PORT = 3;
    //final int MENU_CANCEL = 4;
    final int ACK = 6;
    final int NACK = 21;
    final int SUCCESSFULLY = 0x14;
    public boolean connectToServer = false;
    public String mMessage;
    public Toast toast;
    public String table_name, ipadr, port;
    public TitleChatsItems items;
    ListView newlist;
    //private Bundle savedInstanceState;
    DBChatAdapter db_chat_Adapter; //создаем переменную для работы с базой данных
    View root;
    private static final String PREFS_FILE = "com.example.tcp_ip_client_2_preferences";

    public TitleFragment() {
        // Required empty public constructor
    }

   /* public TitleFragment(MyClickCallback clickCallback) {
        mClickCallback = clickCallback;
    }
    public interface MyClickCallback {
        void onClickSelected(int position);
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate( savedInstanceState );
    }

    @Override
    public void onStart() {
        super.onStart();
        //сработает когда запустится фрагмент fragment_title
        refreshTitleList();//перезагружает список
        registerForContextMenu( newlist ); //если  раньше запускать будет ошибка. фрагменты не мгновенно запускаются
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TitleViewModel titleViewModel = new ViewModelProvider(this).get(TitleViewModel.class);
        binding = FragmentTitleBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        db_chat_Adapter = new DBChatAdapter(getActivity());//создаём адаптер
        newlist = binding.list;
        newlist.setOnItemClickListener((arg0, arg1, position, arg3) -> onClickSelected( position ));
        FloatingActionButton btnAdd = (FloatingActionButton) root.findViewById(R.id.but_add);
        btnAdd.setOnClickListener(v1 -> {openAddDialog();});

       /* Button button = root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.tcpIpFragment);
            }
        });*/
        final TextView textView = binding.textTitle;
        titleViewModel.setmText( new MutableLiveData<String>("Server List"));
        titleViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@Nullable Context context) {
        super.onAttach( context );
        if (context instanceof onListViewFragmentTitle)
        {
            try {
                ONListViewFragmentTitle = (onListViewFragmentTitle) context;
            }catch (ClassCastException e)
            {
                throw new ClassCastException(context.toString() + " must implement ONListViewFragmentTitle");
            }
        }
        if (context instanceof onStartFragmentTcpIp)
        {
            try {
                OnStartFragmentTcpIp = (onStartFragmentTcpIp) context;
            }catch (ClassCastException e)
            {
                throw new ClassCastException(context.toString() + " must implement onStartFragmentTcpIp");
            }
        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info != null ? info.position : 0;
        items = C_Adapter.getItem( position );
        switch (item.getItemId()) {
            case MENU_RENAME:
                openRemoveDialog( items );
                break;
            case MENU_DELETE:
                openDeleteDialog( items );
                break;
            case MENU_SEARCH_PORT:
               openSearchPortDialog(items);
                break;
            //case MENU_CANCEL:break;
            default:
                break;
        }
        return super.onContextItemSelected( item );//false;//
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu( menu, v, menuInfo );
        menu.add( 0, MENU_RENAME, 0, "Редактировать" );
        menu.add( 0, MENU_DELETE, 0, "Удалить" );
        menu.add( 0, MENU_SEARCH_PORT, 0, "Поиск открытого порта" );
        //menu.add( 0, MENU_CANCEL, 0, "Отмена" );
    }


    @SuppressLint("UseRequireInsteadOfGet")
    private void refreshTitleList() {
        ExecutorService executor = Executors.newFixedThreadPool(14);
        ArrayList<TitleChatsItems> list = DBManager.getInstance( getActivity() ).getAllContacts();
        for (TitleChatsItems item : list) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        InetAddress.getByName(item.getIp_adr());
                        //System.out.println("Ip is available");
                        item.setIpOnline(true);
                    } catch (IOException e) {
                        //System.out.println("Ip is not available");
                        item.setIpOnline(false);
                    }
                    try {
                        Socket socket = new Socket(item.getIp_adr(), Integer.parseInt(item.getPort()));
                        socket.setSoTimeout(70);
                        //System.out.println("Port is available");
                        socket.close();
                        item.setPortOnline(true);
                    } catch (IOException e) {
                        //System.out.println("Port is not available");
                        item.setPortOnline(false);
                    }
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MIN_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        C_Adapter = new ChatsTitleAdapter(getActivity() != null ? getActivity() : null, list );
        newlist.setAdapter( C_Adapter );
    }

    public void openAddDialog() {
        LayoutInflater dlgInfater = (LayoutInflater) (getActivity() != null ? getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) : null);
        View root = dlgInfater != null ? dlgInfater.inflate(R.layout.row_pod_menu, null) : null;
        final EditText name_ = root != null ? root.findViewById(R.id.detailsName) : null;
        final EditText ipadr_ = root != null ? root.findViewById(R.id.detailsIpAdr) : null;
        final EditText port_ = root != null ? root.findViewById(R.id.detailsPort) : null;

        final AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setView( root );
        builder.setMessage( "Добавить запись" );

        builder.setPositiveButton( "Сохранить", (dialog, id) -> {
            TitleChatsItems item = new TitleChatsItems(
                    name_ != null ? name_.getText().toString() : null,
                    ipadr_ != null ? ipadr_.getText().toString() : null,
                    port_ != null ? port_.getText().toString() : null);
            DBManager.getInstance( getContext() ).addContact( item );
            //list.add( item );
            refreshTitleList();
        }).setNegativeButton( "Отмена", (dialog, id) -> dialog.cancel());
        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    public void openRemoveDialog(final TitleChatsItems item) {
        LayoutInflater dlgInfater = (LayoutInflater) (getActivity() != null ? getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) : null);
        View root = dlgInfater != null ? dlgInfater.inflate(R.layout.row_pod_menu, null) : null;
        final EditText name_ = root != null ? root.findViewById(R.id.detailsName) : null;
        final EditText ipadr_ = root != null ? root.findViewById(R.id.detailsIpAdr) : null;
        final EditText port_ = root != null ? root.findViewById(R.id.detailsPort) : null;

        Objects.requireNonNull(name_).setText( item.getName() );
        ipadr_.setText( item.getIp_adr() );
        port_.setText( item.getPort() );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setView( root );
        builder.setMessage( "Редактировать запись" );

        builder.setPositiveButton( "Сохранить", (dialog, id) -> {
            item.setName( name_.getText().toString() );
            item.setIp_adr( ipadr_.getText().toString() );
            item.setPort( port_.getText().toString() );

            DBManager.getInstance( getContext() ).updateContact( item );
            refreshTitleList();
        }).setNegativeButton( "Отмена", (dialog, id) -> dialog.cancel());

        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    public void openDeleteDialog(final TitleChatsItems item) {
        LayoutInflater dlgInflater = (LayoutInflater) (getActivity() != null ? getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) : null);
        Objects.requireNonNull(dlgInflater).inflate( R.layout.row_pod_menu, null );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity());
        builder.setMessage( String.format( "Удалить контакт %s?", item.getName() ) );

        builder.setPositiveButton( "Удалить", (dialog, id) -> {
            DBManager.getInstance( getContext() ).deleteContact( item.getID() );
            refreshTitleList();
        }).setNegativeButton( "Отмена", (dialog, id) -> dialog.cancel());

        builder.setCancelable( false );
        builder.create();
        builder.show();
    }

    public void onClickSelected(int position) {
        //items = C_Adapter.getItem( position );
        HashMap<String, String> values = (DBManager.getInstance( getContext() ).readContact( position ));
        table_name = values.get( "param1" );
        ipadr = values.get( "param2" );
        port = values.get( "param3" );
        Adress adr = new Adress(table_name, ipadr, port );
        // создаём или добавляем таблицу в базу
        DBChatHelper.setTableName(table_name);
        db_chat_Adapter.createTableIfNotExists();
        //шлем в активити привет. что пора запускать фрагмент.
        // OnStartFragmentTcpIp.on_startFragmentTcpIp(adr);

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        //editor.putBoolean("switch_andromeda", false);
        editor.putString(EnumsAndStatics.SERVER_IP_PREF, ipadr);
        editor.putString(EnumsAndStatics.SERVER_PORT_PREF, port);
        editor.apply();
        Navigation.findNavController(root).navigate(R.id.nav_tcp_ip);
        //onStartFragmentTCP_IP();
    }

    public void openSearchPortDialog(final TitleChatsItems item) {
        LayoutInflater dlgInfater = (LayoutInflater) (getActivity() != null ? getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) : null);
        View root = dlgInfater != null ? dlgInfater.inflate(R.layout.search_port, null) : null;
        final ListView ports = root != null ? root.findViewById(R.id.table_search_ports_list) : null;
        ArrayList<SearchPortItems> portsList = new ArrayList<>();

        /*Objects.requireNonNull(name_).setText( item.getName() );
        ipadr_.setText( item.getIp_adr() );
        port_.setText( item.getPort() );*/

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setView( root );
        builder.setMessage( "Список открытых портов\n для ip "+item.getIp_adr() );

        portsList = refreshPortOnlineList(item.getIp_adr());
        builder.setNegativeButton( "Ok", (dialog, id) -> dialog.cancel());

        SearchPortAdapter S_Adapter = new SearchPortAdapter(getActivity() != null ? getActivity() : null, portsList );
        //ListView searchPort=Objects.requireNonNull(getView()).findViewById( R.id.table_search_ports );
        ports.setAdapter( S_Adapter );
        builder.setCancelable( false );
        builder.create();
        refreshPortOnlineList(item.getIp_adr());
        builder.show();
        S_Adapter.notifyDataSetChanged();


    }

    @SuppressLint("UseRequireInsteadOfGet")
    private ArrayList<SearchPortItems> refreshPortOnlineList(String ip_adr) {
        ArrayList<SearchPortItems> list = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        final SearchPortItems[] item = new SearchPortItems[100];
        for (int i = 1; i <= 65535; i++) {

            int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket(ip_adr, finalI);
                        socket.setSoTimeout(100);
                        System.out.println("Port is available "+finalI);
                        socket.close();
                        item[0] = new SearchPortItems();
                        item[0].setPort(String.valueOf(finalI));
                        item[0].setPortOnline(true);
                        list.add(item[0]);
                    } catch (IOException e) {
                        System.out.println("Port is not available->"+finalI);
                    }
                    catch (NullPointerException e){e.printStackTrace();}

                }
            });
        }
        executor.shutdown();
        try {
            boolean b = executor.awaitTermination(40000, TimeUnit.MILLISECONDS); //ожидание завершения
            // executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

}
