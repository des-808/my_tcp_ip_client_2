package com.example.tcp_ip_client_2;

import static java.lang.Integer.parseInt;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.tcp_ip_client_2.classs.TitleChatsItems;
import com.example.tcp_ip_client_2.db.DBManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.tcp_ip_client_2.databinding.ActivityMainBinding;
import com.example.tcp_ip_client_2.fragments.fragment_exit;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "LOG_TAG";
    public Toast toast;
    public Class fragmentClass = null;
    public static MenuItem menu_switch_btn;
    public static MenuItem menu_clearChat;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public Fragment fragment = null;
    private static final String PREFS_FILE = "com.example.tcp_ip_client_2_preferences";
    private static final String KEY_ANDROMEDA = "switch_andromeda";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;//Context.MODE_PRIVATE;

    //private final JsonBroadcastReceiver jsonBroadcastReceiver= new JsonBroadcastReceiver( (this));;
    private static final String CUSTOM_INTENT_ACTION = "com.example.broadcast.MY_NOTIFICATION";
    private final JsonBroadcastReceiver receiver = new JsonBroadcastReceiver(this);
    //private final IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT_ACTION);
    private final IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SEND);

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_title, R.id.nav_gallery, R.id.nav_usb, R.id.nav_preference,R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

        //jsonBroadcastReceiver = new JsonBroadcastReceiver(this);
        //IntentFilter filter = new IntentFilter(Intent.ACTION_SEND);
        intentFilter.addCategory(Intent.ACTION_SEND);
        intentFilter.addCategory(CUSTOM_INTENT_ACTION);
        registerReceiver(receiver, intentFilter);
        //registerReceiver(jsonBroadcastReceiver, new IntentFilter(Intent.EXTRA_TEXT));
    }

    @Override
    protected void onDestroy() {
        //unregisterReceiver(jsonBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* try {
            saveTitleListToJson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavDestination currentDestination = navController.getCurrentDestination();
        if (currentDestination != null && currentDestination.getId() == R.id.nav_home) {
            alertDialog("Выйти из приложения!!");
            //finish();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu_clearChat = menu.findItem(R.id.clearChat);
        menu_clearChat.setVisible(false);
        menu_switch_btn = menu.findItem(R.id.action_Connect_Disconnect_TCP_IP);
        menu_switch_btn.setVisible(false);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final int action_Connect_Disconnect_TCP_IP = R.id.action_Connect_Disconnect_TCP_IP;
        final int action_settings = R.id.action_settings;
            if(id==action_settings) { newPreferencesFragment();}
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

    public void newPreferencesFragment() {
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
    }


    /*public void saveTitleListToJson() throws IOException {
        List<TitleChatsItems> titleChatsItemsList = new ArrayList<>();
        DBManager db = new DBManager(this);
        titleChatsItemsList.addAll(db.getAllContacts());
        // Добавьте объекты в список

        Gson gson = new Gson();
        String json = gson.toJson(titleChatsItemsList);

        *//*FileOutputStream fileOutputStream = openFileOutput("contacts_list.json", MODE_PRIVATE);
        fileOutputStream.write(json.getBytes());
        fileOutputStream.close();*//*

       Intent intent = new Intent("com.example.tcp_ip_client_2");
       intent.putExtra("json", json);
       sendBroadcast(intent);
       *//* Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, json);
        intent.setType("text/plain");
        startActivity(intent);*//*

    }*/



}

/* private void setupDialog() {
        dialog = new ProgressDialog( this, ProgressDialog.STYLE_SPINNER );//STYLE_SPINNER
        dialog.setTitle( "Connecting" );
        dialog.setMessage( "Please wait..." );
        dialog.setIndeterminate( true );
        dialog.show();
    }*/