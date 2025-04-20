package com.zadyraichuk.selector;

import com.zadyraichuk.variant.Variant;
import com.zadyraichuk.variant.VariantsCollection;
import java.util.List;
import java.util.Random;

//todo add documentation
public abstract class AbstractRandomSelector<E, V extends Variant<E>>
        implements Selectable<Variant<E>> {

    protected static final Random RANDOM = new Random(System.currentTimeMillis());

    protected VariantsCollection<E, V> variantsList;

    private int lastRotateDegree;

    public AbstractRandomSelector(VariantsCollection<E, V> collection) {
        variantsList = collection;
        lastRotateDegree = 90;
    }

    public abstract void setVariants(List<?> values);

    public abstract void setVariants(Object... values);

    public VariantsCollection<E, V> getVariantsList() {
        return variantsList;
    }

    public int getLastRotateDegree() {
        return lastRotateDegree;
    }

    public void setLastRotateDegree(int lastRotateDegree) {
        this.lastRotateDegree = lastRotateDegree % 360;
    }

    public void setVariantsList(VariantsCollection<E, V> variantsList) {
        this.variantsList = variantsList;
    }

    protected int nextRandomIndex() {
        double[] probabilities = variantsList.probabilities();
        double selected = AbstractRandomSelector.RANDOM.nextDouble();

        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            if (selected <= sum)
                return i;
        }

        return probabilities.length - 1;
    }
}
