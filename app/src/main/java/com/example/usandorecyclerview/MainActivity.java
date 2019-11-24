package com.example.usandorecyclerview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter defaultAdapter;
    public final static int VIC_REQUEST_ENABLE_BT = 1;
    private Button txtVerDispositivos;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.recyclerView = findViewById(R.id.ReciclerListaDispositivosBonded);
        recyclerView.setAdapter(new Adapter());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        defaultAdapter = BluetoothAdapter.getDefaultAdapter();
       // BluetoothAdapter defaultAdapter2 = BluetoothAdapter.getDefaultAdapter();

        setListenersBts();
    }

    private void setListenersBts() {

        this.txtVerDispositivos = findViewById(R.id.btn_verDispositivosBonded);
        this.txtVerDispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encenderBTVIC();
                recyclerView.setAdapter(new Adapter(getDispositivosVinculados()));
            }

            private ArrayList<ItemBT> getDispositivosVinculados() {
                ArrayList<ItemBT> listaBT = new ArrayList<>();

                Set<BluetoothDevice> bondedDevices = defaultAdapter.getBondedDevices();

                for(BluetoothDevice deviceActual : bondedDevices){
                    //los que agrega todos son vinculados.
                    listaBT.add(new ItemBT(deviceActual.getName(), deviceActual.getAddress(), true));
                    
                }

                return listaBT;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case MainActivity.VIC_REQUEST_ENABLE_BT:
                mostrarToast(VIC_REQUEST_ENABLE_BT + " Encendí blutut xd");
                Log.d("ActivityResult", "Bluetooth turning");
                break;
        }

    }

    public void encenderBTVIC(){
        if(defaultAdapter!=null && !defaultAdapter.isEnabled()){

            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT,VIC_REQUEST_ENABLE_BT);

        }
    }

    public void mostrarToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
