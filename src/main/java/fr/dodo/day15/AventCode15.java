package fr.dodo.day15;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode15 {

    public static void main(String[] args) throws IOException {
        String fileName = "test15.txt";

        // To avoid referring non-static method inside main() static method
        AventCode15 instance = new AventCode15();

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

        String line = lines.get(0);
        String[] steps = line.trim().split(",");

        for (int i = 0; i< steps.length ; i++){
            value += hasString(steps[i]);
            System.out.println(steps[i] + " - " + value);
        }
        

        System.out.println("Value: " + value);
    }

    public long hasString(String chaine){
        long result = 0;
        for (int i=0; i < chaine.length();i++){
            result = hash(chaine.charAt(i), result);
        }
        return result;
    }
  
    public long hash(char c, long previous){
        long result = previous;
        result += (int)c;
        result = result * 17;
        result = result % 256;
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
