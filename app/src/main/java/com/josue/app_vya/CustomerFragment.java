package com.josue.app_vya;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.google.firebase.firestore.remote.Datastore;
import com.google.firebase.storage.StorageReference;
import com.josue.app_vya.adapter.cliente_adapter;
import com.josue.app_vya.adapter.venta_adapter;
import com.josue.app_vya.model.cliente;
import com.josue.app_vya.model.venta;

import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private cliente_adapter adapter;
    private String id_ventas;
    private String idd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_ventas = getArguments().getString("id_ventas");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        db = FirebaseFirestore.getInstance();

        Bundle args = getActivity().getIntent().getExtras();
        String id = args.getString("id_ventas");

        idd = id;
        get(id);

        setUpRecyclerView(); // configuramos el RecyclerView

        return v;
    }

    private void get(String id) {
        db.collection("ventas").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerView() {
        Bundle args = getActivity().getIntent().getExtras();
        String id = args.getString("id_ventas");

        idd = id;

        // Obtener una referencia al documento de la colección principal que contiene la subcolección
        DocumentReference ventaRef = db.collection("ventas").document(id);

        // Obtener una referencia a la subcolección del documento principal
        CollectionReference clientesRef = ventaRef.collection("clientes");

        // Crear una nueva consulta para la subcolección "clientes"
        Query query = clientesRef.orderBy("nombre_cliente", Query.Direction.ASCENDING);

        // Crear opciones para el adaptador
        FirestoreRecyclerOptions<cliente> options = new FirestoreRecyclerOptions.Builder<cliente>()
                .setQuery(query, cliente.class)
                .build();

        // Configurar el adaptador en el RecyclerView
        adapter = new cliente_adapter(options, getActivity());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening(); // comenzamos a ver cambios en el adapter
        recyclerView.getRecycledViewPool().clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening(); // dejamos de ver cambios en el adapter
    }
}