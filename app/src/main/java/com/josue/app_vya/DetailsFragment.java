package com.josue.app_vya;

import static android.content.Intent.getIntent;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.math.BigDecimal;
import java.util.Objects;


public class DetailsFragment extends Fragment {

    String id_ventas;
    boolean valid = true;
    CardView btneditar, btneliminar;
    TextInputEditText nombre_producto, talla, precio_compra, precio_venta, stock, invertido, ganancia;
    FirebaseFirestore mfirestore;
    String idd;
    StorageReference storageReference;

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
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        nombre_producto = v.findViewById(R.id.nombre_producto);
        talla = v.findViewById(R.id.talla);
        stock = v.findViewById(R.id.stock);
        precio_compra = v.findViewById(R.id.precio_compra);
        precio_venta = v.findViewById(R.id.precio_venta);
        invertido = v.findViewById(R.id.invertido);
        ganancia = v.findViewById(R.id.ganancia);

        mfirestore = FirebaseFirestore.getInstance();

        Bundle args = getActivity().getIntent().getExtras();
        String id = args.getString("id_ventas");

        idd = id;
        get(id);

        precio_venta.addTextChangedListener(new MoneyTextWatcher(precio_venta));
        precio_venta.setText("0");

        precio_compra.addTextChangedListener(new MoneyTextWatcher(precio_compra));
        precio_compra.setText("0");

        invertido.addTextChangedListener(new MoneyTextWatcher(invertido));
        invertido.setText("0");

        stock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Double c = Double.parseDouble(!stock.getText().toString().isEmpty() ?
                        stock.getText().toString() : "0");

                //Double p = Double.parseDouble(!precio_compra.getText().toString().isEmpty() ?
                  //      precio_compra.getText().toString() : "0");

            }
        });

        return v;
    }

    public void doGetValue(View v) {
        BigDecimal value = MoneyTextWatcher.parseCurrencyValue(precio_venta.getText().toString());
        precio_venta.setText(String.valueOf(value));

        BigDecimal value2 = MoneyTextWatcher.parseCurrencyValue(precio_compra.getText().toString());
        precio_compra.setText(String.valueOf(value2));

        BigDecimal value3 = MoneyTextWatcher.parseCurrencyValue(invertido.getText().toString());
        invertido.setText(String.valueOf(value2));
    }

    private void get(String id){
        mfirestore.collection("ventas").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

}