package com.zadyraichuk;

import com.zadyraichuk.chooser.VariantsList;

import java.util.List;
import java.util.Random;

public class RandomSelector {
    //todo add documentation comments

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    protected VariantsList list;

    public void setVariants(VariantsList list) {
        this.list = list;
    }

    public void setVariants(List elements) {
        list = new VariantsList(elements);
    }

    public void setVariants(Object... elements) {
        list = new VariantsList(elements);
    }

    public String nextVariant() {
        int nextVariantIndex = nextVariantIndex();
        return list.get(nextVariantIndex);
    }

    protected int nextVariantIndex() {
        if (list.isChanged)
            list.normalize();

        double[] probabilities = list.getProbabilities();
        double selected = RANDOM.nextDouble();

        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            if (selected <= sum)
                return i;
        }

        return probabilities.length - 1;
    }

}
