package com.zadyraichuk.selector;

import com.zadyraichuk.general.MathUtils;
import com.zadyraichuk.variant.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RationalRandomSelector
    extends AbstractRandomSelector<String, RationalVariant<String>>
    implements Serializable {

    public RationalRandomSelector(String name) {
        super(name, new RationalVariantsList<>());
    }

    public RationalRandomSelector(String name, RationalVariantsList<String> variants) {
        super(name, variants);
    }

    public static RandomSelector of(RationalRandomSelector selector) {
        VariantsList<String> newList = new VariantsList<>();
        VariantsCollection<String, RationalVariant<String>> oldList = selector.getVariantsList();
        for (RationalVariant<String> variant : oldList) {
            newList.add(Variant.of(variant, VariantsCollection.totalWeight(oldList)));
        }
        return new RandomSelector(selector.getName(), newList);
    }

    @Override
    public void setVariants(List<?> values) {
        List<RationalVariant<String>> list = values.stream()
            .map(e -> new RationalVariant<>(e.toString()))
            .collect(Collectors.toList());
        variantsList = new RationalVariantsList<>(list);
    }

    @Override
    public void setVariants(Object... values) {
        List<RationalVariant<String>> variants = Arrays.stream(values)
            .map(Object::toString)
            .map(RationalVariant::new)
            .collect(Collectors.toList());
        variantsList = new RationalVariantsList<>(variants);
    }

    @Override
    public RationalVariant<String> select() {
        int nextVariantIndex = super.nextRandomIndex();
        setSelected(nextVariantIndex);
        return variantsList.get(nextVariantIndex);
    }

    @Override
    public Variant<String> select(int degree) {
        int nextVariantIndex = super.nextIndexByDegree(degree);
        setSelected(nextVariantIndex);
        return variantsList.get(nextVariantIndex);
    }

    private void setSelected(int index) {
        int size = variantsList.size();
        RationalVariant<String> selected = variantsList.get(index);

        double decreaseValue =
            MathUtils.cutRound(selected.getCurrentPercent() / 2, Variant.DIGITS);
        double diff = selected.decreasePercent(decreaseValue);
        decreaseValue += diff;
        double increaseValue =
            MathUtils.cutRound(decreaseValue / (size - 1), Variant.DIGITS);

        for (int i = 0; i < size; i++) {
            if (i != index)
                variantsList.get(i).increasePercent(increaseValue);
        }

        ((RationalVariantsList<String>)variantsList).normalizeToOne();
    }

}
