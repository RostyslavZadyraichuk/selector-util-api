package com.zadyraichuk.selector;

import com.zadyraichuk.variant.Variant;
import com.zadyraichuk.variant.VariantsCollection;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

//todo add documentation
public abstract class AbstractRandomSelector<E, V extends Variant<E>>
    implements Selector<V>, Serializable {

    protected static final Random RANDOM = new Random(System.currentTimeMillis());

    //todo change to AbstractVariantsList (or even create AbstractVariantsCollection)
    protected VariantsCollection<E, V> variantsList;

    private static final long serialVersionUID = -8193684848916309585L;

    private String name;

    private int currentRotation;

    public AbstractRandomSelector(String name, VariantsCollection<E, V> collection) {
        this.name = name;
        variantsList = collection;
        currentRotation = 90;
    }

    public abstract void setVariants(List<?> values);

    public abstract void setVariants(Object... values);

    public VariantsCollection<E, V> getVariantsList() {
        return variantsList;
    }

    public int getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(int currentRotation) {
        this.currentRotation = currentRotation % 360;
    }

    public void setVariantsList(VariantsCollection<E, V> variantsList) {
        this.variantsList = variantsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //todo check type before cast
    public int indexOf(Variant<E> variant) {
        return variantsList.indexOf((V) variant);
    }

    protected int nextRandomIndex() {
        double percent = AbstractRandomSelector.RANDOM.nextDouble();
        return getVariantIndexByPercent(percent);
    }

    protected int nextIndexByDegree(int degree) {
        double percent = (double) degree / 360;
        return getVariantIndexByPercent(percent);
    }

    private int getVariantIndexByPercent(double percent) {
        double[] probabilities = variantsList.probabilities();

        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            if (percent <= sum)
                return i;
        }

        return probabilities.length - 1;
    }
}
