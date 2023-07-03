package com.josue.app_vya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        String idVentas = getIntent().getStringExtra("id_ventas");

        String idCliente = getIntent().getStringExtra("clienteId");
        idd = idCliente;
        obtenerDatosCliente(idCliente);

        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
                builder.setMessage("¿Desea editar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idd = idCliente;
                                String nombre_clienteA = nombre_cliente.getText().toString().trim();
                                String nombre_productoA = nombre_producto.getText().toString().trim();
                                String cantidadA = cantidad.getText().toString().trim();
                                String tallaA = talla.getText().toString().trim();
                                String precio_unitarioA = precio_unitario.getText().toString().trim();
                                String totalA = total.getText().toString().trim();

                                if (!nombre_productoA.isEmpty() && !totalA.isEmpty()) {
                                    update(nombre_clienteA, nombre_productoA, cantidadA, tallaA, precio_unitarioA, totalA, idCliente);

                                } else {
                                    Toast.makeText(CustomerDetailsActivity.this, "Ingrese los datos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });

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


    private void obtenerDatosCliente(String idCliente) {
        idd = idCliente;
        Bundle args = getIntent().getExtras();
        idCliente = args.getString("clienteId");
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

    private void update(String nombre_clienteA, String nombre_productoA, String cantidadA, String tallaA, String precio_unitarioA, String totalA, String id){
        // Crear un nuevo mapa con los datos que deseas agregar a la subcolección
        Map<String, Object> map = new HashMap<>();
        map.put("nombre_cliente", nombre_clienteA);
        map.put("nombre_producto", nombre_productoA);
        map.put("cantidad", cantidadA);
        map.put("talla", tallaA);
        map.put("precio_unitario", precio_unitarioA);
        map.put("total", totalA);

        // Agregar el mapa como un nuevo documento a la subcolección con el ID del documento principal
        mfirestore.collection("clientes").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CustomerDetailsActivity.this, "Actualizado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerDetailsActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}