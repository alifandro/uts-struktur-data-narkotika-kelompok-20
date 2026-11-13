package app;

public class AuditStack<T> implements StrukturData<T> {
    private Object[] elements;
    private int top;
    private static final int DEFAULT_CAPACITY = 20;

    public AuditStack() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.top = -1;
    }

    private void resize(int newCapacity) {
        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i <= top; i++) {
            newArray[i] = elements[i];
        }
        elements = newArray;
    }

    public void push(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Elemen stack tidak boleh null");
        }
        if (top + 1 == elements.length) {
            resize(elements.length * 2);
        }
        elements[++top] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack kosong, tidak dapat melakukan pop");
        }
        T value = (T) elements[top];
        elements[top] = null;
        top--;
        return value;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return (T) elements[top];
    }

    // Pencarian non-destruktif: membongkar ke stack sementara lalu mengembalikannya (restore)
    // Mengembalikan jarak dari puncak stack (1 = paling atas), atau -1 jika tidak ditemukan
    public int searchPreserve(T target) {
        if (isEmpty() || target == null) return -1;

        AuditStack<T> tempStack = new AuditStack<>();
        int positionFromTop = 1;
        int foundPosition = -1;

        while (!this.isEmpty()) {
            T current = this.pop();
            tempStack.push(current);
            if (current.equals(target) || current.toString().contains(target.toString())) {
                foundPosition = positionFromTop;
                break;
            }
            positionFromTop++;
        }

        // Restore seluruh elemen kembali ke stack utama agar urutan LIFO tetap utuh
        restore(tempStack);
        return foundPosition;
    }

    public void restore(AuditStack<T> tempStack) {
        while (!tempStack.isEmpty()) {
            this.push(tempStack.pop());
        }
    }

    @Override
    public void add(T value) {
        push(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int position) {
        if (position < 0 || position > top) {
            throw new IndexOutOfBoundsException("Posisi stack tidak valid: " + position);
        }
        return (T) elements[position];
    }

    @Override
    public T remove(int position) {
        if (position != top) {
            throw new UnsupportedOperationException("Stack hanya mendukung penghapusan elemen puncak (top)");
        }
        return pop();
    }

    @Override
    public int size() {
        return top + 1;
    }

    @Override
    public boolean isEmpty() {
        return top == -1;
    }

    @Override
    public boolean contains(T value) {
        return searchPreserve(value) != -1;
    }

    @Override
    public void clear() {
        for (int i = 0; i <= top; i++) {
            elements[i] = null;
        }
        top = -1;
    }

    public boolean validateInvariant() {
        if (elements == null || top < -1 || top >= elements.length) {
            return false;
        }
        for (int i = 0; i <= top; i++) {
            if (elements[i] == null) return false;
        }
        for (int i = top + 1; i < elements.length; i++) {
            if (elements[i] != null) return false;
        }
        return true;
    }
}