package com.example.tcp_ip_client_2.ui.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tcp_ip_client_2.MainActivity;
import com.example.tcp_ip_client_2.classs.Swith_Terminal;
import com.example.tcp_ip_client_2.ui.usb.UsbFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothChatService {
    // Debugging
    private static final String TAG = "BluetoothChatService";
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final int REQUEST_ENABLE_BT = 1;
    private static  int REQUEST_CODE = 1;

    // Unique UUID for this application
    private static final UUID SPP_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//fa87c0d0-afac-11de-8a39-0800200c9a66
    //private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    // Member fields
    private BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;
    private int mNewState;
    Context context;

    // Константы, которые указывают текущее состояние соединения
    public static final int STATE_NONE = 0;       // мы ничего не делаем
    public static final int STATE_LISTEN = 1;     // сейчас слушаю входящие соединения
    public static final int STATE_CONNECTING = 2; // сейчас инициируем исходящее соединение
    public static final int STATE_CONNECTED = 3;  // теперь подключен к удаленному устройству


    /**
     * Constructor. Prepares a new BluetoothChat session.
     *
     * @param context The UI Activity Context
     * @param handler A Handler to send messages back to the UI Activity
     */
    public BluetoothChatService(Context context, Handler handler) {
       this.context = context;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mNewState = mState;
        mHandler = handler;
    }

    /**
     * Update UI title according to the current state of the chat connection
     */
    private synchronized void updateUserInterfaceTitle() {
        mState = getState();
        Log.d(TAG, "updateUserInterfaceTitle() " + mNewState + " -> " + mState);
        mNewState = mState;

        // Передайте новое состояние обработчику, чтобы активность пользовательского интерфейса могла обновляться
        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, mNewState, -1).sendToTarget();
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Отменить любой поток, пытающийся установить соединение
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Отменить любой поток в настоящее время работает соединение
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Запустите поток для прослушивания на BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }

        // Обновить заголовок интерфейса
        updateUserInterfaceTitle();
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     * @param //secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        // Обновить заголовок интерфейса
        updateUserInterfaceTitle();
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device) {
        Log.d(TAG, "connected to: " + device);

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);

        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        bundle.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        // Обновить заголовок интерфейса
        /**
         * xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
         * xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
         */
        updateUserInterfaceTitle();
        /**
         * xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
         * xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
         */
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }


        mState = STATE_NONE;
        // Обновить заголовок интерфейса
        updateUserInterfaceTitle();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);


        mState = STATE_NONE;
        // Обновить заголовок интерфейса
        updateUserInterfaceTitle();

        // Start the service over to restart listening mode
        BluetoothChatService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
        // Обновить заголовок интерфейса
        updateUserInterfaceTitle();

        // Start the service over to restart listening mode
        BluetoothChatService.this.start();
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    public class AcceptThread extends Thread {
        // The local server socket
        private final   BluetoothServerSocket mmServerSocket;


        /*public AcceptThread() {
            Log.d(TAG, "AcceptThread AcceptThread()");
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, SPP_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: listen() failed", e);
            }
            mmServerSocket = tmp;
            mState = STATE_LISTEN;
        }*/

        public AcceptThread() {
            mAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_CODE);
            } else {
                try {
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, SPP_UUID);
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type: listen() failed", e);
                }
            }

            mmServerSocket = tmp;
            mState = STATE_LISTEN;
        }


        public void run() {
            Log.d(TAG, "AcceptThread run() Start" + this);
            setName("AcceptThread");

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // Это блокирующий вызов, который вернется только к
                    // успешное соединение или исключение
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type: accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothChatService.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            Log.i(TAG, "AcceptThread run() End" );

        }

        public void cancel() {
            Log.d(TAG, "AcceptThread cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type close() of server failed", e);
            }
        }
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;


        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //return TODO;
                }
                tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread() create() failed", e);
            }
            mmSocket = tmp;
            mState = STATE_CONNECTING;
        }

        public void run() {
            Log.i(TAG, "ConnectThread run() Start");
            setName("ConnectThread");

            // Всегда отменяем обнаружение, потому что это замедлит соединение

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

           /* if(usbConnection == null && usbPermission == UsbFragment.UsbPermission.Unknown && !usbManager.hasPermission(driver.getDevice())) {
                usbPermission = UsbFragment.UsbPermission.Requested;
                int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_MUTABLE : 0;
                Intent intent = new Intent(INTENT_ACTION_GRANT_USB);
                intent.setPackage(getActivity().getPackageName());
                PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, flags);
                usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
                return;
            }*/
            mAdapter.cancelDiscovery();

            // Установить соединение с BluetoothSocket
            try {
                // Это блокирующий вызов, который вернется только к
                // успешное соединение или исключение

                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }


            // Сброс ConnectThread, потому что мы закончили
            synchronized (BluetoothChatService.this) {
                mConnectThread = null;
            }


            // Запускаем связанный поток
            connected(mmSocket, mmDevice);
            Log.i(TAG, "ConnectThread run() End");
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread: ");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
           // byte[] buffer = new byte[128];
            byte[] buffer = new byte[1024];
            int bytesArray = 0;
            //boolean startbit = true;

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                    try{
                        // Read from the InputStream
                        //buffer[bytesArray] = (byte) mmInStream.read();
                        bytesArray = mmInStream.read(buffer);
                        // Send the obtained bytes to the UI Activity
                        mHandler.obtainMessage(Constants.MESSAGE_READ, bytesArray, -1, buffer).sendToTarget();
                    } catch (IOException | NullPointerException e) {
                        Log.e(TAG, "disconnected", e);
                        connectionLost();
                        break;
                    }

                /*if (Swith_Terminal.isTerminal()){
                    try{
                        // Read from the InputStream
                        buffer[bytesArray] = (byte) mmInStream.read();
                        // Send the obtained bytes to the UI Activity
                        if (startbit) {
                            if (buffer[bytesArray] == startBitPacket) {
                                bytesArray = 1;
                                startbit = false;
                            }
                        } else {
                            if (buffer[bytesArray] == stopBitPacket){
                                bytesArray = 0;
                                startbit = true;
                                mHandler.obtainMessage(Constants.MESSAGE_READ, bytesArray, -1, buffer).sendToTarget();
                            } else bytesArray++;

                        }
                    } catch (IOException e) {
                        Log.e(TAG, "disconnected", e);
                        connectionLost();
                        break; }
                }*/

                /*if (Swith_Terminal.isNord()) {
                    try {
                        // Read from the InputStream
                        buffer[bytesArray] = (byte) mmInStream.read();
                        // Send the obtained bytes to the UI Activity
                        if (startbit) {
                            if ((buffer[bytesArray] == startInd1) || (buffer[bytesArray] == startInd2)) {
                                bytesArray = 1;
                                startbit = false;
                            }
                        } else {
                            if (
//                                    ((buffer[bytesArray] == stopInd)) &&
                                    (bytesArray == 40)) {
                                mHandler.obtainMessage(Constants.MESSAGE_READ, bytesArray, -1, buffer).sendToTarget();
                                bytesArray = 0;
                                startbit = true;
                            } else bytesArray++;

                        }
                    } catch (IOException e) {
                        Log.e(TAG, "disconnected", e);
                        connectionLost();
                        break; }
                }*/

               /* if (Swith_Terminal.isLin()){
                    try{
                        // Read from the InputStream
                        buffer[bytesArray] = (byte) mmInStream.read();
                        // Send the obtained bytes to the UI Activity
                        if (startbit) {
                            if (buffer[bytesArray] == startBitPacket) {
                                bytesArray = 1;
                                startbit = false;
                            }
                        } else {
                            if (buffer[bytesArray] == stopBitPacket){
                                bytesArray = 0;
                                startbit = true;
                                mHandler.obtainMessage(Constants.MESSAGE_READ, bytesArray, -1, buffer).sendToTarget();
                            } else bytesArray++;

                        }
                    } catch (IOException e) {
                        Log.e(TAG, "disconnected", e);
                        connectionLost();
                        break; }
                }*/
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
//                Charset cset = Charset.forName("CP866");
//                String sendMessage = new String(buffer, cset);
//                try { sendMessage = hexadecimal(sendMessage, "CP866");}
//                catch (UnsupportedEncodingException e) { e.printStackTrace(); }
//                Log.i(TAG, sendMessage);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}
