package com.keven.krokomierz;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by Keven on 18.04.2017.
 */

public class HandleSTM extends Thread {

    private static final String TAG = "MyActivity";
    private final double meterAsOneStep = 0.762;

    private final InputStream inStream;
    private ArrayList textViewStorage;

    private int valueSteps;
    private int valueCalories;
    private double valueMeters;
    private TextView textViewSteps;
    private TextView textViewCaloriesVar;
    private TextView textViewMetersVar;
    String resultMeters;

    public HandleSTM(BluetoothSocket socket, ArrayList textViewStorage) {
        this.textViewStorage = textViewStorage;
        prepereTextViews();
        InputStream tmpIn = null;

        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        inStream = tmpIn;
    }

    private void prepereTextViews() {
        textViewSteps = (TextView) textViewStorage.get(0);
        textViewMetersVar = (TextView) textViewStorage.get(1);
        textViewCaloriesVar = (TextView) textViewStorage.get(2);
    }

    public void run() {


        while (true) {
            try {
                byte[] buffer = new byte[32];
                inStream.read(buffer);
                increaseValueAfterStep();
      } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }

    private void increaseValueAfterStep() {
        increaseValueOfSteps();
        increaseValueOfMeters();
        increaseValueOfCalories();


        textViewSteps.post(new Runnable() {
            public void run() {
                textViewSteps.setText(Integer.toString(valueSteps));
                textViewMetersVar.setText(resultMeters);
                textViewCaloriesVar.setText(Integer.toString(valueCalories));
            }
        });
    }

    private void increaseValueOfSteps() {
        valueSteps = Integer.parseInt(textViewSteps.getText().toString());
        valueSteps += 1;
    }

    private void increaseValueOfMeters() {
        valueMeters = Double.parseDouble(textViewMetersVar.getText().toString());
        valueMeters += meterAsOneStep;
        BigDecimal bd = new BigDecimal(valueMeters);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        resultMeters = Double.toString(bd.doubleValue());
    }

    private void increaseValueOfCalories() {
        valueCalories = Integer.parseInt(textViewCaloriesVar.getText().toString());
        if (valueSteps % 20 == 0) {
            valueCalories += 1;
        }
    }

}