package com.example.tcp_ip_client_2.ui.bluetooth;





import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tcp_ip_client_2.R;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class DeviceListActivity extends Activity {

    /**
     * Tag for Log
     */
    private static final String TAG = "DeviceListActivity";
    /*private static boolean BLUETOOTH_SCAN_GRANTED = true ;
    private static final int REQUEST_BLUETOOTH_SCAN = 1 ;*/
    private static final int REQUEST_ENABLE_BT = 1;
    private static  int REQUEST_CODE = 1;
    private Context context;
    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    /**
     * Member fields
     */
    private BluetoothAdapter mBtAdapter;

    /**
     * Newly discovered devices
     */
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        // Setup the window
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_device_list);

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        /** Инициализация кнопки которая будет выполнять обнаружение устройства*/
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(v -> {
            doDiscovery();
            v.setVisibility(View.GONE);
        });

        /** Инициализация адаптеров массивов(ArrayAdapter).
         *  Один применяется для уже сопряжённых устройств,
         *  один - для тлько что обнаруженных устройств
         */
        ArrayAdapter<String> pairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        /** Находим и настраиваем ListView для сопряжённых устройств*/
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        /** Находим и настраиваем ListView для только что обнаруженных устройств*/
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//это действие используется для обработки изменения состояния Bluetooth
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//это действие используется для обработки начала обнаружения устройств
        filter.addAction(BluetoothDevice.ACTION_FOUND);//это действие используется для обработки обнаружения нового устройства
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//это действие используется для обработки окончания обнаружения устройств
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);//это действие используется для обработки изменения состояния подключения
        this.registerReceiver(mReceiver, filter);//

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        /** Получаем набор устройств, сопряженных в настоящий момент*/
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);
        } else {
            Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

            /** Если имеются сопряженные устройства, добавляем кождое из них к ArrayAdapter*/
            if (pairedDevices.size() > 0) {
                findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        device.fetchUuidsWithSdp();
                        ParcelUuid[] uuids = device.getUuids();
                        if (uuids != null) {
                            for (ParcelUuid uuid : uuids) {
                                if (uuid.getUuid().equals(MY_UUID)) {
                                    pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                String noDevices = getResources().getText(R.string.none_paired).toString();
                pairedDevicesArrayAdapter.add(noDevices);
            }
        }
    }
    //private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, REQUEST_CODE);
            } else {
                mBtAdapter.cancelDiscovery();
            }
        }
        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Запускаем обнаружение устройства с применением BluetoothAdapter
     */
    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");
        /** Обозначаем процесс сканирования в заголовке*/
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        /** Активируем под заголовок для новых устройств*/
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        /** Если мы занимаемся обнаружением - останавливаем этот процесс*/
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, REQUEST_CODE);
        } else {
            if (mBtAdapter.isDiscovering()) {
                mBtAdapter.cancelDiscovery();
            }

            /** Запрос обнаружения от BluetoothAdapter*/
            mBtAdapter.startDiscovery();
        }
    }



    /**
     * Прослушиватель по щелчку для всех устройств в ListViews
     */
    private  AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);
            } else {
                mBtAdapter.cancelDiscovery();
            }
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };


    /**
     * Широковещательный приёмник BroadcastReceiver, слушающий информацию от
     * обнаруженных устройств и изменяющий заголовок, когда обнаружение завершено.
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                Log.d(TAG, "-------------------ACTION_STATE_CHANGED start-------------------");
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    Log.d(TAG, "-------------------BlueTooth ON-----------------");
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    Log.d(TAG, "-------------------BlueTooth OFF-----------------");
                }
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.d(TAG, "-------------------BlueTooth Discover Started-----------------");
            }

            /** Когда при онаружении удаётся найти устройство*/
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d(TAG, "------------------- ACTION_FOUND -----------------");
                /** Получаем от намерения обьект BluetoothDevice*/
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                /** Если оно ужесопряжено - пропускаем, так как мы уже слушаем его.*/
                Log.d(TAG, "-------------------New Device Found-----------------");
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);
                } else {
                    Log.d(TAG, "-------------------NAME        :" + device.getName());
                    Log.d(TAG, "-------------------MAC ADDRESS :" + device.getAddress());
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                        mNewDevicesArrayAdapter.notifyDataSetChanged();
                    }/** По завершению этапа обнаружения заменяем заголовок этой активности*/
                }
            }

            // When discovery is finished, change the Activity title

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))

            {
                setProgressBarIndeterminateVisibility(true);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
                //Log.d(TAG, "-------------------BlueTooth Discover Finished-----------------");
            }

            if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                setTitle(getString(R.string.select_connecd_device));
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
                Log.d(TAG, "-------------------BlueTooth Connected -----------------");
                //Log.d(TAG, "-------------------BlueTooth Disconnected-----------------");
            }
        }
    };
}




/* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_SCAN) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                BLUETOOTH_SCAN_GRANTED = true;
            }
        }
        if(BLUETOOTH_SCAN_GRANTED){
            Toast.makeText(this, "Разрешение получено", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show();
        }
    }
*/