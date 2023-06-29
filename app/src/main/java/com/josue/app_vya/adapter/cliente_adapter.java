package com.josue.app_vya.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.josue.app_vya.CustomerDetailsActivity;
import com.josue.app_vya.CustomerDetailsFragment;
import com.josue.app_vya.DetailsActivity;
import com.josue.app_vya.R;
import com.josue.app_vya.model.cliente;
import com.josue.app_vya.model.venta;

public class cliente_adapter extends FirestoreRecyclerAdapter<cliente, cliente_adapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FragmentManager fm;

    public Context getContext() {
        return context;
    }
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     * @param options
     * @param context
     */
    public cliente_adapter(@NonNull FirestoreRecyclerOptions<cliente> options, FragmentActivity context) {
        super(options);
        this.context = context;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull cliente_adapter.ViewHolder holder, int position, @NonNull cliente Cliente) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.nombre_cliente.setText(Cliente.getNombre_cliente());
        holder.cantidad.setText(Cliente.getCantidad());
        holder.total.setText(Cliente.getTotal());

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cliente clickedCliente = documentSnapshot.toObject(cliente.class);
                final String clienteId = documentSnapshot.getId();

                Intent intent = new Intent(getContext(), CustomerDetailsActivity.class);
                intent.putExtra("clienteId", clienteId);
                intent.putExtra("nombre_cliente", clickedCliente.getNombre_cliente());
                intent.putExtra("nombre_producto", clickedCliente.getNombre_producto());
                intent.putExtra("cantidad", clickedCliente.getCantidad());
                intent.putExtra("talla", clickedCliente.getTalla());
                intent.putExtra("precio_unitario", clickedCliente.getPrecio_unitario());
                intent.putExtra("total", clickedCliente.getTotal());
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public cliente_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_clientes, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre_cliente, nombre_producto, cantidad, precio_unitario, talla, total;
        RelativeLayout editar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            editar = itemView.findViewById(R.id.btn_editar);
            nombre_cliente = itemView.findViewById(R.id.nombre_cliente);
            cantidad = itemView.findViewById(R.id.cantidad);
            total = itemView.findViewById(R.id.total);
        }
    }
}
