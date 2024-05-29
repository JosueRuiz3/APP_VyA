package com.josue.app_vya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.josue.app_vya.helpers.MoneyTextWatcher;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomerDetailsActivity extends AppCompatActivity {

    private ImageView btneliminar, btnabono;
    private TextInputEditText nombre_cliente, nombre_producto, cantidad, precio_unitario, descripcion,
            total, fecha_entrega, fecha_pago1, fecha_pago2, abonos;
    private RelativeLayout btnmostrarCalendario, btnmostrarpago1, btnmostrarpago2, btneditar;
    private String idd, iddVenta;
    private FirebaseFirestore mfirestore;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);


        cantidad = findViewById(R.id.cantidad);
        precio_unitario = findViewById(R.id.precio_unitario);
        total = findViewById(R.id.total);
        abonos = findViewById(R.id.abonos);
        btnabono = findViewById(R.id.btnabono);
        fecha_entrega = findViewById(R.id.fecha_entrega);
        fecha_pago1 = findViewById(R.id.fecha_pago1);
        fecha_pago2 = findViewById(R.id.fecha_pago2);
        nombre_cliente = findViewById(R.id.nombre_cliente);
        descripcion = findViewById(R.id.descripcion);
        nombre_producto = findViewById(R.id.nombre_producto);
        btneditar = findViewById(R.id.btneditar);
        btneliminar = findViewById(R.id.btneliminar);
        btnmostrarCalendario = findViewById(R.id.btnmostrarCalendario);
        btnmostrarpago1 = findViewById(R.id.btnmostrarpago1);
        btnmostrarpago2 = findViewById(R.id.btnmostrarpago2);
        mfirestore = FirebaseFirestore.getInstance();

        Bundle args = getIntent().getExtras();
        String idCliente = args.getString("idCliente");
        String idVenta = args.getString("idVenta");

        iddVenta = idVenta;
        idd = idCliente;
        textWatcherEditText();
        mostarFecha();
        obtener(idCliente);
        convertirColon();

        btnabono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar();
            }
        });

        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
                builder.setMessage("¿Desea editar este producto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idd = idCliente;
                                checkField(nombre_producto);
                                checkField(nombre_cliente);
                                // checkField(cantidad);
                                checkField(precio_unitario);
                                checkField(descripcion);
                                checkField(total);

                                String nombreClienteA = nombre_cliente.getText().toString().trim();
                                String nombreProductoA = nombre_producto.getText().toString().trim();
                                Integer cantidadA = Integer.valueOf(cantidad.getText().toString().trim());
                                String descripcionA = descripcion.getText().toString().trim();
                                String precioUnitarioA = precio_unitario.getText().toString().trim();
                                String totalA = total.getText().toString().trim();

                                if (!nombreClienteA.isEmpty() && !nombreProductoA.isEmpty() && !descripcionA.isEmpty() && !precioUnitarioA.isEmpty() && !totalA.isEmpty()) {
                                    update(nombreClienteA, nombreProductoA, cantidadA, descripcionA,
                                            precioUnitarioA, totalA, idCliente);
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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {


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

    private void obtener(String idCliente) {
        Bundle args = getIntent().getExtras();

        String idVenta = args.getString("idVenta");
        idCliente = args.getString("idCliente");
        String nombre_clienteA = args.getString("nombre_cliente");
        String nombre_productoA = args.getString("nombre_producto");
        Integer cantidadA = args.getInt("cantidad");
        String descripcionA = args.getString("descripcion");
        String precio_unitarioA = args.getString("precio_unitario");
        String totalA = args.getString("total");
        String fecha_entregaA = args.getString("fecha_entrega");
        String fecha_pago1A = args.getString("fecha_pago1");
        String fecha_pago2A = args.getString("fecha_pago2");

        nombre_cliente.setText(nombre_clienteA);
        nombre_producto.setText(nombre_productoA);
        String cantidadStr = String.valueOf(cantidadA);
        cantidad.setText(cantidadStr);
        descripcion.setText(descripcionA);
        precio_unitario.setText(precio_unitarioA);
        fecha_entrega.setText(fecha_entregaA);
        total.setText(totalA);
        fecha_pago1.setText(fecha_pago1A);
        fecha_pago2.setText(fecha_pago2A);

        mfirestore.collection("Ventas").document(idVenta).collection("Clientes").document(idVenta).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

    private void update(String nombre_clienteA, String nombre_productoA, Integer cantidadA, String descripcionA,
                        String precio_unitarioA, String totalA, String idCliente) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Actualizando detalle cliente...");
        progressDialog.show();

        Bundle args = getIntent().getExtras();
        String idVenta = args.getString("idVenta");

        // Crear un nuevo mapa con los datos que deseas actualizar en la subcolección
        Map<String, Object> map = new HashMap<>();
        map.put("nombre_cliente", nombre_clienteA);
        map.put("nombre_producto", nombre_productoA);
        map.put("cantidad", cantidadA);
        map.put("descripcion", descripcionA);
        map.put("precio_unitario", precio_unitarioA);
        map.put("total", totalA);

        // Actualizar el documento en la subcolección
        mfirestore.collection("Ventas").document(idVenta).collection("Clientes").document(idCliente).update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(CustomerDetailsActivity.this, "Actualizado exitosamente", Toast.LENGTH_SHORT).show();

                        // Después de actualizar los detalles del cliente, también actualiza el stock en el documento principal de la venta
                        updateStock(idVenta, cantidadA, false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CustomerDetailsActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateStock(String idVenta, int cantidadA, boolean isAdd) {
        DocumentReference ventaRef = mfirestore.collection("Ventas").document(idVenta);

        mfirestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(ventaRef);
                int stockActual = snapshot.getLong("stock").intValue();

                // Ajustar el stock según si se agregó o eliminó una cantidad
                int stockActualizado = isAdd ? stockActual - cantidadA : stockActual + cantidadA;

                // Actualizar el stock en el documento principal de la venta
                transaction.update(ventaRef, "stock", stockActualizado);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Stock actualizado exitosamente

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error al actualizar el stock
            }
        });
    }

    private void delete(String idCliente) {
        Bundle args = getIntent().getExtras();
        String idVenta = args.getString("idVenta");

        if (idVenta != null) {
            mfirestore.collection("Ventas")
                    .document(idVenta)
                    .collection("Clientes")
                    .document(idCliente)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            finish(); // Cierra la actividad actual
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CustomerDetailsActivity.this, "Error al borrar el registro!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No se pudo actualizar el registro, hay datos nulos", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(CustomerDetailsActivity.this, "Se borro correctamente!", Toast.LENGTH_SHORT).show();
    }

    private void guardar(){

        String abonosA = abonos.getText().toString().trim();

        if(!abonosA.isEmpty()){
            postAbonos(abonosA);
        }else{
            Toast.makeText(CustomerDetailsActivity.this, "Ingresar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void postAbonos(String abonosA) {

        Bundle args = CustomerDetailsActivity.this.getIntent().getExtras();
        String idCliente = args.getString("idCliente");

        idd = idCliente;

        // Obtener una referencia a la subcolección del documento principal
        DocumentReference clientesRef = mfirestore.collection("Clientes").document(idCliente);

        DocumentReference abonosRef = clientesRef.collection("Abonos").document();

        // Crear un nuevo mapa con los datos que deseas agregar a la subcolección
        Map<String, Object> map = new HashMap<>();
        map.put("idAbono", abonosRef.getId()); // Utilizar el ID de la subcolección
        map.put("idCliente", idCliente);
        map.put("abonos", abonosA);

        // Agregar el mapa como un nuevo documento a la subcolección con el ID del documento principal
        abonosRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CustomerDetailsActivity.this, "Creado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerDetailsActivity.this, "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        btnmostrarpago1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual del sistema
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Crear el DatePickerDialog con la fecha actual como valor predeterminado
                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mostrar la fecha seleccionada en un TextView llamado 'fecha_pago1'
                        fecha_pago1.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

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


        btnmostrarpago2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual del sistema
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Crear el DatePickerDialog con la fecha actual como valor predeterminado
                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mostrar la fecha seleccionada en un TextView llamado 'fecha_pago2'
                        fecha_pago2.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

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

    private void textWatcherEditText(){
        // Agregar el TextWatcher al EditText stock
        cantidad.addTextChangedListener(textWatcher);
        precio_unitario.addTextChangedListener(textWatcher);
        abonos.addTextChangedListener(textWatcher);

    }

    private void convertirColon(){
        precio_unitario.addTextChangedListener(new MoneyTextWatcher(precio_unitario));

        abonos.addTextChangedListener(new MoneyTextWatcher(abonos));
        abonos.setText("0");

        total.addTextChangedListener(new MoneyTextWatcher(total));

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

    private void mostrarAlertaFechaAnterior() {
        Toast.makeText(this, "La fecha seleccionada es anterior a la fecha actual", Toast.LENGTH_SHORT).show();
    }

}