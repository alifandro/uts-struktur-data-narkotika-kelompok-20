package app;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PutusanService {
    private DynamicArray<Putusan> daftarPutusan;
    private AuditStack<String> riwayatAudit;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PutusanService() {
        this.daftarPutusan = new DynamicArray<>();
        this.riwayatAudit = new AuditStack<>();
    }

    private void catatAudit(String aksi) {
        String timestamp = LocalDateTime.now().format(TIME_FMT);
        riwayatAudit.push("[" + timestamp + "] " + aksi);
    }

    public DynamicArray<Putusan> getDaftarPutusan() {
        return daftarPutusan;
    }

    public AuditStack<String> getRiwayatAudit() {
        return riwayatAudit;
    }

    // 1. FITUR IMPOR
    public void imporDariCSV(String filePath) {
        CSVReader reader = new CSVReader();
        reader.readCSV(filePath);
        DynamicArray<Putusan> hasilBaca = reader.getDataPutusan();

        daftarPutusan.clear();
        for (int i = 0; i < hasilBaca.size(); i++) {
            daftarPutusan.add(hasilBaca.get(i));
        }
        catatAudit("IMPOR CSV dari '" + filePath + "' (" + daftarPutusan.size() + " record valid)");
    }

    // 2. FITUR TAMPIL
    public void tampilkanSemua() {
        if (daftarPutusan.isEmpty()) {
            System.out.println("Belum ada data putusan yang dimuat.");
            return;
        }
        System.out.println("=== DAFTAR PUTUSAN (" + daftarPutusan.size() + " RECORD) ===");
        for (int i = 0; i < daftarPutusan.size(); i++) {
            System.out.println((i + 1) + ". " + daftarPutusan.get(i));
        }
        catatAudit("TAMPIL semua putusan (" + daftarPutusan.size() + " record)");
    }

    // 3. FITUR TAMBAH & HAPUS (Operasi asli struktur data)
    public void tambahPutusan(Putusan p) {
        daftarPutusan.add(p);
        catatAudit("TAMBAH putusan " + p.getCaseId() + " (" + p.getNomorPerkara() + ")");
    }

    public void sisipkanPutusan(int index, Putusan p) {
        daftarPutusan.insert(index, p);
        catatAudit("SISIP putusan " + p.getCaseId() + " pada indeks " + index);
    }

    public Putusan hapusPutusanByIndex(int index) {
        Putusan terhapus = daftarPutusan.remove(index);
        catatAudit("HAPUS putusan indeks " + index + " (" + terhapus.getCaseId() + ")");
        return terhapus;
    }

    public Putusan hapusPutusanByCaseId(String caseId) {
        for (int i = 0; i < daftarPutusan.size(); i++) {
            if (daftarPutusan.get(i).getCaseId().equalsIgnoreCase(caseId)) {
                return hapusPutusanByIndex(i);
            }
        }
        catatAudit("HAPUS GAGAL untuk caseId: " + caseId + " (tidak ditemukan)");
        return null;
    }

    // 4. FITUR CARI (Melaporkan jumlah langkah)
    public Putusan cariPutusan(String keyword) {
        int langkah = 0;
        for (int i = 0; i < daftarPutusan.size(); i++) {
            langkah++;
            Putusan p = daftarPutusan.get(i);
            if (p.getCaseId().equalsIgnoreCase(keyword) || p.getNomorPerkara().equalsIgnoreCase(keyword)) {
                System.out.println("[CARI] Ditemukan pada indeks [" + i + "] dalam " + langkah + " langkah.");
                catatAudit("CARI '" + keyword + "' -> DITEMUKAN (" + langkah + " langkah)");
                return p;
            }
        }
        System.out.println("[CARI] Data '" + keyword + "' tidak ditemukan setelah " + langkah + " langkah.");
        catatAudit("CARI '" + keyword + "' -> TIDAK DITEMUKAN (" + langkah + " langkah)");
        return null;
    }

    // 5. FITUR FILTER (Tunggal & Gabungan)
    public DynamicArray<Putusan> filterGabungan(String jenisNarkotika, Double minBerat, Double maxBerat,
                                                String pasalTerbukti, Integer minVonis, Integer maxVonis) {
        DynamicArray<Putusan> hasil = new DynamicArray<>();

        for (int i = 0; i < daftarPutusan.size(); i++) {
            Putusan p = daftarPutusan.get(i);
            boolean cocok = true;

            if (jenisNarkotika != null && !jenisNarkotika.isEmpty()) {
                if (!p.getJenisNarkotika().toLowerCase().contains(jenisNarkotika.toLowerCase())) {
                    cocok = false;
                }
            }
            if (cocok && minBerat != null && p.getBeratBarangBuktiGram() < minBerat) {
                cocok = false;
            }
            if (cocok && maxBerat != null && p.getBeratBarangBuktiGram() > maxBerat) {
                cocok = false;
            }
            if (cocok && pasalTerbukti != null && !pasalTerbukti.isEmpty()) {
                if (!p.getPasalTerbukti().toLowerCase().contains(pasalTerbukti.toLowerCase())) {
                    cocok = false;
                }
            }
            if (cocok && minVonis != null && p.getVonisBulan() < minVonis) {
                cocok = false;
            }
            if (cocok && maxVonis != null && p.getVonisBulan() > maxVonis) {
                cocok = false;
            }

            if (cocok) {
                hasil.add(p);
            }
        }

        catatAudit("FILTER data -> menghasilkan " + hasil.size() + " record");
        return hasil;
    }

    // 6. FITUR AGREGASI (Tanpa HashMap / koleksi bawaan)
    public String buatLaporanAgregasi() {
        int totalPerkara = daftarPutusan.size();
        if (totalPerkara == 0) {
            return "Data kosong, tidak dapat menghitung agregasi.";
        }

        long totalVonis = 0;
        long totalDenda = 0;

        // Penampung manual distribusi jenis narkotika menggunakan array paralel
        String[] daftarZat = new String[totalPerkara];
        int[] jumlahZat = new int[totalPerkara];
        int jumlahJenisUnik = 0;

        for (int i = 0; i < totalPerkara; i++) {
            Putusan p = daftarPutusan.get(i);
            totalVonis += p.getVonisBulan();
            totalDenda += p.getDendaRupiah();

            String zat = p.getJenisNarkotika().trim();
            int indeksDitemukan = -1;
            for (int j = 0; j < jumlahJenisUnik; j++) {
                if (daftarZat[j].equalsIgnoreCase(zat)) {
                    indeksDitemukan = j;
                    break;
                }
            }

            if (indeksDitemukan != -1) {
                jumlahZat[indeksDitemukan]++;
            } else {
                daftarZat[jumlahJenisUnik] = zat;
                jumlahZat[jumlahJenisUnik] = 1;
                jumlahJenisUnik++;
            }
        }

        double rataRataVonis = (double) totalVonis / totalPerkara;

        StringBuilder sb = new StringBuilder();
        sb.append("=== LAPORAN AGREGASI PUTUSAN NARKOTIKA ===\n");
        sb.append("Jumlah Perkara   : ").append(totalPerkara).append(" perkara\n");
        sb.append(String.format("Rata-rata Vonis  : %.2f bulan\n", rataRataVonis));
        sb.append("Total Denda      : Rp").append(totalDenda).append("\n");
        sb.append("Distribusi Jenis Narkotika:\n");
        for (int i = 0; i < jumlahJenisUnik; i++) {
            sb.append("  - ").append(daftarZat[i]).append(": ").append(jumlahZat[i]).append(" perkara\n");
        }

        catatAudit("AGREGASI statistik putusan");
        return sb.toString();
    }

    // 7. FITUR URUTAN (Mengembalikan salinan terurut berdasarkan vonis tanpa merusak invariant struktur utama)
    public DynamicArray<Putusan> urutkanBerdasarkanVonis(boolean ascending) {
        DynamicArray<Putusan> salinan = new DynamicArray<>(daftarPutusan.size() > 0 ? daftarPutusan.size() : 10);
        for (int i = 0; i < daftarPutusan.size(); i++) {
            salinan.add(daftarPutusan.get(i));
        }

        // Bubble Sort pada salinan DynamicArray
        int n = salinan.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Putusan a = salinan.get(j);
                Putusan b = salinan.get(j + 1);
                boolean perluTukar = ascending
                        ? (a.getVonisBulan() > b.getVonisBulan())
                        : (a.getVonisBulan() < b.getVonisBulan());

                if (perluTukar) {
                    salinan.set(j, b);
                    salinan.set(j + 1, a);
                }
            }
        }

        catatAudit("URUTAN berdasarkan vonis (" + (ascending ? "Ascending" : "Descending") + ")");
        return salinan;
    }

    // 8. FITUR AUDIT (Menampilkan riwayat dari struktur sekunder Stack)
    public void tampilkanRiwayatAudit() {
        System.out.println("=== RIWAYAT OPERASI PENGGUNA (LIFO - TERBARU DI ATAS) ===");
        if (riwayatAudit.isEmpty()) {
            System.out.println("Belum ada aktivitas tercatat.");
            return;
        }
        for (int i = riwayatAudit.size() - 1; i >= 0; i--) {
            System.out.println((riwayatAudit.size() - i) + ". " + riwayatAudit.get(i));
        }
    }

    // 9. FITUR EKSPOR (Ke CSV atau TXT)
    public void eksporKeCSV(String outputPath, DynamicArray<Putusan> dataUntukDiekspor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            pw.println("caseId,nomorPerkara,pengadilan,tanggalPutusan,terdakwaKode,jenisNarkotika,beratBarangBuktiGram,pasalTerbukti,vonisBulan,dendaRupiah");
            for (int i = 0; i < dataUntukDiekspor.size(); i++) {
                Putusan p = dataUntukDiekspor.get(i);
                pw.printf("%s,%s,%s,%s,%s,%s,%.3f,%s,%d,%d%n",
                        p.getCaseId(), p.getNomorPerkara(), p.getPengadilan(), p.getTanggalPutusan(),
                        p.getTerdakwaKode(), p.getJenisNarkotika(), p.getBeratBarangBuktiGram(),
                        p.getPasalTerbukti(), p.getVonisBulan(), p.getDendaRupiah());
            }
            System.out.println("[EKSPOR] Berhasil menyimpan " + dataUntukDiekspor.size() + " record ke " + outputPath);
            catatAudit("EKSPOR CSV ke '" + outputPath + "' (" + dataUntukDiekspor.size() + " record)");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal mengekspor CSV: " + e.getMessage());
        }
    }

    public void eksporLaporanTeks(String outputPath, String isiLaporan) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            pw.println(isiLaporan);
            System.out.println("[EKSPOR] Laporan teks berhasil disimpan ke " + outputPath);
            catatAudit("EKSPOR TXT laporan ke '" + outputPath + "'");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal mengekspor laporan teks: " + e.getMessage());
        }
    }
}