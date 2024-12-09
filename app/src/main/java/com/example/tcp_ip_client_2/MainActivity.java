package com.example.tcp_ip_client_2;
/*import static java.lang.Integer.parseInt;*/

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.tcp_ip_client_2.ui.preferences.SharedPreferenceFragment;
import com.example.tcp_ip_client_2.ui.title.tcp_ip.TCP_IP_Fragment;
import com.example.tcp_ip_client_2.fragments.fragment_exit;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.tcp_ip_client_2.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "LOG_TAG";
    public boolean connectToServer = false;
    public Toast toast;
    public Class fragmentClass = null;
    public static MenuItem menu_switch_btn;
    public static MenuItem menu_clearChat;

    public boolean is_fragment_TcpIP = false;
    fragment_exit dialogFragment;
    TCP_IP_Fragment fragTCP_IP;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public Fragment fragment = null;
    private static final String PREFS_FILE = "com.example.tcp_ip_client_2_preferences";
    private static final String KEY_ANDROMEDA = "switch_andromeda";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;//Context.MODE_PRIVATE;

    View root;
    private FragmentManager fManager;

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
                R.id.nav_title,R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_preference)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        root = navigationView.getHeaderView(0);
        //fManager = getSupportFragmentManager();

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        /*sharedPreferences=getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("switch_andromeda", false);
        editor.putString("pref_ip", "192.168.0.1");
        editor.putString("pref_port", "1234");
        editor.apply();*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        //создаем переменную для работы с базой данных
        ////////////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public void onBackPressed() {

    /*    int fragmentsInStack = getSupportFragmentManager().getBackStackEntryCount();
        // tost(String.valueOf(fragmentsInStack));

        if(fragmentsInStack == 0){
            dialogFragment = new fragment_exit( "Так ты точно хочешь выйти???" );
            dialogFragment.show( fManager, "dialog" );
        }*/
        super.onBackPressed();
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
        menu_switch_btn = menu.findItem(R.id.action_Connect_Disconnect_TCP_IP);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final int action_Connect_Disconnect_TCP_IP = R.id.action_Connect_Disconnect_TCP_IP;
        final int action_settings = R.id.action_settings;
            if(id==action_settings) {
          //      DisconnectToServer();
                //replaceFragment(new SharedPreferenceFragment(), "SharedPreferenceFragment");
                //Navigation.findNavController(root).navigate(R.id.root_preference);
                newPreferencesFragment();
            }
        /*if (id == action_Connect_Disconnect_TCP_IP) {
        if (!connectToServer) {
          //  DisconnectToServer();
            //item.setIcon(android.R.drawable.checkbox_off_background);
            item.setIcon(R.drawable.ic_otkl);
        } else if (connectToServer == false && is_fragment_TcpIP == true) {
          //  ConnectToServer();
        } else if (connectToServer == false && is_fragment_TcpIP == false) {
            // if (ipAdr != null || port != null) {
 //               onStartFragmentTCP_IP();
           // } else {
            //    tostShort("Error");
           // }
        }
        return true;
    }*/
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

    public void doPositiveClick() {this.finish();}
    public void doNegativeClick() {}

    /*public void newFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Заменяем текущий фрагмент на новый фрагмент
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, fragment).commit();
    }*/

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
}

/* private void setupDialog() {
        dialog = new ProgressDialog( this, ProgressDialog.STYLE_SPINNER );//STYLE_SPINNER
        dialog.setTitle( "Connecting" );
        dialog.setMessage( "Please wait..." );
        dialog.setIndeterminate( true );
        dialog.show();
    }*/