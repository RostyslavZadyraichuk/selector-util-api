package com.zadyraichuk;

import com.zadyraichuk.chooser.Variant;

import java.util.List;

import static com.zadyraichuk.chooser.Variant.*;

public class RationalRandomSelector extends RandomSelector {

    @Override
    public String nextVariant() {
        int nextVariantIndex = nextVariantIndex();
        setSelected(nextVariantIndex);
        return list.get(nextVariantIndex);
    }

    private void setSelected(int index) {
        List<Variant> variants = list.variants;

        int size = variants.size();
        double decreaseValue =
                CutType.ROUND.cut(variants.get(index).getCurrentPercent() / 2, DIGITS);
        double diff = variants.get(index).decreasePercent(decreaseValue);

        decreaseValue += diff;
        double increaseValue =
                CutType.ROUND.cut(decreaseValue / (size - 1), DIGITS);

        for (int i = 0; i < size; i++) {
            if (i != index)
                variants.get(i).increasePercent(increaseValue);
        }

        list.approxToOneAfterNormalize();
    }
}
