package com.example.ble_sendingdata_oncomand.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.ble_sendingdata_oncomand.adapter.BtConsts;

public class BtConnection {
    private Context context;
    private SharedPreferences pref;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice device;
    private ConnectThread connectThread;

    public BtConnection(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(BtConsts.MY_PREF, Context.MODE_PRIVATE);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void connect() {
        String mac = pref.getString(BtConsts.MAC_KEY, "");
        if(!btAdapter.isEnabled() || mac.isEmpty()) return;
        device = btAdapter.getRemoteDevice(mac);
        if(device == null) return;
        connectThread = new ConnectThread(btAdapter, device);
        connectThread.start();
    }
    /*Тут ми будемо передавати String  і з нашого message дістаємо байти за допомогою методу .getBytes()*/
    public void sendMessage(String message) {
        connectThread.getRThread().sendMessage(message.getBytes());
    }

}
