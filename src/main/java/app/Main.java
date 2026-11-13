package app;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PutusanService service = new PutusanService();
        Scanner scanner = new Scanner(System.in);

        // Otomatis memuat data awal saat aplikasi dibuka
        service.imporDariCSV("data/putusan-50.csv");

        boolean berjalan = true;
        while (berjalan) {
            System.out.println("\n==========================================");
            System.out.println("  APLIKASI DATA PUTUSAN NARKOTIKA (UTS)");
            System.out.println("  Total Record: " + service.getDaftarPutusan().size()
                    + " | Invariant: " + (service.getDaftarPutusan().validateInvariant() ? "VALID" : "RUSAK"));
            System.out.println("==========================================");
            System.out.println("1. Impor Ulang CSV (data/putusan-50.csv)");
            System.out.println("2. Tampilkan Semua Putusan");
            System.out.println("3. Tambah Putusan Baru");
            System.out.println("4. Hapus Putusan (berdasarkan Case ID)");
            System.out.println("5. Cari Putusan (Case ID / Nomor Perkara)");
            System.out.println("6. Filter Putusan (Zat, Berat, Pasal, Vonis)");
            System.out.println("7. Tampilkan Laporan Agregasi");
            System.out.println("8. Urutkan Putusan berdasarkan Vonis");
            System.out.println("9. Lihat Riwayat Operasi (Audit Stack)");
            System.out.println("10. Ekspor Data & Laporan ke File");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1":
                    service.imporDariCSV("data/putusan-50.csv");
                    System.out.println("Selesai memuat. Total data: " + service.getDaftarPutusan().size());
                    break;

                case "2":
                    service.tampilkanSemua();
                    break;

                case "3":
                    try {
                        System.out.print("Case ID (misal P051): ");
                        String caseId = scanner.nextLine().trim();
                        System.out.print("Nomor Perkara: ");
                        String noPerkara = scanner.nextLine().trim();
                        System.out.print("Pengadilan Negeri: ");
                        String pengadilan = scanner.nextLine().trim();
                        System.out.print("Tanggal Putusan (YYYY-MM-DD): ");
                        LocalDate tgl = LocalDate.parse(scanner.nextLine().trim());
                        System.out.print("Kode Anonim Terdakwa (misal TRD-51): ");
                        String terdakwa = scanner.nextLine().trim();
                        System.out.print("Jenis Narkotika: ");
                        String zat = scanner.nextLine().trim();
                        System.out.print("Berat Barang Bukti (gram): ");
                        double berat = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Pasal Dakwaan: ");
                        String dakwaan = scanner.nextLine().trim();
                        System.out.print("Pasal Terbukti: ");
                        String terbukti = scanner.nextLine().trim();
                        System.out.print("Tuntutan (bulan): ");
                        int tuntutan = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Vonis (bulan): ");
                        int vonis = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Denda (rupiah): ");
                        long denda = Long.parseLong(scanner.nextLine().trim());

                        Putusan baru = new Putusan(caseId, noPerkara, pengadilan, tgl, terdakwa,
                                zat, berat, dakwaan, terbukti, tuntutan, vonis, denda,
                                "-", "-", "-", "1");
                        service.tambahPutusan(baru);
                        System.out.println("Putusan berhasil ditambahkan.");
                    } catch (Exception e) {
                        System.out.println("[ERROR] Input tidak valid: " + e.getMessage());
                    }
                    break;

                case "4":
                    System.out.print("Masukkan Case ID yang akan dihapus: ");
                    String hapusId = scanner.nextLine().trim();
                    Putusan dihapus = service.hapusPutusanByCaseId(hapusId);
                    if (dihapus != null) {
                        System.out.println("Berhasil menghapus: " + dihapus);
                    } else {
                        System.out.println("Case ID tidak ditemukan.");
                    }
                    break;

                case "5":
                    System.out.print("Masukkan Case ID atau Nomor Perkara: ");
                    String keyword = scanner.nextLine().trim();
                    Putusan ditemukan = service.cariPutusan(keyword);
                    if (ditemukan != null) {
                        System.out.println("Detail: " + ditemukan);
                    }
                    break;

                case "6":
                    System.out.println("--- Filter Putusan (Tekan Enter untuk melewati kriteria) ---");
                    System.out.print("Jenis Narkotika: ");
                    String fZat = scanner.nextLine().trim();
                    System.out.print("Berat Minimal (gram): ");
                    String sMinBerat = scanner.nextLine().trim();
                    System.out.print("Berat Maksimal (gram): ");
                    String sMaxBerat = scanner.nextLine().trim();
                    System.out.print("Pasal Terbukti: ");
                    String fPasal = scanner.nextLine().trim();
                    System.out.print("Vonis Minimal (bulan): ");
                    String sMinVonis = scanner.nextLine().trim();
                    System.out.print("Vonis Maksimal (bulan): ");
                    String sMaxVonis = scanner.nextLine().trim();

                    Double minBerat = sMinBerat.isEmpty() ? null : Double.parseDouble(sMinBerat);
                    Double maxBerat = sMaxBerat.isEmpty() ? null : Double.parseDouble(sMaxBerat);
                    Integer minVonis = sMinVonis.isEmpty() ? null : Integer.parseInt(sMinVonis);
                    Integer maxVonis = sMaxVonis.isEmpty() ? null : Integer.parseInt(sMaxVonis);

                    DynamicArray<Putusan> hasilFilter = service.filterGabungan(
                            fZat, minBerat, maxBerat, fPasal, minVonis, maxVonis);

                    System.out.println("Hasil Filter (" + hasilFilter.size() + " record):");
                    for (int i = 0; i < hasilFilter.size(); i++) {
                        System.out.println((i + 1) + ". " + hasilFilter.get(i));
                    }
                    break;

                case "7":
                    System.out.println(service.buatLaporanAgregasi());
                    break;

                case "8":
                    System.out.print("Urutkan dari terendah ke tertinggi? (y/n): ");
                    boolean asc = scanner.nextLine().trim().equalsIgnoreCase("y");
                    DynamicArray<Putusan> terurut = service.urutkanBerdasarkanVonis(asc);
                    System.out.println("=== HASIL PENGURUTAN VONIS ===");
                    for (int i = 0; i < terurut.size(); i++) {
                        System.out.println((i + 1) + ". " + terurut.get(i));
                    }
                    break;

                case "9":
                    service.tampilkanRiwayatAudit();
                    break;

                case "10":
                    service.eksporKeCSV("docs/demo/hasil-ekspor.csv", service.getDaftarPutusan());
                    service.eksporLaporanTeks("docs/demo/laporan-agregasi.txt", service.buatLaporanAgregasi());
                    break;

                case "0":
                    berjalan = false;
                    System.out.println("Program selesai.");
                    break;

                default:
                    System.out.println("Pilihan tidak dikenali.");
            }
        }
        scanner.close();
    }
}