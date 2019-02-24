package com.object173.filesorter.sort;

import java.util.Comparator;

public class SorterFactory {
    public static Sorter getMerge(final Comparator<String> comparator, final int mergeCount) {
        return new MergeSorter(comparator, mergeCount);
    }
}
