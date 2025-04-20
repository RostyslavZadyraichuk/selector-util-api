package com.zadyraichuk.variant;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class VariantsList<E>
    extends AbstractVariantsList<E, Variant<E>> {

    public VariantsList() {
    }

    public VariantsList(List<Variant<E>> variants) {
        super(variants);
    }

    @JsonCreator
    protected VariantsList(
        @JsonProperty("variants") List<Variant<E>> variants,
        @JsonProperty("declaredTotalWeight") int declaredTotalWeight,
        @JsonProperty("palette") VariantColorPalette palette) {
        super(variants, declaredTotalWeight, palette);
    }

    @Override
    public void add(E value) {
        add(new Variant<>(value));
    }

    @Override
    public void add(E value, int variantWeight) {
        Variant<E> variant = new Variant<>(value);
        variant.setVariantWeight(variantWeight);
        add(variant);
    }

    @Override
    public void remove(E value) {
        remove(new Variant<>(value));
    }

    @Override
    public boolean contains(E value) {
        return variants.contains(new Variant<>(value));
    }

    @Override
    public void normalizeToOne() {
        int minWeight = VariantsCollection.minimalWeight(this);
        double minDigit = Math.pow(10, Variant.DIGITS * (-1));

        double total = VariantsCollection.totalPercent(this);
        if (total > 1.0) {
            while (total > 1.0) {
                variants.forEach(e -> {
                    double weightMultiplier = (double) e.getVariantWeight() / minWeight;
                    e.decreasePercent(minDigit * weightMultiplier);
                });
                total = VariantsCollection.totalPercent(this);
            }
        }
    }
}
