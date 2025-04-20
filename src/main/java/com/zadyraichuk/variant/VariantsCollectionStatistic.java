package com.zadyraichuk.variant;

public class VariantsCollectionStatistic {

    public static void showStatistic(VariantsCollection<?, ?> variants) {
        if (variants.isEmpty()) {
            System.out.println("Empty collection");
        } else {
            VariantsCollectionStatistic.showCollectionStatistic(variants);
            VariantsCollectionStatistic.showVariants(variants);
        }
    }

    public static void showStatistic(RationalVariantsList<?> variants) {
        if (variants.isEmpty()) {
            System.out.println("Empty collection");
        } else {
            VariantsCollectionStatistic.showCollectionStatistic(variants);
            VariantsCollectionStatistic.showVariants(variants);
        }
    }

    protected static void showCollectionStatistic(VariantsCollection<?, ?> variants) {
        String oneItem = "#";
        double realTotalPercent = VariantsCollection.totalPercent(variants);
        double singleWeightPercent = VariantsCollection.singleWeightPercent(variants, realTotalPercent);

        System.out.printf("Single weight: %s = %.3f%n",
            oneItem, singleWeightPercent);
        System.out.printf("Total weights: %d%n", VariantsCollection.totalWeight(variants));
        System.out.printf("Total percent: %f%n", VariantsCollection.totalPercent(variants));
    }

    protected static void showVariants(VariantsCollection<?, ?> variants) {
        String oneItem = "#";
        double minimalPercent = VariantsCollection.minimalPercent(variants);

        System.out.printf("\t%-20s: Percent | %-20s | %-20s%n",
            "Element", "Init weight", "Weight");

        for (Variant<?> variant : variants) {
            String initialWeight = oneItem.repeat(variant.getVariantWeight());
            double percentWeight = variant.getPercentWeight(minimalPercent);
            StringBuilder currentWeight = new StringBuilder(oneItem.repeat((int) percentWeight));
            if (percentWeight % 1 >= 0.5)
                currentWeight.append('|');

            System.out.printf("\t%-20s: %6.3f%% | %-20s | %-20s%n",
                variant.getValue(),
                variant.getCurrentPercent(),
                initialWeight,
                currentWeight);
        }
    }

    protected static void showVariants(RationalVariantsList<?> variants) {
        String oneItem = "#";
        double minimalPercent = VariantsCollection.minimalPercent(variants);

        System.out.printf("\t%-20s: Min percent | Percent | %-20s | %-20s%n",
            "Element", "Init weight", "Weight");

        for (RationalVariant<?> variant : variants) {
            String initialWeight = oneItem.repeat(variant.getVariantWeight());
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
