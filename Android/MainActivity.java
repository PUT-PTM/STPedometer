package com.keven.krokomierz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewSteps;
    private Button mButtonReset;
    private Button mButtonConnect;
    private Button mButtonCancel;
    private Button mButtonConfirm;
    private PopupWindow mPopupWindow;
    private LayoutInflater mLayoutInflater;
    private ConstraintLayout mConstraintLayout;
    private final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        //

        mTextViewSteps = (TextView) findViewById(R.id.textViewSteps);
        mButtonReset = (Button) findViewById(R.id.buttonReset);
        mButtonConnect = (Button) findViewById(R.id.buttonConnect);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.bgConstraintLayout);

        mButtonReset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Konfiguracja wyskakujacego okienka konfirmujacego
                mLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) mLayoutInflater.inflate(R.layout.reset_layout,null);

                mPopupWindow = new PopupWindow(container,600,300,true);
                mPopupWindow.showAtLocation(mConstraintLayout, Gravity.CENTER,0,0);
                mButtonCancel = (Button) container.findViewById(R.id.buttonCancel);
                mButtonCancel.setOnClickListener(cancel_button);

                mButtonConfirm = (Button) container.findViewById(R.id.buttonConfirm);
                mButtonConfirm.setOnClickListener(confirm_button);
            }
        });

        mButtonConnect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                }
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address
                    }
                }
            }
        });
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    private View.OnClickListener cancel_button = new View.OnClickListener() {
        public void onClick(View v) {
            mPopupWindow.dismiss();
        }
    };

    private View.OnClickListener confirm_button = new View.OnClickListener() {
        public void onClick(View v) {
            mTextViewSteps.setText("0");
            mPopupWindow.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mReceiver);
    }
}
