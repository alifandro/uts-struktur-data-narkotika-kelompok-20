# Dokumentasi Kontribusi (`CONTRIBUTIONS.md`)

## Catatan Pelaksanaan Tugas
Proyek UTS Struktur Data ini dikerjakan secara **individu (mandiri)** oleh satu mahasiswa tanpa pasangan kelompok. Oleh karena itu, seluruh tahapan pekerjaan—mulai dari perancangan struktur data, implementasi fitur aplikasi, pengumpulan serta verifikasi silang 50 berkas PDF putusan, pengujian otomatis, hingga penyusunan dokumentasi—dikerjakan sepenuhnya oleh kontributor tunggal di bawah ini dengan tetap menerapkan alur kerja berbasis **GitHub Issue** dan **Pull Request** secara bertahap.

---

## Ringkasan Kontribusi

* **Kontributor:** Alifandro (`@username-github-anda`)
* **Peran:** Perancang Struktur Data, Pengembang Aplikasi Java, Ekstraktor & Verifikator Dataset (50 Putusan), Penguji, dan Penulis Dokumentasi.

| Area Pekerjaan | Rincian Tugas yang Dikerjakan | Bukti Artefak di Repositori |
| :--- | :--- | :--- |
| **1. Struktur Data Utama & Sekunder** | Merancang dan mengimplementasikan interface `StrukturData<T>`, `DynamicArray<T>` (array dinamis generik), dan `AuditStack<T>` (stack generik) dari nol beserta `validateInvariant()`. | `src/main/java/app/StrukturData.java`, `DynamicArray.java`, `AuditStack.java` |
| **2. Fitur Aplikasi & Parser CSV** | Mengimplementasikan `CSVReader.java` (parser anti-koma dalam tanda kutip & validasi baris rusak), `Putusan.java`, `PutusanService.java` (9 fitur wajib), dan menu interaktif `Main.java`. | `src/main/java/app/CSVReader.java`, `PutusanService.java`, `Main.java` |
| **3. Pengujian & Validasi** | Menyusun program pengujian otomatis `PutusanTest.java` untuk memverifikasi seluruh 7 skenario uji wajib UTS serta mengecek konsistensi invariant. | `src/test/java/PutusanTest.java`, `docs/trace/` |
| **4. Dataset & Provenance** | Mengunduh 50 berkas PDF putusan dari Direktori Putusan MA, menyamarkan identitas terdakwa, memverifikasi ulang setiap atribut dan nomor halaman, serta menyusun 2 berkas CSV. | `data/putusan-50.csv`, `docs/data-provenance.csv`, Arsip Google Drive PDF |
| **5. Dokumentasi & ADR** | Menyusun `README.md`, 2 dokumen keputusan desain di `docs/adr/`, `AI_USAGE.md`, dan `CONTRIBUTIONS.md`. | `README.md`, `docs/adr/`, `AI_USAGE.md` |

---

## Jejak GitHub Issue, Pull Request, dan Commit Utama

*(Ganti tautan dan nomor di bawah ini sesuai dengan Issue dan PR yang Anda buat di repositori GitHub Anda)*

### Daftar GitHub Issues
1. **Issue #1:** Setup struktur proyek Maven dan implementasi struktur data `DynamicArray<T>` serta `AuditStack<T>` — `[Tautan Issue #1]`
2. **Issue #2:** Implementasi `CSVReader` dan 9 fitur aplikasi pada `PutusanService` serta `Main` — `[Tautan Issue #2]`
3. **Issue #3:** Pembuatan unit test 7 skenario wajib (`PutusanTest`) dan dokumen ADR — `[Tautan Issue #3]`
4. **Issue #4:** Ekstraksi dan verifikasi 50 putusan narkotika ke `data/putusan-50.csv` dan `docs/data-provenance.csv` — `[Tautan Issue #4]`

### Daftar Pull Requests & Catatan Review Teknis
1. **PR #1 (`feat/data-structures` -> `main`):** Implementasi struktur data utama `DynamicArray` dan sekunder `AuditStack` — `[Tautan PR #1]`
   * *Catatan Self-Review:* Memastikan operasi `remove` pada `DynamicArray` menghapus referensi elemen sisa (`elements[size - 1] = null`) untuk mencegah kebocoran memori dan menjaga `validateInvariant()` tetap bernilai `true`.
2. **PR #2 (`feat/app-features` -> `main`):** Implementasi parser CSV, layanan 9 fitur aplikasi, dan menu konsol — `[Tautan PR #2]`
   * *Catatan Self-Review:* Memperbaiki `parseCsvLine` agar tanda koma di dalam teks `barangBukti` dan `sourcePages` yang diapit tanda kutip ganda tidak memotong kolom, serta memastikan fitur pengurutan bekerja pada salinan array.
3. **PR #3 (`test/scenarios-and-docs` -> `main`):** Penambahan `PutusanTest` (7 skenario uji), dokumen ADR, dan `AI_USAGE.md` — `[Tautan PR #3]`
   * *Catatan Self-Review:* Menambahkan uji regresi untuk baris CSV rusak agar aplikasi menolak baris tidak valid tanpa menghentikan jalannya program.
4. **PR #4 (`data/putusan-50` -> `main`):** Finalisasi 50 record putusan valid dan jejak audit `data-provenance.csv` — `[Tautan PR #4]`
   * *Catatan Self-Review:* Mencocokkan ulang angka vonis (bulan), denda, berat barang bukti, dan nomor halaman sumber terhadap 50 berkas PDF di Google Drive serta memastikan seluruh nama terdakwa telah disamarkan.

### Tag Rilis Final
* **Tag:** `uts-v1.0` — `[Tautan Release/Tag uts-v1.0]`