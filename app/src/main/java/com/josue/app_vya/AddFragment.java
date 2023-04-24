package com.josue.app_vya;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class AddFragment extends Fragment {

    CardView btnagregar;
    String id_ventas;
    TextInputEditText nombre_producto, talla, precio_compra, precio_venta, stock;
    private FirebaseFirestore mFirestore;
    private ProgressBar progressBar;
    boolean valid = true;
    private ProgressDialog progressDialog;

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
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        nombre_producto = v.findViewById(R.id.nombre_producto);
        talla = v.findViewById(R.id.talla);
        stock = v.findViewById(R.id.stock);
        precio_compra = v.findViewById(R.id.precio_compra);
        precio_venta = v.findViewById(R.id.precio_venta);
        btnagregar = v.findViewById(R.id.btnagregar);
        progressBar = v.findViewById(R.id.progressBar);

        precio_venta.addTextChangedListener(new MoneyTextWatcher(precio_venta));
        precio_venta.setText("0");

        precio_compra.addTextChangedListener(new MoneyTextWatcher(precio_compra));
        precio_compra.setText("0");

        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
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

    private void guardar(){

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

        if(!productoA.isEmpty() && !stockA.isEmpty() && !tallaA.isEmpty() && !precio_compraA.isEmpty() && !precio_ventaA.isEmpty()){
            postVentas(productoA, stockA, tallaA, precio_compraA, precio_ventaA);
        }else{
            Toast.makeText(getContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
        }

    }

    private void postVentas(String productoA, String stockA, String tallaA, String precio_compraA, String precio_ventaA){

        // Creamos una referencia al documento de la colección "ventas" con un ID único generado automáticamente por Firestore
        DocumentReference ventaRef = mFirestore.collection("ventas").document();

        // Creamos un mapa con los datos que queremos agregar al documento de la colección "ventas"
        Map<String, Object> ventaData = new HashMap<>();
        ventaData.put("id", ventaRef.getId());
        ventaData.put("nombre_producto", productoA);
        ventaData.put("talla", tallaA);
        ventaData.put("stock", stockA);
        ventaData.put("precio_compra", precio_compraA);
        ventaData.put("precio_venta", precio_ventaA);

        // Agregamos el mapa como un documento a la colección "ventas" con el ID generado automáticamente
        ventaRef.set(ventaData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // Si se agrega el documento correctamente, creamos una referencia a la subcolección "productos" del documento de la venta
                DocumentReference productoRef = ventaRef.collection("clientes").document();

                // Creamos un mapa con los datos del producto que queremos agregar a la subcolección
                Map<String, Object> productoData = new HashMap<>();

                // Agregamos el mapa como un documento a la subcolección "productos" con un ID único generado automáticamente
                productoRef.set(productoData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Si se agrega el documento correctamente, mostramos un mensaje de éxito y limpiamos los campos
                        Toast.makeText(getContext(), "Creado exitosamente", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si ocurre un error al agregar el documento a la subcolección, mostramos un mensaje de error
                        Toast.makeText(getContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Si ocurre un error al agregar el documento a la colección, mostramos un mensaje de error
                Toast.makeText(getContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void limpiarCampos(){
        nombre_producto.setText("");
        talla.setText("");
        stock.setText("");
        precio_compra.setText("");
        precio_venta.setText("");
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