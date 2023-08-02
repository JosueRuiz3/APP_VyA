package com.josue.app_vya;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.remote.Datastore;
import com.josue.app_vya.adapter.venta_adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.josue.app_vya.adapter.venta_adapter;
import com.josue.app_vya.model.venta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private venta_adapter adapter;
    private RelativeLayout btnMostrarCampo, editTextCampo, btncerrarCampo;
    private TextView txtproducto;
    private TextInputEditText buscar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView =  v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseFirestore.getInstance();
        btnMostrarCampo = v.findViewById(R.id.btnMostrarCampo);
        btncerrarCampo = v.findViewById(R.id.btncerrarCampo);
        editTextCampo = v.findViewById(R.id.editTextCampo);
        txtproducto = v.findViewById(R.id.txtproducto);
        buscar = v.findViewById(R.id.buscar);

        setUpRecyclerView();

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
                    // Ocultar los elementos necesarios
                    editTextCampo.setVisibility(View.GONE);
                    btncerrarCampo.setVisibility(View.GONE);
                    btnMostrarCampo.setVisibility(View.VISIBLE);
                    txtproducto.setVisibility(View.VISIBLE);
                    limpiarCampos();

                    // Cerrar el teclado
                    InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        // Agregar TextWatcher al TextInputEditText para realizar la búsqueda en tiempo real
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Realizar la búsqueda cada vez que el texto cambie
                buscarProductos(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return v;
    }

    private void buscarProductos(String query) {
        Query buscarQuery = db.collection("Ventas")
                .orderBy("nombre_producto")
                .startAt(query)
                .endAt(query + "\uf8ff");

        FirestoreRecyclerOptions<venta> options = new FirestoreRecyclerOptions.Builder<venta>()
                .setQuery(buscarQuery, venta.class).build();

        adapter.updateOptions(options);
        adapter.notifyDataSetChanged(); // Agrega esta línea para notificar al adaptador sobre los cambios en los datos filtrados.
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        Query query = db.collection("Ventas").orderBy("fecha_creacion", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<venta> options = new FirestoreRecyclerOptions.Builder<venta>()
                .setQuery(query, venta.class).build();

        adapter = new venta_adapter(options, getActivity());
        recyclerView.setAdapter(adapter);
    }


    private void limpiarCampos(){
        buscar.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        recyclerView.getRecycledViewPool().clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
