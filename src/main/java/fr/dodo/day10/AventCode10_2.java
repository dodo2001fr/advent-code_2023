package fr.dodo.day10;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode10_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "input10.txt";
        //fileName = "input10.txt";

        // To avoid referring non-static method inside main() static method
        AventCode10_2 instance = new AventCode10_2();

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

    int maxNorth = 0;
    int maxSouth = 0;
    int maxEast = 0;
    int maxWest = 0;


    public void process(List<String> lines) {

        maxLine = lines.size();
        maxPos = lines.get(0).length();
        Position posS = getS(lines);
        Position pos1 = getNexPosition(lines, posS, null);
       // pos1 = getNexPosition(lines, posS,pos1);
        String from = pos1.from;

        Position old1 = posS;

        maxNorth = posS.line;
        maxSouth = posS.line;
        maxEast = posS.pos;
        maxWest = posS.pos;
       
        Map<String,Position> pipe = new HashMap<String,Position>();
        pipe.put(pos1.line+"-"+pos1.pos,pos1);

        boolean finish = false;
        int turnleft = 0;
        while (!finish) {
            Position temp1 = pos1;
            pos1 = getNexPosition(lines, pos1, old1);
            
            turnleft = turnleft + isTurnLeft(lines, pos1, old1);

            if (isEquals(pos1, posS)) {
                finish = true;
                pos1.from2 = from;
            }
            pipe.put(pos1.line+"-"+pos1.pos,pos1);
            if (pos1.line < maxNorth)
                maxNorth = pos1.line;            
            if (pos1.line > maxSouth)
                maxSouth = pos1.line;
            if (pos1.pos > maxEast)
                maxEast = pos1.pos;
            if (pos1.pos < maxWest)
                maxWest = pos1.pos;
            
            old1 = temp1;

        }
        boolean isLeft = turnleft < 0 ? true : false;

        System.out.println("N: " + maxNorth + " - S: " + maxSouth  + " - W: " + maxWest + " - E: " + maxEast);
        System.out.println("Max LS: " + maxLine + " - LW: " + maxPos);

        long enclosed = 0;
        for (int i = maxNorth+1; i < maxSouth; i++) {
            for (int j = maxWest+1; j < maxEast; j++) {
                Position search = new Position(i, j);
                if (pipe.get(search.line+"-"+search.pos) != null) continue;

                boolean find = false;
                int count = 1;
                while(!find){
                    Position temp = null;
 
                    //E
                    temp = pipe.get(search.line+"-"+(search.pos + count));
                    if (temp != null){
                        if (temp.c == '|'){
                            if (temp.from.equals("N") && !isLeft) enclosed ++; 
                            if (temp.from.equals("S") && isLeft) enclosed ++; 
                        }
                        if (temp.c == 'F'){
                            if (temp.from.equals("S") && isLeft) enclosed ++; 
                            if (temp.from.equals("E") && !isLeft) enclosed ++; 
                        }
                        if (temp.c == 'L'){
                            if (temp.from.equals("N") && !isLeft) enclosed ++; 
                            if (temp.from.equals("E") && isLeft) enclosed ++; 
                        }
                        if (temp.c == 'S'){
                            if (temp.from.equals("N") && !isLeft) enclosed ++;
                            if (temp.from.equals("S") && isLeft) enclosed ++;
                            if (temp.from.equals("E")) {
                                if (temp.from2.equals("S") && isLeft) enclosed ++;
                                if (temp.from2.equals("N") && !isLeft) enclosed ++;
                            }
                        }
                        find = true;
                        continue;
                    }

                    //W
                    temp = pipe.get(search.line+"-"+(search.pos-count));
                    if (temp != null){
                        if (temp.c == '|'){
                            if (temp.from.equals("N") && isLeft) enclosed ++; 
                            if (temp.from.equals("S") && !isLeft) enclosed ++; 
                        }
                        if (temp.c == '7'){
                            if (temp.from.equals("S") && !isLeft) enclosed ++; 
                            if (temp.from.equals("W") && isLeft) enclosed ++; 
                        }
                        if (temp.c == 'J'){
                            if (temp.from.equals("N") && isLeft) enclosed ++; 
                            if (temp.from.equals("W") && !isLeft) enclosed ++; 
                        }
                        if (temp.c == 'S'){
                            if (temp.from.equals("N") && isLeft) enclosed ++;
                            if (temp.from.equals("S") && !isLeft) enclosed ++;
                            if (temp.from.equals("W")) {
                                if (temp.from2.equals("S") && !isLeft) enclosed ++;
                                if (temp.from2.equals("N") && isLeft) enclosed ++;
                            }
                        }
                        find = true;
                        continue;
                    }

                   //N
                    temp = pipe.get((search.line-count)+"-"+search.pos);
                    if (temp != null){
                        if (temp.c == '-'){
                            if (temp.from.equals("W") && !isLeft) enclosed ++; 
                            if (temp.from.equals("E") && isLeft) enclosed ++; 
                        }
                        if (temp.c == 'L'){
                            if (temp.from.equals("N") && !isLeft) enclosed ++; 
                            if (temp.from.equals("E") && isLeft) enclosed ++; 
                        }
                        if (temp.c == 'J'){
                            if (temp.from.equals("N") && isLeft) enclosed ++; 
                            if (temp.from.equals("W") && !isLeft) enclosed ++; 
                        }
                        if (temp.c == 'S'){
                            if (temp.from.equals("W") && !isLeft) enclosed ++;
                            if (temp.from.equals("E") && isLeft) enclosed ++;
                            if (temp.from.equals("N")) {
                                if (temp.from2.equals("W") && !isLeft) enclosed ++;
                                if (temp.from2.equals("E") && isLeft) enclosed ++;
                            }
                        }
                        find = true;
                        continue;
                    }

                    //S
                    temp = pipe.get((search.line+count)+"-"+search.pos);
                    if (temp != null){
                        if (temp.c == '-'){
                            if (temp.from.equals("W") && isLeft) enclosed ++; 
                            if (temp.from.equals("E") && !isLeft) enclosed ++; 
                        }
                        if (temp.c == 'F'){
                            if (temp.from.equals("S") && isLeft) enclosed ++; 
                            if (temp.from.equals("E") && !isLeft) enclosed ++; 
                        }
                        if (temp.c == '7'){
                            if (temp.from.equals("S") && !isLeft) enclosed ++; 
                            if (temp.from.equals("W") && isLeft) enclosed ++; 
                        }
                        if (temp.c == 'S'){
                            if (temp.from.equals("W") && isLeft) enclosed ++;
                            if (temp.from.equals("E") && !isLeft) enclosed ++;
                            if (temp.from.equals("S")) {
                                if (temp.from2.equals("W") && isLeft) enclosed ++;
                                if (temp.from2.equals("E") && !isLeft) enclosed ++;
                            }
                        }
                        find = true;
                        continue;
                    }

                    count++;
                    
                    /* | is a vertical pipe connecting north and south.
         * - is a horizontal pipe connecting east and west.
         * L is a 90-degree bend connecting north and east.
         * J is a 90-degree bend connecting north and west.
         * 7 is a 90-degree bend connecting south and west.
         * F is a 90-degree bend connecting south and east.
         * . is ground; there is no pipe in this tile.
         * S is the starting position of the animal; there is a pipe on this*/
                }
              
            }
        }

        System.out.println("Value: " + enclosed);
    }

    public char[] NORTH = { 'L', 'J', '|', 'S' };
    public char[] SOUTH = { '|', '7', 'F', 'S' };
    public char[] EAST = { '-', 'L', 'F', 'S' };
    public char[] WEST = { '-', 'J', '7', 'S' };

    // 1 yes , 0 go ahead, -1 turn right
    public int isTurnLeft(List<String> lines, Position currentPos, Position oldPos) {
        char cur = getChar(currentPos, lines);

        if (cur == 'L') {
            if (currentPos.line > oldPos.line)
                return 1;
            else
                return -1;
        }
        if (cur == 'J') {
            if (currentPos.line > oldPos.line)
                return -1;
            else
                return 1;
        }

        if (cur == '7') {
            if (currentPos.line < oldPos.line)
                return 1;
            else
                return -1;
        }
        if (cur == 'F') {

            if (currentPos.line < oldPos.line)
                return -1;
            else
                return 1;
        }

        return 0;
    }


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
                    temp.from="S";
                    temp.c=tempChar;
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
                    temp.from="W";
                    temp.c=tempChar;
                    return temp;
                }
            }
        }

        // DOWN SOUTH
        if (isArrayContain(SOUTH, currentChar)) {
            Position temp = new Position(current.line + 1, current.pos);
            if (temp.line < maxLine && !isEquals(temp, from)) {
                char tempChar = getChar(temp, lines);
                // NORTH
                if (isArrayContain(NORTH, tempChar)) {
                    temp.from="N";
                    temp.c=tempChar;
                    return temp;
                }
            }
        }

        // LEFT WEST
        if (isArrayContain(WEST, currentChar)) {
            Position temp = new Position(current.line, current.pos - 1);
            if (temp.pos >= 0 && !isEquals(temp, from)) {
                char tempChar = getChar(temp, lines);
                // EAST
                if (isArrayContain(EAST, tempChar)) {
                    temp.from="E";
                    temp.c=tempChar;
                    return temp;
                }
            }
        }
        return null;
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

        String from = null;
        String from2 = null;
        char c;

        public Position(int line, int pos) {
            this.line = line;
            this.pos = pos;
        }
    }
}
