package fr.dodo.day13;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode13_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "input13.txt";

        // To avoid referring non-static method inside main() static method
        AventCode13_2 instance = new AventCode13_2();

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

        List<String> rowList = new ArrayList<String>();
        int minRow = 0;
        for (int i = 0; i < lines.size(); i++) {
            int maxRow = i;
            String currentRow = lines.get(i);
            if (!currentRow.trim().isEmpty()) {
                rowList.add(lines.get(i));
            } else {
                maxRow = i-1;
            }

            if (currentRow.trim().isEmpty() || i == lines.size() - 1) {

                long count = getMirror(rowList);
                long countCol = 0;

                List<String> col = getColums(lines, minRow, maxRow);
                countCol = getMirror(col);

                if (count == 0 && countCol == 0) {
                    System.out.println("null " + i);
                }

                value += count * 100;
                value += countCol;
                rowList = new ArrayList<String>();
                minRow = i + 1;
            }
        }

        System.out.println("Value: " + value);

    }

    public List<String> getColums(List<String> lines, int min, int max) {

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < lines.get(min).length(); i++) {
            String colStr = "";
            for (int k = min; k <= max; k++) {
                colStr = colStr + lines.get(k).charAt(i);
            }
            list.add(colStr);
        }
        return list;
    }

    public long getMirror(List<String> rowList) {
        long value = 0;

        String previous = rowList.get(0);
        for (int i = 1; i < rowList.size(); i++) {

            if (getNumdiif(previous, rowList.get(i)) == 1) {
                if (isRefelect(rowList, i)) {
                    return i;
                }

            } else if (previous.equals(rowList.get(i))) {
                for (int k = 1; k <= i; k++) {
                    int up = i - k - 1;
                    int down = i + k;
                    if (up < 0)
                        break;
                    if (down >= rowList.size())
                        break;
                    if (rowList.get(down).trim().isEmpty())
                        break;

                    if (getNumdiif(rowList.get(up), rowList.get(down)) == 1) {
                        if (isRefelect(rowList, i, up))
                            return i;
                    }
                }
            }
            previous = rowList.get(i);
        }
        return value;
    }

    public boolean isRefelect(List<String> rowList, int line) {
        return isRefelect(rowList, line, -1);
    }

    public boolean isRefelect(List<String> rowList, int line, int smudgeLine) {
        boolean isReflection = true;
        for (int k = 1; k <= line; k++) {
            int up = line - k - 1;
            int down = line + k;
            if (up < 0)
                break;
            if (down >= rowList.size())
                break;
            if (rowList.get(down).trim().isEmpty())
                break;
            if (smudgeLine == up || smudgeLine == down) {
                continue;
            } else {
                if (!rowList.get(up).equals(rowList.get(down))) {
                    isReflection = false;
                    break;
                }
            }
        }
        return isReflection;
    }

    public int getNumdiif(String s1, String s2) {
        int count = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i))
                count++;
        }
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

    public class Position {
        int line;
        int pos;

        public Position(int line, int pos) {
            this.line = line;
            this.pos = pos;
        }
    }
}
