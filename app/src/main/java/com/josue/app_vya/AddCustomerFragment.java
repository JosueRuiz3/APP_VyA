package com.josue.app_vya;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class AddCustomerFragment extends Fragment {

    TextInputEditText nombre_cliente, cantidad, precio_unitario, talla, total;
    TextInputEditText nombre_producto, tallap, precio_compra, precio_venta, stock, invertido, ganancia;
    CardView btnagregar;
    String id_cliente;
    private FirebaseFirestore mfirestore;
    private ProgressBar progressBar;
    boolean valid = true;
    private ProgressDialog progressDialog;
    String idd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_cliente = getArguments().getString("id_ventas");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_customer, container, false);

        cantidad = v.findViewById(R.id.cantidad);
        precio_unitario = v.findViewById(R.id.precio_unitario);
        total = v.findViewById(R.id.total);
        nombre_cliente = v.findViewById(R.id.nombre_cliente);
        talla = v.findViewById(R.id.talla);
        btnagregar = v.findViewById(R.id.btnagregar);

        mfirestore = FirebaseFirestore.getInstance();
        Bundle args = getActivity().getIntent().getExtras();
        String id = args.getString("id_ventas");

        idd = id;
        get(id);

        precio_unitario.addTextChangedListener(new MoneyTextWatcher(precio_unitario));
        precio_unitario.setText("0");

        total.addTextChangedListener(new MoneyTextWatcher(total));
        total.setText("0");

        // Agregar el TextWatcher al EditText stock
        cantidad.addTextChangedListener(textWatcher);

        // Agregar el TextWatcher al EditText precio_compra
        precio_unitario.addTextChangedListener(textWatcher);

        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });

        return v;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                Double c = Double.parseDouble(!cantidad.getText().toString().isEmpty() ?
                        cantidad.getText().toString() : "0");

                String precioString = precio_unitario.getText().toString().replaceAll("[^\\d.,]+", "").replace(',', '.');
                Double p = Double.parseDouble(!precioString.isEmpty() ? precioString : "0");

                Double i = c * p;

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String iFormatted = decimalFormat.format(i);

                total.setText(iFormatted);


            } catch (NumberFormatException e) {
                // Manejar la excepción en caso de que la conversión falle
                Log.e("Error", "No se pudo convertir el valor a Double", e);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void doGetValue(View v) {
        BigDecimal value = MoneyTextWatcher.parseCurrencyValue(precio_unitario.getText().toString());
        precio_unitario.setText(String.valueOf(value));

        BigDecimal value2 = MoneyTextWatcher.parseCurrencyValue(total.getText().toString());
        total.setText(String.valueOf(value2));
    }

    private void get(String id) {
        mfirestore.collection("ventas").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardar(){

        checkField(nombre_cliente);
        checkField(talla);
        checkField(cantidad);
        checkField(total);
        checkField(precio_unitario);

        String nombre_clienteA = nombre_cliente.getText().toString().trim();
        String tallaA = talla.getText().toString().trim();
        String cantidadA = talla.getText().toString().trim();
        String totalA = total.getText().toString().trim();
        String precio_unitarioA = precio_unitario.getText().toString().trim();

        if(!nombre_clienteA.isEmpty() && !cantidadA.isEmpty() && !tallaA.isEmpty() && !totalA.isEmpty() && !precio_unitarioA.isEmpty()){
            postClientes(nombre_clienteA, cantidadA, tallaA, totalA, precio_unitarioA);
        }else{
            Toast.makeText(getContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
        }

    }

    private void postClientes(String nombre_clienteA, String cantidadA, String tallaA, String totalA, String precio_unitarioA){

        Bundle args = getActivity().getIntent().getExtras();
        String id = args.getString("id_ventas");

        idd = id;

        // Obtener una referencia al documento de la colección principal que contiene la subcolección
        DocumentReference ventaRef = mfirestore.collection("ventas").document(id);

        // Obtener una referencia a la subcolección del documento principal
        CollectionReference clientesRef = ventaRef.collection("clientes");

        // Crear un nuevo mapa con los datos que deseas agregar a la subcolección
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("nombre_cliente", nombre_clienteA);
        clienteData.put("talla", tallaA);
        clienteData.put("cantidad", cantidadA);
        clienteData.put("total", totalA);
        clienteData.put("precio_unitario", precio_unitarioA);
        clienteData.put("activo", true);

        // Agregar el mapa como un nuevo documento a la subcolección con el ID del documento principal
        clientesRef.add(clienteData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "Creado exitosamente", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void limpiarCampos(){
        nombre_cliente.setText("");
        talla.setText("");
        cantidad.setText("");
        total.setText("");
        precio_unitario.setText("");
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