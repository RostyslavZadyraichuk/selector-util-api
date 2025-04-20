package com.zadyraichuk.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractVariantsList<E, V extends Variant<E>> implements VariantsCollection<E, V> {

    protected final List<V> variants;

    private VariantColor[] palette;

    private int nextColorIndex;

    public AbstractVariantsList() {
        variants = new ArrayList<>();
        palette = VariantColor.generateOrderedPalette();
    }

    public AbstractVariantsList(List<V> variants) {
        this.variants = variants;
        palette = VariantColor.generateOrderedPalette();
        setUpColors();
    }

    @Override
    public E getValue(int index) {
        return variants.get(index).getValue();
    }

    @Override
    public V get(int index) {
        return variants.get(index);
    }

    @Override
    public void add(V variant) {
        variants.add(variant);
        variant.setColor(nextColor());
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
        V removed = variants.remove(index);
        updateColors(removed, index);
    }

    @Override
    public void remove(V variant) {
        int variantIndex = variants.indexOf(variant);
        V removed = variants.remove(variantIndex);
        updateColors(removed, variantIndex);
    }

    @Override
    public boolean contains(V variant) {
        return variants.contains(variant);
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
        return variants.stream();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(variants);
        setUpColors();
    }

    @Override
    public double[] probabilities() {
        return variants.stream()
                .mapToDouble(Variant::getCurrentPercent)
                .toArray();
    }

    @Override
    public Iterator<V> iterator() {
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

    public void generateNewPalette(int colorsCount) {
        palette = VariantColor.generateOrderedPalette(colorsCount);
        setUpColors();
    }

    protected void setUpColors() {
        nextColorIndex = 0;

        for (V variant : variants) {
            variant.setColor(nextColor());
        }
    }

    protected void updateColors(V removed, int updateStartIndex) {
        nextColorIndex = indexInPalette(removed.getColor());

        for (int i = updateStartIndex; i < variants.size(); i++) {
            variants.get(i).setColor(nextColor());
        }
    }

    protected VariantColor nextColor() {
        VariantColor color = palette[nextColorIndex];
        nextColorIndex++;
        if (nextColorIndex >= palette.length)
            nextColorIndex = 0;

        return color;
    }

    private int indexInPalette(VariantColor color) {
        for (int i = 0; i < palette.length; i++) {
            if (palette[i].ordinal() == color.ordinal())
                return i;
        }

        //TODO optional
        return 0;
    }
}
