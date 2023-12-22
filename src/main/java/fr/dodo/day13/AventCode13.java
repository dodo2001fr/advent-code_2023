package fr.dodo.day13;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode13 {

    public static void main(String[] args) throws IOException {
        String fileName = "input13.txt";

        // To avoid referring non-static method inside main() static method
        AventCode13 instance = new AventCode13();

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

        int minRow = 0;

        String oldRow = "";
        int countRow = 0;
        int countCol = 0;

        for (int i = 0; i < lines.size(); i++) {

            String currentRow = lines.get(i);
            if (currentRow.equals(oldRow)) {
                boolean isReflection = true;
                for (int k = 1; k <= i; k++) {
                    int up = i - k - 1;
                    int down = i + k;
                    if (up < minRow)
                        break;
                    if (down >= lines.size())
                        break;
                    if (lines.get(down).trim().isEmpty())
                        break;
                    if (!lines.get(up).equals(lines.get(down))) {
                        isReflection = false;
                        break;
                    }
                }
                if (isReflection) {
                    countRow = i - minRow;
                }
            }
            oldRow = currentRow;

            if (currentRow.trim().isEmpty() || i == lines.size() - 1) {

                int lastLine = i - 1;
                if (i == lines.size() - 1) {
                    lastLine = i;
                }
                String oldCol = "";
                List<String> colList = new ArrayList<String>();
                for (int j = 0; j < lines.get(lastLine).length(); j++) {
                    String currentCol = getCol(lines, j, minRow, lastLine);
                    colList.add(currentCol);

                    if (currentCol.equals(oldCol) ) {
                        boolean isReflection = true;
                        for (int k = 1; k <= j; k++) {
                            int left = j - k - 1;
                            int right = j + k;
                            if (left < 0)
                                break;
                            if (right >= lines.get(lastLine).length())
                                break;
                            if (!colList.get(left).equals(getCol(lines, right, minRow, lastLine))) {
                                isReflection = false;
                                break;
                            }
                        }
                        if (isReflection) {
                            countCol = j;
                        }
                    }
                    oldCol = currentCol;

                }
                value += countCol;
                value += countRow * 100;
                countCol = 0;
                countRow = 0;
                minRow = i + 1;
                oldRow = "";
            }
        }

        System.out.println("Value: " + value);
    }

    public String getCol(List<String> lines, int col, int minRow, int lastLine) {
        String result = "";
        for (int k = minRow; k <= lastLine; k++) {
            result = result + lines.get(k).charAt(col);
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
