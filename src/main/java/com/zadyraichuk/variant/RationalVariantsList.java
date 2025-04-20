package com.zadyraichuk.variant;

import com.zadyraichuk.general.MathUtils;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class RationalVariantsList<E> extends AbstractVariantsList<E, RationalVariant<E>> {

    /**
     * Collection stage for lazy normalization during obtain elements operation
     */
    protected boolean isChanged;

    public RationalVariantsList() {
        isChanged = true;
    }

    public RationalVariantsList(List<RationalVariant<E>> variants) {
        super(variants);
        isChanged = true;
    }

    @Override
    public E getValue(int index) {
        normalize();
        return super.getValue(index);
    }

    @Override
    public RationalVariant<E> get(int index) {
        normalize();
        return super.get(index);
    }

    @Override
    public void add(RationalVariant<E> variant) {
        super.add(variant);
        isChanged = true;
    }

    @Override
    public void remove(int index) {
        super.remove(index);
        isChanged = true;
    }

    @Override
    public void remove(RationalVariant<E> variant) {
        super.remove(variant);
        isChanged = true;
    }

    @Override
    public Stream<RationalVariant<E>> stream() {
        normalize();
        return super.stream();
    }

    @Override
    public double[] probabilities() {
        normalize();
        return super.probabilities();
    }

    @Override
    public Iterator<RationalVariant<E>> iterator() {
        normalize();
        return super.iterator();
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

    public void normalize() {
        if (isChanged) {
            int totalWeight = VariantsCollection.getTotalWeight(this);
            if (totalWeight != 0) {
                double onePercentNormalized =
                        MathUtils.cutRound(1.0 / totalWeight, Variant.DIGITS);

                for (Variant<E> variant : variants) {
                    double normalized = variant.getVariantWeight() * onePercentNormalized;
                    variant.setCurrentPercent(normalized);
                }
            }

            approxToOneAfterNormalize();
            isChanged = false;
        }
    }

    public void approxToOneAfterNormalize() {
        double minDigit = Math.pow(10, RationalVariant.DIGITS_FOR_MINIMAL * (-1));
        double edge = variants.stream()
                .mapToDouble(e -> minDigit * e.getVariantWeight()).sum();

        double total = VariantsCollection.getTotalPercent(this);
        if (total > 1.0) {
            while (total > 1.0) {
                variants.stream()
                        .map(e -> (RationalVariant<E>) e)
                        .forEach(e -> e.decreasePercent(minDigit * e.getVariantWeight()));
                total = VariantsCollection.getTotalPercent(this);
            }
        } else if (total < 1.0 - edge) {
            while (total < 1.0 - edge) {
                variants.stream()
                        .map(e -> (RationalVariant<E>) e)
                        .forEach(e -> e.increasePercent(minDigit * e.getVariantWeight()));
                total = VariantsCollection.getTotalPercent(this);
            }
        }
    }
}
