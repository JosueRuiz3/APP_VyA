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
    private venta_adapter adapter;
    private List<Datastore> activity;


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

    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening(); // comenzamos a escuchar cambios en el adapter
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening(); // dejamos de escuchar cambios en el adapter
    }
}