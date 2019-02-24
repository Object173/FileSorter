package com.object173.filesorter.sort;

import com.object173.filesorter.adapter.Adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Sorter {
    void sort(List<Adapter> inAdapters, Adapter outAdapter, boolean isAscending) throws IOException;
}
