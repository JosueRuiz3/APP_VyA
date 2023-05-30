package com.josue.app_vya;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class EditFragment extends Fragment {

    private boolean valid = true;
    private CardView btneditar, btneliminar;
    private String id_ventas;
    private TextInputEditText nombre_producto, talla, precio_compra, precio_venta, stock;
    private FirebaseFirestore mfirestore;
    private String idd;
    private StorageReference storageReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_ventas = getArguments().getString("id_ventas");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit, container, false);

        nombre_producto = v.findViewById(R.id.nombre_producto);
        talla = v.findViewById(R.id.talla);
        stock = v.findViewById(R.id.stock);
        precio_compra = v.findViewById(R.id.precio_compra);
        precio_venta = v.findViewById(R.id.precio_venta);
        btneditar = v.findViewById(R.id.btneditar);
        btneliminar = v.findViewById(R.id.btneliminar);
        mfirestore = FirebaseFirestore.getInstance();

        Bundle args = getActivity().getIntent().getExtras();
        String idVentas = args.getString("id_ventas");

        idd = idVentas;
        get(idVentas);

        precio_venta.addTextChangedListener(new MoneyTextWatcher(precio_venta));
        precio_venta.setText("0");

        precio_compra.addTextChangedListener(new MoneyTextWatcher(precio_compra));
        precio_compra.setText("0");


        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Desea editar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idd = idVentas;
                                checkField(nombre_producto);
                                checkField(talla);
                                checkField(stock);
                                checkField(precio_compra);
                                checkField(precio_venta);

                                String productoA = nombre_producto.getText().toString().trim();
                                String stockA = stock.getText().toString().trim();
                                String tallaA = talla.getText().toString().trim();
                                String precio_compraA = precio_compra.getText().toString().trim();
                                String precio_ventaA = precio_venta.getText().toString().trim();

                                if (!productoA.isEmpty() && !stockA.isEmpty() && !tallaA.isEmpty() && !precio_compraA.isEmpty() && !precio_ventaA.isEmpty()) {
                                    update(productoA, stockA, tallaA, precio_compraA, precio_ventaA, idVentas);

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


        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Desea eliminar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idd = idVentas;
                                delete(idVentas);
                                Intent intent = new Intent(getContext(), MainActivity.class);
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
    public void doGetValue(View v) {
        BigDecimal value = MoneyTextWatcher.parseCurrencyValue(precio_venta.getText().toString());
        precio_venta.setText(String.valueOf(value));

        BigDecimal value2 = MoneyTextWatcher.parseCurrencyValue(precio_compra.getText().toString());
        precio_compra.setText(String.valueOf(value2));
    }

    private void update(String productoA,String stockA, String tallaA,String precio_compraA, String precio_ventaA, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre_producto",productoA);
        map.put("talla",tallaA);
        map.put("stock",stockA);
        map.put("precio_compra",precio_compraA);
        map.put("precio_venta",precio_ventaA);

        mfirestore.collection("ventas").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void get(String idVentas){
        mfirestore.collection("ventas").document(idVentas).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("nombre_producto");
                String stocks = documentSnapshot.getString("stock");
                String size = documentSnapshot.getString("talla");
                String price_c = documentSnapshot.getString("precio_compra");
                String price_s = documentSnapshot.getString("precio_venta");

                nombre_producto.setText(name);
                stock.setText(stocks);
                talla.setText(size);
                precio_compra.setText(price_c);
                precio_venta.setText(price_s);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al obtener los datos!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void delete(String id) {
        mfirestore.collection("ventas").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        else  {
            valid = true;
        }
        return valid;
    }
}