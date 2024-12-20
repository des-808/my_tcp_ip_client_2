package com.example.tcp_ip_client_2.ui.search_ports;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.adapter.ChatMessageAdapter;
import com.example.tcp_ip_client_2.adapter.SearchPortAdapter;
import com.example.tcp_ip_client_2.classs.SearchPortItems;
import com.example.tcp_ip_client_2.classs.TitleChatsItems;
import com.example.tcp_ip_client_2.databinding.FragmentSearchPortBinding;
import com.example.tcp_ip_client_2.db.DBChatAdapter;
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
    private FragmentSearchPortBinding binding;
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
    private boolean stopSearch = false;

    public SearchPortsFragment() {}

    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchPortBinding.inflate(inflater, container, false);
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
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @SuppressLint({"ShowToast", "UseRequireInsteadOfGet"})
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                portsList.clear();
                ip_adr = parent.getItemAtPosition(position).toString();
                //tostShort("Выбран Адрес->"+ip_adr);
                S_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Обработка случая, когда ничего не выбрано
            }
        };
        spinner.setOnItemSelectedListener(listener);

        btn_searchPorts.setOnClickListener(new View.OnClickListener() {
            private boolean isTaskRunning = false;
            @SuppressLint({"ShowToast", "UseRequireInsteadOfGet", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View v) {//if(stopSearch == false){new RefreshPortOnlineListTask().execute(ip_adr);}
                if (!isTaskRunning) {
                    new RefreshPortOnlineListTask().execute(ip_adr);
                    btn_searchPorts.setImageDrawable(getResources().getDrawable(R.drawable.btn_stop25));
                    isTaskRunning = true;
                } else {
                    stopSearch = true;
                    isTaskRunning = false;
                }
            }
        });
        //========================
    }

    private class RefreshPortOnlineListTask extends AsyncTask<String, Void, ArrayList<SearchPortItems>> {
        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<SearchPortItems> doInBackground(String... ip_adr) {
            ArrayList<SearchPortItems> portsLists = new ArrayList<>();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    portsList.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                }
            });
            portsLists = refreshPortOnlineList(ip_adr[0]);
            completedTasks =  0;
            return portsLists;
        }

        @Override
        protected void onPostExecute(ArrayList<SearchPortItems> portsList2) {
            // Обработка результата здесь
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///portsList = portsList2;
                    btn_searchPorts.setImageDrawable(getResources().getDrawable(R.drawable.btn_play25));
                    S_Adapter.notifyDataSetChanged();
                    if(portsList.isEmpty()){noItemDialog("Ничего не найдено");}
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    //private final Handler handler = new Handler(Looper.getMainLooper());
    private final int totalTasks = 65536;//65535;
    private int completedTasks = 0;
    //private int zahod = 256;

    @SuppressLint("UseRequireInsteadOfGet")
    private  ArrayList<SearchPortItems> refreshPortOnlineList(String ip_adr) {
        ArrayList<SearchPortItems> list = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        /*try {
            boolean b = executor.awaitTermination(100, TimeUnit.MILLISECONDS); //ожидание завершения
            // executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        final SearchPortItems[] item = new SearchPortItems[1];
        for (int i = 0; i < totalTasks; i++) {
            int finalI = i;
            if(stopSearch){break;}
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
                        list.add(item[0]);
                        portsList.add(item[0]);
                    } catch (IOException e) {
                        //System.out.println("Port is not available->"+finalI);
                    }
                    catch (NullPointerException e){e.printStackTrace();}
                }
            });
            completedTasks++;
            progressBar.setProgress(i);
            progressBar.postInvalidate();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///portsList = portsList2;
                    S_Adapter.notifyDataSetChanged();
                }
            });
        }
        executor.shutdown();
        stopSearch = false;
        return list;
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
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void noItemDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        //alertDialogBuilder.setTitle("Так ты точно хочешь выйти??");
        alertDialogBuilder.setTitle(msg);
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder
                .setCancelable(true)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }




}

 /*progressBar.setProgress(0);
            portsList = refreshPortOnlineList(ip_adr[0]);

            final RefreshPortOnlineListTask task = new RefreshPortOnlineListTask();
            for (int il = 0; il < zahod; il++) {
                final int finalI = il;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(finalI);
                        progressBar.postInvalidate();
                        S_Adapter.notifyDataSetChanged();
                    }
                });
                // latch.countDown();
                Log.d("test ", String.valueOf(il));
                    try {
                        task.execute(ip_adr);
                        latch.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                latch.countDown();
            }
            completedTasks =  0;
            return portsList;*/
