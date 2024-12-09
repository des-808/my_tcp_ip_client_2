package com.example.tcp_ip_client_2.ui.search_ports;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tcp_ip_client_2.adapter.ChatMessageAdapter;
import com.example.tcp_ip_client_2.adapter.SearchPortAdapter;
import com.example.tcp_ip_client_2.classs.SearchPortItems;
import com.example.tcp_ip_client_2.classs.TitleChatsItems;
import com.example.tcp_ip_client_2.db.DBChatAdapter;
import com.example.tcp_ip_client_2.databinding.SearchPortBinding;
import com.example.tcp_ip_client_2.db.DBManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SearchPortsFragment extends Fragment {
    private SearchPortBinding binding;
    View root;
    DBManager db_manager_Adapter; //создаем переменную для работы с базой данных
    ChatMessageAdapter chatMessageAdapter;//создаем переменную для работы с clickable
    Spinner spinner;
    ListView listView;
    ProgressBar progressBar;
    FloatingActionButton btn_searchPorts;
    private Toast toast;
    String ip_adr;
    ArrayList<SearchPortItems> portsList;
    SearchPortAdapter S_Adapter;
    public SearchPortsFragment() {}

    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SearchPortBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        //View view = binding.getRoot();
       // db_chat_Adapter = new DBChatAdapter(Objects.requireNonNull(getActivity()));
        spinner = binding.spinnerSearchPorts;
        listView = binding.tableSearchPortsList;
        progressBar = binding.progressBarSearchPorts;
        btn_searchPorts = binding.floatingActionButton;
        db_manager_Adapter = new DBManager(Objects.requireNonNull(getActivity()));

        portsList = new ArrayList<>();
        /*portsList.add(new SearchPortItems("192.168.7.77",true));
        portsList.add(new SearchPortItems("192.168.3.57",false));*/
        S_Adapter = new SearchPortAdapter(root.getContext(), portsList );
        progressBar.setMax(totalTasks);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        listView.setAdapter(S_Adapter);
        S_Adapter.notifyDataSetChanged();

        ArrayList<TitleChatsItems>list = new ArrayList<TitleChatsItems>(db_manager_Adapter.getAllContacts());
        ArrayList<String> list_search = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            TitleChatsItems items = list.get(i);
            list_search.add(items.getIp_adr());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, list_search);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @SuppressLint({"ShowToast", "UseRequireInsteadOfGet"})
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ip_adr = parent.getItemAtPosition(position).toString();
                tostShort("Выбран Адрес->"+ip_adr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Обработка случая, когда ничего не выбрано
            }
        };
        spinner.setOnItemSelectedListener(listener);


        btn_searchPorts.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ShowToast", "UseRequireInsteadOfGet"})
            @Override
            public void onClick(View v) {
               // tostShort("Поиск портов");
                portsList.clear();

                //portsList = refreshPortOnlineList(ip_adr);
                new RefreshPortOnlineListTask().execute(ip_adr);
                S_Adapter.notifyDataSetChanged();
            }
        });
        //========================
    }
    private final class RefreshPortOnlineListTask extends AsyncTask<String, Void, ArrayList<SearchPortItems>> {
        @Override
        protected ArrayList<SearchPortItems> doInBackground(String... ip_adr) {
            return refreshPortOnlineList(ip_adr[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<SearchPortItems> portsList2) {
            // Обработка результата здесь
            //portsList.clear();
            //progressBar.setProgress(0);
            //portsList.addAll(portsList2);
          // portsList = portsList2;
            S_Adapter.notifyDataSetChanged();

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


    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int totalTasks = 2000;//65535;
    private int completedTasks = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private  ArrayList<SearchPortItems> refreshPortOnlineList(String ip_adr) {

        progressBar.setProgress(0);
        ArrayList<SearchPortItems> list = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        /*try {
            boolean b = executor.awaitTermination(100, TimeUnit.MILLISECONDS); //ожидание завершения
            // executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        final SearchPortItems[] item = new SearchPortItems[300];
        for (int i = 1; i < totalTasks; i++) {
            int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket(ip_adr, finalI);
                        socket.setSoTimeout(100);
                        //System.out.println("Port is available "+finalI);
                        socket.close();
                        item[0] = new SearchPortItems();
                        item[0].setPort(String.valueOf(finalI));
                        item[0].setPortOnline(true);
                        portsList.add(item[0]);
                    } catch (IOException e) {
                        //System.out.println("Port is not available->"+finalI);
                    }
                    catch (NullPointerException e){e.printStackTrace();}
                    finally {
                        completedTasks++;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(completedTasks % 100 == 0){progressBar.setProgress(completedTasks);}
                            }
                        });
                    }
                }
            });
        }
        completedTasks =  0;
        executor.shutdown();
        progressBar.setProgress(0);
        return list;
    }


}
