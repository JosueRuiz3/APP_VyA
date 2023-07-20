package com.josue.app_vya.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.josue.app_vya.DetailsActivity;
import com.josue.app_vya.R;
import com.josue.app_vya.model.venta;

public class venta_adapter extends FirestoreRecyclerAdapter<venta, venta_adapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    Context context;

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
    public venta_adapter(@NonNull FirestoreRecyclerOptions<venta> options, FragmentActivity context) {
        super(options);
        this.context = context;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull venta Venta) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.nombre_producto.setText(Venta.getNombre_producto());
        holder.descripcion.setText(Venta.getDescripcion());
        holder.precio_venta.setText(Venta.getPrecio_venta());

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DetailsActivity.class);
                i.putExtra("idVenta", id);
                context.startActivity(i);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_productos, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre_producto, descripcion, precio_venta;
        CardView editar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            editar = itemView.findViewById(R.id.btn_editar);
            nombre_producto = itemView.findViewById(R.id.nombre_producto);
            descripcion = itemView.findViewById(R.id.descripcion);
            precio_venta = itemView.findViewById(R.id.precio_venta);
        }
    }
}
