
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

    // Array dinamis untuk menyimpan putusan
    private DynamicArray<Putusan> dataPutusan;

    public CSVReader() {
        dataPutusan = new DynamicArray<>();
    }

    // Mencari putusan berdasarkan case ID
    private Putusan searchCase(String caseId) {

        for (int i = 0; i < dataPutusan.size(); i++) {

            Putusan p = dataPutusan.get(i);

            if (p.getCaseId().equals(caseId)) {
                return p;
            }
        }

        return null;
    }

    // Konversi tipe data-putusan.csv
    private Object convertValue(
            String fieldName,
            String value
    ) {

        switch (fieldName) {

            case "vonisBulan":
            case "tuntutanBulan":
                return Integer.parseInt(value);

            case "beratBarangBuktiGram":
                return Double.parseDouble(value);

            case "dendaRupiah":
                return Long.parseLong(value);

            default:
                return value;
        }
    }

    // Membaca file CSV
    public void readCSV(String filePath) {

        try (
                BufferedReader br =
                        new BufferedReader(new FileReader(filePath))
        ) {

            String line;

            // Lewati header CSV
            br.readLine();

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] row = line.split(",", -1);

                if (row.length != 8) {
                    System.out.println(
                            "Format CSV tidak valid: " + line
                    );
                    continue;
                }

                // Mengambil data-putusan.csv CSV
                String caseId = row[0].trim();
                String fieldName = row[1].trim();
                String value = row[2].trim();

                String sourceFile = row[3].trim();
                int sourcePage =
                        Integer.parseInt(row[4].trim());

                String extractor = row[5].trim();
                String reviewer = row[6].trim();
                String status = row[7].trim();

                // Mencari putusan
                Putusan p = searchCase(caseId);

                // Jika belum ada, buat putusan baru
                if (p == null) {

                    p = new Putusan(caseId);

                    dataPutusan.add(p);
                }

                // Konversi nilai
                Object convertedValue =
                        convertValue(fieldName, value);

                // Membuat objek atribut
                Atribut a = new Atribut(
                        fieldName,
                        convertedValue,
                        sourceFile,
                        sourcePage,
                        extractor,
                        reviewer,
                        status
                );

                // Memasukkan atribut ke putusan
                p.addAtribut(a);
            }

            System.out.println("CSV berhasil dibaca!");

        } catch (IOException e) {

            System.out.println(
                    "Gagal membaca file: " + e.getMessage()
            );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Kesalahan konversi angka: " + e.getMessage()
            );
        }
    }

    // Mendapatkan seluruh data-putusan.csv putusan
    public DynamicArray<Putusan> getDataPutusan() {
        return dataPutusan;
    }

    // Mendapatkan jumlah putusan
    public int getJumlahPutusan() {
        return dataPutusan.size();
    }
}
