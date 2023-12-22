package fr.dodo.day10;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode10 {

    public static void main(String[] args) throws IOException {
        String fileName = "input10.txt";

        // To avoid referring non-static method inside main() static method
        AventCode10 instance = new AventCode10();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    int pos = 1;
    int maxLine = 0;
    int maxPos = 0;

    public void process(List<String> lines) {
        long value = 0;

        maxLine = lines.size();
        maxPos = lines.get(0).length();
        Position posS = getS(lines);
        Position pos1 = getNexPosition(lines, posS, null);
        Position pos2 = getNexPosition(lines, posS, pos1);
        long nb1 = 1;
        long nb2 = 1;
        Position old1 = posS;
        Position old2 = posS;

        boolean finish = false;
        while (!finish) {
            Position temp1 = pos1;
            Position temp2 = pos2;
            pos1 = getNexPosition(lines, pos1, old1);
            pos2 = getNexPosition(lines, pos2, old2);
            nb1++;
            nb2++;
            if (isEquals(pos1, pos2)) {
                finish = true;
            }
            if (isEquals(pos1, old2) || isEquals(pos2, old1)) {
                finish = true;
                nb1--;
                nb2--;
            }
            old1 = temp1;
            old2 = temp2;
          
        }

        if (nb1 < nb2) {
            value = nb1;
        } else if (nb2 < nb1) {
            value = nb2;
        } else {
            value = nb1;
        }

        System.out.println("Value: " + value);
    }

    public char[] NORTH = { 'L', 'J', '|','S' };
    public char[] SOUTH = { '|', '7', 'F','S' };
    public char[] EAST = { '-', 'L', 'F','S' };
    public char[] WEST = { '-', 'J', '7','S' };

    public Position getNexPosition(List<String> lines, Position current, Position from) {
        /*
         * | is a vertical pipe connecting north and south.
         * - is a horizontal pipe connecting east and west.
         * L is a 90-degree bend connecting north and east.
         * J is a 90-degree bend connecting north and west.
         * 7 is a 90-degree bend connecting south and west.
         * F is a 90-degree bend connecting south and east.
         * . is ground; there is no pipe in this tile.
         * S is the starting position of the animal; there is a pipe on this
         */

        char currentChar = getChar(current, lines);
        // Up
        if (isArrayContain(NORTH, currentChar)) {
            Position temp = new Position(current.line - 1, current.pos);
            if (temp.line >= 0 && !isEquals(temp, from)) {
                char tempChar = getChar(temp, lines);
                if (isArrayContain(SOUTH, tempChar)) {
                    return temp;
                }
            }
        }

        // RIGHT EAST
        if (isArrayContain(EAST, currentChar)) {
            Position temp = new Position(current.line, current.pos + 1);
            if (temp.pos < maxPos && !isEquals(temp, from)) {
                char tempChar = getChar(temp, lines);
                // WEST
                if (isArrayContain(WEST, tempChar)) {
                    return temp;
                }
            }
        }

        // DOWN SOUTH
        if (isArrayContain(SOUTH, currentChar)) {
            Position temp = new Position(current.line + 1, current.pos);
            if (temp.line < maxLine && !isEquals(temp, from)) {
                char tempChar = getChar(temp, lines);
                // WEST
                if (isArrayContain(NORTH, tempChar)) {
                    return temp;
                }
            }
        }

        // LEFT WEST
        if (isArrayContain(WEST, currentChar)) {
            Position temp = new Position(current.line, current.pos - 1);
            if (temp.pos >= 0 && !isEquals(temp, from)) {
                char tempChar = getChar(temp, lines);
                // WEST
                if (isArrayContain(EAST, tempChar)) {
                    return temp;
                }
            }
        }
        return null;
    }

    public List<Position> getStartPoint(List<String> lines, Position s) {
        List<Position> result = new ArrayList<Position>();
        Position pos1 = getNexPosition(lines, s, null);
        result.add(pos1);
        Position pos2 = getNexPosition(lines, s, pos1);
        result.add(pos2);
        return result;

    }

    public boolean isArrayContain(char[] array, char c) {
        for (char x : array) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }

    public boolean isEquals(Position a, Position b) {
        if (a == null || b == null)
            return false;
        if (a.line == b.line && a.pos == b.pos)
            return true;
        return false;
    }

    public char getChar(Position position, List<String> lines) {
        return lines.get(position.line).charAt(position.pos);

    }

    public Position getS(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == 'S')
                    return new Position(i, j);
            }
        }
        return null;
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
