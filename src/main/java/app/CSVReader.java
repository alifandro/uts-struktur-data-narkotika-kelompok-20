package app;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class CSVReader {
    private DynamicArray<Putusan> dataPutusan;

    public CSVReader() {
        this.dataPutusan = new DynamicArray<>();
    }

    public void readCSV(String filePath) {
        dataPutusan.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int nomorBaris = 0;

            while ((line = br.readLine()) != null) {
                nomorBaris++;
                // Lewati header (baris 1) dan baris kosong
                if (nomorBaris == 1 || line.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] kolom = parseCsvLine(line, 16);
                    if (kolom == null) {
                        System.out.println("[PERINGATAN] Baris " + nomorBaris + " ditolak: jumlah kolom bukan 16.");
                        continue;
                    }

                    String caseId = kolom[0].trim();
                    String nomorPerkara = kolom[1].trim();
                    String pengadilan = kolom[2].trim();
                    LocalDate tanggalPutusan = LocalDate.parse(kolom[3].trim());
                    String terdakwaKode = kolom[4].trim();
                    String jenisNarkotika = kolom[5].trim();
                    double beratGram = Double.parseDouble(kolom[6].trim());
                    String pasalDakwaan = kolom[7].trim();
                    String pasalTerbukti = kolom[8].trim();
                    int tuntutanBulan = Integer.parseInt(kolom[9].trim());
                    int vonisBulan = Integer.parseInt(kolom[10].trim());
                    long dendaRupiah = Long.parseLong(kolom[11].trim());
                    String barangBukti = kolom[12].trim();
                    String keadaanMemberatkan = kolom[13].trim();
                    String keadaanMeringankan = kolom[14].trim();
                    String sourcePages = kolom[15].trim();

                    if (caseId.isEmpty() || caseId.equalsIgnoreCase("null") || beratGram < 0 || vonisBulan < 0) {
                        System.out.println("[PERINGATAN] Baris " + nomorBaris + " ditolak: nilai data tidak valid.");
                        continue;
                    }

                    Putusan putusan = new Putusan(
                        caseId, nomorPerkara, pengadilan, tanggalPutusan, terdakwaKode,
                        jenisNarkotika, beratGram, pasalDakwaan, pasalTerbukti,
                        tuntutanBulan, vonisBulan, dendaRupiah, barangBukti,
                        keadaanMemberatkan, keadaanMeringankan, sourcePages
                    );

                    dataPutusan.add(putusan);

                } catch (Exception e) {
                    // Menolak record tidak valid tanpa menghentikan program
                    System.out.println("[ERROR] Baris " + nomorBaris + " gagal diproses (" + e.getMessage() + "). Dilewati.");
                }
            }
        } catch (IOException e) {
            System.out.println("[FATAL] Gagal membaca file CSV: " + e.getMessage());
        }
    }

    public DynamicArray<Putusan> getDataPutusan() {
        return dataPutusan;
    }

    // Memecah baris CSV tanpa memotong koma yang berada di dalam tanda kutip ganda ("...")
    private String[] parseCsvLine(String line, int expectedCols) {
        String[] result = new String[expectedCols];
        StringBuilder buffer = new StringBuilder();
        boolean inQuotes = false;
        int colIndex = 0;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                if (colIndex >= expectedCols) {
                    return null;
                }
                result[colIndex++] = buffer.toString();
                buffer.setLength(0);
            } else {
                buffer.append(c);
            }
        }

        if (colIndex != expectedCols - 1) {
            return null;
        }
        result[colIndex] = buffer.toString();
        return result;
    }
}