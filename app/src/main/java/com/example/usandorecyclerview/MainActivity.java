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
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter defaultAdapter;
    public final static int VIC_REQUEST_ENABLE_BT = 1;
    private Button txtVerDispositivos;
    private Button btn_scanDispositivos;
    RecyclerView recyclerView;
    private ArrayList<BluetoothDevice> listaBluetoothDeviceFound = new ArrayList<>();

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
                recyclerView.setAdapter(new Adapter(getDispositivosVinculados(),new LeerClickedElement(){

                    @Override
                    public void clickedElement(ItemBT itemBTClicked){

                        //Inten activity...
                       // if(itemBTClicked.isVinculado()){

                       // }
                        //else{
                            //BluetoothDevice bluetoothDevice = new BluetoothDevice(itemBTClicked.getMAC());
                            for(BluetoothDevice btD : listaBluetoothDeviceFound){

                                if(btD.getAddress().equals(itemBTClicked.getMAC())){

                                    ParcelUuid[] uuids = btD.getUuids();
                                   // mostrarToast(defaultAdapter.isDiscovering() + " uuids ");

                                    try {
                                        defaultAdapter.cancelDiscovery();
                                        mostrarToast(defaultAdapter.isDiscovering() + " a ");
                                        boolean b = btD.fetchUuidsWithSdp();
                                        ParcelUuid[] uuids4 = btD.getUuids();
                                        mostrarToast(defaultAdapter.isDiscovering() + " uuids4 ");
                                        BluetoothSocket socket;

                                        if(uuids4!=null) {

                                            socket = btD.createRfcommSocketToServiceRecord(uuids4[0].getUuid());
                                            //defaultAdapter.cancelDiscovery();
                                            // UUID Identificador unico de URI para esta aplicacion
                                            //private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                                            // UUID para chat con otro Android      ("fa87c0d0-afac-11de-8a39-0800200c9a66");
                                            // UUID para modulos BT RN42            ("00001101-0000-1000-8000-00805F9B34FB");
                                            //  btD.crea
                                            socket.connect();
                                            mostrarToast(socket.isConnected() + "Conected ");

                                            btD.createBond();
                                            mostrarToast(socket.getInputStream().read()+"Read Stream");

                                            mostrarToast(defaultAdapter.isDiscovering() + " a ");
                                            mostrarToast("cre :V");
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        mostrarToast(e.getMessage());

                                    }
                                    //   btD.createBond();
                                //    Bluetooth



                                    break;
                                }
                            }
                       // }
                    }

                }));
            }


        });

        this.btn_scanDispositivos = findViewById(R.id.btn_scannDevices);
        this.btn_scanDispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encenderBTVIC();
                //listaBT.add(new ItemBT("VIc", "XDXD", false));, no funciona, no actualiza, ocuparia hacerce desde el recyclerview
                //recyclerView.refreshDrawableState(); no probé esto xd

                // BLUETOOTH_ADMIN
                if(defaultAdapter.startDiscovery())//le dice a la antena que empiece a escanear,durante 12 segundos.
                {
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(vic_ReceiverBluetooth, filter);
                }

            }
        });

    }

    ArrayList<ItemBT> listaBT = new ArrayList<>();
    private ArrayList<ItemBT> getDispositivosVinculados() {


        Set<BluetoothDevice> bondedDevices = defaultAdapter.getBondedDevices();

        for(BluetoothDevice deviceActual : bondedDevices){
            //los que agrega todos son vinculados.
            listaBluetoothDeviceFound.add(deviceActual);
            listaBT.add(new ItemBT(deviceActual.getName(), deviceActual.getAddress(), true));

        }

        return listaBT;
    }


    public BroadcastReceiver vic_ReceiverBluetooth = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //cuando encuentra un dispositivo llama a esta funcion de este objeto y entra a este IF.
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listaBluetoothDeviceFound.add(bluetoothDevice);

                if(bluetoothDevice.fetchUuidsWithSdp()){
                    ParcelUuid[] uuids = bluetoothDevice.getUuids();
                    Parcelable uuidExtra = intent.getParcelableExtra(BluetoothDevice.EXTRA_UUID);

                    int a =1;
                    a++;
                }
                String deiceName = bluetoothDevice.getName();
                String deviceMAC = bluetoothDevice.getAddress();

                listaBT.add(new ItemBT(deiceName, deviceMAC, false));
                //notifica toda la lista
                //recyclerView.getAdapter().notifyDataSetChanged();
                //notifica el cambio en un item
                recyclerView.getAdapter().notifyItemChanged(recyclerView.getAdapter().getItemCount()-1);
            }
        }


    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(vic_ReceiverBluetooth);
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  this.txtVerDispositivos.performClick();
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
