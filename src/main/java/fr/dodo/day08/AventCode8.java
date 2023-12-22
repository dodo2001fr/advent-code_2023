package fr.dodo.day08;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode8 {

    public static void main(String[] args) throws IOException {
        String fileName = "day08/input8.txt";

        // To avoid referring non-static method inside main() static method
        AventCode8 instance = new AventCode8();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    int pos = 1;

    public void process(List<String> lines) {
        long value = 0;

        String instruction = lines.get(0);

        Map<String,Mapping> map = getMapping(lines);
        
        String position = "AAA";
        int pos = 0;
        System.err.println(value + " - " + position);
        while (!position.equals("ZZZ")){
            if (pos == instruction.length()){
                pos = 0;
            }
    
            String direction = String.valueOf(instruction.charAt(pos));
            Mapping currentMap = map.get(position);
            if (direction.equals("L")) position = currentMap.left;
            if (direction.equals("R")) position = currentMap.right;
            value++;
            pos++;
            System.err.println(value + " - " + position);
        }

       

        System.out.println("Value: " + value);
    }

    public Map<String,Mapping> getMapping(List<String> lines){
        Map<String,Mapping> result = new HashMap<String, Mapping>();
       for (int i = 1; i< lines.size();i++){
            String line = lines.get(i);
            if (line.trim().equals("")) continue;

            String[] mapArray = line.trim().split("=");
            String src = mapArray[0].trim();
            String[] direction = mapArray[1].trim().split(",");
            String left = direction[0].trim().substring(1);
            String right = direction[1].trim().substring(0, direction[1].trim().length()-1);
            
            result.put(src,new Mapping(left,right));
       }
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

    public static boolean isNumeric(char str) {
        try {
            Double.parseDouble(str + "");
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    class Mapping {
        String left;
        String right;

        public Mapping(String left, String right){
            this.left = left;
            this.right = right;
        }
    }
}
