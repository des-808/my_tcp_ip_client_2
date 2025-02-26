package com.example.tcp_ip_client_2;


import static java.lang.Integer.parseInt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.example.tcp_ip_client_2.classs.ServerListItem;
import com.example.tcp_ip_client_2.db.DBManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.tcp_ip_client_2.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;*/

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "LOG_TAG";
    public Toast toast;
    public Class fragmentClass = null;
   /* public static MenuItem menu_switch_btn;
    public static MenuItem menu_clearChat;
    public static MenuItem menu_bluetooth_search_to_connect_btn;*/

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public Fragment fragment = null;
    /*private static final String PREFS_FILE = "com.example.tcp_ip_client_2_preferences";
    private static final String KEY_ANDROMEDA = "switch_andromeda";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;//Context.MODE_PRIVATE;*/
    NavController navController = null;
   // private final JsonBroadcastReceiver jsonBroadcastReceiver= new JsonBroadcastReceiver( (this));;
    private static final String CUSTOM_INTENT_ACTION = "com.example.broadcast.MY_NOTIFICATION";
    //private final JsonBroadcastReceiver receiver = new JsonBroadcastReceiver(this);
    //private final IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT_ACTION);
    //private final IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SEND);
    Map<String, Charset> charsetMap = Charset.availableCharsets();
    ArrayList<String> charsetList = new ArrayList<>();

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_title, R.id.nav_gallery,
                R.id.nav_usb, R.id.nav_settings, R.id.nav_bluetooth, R.id.nav_keyboard,R.id.nav_tcpIpFragment,R.id.nav_mipIFragment)//, R.id.nav_preference
                .setOpenableLayout(drawer)//.setDrawerLayout(drawer)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_home) {
                navController.popBackStack(destination.getId(), false);
            }
        });
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        checkPermissionLocation();
        //###################################################################################################
        // Регистрация слушателя для отслеживания первого перехода
        /*navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                // Проверяем, является ли это первым переходом
                if (destination.getId() == R.id.nav_home) { // Замените R.id.nav_home на ID начального фрагмента
                    // Добавляем начальный фрагмент в back stack
                    Log.d("nav_home", "nav_home");
                    navController.popBackStack(destination.getId(), false);
                }
            }
        });*/
        //###################################################################################################




        /*int backgroundColor = ContextCompat.getColor(this, android.R.color.background_dark);
       View root = navigationView.getHeaderView(0);
       root.setBackgroundColor(backgroundColor);*/

        //###################################################################################################
        //SharedPreferences sharedPreferences;
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        /*sharedPreferences=getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("switch_andromeda", false);
        editor.putString("pref_ip", "192.168.0.1");
        editor.putString("pref_port", "1234");
        editor.apply();*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // для тёмной темы
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // для светлой темы
        ////////////////////////////////////////////////////////////////////////////////////////////////////

       // jsonBroadcastReceiver = new JsonBroadcastReceiver(this);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if ("com.example.receiver.CUSTOM_ACTION".equals(action) && "text/plain".equals(type)) {
        //if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            handleSendText(intent); // Обработчик полученного текста
        }

        handleUsbDeviceConnectedIntent(getIntent());// Если Приложение было не активно и Activity была создана впервые, обрабатываем первый Intent
        /*for (String charsetName : charsetMap.keySet()) {
            charsetList.add(charsetName);
        }
        File myFolderFile = new File(getFilesDir(),"charsets.txt");
        if (!myFolderFile.exists()) {
            try {
                myFolderFile.createNewFile();
            } catch (IOException e) {
                Log.e("charsets_txt", e.getMessage());
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(myFolderFile))) {
            for (String charset : charsetList) {
                writer.write(charset);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //###################################################################################################
        // List<String> SystemFontsList = getSystemFonts(this);
       /* File myFolderFile = new File(getFilesDir(),"SystemFonts.txt");
        if (!myFolderFile.exists()) {
            try {
                myFolderFile.createNewFile();
            } catch (IOException e) {
                Log.e("SystemFonts_txt", e.getMessage());
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(myFolderFile))) {
            for (String charset : SystemFontsList) {
                writer.write(charset);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //###################################################################################################


    }
    private void handleSendText(Intent intent) {
        final DBManager dbManager = new DBManager(getBaseContext());
        HashSet<ServerListItem>  listHash = new HashSet<>(dbManager.getAllContacts());//получаем конткты из бд
        String json = intent.getStringExtra(Intent.EXTRA_TEXT);
        List<ServerListItem> list = new ArrayList<>();
        if (json != null) {
            try {
                list = loadTitleListFromJson(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            listHash.addAll(list);
            for (ServerListItem contact : listHash) {
                dbManager.addContact(contact);
            }
            tost(json);
        }
    }

        public static List<String> getSystemFonts(Context context) {
            List<String> fontList = new ArrayList<>();
            File fontDir = new File("/system/fonts");
            if (fontDir.exists() && fontDir.isDirectory()) {
                File[] fontFiles = fontDir.listFiles();
                if (fontFiles != null) {
                    for (File fontFile : fontFiles) {
                        fontList.add(fontFile.getName());
                    }
                }
            }
            return fontList;
        }

    @Override
    protected void onDestroy() {
        //unregisterReceiver(jsonBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
       // navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavDestination currentDestination = navController.getCurrentDestination();
        if (currentDestination != null && currentDestination.getId() == R.id.nav_home) {
            alertDialog("Выйти из приложения!!");
        } else {
            super.onBackPressed();
        }
    }
    //###################################################################################################*/
    public void tost(String msg) {
        toast = Toast.makeText( this, msg, Toast.LENGTH_SHORT );
        toast.show();
    }
    public void tostShort(String msg) {
        toast = Toast.makeText( this, msg, Toast.LENGTH_SHORT );
        toast.show();
    }
    //###################################################################################################

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
       /* menu_clearChat = menu.findItem(R.id.clearChat);
        menu_clearChat.setVisible(false);
        menu_switch_btn = menu.findItem(R.id.action_Connect_Disconnect_TCP_IP);
        menu_switch_btn.setVisible(false);*/
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final int action_Connect_Disconnect_TCP_IP = R.id.action_Connect_Disconnect_TCP_IP;
        /*final int action_settings = R.id.action_settings;
            if(id==action_settings) { newPreferencesFragment();}*/
        return super.onOptionsItemSelected(item);
    }

    private void alertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //alertDialogBuilder.setTitle("Так ты точно хочешь выйти??");
        alertDialogBuilder.setTitle(msg);
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("Да", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        //tostShort("Правильный выбор!!");
                        finish();
                    }
                })
                .setNegativeButton("Нет",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState( outState );
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*public void newPreferencesFragment() {
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Заменяем текущий фрагмент на новый фрагмент
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        navController.navigate(R.id.nav_preference);
    }*/

    /*public void newPreferencesFragment() {
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Получаем NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        // Определяем текущий активный фрагмент
        List<Fragment> fragments = navHostFragment.getChildFragmentManager().getFragments();
        if (!fragments.isEmpty()) {
            Fragment currentFragment = fragments.get(fragments.size() - 1);

            // Получаем идентификатор текущего фрагмента
            int currentFragmentId = currentFragment.getId();

            // Добавляем текущий фрагмент в back stack
            navController.popBackStack(currentFragmentId, false); // регистрируем вызов
        }

        // Переходим к новому фрагменту
        navController.navigate(R.id.nav_preference);
    }*/


 /*   public void saveTitleListToJson() throws IOException {
        List<ServerListItem> titleChatsItemsList = new ArrayList<>();
        DBManager db = new DBManager(this);
        titleChatsItemsList.addAll(db.getAllContacts());
        // Добавьте объекты в список

        Gson gson = new Gson();
        String json = gson.toJson(titleChatsItemsList);

        FileOutputStream fileOutputStream = openFileOutput("contacts_list.json", MODE_PRIVATE);
        fileOutputStream.write(json.getBytes());
        fileOutputStream.close();

       Intent intent = new Intent("com.example.tcp_ip_client_2");
       intent.putExtra("json", json);
       sendBroadcast(intent);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, json);
        intent.setType("text/plain");
        startActivity(intent);

    }*/



    private void handleUsbDeviceConnectedIntent(Intent intent) {
        /*Log.d(LOG_TAG, "USB_DEVICE_ATTACHED");
        navController.navigate(R.id.nav_usb_device);*/
        if("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(intent.getAction())) {
            Log.d(LOG_TAG, "USB_DEVICE_ATTACHED");
            navController.navigate(R.id.nav_usb_device);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
            handleUsbDeviceConnectedIntent(intent);
        }
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            // Обработка подключения по Bluetooth
        }

    }
    /*@Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mReceiver);
    }*/

    public List<ServerListItem> loadTitleListFromJson(String json) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ServerListItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void checkPermissionLocation() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int check = checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            check += checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");

            if (check != 0) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1002);
            }
        }

    }


}

/* private void setupDialog() {
        dialog = new ProgressDialog( this, ProgressDialog.STYLE_SPINNER );//STYLE_SPINNER
        dialog.setTitle( "Connecting" );
        dialog.setMessage( "Please wait..." );
        dialog.setIndeterminate( true );
        dialog.show();
    }*/



/*

// Предположим, что charsetList - это список доступных кодировок
List<String> charsetList = Arrays.asList("UTF-8", "ISO-8859-1", "US-ASCII");
// Выбираем нужную кодировку из списка
String selectedCharset = charsetList.get(0); // Здесь выбрана первая кодировка из списка

// Примените выбранную кодировку для чтения файла
try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(myFolderFile), selectedCharset))) {
String line;
    while ((line = reader.readLine()) != null) {
        // Обработка строки
        }
        } catch (IOException e) {
        Log.e("charsets_txt", e.getMessage());
        }

// Или примените выбранную кодировку для записи в файл
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(myFolderFile), selectedCharset))) {
        writer.write("Текст для записи");
} catch (IOException e) {
Log.e*/
