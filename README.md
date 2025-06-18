# Aplikasi Manajemen Uang

Aplikasi Android untuk mengelola, melacak, dan memantau keuangan pribadi. Dirancang dengan Jetpack Compose untuk antarmuka pengguna modern dan responsif.

## Fitur Utama

- **Pelacakan Transaksi**: Catat pemasukan dan pengeluaran dengan kategori
- **Ringkasan Keuangan**: Lihat saldo, total pemasukan, dan pengeluaran
- **Laporan**: Analisis keuangan harian, bulanan, dan tahunan
- **Visualisasi Data**: Diagram batang dan pie chart untuk memvisualisasikan data keuangan

## Persyaratan Sistem

- Android Studio Hedgehog (2023.1.1) atau lebih baru
- Java Development Kit (JDK) 21
- Minimal SDK: 24 (Android 7.0/Nougat)
- Target SDK: 34 (Android 14)
- Gradle 8.9.0 atau lebih tinggi

## Teknologi yang Digunakan

- **Kotlin**: Bahasa pemrograman utama
- **Jetpack Compose**: Framework UI modern berbasis deklaratif
- **Material 3**: Desain UI mengikuti pedoman Material Design terbaru
- **Navigation Compose**: Navigasi antar layar
- **Gson**: Serialisasi data untuk menyimpan transaksi
- **Vico Charts**: Library untuk visualisasi data grafik

## Cara Menjalankan Aplikasi

1. Pastikan sudah telah menginstal JDK 21
2. Unduh dan instal Android Studio versi terbaru
3. Clone repositori ini
4. Buka project dalam Android Studio
5. Sinkronkan Gradle
6. Pastikan bahwa Gradle menggunakan JDK yang benar (konfigurasi di gradle.properties)
7. Jalankan aplikasi pada emulator atau perangkat fisik


## Konfigurasi Tambahan

Jika mengalami masalah dengan lokasi JDK, pastikan untuk mengonfigurasi lokasi JDK 21 di file `gradle.properties`:

```
org.gradle.java.home=C:\\Program Files\\Java\\jdk-21
```

Sesuaikan path tersebut dengan lokasi instalasi JDK 21 di komputer.
