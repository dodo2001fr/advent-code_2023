package fr.dodo.day16;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode16_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "input16.txt";

        // To avoid referring non-static method inside main() static method
        AventCode16_2 instance = new AventCode16_2();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.lines = lines;
            instance.process();

        }
    }

    public boolean[][] grid;
    public List<String> lines;

    public void process() {
        long value = 0;

        grid = new boolean[lines.size()][lines.get(0).length()];

        long result = 0;
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {

                if (i == 0 && j == 0) {
                    ernergized(i, j, 'R');
                    result = evaluate();
                    if (result > value)
                        value = result;
                    ernergized(i, j, 'D');
                    result = evaluate();
                    if (result > value)
                        value = result;
                } else if (i == 0 && j == lines.get(i).length() - 1) {
                    ernergized(i, j, 'L');
                    result = evaluate();
                    if (result > value)
                        value = result;
                    ernergized(i, j, 'D');
                    result = evaluate();
                    if (result > value)
                        value = result;
                } else if (i == lines.size() - 1 && j == 0) {
                    ernergized(i, j, 'R');
                    result = evaluate();
                    if (result > value)
                        value = result;
                    ernergized(i, j, 'U');
                    result = evaluate();
                    if (result > value)
                        value = result;
                } else if (i == lines.size() - 1 && j == lines.get(i).length() - 1) {
                    ernergized(i, j, 'L');
                    result = evaluate();
                    if (result > value)
                        value = result;
                    ernergized(i, j, 'U');
                    result = evaluate();
                    if (result > value)
                        value = result;
                } else if (i == 0) {
                    ernergized(i, j, 'D');
                    result = evaluate();
                    if (result > value)
                        value = result;
                } else if (j == 0) {
                    ernergized(i, j, 'D');
                    result = evaluate();
                    if (result > value)
                        value = result;
                } else if (i == lines.size() - 1) {
                    ernergized(i, j, 'U');
                    result = evaluate();
                    if (result > value)
                        value = result;
                } else if (j == lines.get(i).length() - 1) {
                    ernergized(i, j, 'L');
                    result = evaluate();
                    if (result > value)
                        value = result;
                }
            }
        }

        System.out.println("Value: " + value);

    }

    public long evaluate() {
        long result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j])
                    result++;
            }
        }
        grid = new boolean[lines.size()][lines.get(0).length()];
        cache = new ArrayList<String>();
        
        return result;
    }

    public List<String> cache = new ArrayList<String>();

    public void ernergized(int line, int col, char d) {
        int l = line;
        int c = col;
        char direction = d;

        boolean find = false;
        while (!find) {
            String key = l + "-" + c + "-" + direction;
            if (cache.contains(key))
                return;

            if (l < 0 || l >= lines.size() || c < 0 || c >= lines.get(0).length()) {
                return;
            }

            grid[l][c] = true;
            cache.add(key);
            char current = getValue(l, c);

            if (current == '.' || (current == '-' && (direction == 'R' || direction == 'L'))
                    || (current == '|' && (direction == 'U' || direction == 'D'))) {
                if (direction == 'R') {
                    c = c + 1;
                    continue;
                }
                if (direction == 'L') {
                    c = c - 1;
                    continue;
                }
                if (direction == 'U') {
                    l = l - 1;
                    continue;
                }
                if (direction == 'D') {
                    l = l + 1;
                    continue;
                }
            }
            if (current == '/') {
                if (direction == 'R') {
                    l = l - 1;
                    direction = 'U';
                    continue;
                }
                if (direction == 'L') {
                    l = l + 1;
                    direction = 'D';
                    continue;
                }
                if (direction == 'U') {
                    c = c + 1;
                    direction = 'R';
                    continue;
                }
                if (direction == 'D') {
                    c = c - 1;
                    direction = 'L';
                    continue;
                }
            }
            if (current == '\\') {
                if (direction == 'R') {
                    l = l + 1;
                    direction = 'D';
                    continue;
                }
                if (direction == 'L') {
                    l = l - 1;
                    direction = 'U';
                    continue;
                }
                if (direction == 'U') {
                    c = c - 1;
                    direction = 'L';
                    continue;
                }
                if (direction == 'D') {
                    c = c + 1;
                    direction = 'R';
                    continue;
                }
            }

            if (current == '-') {
                ernergized(l, c - 1, 'L');
                c = c + 1;
                direction = 'R';
                continue;

            }
            if (current == '|') {
                ernergized(l - 1, c, 'U');
                l = l + 1;
                direction = 'D';
                continue;
            }
        }
    }

    public char getValue(int l, int c) {
        return lines.get(l).charAt(c);
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
}
