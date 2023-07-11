package com.josue.app_vya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomerDetailsActivity extends AppCompatActivity {

   private CardView btneditar, btneliminar;
    private TextInputEditText nombre_cliente, nombre_producto, cantidad, precio_unitario, talla, total;
    private String idd, iddVenta;
    private FirebaseFirestore mfirestore;
    private boolean valid = true;

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
        btneliminar = findViewById(R.id.btneliminar);
        mfirestore = FirebaseFirestore.getInstance();

        Bundle args = getIntent().getExtras();
        String idCliente = args.getString("idCliente");
        String idVenta = args.getString("idVenta");

        iddVenta = idVenta;
        idd = idCliente;

        getCliente(idCliente);

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

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
                builder.setMessage("¿Desea eliminar este registro?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idd = idCliente;
                                delete(idCliente);
                                Intent intent = new Intent(CustomerDetailsActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // No hacer nada
                            }
                        })
                        .show();
            }
        });

    }

    private void get(String idVenta) {
        mfirestore.collection("Ventas").document(idVenta).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerDetailsActivity.this, "Error al obtener los datos de ventas!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCliente(String idCliente) {
        idd = idCliente;
        Bundle args = getIntent().getExtras();
        idCliente = args.getString("idCliente");
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

       mfirestore.collection("Ventas").document().collection("Clientes").document(idd).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

    private void update(String nombre_clienteA, String nombre_productoA, String cantidadA, String tallaA, String precio_unitarioA, String totalA, String idCliente){
        // Crear un nuevo mapa con los datos que deseas agregar a la subcolección
        Map<String, Object> map = new HashMap<>();
        map.put("nombre_cliente", nombre_clienteA);
        map.put("nombre_producto", nombre_productoA);
        map.put("cantidad", cantidadA);
        map.put("talla", tallaA);
        map.put("precio_unitario", precio_unitarioA);
        map.put("total", totalA);

        mfirestore.collection("ventas").document().collection("clientes").document().update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void delete(String idCliente) {
        String idVenta = getIntent().getStringExtra("idVenta");

        if (idVenta != "D8Qf8DdTFqyJdEL11NXx") {
            mfirestore.collection("Ventas")
                    .document("D8Qf8DdTFqyJdEL11NXx")
                    .collection("Clientes")
                    .document(idCliente)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CustomerDetailsActivity.this, "Eliminado correctamente!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CustomerDetailsActivity.this, "Error al borrar el registro!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}