package com.zadyraichuk.selector;

import com.fasterxml.jackson.annotation.*;
import com.zadyraichuk.variant.Variant;
import com.zadyraichuk.variant.VariantsCollection;
import java.util.List;
import java.util.Random;

//todo add documentation
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = RandomSelector.class, name = "RandomSelector"),
    @JsonSubTypes.Type(value = RationalRandomSelector.class, name = "RationalRandomSelector")
})
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public abstract class AbstractRandomSelector<E, V extends Variant<E>>
    implements Selector<V> {

    protected static final Random RANDOM = new Random(System.currentTimeMillis());

    @JsonProperty("id")
    private long id;

    //todo change to AbstractVariantsList (or even create AbstractVariantsCollection)
    @JsonProperty("variantsList")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
    protected VariantsCollection<E, V> variantsList;

    @JsonProperty("name")
    private String name;

    @JsonProperty("currentRotation")
    private int currentRotation;

    public AbstractRandomSelector(String name, VariantsCollection<E, V> collection) {
        this.id = System.currentTimeMillis();
        this.name = name;
        variantsList = collection;
        currentRotation = 90;
    }

    protected AbstractRandomSelector(long id, VariantsCollection<E, V> variantsList, String name, int currentRotation) {
        this.id = id;
        this.variantsList = variantsList;
        this.name = name;
        this.currentRotation = currentRotation;
    }

    public abstract void setVariants(List<?> values);

    @JsonIgnore
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
