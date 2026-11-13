package app;

public class DynamicArray<T> implements StrukturData<T> {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public DynamicArray() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Kapasitas awal harus lebih dari 0");
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    // Mengubah kapasitas array internal saat penuh atau menyusut
    private void resize(int newCapacity) {
        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = elements[i];
        }
        elements = newArray;
    }

    @Override
    public void add(T value) {
        if (size == elements.length) {
            resize(elements.length * 2);
        }
        elements[size] = value;
        size++;
    }

    // Menyisipkan elemen pada indeks tertentu dan menggeser elemen setelahnya ke kanan
    public void insert(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Indeks sisip tidak valid: " + index);
        }
        if (size == elements.length) {
            resize(elements.length * 2);
        }
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = value;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Indeks di luar batas: " + position);
        }
        return (T) elements[position];
    }

    // Mengganti elemen pada posisi tertentu
    @SuppressWarnings("unchecked")
    public T set(int position, T value) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Indeks di luar batas: " + position);
        }
        T oldValue = (T) elements[position];
        elements[position] = value;
        return oldValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T remove(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Indeks hapus tidak valid: " + position);
        }
        T removedElement = (T) elements[position];

        // Geser elemen setelah posisi yang dihapus ke kiri
        for (int i = position; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null; // Hapus referensi sisa agar tidak memory leak
        size--;

        // Perkecil kapasitas jika penggunaan <= 25% namun tetap di atas DEFAULT_CAPACITY
        if (size > 0 && size <= elements.length / 4 && elements.length / 2 >= DEFAULT_CAPACITY) {
            resize(elements.length / 2);
        }

        return removedElement;
    }

    // Mencari indeks elemen (mengembalikan -1 jika tidak ditemukan)
    public int search(T value) {
        for (int i = 0; i < size; i++) {
            if (value == null) {
                if (elements[i] == null) return i;
            } else if (value.equals(elements[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(T value) {
        return search(value) != -1;
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return elements.length;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Memastikan aturan invariant struktur data tetap terjaga
    public boolean validateInvariant() {
        if (elements == null || size < 0 || elements.length < size) {
            return false;
        }
        // Pastikan tidak ada slot kosong di tengah data aktif (0 sampai size-1)
        for (int i = 0; i < size; i++) {
            if (elements[i] == null) {
                return false;
            }
        }
        // Pastikan slot di luar size (size sampai length-1) bersih (null)
        for (int i = size; i < elements.length; i++) {
            if (elements[i] != null) {
                return false;
            }
        }
        return true;
    }
}