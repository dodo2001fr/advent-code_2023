package fr.dodo.day21;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode21 {

    public static void main(String[] args) throws IOException {
        String fileName = "input21.txt";

        // To avoid referring non-static method inside main() static method
        AventCode21 instance = new AventCode21();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    static int STEP = 64;
    public char[][] garden;

    public void process(List<String> lines) {
        long value = 0;

        long count = 1;

        Set<Position> startingPoint = new HashSet<Position>();
        startingPoint.add(parse(lines));

        // search cycle
        while (count <= STEP) {
            startingPoint = move(startingPoint);
            count++;
        }
        value = startingPoint.size();
        System.out.println("Value: " + value);
    }

    public Set<Position> move(final Set<Position> start) {
        Set<Position> result = new HashSet<Position>();
        for (Position position : start) {
            // UP
            if (position.line > 0) {
                if (isPlot(garden[position.line - 1][position.row])) {
                    result.add(new Position(position.line - 1, position.row));
                }
            }
            // DOW
            if (position.line < garden.length - 1) {
                if (isPlot(garden[position.line + 1][position.row])) {
                    result.add(new Position(position.line + 1, position.row));
                }
            }
            // LEFT
            if (position.row > 0) {
                if (isPlot(garden[position.line][position.row - 1])) {
                    result.add(new Position(position.line, position.row - 1));
                }
            }
            // RIGHT
            if (position.row < garden[0].length - 1) {
                if (isPlot(garden[position.line][position.row + 1])) {
                    result.add(new Position(position.line, position.row + 1));
                }
            }
        }
        return result;
    }

    public boolean isPlot(char c) {
        if (c == 'S' || c == '.') {
            return true;
        }
        return false;
    }

    public Position parse(List<String> lines) {
        Position start = null;
        garden = new char[lines.size()][lines.get(0).length()];

        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                garden[i][j] = lines.get(i).charAt(j);
                if (lines.get(i).charAt(j) == 'S') {
                    start = new Position(i, j);
                }
            }
        }
        return start;
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
        public int line;
        public int row;

        public Position(int line, int row) {
            this.line = line;
            this.row = row;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Position)) {
                return false;
            }
            Position other = (Position) o;
            return line == other.line && row == other.row;
        }

        @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + line;
			result = prime * result + row;
			return result;
		}
		
    }

}
