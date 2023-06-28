package com.josue.app_vya;

import android.content.DialogInterface;
import android.content.Intent;
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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mainCollectionRef = db.collection("ventas");
    private DocumentReference documentRef = mainCollectionRef.document();
    private CollectionReference subCollectionRef = documentRef.collection("clientes");

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
        String idC = args.getString("id");

        idd = idC;
        getClient();

        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Desea editar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idd = idC;
                                checkField(nombre_producto);
                                checkField(nombre_cliente);
                                checkField(cantidad);
                                checkField(precio_unitario);
                                checkField(talla);
                                checkField(total);

                                String nombreClienteA = nombre_cliente.getText().toString().trim();
                                String nombreProductoA = nombre_producto.getText().toString().trim();
                                String cantidadA = cantidad.getText().toString().trim();
                                String tallaA = talla.getText().toString().trim();
                                String precioUnitarioA = precio_unitario.getText().toString().trim();
                                String totalA = total.getText().toString().trim();

                                if (!nombreClienteA.isEmpty() && !nombreProductoA.isEmpty() && !cantidadA.isEmpty() && !tallaA.isEmpty() && !precioUnitarioA.isEmpty() && !totalA.isEmpty()) {
                                    update(nombreClienteA, nombreProductoA, cantidadA, tallaA, precioUnitarioA, totalA, idC);

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

        btnelimimar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Desea eliminar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idd = idC;
                                delete(idC);
                                Intent intent = new Intent(getContext(), DetailsActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        });


        return v;
    }

    private void update(String nombreClienteA,String nombreProductoA, String cantidadA,String precioUnitarioA, String tallaA, String totalA, String idCliente) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre_cliente", nombreClienteA);
        map.put("nombre_producto", nombreProductoA);
        map.put("cantidad", cantidadA);
        map.put("precio_unitario", precioUnitarioA);
        map.put("talla", tallaA);
        map.put("total", totalA);

        mfirestore.collection("clientes").document(idCliente).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void getClient() {
        Bundle args = getArguments();
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

        // Obtener la referencia al documento del cliente en la subcolección
        DocumentReference clienteRef = subCollectionRef.document(id);

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

    private void delete(String id){
        mfirestore.collection("clientes").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"Eliminado correctamente!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al borrar los datos!",Toast.LENGTH_SHORT).show();
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