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

    private static final long serialVersionUID = -7500842157433621218L;

    public RationalRandomSelector(String name) {
        super(name, new RationalVariantsList<>());
    }

    public RationalRandomSelector(String name, RationalVariantsList<String> variants) {
        super(name, variants);
    }

    public static RationalRandomSelector of(AbstractRandomSelector<String, ? extends Variant<String>> selector) {
        RationalVariantsList<String> newList = new RationalVariantsList<>();
        VariantsCollection<String, ? extends Variant<String>> oldList = selector.getVariantsList();
        newList.setPalette(((AbstractVariantsList<?, ?>) oldList).getPalette());
        for (Variant<String> variant : oldList) {
            newList.add(RationalVariant.of(variant));
        }
        return new RationalRandomSelector(selector.getName(), newList);
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
    public RationalVariant<String> select(int degree) {
        int nextVariantIndex = super.nextIndexByDegree(degree);
        setSelected(nextVariantIndex);
        return variantsList.get(nextVariantIndex);
    }

    //todo change increasePercent model that variants get percent
    // appropriate to their weights
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
