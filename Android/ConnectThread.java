package com.keven.krokomierz;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Keven on 18.04.2017.
 */

public class ConnectThread extends Thread {
    private static final String TAG = "INFO";

    private ArrayList<TextView> textViewStorage;
    private BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private final UUID MY_UUID = UUID.fromString("c1f5f63e-17c7-44fb-a037-c6eb48136c1f");

    public ConnectThread(BluetoothDevice device, ArrayList textViewStorage) {
        BluetoothSocket temporaryBtS = null;
        bluetoothDevice = device;

        try {
            temporaryBtS = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        this.textViewStorage = textViewStorage;
        bluetoothSocket = temporaryBtS;
    }

    public void run() {
        try {
            bluetoothSocket.connect();
        } catch (IOException connectException) {
            tryToConnectAgain();
        }
        (new HandleSTM(bluetoothSocket, textViewStorage)).start();
    }

    private void tryToConnectAgain() {
        try {
            bluetoothSocket = (BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(bluetoothDevice, 1);
            bluetoothSocket.connect();
        } catch (Exception ex) {
            cancel();
        }
    }

    private void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}
