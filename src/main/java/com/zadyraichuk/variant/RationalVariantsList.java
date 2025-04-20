package com.zadyraichuk.variant;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

public class RationalVariantsList<E>
    extends AbstractVariantsList<E, RationalVariant<E>>
    implements Serializable {

    private static final long serialVersionUID = -8583087989581867701L;

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

    @Override
    public void initVariantPercents() {
        super.initVariantPercents();
        normalizeToOne();
    }

    public void normalizeToOne() {
        double minDigit = Math.pow(10, RationalVariant.DIGITS_FOR_MINIMAL * (-1));
        double edge = variants.stream()
            .mapToDouble(e -> minDigit * e.getVariantWeight()).sum();

        double total = VariantsCollection.totalPercent(this);
        if (total > 1.0) {
            while (total > 1.0) {
                variants.forEach(e -> e.decreasePercent(minDigit * e.getVariantWeight()));
                total = VariantsCollection.totalPercent(this);
            }
        } else if (total < 1.0 - edge) {
            while (total < 1.0 - edge) {
                variants.forEach(e -> e.increasePercent(minDigit * e.getVariantWeight()));
                total = VariantsCollection.totalPercent(this);
            }
        }
    }

}