package com.josue.app_vya;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.josue.app_vya.adapter.cliente_adapter;
import com.josue.app_vya.model.cliente;

public class CustomerDetailsFragment extends Fragment {

    private boolean valid = true;
    private TextInputEditText nombre_cliente, nombre_producto, cantidad, precio_unitario, talla, total;
    private String idd, iddventas;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mainCollectionRef = db.collection("ventas");
    private DocumentReference documentRef = mainCollectionRef.document();
    private CollectionReference subCollectionRef = documentRef.collection("clientes");

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
        View v = inflater.inflate(R.layout.fragment_details_customer, container, false);

        cantidad = v.findViewById(R.id.cantidad);
        precio_unitario = v.findViewById(R.id.precio_unitario);
        total = v.findViewById(R.id.total);
        nombre_cliente = v.findViewById(R.id.nombre_cliente);
        nombre_producto = v.findViewById(R.id.nombre_producto);
        talla = v.findViewById(R.id.talla);

        db = FirebaseFirestore.getInstance();
        Bundle args = getArguments();
        if (args != null) {
            String id = args.getString("id");
            String nombreCliente = args.getString("nombre_cliente");
            String nombreProducto = args.getString("nombre_producto");
            String cantidadCliente = args.getString("cantidad");
            String precioUnitario = args.getString("precio_unitario");
            String tallaCliente = args.getString("talla");
            String totalCliente = args.getString("total");

            cantidad.setText(cantidadCliente);
            precio_unitario.setText(precioUnitario);
            total.setText(totalCliente);
            nombre_cliente.setText(nombreCliente);
            nombre_producto.setText(nombreProducto);
            talla.setText(tallaCliente);

            getClient(id);
        }

        return v;
    }
    private void getClient(String id) {
        // Obtener la referencia al documento del cliente en la subcolección
        DocumentReference clienteRef = mainCollectionRef.document(id);

        // Obtener los datos del cliente desde Firestore
        clienteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nombreCliente = documentSnapshot.getString("nombre_cliente");
                    String nombreProducto = documentSnapshot.getString("nombre_producto");
                    String cantidadCliente = documentSnapshot.getString("cantidad");
                    String precioUnitario = documentSnapshot.getString("precio_unitario");
                    String tallaCliente = documentSnapshot.getString("talla");
                    String totalCliente = documentSnapshot.getString("total");

                    // Actualizar los TextInputEditText con los valores recuperados
                    cantidad.setText(cantidadCliente);
                    nombre_producto.setText(nombreProducto);
                    precio_unitario.setText(precioUnitario);
                    total.setText(totalCliente);
                    nombre_cliente.setText(nombreCliente);
                    talla.setText(tallaCliente);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos del cliente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}