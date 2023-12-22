package fr.dodo.day12;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode12_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "input12.txt";

        // To avoid referring non-static method inside main() static method
        AventCode12_2 instance = new AventCode12_2();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    public void process(List<String> lines) {
        long value = 0;

        for (String line : lines) {
            findCache = new HashMap<String, Long>();
            value += getNumArrangByLine(line);

        }

        System.out.println("Value: " + value);
    }

    public static int ADD = 5;
    public Map<String, Long> findCache = new HashMap<String, Long>();

    public long getNumArrangByLine(String line) {
        long result = 0;
        List<Integer> tmpDamagedList = getGroupDamaged(line);
        List<Integer> damagedList = new ArrayList<Integer>();
        String searchLineTp = line.trim().split(" ")[0].trim();
        String searchLine = searchLineTp;
        damagedList.addAll(tmpDamagedList);

        for (int i = 1; i < ADD; i++) {

            searchLine = searchLine + "?" + searchLineTp;
            damagedList.addAll(tmpDamagedList);
        }

        result = getNumArrange(searchLine, damagedList);

        System.out.println(result);
        return result;
    }

    public long getNumArrange(String searchLine, List<Integer> damagedList) {

        long count = 0;

        long nbDameToFind = getNumDamager(damagedList);

        char old = searchLine.charAt(0);
        if (old == '?') {

            count += find(substitute(searchLine, 0, '.'), nbDameToFind, damagedList);
            count += find(substitute(searchLine, 0, '#'), nbDameToFind, damagedList);

        } else {
            count += find(searchLine, nbDameToFind, damagedList);
        }

        return count;

    }

    public long find(final String line, long toFind, List<Integer> damagedList) {
        String key = line + damagedList.toString();
        Long cache = findCache.get(key);
        if (cache != null)
            return cache.longValue();

        int posList = 0;

        char old = line.charAt(0);
        int dam = 0;
        int damTotal = 0;
        if (old == '#') {
            dam = 1;
            damTotal = 1;
        }

        for (int i = 1; i < line.length(); i++) {
            char current = line.charAt(i);
            if (current == '.' && old == '.') {
                continue;
            }
            if (current == '.' && old == '#') {
                if (posList >= damagedList.size()) {
                    findCache.put(key, Long.valueOf(0));
                    return 0;
                }
                Integer currentSearch = damagedList.get(posList);

                if (currentSearch != null && currentSearch.intValue() == dam) {
                    posList++;
                    if (i == line.length() - 1) {
                        if (posList == damagedList.size()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }

                    String temp = line.substring(i + 1);
                    List<Integer> tempList = damagedList.subList(posList, damagedList.size());
                    return getNumArrange(temp, tempList);

                    // continue;
                } else {
                    findCache.put(key, Long.valueOf(0));
                    return 0;
                }

            }
            if (current == '#') {
                dam++;
                damTotal++;
                if (damTotal > toFind) {
                    findCache.put(key, Long.valueOf(0));
                    return 0;
                }
                if (posList >= damagedList.size()) {
                    findCache.put(key, Long.valueOf(0));
                    return 0;
                }
                Integer currentSearch = damagedList.get(posList);
                if (currentSearch == null || currentSearch.intValue() < dam) {
                    findCache.put(key, Long.valueOf(0));
                    return 0;
                }
                old = current;
            }
            if (current == '?') {
                long result = 0;
                if (old == '#') {
                    if (posList >= damagedList.size()) {
                        findCache.put(key, Long.valueOf(0));
                        return 0;
                    }
                    if (dam == damagedList.get(posList)) {
                        posList++;
                        if (i == line.length() - 1) {
                            if (posList == damagedList.size()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }

                        String temp = line.substring(i + 1);
                        List<Integer> tempList = damagedList.subList(posList, damagedList.size());
                        return getNumArrange(temp, tempList);
                    } else {
                        if (toFind > damTotal) {
                            result += find(substitute(line, i, '#'), toFind, damagedList);
                        } else {
                            findCache.put(key, Long.valueOf(0));
                            return 0;
                        }
                    }
                } else {
                    result += find(substitute(line, i, '.'), toFind, damagedList);
                    if (toFind > damTotal)
                        result += find(substitute(line, i, '#'), toFind, damagedList);
                }
                findCache.put(key, Long.valueOf(result));
                return result;
            }

        }

        if (old == '#') {
            if (posList >= damagedList.size()) {
                findCache.put(key, Long.valueOf(0));
                return 0;
            }
            Integer currentSearch = damagedList.get(posList);
            if (currentSearch == null || currentSearch.intValue() != dam) {
                findCache.put(key, Long.valueOf(0));
                return 0;
            }
        }
        if (damTotal == toFind) {
            findCache.put(key, Long.valueOf(1));
            return 1;
        }

        findCache.put(key, Long.valueOf(0));
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
        return getnumbChar(line, '#');
    }

    public long getnumQuoteMark(String line) {
        return getnumbChar(line, '?');
    }

    public long getnumbChar(String line, char c) {
        long result = 0;

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == c) {
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
