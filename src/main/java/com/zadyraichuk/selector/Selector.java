package com.zadyraichuk.selector;

import java.util.function.Supplier;

/**
 * <p>Collection that allow to select only one value in defined way</p>
 * @param <E> type of stored objects
 */
public interface Selector<E> {

    E select();

    E select(int degree);

    default E select(Supplier<E> supplier) {
        return supplier.get();
    }

}
