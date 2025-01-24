package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StokBarangActivity extends AppCompatActivity {

    private EditText etIDBarang, etNamaBarang, etJumlah, etHargaSatuan;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stok_barang); // Ganti dengan layout yang sesuai

        // Inisialisasi elemen UI
        etIDBarang = findViewById(R.id.etIDBarang);
        etNamaBarang = findViewById(R.id.etNamaBarang);
        etJumlah = findViewById(R.id.etJumlah);
        etHargaSatuan = findViewById(R.id.etHargaSatuan);
        btnSubmit = findViewById(R.id.btnTambahStok);

        // Kirim data stok barang
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etIDBarang.getText().toString().trim().isEmpty() ||
                        etNamaBarang.getText().toString().trim().isEmpty() ||
                        etJumlah.getText().toString().trim().isEmpty() ||
                        etHargaSatuan.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Form belum lengkap!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kirim data ke server
                kirimDataKeServer();
            }
        });
    }

    private void kirimDataKeServer() {
        String url = Db_Contract.urlInsertStokBarang; // Ganti dengan URL endpoint untuk menyimpan data stok barang

        // Membuat request POST ke server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                clearForm(); // Bersihkan form
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_barang", etIDBarang.getText().toString().trim());
                params.put("nama_barang", etNamaBarang.getText().toString().trim());
                params.put("jumlah", etJumlah.getText().toString().trim());
                params.put("harga_satuan", etHargaSatuan.getText().toString().trim());
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(StokBarangActivity.this);
        requestQueue.add(stringRequest);
    }

    // Fungsi untuk mengosongkan form
    private void clearForm() {
        etIDBarang.setText("");
        etNamaBarang.setText("");
        etJumlah.setText("");
        etHargaSatuan.setText("");
    }
}