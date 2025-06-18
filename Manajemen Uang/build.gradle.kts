// File build tingkat-atas di mana Anda dapat menambahkan opsi konfigurasi yang umum untuk semua sub-proyek/modul.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}