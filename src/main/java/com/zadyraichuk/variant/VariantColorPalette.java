package com.zadyraichuk.variant;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Optional;
import java.util.Random;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class VariantColorPalette implements Cloneable {

    /**
     * Default colors count value for palette generation.
     */
    public static final int COLORS_COUNT = 4;

    public static final int MAX_COLORS_COUNT = Color.values().length;

    private static final Random RAND = new Random(System.currentTimeMillis());

    @JsonProperty("colors")
    private final VariantColor[] colors;

    @JsonProperty("nextColorIndex")
    private int nextColorIndex;

    private VariantColorPalette(int colorCount) {
        colors = new VariantColor[colorCount];
        nextColorIndex = 0;
    }

    @JsonCreator
    protected VariantColorPalette(
        @JsonProperty("colors") VariantColor[] colors,
        @JsonProperty("nextColorIndex") int nextColorIndex) {
        this.colors = colors;
        this.nextColorIndex = nextColorIndex;
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

    public Optional<Integer> indexInPalette(VariantColor color) {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i].equals(color))
                return Optional.of(i);
        }

        return Optional.empty();
    }

    public int getColorsCount() {
        return colors.length;
    }

    @Override
    public VariantColorPalette clone() {
        try {
            VariantColorPalette clone = (VariantColorPalette) super.clone();
            clone.resetColorIndex();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    protected enum Color {
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
