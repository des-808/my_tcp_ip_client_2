package com.example.tcp_ip_client_2.ui.search_ports;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.adapter.ChatMessageAdapter;
import com.example.tcp_ip_client_2.adapter.SearchPortAdapter;
import com.example.tcp_ip_client_2.adapter.SearchPortsItemAdapter;
import com.example.tcp_ip_client_2.classs.SearchPortOnlinePortItems;
import com.example.tcp_ip_client_2.classs.SearchPortsDropDownItem;
import com.example.tcp_ip_client_2.classs.ServerListItem;
import com.example.tcp_ip_client_2.databinding.FragmentSearchPortBinding;
import com.example.tcp_ip_client_2.db.DBManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SearchPortsFragment extends Fragment {
    private FragmentSearchPortBinding binding;
    View root;
    DBManager db_manager_Adapter; //создаем переменную для работы с базой данных
    ChatMessageAdapter chatMessageAdapter;//создаем переменную для работы с clickable
    Spinner spinner;
    ListView listView;
    TextView ports_scaning_time_to_end, ports_scaning_item_count;
    ProgressBar progressBar;
    FloatingActionButton btn_searchPorts;
    private Toast toast;
    String ip_adr;
    ArrayList<SearchPortOnlinePortItems> portsList;
    SearchPortAdapter S_Adapter;
    ExecutorService executor;
    //boolean stopSearch_scan = false;
    public SearchPortsFragment() {}
    static long allTime;
    private static boolean isTaskRunning = false;

    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchPortBinding.inflate(inflater, container, false);
        root = binding.getRoot();
       // db_chat_Adapter = new DBChatAdapter(Objects.requireNonNull(getActivity()));
        spinner = binding.spinnerSearchPorts;
        listView = binding.tableSearchPortsList;
        progressBar = binding.progressBarSearchPorts;
        btn_searchPorts = binding.floatingActionButton;
        db_manager_Adapter = new DBManager(Objects.requireNonNull(getActivity()));
        ports_scaning_time_to_end = binding.portsScaningTimeToEnd;
        ports_scaning_item_count = binding.portsScaningItemCount;

        portsList = new ArrayList<>();
        /*portsList.add(new SearchPortOnlinePortItems("192.168.7.77",true));
        portsList.add(new SearchPortOnlinePortItems("192.168.3.57",false));*/
        S_Adapter = new SearchPortAdapter(root.getContext(), portsList );
        progressBar.setMax(totalTasks);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        listView.setAdapter(S_Adapter);
        S_Adapter.notifyDataSetChanged();
        List<ServerListItem>list = new ArrayList<>(db_manager_Adapter.getAllContacts());
        ArrayList<SearchPortsDropDownItem> list_search = new ArrayList<SearchPortsDropDownItem>();
        CountDownLatch latch = new CountDownLatch(1); // Создаем CountDownLatch с начальным значением 1
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < list.size(); i++) {
                    ServerListItem items = list.get(i);
                    map.put(items.getIp_adr(), items.getName());
                }
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    try {
                        InetAddress.getByName(entry.getKey());
                        list_search.add(new SearchPortsDropDownItem(entry.getKey(), entry.getValue()));
                        //Log.d("IP", entry.getKey());
                        //list_search.add(new SearchPortsDropDownItem(items.getIp_adr(),items.getName()));
                    } catch (IOException e) {
                        // IP-адрес не может быть добавлен так как не активен, пропускаем его
                    }
                }
                latch.countDown(); // Уменьшаем значение CountDownLatch на 1, когда заполнение list_search завершено
            }
        }).start();

        try {
            latch.await(); // Ожидаем завершения заполнения list_search
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SearchPortsItemAdapter adapter = new SearchPortsItemAdapter(root.getContext(), list_search);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @SuppressLint({"ShowToast", "UseRequireInsteadOfGet"})
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                portsList.clear();
                SearchPortsDropDownItem item = (SearchPortsDropDownItem) parent.getItemAtPosition(position);
                ip_adr = item.getIp_adr();
                tostShort("Выбран Адрес->"+ip_adr);
                S_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Обработка случая, когда ничего не выбрано
                portsList.clear();
                SearchPortsDropDownItem item = (SearchPortsDropDownItem) parent.getItemAtPosition(0);
                ip_adr = item.getIp_adr();
                //tostShort("Выбран Адрес->"+ip_adr);
                S_Adapter.notifyDataSetChanged();
            }
        };
        spinner.setOnItemSelectedListener(listener);

        btn_searchPorts.setOnClickListener(new View.OnClickListener() {

            @SuppressLint({"ShowToast", "UseRequireInsteadOfGet", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View v) {//if(stopSearch == false){new RefreshPortOnlineListTask().execute(ip_adr);}
                if (!isTaskRunning) {
                    new RefreshPortOnlineListTask().execute(ip_adr);
                    btn_searchPorts.setImageDrawable(getResources().getDrawable(R.drawable.btn_stop25));
                    isTaskRunning = true;
                } else {
                    try {
                        executor.shutdownNow(); // Остановка всех задач
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isTaskRunning = false;
                }
            }
        });
        //========================
    }

    private class RefreshPortOnlineListTask extends AsyncTask<String, Void, ArrayList<SearchPortOnlinePortItems>> {
        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<SearchPortOnlinePortItems> doInBackground(String... ip_adr) {
            ArrayList<SearchPortOnlinePortItems> portsLists = new ArrayList<>();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    portsList.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                    ports_scaning_time_to_end.setVisibility(View.VISIBLE);
                    ports_scaning_item_count.setVisibility(View.VISIBLE);
                    ports_scaning_time_to_end.setText("");
                    ports_scaning_item_count.setText("");
                }
            });
            portsLists = refreshPortOnlineList(ip_adr[0]);
            return portsLists;
        }

        @Override
        protected void onPostExecute(ArrayList<SearchPortOnlinePortItems> portsList2) {
            // Обработка результата здесь
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ///portsList = portsList2;
                    btn_searchPorts.setImageDrawable(getResources().getDrawable(R.drawable.btn_play25));
                    S_Adapter.notifyDataSetChanged();
                    if(portsList.isEmpty()){
                        messageItemDialog("Ничего не найдено"+"\nВремени затрачено: "+millisToTime(allTime));}else{
                        messageItemDialog("Найдено портов->"+portsList.size()+"\nВремени затрачено: "+millisToTime(allTime));
                    }
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.INVISIBLE);
                    //ports_scaning_time_to_end.setVisibility(View.INVISIBLE);
                    ports_scaning_item_count.setVisibility(View.INVISIBLE);
                    //ports_scaning_time_to_end.setText("");
                    ports_scaning_item_count.setText("");
                }
            });
        }
    }

    private final int totalTasks = 65536;//65535;

    private ArrayList<SearchPortOnlinePortItems> refreshPortOnlineList(String ip_adr) {
        if(executor!= null){executor.shutdownNow();}
        ArrayList<SearchPortOnlinePortItems> list = new ArrayList<>();
        executor = Executors.newFixedThreadPool(100);
        AtomicInteger scannedPorts = new AtomicInteger(0); // Счетчик опрошенных портов
        AtomicLong totalTime = new AtomicLong(0); // Счетчик общего времени сканирования
        HashMap<Integer, Boolean> scannedPortsMap = new HashMap<>(); // Карта просканированных портов
        long startTimeStart = System.currentTimeMillis();
        for (int i = 0; i < totalTasks; i++) {
            int finalI = i;
            /*if(stopSearch){
                executor.shutdownNow(); // Остановка всех задач
                break;
            }*/
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis(); // Запись времени начала сканирования
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(ip_adr, finalI), 1000);
                        socket.close();
                        SearchPortOnlinePortItems item = new SearchPortOnlinePortItems();
                        item.setPort(String.valueOf(finalI));
                        item.setPortOnline(true);
                        list.add(item);
                        if (getActivity() != null) { // Проверка, что FragmentActivity все еще существует
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!scannedPortsMap.containsKey(finalI)) { // Проверка, был ли порт просканирован
                                    portsList.add(item);
                                    S_Adapter.notifyDataSetChanged();
                                    scannedPortsMap.put(finalI, true); // Добавление порта в список просканированных портов
                                }
                            }
                        });
                    }
                    } catch (IOException e) {
                    }
                    catch (NullPointerException e){e.printStackTrace();}
                    finally {
                        allTime = System.currentTimeMillis() - startTimeStart;
                        long endTime = System.currentTimeMillis(); // Запись времени окончания сканирования
                        totalTime.addAndGet(endTime - startTime); // Увеличение счетчика общего времени сканирования
                        scannedPorts.incrementAndGet(); // Увеличение счетчика опрошенных портов
                        if (getActivity() != null) { // Проверка, что FragmentActivity все еще существует
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (scannedPorts.get() > 0 && (scannedPorts.get() % 256 == 0)) { // Проверка, что сканирование портов началось
                                        double ostTaskTime = ((double)allTime / scannedPorts.get()) * (totalTasks-scannedPorts.get());
                                        long estimatedTime = (long)ostTaskTime;
                                        //long estimatedTime = allTime;

                                        String timeToEnd = millisToTime(estimatedTime);
                                        ports_scaning_time_to_end.setText(String.valueOf(timeToEnd)); // Отображение прогнозируемого времени до конца сканирования
                                        ports_scaning_time_to_end.postInvalidate();
                                        ports_scaning_item_count.setText(String.valueOf("Просканировано портов: " + scannedPorts.get()));
                                        ports_scaning_item_count.postInvalidate();
                                        progressBar.setProgress(scannedPorts.get());
                                        progressBar.postInvalidate();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        executor.shutdown();//shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //stopSearch = false;
        return list;
    }
@SuppressLint("DefaultLocale")
public String millisToTime(long estimatedTime){
    long hours = TimeUnit.MILLISECONDS.toHours(estimatedTime);
    estimatedTime -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(estimatedTime);
    estimatedTime -= TimeUnit.MINUTES.toMillis(minutes);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(estimatedTime);
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
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
        //setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void messageItemDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopSearch = true;
        //executor.shutdownNow();
        }

}


