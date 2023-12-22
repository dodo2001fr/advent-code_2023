package fr.dodo.day04;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode4_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "day04/input4.txt";
        long value = 0;

        // To avoid referring non-static method inside main() static method
        AventCode4_2 instance = new AventCode4_2();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {

            List<String> lines = stream.collect(Collectors.toList());
            Map<Integer, Integer> carWin = new HashMap<Integer, Integer>();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
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
                        winValue = winValue + 1;

                    }
                }
                Integer currentCount = carWin.get(Integer.valueOf(i));
                int currentNb = 1;
                if (currentCount != null) {
                    currentNb = currentCount.intValue() + 1;
                    carWin.put(i, Integer.valueOf(currentNb));
                } else {
                    carWin.put(i, Integer.valueOf(1));
                }

                if (winValue > 0) {

                    for (int j = 1; j <= winValue; j++) {
                        // Next pos
                        int pos = i + j;
                        Integer posInt = Integer.valueOf(pos);
                        Integer cardCount = carWin.get(posInt);
                        if (cardCount == null) {
                            carWin.put(posInt, Integer.valueOf(currentNb));
                        } else {
                            int newVal = cardCount.intValue() + currentNb;
                            carWin.put(posInt, Integer.valueOf(newVal));
                        }
                    }
                }

                Integer thisCard = carWin.get(Integer.valueOf(i));
                System.out.println("Card: " + i + "-" + thisCard + "-" + winValue);
                if (thisCard != null) {
                    value = value + thisCard.intValue();
                }

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
