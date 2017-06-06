package com.keven.krokomierz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Keven on 17.04.2017.
 */

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_ENABLE_BT = 1;

    private TextView textViewSteps;
    private TextView textViewCaloriesVar;
    private TextView textViewMetersVar;
    private Button buttonReset;
    private Button buttonConnect;
    private PopupWindow popupWindowConfirmReset;
    private BluetoothAdapter bluetoothAdapterOfDevice;
    ViewGroup container;

    ArrayList<TextView> storageOfTextViewsForFurtherPassing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLocalVariables();
        setTextViewStorage();

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setButtonConnect();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setButtonReset();
            }
        });
    }

    private void setLocalVariables() {
        textViewSteps = (TextView) findViewById(R.id.textViewSteps);
        textViewCaloriesVar = (TextView) findViewById(R.id.textViewCaloriesVar);
        textViewMetersVar = (TextView) findViewById(R.id.textViewMetersVar);
        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonConnect = (Button) findViewById(R.id.buttonConnect);
    }

    private void setTextViewStorage() {
        storageOfTextViewsForFurtherPassing = new ArrayList<>();
        storageOfTextViewsForFurtherPassing.add(textViewSteps);
        storageOfTextViewsForFurtherPassing.add(textViewMetersVar);
        storageOfTextViewsForFurtherPassing.add(textViewCaloriesVar);
    }

    private void setButtonConnect() {
        setAndCheckIfSupportedBluetoothAdapter();
        if (!isBluetoothEnabled()) {
            intentToEnableBluetooth();
        }
        connectToDevice();
    }

    private void setAndCheckIfSupportedBluetoothAdapter() {
        bluetoothAdapterOfDevice = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapterOfDevice == null) {
            Toast.makeText(getApplicationContext(), "Device does not support Bluetooth", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isBluetoothEnabled() {
        if (!bluetoothAdapterOfDevice.isEnabled()) {
            return false;
        }
        Toast.makeText(getApplicationContext(), "Bluetooth already on", Toast.LENGTH_LONG).show();
        return true;
    }

    private void intentToEnableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        try {
            TimeUnit.MILLISECONDS.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connectToDevice() {
        BluetoothDevice btDevice = getDevice();
        if (btDevice == null) {
            Toast.makeText(getApplicationContext(), "Cannot connect.", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(getApplicationContext(), "Connected to " + btDevice.getName(), Toast.LENGTH_LONG).show();
        (new ConnectThread(btDevice, storageOfTextViewsForFurtherPassing)).start();
    }

    private BluetoothDevice getDevice() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapterOfDevice.getBondedDevices();
        if (pairedDevices.size() > 0) {

            for (BluetoothDevice device : pairedDevices) {

                if (device.getName().equals("HC-05")) {
                    return device;
                }
            }
        }
        return null;
    }

    private void setButtonReset() {
        setPopupWindowForConfirmationOfResetingSteps();
    }

    private void setPopupWindowForConfirmationOfResetingSteps() {
        setPopupWindowLayout();
    }

    private void setPopupWindowLayout() {
        setContainer();
        int widthOfDisplay = getWidthOfDisplay();
        int heightOfDisplay = getHeightOfDisplay();

        int widthOfPopup = (int) (widthOfDisplay * .55);
        int heightOfPopup = (int) (heightOfDisplay * .16);
        int widthOfTextViewConfirm = (int) (widthOfDisplay * .55 * .25);
        int heightOfTextViewConfirm = (int) (heightOfDisplay * .16 * .1);
        int widthOfButtonCancel = (int) (heightOfDisplay * .16 * .4);
        int heightOfButtonCancel = (int) (widthOfDisplay * .55 * .05);
        int widthOfButtonConfirm = (int) (widthOfDisplay * .55 * .05);
        int heightOfButtonConfim = (int) (heightOfDisplay * .16 * .4);

        setPopupWindowLayout(widthOfPopup, heightOfPopup);
        setTextViewConfirm(widthOfTextViewConfirm, heightOfTextViewConfirm);
        setButtonCancel(widthOfButtonCancel, heightOfButtonCancel);
        setButtonConfirm(widthOfButtonConfirm, heightOfButtonConfim);
    }

    private void setContainer() {
        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container = (ViewGroup) layoutInflater.inflate(R.layout.reset_layout, null);
    }

    private int getWidthOfDisplay() {
        return getDisplaysMetrics().widthPixels;
    }

    private int getHeightOfDisplay() {
        return getDisplaysMetrics().heightPixels;
    }

    private DisplayMetrics getDisplaysMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    private void setPopupWindowLayout(int widthOfPopup, int heightOfPopup) {
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.bgConstraintLayout);
        popupWindowConfirmReset = new PopupWindow(container, widthOfPopup, heightOfPopup, true);
        popupWindowConfirmReset.showAtLocation(constraintLayout, Gravity.CENTER, 0, 0);
    }

    private void setTextViewConfirm(int widthOfTextViewConfirm, int heightOfTextViewConfirm) {
        TextView textViewConfirm = (TextView) container.findViewById(R.id.textViewConfirm);
        setMargins(textViewConfirm, widthOfTextViewConfirm, heightOfTextViewConfirm, 0, 0);
    }

    private void setButtonCancel(int widthOfButtonCancel, int heightOfButtonCancel) {
        Button buttonCancel = (Button) container.findViewById(R.id.buttonCancel);
        setMargins(buttonCancel, 0, widthOfButtonCancel, heightOfButtonCancel, 0);
        buttonCancel.setOnClickListener(cancel_button);
    }

    private void setButtonConfirm(int widthOfButtonConfirm, int heightOfButtonConfim) {
        Button buttonConfirm = (Button) container.findViewById(R.id.buttonConfirm);
        setMargins(buttonConfirm, widthOfButtonConfirm, heightOfButtonConfim, 0, 0);
        buttonConfirm.setOnClickListener(confirm_button);
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    // Occurs after clicking reset button
    // Cancels reseting steps
    private View.OnClickListener cancel_button = new View.OnClickListener() {
        public void onClick(View v) {
            popupWindowConfirmReset.dismiss();
        }
    };

    // Occurs after clicking reset button
    // Confirms and resets values of counted steps
    private View.OnClickListener confirm_button = new View.OnClickListener() {
        public void onClick(View v) {
            textViewSteps.setText("0");
            textViewCaloriesVar.setText("0");
            textViewMetersVar.setText("0");
            popupWindowConfirmReset.dismiss();
        }
    };

}
