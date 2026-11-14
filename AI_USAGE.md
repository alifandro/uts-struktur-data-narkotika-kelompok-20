# Log Penggunaan AI Generatif (`AI_USAGE.md`)

Dokumen ini merupakan catatan audit penggunaan alat bantu AI generatif dalam pengerjaan tugas UTS Struktur Data[cite: 6]. Seluruh saran dan keluaran dari AI telah diuji, disesuaikan dengan desain arsitektur aplikasi, serta diverifikasi secara manual terhadap kode sumber dan berkas PDF putusan pengadilan[cite: 6].

---

## Ringkasan Sesi Penggunaan AI

| No | Tanggal | Anggota | Alat AI | Fokus / Tujuan Penggunaan | Status Verifikasi |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | 13 Nov 2026 | Alifandro | Gemini | Pemahaman spesifikasi tugas & pemetaan 16 atribut skema CSV | Terverifikasi |
| 2 | 13 Nov 2026 | Alifandro | Gemini | Perancangan `DynamicArray<T>`, `AuditStack<T>`, dan 9 fitur aplikasi | Terverifikasi (`PutusanTest` PASS) |
| 3 | 13 Nov 2026 | Alifandro | Gemini | Debugging `package app;`, konfigurasi `pom.xml`, & pemisahan 2 CSV | Terverifikasi (Build & Run sukses) |
| 4 | 13 Nov 2026 | Alifandro | Gemini | Penyusunan struktur dokumentasi proyek (`README`, `ADR`, `CONTRIBUTIONS`) | Terverifikasi |

---

## Detail Log Penggunaan AI

### Sesi 1 — Pemahaman Spesifikasi & Pemetaan Skema CSV
* **Tanggal:** 13 November 2026
* **Anggota:** Alifandro
* **Alat:** Gemini
* **Tujuan:** Memahami perbedaan fungsi antara berkas dataset utama (`data/putusan-50.csv`) dan berkas jejak audit (`docs/data-provenance.csv`), serta aturan ekstraksi atribut dari dokumen PDF putusan[cite: 2, 6].
* **Prompt / Ringkasan:** *"Jelaskan apa yang diminta pada tugas UTS ini, bagaimana perbedaan peran data/putusan-50.csv dan docs/data-provenance.csv, serta contoh pemetaan atribut dari berkas PDF putusan."*
* **Bagian Keluaran yang Digunakan:** Pemetaan 16 atribut skema minimum (`caseId` hingga `sourcePages`), format 8 kolom `data-provenance.csv`, dan aturan pembungkusan tanda kutip ganda untuk teks yang mengandung tanda koma[cite: 2].
* **Perubahan yang Dilakukan:** Menyamarkan nama lengkap terdakwa menjadi kode anonim (`TRD-01`, dst.), mengonversi satuan masa pidana dari tahun ke bulan, serta memastikan penulisan nilai kosong menggunakan `0` atau `null` tanpa memperkirakan data[cite: 1, 2].
* **Cara Verifikasi:** Mencocokkan struktur header dan tipe data setiap kolom terhadap dokumen Panduan UTS serta memeriksa kesesuaian nilai dengan halaman PDF asli[cite: 2, 6].

### Sesi 2 — Perancangan Struktur Data & Logika Fitur Aplikasi
* **Tanggal:** 13 November 2026[cite: 30]
* **Anggota:** Alifandro
* **Alat:** Gemini
* **Tujuan:** Membangun struktur data utama `DynamicArray<T>` dan struktur sekunder `AuditStack<T>` dari nol tanpa koleksi `java.util`, beserta logika 9 fitur aplikasi dan 7 skenario pengujian[cite: 3, 4].
* **Prompt / Ringkasan:** *"Buatkan kerangka kode Java terpisah dalam package app untuk DynamicArray generik buatan sendiri, AuditStack untuk riwayat operasi, PutusanService untuk 9 fitur wajib, dan PutusanTest untuk 7 skenario pengujian."*
* **Bagian Keluaran yang Digunakan:** Kerangka kelas `StrukturData.java`, `DynamicArray.java`, `AuditStack.java`, `CSVReader.java`, `PutusanService.java`, `Main.java`, dan `PutusanTest.java`.
* **Perubahan yang Dilakukan:** Menyesuaikan seluruh kelas ke dalam `package app;`, menyempurnakan algoritma penghitung langkah pada fitur pencarian, serta menambahkan pemeriksaan `validateInvariant()` pada setiap operasi manipulasi elemen[cite: 3, 4].
* **Cara Verifikasi:** Menjalankan kelas pengujian `PutusanTest.java` yang menguji seluruh 7 skenario wajib (impor, pencarian batas, hapus awal-tengah-akhir, sisip ulang, filter gabungan, `searchPreserve` pada Stack, dan penolakan baris CSV rusak) hingga seluruhnya berstatus `[PASS]`[cite: 4].

