<?php

include 'koneksi.php';

$username = $_POST['username'];
$password = $_POST['password'];
$alamat = $_POST['alamat'];

$queryRegister = "SELECT * FROM user WHERE username = '".$username."'";

$msql = mysqli_query($koneksi, $queryRegister);

$result = mysqli_num_rows($msql);

if (!empty($username) && !empty($password) && !empty($alamat)){

    if($result == 0){
        $regis = "INSERT INTO user (username, password, alamat)
        VALUES ('$username', '$password', '$alamat')";

        $msqlRegis = mysqli_query($koneksi, $regis);

        echo "Daftar Berhasil";
    }else{
        echo "Username Sudah Digunakan";
    }
}else{
    echo "Semua Data Harus Di Isi Lengkap";
}