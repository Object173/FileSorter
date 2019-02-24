package com.object173.filesorter.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class ListAdapter implements Adapter {

    private final List<String> mCash;

    ListAdapter(final List<String> cash) {
        this.mCash = cash;
    }

    @Override
    public Stream<String> openToRead() {
        return mCash.stream();
    }

    @Override
    public StreamWriter openToWrite() {
        return new StreamListWriter(mCash);
    }

    @Override
    public boolean isExist() {
        return mCash != null;
    }

    @Override
    public Adapter createEmpty() {
        return new ListAdapter(new ArrayList<>());
    }

    @Override
    public void delete() throws IllegalStateException {
        mCash.clear();
    }

    static class StreamListWriter implements StreamWriter {

        private final List<String> mCash;

        StreamListWriter(final List<String> cash) {
            this.mCash = cash;
            mCash.clear();
        }

        @Override
        public void write(final String input) {
            mCash.add(input);
        }

        @Override
        public void close() {
        }
    }
}
