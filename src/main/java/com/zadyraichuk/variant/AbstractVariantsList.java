package com.zadyraichuk.variant;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zadyraichuk.general.MathUtils;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@class")
@JsonSubTypes({
    @JsonSubTypes.Type(value = VariantsList.class, name = "VariantsList"),
    @JsonSubTypes.Type(value = RationalVariantsList.class, name = "RationalVariantsList")
})
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public abstract class AbstractVariantsList<E, V extends Variant<E>>
    implements VariantsCollection<E, V>, Serializable {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
    @JsonProperty("variants")
    protected final List<V> variants;

    /**
     * Collection stage for lazy normalization during obtain elements operation
     */
    @JsonProperty("declaredTotalWeight")
    protected int declaredTotalWeight;

    private static final long serialVersionUID = -2543453820153602704L;

    @JsonProperty("palette")
    private VariantColorPalette palette;

    public AbstractVariantsList() {
        variants = new ArrayList<>();
        palette = VariantColorPalette.generateOrderedPalette();
        declaredTotalWeight = 0;
    }

    public AbstractVariantsList(List<V> variants) {
        this.variants = variants;
        palette = VariantColorPalette.generateOrderedPalette();
        setUpColors();
        declaredTotalWeight = 0;
    }

    protected AbstractVariantsList(List<V> variants, int declaredTotalWeight, VariantColorPalette palette) {
        this.variants = variants;
        this.declaredTotalWeight = declaredTotalWeight;
        this.palette = palette;
    }

    @Override
    public void initVariantPercents() {
        int totalWeight = getListTotalWeight();
        if (declaredTotalWeight != totalWeight) {
            declaredTotalWeight = totalWeight;

            initPercents();
            normalizeToOne();
        }
    }

    @Override
    public E getValue(int index) {
        initVariantPercents();
        return variants.get(index).getValue();
    }

    @Override
    public V get(int index) {
        initVariantPercents();
        return variants.get(index);
    }

    @Override
    public void add(V variant) {
        variants.add(variant);
        variant.setColor(palette.nextColor());
    }

    public void addColored(V variant) {
        variants.add(variant);
        setNextColor();
    }

    @Override
    public void swap(int firstIndex, int secondIndex) {
        V first = variants.get(firstIndex);
        V second = variants.get(secondIndex);
        VariantColor temp = first.getColor();
        first.setColor(second.getColor());
        second.setColor(temp);
        variants.set(firstIndex, second);
        variants.set(secondIndex, first);
    }

    @Override
    public void remove(int index) {
        variants.remove(index);
        setNextColor();
    }

    @Override
    public void remove(V variant) {
        variants.remove(variant);
        setNextColor();
    }

    @Override
    public boolean contains(V variant) {
        return variants.contains(variant);
    }

    @Override
    public int indexOf(V variant) {
        return variants.indexOf(variant);
    }

    @Override
    public boolean isEmpty() {
        return variants.isEmpty();
    }

    @Override
    public int size() {
        return variants.size();
    }

    @Override
    public Stream<V> stream() {
        initVariantPercents();
        return variants.stream();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(variants);
        setUpColors();
    }

    @Override
    public double[] probabilities() {
        initVariantPercents();
        return variants.stream()
            .mapToDouble(Variant::getCurrentPercent)
            .toArray();
    }

    @Override
    public Iterator<V> iterator() {
        initVariantPercents();
        return variants.iterator();
    }

    @Override
    public double leftProbabilityBound(V variant) {
        double percentSum = 0;

        for (V v : variants) {
            if (v.equals(variant))
                break;

            percentSum += v.getCurrentPercent();
        }

        return percentSum;
    }

    @Override
    public double rightProbabilityBound(V variant) {
        int variantIndex = variants.indexOf(variant);
        if (variantIndex == -1)
            return 0;

        variant = variants.get(variantIndex);
        return leftProbabilityBound(variant) + variant.getCurrentPercent();
    }

    public VariantColorPalette getPalette() {
        return palette;
    }

    public void setPalette(VariantColorPalette palette) {
        this.palette = palette;
    }

    public void generateNewPalette(int colorsCount) {
        palette = VariantColorPalette.generateOrderedPalette(colorsCount);
        setUpColors();
    }

    protected void initPercents() {
        int totalWeight = VariantsCollection.totalWeight(this);
        if (totalWeight != 0) {
            double singleWeightPercent = VariantsCollection.singleWeightPercent(this, 1.0);
            singleWeightPercent = MathUtils.cutRound(singleWeightPercent, Variant.DIGITS);

            for (Variant<E> variant : variants) {
                double normalized = variant.getVariantWeight() * singleWeightPercent;
                variant.setCurrentPercent(normalized);
            }
        }
    }

    protected int getListTotalWeight() {
        int totalWeight = 0;

        for (V variant : variants) {
            totalWeight += variant.getVariantWeight();
        }

        return totalWeight;
    }

    private void setUpColors() {
        palette.resetColorIndex();

        for (V variant : variants) {
            variant.setColor(palette.nextColor());
        }
    }

    private void setNextColor() {
        if (variants.isEmpty()) {
            palette.resetColorIndex();
        } else {
            V lastVariant = variants.get(variants.size() - 1);
            Optional<Integer> colorIndex = palette.indexInPalette(lastVariant.getColor());
            if (colorIndex.isPresent()) {
                palette.setColorIndex(colorIndex.get());
                palette.nextColor();
            }
        }
    }

}
