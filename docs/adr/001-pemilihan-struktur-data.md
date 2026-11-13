# ADR 001: Pemilihan Struktur Data Utama (`DynamicArray`) dan Sekunder (`AuditStack`)

* **Status:** Diterima
* **Tanggal:** 13 November 2026
* **Penulis:** Alifandro

---

## Konteks
Aplikasi pengelolaan 50 putusan pengadilan narkotika membutuhkan dua jenis penyimpanan inti yang dibangun secara mandiri (generik `<T>`) tanpa menggunakan koleksi bawaan `java.util` (`ArrayList`, `LinkedList`, `Stack`, `Queue`, `Vector`, atau `HashMap`):
1. **Struktur Data Utama:** Menyimpan seluruh 50 objek `Putusan` selama aplikasi berjalan dan mendukung operasi akses indeks, penyisipan, penghapusan, pencarian, penyaringan, agregasi, serta pengurutan.
2. **Struktur Data Sekunder:** Menyimpan riwayat aktivitas pengguna (*audit log*) secara berurutan dari aksi terbaru serta mendukung pencarian riwayat tanpa merusak urutan penyimpanan.

---

## Keputusan
1. Memilih **Array Dinamis (`DynamicArray<T>`)** berbasis array mentah `Object[]` sebagai **struktur data utama**.
2. Memilih **Tumpukan (`AuditStack<T>`)** berbasis prinsip *Last-In, First-Out* (LIFO) dengan array mentah `Object[]` sebagai **struktur data sekunder**.

---

## Pertimbangan & Perbandingan Alternatif

### A. Struktur Data Utama (`DynamicArray<T>`)
* **Mengapa `DynamicArray<T>` dipilih:**
  * Operasi **Tampil**, **Filter**, **Agregasi**, dan **Urutan** memerlukan pembacaan seluruh 50 elemen secara berulang serta pertukaran elemen berdasarkan indeks (`get(i)` dan `set(i, val)`) dalam waktu konstan $O(1)$.
  * Pengelolaan kapasitas otomatis (*auto-resizing* menggandakan kapasitas saat penuh dan menyusutkannya saat penggunaan $\le 25\%$) menjaga efisiensi memori sekaligus mempertahankan akses langsung berbasis indeks.
* **Mengapa alternatif lain tidak dipilih:**
  * *Singly Linked List:* Akses indeks acak (`get(i)`) dan pertukaran elemen saat pengurutan membutuhkan penelusuran pointer dari `head` ($O(n)$), serta memerlukan memori tambahan untuk menyimpan referensi `next` pada setiap *node*.
  * *Stack / Queue sebagai struktur utama:* Membatasi akses langsung ke elemen tengah sehingga operasi penghapusan indeks tertentu dan penyaringan multi-kriteria memerlukan pembongkaran dan pemulihan struktur berulang kali.

### B. Struktur Data Sekunder (`AuditStack<T>`)
* **Mengapa `AuditStack<T>` dipilih:**
  * Kebutuhan utama fitur **Audit** adalah menampilkan aktivitas pengguna yang paling baru dilakukan terlebih dahulu (*most recent first*), yang secara alami sesuai dengan mekanisme LIFO (`push` dan `pop` dalam waktu $O(1)$).
  * Pencarian log aktivitas diimplementasikan secara non-destruktif melalui `searchPreserve` dengan memindahkan elemen ke stack sementara lalu memulihkannya kembali (`restore`).

---

## Analisis Kompleksitas & Invariant

| Struktur Data | Operasi | Kompleksitas Waktu | Kompleksitas Ruang | Invariant yang Dijaga (`validateInvariant()`) |
| :--- | :--- | :--- | :--- | :--- |
| **`DynamicArray<T>`** | `add(T)` | $O(1)$ *amortized* ($O(n)$ saat *resize*) | $O(n)$ | Kapasitas array $\ge \text{size}$; indeks `0` s.d. `size - 1` terisi non-`null`; indeks `size` s.d. `capacity - 1` bernilai `null`. |
| **`DynamicArray<T>`** | `get(pos)` / `set(pos, val)` | $O(1)$ | $O(1)$ | Indeks berada pada rentang valid `0 <= pos < size`. |
| **`DynamicArray<T>`** | `insert(idx, val)` / `remove(pos)` | $O(n)$ (pergeseran elemen) | $O(1)$ | Tidak ada celah kosong di tengah array; referensi sisa setelah `remove` dibersihkan menjadi `null`. |
| **`AuditStack<T>`** | `push(T)` / `pop()` / `peek()` | $O(1)$ *amortized* | $O(n)$ | Penunjuk puncak `-1 <= top < capacity`; elemen aktif tersusun rapat dari `0` hingga `top`. |
| **`AuditStack<T>`** | `searchPreserve(T)` | $O(n)$ | $O(n)$ (stack bantu) | Urutan elemen LIFO sebelum dan sesudah pencarian tetap identik. |