package com.example.usandorecyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = Adapter.class.getSimpleName();
    private ArrayList<ItemBT> listItemsBT;

    private static final int TYPE_VINCULADO= 0;
    private static final int TYPE_NO_VINCULADO = 1;

    //para probar la lista.
    public Adapter() {
        super();
        this.listItemsBT = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i<50;i++){
            listItemsBT.add(new ItemBT("El vic xd" + i, "10:12:... NUMBER:  "+i, random.nextBoolean()));
        }
    }

    //should create the view, and return a matching ViewHolder,
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Infla una variable del tipo itemBT


        int layout = viewType == TYPE_VINCULADO?R.layout.itembt_vinculado:R.layout.itembt;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent,false);
        //retorna una instancia del tipo ViewHolder (clase estatica que hemos creado) :v
        //como parametro necesita una view, es decir la instancia que acabamos de inflar a partir del layout que le indicamos.
        //de la clase interna :v
        return new ViewHolder(view);
    }

    //should fill the ViewHolder with data from item at position position,
    //los elementos internos los obtenemos fon fiviewbyid... a partir del contexto del view inflado que llega.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ItemBT itemBT = listItemsBT.get(position);
        //como cambia la arquitectura de listview a recyclerview. para reciclar cada row , entonces solo se vuelve a llenar la info del objeto a partir del onBinViewHolder
        //así no sé tiene que llamas una nueva instancia.
        holder.nombreItemBT.setText(itemBT.getName() + (itemBT.isVinculado()?" VINCULADO":" ") );
        holder.macItemBT.setText(itemBT.getMAC());
      //  ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        //holder.selectOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public int getItemViewType(int position) {

        final ItemBT itemBT = listItemsBT.get(position);

       return itemBT.isVinculado()?TYPE_VINCULADO:TYPE_NO_VINCULADO;
     //   return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return listItemsBT.size();
    }

    public Adapter(ArrayList<ItemBT> listItemsBT) {
        super();
        this.listItemsBT = listItemsBT;
    }

    //esta clase le da valores a la instancia inflada que nos pasará

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        TextView nombreItemBT;
        TextView macItemBT;

        View selectOverlay;

        public ViewHolder(View itemView) {
            super(itemView);
            //retorna un ViewHolder
            //es decir un almacen de objetos que van a referenciar
            this.nombreItemBT = (TextView) itemView.findViewById(R.id.txt_nameBT);
            this.macItemBT = (TextView) itemView.findViewById(R.id.txt_mac);
            this.selectOverlay = itemView.findViewById(R.id.selected_overlay);

            //en lugar de pasarle una instancia anonima, se le pasa this, para que llame al metodo que le corresponda de su viewholder.
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
          //  Log.d(TAG, "Item clicked at position " + getPosition());
            Log.d(TAG, "Item clicked at position " + getLayoutPosition());
            Log.d(TAG, "Item clicked at position " + getAdapterPosition());
          //  this.selectOverlay.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onLongClick(View view) {
            Log.d(TAG, "Item clicked at position " + getLayoutPosition());
            Log.d(TAG, "Item clicked at position " + getAdapterPosition());
            return true;
        }
    }

}
