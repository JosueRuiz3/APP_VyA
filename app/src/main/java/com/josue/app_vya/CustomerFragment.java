package com.josue.app_vya;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class CustomerFragment extends Fragment {

    String id_cliente;
    boolean valid = true;
    CardView btneditar, btneliminar;
    TextInputEditText nombre_cliente, cantidad, precio_unitario, talla, total;
    FirebaseFirestore mfirestore;
    String idd;
    StorageReference storageReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_cliente = getArguments().getString("id_cliente");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer, container, false);

        cantidad = v.findViewById(R.id.cantidad);
        precio_unitario = v.findViewById(R.id.precio_unitario);
        total = v.findViewById(R.id.total);
        nombre_cliente = v.findViewById(R.id.nombre_cliente);
        talla = v.findViewById(R.id.talla);
        mfirestore = FirebaseFirestore.getInstance();

        Bundle args = getActivity().getIntent().getExtras();
        String id = args.getString("id_cliente");

        idd = id;
       // get(id);

        return v;
    }

    private void get(String id){
        mfirestore.collection("ventas").document().collection("clientes").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("nombre_cliente");
                String qty = documentSnapshot.getString("cantidad");
                String size = documentSnapshot.getString("talla");
                String total = documentSnapshot.getString("total");
                String price_u = documentSnapshot.getString("precio_unitario");

                nombre_cliente.setText(name);
                cantidad.setText(qty);
                talla.setText(size);
                talla.setText(total);
                precio_unitario.setText(price_u);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al obtener los datos!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}