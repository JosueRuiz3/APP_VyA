package com.josue.app_vya;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.josue.app_vya.helpers.MoneyTextWatcher;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddCustomerFragment extends Fragment {

    private TextInputEditText nombre_cliente, nombre_producto, cantidad, precio_unitario, descripcion, total, fecha_entrega, fecha_pago1, fecha_pago2;
    private CardView btnagregar;
    private String idVenta;
    private RelativeLayout btnmostrarCalendario, btnmostrarpago1, btnmostrarpago2;
    private FirebaseFirestore mfirestore;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private String idd;
    boolean valid = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idVenta = getArguments().getString("idVenta");
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
        nombre_producto = v.findViewById(R.id.nombre_producto);
        descripcion = v.findViewById(R.id.descripcion);
        btnagregar = v.findViewById(R.id.btnagregar);
        fecha_entrega = v.findViewById(R.id.fecha_entrega);
        fecha_pago1 = v.findViewById(R.id.fecha_pago1);
        fecha_pago2 = v.findViewById(R.id.fecha_pago2);
        btnmostrarCalendario = v.findViewById(R.id.btnmostrarCalendario);
        btnmostrarpago1 = v.findViewById(R.id.btnmostrarpago1);
        btnmostrarpago2 = v.findViewById(R.id.btnmostrarpago2);
        mfirestore = FirebaseFirestore.getInstance();

        Bundle args = getActivity().getIntent().getExtras();
        String idVenta = args.getString("idVenta");

        idd = idVenta;
        get(idVenta);

        precio_unitario.addTextChangedListener(new MoneyTextWatcher(precio_unitario));
        precio_unitario.setText("0");

        total.addTextChangedListener(new MoneyTextWatcher(total));
        total.setText("0");

        // Agregar el TextWatcher al EditText stock
        cantidad.addTextChangedListener(textWatcher);

        // Agregar el TextWatcher al EditText precio_compra
        precio_unitario.addTextChangedListener(textWatcher);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mostrar la fecha seleccionada en un TextView llamado 'fecha_pago1'
                        fecha_pago1.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mostrar la fecha seleccionada en un TextView llamado 'fecha_pago2'
                        fecha_pago2.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);

                // Mostrar el DatePickerDialog
                datePickerDialog.show();
            }
        });


        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });

        return v;
    }

    private void mostrarAlertaFechaAnterior() {
        Toast.makeText(requireContext(), "La fecha seleccionada es anterior a la fecha actual", Toast.LENGTH_SHORT).show();
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

    private void get(String idVenta) {
        mfirestore.collection("Ventas").document(idVenta).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
        checkField(nombre_producto);
        checkField(descripcion);
        checkField(cantidad);
        checkField(precio_unitario);
        checkField(total);
        checkField(fecha_entrega);
        checkField(fecha_pago1);
        checkField(fecha_pago2);

        String nombre_clienteA = nombre_cliente.getText().toString().trim();
        String nombre_productoA = nombre_producto.getText().toString().trim();
        String descripcionA = descripcion.getText().toString().trim();
        Integer cantidadA = Integer.parseInt(cantidad.getText().toString().trim());
        String precio_unitarioA = precio_unitario.getText().toString().trim();
        String totalA = total.getText().toString().trim();
        String fecha_entregaA = fecha_entrega.getText().toString().trim();
        String fecha_pago1A = fecha_pago1.getText().toString().trim();
        String fecha_pago2A = fecha_pago2.getText().toString().trim();

        if(!nombre_clienteA.isEmpty() && !nombre_productoA.isEmpty() && !descripcionA.isEmpty() && !precio_unitarioA.isEmpty() && !totalA.isEmpty() && !fecha_entregaA.isEmpty() && !fecha_pago1A.isEmpty() && !fecha_pago2A.isEmpty()){
            postClientes(nombre_clienteA, nombre_productoA, cantidadA, descripcionA, precio_unitarioA, totalA, fecha_entregaA, fecha_pago1A, fecha_pago2A);
        }else{
            Toast.makeText(getContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void postClientes(String nombre_clienteA, String nombre_productoA, Integer cantidadA, String descripcionA, String precio_unitarioA, String totalA,  String fecha_entregaA, String fecha_pago1A, String fecha_pago2A) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Agregando cliente...");
        progressDialog.show();
        Bundle args = getActivity().getIntent().getExtras();
        String idVenta = args.getString("idVenta");

        idd = idVenta;

        // Obtener una referencia al documento de la colección principal que contiene la subcolección
        DocumentReference ventaRef = mfirestore.collection("Ventas").document(idVenta);

        // Obtener una referencia a la subcolección del documento principal
        DocumentReference clientesRef = ventaRef.collection("Clientes").document();

        // Generar un ID único para el documento de la subcolección
        //String idCliente = clientesRef.getId();

        // Crear un nuevo mapa con los datos que deseas agregar a la subcolección
        Map<String, Object> map = new HashMap<>();
        map.put("idCliente", clientesRef.getId()); // Utilizar el ID de la subcolección
        map.put("idVenta", idVenta);
        map.put("nombre_cliente", nombre_clienteA);
        map.put("nombre_producto", nombre_productoA);
        map.put("cantidad", cantidadA);
        map.put("descripcion", descripcionA);
        map.put("precio_unitario", precio_unitarioA);
        map.put("total", totalA);
        map.put("fecha_entrega", fecha_entregaA);
        map.put("fecha_pago1", fecha_pago1A);
        map.put("fecha_pago2", fecha_pago2A);

        // Agregar el mapa como un nuevo documento a la subcolección con el ID del documento principal
        clientesRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Actualizar el stock solo si se agregó correctamente el cliente
                updateStock(idVenta, cantidadA);
                limpiarCampos();
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Creado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStock(String idVenta, int cantidadComprada) {
        // Obtener una referencia al documento de la colección principal que contiene el stock
        DocumentReference ventaRef = mfirestore.collection("Ventas").document(idVenta);

        ventaRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Obtener el stock actual del documento principal de la venta
                    int stockActual = documentSnapshot.getLong("stock").intValue();

                    // Calcular el nuevo stock después de la compra del cliente o cancelación
                    int nuevoStock = stockActual - cantidadComprada;

                    // Crear un mapa para actualizar el campo "stock" en el documento de la colección "Ventas"
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("stock", nuevoStock);

                    // Actualizar el campo "stock" en el documento de la colección "Ventas"
                    ventaRef.update(updateData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // El stock ha sido actualizado correctamente
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Manejar el error en caso de que la actualización falle
                                }
                            });
                } else {
                    // El documento principal de la venta no existe
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejar el error en caso de que la consulta falle
            }
        });
    }

    private void limpiarCampos(){
        nombre_cliente.setText("");
        nombre_producto.setText("");
        descripcion.setText("");
        cantidad.setText("");
        precio_unitario.setText("");
        total.setText("");
        fecha_entrega.setText("");
        fecha_pago1.setText("");
        fecha_pago2.setText("");
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