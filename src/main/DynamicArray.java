
public class DynamicArray<T> {

    private Object[] data;
    private int size;
    private int capacity;

    public DynamicArray() {
        capacity = 2;
        size = 0;
        data = new Object[capacity];
    }

    // Memperbesar kapasitas array
    private void resize() {

        capacity = capacity * 2;

        Object[] newData = new Object[capacity];

        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }

        data = newData;
    }

    // Menambahkan elemen
    public void add(T value) {

        if (size == capacity) {
            resize();
        }

        data[size] = value;
        size++;
    }

    // Mengambil elemen
    @SuppressWarnings("unchecked")
    public T get(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index tidak valid"
            );
        }

        return (T) data[index];
    }

    // Mengambil jumlah elemen
    public int size() {
        return size;
    }

    // Mengecek apakah array kosong
    public boolean isEmpty() {
        return size == 0;
    }
}
