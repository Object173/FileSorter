package com.object173.filesorter.sort;

import com.object173.filesorter.adapter.Adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MergeSorter implements Sorter {

    private final Comparator<String> mComparator;
    private final int mMergeCount;

    private boolean mIsAscending;
    private Predicate<Integer> mIsOrderBroken;

    MergeSorter(final Comparator<String> comparator, final int mergeCount) {
        this.mComparator = comparator;
        this.mMergeCount = mergeCount;
    }

    @Override
    public void sort(final List<Adapter> inAdapters, final Adapter outAdapter, final boolean isAscending) throws IOException {
        mIsAscending = isAscending;
        mIsOrderBroken = integer -> (isAscending && integer < 0) || (!isAscending && integer > 0);

        final Queue<Adapter> currentMerge = new LinkedList<>();
        final Queue<Adapter> adapterQueue = new LinkedList<>(inAdapters);
        adapterQueue.removeIf(adapter -> !adapter.isExist());

        if(adapterQueue.isEmpty()) {
            throw  new FileNotFoundException();
        }

        Adapter currentCash = inAdapters.size() > mMergeCount ? outAdapter.createEmpty() : outAdapter;
        boolean isInAdaptersEnd = false;

        do {
            if(adapterQueue.size() <= mMergeCount) {
                Objects.requireNonNull(currentCash).delete();
                currentCash = outAdapter;
            }

            while (currentMerge.size() < mMergeCount && !adapterQueue.isEmpty()) {
                currentMerge.add(adapterQueue.poll());
            }
            merge(currentMerge, Objects.requireNonNull(currentCash).openToWrite());
            adapterQueue.add(currentCash);

            currentCash = currentMerge.poll();
            if (isInAdaptersEnd) {
                currentMerge.forEach(Adapter::delete);
            }
            else {
                if(inAdapters.contains(currentCash)) {
                    currentCash = outAdapter.createEmpty();
                }
                for (Adapter adapter : currentMerge) {
                    if(!inAdapters.contains(adapter)) {
                        adapter.delete();
                        isInAdaptersEnd = true;
                    }
                }
            }
            currentMerge.clear();
        }
        while (adapterQueue.size() > 1);

        Objects.requireNonNull(currentCash).delete();
    }

    private static class Item {
        final Iterator<String> iterator;
        String lastValue;

        Item(Iterator<String> iterator) {
            this.iterator = iterator;
        }
    }

    private void merge(final Queue<Adapter> adapters, final Adapter.StreamWriter cash) throws IOException {
        final List<Stream<String>> streams = adapters.stream().map(adapter -> {
            try {return adapter.openToRead();} catch (IOException e) {return null;}})
                .filter(Objects::nonNull).collect(Collectors.toList());

        final List<Item> items = new ArrayList<>(streams.size());
        final Comparator<Item> itemComparator = (o1, o2) -> mComparator.compare(o1.lastValue, o2.lastValue);
        streams.forEach(stream -> items.add(new Item(stream.iterator())));

        items.forEach(item -> item.lastValue = getNext(item.iterator, null));
        items.removeIf(stringItem -> stringItem.lastValue == null);

        Item lastItem;
        while(!items.isEmpty()) {
            lastItem = (mIsAscending ? items.stream().min(itemComparator) : items.stream().max(itemComparator)).get();
            cash.write(lastItem.lastValue);
            lastItem.lastValue = getNext(lastItem.iterator, lastItem.lastValue);
            if(lastItem.lastValue == null) {
                items.remove(lastItem);
            }
        }

        streams.forEach(BaseStream::close);
        cash.close();
    }

    private String getNext(final Iterator<String> buf, final String lastItem) {
        String item;
        do {
            if(!buf.hasNext()) {
                return null;
            }
            item = buf.next();
        }
        while (lastItem != null &&
                mIsOrderBroken.test(mComparator.compare(item, lastItem)));
        return item;
    }
}
