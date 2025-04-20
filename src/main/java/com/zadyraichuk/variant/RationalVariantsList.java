package com.zadyraichuk.variant;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

public class RationalVariantsList<E>
    extends AbstractVariantsList<E, RationalVariant<E>>
    implements Serializable {

    public RationalVariantsList() {
        super();
    }

    public RationalVariantsList(List<RationalVariant<E>> variants) {
        super(variants);
    }

    @Override
    public RationalVariant<E> get(int index) {
        return super.get(index);
    }

    @Override
    public void add(RationalVariant<E> variant) {
        super.add(variant);

    }

    @Override
    public void remove(RationalVariant<E> variant) {
        super.remove(variant);
    }

    @Override
    public Stream<RationalVariant<E>> stream() {
        return super.stream();
    }

    @Override
    public void add(E value) {
        super.add(new RationalVariant<>(value));
        isChanged = true;
    }

    @Override
    public void add(E value, int variantWeight) {
        RationalVariant<E> variant = new RationalVariant<>(value);
        variant.setVariantWeight(variantWeight);
        super.add(variant);
        isChanged = true;
    }

    @Override
    public void remove(E value) {
        super.remove(new RationalVariant<>(value));
        isChanged = true;
    }

    @Override
    public boolean contains(E value) {
        return variants.contains(new RationalVariant<>(value));
    }
}
