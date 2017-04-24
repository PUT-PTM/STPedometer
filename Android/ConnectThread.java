package com.keven.krokomierz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.util.UUID;
import android.widget.TextView;


import java.io.IOException;

/**
 * Created by Keven on 18.04.2017.
 */

public class ConnectThread extends Thread {
    private HandleSTM mHandleSTM;
    private TextView mTextView;
    private BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private final UUID MY_UUID = UUID.fromString("c1f5f63e-17c7-44fb-a037-c6eb48136c1f");
    private static final String TAG = "INFO";

    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter, TextView textView) {
        // Use a temporary object that is later assigned to mSocket
        BluetoothSocket tmp = null;
        mDevice = device;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mTextView = textView;
        mBluetoothAdapter = adapter;
        mSocket = tmp;
    }

    public void run() {

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mSocket.connect();
        } catch (IOException connectException) {
            // unable to connect; tries again
            try {
                mSocket = (BluetoothSocket) mDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mDevice,1);
                mSocket.connect();
            } catch (Exception rx){
                try{
                    // Unable to connect; close the socket and return.
                    mSocket.close();
                }catch(IOException ioe){
                    Log.e(TAG, "Could not close the client socket", ioe);
                    return;
                }
            }

        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        (mHandleSTM = new HandleSTM(mSocket,mTextView)).start();
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}
