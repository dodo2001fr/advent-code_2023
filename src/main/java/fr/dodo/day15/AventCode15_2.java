package fr.dodo.day15;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode15_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "input15.txt";

        // To avoid referring non-static method inside main() static method
        AventCode15_2 instance = new AventCode15_2();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    Map<Integer, List<String>> boxes = new HashMap<Integer, List<String>>();
    Map<String, Integer> lens = new HashMap<String, Integer>();

    public void process(List<String> lines) {
        long value = 0;

        String line = lines.get(0);
        String[] steps = line.trim().split(",");

        for (int i = 0; i < steps.length; i++) {
            hasString(steps[i]);
            // System.out.println(steps[i] + " - " + value);
        }

        for (int i = 0; i < 256; i++) {
            List<String> box = boxes.get(i);
            if (box != null) {
                for (int j = 0; j < box.size(); j++) {
                    String label = box.get(j);
                    Integer focal = lens.get(label);
                    long calc = (i + 1) * (j + 1) * focal.intValue();
                    System.out.println(i + " - " + calc);
                    value += calc;
                }
            }
        }

        System.out.println("Value: " + value);
    }

    public long hasString(String chaine) {
        int boxNum = 0;
        String label = "";
        for (int i = 0; i < chaine.length(); i++) {
            char c = chaine.charAt(i);
            if (c == '=' || c == '-') {
                break;
            } else {
                label = label + c;
                boxNum = hash(c, boxNum);
            }
        }
        if (chaine.contains("-")) {
            List<String> box = boxes.get(Integer.valueOf(boxNum));
            if (box != null) {
                box.remove(label);
                boxes.put(Integer.valueOf(boxNum), box);
                lens.remove(label);
            }
        } else if (chaine.contains("=")) {
            String[] array = chaine.split("=");
            String value = array[1];
            List<String> box = boxes.get(Integer.valueOf(boxNum));
            if (box == null) {
                box = new ArrayList<>();
                box.add(label);
                lens.put(label, Integer.valueOf(value));

            } else {
                if (box.contains(label)) {
                    lens.put(label, Integer.valueOf(value));
                } else {
                    box.add(label);
                    lens.put(label, Integer.valueOf(value));
                }
            }
            boxes.put(Integer.valueOf(boxNum), box);
        }
        return boxNum;
    }

    public int hash(char c, int previous) {
        int result = previous;
        result += (int) c;
        result = result * 17;
        result = result % 256;
        return result;

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

    public class Lens {
        int label;
        int focal;

        public Lens(int label, int focal) {
            this.label = label;
            this.focal = focal;
        }
    }
}
