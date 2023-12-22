package fr.dodo.day04;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode4 {

    public static void main(String[] args) throws IOException {
        String fileName = "day04/input4.txt";
        long value = 0;

        // To avoid referring non-static method inside main() static method
        AventCode4 instance = new AventCode4();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {

            List<String> lines = stream.collect(Collectors.toList());
            for (String line : lines) {
                long winValue = 0;
                String[] card = line.split(":");
                StringTokenizer tokenizer = new StringTokenizer(card[1].trim(), "|");
                // String[] allNumber = card[1].trim().split("|");

                String[] winArray = tokenizer.nextToken().trim().split(" ");
                String[] yourNumber = tokenizer.nextToken().trim().split(" ");

                // String[] winArray = allNumber[0].trim().split(" ");
                // String[] yourNumber = allNumber[1].trim().split(" ");
                List<String> winList = Arrays.asList(winArray);
                List<String> yourList = Arrays.asList(yourNumber);

                for (String number : yourList) {
                    if (number.equals(" ")) {
                        continue;
                    }
                    if (number.equals("")) {
                        continue;
                    }
                    if (winList.contains(number.trim())) {
                        if (winValue == 0) {
                            winValue = 1;
                        } else {
                            winValue = winValue * 2;
                        }
                    }
                }
                value = value + winValue;

            }

            System.out.println("Value: " + value);
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
