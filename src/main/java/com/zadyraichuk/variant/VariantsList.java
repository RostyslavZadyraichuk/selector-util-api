package com.zadyraichuk.variant;

import java.io.Serializable;
import java.util.List;

public class VariantsList<E>
    extends AbstractVariantsList<E, Variant<E>>
    implements Serializable {

    public VariantsList() {}

    public VariantsList(List<Variant<E>> variants) {
        super(variants);
    }

    @Override
    public void add(E value) {
        add(new Variant<>(value));
    }

    @Override
    public void add(E value, int variantWeight) {
        Variant<E> variant = new Variant<>(value);
        variant.setVariantWeight(variantWeight);
        add(variant);
    }

    @Override
    public void remove(E value) {
        remove(new Variant<>(value));
    }

    @Override
    public boolean contains(E value) {
        return variants.contains(new Variant<>(value));
    }
}
