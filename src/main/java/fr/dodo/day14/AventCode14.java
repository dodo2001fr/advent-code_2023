package fr.dodo.day14;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode14 {

    public static void main(String[] args) throws IOException {
        String fileName = "input14.txt";

        // To avoid referring non-static method inside main() static method
        AventCode14 instance = new AventCode14();

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

        long maxLine = lines.size();

        List<String> colums = getColums(lines, 0, lines.size() - 1);
        value = calculateLoad(moveRock(colums), maxLine);

        System.out.println("Value: " + value);
    }

    public List<String> moveRock(List<String> colums) {
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

    public long calculateLoad(List<String> colums, long maxLines) {
        long result = 0;
        for (String col : colums) {
            for (int i = 0; i < col.length(); i++) {
                if (col.charAt(i) == 'O') {
                    result += maxLines - i;
                }
            }
        }

        return result;
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
