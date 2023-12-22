package fr.dodo.day14;

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

public class AventCode14_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "input14.txt";

        // To avoid referring non-static method inside main() static method
        AventCode14_2 instance = new AventCode14_2();

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
        Map<String, Long> cache = new HashMap<String, Long>();

        long numCycle = 1;

        long CYCLE = 1000000000;

        for (numCycle = 1; numCycle <= CYCLE; numCycle++) {

            lines = runCycle(lines);
            if (cache.get(lines.toString()) != null) {
               break;
            }
            cache.put(lines.toString(), Long.valueOf(numCycle));

        }
        if (numCycle != CYCLE) {

            long firstCycle = cache.get(lines.toString()).longValue();
            long remainder = (CYCLE - firstCycle) % (numCycle - firstCycle);

            System.out.println(
                    "Cycle: " + CYCLE + " - " + numCycle + " | " + remainder + " - " + cache.get(lines.toString()));
            for (int i = 1; i <= remainder; i++) {
                lines = runCycle(lines);
            }
        }
        value = calculateLoad(lines);
        System.out.println("Value: " + value);
    }

    public List<String> runCycle(final List<String> lines) {
        List<String> north = moveRock(getColums(lines));
        List<String> west = moveRock(getColums(north));
        List<String> south = moveRock(getColumsInverseCol(west));
        List<String> east = moveRock(getColumsInverseAll(south));
        return getInverseLine(east);
    }

    public List<String> moveRock(final List<String> colums) {
        List<String> colMove = new ArrayList<String>();
        for (String col : colums) {
            colMove.add(moveRock(col.toCharArray()));
        }
        return colMove;
    }

    public String moveRock(char[] charArray) {

        char old = charArray[0];
        for (int i = 1; i < charArray.length; i++) {
            if (old == '.' && charArray[i] == 'O') {
                charArray[i - 1] = 'O';
                charArray[i] = '.';
                return moveRock(charArray);
            }
            old = charArray[i];

        }

        return String.valueOf(charArray);

    }

    public long calculateLoad(List<String> lines) {
        long result = 0;
        long maxLines = lines.size();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == 'O') {
                    result += maxLines - i;
                }
            }
        }

        return result;
    }

    public List<String> getInverseLine(List<String> lines) {

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < lines.size(); i++) {

            String colStr = "";
            for (int k = lines.get(i).length() - 1; k >= 0; k--) {
                colStr = colStr + lines.get(i).charAt(k);
            }
            list.add(colStr);
        }
        return list;
    }

    public List<String> getColumsInverseCol(List<String> lines) {

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < lines.get(0).length(); i++) {
            String colStr = "";
            for (int k = lines.size() - 1; k >= 0; k--) {
                colStr = colStr + lines.get(k).charAt(i);
            }
            list.add(colStr);
        }
        return list;
    }

    public List<String> getColumsInverseAll(List<String> lines) {

        List<String> list = new ArrayList<String>();

        for (int i = lines.get(0).length() - 1; i >= 0; i--) {
            String colStr = "";
            for (int k = lines.size() - 1; k >= 0; k--) {
                colStr = colStr + lines.get(k).charAt(i);
            }
            list.add(colStr);
        }
        return list;
    }

    public List<String> getColums(List<String> lines) {

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < lines.get(0).length(); i++) {
            String colStr = "";
            for (int k = 0; k < lines.size(); k++) {
                colStr = colStr + lines.get(k).charAt(i);
            }
            list.add(colStr);
        }
        return list;
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
