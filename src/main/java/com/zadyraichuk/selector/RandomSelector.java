package com.zadyraichuk.selector;

import com.zadyraichuk.variant.RationalVariant;
import com.zadyraichuk.variant.RationalVariantsList;
import com.zadyraichuk.variant.Variant;
import com.zadyraichuk.variant.VariantsList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RandomSelector extends AbstractRandomSelector<String, Variant<String>> {

    public RandomSelector(VariantsList<String> variants) {
        super(variants);
    }

    public static RationalRandomSelector of(RandomSelector selector) {
        RationalVariantsList<String> newList = new RationalVariantsList<>();
        for (Variant<String> variant : selector.getVariantsList()) {
            newList.add(RationalVariant.of(variant));
        }
        return new RationalRandomSelector(newList);
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
        return variantsList.get(nextRandomIndex());
    }

}
