package com.zadyraichuk;

import java.io.Serializable;
import java.util.Objects;

import static com.zadyraichuk.chooser.VariantsList.*;

public class Variant implements Serializable {

    public static final int DEFAULT_WEIGHT = 1;
    public static final int DIGITS = 3;

    private String value;
    private int percentWeight;
    private double currentPercent;
    private double minPercent;

    public Variant(Object value) {
        this.value = String.valueOf(value);
        this.percentWeight = DEFAULT_WEIGHT;
    }

    public Variant(Object value, int percentWeight) {
        this.value = String.valueOf(value);
        this.percentWeight = percentWeight;
    }

    protected Variant(Object value, int percentWeight, double minPercent, double currentPercent) {
        this.value = String.valueOf(value);
        this.percentWeight = percentWeight;
        this.minPercent = minPercent;
        this.currentPercent = currentPercent;
    }

    public static <S> Variant[] toVariants(S... values) {
        Variant[] variants = new Variant[values.length];

        for (int i = 0; i < values.length; i++) {
            variants[i] = new Variant(String.valueOf(values[i]));
        }

        return variants;
    }

    public static double singleWeightPercent(Variant variant) {
        return variant.currentPercent / variant.percentWeight;
    }

    public String getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = String.valueOf(value);
    }

    public int getPercentWeight() {
        return percentWeight;
    }

    public void setPercentWeight(int percentWeight) {
        this.percentWeight = percentWeight;
    }

    public double getPercentWeight(double minimumPercent) {
        return CutType.ROUND.cut(currentPercent / minimumPercent, 1);
    }

    public double getMinPercent() {
        return minPercent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variant variant = (Variant) o;
        return Objects.equals(value, variant.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    protected double getCurrentPercent() {
        return currentPercent;
    }

    protected void setCurrentPercent(double currentPercent) {
        this.currentPercent = CutType.ROUND.cut(currentPercent, DIGITS);
        double realMinPercent = this.currentPercent / Math.pow(2, MAX_TIMES_DIVIDING);
        this.minPercent = CutType.FLOOR.cut(realMinPercent, DIGITS);
    }

    protected void increasePercent(double value) {
        currentPercent += CutType.ROUND.cut(value, DIGITS);
    }

    protected double decreasePercent(double value) {
        currentPercent -= CutType.ROUND.cut(value, DIGITS);
        double diff = minPercent - currentPercent;

        if (diff < 0)
            return 0;

        currentPercent += diff;
        return diff;
    }

    protected enum CutType {
        ROUND {
            @Override
            public double cut(double value, int digits) {
                double cutter = CutType.getCutter(digits);
                return Math.round(value * cutter) / cutter;
            }
        },
        CEIL {
            @Override
            public double cut(double value, int digits) {
                double cutter = CutType.getCutter(digits);
                return Math.ceil(value * cutter) / cutter;
            }
        },
        FLOOR {
            @Override
            public double cut(double value, int digits) {
                double cutter = CutType.getCutter(digits);
                return Math.floor(value * cutter) / cutter;
            }
        };

        public abstract double cut(double value, int digits);

        private static double getCutter(int digits) {
            return Math.pow(10, digits);
        }
    }
}
