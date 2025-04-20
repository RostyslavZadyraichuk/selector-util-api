package com.zadyraichuk.selector;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zadyraichuk.variant.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class RandomSelector
    extends AbstractRandomSelector<String, Variant<String>> {

    public RandomSelector(String name) {
        super(name, new VariantsList<>());
    }

    public RandomSelector(String name, VariantsList<String> variants) {
        super(name, variants);
    }

    @JsonCreator
    protected RandomSelector(
        @JsonProperty("id") long id,
        @JsonProperty("variantsList") VariantsCollection<String, Variant<String>> variantsList,
        @JsonProperty("name") String name,
        @JsonProperty("currentRotation") int currentRotation) {
        super(id, variantsList, name, currentRotation);
    }

    public static RandomSelector of(AbstractRandomSelector<String, ? extends Variant<String>> selector) {
        VariantsList<String> newList = new VariantsList<>();
        VariantsCollection<String, ? extends Variant<String>> oldList = selector.getVariantsList();
        VariantColorPalette palette = ((AbstractVariantsList<?, ?>) oldList).getPalette();
        palette.resetColorIndex();
        newList.setPalette(palette);
        for (Variant<String> variant : oldList) {
            newList.add(Variant.of(variant, VariantsCollection.totalWeight(oldList)));
        }

        RandomSelector newSelector = new RandomSelector(selector.getId(),
            newList,
            selector.getName(),
            selector.getCurrentRotation());
        newSelector.setCurrentRotation(selector.getCurrentRotation());
        return newSelector;

    }

    @Override
    public void setVariants(List<?> values) {
        List<Variant<String>> list = values.stream()
            .map(e -> new Variant<>(e.toString()))
            .collect(Collectors.toList());
        variantsList = new VariantsList<>(list);
    }

    @Override
    public void setVariants(Object... values) {
        List<Variant<String>> variants = Arrays.stream(values)
            .map(Object::toString)
            .map(Variant::new)
            .collect(Collectors.toList());
        variantsList = new VariantsList<>(variants);
    }

    @Override
    public Variant<String> select() {
        return variantsList.get(super.nextRandomIndex());
    }

    @Override
    public Variant<String> select(int degree) {
        return variantsList.get(super.nextIndexByDegree(degree));
    }
}
