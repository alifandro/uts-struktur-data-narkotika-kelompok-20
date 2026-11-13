# ADR 002: Strategi Pencarian, Penyaringan, dan Pengurutan Data Putusan

* **Status:** Diterima
* **Tanggal:** 13 November 2026
* **Penulis:** Alifandro

---

## Konteks
Sesuai spesifikasi fitur wajib aplikasi, sistem harus mampu:
1. Mencari satu putusan berdasarkan `caseId` atau `nomorPerkara` sambil melaporkan **jumlah langkah** pencariannya.
2. Menyaring data berdasarkan kombinasi kriteria (`jenisNarkotika`, rentang `beratBarangBuktiGram`, `pasalTerbukti`, dan rentang `vonisBulan`).
3. Mengurutkan daftar putusan berdasarkan lama pidana penjara (`vonisBulan`) **tanpa merusak *invariant* maupun urutan asli** pada struktur data utama.

---

## Keputusan
1. **Strategi Pencarian & Filter:** Menggunakan **Pencarian Sekuensial (*Linear Search*)** dengan pencacah iterasi eksplisit (`langkah`) yang memeriksa elemen dari indeks `0` hingga `size - 1`.
2. **Strategi Pengurutan:** Menggunakan algoritma **Bubble Sort** yang dijalankan pada **salinan `DynamicArray<Putusan>`** (*defensive copy*), bukan langsung pada array utama.

---

## Alasan Teknis & Konsekuensi

### 1. Pencarian Sekuensial (*Linear Search*) dengan Pencacah Langkah
* **Alasan:** Data yang diimpor dari `data/putusan-50.csv` tersimpan sesuai urutan masuk berkas (berdasarkan `caseId`), bukan terurut berdasarkan `nomorPerkara` atau atribut lainnya. *Binary Search* tidak dapat diterapkan tanpa mengurutkan ulang struktur utama terlebih dahulu.
* **Konsekuensi Kinerja:**
  * *Best Case:* $O(1)$ — ditemukan pada indeks ke-0 dalam `1 langkah`.
  * *Average Case:* $O(n/2)$ — rata-rata `25 langkah` untuk dataset berisi 50 putusan.
  * *Worst Case:* $O(n)$ — ditemukan pada indeks terakhir atau data tidak ditemukan setelah `50 langkah`.
  * Untuk ukuran dataset $N = 50$, 50 iterasi memakan waktu mikrodetik dan memberikan transparansi penghitungan langkah yang akurat saat pengujian batas.

### 2. Pengurutan (*Bubble Sort*) pada Salinan Array
* **Alasan:** Panduan tugas mensyaratkan agar fitur pengurutan menghasilkan tampilan terurut berdasarkan vonis tanpa merusak *invariant* struktur utama. Dengan menyalin referensi objek `Putusan` ke instansi `DynamicArray` baru sebelum melakukan pertukaran elemen (`set`), urutan asli data hasil impor di struktur utama tetap terjaga.
* **Konsekuensi Kinerja:**
  * Kompleksitas waktu *Bubble Sort* adalah $O(n^2)$ dengan maksimal $\frac{50 \times 49}{2} = 1.225$ perbandingan untuk $N = 50$, namun tidak membutuhkan memori tumpukan rekursi tambahan ($O(1)$ ruang bantu di luar array salinan) dan bersifat stabil (*stable sort*) untuk putusan dengan lama vonis yang sama.