package MyHashMap;

import java.util.*;
/**
 * Класс реализации собственной HashMap
 * @autor Ayrat Zagidullin
 * @version 2.0
 */
public class MyHashMap<K, V>  implements java.util.Map<K, V>{

    private int defaultCapacity;
    private Entry<K, V>[] table;
    private int count = 0;
    private float loadFactor = 0.75f;

    /**
     * Внутренний класс, для хранения пар ключ-значение
     */
    public MyHashMap() {
        defaultCapacity = 16;
        table = new Entry[defaultCapacity];
    }

    public class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;
        int hash;
        Entry<K, V> next;

        public Entry(int hash, K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return null;
        }
    }

    /**
     * Проверка, что коллекция пуста
     * @return true - если коллекция пуста
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Наличие переданного значения
     * @return true - если переданное значение есть в коллекции
     */
    @Override
    public boolean containsValue(Object value) {
        Entry<K, V> e;
        for(int i = 0; i < table.length; i++) {
            e = table[i];
            while(e != null) {
                if(((e.value) == value) || (value != null && value.equals(e.value))) {
                    return true;
                }
                e = e.next;
            }
        }
        return false;
    }

    /**
     * Добавление элементов новой Map в коллекцию
     * @param m - передаваемая Map
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry <? extends K, ? extends V> e: m.entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            putEntry(key, value);
        }
    }

    /**
     * Очистка коллекции
     */
    @Override
    public void clear() {
        if(size() != 0) {
            count = 0;
            for(int i = 0; i < table.length; i++) {
                table[i] = null;
            }
        }


    }

    /**
     * Получение множества Set ключей из коллекции
     * @return keys - Set ключей
     */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<K>();
        Entry<K, V> e;
        for(int i = 0; i < table.length; i++) {
            e = table[i];
            if(size() != 0) {
                while(e != null) {
                    keys.add(e.key);
                    e = e.next;
                }
            }
        }
        return keys;
    }

    /**
     * Получение Collection значений из коллекции
     * @return values - Collection значений
     */
    @Override
    public Collection values() {
        Set val = new HashSet();
        for(int i = 0; i < table.length; i++) {
            for(Entry entry = table[i]; entry != null; entry = entry.next) {
                val.add(entry.value);
            }
        }
        return val;
    }

    /**
     * Получение множества Set ключей и значений
     * @return entrySet - множества Set ключей и значений
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
       Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        Entry<K, V> e;
        for(int i = 0; i < table.length; i++) {
            e = table[i];
            if(size() != 0) {
                while(e != null) {
                    entrySet.add(e);
                    e = e.next;
                }
            }
        }
        return entrySet;
    }

    /**
     * Проверка наличия переданног ключа
     * @param key - ключ
     * @return true если переданный ключ есть в коллекции
     */
    @Override
    public boolean containsKey(Object key) {
        Entry<K, V> e = getEntry(hash((K) key), (K) key);
        return e != null;
    }

    /**
     * Удаление элемента по ключу
     * @param key - ключ
     * @return true если удаение успешно
     */
    @Override
    public V remove(Object key) {
        int hash = hash((K)key);
        int index = indexFor(hash, defaultCapacity);
        Entry<K, V> e = table[indexFor(hash, defaultCapacity)];
        if(e != null) {
            return removeEntry(e, (K)key, index).value;
        } else
            return null;
    }

    // true если удаление успешно
    private Entry<K, V> removeEntry(Entry<K, V> e, K key, int index) {
        Entry<K, V> prev = e;
        Entry<K, V> current = e;
        Entry<K, V> tempEntry;
        while(!( (key.equals((current.key))))) {
            if (current.next == null) { //если элемент не найден
                return null;
            } else {
                prev = current;
                current = current.next;
            }
        }
        if(e == current) {
            tempEntry = e;
            e = e.next;
            table[index] = e;
            count--;
            return tempEntry;
        } else {
            prev.next = current.next;
            count--;
        }
        return e;
    }

    /**
     * Добавление элемента в коллекцию, если key одинаковые, то value перезаписывается
     * @param key - ключ
     * @param value - значение
     * @exception IllegalArgumentException - если ключ null
     */
    @Override
    public Object put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Ключ не может быть null!");
        }
        return putEntry(key, value);
    }

    private Object putEntry(Object key, Object value) {
        if((defaultCapacity * loadFactor) < count) {
            transfer(resize());
        }

        int hash = hash((K)(key));
        int tableIndex = indexFor(hash, defaultCapacity);

        if(table[tableIndex] != null) {
            for(Entry entry = table[tableIndex]; entry != null; entry = entry.next) {
                if(entry.hash == hash && (entry.key == key || key.equals(entry.key))) {
                    entry.value = value;
                } else {
                    addEntry(hash, (K)key, (V)value, tableIndex, true);
                }
            }
        } else {
            addEntry(hash, (K)key, (V)value, tableIndex, true);
        }
        return key;
    }

    /**
     * Увеличение размера коллекции при loadFactor > 0.75
     * @return Entry[] - массив с увеличенным размером
     */
    private Entry<K, V>[] resize () {
        defaultCapacity *= 2;
        return new Entry[defaultCapacity];
    }

    /**
     * Копирует элементы в newTable[]
     * @param newTable - новый массив
     */
    private void transfer(Entry<K, V>[] newTable) {
        Entry[] tempTable = table;
        table = newTable;
        Entry entry;

        for(int i = 0; i < tempTable.length; i++ ) {
            if(tempTable[i] != null) {
                int tableIndex = indexFor(tempTable[i].hash, defaultCapacity);
                entry = tempTable[i];
                if(entry.next == null) {
                    table[tableIndex] = tempTable[i];
                } else {
                    for(Entry entryFor = entry; entry != null; entry = entry.next) {
                        int newIndex = indexFor(entryFor.hash, defaultCapacity);
                        addEntry(entryFor.hash, (K)entryFor.key
                                , (V)entryFor.value, newIndex, false);
                    }
                }
            }
        }
    }

    /**
     * Получение value ко key, если value не найден, то null
     * @param key - ключ
     * @return value
     */
    @Override
    public V get(Object key) {
        int hash = hash((K)key);
        Entry<K, V> e;
        return (e = getEntry(hash, (K)key)) == null ? null : e.value;
    }


    private Entry<K, V> getEntry(int hash, K key) {
        int index = indexFor(hash, defaultCapacity);
        for (Entry entry = table[index]; entry != null ; entry = entry.next) {
            if(entry.hash == hash && (entry.key == key || key.equals(entry.key))) {
                return entry;
            }
        }
        return null;
    }

    private void addEntry(int hash, K key, V value, int index, boolean flag) {
        Entry<K, V> e = table[index];
        table[index] = new Entry<K, V>(hash, key, value, e);
        if(flag) {
            count++;
        }
    }

    private int indexFor(int hash, int tableLenght) {
        return hash & (tableLenght - 1);
    }

    private int hash(K key) {
        int hash = key.hashCode();
        return 31 * hash + 17;
    }

    /**
     * Количество элементов в коллекции
     * @return count - количество
     */
    @Override
    public int size() {
        return count;
    }

    public static void main(String[] args) {

    }

}
