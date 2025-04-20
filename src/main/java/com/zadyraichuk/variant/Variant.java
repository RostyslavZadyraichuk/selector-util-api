package com.zadyraichuk.variant;

import com.zadyraichuk.general.MathUtils;
import java.io.Serializable;
import java.util.Objects;

/**
 * A class that represents unit (variant) of possible variants in collection
 * and have its own chance in that collection from 0 to 1 both included.
 * The possibility of each variant depends on variant's quantity in collection
 * and their abstract weight value.
 */
public class Variant<E>
    implements Serializable {

    /**
     * Default weight value is the less natural number,
     * because we can have at least one count, piece, etc. of something.
     */
    public static final int WEIGHT = 1;

    /**
     * Default value of precision is 3 digits after comma.
     */
    public static final int DIGITS = 3;

    protected E value;

    /**
     * <p>Abstract weight that current variant has in appropriate collection.</p>
     * It's natural number from 1 to {@link Integer#MAX_VALUE}
     */
    protected int variantWeight;

    /**
     * <p>Percent value that variant in appropriate collection has.</p>
     * Calculates automatically by its own {@link #variantWeight} when is in collection.
     * Is in range from 0 to 1 both included.
     */
    protected double currentPercent;

    protected VariantColor color;

    private static final long serialVersionUID = -377519670316023299L;

    public Variant(E value) {
        this.value = value;
        this.variantWeight = Variant.WEIGHT;
        this.color = VariantColor.DEFAULT;
    }

    public Variant(E value,
                   int variantWeight,
                   double currentPercent,
                   VariantColor color) {
        this.value = value;
        this.variantWeight = variantWeight;
        this.currentPercent = currentPercent;
        this.color = color;
    }

    public static <E> Variant<E> of(Variant<E> variant, int totalWeight) {
        double initPercent = (double) variant.getVariantWeight() / totalWeight;
        return new Variant<>(variant.value, variant.variantWeight, initPercent, variant.color);
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public double getCurrentPercent() {
        return currentPercent;
    }

    public int getVariantWeight() {
        return variantWeight;
    }

    public void setVariantWeight(int variantWeight) {
        this.variantWeight = variantWeight;
    }

    /**
     * Calculates current variant's weight accordingly to defined minimum percent
     */
    //TODO test
    public double getPercentWeight(double minimumPercent) {
        return MathUtils.cutRound(currentPercent / minimumPercent, 1);
    }

    public VariantColor getColor() {
        return color;
    }

    public void setColor(VariantColor color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variant<?> variant = (Variant<?>) o;
        return Objects.equals(value, variant.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    protected void setCurrentPercent(double currentPercent) {
        this.currentPercent = MathUtils.cutRound(currentPercent, Variant.DIGITS);
    }

    /**
     * Converts raw values objects to array with Variants that contains these objects
     */
    public static class ValueConverter {
        /**
         * Map one type objects to Variants array. Use randomly generated palette
         * with {@link VariantColor} default colors count
         * @param values objects for mapping
         * @param <E> objects type
         * @return Variants array with object type elements
         */
        @SafeVarargs
        public static <E> Variant<E>[] toVariants(E... values) {
            VariantColorPalette palette = VariantColorPalette.generateOrderedPalette();
            return ValueConverter.toVariants(palette, values);
        }

        /**
         * Map one type objects to Variants array
         * @param colorsCount how many colors include in randomly generated palette
         * @param values objects for mapping
         * @param <E> objects type
         * @return Variants array with object type elements
         */
        @SafeVarargs
        public static <E> Variant<E>[] toVariants(int colorsCount, E... values) {
            VariantColorPalette palette = VariantColorPalette.generateOrderedPalette(colorsCount);
            return ValueConverter.toVariants(palette, values);
        }

        /**
         * Map one type objects to Variants array
         * @param palette set of colors for each variant. If palette size is less than
         *                values size, than colors from palette used in loop
         * @param values objects for mapping
         * @param <E> objects type
         * @return Variants array with object type elements
         */
        @SafeVarargs
        public static <E> Variant<E>[] toVariants(VariantColorPalette palette, E... values) {
            Variant<E>[] variants = new Variant[values.length];

            for (int i = 0; i < values.length; i++) {
                variants[i] = new Variant<>(values[i]);
                variants[i].setColor(palette.nextColor());
            }

            return variants;
        }
    }
}
