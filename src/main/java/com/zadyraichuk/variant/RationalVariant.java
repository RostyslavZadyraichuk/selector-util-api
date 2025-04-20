package com.zadyraichuk.variant;

import com.zadyraichuk.general.MathUtils;
import java.io.Serializable;

public class RationalVariant<E> extends Variant<E>
    implements Serializable {

    /**
     * Default value for {@link #minPercent} precision.
     */
    public static final int DIGITS_FOR_MINIMAL = 3;

    /**
     * Default value for {@link #minPercent} definition algorithm.
     */
    public static final int POW_FOR_MINIMAL = 3;

    /**
     * <p>Edge of minimal percent value, when rational algorithm is used.</p>
     * Defines when {@link #currentPercent} is calculated in method {@link #setCurrentPercent}.
     * For calculation used default power value of 2 - {@link #DIGITS_FOR_MINIMAL}
     */
    protected double minPercent;

    public RationalVariant(E value) {
        super(value);
    }

    public RationalVariant(E value,
                           int variantWeight,
                           double currentPercent,
                           VariantColor color) {
        super(value);
        this.setVariantWeight(variantWeight);
        this.setCurrentPercent(currentPercent);
        this.setColor(color);
    }

    public static <E> RationalVariant<E> of(Variant<E> variant) {
        return new RationalVariant<>(variant.value, variant.variantWeight,
            variant.currentPercent, variant.color);
    }

    public double getMinPercent() {
        return minPercent;
    }

    @Override
    public void setCurrentPercent(double currentPercent) {
        super.setCurrentPercent(currentPercent);
        double realMinPercent = this.currentPercent / Math.pow(2, RationalVariant.POW_FOR_MINIMAL);
        this.minPercent = MathUtils.cutFloor(realMinPercent, RationalVariant.DIGITS_FOR_MINIMAL);
    }

    /**
     * Increase current variant's percent
     */
    public void increasePercent(double value) {
        currentPercent += MathUtils.cutRound(value, Variant.DIGITS);
    }

    /**
     * Decrease current variant's percent
     * @return value that cannot be decreased because currentPercent is equal to minPercent
     */
    public double decreasePercent(double value) {
        currentPercent -= MathUtils.cutRound(value, Variant.DIGITS);
        double diff = minPercent - currentPercent;

        if (diff < 0)
            return 0;

        currentPercent += diff;
        return diff;
    }

}
