package fr.dodo.day11;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode11_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "input11.txt";

        // To avoid referring non-static method inside main() static method
        AventCode11_2 instance = new AventCode11_2();

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
        BigInteger value = BigInteger.valueOf(0);

        List<Position> posList = new ArrayList<Position>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            boolean isEmptyLine = true;
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if (c == '#') {
                    isEmptyLine = false;
                    posList.add(new Position(i, j));
                }
            }
            if (isEmptyLine) {
                emptyLine.add(Integer.valueOf(i));
            }
        }

        int maxCol = lines.get(0).length();

        for (int i = 0; i < maxCol; i++) {
            boolean isEmptyCol = true;
            for (int j = 0; j < lines.size(); j++) {
                if (lines.get(j).charAt(i) == '#') {
                    isEmptyCol = false;
                    break;
                }
            }
            if (isEmptyCol) {
                emptyCol.add(Integer.valueOf(i));
            }
        }

        for (int i = 0; i < posList.size(); i++) {
            Position point1 = posList.get(i);
            for (int j = i + 1; j < posList.size(); j++) {
                Position point2 = posList.get(j);
                 
                BigInteger addLine = getAddLine(point1.line, point2.line);
                BigInteger addCol = getAddCol(point1.pos, point2.pos);
                BigInteger sum = addLine.add(addCol);

                long num = 0;
                if (point1.line < point2.line){
                    num += point2.line - point1.line;
                } else {
                    num += point1.line - point2.line;
                }
                if (point1.pos < point2.pos){
                    num += point2.pos - point1.pos;
                } else {
                    num += point1.pos - point2.pos;
                }
                
                sum = sum.add(BigInteger.valueOf(num));
                //System.out.println(point1.line + "/" + point1.pos + " - " + point2.line + "/" + point2.pos +" : " + num);
                value = value.add(sum);
            }
            
        }

        System.out.println("Value: " + value);
    }

    public BigInteger getAddLine(int line1, int line2){
        if (line1 > line2){
            int temp = line2;
            line2 = line1;
            line1 = temp;
        }
        BigInteger count = BigInteger.valueOf(0);
        for (int i = line1+1; i< line2; i++){
            if (emptyLine.contains(Integer.valueOf(i))) count = count.add(BigInteger.valueOf(1000000-1));
        }

        return count;
    }

    public BigInteger getAddCol(int col1, int col2){
        if (col1 > col2){
            int temp = col2;
            col2 = col1;
            col1 = temp;
        }
        BigInteger count = BigInteger.valueOf(0);
        for (int i = col1+1; i< col2; i++){
            if (emptyCol.contains(Integer.valueOf(i))) count = count.add(BigInteger.valueOf(1000000-1));
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
