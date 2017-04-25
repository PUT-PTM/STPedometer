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

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewSteps;
    private Button mButtonReset;
    private Button mButtonConnect;
    private Button mButtonCancel;
    private Button mButtonConfirm;
    private PopupWindow mPopupWindow;
    private BluetoothAdapter mBluetoothAdapter;
    private LayoutInflater mLayoutInflater;
    private ConstraintLayout mConstraintLayout;
    private final int REQUEST_ENABLE_BT = 1;
    private ConnectThread mConnectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewSteps = (TextView) findViewById(R.id.textViewSteps);
        mButtonReset = (Button) findViewById(R.id.buttonReset);
        mButtonConnect = (Button) findViewById(R.id.buttonConnect);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.bgConstraintLayout);

        // Resets steps
        mButtonReset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                 // Popup window configuration
                mLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) mLayoutInflater.inflate(R.layout.reset_layout,null);

                // Metrics needed for margins
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = dm.widthPixels;
                int height = dm.heightPixels;

                mPopupWindow = new PopupWindow(container,(int)(width*.55),(int)(height*.16),true);
                mPopupWindow.showAtLocation(mConstraintLayout, Gravity.CENTER,0,0);

                TextView tv = (TextView) container.findViewById(R.id.textViewConfirm);
                setMargins(tv, (int)(width*.55*.25),(int)(height*.16*.1),0,0);

                mButtonCancel = (Button) container.findViewById(R.id.buttonCancel);
                setMargins(mButtonCancel, 0,(int)(height*.16*.4),(int)(width*.55*.05),0);
                mButtonCancel.setOnClickListener(cancel_button);

                mButtonConfirm = (Button) container.findViewById(R.id.buttonConfirm);
                setMargins(mButtonConfirm, (int)(width*.55*.05),(int)(height*.16*.4),0,0);
                mButtonConfirm.setOnClickListener(confirm_button);
            }
        });

        // Bluetooth configuration
        mButtonConnect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Returns a BluetoothAdapter that represents
                // the device's own Bluetooth adapter
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(getApplicationContext(), "Device does not support Bluetooth",Toast.LENGTH_LONG).show();
                }
                // Checks if bluetooth is enabled
                if (!mBluetoothAdapter.isEnabled()) {
                    // if not let the user enable it
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                    //wait until user enables bluetooth
                    while(!mBluetoothAdapter.isEnabled());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Bluetooth already on", Toast.LENGTH_LONG).show();

                }

                // Checks if desired device is already known
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

               if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress();
                        // if our device is found make a connection and break the loop
                        if(device.getName().equals("HC-05"))
                        {
                            Toast.makeText(getApplicationContext(), "Connected to HC-05",Toast.LENGTH_LONG).show();
                            (mConnectThread = new ConnectThread(device,mBluetoothAdapter,mTextViewSteps)).start();
                            break;
                        }
                    }
                }
            }
        });
    }


    // Occurs after clicking reset button
    // Cancels values of counted steps
    private View.OnClickListener cancel_button = new View.OnClickListener() {
        public void onClick(View v) {
            mPopupWindow.dismiss();
        }
    };

    // Occurs after clicking reset button
    // Resets values of counted steps
    private View.OnClickListener confirm_button = new View.OnClickListener() {
        public void onClick(View v) {
            mTextViewSteps.setText("0");
            mPopupWindow.dismiss();
        }
    };

    // Sets margins (used for popup window)
    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}
