package fr.dodo.day09;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode9_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "day09/input9.txt";

        // To avoid referring non-static method inside main() static method
        AventCode9_2 instance = new AventCode9_2();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    int pos = 1;

    public void process(List<String> lines) {
        long value = 0;

        for (String line : lines) {
            String[] hsitoryArray = line.trim().split(" ");
            List<Long> history = new ArrayList<Long>();
            for (String hist : hsitoryArray) {
                history.add(Long.valueOf(hist));
            }
            long next = getNext(history);
            System.out.println("Next:" + next);
            value = value + next;
        }

        System.out.println("Value: " + value);
    }

    public long getNext(List<Long> history) {

        List<Long> next = new ArrayList<Long>();

        long previous = history.get(0).longValue();
        long first = previous;
        boolean allZero = true;
        for (int i = 1; i < history.size(); i++) {
            long current = history.get(i).longValue();
            long diff = current - previous;
            if (diff != 0)
                allZero = false;
            next.add(diff);
            previous = current;
        }

        if (!allZero) {
            long diff = getNext(next);
            return first - diff;
        } else {
            return first;
        }
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
