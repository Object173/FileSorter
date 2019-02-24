package com.object173.filesorter.adapter;

import java.io.File;
import java.util.List;

public class AdapterFactory {
    public static Adapter createadapter(final List<String> list) {
        return new ListAdapter(list);
    }
    public static Adapter createadapter(final File file) {
        return new FileAdapter(file);
    }
}
