package fr.dodo.day12;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode12 {

    public static void main(String[] args) throws IOException {
        String fileName = "input12.txt";

        // To avoid referring non-static method inside main() static method
        AventCode12 instance = new AventCode12();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    List<Integer> emptyLine = new ArrayList<Integer>();
    List<Integer> emptyCol = new ArrayList<Integer>();

    public void process(List<String> lines) {
        long value = 0;

        for (String line : lines) {
            value += getNumbArrang(line);
        }

        System.out.println("Value: " + value);
    }

    public long getNumbArrang(String line) {
        long count = 0;
        List<Integer> damagedList = getGroupDamaged(line);
        String searchLine = line.trim().split(" ")[0].trim();

        long nbDameToFind = getNumDamager(damagedList);
        long nbCurrentDamage = getNumDamager(searchLine);
        if (nbCurrentDamage == nbDameToFind) {
            count = 1;
            System.out.println(count);
            return count;
        }

        count += find(searchLine, nbDameToFind, damagedList);
        System.out.println(count);
        return count;
    }

    public long find(final String line, long toFind, List<Integer> damagedList) {
        
        int posList = 0;

        char old = line.charAt(0);
        int dam = 0;
        int damTotal = 0;
        if (old == '#') {
            dam = 1;
            damTotal = 1;
        }

        if (old == '?') {
            long result = 0;
            result += find(substitute(line, 0, '.'), toFind, damagedList);
            result += find(substitute(line, 0, '#'), toFind, damagedList);
            return result;
        }

        for (int i = 1; i < line.length(); i++) {
            char current = line.charAt(i);
            if (current == '.' && old == '.') {
                dam = 0;
                old = current;
                continue;
            }
            if (current == '.' && old == '#') {
                Integer currentSearch = damagedList.get(posList);

                if (currentSearch != null && currentSearch.intValue() == dam) {
                    posList++;
                    dam = 0;
                    old = current;
                    continue;
                } else {
                    return 0;
                }

            }
            if (current == '#') {
                dam++;
                damTotal++;
                if (damTotal > toFind)
                    return 0;
                if (posList >= damagedList.size()) {
                    return 0;
                }
                Integer currentSearch = damagedList.get(posList);
                if (currentSearch == null || currentSearch.intValue() < dam) {
                    return 0;
                }
                old = current;
            }
            if (current == '?') {
                long result = 0;
                if (old == '#') {
                    if (dam + 1 > damagedList.get(posList)) {
                        result += find(substitute(line, i, '.'), toFind, damagedList);
                    } else {
                        result += find(substitute(line, i, '.'), toFind, damagedList);
                        result += find(substitute(line, i, '#'), toFind, damagedList);
                    }
                } else {
                    result += find(substitute(line, i, '.'), toFind, damagedList);
                    result += find(substitute(line, i, '#'), toFind, damagedList);
                }
                return result;
            }

        }

        if (old == '#') {
            if (posList >= damagedList.size()) {
                return 0;
            }
            Integer currentSearch = damagedList.get(posList);
            if (currentSearch == null || currentSearch.intValue() != dam) {
                return 0;
            }
        }
        if (damTotal == toFind) return 1 ;
        
        return 0;
    }

    public String substitute(final String str, int index, char ch) {
        if (index == 0) {
            return ch + str.substring(index + 1);
        }
        if (index == str.length() - 1) {
            return str.substring(0, index) + ch;
        }
        return str.substring(0, index) + ch
                + str.substring(index + 1);
    }

    public long getNumDamager(String line) {
        long result = 0;

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '#') {
                result++;
            }
        }
        return result;
    }

    public long getNumDamager(List<Integer> list) {
        long result = 0;

        for (Integer i : list) {
            result += i.intValue();
        }
        return result;
    }

    public List<Integer> getGroupDamaged(String line) {
        List<Integer> result = new ArrayList<Integer>();
        int pos = line.indexOf(" ");
        String temp = line.substring(pos, line.length());
        String[] array = temp.trim().split(",");

        List<String> tempList = Arrays.asList(array);
        for (String s : tempList) {
            result.add(Integer.valueOf(s));
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

    public class Position {
        int line;
        int pos;

        public Position(int line, int pos) {
            this.line = line;
            this.pos = pos;
        }
    }
}
