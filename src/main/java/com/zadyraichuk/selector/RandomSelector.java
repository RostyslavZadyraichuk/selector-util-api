package com.zadyraichuk.selector;

import com.zadyraichuk.variant.RationalVariant;
import com.zadyraichuk.variant.RationalVariantsList;
import com.zadyraichuk.variant.Variant;
import com.zadyraichuk.variant.VariantsList;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RandomSelector
    extends AbstractRandomSelector<String, Variant<String>>
    implements Serializable {

    public RandomSelector(String name) {
        super(name, new VariantsList<>());
    }

    public RandomSelector(String name, VariantsList<String> variants) {
        super(name, variants);
    }

    public static RationalRandomSelector of(RandomSelector selector) {
        RationalVariantsList<String> newList = new RationalVariantsList<>();
        for (Variant<String> variant : selector.getVariantsList()) {
            newList.add(RationalVariant.of(variant));
        }
        return new RationalRandomSelector(selector.getName(), newList);
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
