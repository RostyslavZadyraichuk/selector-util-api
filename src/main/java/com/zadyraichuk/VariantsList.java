package com.zadyraichuk;

import com.zadyraichuk.chooser.Variant;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.zadyraichuk.chooser.Variant.*;

public class VariantsList implements Iterable<Variant> {

    protected static final int MAX_TIMES_DIVIDING = 3;

    private static final Variant FOR_COMPARE = new Variant(null);

    protected final List<Variant> variants;
    protected boolean isChanged;

    public VariantsList() {
        variants = new ArrayList<>();
    }

    public VariantsList(Object... elements) {
        variants = List.of(toVariants(elements));
        normalize();
    }

    public VariantsList(List<Object> elements) {
        variants = elements.stream()
                .map(Variant::new)
                .collect(Collectors.toList());
        normalize();
    }


    public static VariantsList readFromFile(File file) {
        VariantsList list = new VariantsList();

        try (Reader r = new FileReader(file);
            BufferedReader reader = new BufferedReader(r)) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] elements = line.split("\t");
                Variant variant = new Variant(
                        elements[0],
                        Integer.parseInt(elements[1]),
                        Double.parseDouble(elements[2].replace(',', '.')),
                        Double.parseDouble(elements[3].replace(',', '.'))
                );
                list.add(variant);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void writeToFile(VariantsList list, File file) {
        if (list.isChanged)
            list.normalize();

        try (FileWriter w = new FileWriter(file);
            PrintWriter writer = new PrintWriter(w)) {
            writer.println("Value\tWeight\tMinimum\tCurrent");

            for (Variant variant : list.variants) {
                String str = String.format("%s\t%d\t%.3f\t%.3f",
                        variant.getValue(),
                        variant.getPercentWeight(),
                        variant.getMinPercent(),
                        variant.getCurrentPercent());
                writer.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showStatistic(VariantsList list) {
        if (list.variants.isEmpty()) {
            System.out.println("Empty collection");
        } else {
            if (list.isChanged)
                list.normalize();

            String oneItem = "#";
            double oneItemPercent = singleWeightPercent(list.variants.get(0));
            double minimalPercent = list.getMinimalPercent();

            System.out.printf("Single weight: %s = %.3f%n",
                    oneItem, oneItemPercent);
            System.out.printf("Total weights: %d%n", list.getTotalWeight());
            System.out.printf("Total percent: %f%n", list.getTotalPercent());

            System.out.printf("\t%-20s: Min percent | Percent | %-20s | %-20s%n",
                    "Element", "Init weight", "Weight");
            for (Variant variant : list.variants) {
                String initialWeight = oneItem.repeat(variant.getPercentWeight());
                double percentWeight = variant.getPercentWeight(minimalPercent);
                StringBuilder currentWeight = new StringBuilder(oneItem.repeat((int) percentWeight));
                if (percentWeight % 1 >= 0.5)
                    currentWeight.append('|');

                System.out.printf("\t%-20s: %10.3f%% | %6.3f%% | %-20s | %-20s%n",
                        variant.getValue(),
                        variant.getMinPercent(),
                        variant.getCurrentPercent(),
                        initialWeight,
                        currentWeight);
            }
        }
    }

    public String get(int index) {
        return variants.get(index).getValue();
    }

    public void add(Object element) {
        variants.add(new Variant(element));
        isChanged = true;
    }

    public void add(Object element, int percentWeight) {
        variants.add(new Variant(element, percentWeight));
        isChanged = true;
    }

    public void remove(Object element) {
        FOR_COMPARE.setValue(element);
        variants.remove(FOR_COMPARE);
        isChanged = true;
    }

    public boolean contains(Object element) {
        FOR_COMPARE.setValue(element);
        return variants.contains(FOR_COMPARE);
    }

    public void clear() {
        variants.clear();
        isChanged = false;
    }

    public void shuffle() {
        Collections.shuffle(variants);
    }

    @Override
    public Iterator<Variant> iterator() {
        return variants.iterator();
    }

    protected double[] getProbabilities() {
        return variants.stream()
                .mapToDouble(Variant::getCurrentPercent)
                .toArray();
    }

    protected void normalize() {
        int totalWeight = getTotalWeight();
        if (totalWeight != 0) {
            double onePercentNormalized =
                CutType.ROUND.cut(1.0 / totalWeight, DIGITS);

            for (Variant variant : variants) {
                double normalized = variant.getPercentWeight() * onePercentNormalized;
                variant.setCurrentPercent(normalized);
            }
        }

        approxToOneAfterNormalize();
        isChanged = false;
    }

    protected void approxToOneAfterNormalize() {
        double minDigit = Math.pow(10, MAX_TIMES_DIVIDING * (-1));
        double edge = variants.stream()
                .mapToDouble(e -> minDigit * e.getPercentWeight()).sum();

        double total = getTotalPercent();
        if (total > 1.0) {
            while (total > 1.0) {
                variants.forEach(e -> e.decreasePercent(minDigit * e.getPercentWeight()));
                total = getTotalPercent();
            }
        } else if (total < 1.0 - edge) {
            while (total < 1.0 - edge) {
                variants.forEach(e -> e.increasePercent(minDigit * e.getPercentWeight()));
                total = getTotalPercent();
            }
        }
    }

    private int getTotalWeight() {
        int totalWeight = 0;

        for (Variant variant : variants) {
            totalWeight += variant.getPercentWeight();
        }

        return totalWeight;
    }

    private double getTotalPercent() {
        double totalPercent = 0;

        for (Variant variant : variants) {
            totalPercent += variant.getCurrentPercent();
        }

        return totalPercent;
    }

    private double getMinimalPercent() {
        if (variants.isEmpty())
            return 0;
        return variants.stream().mapToDouble(Variant::getCurrentPercent).min().getAsDouble();
    }

    private void add(Variant variant) {
        variants.add(variant);
    }

}
