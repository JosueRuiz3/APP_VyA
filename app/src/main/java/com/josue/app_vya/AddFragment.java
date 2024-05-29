package com.josue.app_vya;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.josue.app_vya.helpers.MoneyTextWatcher;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddFragment extends Fragment {

    private RelativeLayout btnagregar;
    private String id_ventas;
    private TextInputEditText nombre_producto, descripcion, precio_compra, precio_venta,
            stock, fecha_entrega;
    private RelativeLayout btnmostrarCalendario;
    private FirebaseFirestore mFirestore;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    boolean valid = true;

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
        View v = inflater.inflate(R.layout.fragment_add, container, false);

        mFirestore = FirebaseFirestore.getInstance();
        nombre_producto = v.findViewById(R.id.nombre_producto);
        descripcion = v.findViewById(R.id.descripcion);
        stock = v.findViewById(R.id.stock);
        precio_compra = v.findViewById(R.id.precio_compra);
        precio_venta = v.findViewById(R.id.precio_venta);
        btnagregar = v.findViewById(R.id.btnagregar);
        progressBar = v.findViewById(R.id.progressBar);
        fecha_entrega = v.findViewById(R.id.fecha_entrega);
        btnmostrarCalendario = v.findViewById(R.id.btnmostrarCalendario);

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
        checkField(descripcion);
        checkField(stock);
        checkField(precio_compra);
        checkField(precio_venta);

        String productoA = nombre_producto.getText().toString().trim();
        Integer stockA = Integer.parseInt(stock.getText().toString().trim());
        String descripcionA = descripcion.getText().toString().trim();
        String precio_compraA = precio_compra.getText().toString().trim();
        String precio_ventaA = precio_venta.getText().toString().trim();
        Timestamp fecha_creacionA = Timestamp.now();

        if(!productoA.isEmpty() && !descripcionA.isEmpty() && !precio_compraA.isEmpty() && !precio_ventaA.isEmpty()){
            postVentas(productoA, stockA, descripcionA, precio_compraA, precio_ventaA, fecha_creacionA);
        }
        else{
            Toast.makeText(getContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
        }

    }

    private void postVentas(String productoA, Integer stockA, String descripcionA, String precio_compraA, String precio_ventaA, Timestamp fecha_creacionA) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Agregando producto...");
        progressDialog.show();

        // Creamos una referencia al documento de la colección "ventas" con un ID único generado automáticamente por Firestore
        DocumentReference ventaRef = mFirestore.collection("Ventas").document();

        // Creamos un mapa con los datos que queremos agregar al documento de la colección "ventas"
        Map<String, Object> map = new HashMap<>();
        map.put("idVenta", ventaRef.getId());
        map.put("nombre_producto", productoA);
        map.put("descripcion", descripcionA);
        map.put("stock", stockA);
        map.put("precio_compra", precio_compraA);
        map.put("precio_venta", precio_ventaA);
        map.put("fecha_creacion", fecha_creacionA);

        // Agregamos el mapa como un documento a la colección "ventas" con el ID generado automáticamente
        ventaRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Creado Correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Si ocurre un error al agregar el documento a la colección, mostramos un mensaje de error
                Toast.makeText(getContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });

        limpiarCampos();
    }

    private void mostarFecha(){
        // Listener para el clic en el botón
        btnmostrarCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual del sistema
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Crear el DatePickerDialog con la fecha actual como valor predeterminado
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mostrar la fecha seleccionada en un TextView llamado 'fecha_entrega'
                        fecha_entrega.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        // Verificar si la fecha seleccionada es anterior a la fecha actual
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);
                        if (selectedCalendar.before(Calendar.getInstance())) {
                            // La fecha seleccionada es anterior a la fecha actual, mostrar una alerta (puedes usar Toast o AlertDialog)
                            mostrarAlertaFechaAnterior();
                        }
                    }
                }, year, month, dayOfMonth);

                // Mostrar el DatePickerDialog
                datePickerDialog.show();
            }
        });
    }

    private void mostrarAlertaFechaAnterior() {
        Toast.makeText(getContext(), "La fecha seleccionada es anterior a la fecha actual", Toast.LENGTH_SHORT).show();
    }

    private void limpiarCampos(){
        nombre_producto.setText("");
        descripcion.setText("");
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