### Sesi 3 — Konfigurasi Maven (`pom.xml`) & Debugging Pembacaan CSV
* **Tanggal:** 13 November 2026[cite: 30]
* **Anggota:** Alifandro
* **Alat:** Gemini
* **Tujuan:** Mengatasi error deklarasi *package* saat memindahkan berkas ke `src/main/java/app/`, mengatur jalur direktori `src/test/java` di `pom.xml`, dan memperbaiki penolakan kolom CSV saat pengujian awal[cite: 5, 30].
* **Prompt / Ringkasan:** *"Bagaimana cara setup source directory dan test directory di pom.xml agar tidak perlu pengaturan manual di IDE, dan mengapa CSVReader menolak baris CSV dengan peringatan jumlah kolom bukan 16?"*
* **Bagian Keluaran yang Digunakan:** Konfigurasi `<sourceDirectory>` dan `<testSourceDirectory>` pada `pom.xml` serta pemisahan isi sampel CSV 8 kolom ke `docs/data-provenance.csv` dan 16 kolom ke `data/putusan-50.csv`.
* **Perubahan yang Dilakukan:** Menghapus pengaturan `.classpath` manual, memuat ulang proyek Maven di VS Code, dan mengisi baris uji awal pada `data/putusan-50.csv` sesuai skema 16 kolom.
* **Cara Verifikasi:** Mengompilasi ulang proyek menggunakan konfigurasi `pom.xml` dan menjalankan `app.Main` hingga seluruh data terbaca tanpa peringatan kolom[cite: 30].

### Sesi 4 — Penyusunan Kerangka Dokumentasi Repositori
* **Tanggal:** 13 November 2026[cite: 30]
* **Anggota:** Alifandro
* **Alat:** Gemini
* **Tujuan:** Menyusun struktur berkas dokumentasi wajib (`README.md`, `AI_USAGE.md`, `CONTRIBUTIONS.md`, dan dokumen ADR) agar selaras dengan daftar periksa pengumpulan[cite: 7, 31].
* **Prompt / Ringkasan:** *"Buatkan kerangka file README.md, AI_USAGE.md, dan CONTRIBUTIONS.md sesuai syarat pengumpulan UTS."*
* **Bagian Keluaran yang Digunakan:** Susunan bagian dokumentasi, format tabel kontribusi, dan panduan perintah *build* serta *run* aplikasi.
* **Perubahan yang Dilakukan:** Mengisi identitas pribadi, menyesuaikan perintah eksekusi untuk lingkungan Windows PowerShell dan Maven, serta mencatat status pengerjaan mandiri secara transparan.
* **Cara Verifikasi:** Memeriksa kelengkapan setiap berkas terhadap rubrik penilaian dan formulir pengumpulan tugas[cite: 6, 31].

---

## Template Log Tambahan (Untuk Sesi Ekstraksi 50 PDF)
*(Salin dan isi blok di bawah ini setiap kali menggunakan AI untuk membantu merangkum teks PDF putusan)*

### Sesi 5 — Ekstraksi Teks PDF Putusan (`P001` – `P050`)
* **Tanggal:** `[Isi Tanggal Pengerjaan]`
* **Anggota:** Alifandro
* **Alat:** Gemini
* **Tujuan:** Membantu mengidentifikasi atribut hukum dari berkas PDF putusan pengadilan sebelum dilakukan verifikasi manual.
* **Prompt / Ringkasan:** *"Ekstrak 16 atribut putusan (nomor perkara, pengadilan, tanggal, jenis narkotika, berat gram, pasal dakwaan/terbukti, tuntutan, vonis, denda, barang bukti, keadaan memberatkan/meringankan, dan nomor halaman) ke format baris CSV."*
* **Bagian Keluaran yang Digunakan:** Draf awal nilai atribut per putusan.
* **Perubahan yang Dilakukan:** Memeriksa ulang seluruh angka, pasal, dan nomor halaman langsung pada PDF asli di Google Drive, memperbaiki konversi satuan gram/bulan, menyamarkan identitas terdakwa, dan mencatat halaman sumber di `docs/data-provenance.csv`.
* **Cara Verifikasi:** Mengimpor `data/putusan-50.csv` ke aplikasi Java (`size = 50`) dan mencocokkan hasil filter gabungan dengan catatan di `docs/data-provenance.csv`[cite: 4].