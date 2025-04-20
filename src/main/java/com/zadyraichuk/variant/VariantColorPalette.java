package com.zadyraichuk.variant;

import java.io.Serializable;
import java.util.Random;

public class VariantColorPalette implements Serializable {

    /**
     * Default colors count value for palette generation.
     */
    public static final int COLORS_COUNT = 4;

    private static final Random RAND = new Random(System.currentTimeMillis());

    private static final long serialVersionUID = -1293535583279688045L;

    private final VariantColor[] colors;

    private int nextColorIndex;

    private VariantColorPalette(int colorCount) {
        colors = new VariantColor[colorCount];
        nextColorIndex = 0;
    }

    public static VariantColorPalette generateOrderedPalette() {
        return generateOrderedPalette(COLORS_COUNT);
    }

    public static VariantColorPalette generateOrderedPalette(int colorsCount) {
        if (colorsCount <= 0)
            return new VariantColorPalette(0);
        else if (colorsCount > Color.values().length)
            colorsCount = Color.values().length;

        int startPos = RAND.nextInt(Color.values().length);
        VariantColorPalette palette = new VariantColorPalette(colorsCount);

        int counter = 0;
        while (counter < colorsCount) {
            palette.colors[counter++] = Color.values()[startPos++].color;

            if (startPos > Color.values().length - 1)
                startPos = 0;
        }
        return palette;
    }

    public static VariantColorPalette generateRandomPalette() {
        return generateRandomPalette(COLORS_COUNT);
    }

    public static VariantColorPalette generateRandomPalette(int colorsCount) {
        if (colorsCount <= 0)
            return new VariantColorPalette(0);
        else if (colorsCount > Color.values().length)
            colorsCount = Color.values().length;

        VariantColorPalette palette = new VariantColorPalette(colorsCount);
        for (int i = 0; i < palette.colors.length; i++) {
            int colorPos = RAND.nextInt(Color.values().length);
            palette.colors[i] = Color.values()[colorPos].color;
        }
        return palette;
    }

    public VariantColor nextColor() {
        VariantColor color = colors[nextColorIndex];
        nextColorIndex++;
        if (nextColorIndex >= colors.length)
            nextColorIndex = 0;

        return color;
    }

    public void resetColorIndex() {
        setColorIndex(0);
    }

    public void setColorIndex(int index) {
        nextColorIndex = index;
    }

    public int indexInPalette(VariantColor color) {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i].equals(color))
                return i;
        }

        //TODO optional
        return 0;
    }

    protected enum Color implements Serializable {
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

        public final VariantColor color;

        Color(String color) {
            this.color = new VariantColor(color);
        }
    }

}
