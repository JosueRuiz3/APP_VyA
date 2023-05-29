package com.josue.app_vya;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.josue.app_vya.adapter.cliente_adapter;

public class DetailsCustomerFragment extends Fragment {

    TextInputEditText nombre_cliente, nombre_producto, cantidad, precio_unitario, talla, total;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private cliente_adapter adapter;
    String id_ventas;
    FirebaseFirestore mfirestore;
    String idd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_ventas = getArguments().getString("id_ventas");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details_customer, container, false);

        cantidad = v.findViewById(R.id.cantidad);
        precio_unitario = v.findViewById(R.id.precio_unitario);
        total = v.findViewById(R.id.total);
        nombre_cliente = v.findViewById(R.id.nombre_cliente);
        nombre_producto = v.findViewById(R.id.nombre_producto);
        talla = v.findViewById(R.id.talla);

        db = FirebaseFirestore.getInstance();

        mfirestore = FirebaseFirestore.getInstance();
        Bundle args = getActivity().getIntent().getExtras();
        String id = args.getString("id_ventas");

        idd = id;
        get(id);
        showData(id);
        return v;
    }

    private void get(String id) {
        mfirestore.collection("ventas").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showData(String id) {

        DocumentReference clienteRef = mfirestore.collection("ventas").document(id_ventas).collection("clientes").document(id);

        clienteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // obtener los datos del cliente
                    String qty = documentSnapshot.getString("cantidad");
                    String p_unitary = documentSnapshot.getString("precio_unitario");
                    String to = documentSnapshot.getString("total");
                    String n_customer = documentSnapshot.getString("nombre_cliente");
                    String n_product = documentSnapshot.getString("nombre_producto");
                    String size = documentSnapshot.getString("talla");

                    cantidad.setText(qty);
                    precio_unitario.setText(p_unitary);
                    talla.setText(size);
                    total.setText(to);
                    nombre_cliente.setText(n_customer);
                    nombre_producto.setText(n_product);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al obtener los datos!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

