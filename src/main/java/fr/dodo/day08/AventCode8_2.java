package fr.dodo.day08;

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

public class AventCode8_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "day08/input8.txt";

        // To avoid referring non-static method inside main() static method
        AventCode8_2 instance = new AventCode8_2();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    int pos = 1;
    List<String> begin = null;

    public void process(List<String> lines) {
        long value = 0;

        String instruction = lines.get(0);

        Map<String, Mapping> map = getMapping(lines);

        List<Integer> stepList = new ArrayList<Integer>();
        for (String position : begin) {
            int nbStep = 0;
            int pos = 0;
            boolean isFinish = false;
            while (!isFinish) {
                if (pos == instruction.length()) {
                    pos = 0;
                }

                String direction = String.valueOf(instruction.charAt(pos));
                Mapping currentMap = map.get(position);
                if (direction.equals("L"))
                    position = currentMap.left;
                if (direction.equals("R"))
                    position = currentMap.right;
                nbStep++;
                pos++;
                if (position.lastIndexOf("Z") == 2)
                    isFinish = true;
                // System.err.println(value + " - " + position);
            }
            System.err.println(position + " - " + nbStep);
            stepList.add(Integer.valueOf(nbStep));

        }

        int pgcd = pgcd(stepList.get(0).intValue(), stepList.get(1).intValue());
        for (int i = 3; i < stepList.size(); i++) {
            pgcd = pgcd(pgcd, stepList.get(i).intValue());
        }

        long ppcm = Calcule_PPCM(stepList.get(0).intValue(), stepList.get(1).intValue());
        for (int i = 2; i < stepList.size(); i++) {
            ppcm = Calcule_PPCM(ppcm, stepList.get(i).intValue());
        }

        long ppcm2 = ppcm(stepList.get(0).intValue(), stepList.get(1).intValue());
        for (int i = 2; i < stepList.size(); i++) {
            ppcm2 = ppcm(ppcm2, stepList.get(i).intValue());
        }

        long prod = 1;
        for (Integer step : stepList) {
            prod = prod + step.intValue();
        }
        System.out.println("pgcd " + pgcd);
        System.out.println("ppcm " + ppcm);
        System.out.println("ppcm2 " + ppcm2);
        value = prod / pgcd;

        System.out.println("Value: " + value);
    }

    public int pgcd(int a, int b) {
        return (b == 0) ? a : pgcd(b, a % b);
    }

    public static long Calcule_PPCM(long Nb1, long Nb2) {
        long Produit, Reste, PPCM;

        Produit = Nb1 * Nb2;
        Reste = Nb1 % Nb2;
        while (Reste != 0) {
            Nb1 = Nb2;
            Nb2 = Reste;
            Reste = Nb1 % Nb2;
        }
        PPCM = Produit / Nb2;
        // System.out.println("PGCD = " + Nb2 + " PPCM = " + PPCM);
        return PPCM;
    } // fin Calcule_PPCM

    public long ppcm(long a, long b) {
        long p = a * b;
        while (a != b) {
            if (a < b)
                b -= a;
            else
                a -= b;
        }
        return p / a;
    }

    public Map<String, Mapping> getMapping(List<String> lines) {
        begin = new ArrayList<String>();
        Map<String, Mapping> result = new HashMap<String, Mapping>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().equals(""))
                continue;

            String[] mapArray = line.trim().split("=");
            String src = mapArray[0].trim();
            String[] direction = mapArray[1].trim().split(",");
            String left = direction[0].trim().substring(1);
            String right = direction[1].trim().substring(0, direction[1].trim().length() - 1);

            result.put(src, new Mapping(left, right));

            if (src.lastIndexOf("A") == 2) {
                begin.add(src);
            }
        }
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

    public static boolean isNumeric(char str) {
        try {
            Double.parseDouble(str + "");
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    class Mapping {
        String left;
        String right;

        public Mapping(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }
}
