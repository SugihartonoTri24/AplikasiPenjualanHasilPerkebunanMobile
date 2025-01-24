package com.example.projectuas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PemesananActivity extends AppCompatActivity {

    private EditText etNamaPemesanan, etAlamatPemesanan;
    private Spinner spinnerBarang;
    private Button btnPilihBarang, btnSubmit;
    private ListView lvPesanan;
    private TextView tvTotalHarga;

    private ArrayList<String> daftarPesanan;
    private ArrayAdapter<String> pesananAdapter;
    private ArrayList<Integer> hargaBarang;
    private int totalHarga = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        // Inisialisasi elemen UI
        etNamaPemesanan = findViewById(R.id.etNamaPemesanan);
        etAlamatPemesanan = findViewById(R.id.etAlamatPemesanan);
        spinnerBarang = findViewById(R.id.spinnerBarang);
        btnPilihBarang = findViewById(R.id.btnPilihBarang);
        btnSubmit = findViewById(R.id.btnSubmit);
        lvPesanan = findViewById(R.id.lvPesanan);
        tvTotalHarga = findViewById(R.id.tvTotalHarga);

        // Daftar barang dan harga
        final String[] barang = {"Apel", "Jeruk", "Bayam", "Tomat", "Kentang", "Jagung", "Wortel", "Singkong", "Durian", "Cabai"};
        hargaBarang = new ArrayList<>();
        hargaBarang.add(5000);  // Apel
        hargaBarang.add(3000);  // Jeruk
        hargaBarang.add(2000);  // Bayam
        hargaBarang.add(1500);  // Tomat
        hargaBarang.add(2500);  // Kentang
        hargaBarang.add(3500);  // Jagung
        hargaBarang.add(1000);  // Wortel
        hargaBarang.add(1200);  // Singkong
        hargaBarang.add(10000); // Durian
        hargaBarang.add(1600);  // Cabai

        // Setup spinner untuk memilih barang
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, barang);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBarang.setAdapter(adapter);

        // Daftar pesanan
        daftarPesanan = new ArrayList<>();
        pesananAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, daftarPesanan);
        lvPesanan.setAdapter(pesananAdapter);

        // Pilih barang dan tambahkan ke dalam daftar pesanan
        btnPilihBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posisiBarang = spinnerBarang.getSelectedItemPosition();
                String namaBarang = barang[posisiBarang];
                int harga = hargaBarang.get(posisiBarang);

                // Menambahkan barang ke dalam list
                daftarPesanan.add(namaBarang + " - Rp " + harga);
                pesananAdapter.notifyDataSetChanged();

                // Tambahkan harga barang ke total harga
                totalHarga += harga;
                tvTotalHarga.setText("Total Harga: Rp " + totalHarga);
            }
        });

        // Kirim pemesanan
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNamaPemesanan.getText().toString().trim().isEmpty() ||
                        etAlamatPemesanan.getText().toString().trim().isEmpty() ||
                        daftarPesanan.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Form belum lengkap!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Buat string detail pesanan
                StringBuilder detailPesanan = new StringBuilder();
                detailPesanan.append("Nama Pemesan: ").append(etNamaPemesanan.getText().toString().trim()).append("\n");
                detailPesanan.append("Alamat Pemesan: ").append(etAlamatPemesanan.getText().toString().trim()).append("\n");
                detailPesanan.append("Pesanan:\n");

                for (String item : daftarPesanan) {
                    detailPesanan.append("- ").append(item).append("\n");
                }

                detailPesanan.append("Total Harga: Rp ").append(totalHarga);

                // Tampilkan dialog detail pesanan
                new androidx.appcompat.app.AlertDialog.Builder(PemesananActivity.this)
                        .setTitle("Detail Pemesanan")
                        .setMessage(detailPesanan.toString())
                        .setPositiveButton("Kirim", (dialog, which) -> {
                            // Kirim data ke server jika pengguna menekan "Kirim"
                            kirimDataKeServer();
                        })
                        .setNegativeButton("Batal", (dialog, which) -> {
                            // Tutup dialog jika pengguna menekan "Batal"
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }

    private void kirimDataKeServer() {
        String url = Db_Contract.urlInsertPemesanan;

        // Mengonversi daftarPesanan menjadi JSON string
        JSONArray jsonArrayPesanan = new JSONArray();
        for (String pesanan : daftarPesanan) {
            jsonArrayPesanan.put(pesanan);
        }

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
                params.put("nama_pemesan", etNamaPemesanan.getText().toString().trim());
                params.put("alamat_pemesan", etAlamatPemesanan.getText().toString().trim());
                params.put("daftar_pesanan", jsonArrayPesanan.toString());
                params.put("total_harga", String.valueOf(totalHarga));
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(PemesananActivity.this);
        requestQueue.add(stringRequest);
    }

    // Fungsi untuk mengosongkan form
    private void clearForm() {
        etNamaPemesanan.setText("");
        etAlamatPemesanan.setText("");
        daftarPesanan.clear();
        pesananAdapter.notifyDataSetChanged();
        totalHarga = 0;
        tvTotalHarga.setText("Total Harga: Rp 0");
    }
}
