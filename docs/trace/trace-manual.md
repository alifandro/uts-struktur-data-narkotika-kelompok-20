# Trace Manual Operasi Struktur Data & Pencacah Langkah

Dokumen ini berisi penelusuran (*trace*) manual perubahan indeks, pergeseran elemen, dan jumlah langkah operasi pada `DynamicArray<Putusan>` dan `AuditStack<String>` sesuai skenario pengujian wajib.

---

## 1. Trace Pencacah Langkah Pencarian (`cariPutusan`)
* **Kondisi Awal:** `DynamicArray<Putusan>` memuat `N = 50` record (`P001` pada indeks `0` hingga `P050` pada indeks `49`).

| Skenario Pencarian | Target Pencarian | Indeks Ditemukan | Jumlah Langkah | Status |
| :--- | :--- | :---: | :---: | :--- |
| **Batas Awal** | `P001` (`02/Pid.Sus/2015/PN.Kpn.`) | `0` | `1 langkah` | Ditemukan pada iterasi pertama (`i = 0`). |
| **Data Tengah** | `P025` | `24` | `25 langkah` | Ditemukan pada iterasi ke-25 (`i = 24`). |
| **Batas Akhir** | `P050` | `49` | `50 langkah` | Ditemukan pada iterasi terakhir (`i = 49`). |
| **Tidak Ada** | `P999-TIDAK-ADA` | `-1` (tidak ada) | `50 langkah` | Menelusuri seluruh elemen hingga `i = 49` lalu berhenti. |

---

## 2. Trace Pergeseran Elemen `DynamicArray` saat `remove` dan `insert`/`add`
Misalkan kondisi awal berisi 5 elemen aktif (`size = 5`, `capacity = 10`):
* **State Awal:** `[ P001, P002, P003, P004, P005, null, null, null, null, null ]` -> `size = 5`, `validateInvariant() = true`.

### Langkah A: Hapus Elemen Pertama — `remove(0)` (`P001`)
* Elemen indeks `1..4` digeser satu posisi ke kiri (`elements[i] = elements[i + 1]`), lalu `elements[4]` diisi `null`.
* **State Hasil:** `[ P002, P003, P004, P005, null, null, null, null, null, null ]` -> `size = 4`, `validateInvariant() = true`.

### Langkah B: Hapus Elemen Tengah — `remove(size / 2)` -> `remove(2)` (`P004`)
* Elemen indeks `3` (`P005`) digeser ke indeks `2`, lalu `elements[3]` diisi `null`.
* **State Hasil:** `[ P002, P003, P005, null, null, null, null, null, null, null ]` -> `size = 3`, `validateInvariant() = true`.

### Langkah C: Hapus Elemen Terakhir — `remove(size - 1)` -> `remove(2)` (`P005`)
* Tidak ada pergeseran ke kiri; `elements[2]` langsung diisi `null`.
* **State Hasil:** `[ P002, P003, null, null, null, null, null, null, null, null ]` -> `size = 2`, `validateInvariant() = true`.

### Langkah D: Tambahkan Kembali 3 Record (`insert(0, P001)`, `add(P004)`, `add(P005)`)
1. `insert(0, P001)` menggeser `P002` dan `P003` ke kanan -> `[ P001, P002, P003, ... ]` (`size = 3`).
2. `add(P004)` menempatkan `P004` di indeks `3` -> `[ P001, P002, P003, P004, ... ]` (`size = 4`).
3. `add(P005)` menempatkan `P005` di indeks `4` -> `[ P001, P002, P003, P004, P005, null, ... ]` (`size = 5`, `validateInvariant() = true`).

---

## 3. Trace Pencarian Non-Destruktif (`searchPreserve`) pada `AuditStack`
* **Kondisi Awal Stack Utama (`top = 2`):**
  * Indeks `2` (TOP): `"FILTER data"`
  * Indeks `1`: `"CARI 'P001'"`
  * Indeks `0` (BOTTOM): `"IMPOR CSV"`
* **Target Pencarian:** `"IMPOR"`
  1. `pop()` `"FILTER data"` -> `push()` ke `tempStack` (posisi = 1, belum cocok).
  2. `pop()` `"CARI 'P001'"` -> `push()` ke `tempStack` (posisi = 2, belum cocok).
  3. `pop()` `"IMPOR CSV"` -> `push()` ke `tempStack` (posisi = 3, **COCOK** -> berhenti membongkar).
  4. **Proses `restore(tempStack)`:** Melakukan `pop()` dari `tempStack` secara berurutan (`"IMPOR CSV"`, lalu `"CARI 'P001'"`, lalu `"FILTER data"`) dan `push()` kembali ke Stack Utama.
* **Kondisi Akhir Stack Utama (`top = 2`):** Kembali menjadi `["IMPOR CSV", "CARI 'P001'", "FILTER data"]` dengan puncak tetap `"FILTER data"` dan `validateInvariant() = true`.