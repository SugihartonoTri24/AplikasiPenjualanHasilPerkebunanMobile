<?php

include 'koneksi.php';

// Get data from POST request
$id_pelanggan = $_POST['id_pelanggan'];
$nama = $_POST['nama'];
$alamat = $_POST['alamat'];
$telepon = $_POST['telepon'];
$email = $_POST['email'];

// Check if all fields are filled
if (!empty($id_pelanggan) && !empty($nama) && !empty($alamat) && !empty($telepon) && !empty($email)) {
    
    // Check if the ID_Pelanggan already exists
    $queryCheck = "SELECT * FROM pelanggan WHERE id_pelanggan = '".$id_pelanggan."'";
    $msqlCheck = mysqli_query($koneksi, $queryCheck);
    $resultCheck = mysqli_num_rows($msqlCheck);

    if ($resultCheck == 0) {
        // Insert new customer data
        $queryInsert = "INSERT INTO pelanggan (id_pelanggan, nama, alamat, telepon, email)
                        VALUES ('$id_pelanggan', '$nama', '$alamat', '$telepon', '$email')";
        
        $msqlInsert = mysqli_query($koneksi, $queryInsert);

        if ($msqlInsert) {
            echo json_encode(["status" => "success", "message" => "Pendaftaran Pelanggan Berhasil"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Pendaftaran Pelanggan Gagal"]);
        }
    } else {
        echo json_encode(["status" => "error", "message" => "ID Pelanggan Sudah Digunakan"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Semua Data Harus Di Isi Lengkap"]);
}

?>