package com.keven.krokomierz;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


/**
 * Created by Keven on 18.04.2017.
 */

public class HandleSTM extends Thread {

    private final InputStream mInStream;
    private final BluetoothSocket mSocket;
    private final OutputStream mOutStream;
    private ArrayList textViewStorage;
    private static final String TAG = "MyActivity";
    private byte[] mBuffer; // mmBuffer store for the stream
    private int valueSteps;
    private int valueCalories;
    private double valueMeters;


    private TextView mTextViewSteps;
    private TextView mTextViewCaloriesVar;
    private TextView mTextViewMetersVar;
    String resultMeters;


    public HandleSTM(BluetoothSocket socket, ArrayList textViewStorage) {
        this.textViewStorage = textViewStorage;
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

    private void increaseValueAfterStep() {

        mTextViewSteps = (TextView) textViewStorage.get(0);
        mTextViewMetersVar = (TextView) textViewStorage.get(1);
        mTextViewCaloriesVar = (TextView) textViewStorage.get(2);

        // increase value of steps
        valueSteps = Integer.parseInt(mTextViewSteps.getText().toString());
        valueMeters = Double.parseDouble(mTextViewMetersVar.getText().toString());
        valueCalories = Integer.parseInt(mTextViewCaloriesVar.getText().toString());
        valueSteps += 1;

        // increase value of calories
        if (valueSteps % 20 == 0) {
            valueCalories += 1;
        }

        // increase value of meters walked
        valueMeters+=0.762;
        BigDecimal bd = new BigDecimal(valueMeters);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        resultMeters = Double.toString(bd.doubleValue());




        mTextViewSteps.post(new Runnable() {
            public void run() {
                mTextViewSteps.setText(Integer.toString(valueSteps));
                mTextViewMetersVar.setText(resultMeters);
                mTextViewCaloriesVar.setText(Integer.toString(valueCalories));
            }
        });
    }

    public void run() {
        mBuffer = new byte[1024];
        int numBytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
                numBytes = mInStream.read(mBuffer);
                increaseValueAfterStep();

            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }
}
