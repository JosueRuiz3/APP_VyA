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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private cliente_adapter adapter;
    private String idd;
    private ImageView btncerrarCampo ;
    private RelativeLayout btnMostrarCampo,  editTextCampo;
    private TextView txtproducto;
    private TextInputEditText buscar;

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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        db = FirebaseFirestore.getInstance();
        btnMostrarCampo = v.findViewById(R.id.btnMostrarCampo);
        btncerrarCampo = v.findViewById(R.id.btncerrarCampo);
        editTextCampo = v.findViewById(R.id.editTextCampo);
        txtproducto = v.findViewById(R.id.txtproducto);
        buscar = v.findViewById(R.id.buscar);


        setUpRecyclerView(); // configuramos el RecyclerView

        btnMostrarCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCampo.getVisibility() == View.GONE) {
                    editTextCampo.setVisibility(View.VISIBLE);
                    btncerrarCampo.setVisibility(View.VISIBLE);
                    btnMostrarCampo.setVisibility(View.GONE);
                    txtproducto.setVisibility(View.GONE);
                }
            }
        });

        btncerrarCampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCampo.getVisibility() == View.VISIBLE) {
                    editTextCampo.setVisibility(View.GONE);
                    btncerrarCampo.setVisibility(View.GONE);
                    btnMostrarCampo.setVisibility(View.VISIBLE);
                    txtproducto.setVisibility(View.VISIBLE);
                    limpiarCampos();
                }
            }
        });

        return v;
    }

    private void setUpRecyclerView() {
        Bundle args = getActivity().getIntent().getExtras();
        String idVenta = args.getString("idVenta");

        idd = idVenta;

        // Obtener una referencia al documento de la colección principal que contiene la subcolección
        DocumentReference ventaRef = db.collection("Ventas").document(idVenta);

        Query query = ventaRef.collection("Clientes").orderBy("nombre_cliente", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<cliente> options = new FirestoreRecyclerOptions.Builder<cliente>()
                .setQuery(query, cliente.class).build();

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

    private void limpiarCampos(){
        buscar.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening(); // dejamos de ver cambios en el adapter
    }
}