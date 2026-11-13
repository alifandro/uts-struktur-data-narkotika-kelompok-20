package app;

public class Main {
    public static void main(String[] args) {
        CSVReader reader = new CSVReader();

        // Membaca dataset utama sesuai lokasi folder data/putusan-50.csv
        reader.readCSV("data/putusan-50.csv");

        DynamicArray<Putusan> data = reader.getDataPutusan();

        System.out.println("=== HASIL IMPOR DATA PUTUSAN ===");
        System.out.println("Total record valid : " + data.size());
        System.out.println("Status Invariant   : " + (data.validateInvariant() ? "VALID" : "TIDAK VALID"));
        System.out.println("--------------------------------");

        for (int i = 0; i < data.size(); i++) {
            System.out.println((i + 1) + ". " + data.get(i));
        }
    }
}