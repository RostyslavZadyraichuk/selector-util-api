package com.zadyraichuk.variant;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zadyraichuk.general.MathUtils;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class RationalVariant<E> extends Variant<E> {

    /**
     * Default value for {@link #minPercent} precision.
     */
    public static final int DIGITS_FOR_MINIMAL = 5;

    /**
     * Default value for {@link #minPercent} definition algorithm.
     */
    public static final int POW_FOR_MINIMAL = 3;

    /**
     * <p>Edge of minimal percent value, when rational algorithm is used.</p>
     * Defines when {@link #currentPercent} is calculated in method {@link #setCurrentPercent}.
     * For calculation used default power value of 2 - {@link #DIGITS_FOR_MINIMAL}
     */
    @JsonProperty("minPercent")
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

    @JsonCreator
    protected RationalVariant(
        @JsonProperty("value") E value,
        @JsonProperty("variantWeight") int variantWeight,
        @JsonProperty("currentPercent") double currentPercent,
        @JsonProperty("color") VariantColor color,
        @JsonProperty("minPercent") double minPercent) {
        super(value, variantWeight, currentPercent, color);
        this.minPercent = minPercent;
    }

    public static <E> RationalVariant<E> of(Variant<E> variant) {
        return new RationalVariant<>(variant.value,
            variant.variantWeight,
            variant.currentPercent,
            variant.color);
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

    @Override
    public double decreasePercent(double value) {
        super.decreasePercent(value);
        double diff = minPercent - currentPercent;

        if (diff < 0)
            return 0;

        currentPercent += diff;
        return diff;
    }

}
