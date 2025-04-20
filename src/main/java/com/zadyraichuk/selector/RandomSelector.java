package com.zadyraichuk.selector;

import com.zadyraichuk.variant.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RandomSelector
    extends AbstractRandomSelector<String, Variant<String>>
    implements Serializable {

    private static final long serialVersionUID = 9097060254207232564L;

    public RandomSelector(String name) {
        super(name, new VariantsList<>());
    }

    public RandomSelector(String name, VariantsList<String> variants) {
        super(name, variants);
    }

    public static RandomSelector of(AbstractRandomSelector<String, ? extends Variant<String>> selector) {
        VariantsList<String> newList = new VariantsList<>();
        VariantsCollection<String, ? extends Variant<String>> oldList = selector.getVariantsList();
        newList.setPalette(((AbstractVariantsList<?, ?>) oldList).getPalette());
        for (Variant<String> variant : oldList) {
            newList.add(Variant.of(variant, VariantsCollection.totalWeight(oldList)));
        }
        return new RandomSelector(selector.getName(), newList);
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
