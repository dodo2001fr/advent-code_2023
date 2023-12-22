package fr.dodo.day01;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode1_1 {

    public static void main(String[] args) throws IOException {
        String fileName = "day01/input1.txt";
        long value = 0;

        // To avoid referring non-static method inside main() static method
        AventCode1_1 instance = new AventCode1_1();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {

            List<String> lines = stream.collect(Collectors.toList());
            for (String line : lines) {
                String first = getFirstDigit(line);
                String last = getLastDigit(line);
                System.out.println(line + ":" + first + last + "-");
                String result = first.trim() + last.trim();
                long resultInt = Long.parseLong(result);
                value = value + resultInt;

            }

            System.out.println("Result : " + value);
        }

    }

    public static String getFirstDigit(String text) {

        Pattern p;
        Matcher m;
        // compilation de la regex avec le motif : "a"
        p = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");
        // créer et associer le moteur à la regex sur la chaîne "ab"
        m = p.matcher(text);
        // si le motif est trouvé
        if (!m.find()) {
            System.out.print("Not found first digit: " + text);
        } else {
            // System.out.print(text + ": " + m.group(1));
        }
        return convert(m.group(1));
    }

    public static String getLastDigit(String text) {

        int pos = 0;

        String result = null;
        if (getLastIndex("(one)", text) > pos) {
            pos = getLastIndex("(one)", text);
            result = "1";
        }
        if (getLastIndex("(two)", text) > pos) {
            pos = getLastIndex("(two)", text);
            result = "2";
        }
        if (getLastIndex("(three)", text) > pos) {
            pos = getLastIndex("(three)", text);
            result = "3";
        }
        if (getLastIndex("(four)", text) > pos) {
            pos = getLastIndex("(four)", text);
            result = "4";
        }
        if (getLastIndex("(five)", text) > pos) {
            pos = getLastIndex("(five)", text);
            result = "5";
        }
        if (getLastIndex("(six)", text) > pos) {
            pos = getLastIndex("(six)", text);
            result = "6";
        }
        if (getLastIndex("(seven)", text) > pos) {
            pos = getLastIndex("(seven)", text);
            result = "7";
        }
        if (getLastIndex("(eight)", text) > pos) {
            pos = getLastIndex("(eight)", text);
            result = "8";
        }
        if (getLastIndex("(nine)", text) > pos) {
            pos = getLastIndex("(nine)", text);
            result = "9";
        }
        if (getLastIndex("(\\d)", text) > pos) {
            result = getLastInt(text);
        }
        return result;
    }

    public static int getLastIndex(String regexp, String text) {

        Pattern p;
        Matcher m;
        // compilation de la regex avec le motif : "a"
        p = Pattern.compile(regexp);
        // créer et associer le moteur à la regex sur la chaîne "ab"
        m = p.matcher(text);
        int pos = 0;
        
        while (m.find()) {

            m.group(1);
            pos = m.end();
        }
        // System.out.println(" - " + match);
        return pos;
    }

    public static String getLastInt(String text) {

        Pattern p;
        Matcher m;
        // compilation de la regex avec le motif : "a"
        p = Pattern.compile("(\\d)");
        // créer et associer le moteur à la regex sur la chaîne "ab"
        m = p.matcher(text);

        String match = null;
        while (m.find()) {

            match = m.group(1);
        }
        // System.out.println(" - " + match);
        return match;
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

    public static String convert(String value) {
        String result = value;
        if ("one".equals(value)) {
            result = "1";
        } else if ("two".equals(value)) {
            result = "2";
        } else if ("three".equals(value)) {
            result = "3";
        } else if ("four".equals(value)) {
            result = "4";
        } else if ("five".equals(value)) {
            result = "5";
        } else if ("six".equals(value)) {
            result = "6";
        } else if ("seven".equals(value)) {
            result = "7";
        } else if ("eight".equals(value)) {
            result = "8";
        } else if ("nine".equals(value)) {
            result = "9";
        }

        return result;

    }
}
