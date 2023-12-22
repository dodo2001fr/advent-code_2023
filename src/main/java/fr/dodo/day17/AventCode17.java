package fr.dodo.day17;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode17 {

    public static void main(String[] args) throws IOException {
        String fileName = "input16.txt";

        // To avoid referring non-static method inside main() static method
        AventCode17 instance = new AventCode17();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.lines = lines;
            instance.process();

        }
    }

    public boolean[][] grid;
    public List<String> lines;

    public void process() {
        long value = 0;

        System.out.println("Value: " + value);
    }


    private File getFile(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }
    }
}
