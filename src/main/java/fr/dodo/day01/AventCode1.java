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

public class AventCode1 {

    public static void main(String[] args) throws IOException {
        String fileName = "day01/input.txt";
        long value = 0;

        // To avoid referring non-static method inside main() static method
        AventCode1 instance = new AventCode1();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {

            List<String> lines = stream.collect(Collectors.toList());
            for (String line : lines) {
                String result = getFirstDigit(line) + getLastDigit(line);
                int resultInt = Integer.parseInt(result);
                value = value + resultInt;

            }

            System.out.println("Result : " + value);
        }

    }

    public static String getFirstDigit(String text) {

        Pattern p;
        Matcher m;
        // compilation de la regex avec le motif : "a"
        p = Pattern.compile("^[^\\d]*(\\d)");
        // créer et associer le moteur à la regex sur la chaîne "ab"
        m = p.matcher(text);
        // si le motif est trouvé
        if (!m.find()) {
            System.out.println("Not found first digit: " + text);
        } else {
            System.out.println("first: " + m.group(1));
        }
        return m.group(1);
    }

    public static String getLastDigit(String text) {

        Pattern p;
        Matcher m;
        // compilation de la regex avec le motif : "a"
        p = Pattern.compile("(\\d)(?!.*\\d)");
        // créer et associer le moteur à la regex sur la chaîne "ab"
        m = p.matcher(text);
        // si le motif est trouvé
        if (!m.find()) {
            System.out.println("Not found last digit: " + text);
        } else {
            System.out.println("last: " + m.group(1));
        }
        return m.group(1);
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
