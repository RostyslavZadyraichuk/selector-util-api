package com.zadyraichuk.variant;

import java.util.Random;

/**
 * Set of possible wheel's colors for Selector application
 */
public enum VariantColor {
    DEFAULT("#F3F3F3"),
    BLUE("#0089BA"),
    YELLOW("#FFC75F"),
    VIOLET("#845EC2"),
    ORANGE("#FF9671"),
    PURPLE("#D65DB1"),
    PINK("#FF6F91"),
    LAWANDA("#B39CD0"),
    GREEN("#008F7A"),
    OLIVE("#909741"),
    RED("#C34A36"),
    CORAL("#FF8066"),
    LEMON("#F9F871");

    static final Random RAND = new Random(System.currentTimeMillis());

    /**
     * Default colors count value for palette generation.
     */
    static final int COLORS_COUNT = 4;

    private final String hexColor;

    VariantColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public static VariantColor[] generateOrderedPalette() {
        return generateOrderedPalette(COLORS_COUNT);
    }

    /**
     * Generates palette which consists of ordered colors except {@link #DEFAULT}.
     * @param colorsCount palette size
     * @return {@link VariantColor} array
     */
    public static VariantColor[] generateOrderedPalette(int colorsCount) {
        if (colorsCount <= 0)
            return new VariantColor[0];
        else if (colorsCount > VariantColor.values().length)
            colorsCount = VariantColor.values().length;

        int startPos = RAND.nextInt(values().length - 1) + 1;
        VariantColor[] palette = new VariantColor[colorsCount];
        for (int i = startPos; i < colorsCount; i++) {
            palette[i] = VariantColor.values()[i++];

            if (i > VariantColor.values().length - 1)
                i = 1;
        }
        return palette;
    }

    public static VariantColor[] generateRandomPalette() {
        return generateRandomPalette(COLORS_COUNT);
    }

    public static VariantColor[] generateRandomPalette(int colorsCount) {
        if (colorsCount <= 0)
            return new VariantColor[0];
        else if (colorsCount > VariantColor.values().length)
            colorsCount = VariantColor.values().length;

        VariantColor[] palette = new VariantColor[colorsCount];
        for (int i = 0; i < palette.length; i++) {
            int colorPos = RAND.nextInt(values().length);
            palette[i] = VariantColor.values()[colorPos];
        }
        return palette;
    }

    public String getHexColor() {
        return hexColor;
    }
}
