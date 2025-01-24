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

public class PelangganActivity extends AppCompatActivity {

    private EditText etIDPelanggan, etNama, etAlamat, etTelepon, etEmail;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan);

        // Inisialisasi elemen UI
        etIDPelanggan = findViewById(R.id.etIDPelanggan);
        etNama = findViewById(R.id.etNama);
        etAlamat = findViewById(R.id.etAlamat);
        etTelepon = findViewById(R.id.etTelepon);
        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnTambah);

        // Kirim data pelanggan
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etIDPelanggan.getText().toString().trim().isEmpty() ||
                        etNama.getText().toString().trim().isEmpty() ||
                        etAlamat.getText().toString().trim().isEmpty() ||
                        etTelepon.getText().toString().trim().isEmpty() ||
                        etEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Form belum lengkap!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kirim data ke server
                kirimDataKeServer();
            }
        });
    }

    private void kirimDataKeServer() {
        String url = Db_Contract.urlInsertPelanggan; // Ganti dengan URL endpoint untuk menyimpan data pelanggan

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
                params.put("id_pelanggan", etIDPelanggan.getText().toString().trim());
                params.put("nama", etNama.getText().toString().trim());
                params.put("alamat", etAlamat.getText().toString().trim());
                params.put("telepon", etTelepon.getText().toString().trim());
                params.put("email", etEmail.getText().toString().trim());
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(PelangganActivity.this);
        requestQueue.add(stringRequest);
    }

    // Fungsi untuk mengosongkan form
    private void clearForm() {
        etIDPelanggan.setText("");
        etNama.setText("");
        etAlamat.setText("");
        etTelepon.setText("");
        etEmail.setText("");
    }
}