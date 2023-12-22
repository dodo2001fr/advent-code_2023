package fr.dodo.day03;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode3 {

    public static void main(String[] args) throws IOException {
        String fileName = "day03/input3.txt";
        long value = 0;

        // To avoid referring non-static method inside main() static method
        AventCode3 instance = new AventCode3();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {

            List<String> lines = stream.collect(Collectors.toList());
            List<String> number = new ArrayList<String>();

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                System.out.println("Line: " + line);
                String part = "";
                boolean isPart = false;
                for (int j = 0; j < line.length(); j++) {
                    if (isNumeric(line.charAt(j))) {
                        part = part + line.charAt(j);
                        if (!isPart) {
                            isPart = isPart(lines, i, j);
                        }
                    } else {
                        if (part.equals("")) {
                            continue;
                        } else {
                            if (isPart) {
                                number.add(part);
                            }
                            part = "";
                            isPart = false;
                        }
                    }

                }
                if (!part.equals("")) {
                    if (isPart) {
                        number.add(part);
                    }
                    part = "";
                    isPart = false;
                }
            }

            for (String part : number) {
                value = value + Integer.parseInt(part);
            }
            System.out.println("Value: " + value);
        }

    }

    public static boolean isPart(List<String> lines, int line, int posChar) {
        boolean value = false;
        char pos;
        if (posChar > 0) {
            // test left
            pos = lines.get(line).charAt(posChar - 1);
            if (isNumeric(pos)) {
                value = false;
            } else if (isDot(pos)) {
                value = false;
            } else {
                return true;
            }

            if (line > 0) {
                // Test diag upper left
                pos = lines.get(line - 1).charAt(posChar - 1);
                if (isNumeric(pos)) {
                    value = false;
                } else if (isDot(pos)) {
                    value = false;
                } else {
                    return true;
                }

            }

            // Test diag down left
            if (line < lines.size() - 1) {
                pos = lines.get(line + 1).charAt(posChar - 1);
                if (isNumeric(pos)) {
                    value = false;
                } else if (isDot(pos)) {
                    value = false;
                } else {
                    return true;
                }

            }

        }

        // test Test upper
        if (line > 0) {
            // Test diag upper left
            pos = lines.get(line - 1).charAt(posChar);
            if (isNumeric(pos)) {
                value = false;
            } else if (isDot(pos)) {
                value = false;
            } else {
                return true;
            }

        }

        // test Down
        if (line < lines.size() - 1) {
            pos = lines.get(line + 1).charAt(posChar);
            if (isNumeric(pos)) {
                value = false;
            } else if (isDot(pos)) {
                value = false;
            } else {
                return true;
            }

        }

        // test right
        if (posChar < lines.get(line).length() - 1) {
            pos = lines.get(line).charAt(posChar + 1);
            if (isNumeric(pos)) {
                value = false;
            } else if (isDot(pos)) {
                value = false;
            } else {
                return true;
            }

            if (line > 0) {
                // Test diag upper right
                pos = lines.get(line - 1).charAt(posChar + 1);
                if (isNumeric(pos)) {
                    value = false;
                } else if (isDot(pos)) {
                    value = false;
                } else {
                    return true;
                }

            }

            // Test diag down right
            if (line < lines.size() - 1) {
                pos = lines.get(line + 1).charAt(posChar + 1);
                if (isNumeric(pos)) {
                    value = false;
                } else if (isDot(pos)) {
                    value = false;
                } else {
                    return true;
                }

            }
        }

        return value;
    }

    public static boolean isNumeric(char str) {
        try {
            Double.parseDouble(str + "");
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDot(char str) {
        return str == '.';
    }

    public static int getInt(String text) {
        Pattern p;
        Matcher m;
        // compilation de la regex avec le motif : "a"
        p = Pattern.compile("(\\d+)");
        // créer et associer le moteur à la regex sur la chaîne "ab"
        m = p.matcher(text);
        m.find();
        return Integer.parseInt(m.group(1));
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
