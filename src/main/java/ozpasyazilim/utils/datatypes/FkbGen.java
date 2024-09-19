package ozpasyazilim.utils.datatypes;

import java.util.Map;

/**
 * @param <E> Optional FiKeyBean ile ilgili meta data içeren class
 */
public class FkbGen<E> extends FiKeyBean {

    public FkbGen() {
    }

    public FkbGen(Map<? extends String, ?> m) {
        super(m);
    }

    public FkbGen(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public FkbGen(int initialCapacity) {
        super(initialCapacity);
    }

    public FkbGen(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

}
