package com.zadyraichuk.general;

public final class MathUtils {

    /**
     * Cut double value to defined digits count after comma using Math.round
     *
     * @param value  double value needed cutting
     * @param digits integer value of digits count after comma
     * @return cut value
     */
    public static double cutRound(double value, int digits) {
        double cutter = MathUtils.getCutter(digits);
        return Math.round(value * cutter) / cutter;
    }

    /**
     * Cut double value to defined digits count after comma using Math.ceil
     *
     * @param value  double value needed cutting
     * @param digits integer value of digits count after comma
     * @return cut value
     */
    public static double cutCeil(double value, int digits) {
        double cutter = MathUtils.getCutter(digits);
        return Math.ceil(value * cutter) / cutter;
    }

    /**
     * Cut double value to defined digits count after comma using Math.floor
     *
     * @param value  double value needed cutting
     * @param digits integer value of digits count after comma
     * @return cut value
     */
    public static double cutFloor(double value, int digits) {
        double cutter = MathUtils.getCutter(digits);
        return Math.floor(value * cutter) / cutter;
    }

    private static double getCutter(int digits) {
        return Math.pow(10, digits);
    }

}
