package fr.dodo.day18;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode18_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "test18.txt";

        // To avoid referring non-static method inside main() static method
        AventCode18_2 instance = new AventCode18_2();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    public Position start;
    public boolean[][] grid;
    public int pgcd = 0;
    public int pgcdLine = 0;
    public int pgcdCol = 0;

    public void process(List<String> lines) {
        long value = 0;

        initiateGrid(lines);
        construct(lines);

        fillInAll();
        // writeFile();
        value = calculateCubic();

        System.out.println("Value: " + value);
    }

    public long calculateCubic() {
        long result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }

    public void fillInAll() {
        Position firstIn = null;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (inGrid(j, i)) {
                    firstIn = new Position(j, i);
                    break;
                }
            }
        }
        fillIn(firstIn);

        System.out.println("FillIn end");
    }

    public void fillIn(Position p) {
        try {
            if (p.x < 0 || p.x >= grid[0].length || p.y < 0 || p.y >= grid.length)
                return;

            if (grid[p.y][p.x])
                return;

            grid[p.y][p.x] = true;

            fillIn(new Position(p.x, p.y - 1));
            fillIn(new Position(p.x, p.y + 1));
            fillIn(new Position(p.x - 1, p.y));
            fillIn(new Position(p.x + 1, p.y));
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }

    }

    public boolean inGrid(int x, int y) {
        if (grid[y][x])
            return false;

        if (y == 0 || y == grid.length - 1 || x == 0 || x == grid[0].length - 1)
            return false;

        Position wall = getNearestWall(x, y);
        if (wall == null)
            return false;
        return isInGrid(x, y, wall);

    }

    public boolean isInGrid(int x, int y, Position wall) {

        int count = 1;
        boolean previous = grid[wall.y][wall.x];

        if (x == wall.x) {
            if (y < wall.y) {
                for (int i = wall.y + 1; i < grid.length; i++) {
                    boolean current = grid[i][x];
                    if (current && previous) {
                        return false;
                    }
                    if (current && !previous) {
                        count++;
                    }
                    previous = current;

                }
                if (count % 2 == 0)
                    return false;
                else
                    return true;
            }

            if (y > wall.y) {
                for (int i = wall.y - 1; i >= 0; i--) {
                    boolean current = grid[i][x];
                    if (current && previous) {
                        return false;
                    }
                    if (current && !previous) {
                        count++;
                    }
                    previous = current;

                }
                if (count % 2 == 0)
                    return false;
                else
                    return true;
            }
        }

        if (y == wall.y) {

            if (x < wall.x) {
                for (int i = wall.x + 1; i < grid[y].length; i++) {
                    boolean current = grid[y][i];
                    if (current && previous) {
                        return false;
                    }
                    if (current && !previous) {
                        count++;
                    }
                    previous = current;

                }
                if (count % 2 == 0)
                    return false;
                else
                    return true;
            }

            if (y > wall.y) {
                for (int i = wall.x - 1; i >= 0; i--) {
                    boolean current = grid[y][i];
                    if (current && previous) {
                        return false;
                    }
                    if (current && !previous) {
                        count++;
                    }
                    previous = current;

                }
                if (count % 2 == 0)
                    return false;
                else
                    return true;
            }

        }

        return true;
    }

    public Position getNearestWall(int x, int y) {
        boolean find = false;
        int count = 1;
        int wallx = x;
        int wally = y;
        Position firstFind = null;
        boolean uFind = false;
        boolean dFind = false;
        boolean lFind = false;
        boolean rFind = false;
        while (!find) {
            // UP
            if (!uFind && wally - count >= 0 && grid[wally - count][x]) {
                uFind = true;
                if (firstFind == null)
                    firstFind = new Position(x, wally - count);
            }

            // DOWN
            if (!dFind && wally + count < grid.length && grid[wally + count][x]) {
                dFind = true;
                if (firstFind == null)
                    firstFind = new Position(x, wally + count);
            }

            // LEFT
            if (!lFind && wallx - count >= 0 && grid[y][wallx - count]) {
                lFind = true;
                if (firstFind == null)
                    firstFind = new Position(wallx - count, y);
            }

            // RIGHT
            if (!rFind && wallx + count < grid[y].length && grid[y][wallx + count]) {
                rFind = true;
                if (firstFind == null)
                    firstFind = new Position(wallx + count, y);
            }
            if (uFind && dFind && lFind && rFind)
                return firstFind;
            count++;
            if (count >= grid.length && count >= grid[y].length)
                find = true;

        }
        if (!uFind || !dFind || !rFind || !lFind) {
            return null;
        }
        return firstFind;

    }

    public void construct(List<String> lines) {

        Position oldPos = new Position(start.x, start.y);
        for (String line : lines) {
            Position newPos = getNewPosition(parseLine(line), oldPos);

            draw(newPos, oldPos);
            oldPos = newPos;
        }
        System.out.println("Construct end");
    }

    public void draw(Position from, Position to) {
        if (from.x == to.x) {
            int min = from.y;
            int max = to.y;
            if (from.y > to.y) {
                min = to.y;
                max = from.y;
            }
            for (int i = min; i <= max; i++) {
                grid[i][from.x] = true;

            }

        }
        if (from.y == to.y) {
            int min = from.x;
            int max = to.x;
            if (from.x > to.x) {
                min = to.x;
                max = from.x;
            }
            for (int i = min; i <= max; i++) {
                grid[from.y][i] = true;

            }
        }
    }

    public void initiateGrid(List<String> lines) {
        int minLine = 0;
        int maxLine = 0;
        int minCol = 0;
        int maxCol = 0;
        Position current = new Position(0, 0);

        for (String line : lines) {
            String[] parsedLine = parseLine(line);
            current = getNewPosition(parsedLine, current);
            if (minLine > current.y)
                minLine = current.y;
            if (maxLine < current.y)
                maxLine = current.y;
            if (minCol > current.x)
                minCol = current.x;
            if (maxCol < current.x)
                maxCol = current.x;
        }

        System.out.println("PGCD " + pgcd);
        System.out.println("PGCD " + pgcdLine + " " + pgcdCol);
        
        int numLine = maxLine - minLine;
        int numCol = maxCol - minCol;
        System.out.println("Size " + numLine + "-" + numCol);

        grid = new boolean[numLine][numCol];
        // grid = new ArrayList<String>();
        String initLine = "";
        for (int j = 0; j <= numCol; j++) {
            initLine = initLine + ".";
        }

        for (int i = 0; i <= numLine; i++) {
            for (int j = 0; j <= numCol; j++) {
                grid[i][j] = false;
            }
        }

        start = new Position(0, 0);
        if (minCol < 0)
            start.x = minCol * -1;
        if (minLine < 0)
            start.y = minLine * -1;

        System.out.println("init done");

    }

    public Position getNewPosition(String[] command, Position current) {
        String direction = command[0].trim();
        int meter = Integer.parseInt(command[1].trim());

        String hexa = command[2].trim();
        String meterStr = hexa.substring(2, 7);
        char directionStr = hexa.charAt(hexa.length() - 1);

        if (directionStr == '0')
            direction = "R";
        if (directionStr == '1')
            direction = "D";
        if (directionStr == '2')
            direction = "L";
        if (directionStr == '3')
            direction = "U";

        meter = Integer.parseInt(meterStr, 16);

        if (pgcd == 0)
            pgcd = meter;
        else
            pgcd = pgcd(pgcd, meter);

        if (direction.equals("U")) {
            if (pgcdLine == 0)
                pgcdLine = meter;
            else
                pgcdLine = pgcd(pgcdLine, meter);
            return new Position(current.x, current.y - meter);
        }
        if (direction.equals("D")) {
            if (pgcdLine == 0)
                pgcdLine = meter;
            else
                pgcdLine = pgcd(pgcdLine, meter);
            return new Position(current.x, current.y + meter);
        }
        if (direction.equals("R")) {
            if (pgcdCol == 0)
                pgcdCol = meter;
            else
                pgcdCol = pgcd(pgcdCol, meter);
            return new Position(current.x + meter, current.y);
        }
        if (direction.equals("L")) {
            if (pgcdCol == 0)
                pgcdCol = meter;
            else
                pgcdCol = pgcd(pgcdCol, meter);
            return new Position(current.x - meter, current.y);
        }
        return null;
    }

    public String[] parseLine(String line) {
        String[] parse = line.trim().split(" ");
        return parse;
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

    public int pgcd(int a, int b) {
        return (b == 0) ? a : pgcd(b, a % b);
    }

    public class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
