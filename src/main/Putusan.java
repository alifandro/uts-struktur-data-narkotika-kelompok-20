
public class Putusan {

    private String caseId;

    private DynamicArray<Atribut> atribut;

    public Putusan(String caseId) {

        this.caseId = caseId;
        this.atribut = new DynamicArray<>();
    }

    // Mendapatkan case ID
    public String getCaseId() {
        return caseId;
    }

    // Menambahkan atribut
    public void addAtribut(Atribut a) {
        atribut.add(a);
    }

    // Mendapatkan jumlah atribut
    public int getJumlahAtribut() {
        return atribut.size();
    }

    // Mengambil atribut berdasarkan index
    public Atribut getAtribut(int index) {
        return atribut.get(index);
    }

    // Mencari atribut berdasarkan nama
    public Atribut searchAtribut(String fieldName) {

        for (int i = 0; i < atribut.size(); i++) {

            Atribut a = atribut.get(i);

            if (a.getFieldName().equals(fieldName)) {
                return a;
            }
        }

        return null;
    }
}
