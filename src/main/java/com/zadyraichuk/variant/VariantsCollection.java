package com.zadyraichuk.variant;

import java.util.stream.Stream;

/**
 * Specified set of methods classes with Variant collections must implement
 * @param <E> type of elements stored in variants
 */
public interface VariantsCollection<E, V extends Variant<E>>
    extends Iterable<V> {

    static <E, V extends Variant<E>> double singleWeightPercent(VariantsCollection<E, V> variants,
                                                                double totalPercent) {
        return totalPercent / totalWeight(variants);
    }

    static <E, V extends Variant<E>> int totalWeight(VariantsCollection<E, V> variants) {
        int totalWeight = 0;

        for (V variant : variants) {
            totalWeight += variant.getVariantWeight();
        }

        return totalWeight;
    }

    static <E, V extends Variant<E>> double totalPercent(VariantsCollection<E, V> variants) {
        double totalPercent = 0;

        for (V variant : variants) {
            totalPercent += variant.getCurrentPercent();
        }

        return totalPercent;
    }

    static <E, V extends Variant<E>> double minimalPercent(VariantsCollection<E, V> variants) {
        if (variants.isEmpty())
            return 0;
        return variants.stream()
            .mapToDouble(Variant::getCurrentPercent)
            .min().orElse(0);
    }

    E getValue(int index);

    V get(int index);

    void add(V variant);

    void add(E value);

    void add(E value, int variantWeight);

    void swap(int firstIndex, int secondIndex);

    void remove(int index);

    void remove(E value);

    void remove(V variant);

    boolean contains(E value);

    boolean contains(V variant);

    boolean isEmpty();

    int size();

    Stream<V> stream();

    /**
     * Shuffle variants collection randomly
     */
    void shuffle();

    /**
     * @return array of double probabilities of each variant
     */
    double[] probabilities();

    /**
     * Min probability percent must be generated that variant will be selected.
     * Calculates as sum all previous variants percent.
     * @param variant element in collection that must be selected
     * @return left percent bound
     */
    double leftProbabilityBound(V variant);

    /**
     * Max probability percent must be generated that variant will be selected.
     * Calculates as sum all previous and current variants percent.
     * @param variant element in collection that must be selected
     * @return right percent bound
     */
    double rightProbabilityBound(V variant);

    void initVariantPercents();
}
