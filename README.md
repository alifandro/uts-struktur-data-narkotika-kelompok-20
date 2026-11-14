# Aplikasi Pengelolaan Data Putusan Pengadilan Narkotika

Proyek Ujian Tengah Semester (UTS) Mata Kuliah **Struktur Data**
Program Studi Informatika — Fakultas Teknik, Universitas Muhammadiyah Malang (UMM)
**Semester Ganjil 2026/2027**
**Dosen Pengampu:** Galih W. Wicaksono

---

## Identitas Mahasiswa
* **Nama:** Alifandro Atilla Assyawa
* **NIM:** 202510370110162
* **Kelas:** 3B
* **Status Pengerjaan:** Individu (Mandiri)
* **Pengadilan Negeri yang Digunakan:** Mojokerto
* **Arsip 50 PDF Putusan (Google Drive Public):** https://drive.google.com/drive/folders/1cU8-SEq8GOmB2lekXRZchbEOSEzrtbP1

---

## Pilihan Struktur Data & Desain

Aplikasi ini dibangun menggunakan pemrograman berorientasi objek (Java 17) dengan struktur data yang diimplementasikan dari nol tanpa menggunakan koleksi bawaan `java.util` (`ArrayList`, `LinkedList`, `Stack`, `Vector`, `Queue`, `Deque`, `PriorityQueue`, maupun `HashMap`) untuk penyimpanan inti:

1. **Struktur Data Utama — `DynamicArray<T>`**
   * **Representasi:** Array dinamis generik berbasis array mentah (`Object[]`) yang mengatur kapasitas secara otomatis (*auto-resizing* menggandakan kapasitas saat penuh dan menyusutkan kapasitas setengahnya saat penggunaan $\le 25\%$).
   * **Operasi Minimum:** `add`, `insert`, `get`, `set`, `remove`, `search`, `contains`, `size`, `isEmpty`, `clear`, `resize`, dan `validateInvariant()`.
   * **Invariant Utama:** Kapasitas array internal selalu $\ge \text{size}$, seluruh elemen aktif tersusun berurutan pada indeks `0` hingga `size - 1` tanpa nilai `null` di tengah, dan slot indeks `size` hingga `capacity - 1` selalu bersih (`null`).

2. **Struktur Data Sekunder — `AuditStack<T>`**
   * **Representasi:** Tumpukan (*Last-In, First-Out* / LIFO) generik berbasis array mentah (`Object[]`) untuk merekam riwayat seluruh aktivitas pengguna (*audit log*).
   * **Operasi Minimum:** `push`, `pop`, `peek`, `searchPreserve`, `restore`, `add`, `get`, `remove`, `size`, `isEmpty`, `contains`, `clear`, dan `validateInvariant()`.
   * **Invariant Utama:** Penunjuk puncak `top` selalu berada pada rentang `-1` hingga `capacity - 1`, dan pencarian riwayat menggunakan `searchPreserve` memindahkan elemen sementara ke stack bantu lalu mengembalikannya (`restore`) sehingga urutan LIFO asli tidak berubah.

---

## Struktur Direktori Repositori

```text
uts-struktur-data-narkotika-kelompok-XX/
├── src/
│   ├── main/java/app/
│   │   ├── Atribut.java          # Model baris audit untuk docs/data-provenance.csv
│   │   ├── AuditStack.java       # Struktur data sekunder (Stack LIFO generik)
│   │   ├── CSVReader.java        # Parser & validator CSV putusan (16 kolom atribut)
│   │   ├── DynamicArray.java     # Struktur data utama (Array dinamis generik)
│   │   ├── Main.java             # Antarmuka menu interaktif berbasis konsol
│   │   ├── Putusan.java          # Model entitas Putusan Pengadilan (16 atribut)
│   │   ├── PutusanService.java   # Logika 9 fitur aplikasi (Cari, Filter, Agregasi, Urut, Ekspor)
│   │   └── StrukturData.java     # Kontrak interface generik struktur data
│   └── test/java/
│       └── PutusanTest.java      # Pengujian otomatis 7 skenario wajib UTS
├── data/
│   └── putusan-50.csv            # Dataset terstruktur 50 putusan (tanpa file PDF asli)
├── docs/
│   ├── data-provenance.csv       # Jejak audit & halaman sumber PDF per atribut
│   ├── adr/                      # Architecture Decision Records (minimal 2 keputusan)
│   ├── trace/                    # Bukti trace manual & keluaran pengujian
│   └── demo/                     # Keluaran hasil ekspor aplikasi (.csv dan .txt)
├── AI_USAGE.md                   # Log transparan penggunaan AI generatif
├── CONTRIBUTIONS.md              # Dokumentasi kontribusi, Issue, PR, dan commit
├── README.md                     # Dokumentasi utama proyek
└── pom.xml                       # Konfigurasi build Maven (Java 17)

```

---

## Cara Build, Test, dan Menjalankan Program

### Prasyarat

* Java Development Kit (JDK) versi **17** atau lebih baru.
* Apache Maven **3.8+** (opsional jika menjalankan via VS Code atau `javac`).
* Visual Studio Code dengan ekstensi *Extension Pack for Java* (jika menjalankan lewat IDE).

