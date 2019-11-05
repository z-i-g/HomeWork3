package MyHashMap;

import com.sun.javafx.collections.MappingChange;
import com.sun.xml.internal.fastinfoset.util.CharArrayString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyHashMapTest {

    MyHashMap myHashMap = new MyHashMap();
    HashMap hashMap = new HashMap();
    HashMap putHashMap = new HashMap();

    int key = 7;
    int value = 8;

    @Test
    void isEmpty() {
        myHashMap.put(key, value);
        hashMap.put(key, value);
        assertFalse(myHashMap.isEmpty());
        assertFalse(hashMap.isEmpty());

        myHashMap.clear();
        hashMap.clear();
        assertTrue(myHashMap.isEmpty());
        assertTrue(hashMap.isEmpty());
    }

    @Test
    void containsValue() {
        myHashMap.put(key, value);
        hashMap.put(key, value);

        assertTrue(myHashMap.containsValue(value) && hashMap.containsValue(value));
        assertFalse(myHashMap.containsValue(1) && hashMap.containsValue(1));
    }

    @Test
    void putAll() {
        putHashMap.put(key, value);
        myHashMap.putAll(putHashMap);
        hashMap.putAll(putHashMap);

        assertEquals(myHashMap.get(key), hashMap.get(key));
        assertEquals(myHashMap.size(), hashMap.size());
    }

    @Test
    void clear() {
        myHashMap.put(key, value);
        hashMap.put(key, value);

        myHashMap.clear();
        hashMap.clear();
        assertTrue(myHashMap.isEmpty());
        assertTrue(hashMap.isEmpty());
    }

    @Test
    void keySet() {
        for(int i = 0; i < 20; i++) {
            myHashMap.put(i, i);
            hashMap.put(i, i);
        }

        Set keySet = hashMap.keySet();
        Set myKeySet = myHashMap.keySet();

        assertEquals(keySet, myKeySet);
    }

    @Test
    void values() {
        for(int i = 0; i < 20; i++) {
            myHashMap.put(i, i);
            hashMap.put(i, i);
        }

        Collection collHashMap = hashMap.values();
        Collection collMyHashMap = myHashMap.values();

        assertEquals(collHashMap.toString(), collMyHashMap.toString());
    }

    @Test
    void entrySet() {
        for(int i = 0; i < 20; i++) {
            myHashMap.put(i, i);
            hashMap.put(i, i);
        }
        Set mySet = myHashMap.entrySet();
        Set set = hashMap.entrySet();

        assertEquals(set, mySet);
    }

    @Test
    void containsKey() {
        myHashMap.put(key, value);
        hashMap.put(key, value);

        assertTrue(myHashMap.containsKey(key) && hashMap.containsKey(key));
        assertFalse(myHashMap.containsKey(1) && hashMap.containsKey(1));
    }

    @Test
    void remove() {
        for(int i = 0; i < 20; i++) {
            myHashMap.put(i, i);
            hashMap.put(i, i);
            hashMap.remove(i);
            myHashMap.remove(i);
            assertEquals(myHashMap.size(), hashMap.size());
        }

        assertEquals(myHashMap.remove(key), hashMap.remove(key));
    }

    @Test
    void put() {
        for(int i = 0; i < 20; i++) {
            myHashMap.put(i, i);
            hashMap.put(i, i);
            assertEquals(myHashMap.get(i), hashMap.get(i));
        }

        assertEquals(hashMap.size(), myHashMap.size());
    }

    @Test
    void get() {
        for(int i = 0; i < 20; i++) {
            myHashMap.put(i, i);
            hashMap.put(i, i);
            assertEquals(myHashMap.get(i), hashMap.get(i));
        }
    }

    @Test
    void size() {
        for(int i = 0; i < 20; i++) {
            myHashMap.put(i, i);
            hashMap.put(i, i);
        }

        assertEquals(hashMap.size(), myHashMap.size());
    }
}