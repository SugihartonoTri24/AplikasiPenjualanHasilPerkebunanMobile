package com.example.projectuas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPelanggan = findViewById(R.id.btnPelanggan);
        Button btnPemesanan = findViewById(R.id.btnPemesanan);
        Button btnStokBarang = findViewById(R.id.btnStokBarang);


        btnPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PelangganActivity.class);
                startActivity(intent);
            }
        });

        btnPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PemesananActivity.class);
                startActivity(intent);
            }
        });

        btnStokBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StokBarangActivity.class);
                startActivity(intent);
            }
        });


    }
}