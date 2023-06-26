package com.josue.app_vya;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class EditCustomerFragment extends Fragment {

    private boolean valid = true;
    private String id;
    private CardView btneditar, btnelimimar;
    private TextInputEditText nombre_cliente, nombre_producto, cantidad, precio_unitario, talla, total;
    private String idd;
    private FirebaseFirestore mfirestore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_customer, container, false);
        cantidad = v.findViewById(R.id.cantidad);
        precio_unitario = v.findViewById(R.id.precio_unitario);
        total = v.findViewById(R.id.total);
        nombre_cliente = v.findViewById(R.id.nombre_cliente);
        nombre_producto = v.findViewById(R.id.nombre_producto);
        talla = v.findViewById(R.id.talla);
        btneditar = v.findViewById(R.id.btneditar);
        btnelimimar = v.findViewById(R.id.btneliminar);
        mfirestore = FirebaseFirestore.getInstance();

        Bundle args = getActivity().getIntent().getExtras();
        String id = args.getString("id");

        idd = id;
        getClient(id);

        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Desea editar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idd = id;
                                checkField(nombre_producto);
                                checkField(nombre_cliente);
                                checkField(cantidad);
                                checkField(precio_unitario);
                                checkField(talla);
                                checkField(total);

                                String clienteA = nombre_cliente.getText().toString().trim();
                                String productoA = nombre_producto.getText().toString().trim();
                                String stockA = cantidad.getText().toString().trim();
                                String tallaA = talla.getText().toString().trim();
                                String precio_unitarioA = precio_unitario.getText().toString().trim();
                                String totalA = total.getText().toString().trim();

                                if (!clienteA.isEmpty() && !productoA.isEmpty() && !stockA.isEmpty() && !tallaA.isEmpty() && !precio_unitarioA.isEmpty() && !totalA.isEmpty()) {
                                    update(clienteA, productoA, stockA, tallaA, precio_unitarioA, totalA, id);

                                } else {
                                    Toast.makeText(getContext(), "Ingrese los datos", Toast.LENGTH_SHORT).show();
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

        return v;
    }

    private void getClient(String id) {
        Bundle args = getArguments();
        String nombreCliente = args.getString("nombre_cliente");
        String nombreProducto = args.getString("nombre_producto");
        String cantidadCliente = args.getString("cantidad");
        String tallaCliente = args.getString("talla");
        String precioUnitario = args.getString("precio_unitario");
        String totalCliente = args.getString("total");

        nombre_cliente.setText(nombreCliente);
        nombre_producto.setText(nombreProducto);
        cantidad.setText(cantidadCliente);
        talla.setText(tallaCliente);
        precio_unitario.setText(precioUnitario);
        total.setText(totalCliente);

        // Obtener los datos del cliente desde Firestore
        mfirestore.collection("clientes").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

    private void update(String nombreClienteA,String nombreProductoA, String cantidadA,String precioUnitarioA, String tallaA, String totalA, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre_cliente", nombreClienteA);
        map.put("nombre_producto", nombreProductoA);
        map.put("cantidad", cantidadA);
        map.put("precio_unitario", precioUnitarioA);
        map.put("talla", tallaA);
        map.put("total", totalA);

        // Obtener los datos del cliente desde Firestore
        mfirestore.collection("clientes").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkField(EditText textField){
        if (textField.getText().toString().isEmpty()){
            textField.setError("Debes llenar los campos");
            valid = false;
        }
        else {
            valid = true;
        }
        return valid;
    }
}