package com.zadyraichuk.selector;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.function.Supplier;

/**
 * <p>Collection that allow to select only one value in defined way</p>
 * @param <E> type of stored objects
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = RandomSelector.class, name = "RandomSelector"),
    @JsonSubTypes.Type(value = RationalRandomSelector.class, name = "RationalRandomSelector")
})
public interface Selector<E> {

    E select();

    E select(int degree);

    default E select(Supplier<E> supplier) {
        return supplier.get();
    }

}
