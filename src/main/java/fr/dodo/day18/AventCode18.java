package fr.dodo.day18;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode18 {

    public static void main(String[] args) throws IOException {
        String fileName = "input18.txt";

        // To avoid referring non-static method inside main() static method
        AventCode18 instance = new AventCode18();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    public Position start;
    public List<List<String>> grid;

    public void process(List<String> lines) {
        long value = 0;

        initiateGrid(lines);
        construct(lines);
        
        fillInAll();
        //writeFile();
        value = calculateCubic();

        System.out.println("Value: " + value);
    }

    public void writeFile() {
        File myObj = new File("c:\\TEMP\\18.txt");

        FileWriter myWriter;
        try {
            myObj.createNewFile();
            myWriter = new FileWriter("c:\\TEMP\\18.txt");
            for (List<String> line : grid) {
                String toInsert = "";
                for (String c : line) {
                    toInsert = toInsert + c;
                }
                myWriter.write(toInsert + "\n");
            }
            myWriter.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public long calculateCubic() {
        long result = 0;
        for (int i = 0; i < grid.size(); i++) {
            List<String> line = grid.get(i);
            for (int j = 0; j < line.size(); j++) {
                if (getValue(j,i).equals("#")) {
                    result++;
                }
            }

        }

        return result;
    }

    public void fillInAll() {
        Position firstIn = null;
        for (int i = 0; i < grid.size(); i++) {
            List<String> line = grid.get(i);
            for (int j = 0; j < line.size(); j++) {
                if (inGrid(j, i)) {
                    firstIn = new Position(j, i);
                    break;
                }
            }

        }
        fillIn(firstIn);
    }

    public void fillIn(Position p) {
        try{
        if (p.x < 0 || p.x >= grid.get(0).size() || p.y < 0 || p.y >= grid.size())
            return;

        String current = getValue(p);
        if (current.equals("#"))
            return;
        
        List<String> line = grid.get(p.y);
        line.set(p.x,"#");
        grid.set(p.y,line);
        
        fillIn(new Position(p.x, p.y - 1));
        fillIn(new Position(p.x, p.y + 1));
        fillIn(new Position(p.x - 1, p.y));
        fillIn(new Position(p.x + 1, p.y));
        } catch (Throwable e){
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }

    }

    public boolean inGrid(int x, int y) {
        String current = grid.get(y).get(x);
        if (current.equals("#"))
            return false;

        if (y == 0 || y == grid.size() - 1 || x == 0 || x == grid.get(0).size() - 1)
            return false;

        Position wall = getNearestWall(x, y);
        if (wall == null)
            return false;
        return isInGrid(x, y, wall);

    }

    public boolean isInGrid(int x, int y, Position wall) {

        int count = 1;
        String previous = getValue(wall.x, wall.y);

        if (x == wall.x) {
            if (y < wall.y) {
                for (int i = wall.y + 1; i < grid.size(); i++) {
                    String current = getValue(x, i);
                    if (current.equals("#") && previous.equals("#")) {
                        return false;
                    }
                    if (current.equals("#") && previous.equals(".")) {
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
                    String current = getValue(x, i);
                    if (current.equals("#") && previous.equals("#")) {
                        return false;
                    }
                    if (current.equals("#") && previous.equals(".")) {
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
                for (int i = wall.x + 1; i < grid.get(y).size(); i++) {
                    String current = getValue(i, y);
                    if (current.equals("#") && previous.equals("#")) {
                        return false;
                    }
                    if (current.equals("#") && previous.equals(".")) {
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
                    String current = getValue(i, y);
                    if (current.equals("#") && previous.equals("#")) {
                        return false;
                    }
                    if (current.equals("#") && previous.equals(".")) {
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

    public String getValue(Position current) {
        return grid.get(current.y).get(current.x);
    }

    public String getValue(int x, int y) {
        return grid.get(y).get(x);
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
            if (!uFind && wally - count >= 0 && grid.get(wally - count).get(x).equals("#")) {
                uFind = true;
                if (firstFind == null)
                    firstFind = new Position(x, wally - count);
            }

            // DOWN
            if (!dFind && wally + count < grid.size() && grid.get(wally + count).get(x).equals("#")) {
                dFind = true;
                if (firstFind == null)
                    firstFind = new Position(x, wally + count);
            }

            // LEFT
            if (!lFind && wallx - count >= 0 && grid.get(y).get(wallx - count).equals("#")) {
                lFind = true;
                if (firstFind == null)
                    firstFind = new Position(wallx - count, y);
            }

            // RIGHT
            if (!rFind && wallx + count < grid.get(y).size() && grid.get(y).get(wallx + count).equals("#")) {
                rFind = true;
                if (firstFind == null)
                    firstFind = new Position(wallx + count, y);
            }
            if (uFind && dFind && lFind && rFind)
                return firstFind;
            count++;
            if (count >= grid.size() && count >= grid.get(y).size())
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
                List<String> line = grid.get(i);
                line.set(from.x, "#");
                grid.set(i, line);
            }

        }
        if (from.y == to.y) {
            List<String> line = grid.get(from.y);
            int min = from.x;
            int max = to.x;
            if (from.x > to.x) {
                min = to.x;
                max = from.x;
            }
            for (int i = min; i <= max; i++) {
                line.set(i, "#");
            }
            grid.set(from.y, line);
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

        int numLine = maxLine - minLine;
        int numCol = maxCol - minCol;

        grid = new ArrayList<List<String>>();

        for (int i = 0; i <= numLine; i++) {
            List<String> line = new ArrayList<String>();

            for (int j = 0; j <= numCol; j++) {
                line.add(".");
            }
            grid.add(line);

        }

        start = new Position(0, 0);
        if (minCol < 0)
            start.x = minCol * -1;
        if (minLine < 0)
            start.y = minLine * -1;

    }

    public Position getNewPosition(String[] command, Position current) {
        String direction = command[0].trim();
        int meter = Integer.parseInt(command[1].trim());
        if (direction.equals("U")) {
            return new Position(current.x, current.y - meter);
        }
        if (direction.equals("D")) {
            return new Position(current.x, current.y + meter);
        }
        if (direction.equals("R")) {
            return new Position(current.x + meter, current.y);
        }
        if (direction.equals("L")) {
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

    public class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
