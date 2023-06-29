package com.josue.app_vya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.josue.app_vya.model.cliente;

import java.util.HashMap;
import java.util.Map;

public class CustomerDetailsActivity extends AppCompatActivity {

   private CardView btneditar, btnelimimar;
    private TextInputEditText nombre_cliente, nombre_producto, cantidad, precio_unitario, talla, total;
    private String idd, clienteId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        cantidad = findViewById(R.id.cantidad);
        precio_unitario = findViewById(R.id.precio_unitario);
        total = findViewById(R.id.total);
        nombre_cliente = findViewById(R.id.nombre_cliente);
        nombre_producto = findViewById(R.id.nombre_producto);
        talla = findViewById(R.id.talla);
        btneditar = findViewById(R.id.btneditar);
        btnelimimar = findViewById(R.id.btneliminar);
        mfirestore = FirebaseFirestore.getInstance();

        String id = getIntent().getStringExtra("clienteId");
        idd = id;
        obtenerDatosCliente(id);

    }

    private void get(String idVentas) {
        mfirestore.collection("ventas").document(idVentas).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerDetailsActivity.this, "Error al obtener los datos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerDatosCliente(String id) {
        idd = id;

        Bundle args = getIntent().getExtras();
        id = args.getString("clienteId");
        String nombre_clienteA = args.getString("nombre_cliente");
        String nombre_productoA = args.getString("nombre_producto");
        String cantidadA = args.getString("cantidad");
        String tallaA = args.getString("talla");
        String precio_unitarioA = args.getString("precio_unitario");
        String totalA = args.getString("total");

        nombre_cliente.setText(nombre_clienteA);
        nombre_producto.setText(nombre_productoA);
        cantidad.setText(cantidadA);
        talla.setText(tallaA);
        precio_unitario.setText(precio_unitarioA);
        total.setText(totalA);


        // Obtener una referencia al documento de la colección principal que contiene la subcolección
        DocumentReference clienteRef = db.collection("ventas").document().collection("clientes").document(idd);;

       clienteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
               if (documentSnapshot.exists()) {
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(CustomerDetailsActivity.this, "Error al obtener los datos!", Toast.LENGTH_SHORT).show();
           }
       });
    }
}