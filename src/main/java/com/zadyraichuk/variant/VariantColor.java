package com.zadyraichuk.variant;

import java.io.Serializable;
import java.util.Objects;

public class VariantColor
    implements Serializable {

    public static final VariantColor DEFAULT = new VariantColor("#F3F3F3");

    private static final long serialVersionUID = -8284197205035375012L;

    private final String hexColor;

    public VariantColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public VariantColor(int reg, int green, int blue) {
        this.hexColor = String.format("#%02x%02x%02x", reg, green, blue);
    }

    public String getHexColor() {
        return hexColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariantColor)) return false;
        VariantColor that = (VariantColor) o;
        return Objects.equals(hexColor, that.hexColor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hexColor);
    }
}
