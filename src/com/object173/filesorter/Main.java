package com.object173.filesorter;

import com.object173.filesorter.adapter.Adapter;
import com.object173.filesorter.adapter.AdapterFactory;
import com.object173.filesorter.sort.Sorter;
import com.object173.filesorter.sort.SorterFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(final String[] args) {
        final Sorter sorter = SorterFactory.getMerge(Comparator.comparingDouble(Double::parseDouble), 2);
        final List<Adapter> adapters = new ArrayList<>();
        adapters.add(AdapterFactory.createadapter(new File("1.txt")));
        adapters.add(AdapterFactory.createadapter(new File("2.txt")));
        adapters.add(AdapterFactory.createadapter(new File("3.txt")));
        adapters.add(AdapterFactory.createadapter(new File("4.txt")));

        try {
            sorter.sort(adapters, AdapterFactory.createadapter(new File("result.txt")), true);
        }
        catch (FileNotFoundException ex) {
            System.out.println("no file found");
        }
        catch (SecurityException ex) {
            System.out.println("file access error " + ex.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
