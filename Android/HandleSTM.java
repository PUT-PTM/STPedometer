package com.keven.krokomierz;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Keven on 18.04.2017.
 */

public class HandleSTM extends Thread{

    private final InputStream mInStream;
    private final BluetoothSocket mSocket;
    private final OutputStream mOutStream;
    private TextView mTextViewSteps;
    private static final String TAG = "MyActivity";
    private byte[] mBuffer; // mmBuffer store for the stream
    private int valueSteps;


    public HandleSTM(BluetoothSocket socket, TextView textView) {
        mTextViewSteps = textView;
        mSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mInStream = tmpIn;
        mOutStream = tmpOut;
    }

    public void run() {
        mBuffer = new byte[1024];
        int numBytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.

                numBytes = mInStream.read(mBuffer);

                // increase value of steps
                valueSteps = Integer.parseInt(mTextViewSteps.getText().toString());
                valueSteps+=1;
                mTextViewSteps.post(new Runnable() {
                                  public void run() {
                                      mTextViewSteps.setText(Integer.toString(valueSteps));
                                  }
                });


            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }
}