### Opsi 1: Menggunakan Visual Studio Code

1. Buka folder utama proyek (`UTS-STRUKDAT`) di Visual Studio Code.
2. Tunggu hingga proses *Maven / Java Language Server* selesai memuat `pom.xml`.
3. **Menjalankan Pengujian (7 Skenario Wajib):** Buka berkas `src/test/java/PutusanTest.java`, lalu klik tombol **`Run`** di atas method `public static void main(String[] args)`.
4. **Menjalankan Aplikasi Utama:** Buka berkas `src/main/java/app/Main.java`, lalu klik tombol **`Run`** di atas method `public static void main(String[] args)` (atau tekan `F5`).

### Opsi 2: Menggunakan Apache Maven & Terminal

Jalankan perintah berikut dari direktori utama proyek (`UTS-STRUKDAT`):

1. **Build (Kompilasi kode utama dan kode pengujian):**
```bash
mvn clean test-compile

```


2. **Menjalankan Pengujian (`PutusanTest`):**
* **Windows (PowerShell / CMD):**
```powershell
java -cp "target/classes;target/test-classes" PutusanTest

```


* **Linux / macOS:**
```bash
java -cp "target/classes:target/test-classes" PutusanTest

```




3. **Menjalankan Aplikasi Utama (`app.Main`):**
```bash
java -cp target/classes app.Main

```



### Opsi 3: Menggunakan `javac` dan `java` Manual (Tanpa Maven)

#### Windows (PowerShell / CMD)

```powershell
# 1. Kompilasi seluruh kode sumber dan pengujian ke folder out/
if (!(Test-Path out)) { mkdir out }
javac -encoding UTF-8 -d out src/main/java/app/*.java src/test/java/PutusanTest.java

# 2. Jalankan pengujian 7 skenario wajib
java -cp out PutusanTest

# 3. Jalankan aplikasi utama
java -cp out app.Main

```

#### Linux / macOS (Bash / Zsh)

```bash
# 1. Kompilasi seluruh kode sumber dan pengujian ke folder out/
mkdir -p out
javac -encoding UTF-8 -d out src/main/java/app/*.java src/test/java/PutusanTest.java

# 2. Jalankan pengujian 7 skenario wajib
java -cp out PutusanTest

# 3. Jalankan aplikasi utama
java -cp out app.Main

```

---

## Fitur Aplikasi & Contoh Perintah

Saat `app.Main` dijalankan, program otomatis memuat berkas `data/putusan-50.csv` ke dalam `DynamicArray<Putusan>` dan menampilkan menu interaktif:

```text
==========================================
  APLIKASI DATA PUTUSAN NARKOTIKA (UTS)
  Total Record: 50 | Invariant: VALID
==========================================
1. Impor Ulang CSV (data/putusan-50.csv)
2. Tampilkan Semua Putusan
3. Tambah Putusan Baru
4. Hapus Putusan (berdasarkan Case ID)
5. Cari Putusan (Case ID / Nomor Perkara)
6. Filter Putusan (Zat, Berat, Pasal, Vonis)
7. Tampilkan Laporan Agregasi
8. Urutkan Putusan berdasarkan Vonis
9. Lihat Riwayat Operasi (Audit Stack)
10. Ekspor Data & Laporan ke File
0. Keluar
Pilih menu:

```

### Contoh Penggunaan Perintah Interaktif

* **Menu `5` (Cari Putusan & Hitung Langkah):**
```text
Pilih menu: 5
Masukkan Case ID atau Nomor Perkara: 02/Pid.Sus/2015/PN.Kpn.
[CARI] Ditemukan pada indeks [0] dalam 1 langkah.
Detail: [P001] 02/Pid.Sus/2015/PN.Kpn. | Pengadilan Negeri Kepanjen | Zat: Ganja (2,633g) | Pasal: Pasal 114 ayat (1) | Vonis: 60 bln | Denda: Rp1000000000

```


* **Menu `6` (Filter Gabungan):**
```text
Pilih menu: 6
Jenis Narkotika: Ganja
Berat Minimal (gram): 1.0
Berat Maksimal (gram): 10.0
Pasal Terbukti: 114
Vonis Minimal (bulan): 48
Vonis Maksimal (bulan): 72

```


* **Menu `7` (Laporan Agregasi):** Menampilkan jumlah total perkara, rata-rata masa vonis (bulan), total nominal denda (rupiah), serta distribusi jumlah perkara per jenis narkotika.
* **Menu `8` (Urutkan Berdasarkan Vonis):** Ketik `y` untuk urutan terendah ke tertinggi (*ascending*) atau `n` untuk tertinggi ke terendah (*descending*) pada salinan array tanpa mengubah urutan penyimpanan asli di struktur utama.
* **Menu `9` (Riwayat Operasi / Audit Stack):** Menampilkan seluruh riwayat aksi pengguna secara LIFO (aktivitas terbaru berada di urutan paling atas).
* **Menu `10` (Ekspor Laporan):** Menyimpan dataset hasil olahan ke `docs/demo/hasil-ekspor.csv` dan ringkasan statistik ke `docs/demo/laporan-agregasi.txt`.
