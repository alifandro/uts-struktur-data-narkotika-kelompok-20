
public class Main {

    public static void main(String[] args) {

        // Membuat objek CSVReader
        CSVReader reader = new CSVReader();

        // Membaca file CSV
        reader.readCSV("./data/data-putusan.csv");

        // Mengambil seluruh data-putusan.csv putusan
        DynamicArray<Putusan> data =
                reader.getDataPutusan();

        // Menampilkan hasil
        System.out.println("\n===== DATA PUTUSAN =====");

        for (int i = 0; i < data.size(); i++) {

            Putusan p = data.get(i);

            System.out.println("\nCase ID: " + p.getCaseId());

            for (int j = 0; j < p.getJumlahAtribut(); j++) {

                Atribut a = p.getAtribut(j);

                System.out.println("----------------------");

                System.out.println(
                        "Field: " + a.getFieldName()
                );

                System.out.println(
                        "Value: " + a.getValue()
                );

                System.out.println(
                        "Source File: " + a.getSourceFile()
                );

                System.out.println(
                        "Source Page: " + a.getSourcePage()
                );

                System.out.println(
                        "Extractor: " + a.getExtractor()
                );

                System.out.println(
                        "Reviewer: " + a.getReviewer()
                );

                System.out.println(
                        "Status: " + a.getStatus()
                );
            }
        }

        System.out.println("\n=======================");
        System.out.println(
                "Total putusan: " + reader.getJumlahPutusan()
        );
    }
}
