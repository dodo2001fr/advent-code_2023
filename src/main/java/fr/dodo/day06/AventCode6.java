package fr.dodo.day06;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode6 {

    public static void main(String[] args) throws IOException {
        String fileName = "day06/input6.txt";

        // To avoid referring non-static method inside main() static method
        AventCode6 instance = new AventCode6();

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
        long value = 1;
        String firstLine = lines.get(0);
        String[] firstSplit = firstLine.split(":");

        String[] timeArray = firstSplit[1].trim().split(" ");
        List<Long> timeList = new ArrayList<Long>();
        for (int i = 0; i < timeArray.length; i++) {
            if (timeArray[i].trim().equals(""))
                continue;
            timeList.add(Long.parseLong(timeArray[i].trim()));
        }

        String secondLine = lines.get(1);
        String[] secondSplit = secondLine.split(":");

        String[] distArray = secondSplit[1].trim().split(" ");
        List<Long> distList = new ArrayList<Long>();
        for (int i = 0; i < distArray.length; i++) {
            if (distArray[i].trim().equals(""))
                continue;
            distList.add(Long.parseLong(distArray[i].trim()));
        }

        for (int i = 0; i < timeList.size(); i++) {
            value = value * getNbCase(timeList.get(i), distList.get(i));
        }

        System.out.println("Value: " + value);
    }

    public long getNbCase(long time, long dist) {
        long count = 0;

        for (int i = 1; i < time; i++) {
            if (i >= dist) {
                break;
            }
            // Temps restant
            long restTime = time - i;
            long raceDist = restTime * i;

            if (raceDist > dist) {
                System.out.print(" " + i + "/" + raceDist);
                count++;
            }
        }
        System.out.println("\nRace: " + time + " - " + dist + " = " + count);

        return count;
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

    class MapGarden {
        public long source;
        public long dest;
        public long range;
    }
}
