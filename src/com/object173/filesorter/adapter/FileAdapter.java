package com.object173.filesorter.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileAdapter implements Adapter {

    private static final String NEW_FILE_NAME = "new_file";

    private final Path mPath;

    FileAdapter(final File file) {
        this.mPath = Paths.get(file.getAbsolutePath());
    }

    @Override
    public Stream<String> openToRead() throws IOException {
        return Files.lines(mPath);
    }

    @Override
    public StreamWriter openToWrite() throws IOException {
        return new StreamFileWriter(mPath.toFile());
    }

    @Override
    public boolean isExist() {
        return Files.exists(mPath);
    }

    @Override
    public Adapter createEmpty() throws IOException {
        final Path newPath = findEmptyName();
        Files.createFile(newPath);
        return new FileAdapter(newPath.toFile());
    }

    private Path findEmptyName() {
        Path newPath = Paths.get(NEW_FILE_NAME);
        for(int i=0;Files.exists(newPath);i++) {
            newPath = Paths.get(newPath.toAbsolutePath() + Integer.toString(i));
        }
        return newPath;
    }

    @Override
    public void delete() {
        try {
            Files.delete(mPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class StreamFileWriter implements StreamWriter {

        private final PrintWriter mWriter;

        StreamFileWriter(final File file) throws IOException {
            mWriter = new PrintWriter(new FileOutputStream(file, false));
        }

        @Override
        public void write(final String input) {
            mWriter.println(input);
        }

        @Override
        public void close() {
            mWriter.close();
        }
    }
}
