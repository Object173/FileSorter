package com.object173.filesorter.adapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.stream.Stream;

public interface Adapter {
    Stream<String> openToRead() throws IOException;
    StreamWriter openToWrite() throws IOException;
    boolean isExist();
    Adapter createEmpty() throws IOException;
    void delete();

    interface StreamWriter extends Closeable {
        void write(String input);
    }
}
