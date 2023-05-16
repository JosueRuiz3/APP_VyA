package com.josue.app_vya;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.josue.app_vya.adapter.cliente_adapter;
import com.josue.app_vya.model.cliente;

public class CustomerDetailsFragment extends Fragment {

    private boolean valid = true;
    private TextInputEditText nombre_cliente, nombre_producto, cantidad, precio_unitario, talla, total;
    private FirebaseFirestore mfirestore;
    private cliente_adapter adapter;
    private String id_cliente, id_ventas;
    private String idd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (getArguments() != null) {
            id_cliente = args.getString("id_cliente");
            id_ventas = getArguments().getString("id_ventas");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_details, container, false);

        cantidad = v.findViewById(R.id.cantidad);
        precio_unitario = v.findViewById(R.id.precio_unitario);
        total = v.findViewById(R.id.total);
        nombre_cliente = v.findViewById(R.id.nombre_cliente);
        nombre_producto = v.findViewById(R.id.nombre_producto);
        talla = v.findViewById(R.id.talla);
        mfirestore = FirebaseFirestore.getInstance();

        Bundle args = getActivity().getIntent().getExtras();
        String id = args.getString("id_cliente");


        idd = id;
        getCLient(id);

        return v;
    }

    private void getCLient(String id){
        mfirestore.collection("clientes").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String qty = documentSnapshot.getString("cantidad");
                String c_name = documentSnapshot.getString("nombre_cliente");
                String p_name = documentSnapshot.getString("nombre_producto");
                String u_price = documentSnapshot.getString("precio_unitario");
                String size = documentSnapshot.getString("talla");
                String to = documentSnapshot.getString("total");

                cantidad.setText(qty);
                precio_unitario.setText(u_price);
                total.setText(to);
                nombre_cliente.setText(c_name);
                nombre_producto.setText(p_name);
                talla.setText(size);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al obtener los datos!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}