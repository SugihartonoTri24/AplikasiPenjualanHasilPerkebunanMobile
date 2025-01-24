<?php
include 'koneksi.php';

// Ambil data dari parameter POST
$id_barang = isset($_POST['id_barang']) ? mysqli_real_escape_string($koneksi, $_POST['id_barang']) : null;
$nama_barang = isset($_POST['nama_barang']) ? mysqli_real_escape_string($koneksi, $_POST['nama_barang']) : null;
$jumlah = isset($_POST['jumlah']) ? mysqli_real_escape_string($koneksi, $_POST['jumlah']) : null;
$harga_satuan = isset($_POST['harga_satuan']) ? mysqli_real_escape_string($koneksi, $_POST['harga_satuan']) : null;

// Validasi input agar tidak ada data kosong
if (!empty($id_barang) && !empty($nama_barang) && !empty($jumlah) && !empty($harga_satuan)) {
    // Query untuk menyimpan data stok barang ke database
    $query_stok = "INSERT INTO stok_barang (id_barang, nama_barang, jumlah, harga_satuan) 
                    VALUES ('$id_barang', '$nama_barang', '$jumlah', '$harga_satuan') 
                    ON DUPLICATE KEY UPDATE 
                        nama_barang = VALUES(nama_barang), 
                        jumlah = VALUES(jumlah), 
                        harga_satuan = VALUES(harga_satuan)";

    $result_stok = mysqli_query($koneksi, $query_stok);

    if ($result_stok) {
        $response = [
            "status" => "success",
            "message" => "Stok barang berhasil disimpan!"
        ];
    } else {
        $response = [
            "status" => "error",
            "message" => "Gagal menyimpan stok barang: " . mysqli_error($koneksi)
        ];
    }
} else {
    $response = [
        "status" => "error",
        "message" => "Ada data yang masih kosong!"
    ];
}

// Kembalikan response dalam format JSON
header('Content-Type: application/json');
echo json_encode($response);
?>
