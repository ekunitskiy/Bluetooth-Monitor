package com.example.ble_sendingdata_oncomand;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RecieveThread extends Thread {
    private BluetoothSocket socket;
    private InputStream inputS;
    private OutputStream outputS;
    private byte[] rBuffer;

    public RecieveThread(BluetoothSocket socket) {
        this.socket = socket;
        try{
           inputS = socket.getInputStream();
        } catch (IOException e) {
            Log.d(TAG, "Receive Thread: InputStream is not opened");
        }
        try{
            outputS = socket.getOutputStream();
        } catch (IOException e) {
            Log.d(TAG, "Receive Thread: InputStream is not opened");
        }
    }

    //Method for reading info from bluetooth device
    @Override
    public void run() {
        rBuffer = new byte[1024];
        while(true) {
            try{
                int size = inputS.read(rBuffer);  //Here we write in size of input info
                //Transfer our byte array to String
                String message = new String(rBuffer, 0, size);
                Log.d(TAG, "run:Message: " + message);
            } catch (IOException e) {
                Log.d(TAG, "run: Something wrong. Check your connection");
                break;
            }
        }
    }

    //method to send info for bluetooth device
    public void sendMessage(byte[] byteArray) {
        try{
            outputS.write(byteArray);
        } catch (IOException e){

        }
    }
}
