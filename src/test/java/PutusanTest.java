import app.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;

public class PutusanTest {
    public static void main(String[] args) throws Exception {
        System.out.println("=== MENJALANKAN 7 SKENARIO PENGUJIAN WAJIB UTS ===\n");

        PutusanService service = new PutusanService();

        // SKENARIO 1: Muat dataset CSV dan buktikan size & invariant
        service.imporDariCSV("data/putusan-50.csv");
        DynamicArray<Putusan> data = service.getDaftarPutusan();
        assertCondition(data.size() > 0, "Skenario 1: Data berhasil dimuat (size = " + data.size() + ", target akhir = 50)");
        assertCondition(data.validateInvariant(), "Skenario 1: Invariant struktur utama VALID setelah impor");

        // Tambahkan dummy sementara jika dataset belum genap 5 agar skenario hapus awal-tengah-akhir bisa diuji
        while (data.size() < 5) {
            int idx = data.size() + 1;
            service.tambahPutusan(new Putusan("P00" + idx, idx + "/Pid.Sus/2024/PN.Test",
                    "PN Test", LocalDate.of(2024, 1, idx), "TRD-0" + idx,
                    "Sabu", 1.5 * idx, "Pasal 112", "Pasal 112", 48, 36 + idx, 800000000L,
                    "Sabu", "-", "-", "1"));
        }

        // SKENARIO 2: Cari perkara yang ada, tidak ada, dan posisi batas (pertama & terakhir)
        Putusan cariPertama = service.cariPutusan(data.get(0).getCaseId());
        Putusan cariTerakhir = service.cariPutusan(data.get(data.size() - 1).getCaseId());
        Putusan cariTidakAda = service.cariPutusan("P999-TIDAK-ADA");
        assertCondition(cariPertama != null && cariTerakhir != null && cariTidakAda == null,
                "Skenario 2: Pencarian batas awal, batas akhir, dan data tidak ada berjalan akurat");

        // SKENARIO 3: Hapus elemen pertama, tengah, dan terakhir
        int sizeSebelumHapus = data.size();
        Putusan hapusAwal = service.hapusPutusanByIndex(0);
        Putusan hapusTengah = service.hapusPutusanByIndex(data.size() / 2);
        Putusan hapusAkhir = service.hapusPutusanByIndex(data.size() - 1);
        assertCondition(data.size() == sizeSebelumHapus - 3 && data.validateInvariant(),
                "Skenario 3: Hapus awal (" + hapusAwal.getCaseId() + "), tengah (" + hapusTengah.getCaseId()
                        + "), akhir (" + hapusAkhir.getCaseId() + ") menjaga pergeseran elemen & invariant");

        // SKENARIO 4: Tambahkan kembali 3 record dan buktikan invariant serta urutan data
        service.sisipkanPutusan(0, hapusAwal);
        service.tambahPutusan(hapusTengah);
        service.tambahPutusan(hapusAkhir);
        assertCondition(data.size() == sizeSebelumHapus && data.validateInvariant(),
                "Skenario 4: Penambahan kembali 3 record mempertahankan size (" + data.size() + ") & invariant");

        // SKENARIO 5: Jalankan filter gabungan untuk dicocokkan dengan data-provenance.csv
        DynamicArray<Putusan> hasilFilter = service.filterGabungan("Ganja", 1.0, 10.0, "114", 48, 72);
        assertCondition(hasilFilter.size() >= 1,
                "Skenario 5: Filter gabungan menemukan " + hasilFilter.size() + " perkara (siap dicocokkan dengan docs/data-provenance.csv)");

        // SKENARIO 6: Pencarian pada struktur sekunder (AuditStack) tidak mengubah urutan LIFO
        AuditStack<String> stack = service.getRiwayatAudit();
        String topSebelum = stack.peek();
        int ukuranStackSebelum = stack.size();
        int posisiCari = stack.searchPreserve("IMPOR");
        assertCondition(posisiCari != -1 && stack.peek().equals(topSebelum) && stack.size() == ukuranStackSebelum && stack.validateInvariant(),
                "Skenario 6: searchPreserve pada AuditStack menemukan item di posisi " + posisiCari + " tanpa mengubah urutan puncak");

        // SKENARIO 7: Masukkan record CSV tidak valid dan pastikan program tidak crash & tetap konsisten
        String tempInvalidCsv = "data/temp-invalid-test.csv";
        try (PrintWriter pw = new PrintWriter(new FileWriter(tempInvalidCsv))) {
            pw.println("header1,header2");
            pw.println("BARIS_RUSAK_KOLOM_KURANG,123");
            pw.println("P099,NoPerkara,PN,FORMAT_TANGGAL_SALAH,TRD,Sabu,BUKAN_ANGKA,P112,P112,10,10,1000,BB,M1,M2,1");
        }
        CSVReader testReader = new CSVReader();
        testReader.readCSV(tempInvalidCsv);
        new java.io.File(tempInvalidCsv).delete(); // Bersihkan file sementara
        assertCondition(testReader.getDataPutusan().size() == 0 && testReader.getDataPutusan().validateInvariant(),
                "Skenario 7: Record rusak ditolak tanpa menghentikan program & invariant tetap terjaga");

        System.out.println("\n=== SELURUH 7 SKENARIO UJI BERHASIL LOLOS ===");
    }

    private static void assertCondition(boolean condition, String message) {
        if (condition) {
            System.out.println("[PASS] " + message);
        } else {
            throw new AssertionError("[FAIL] " + message);
        }
    }
}