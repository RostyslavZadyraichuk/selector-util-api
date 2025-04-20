package com.zadyraichuk.variant;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class VariantColor {

    public static final VariantColor DEFAULT = new VariantColor("#F3F3F3");

    @JsonProperty("hexColor")
    private final String hexColor;

    @JsonCreator
    public VariantColor(@JsonProperty("hexColor") String hexColor) {
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
