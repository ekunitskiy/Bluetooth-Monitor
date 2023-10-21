package com.example.ble_sendingdata_oncomand;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.ble_sendingdata_oncomand.adapter.BtConsts;
import com.example.ble_sendingdata_oncomand.bluetooth.BtConnection;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    //private static final boolean TODO = true;
    private MenuItem menuItem;
    private BluetoothAdapter btAdapter;
    private final int ENABLE_REQUEST = 15;
    private SharedPreferences pref;
    private BtConnection btConnection;
    private Button bA;
    private Button bB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bA = findViewById(R.id.buttonA);
        bB = findViewById(R.id.buttonB);
        init();
        bA.setOnClickListener(v -> {
            btConnection.sendMessage("A");
        });
        bB.setOnClickListener(v -> {
            btConnection.sendMessage("B");
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuItem = menu.findItem(R.id.id_bt_button);
        setBtIcon();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.id_bt_button) {
            if (!btAdapter.isEnabled()) {
                enableBT();
            } else {
//*             //Тут ми будемо додавати чек пермішон для роботи нашого коду btAdapter.disable()

                btAdapter.disable();
                menuItem.setIcon(R.drawable.baseline_bt_enable);

            }
        } else if (item.getItemId() == R.id.id_menu) {
            if (btAdapter.isEnabled()) {
                Intent i = new Intent(MainActivity.this, BtListActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Pls turn on bluetooth", Toast.LENGTH_SHORT).show();
            }

        } else if (item.getItemId() == R.id.id_connect) {
            btConnection.connect();
        }
        return super.onOptionsItemSelected(item);
    }

    //Тут ми "ловимо" дозвіл користувача на ввімкнення блютуз
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*Тут ми перевіряємо відповідь на наш запит - тому ми порівнюємо
         чи рівний код який прийшов коду нашого запиту, щоб знати що нам прийшла відповідь на
         мій запит по блютузу*/
        if (requestCode == ENABLE_REQUEST) {
            /*Тут ми перевіряємо чи був надай дозвіл на ввімкнення блютуз, тобто чи був ввімкнений блютуз*/
            if (requestCode == RESULT_OK) {
                setBtIcon();
            }

        }
    }

    private void setBtIcon() {
        if (btAdapter.isEnabled()) {

            menuItem.setIcon(R.drawable.baseline_bt_disable);
        } else {
            menuItem.setIcon(R.drawable.baseline_bt_enable);

        }
    }

    private void init() {

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pref = getSharedPreferences(BtConsts.MY_PREF, Context.MODE_PRIVATE);
        btConnection = new BtConnection(this);

    }

    private void enableBT() {
        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//*        //Тут ми будемо додавати чек пермішон для роботи нашого коду і
        startActivityForResult(i, ENABLE_REQUEST);
    }

}