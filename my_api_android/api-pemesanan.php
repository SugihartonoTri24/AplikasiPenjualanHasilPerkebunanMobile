<?php
include 'koneksi.php';

// Ambil data dari parameter POST
$nama_pemesan = isset($_POST['nama_pemesan']) ? mysqli_real_escape_string($koneksi, $_POST['nama_pemesan']) : null;
$alamat_pemesan = isset($_POST['alamat_pemesan']) ? mysqli_real_escape_string($koneksi, $_POST['alamat_pemesan']) : null;
$daftar_pesanan = isset($_POST['daftar_pesanan']) ? $_POST['daftar_pesanan'] : null; // Berupa JSON string
$total_harga = isset($_POST['total_harga']) ? mysqli_real_escape_string($koneksi, $_POST['total_harga']) : null;

// Validasi input agar tidak ada data kosong
if (!empty($nama_pemesan) && !empty($alamat_pemesan) && !empty($daftar_pesanan) && !empty($total_harga)) {
    // Konversi JSON daftar_pesanan ke format array
    $pesanan_array = json_decode($daftar_pesanan, true);

    if (json_last_error() === JSON_ERROR_NONE) {
        // Query untuk menyimpan data pemesanan ke database
        $query_pemesanan = "INSERT INTO pemesanan (nama_pemesan, alamat_pemesan, total_harga) 
                            VALUES ('$nama_pemesan', '$alamat_pemesan', '$total_harga')";

        $result_pemesanan = mysqli_query($koneksi, $query_pemesanan);

        if ($result_pemesanan) {
            // Ambil ID pemesanan yang baru saja dimasukkan
            $id_pemesanan = mysqli_insert_id($koneksi);

            // Loop untuk menyimpan detail pesanan
            $errors = [];
            foreach ($pesanan_array as $pesanan) {
                // Pisahkan nama barang dan harga (format: "Nama Barang - Rp Harga")
                $pesanan_parts = explode(" - Rp ", $pesanan);
                if (count($pesanan_parts) == 2) {
                    $nama_barang = mysqli_real_escape_string($koneksi, $pesanan_parts[0]);
                    $harga_barang = mysqli_real_escape_string($koneksi, str_replace(".", "", $pesanan_parts[1])); // Hilangkan titik di harga

                    $query_detail = "INSERT INTO detail_pemesanan (id_pemesanan, nama_barang, harga_barang) 
                                     VALUES ('$id_pemesanan', '$nama_barang', '$harga_barang')";

                    $result_detail = mysqli_query($koneksi, $query_detail);

                    if (!$result_detail) {
                        $errors[] = "Gagal menyimpan detail: " . mysqli_error($koneksi);
                    }
                } else {
                    $errors[] = "Format pesanan tidak valid: " . $pesanan;
                }
            }

            // Jika semua detail berhasil disimpan
            if (empty($errors)) {
                $response = [
                    "status" => "success",
                    "message" => "Pemesanan berhasil disimpan!"
                ];
            } else {
                $response = [
                    "status" => "partial_success",
                    "message" => "Pemesanan berhasil, tetapi beberapa detail gagal disimpan.",
                    "errors" => $errors
                ];
            }
        } else {
            $response = [
                "status" => "error",
                "message" => "Gagal menyimpan pemesanan: " . mysqli_error($koneksi)
            ];
        }
    } else {
        $response = [
            "status" => "error",
            "message" => "Format JSON daftar pesanan tidak valid!"
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
