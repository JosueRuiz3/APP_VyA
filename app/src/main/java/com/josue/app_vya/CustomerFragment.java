package com.josue.app_vya;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        db = FirebaseFirestore.getInstance();


        setUpRecyclerView(); // configuramos el RecyclerView

        return v;
    }

    private void setUpRecyclerView() {
        // Obtener la referencia a la subcolección "clientes" dentro del documento actual
        CollectionReference clientesRef = db.collection("ventas").document().collection("clientes");

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
        recyclerView.getRecycledViewPool().clear();
        adapter.notifyDataSetChanged();
        adapter.startListening(); // comenzamos a escuchar cambios en el adapter
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening(); // dejamos de escuchar cambios en el adapter
    }
}