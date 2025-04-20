package com.zadyraichuk.variant;

import java.io.Serializable;
import java.util.List;

public class VariantsList<E>
    extends AbstractVariantsList<E, Variant<E>>
    implements Serializable {

    private static final long serialVersionUID = -1417506786550866887L;

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

    @Override
    public void normalizeToOne() {
        int minWeight = VariantsCollection.minimalWeight(this);
        double minDigit = Math.pow(10, Variant.DIGITS * (-1));

        double total = VariantsCollection.totalPercent(this);
        if (total > 1.0) {
            while (total > 1.0) {
                variants.forEach(e -> {
                    double weightMultiplier = (double) e.getVariantWeight() / minWeight;
                    e.decreasePercent(minDigit * weightMultiplier);
                });
                total = VariantsCollection.totalPercent(this);
            }
        }
    }
}
