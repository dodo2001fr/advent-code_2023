package fr.dodo.day02;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode2 {

    static int RED = 12;
    static int GREEN = 13;
    static int BLUE = 14;
	
    public static void main(String[] args) throws IOException {
    String fileName ="day02/input2.txt";
    long value= 0;

    //To avoid referring non-static method inside main() static method
    AventCode2 instance = new AventCode2();
    
    File file = instance.getFile(fileName);
     
    //validate file path
    System.out.println(file.getPath());
        
        try (Stream<String> stream = Files.lines(file.toPath())) {
        
        List<String> lines = stream.collect(Collectors.toList());
        for (String line : lines) {
			
			StringTokenizer tokenizer = new StringTokenizer(line, ":");
            
            String game = tokenizer.nextToken();
                        //compilation de la regex avec le motif : "a"
            Pattern p = Pattern.compile("(\\d+)");
            //créer et associer le moteur à la regex sur la chaîne "ab"
            Matcher m = p.matcher(game);
            m.find();
            int gameNum = Integer.parseInt(m.group(1));
            String sets = tokenizer.nextToken();
            
            if (isGame(sets)){
                System.out.println("Game OK: " + gameNum + " - set: " + sets);
                value = value + gameNum;
            }

        }
        
        System.out.println("Result : " + value);
	}

 }

 public static boolean isGame(String sets){
System.out.println("sets: " + sets);
    StringTokenizer tokenizer = new StringTokenizer(sets, ";");
    while (tokenizer.hasMoreElements()){

        if (!isSetInGame(tokenizer.nextToken())){
            return false;
        }

    }


    return true;
 }

 public static boolean isSetInGame(String set){
    System.out.println("set: " + set);
    StringTokenizer tokenizer = new StringTokenizer(set, ",");
    while (tokenizer.hasMoreElements()){
        
        String colorSet = tokenizer.nextToken();
        System.out.println("Color: " + colorSet+"-");
        int colorValue = getInt(colorSet);
        String color = getColor(colorSet);

    System.out.println("color: " + color + " - " + colorValue);
        if (color.equals("blue")){
            if (colorValue > BLUE) return false;
        } else if (color.equals("green")){
            if (colorValue > GREEN) return false;
        } else if (color.equals("red")){
            if (colorValue > RED) return false;
        }
    }
    return true;
 }

 public static String getColor(String colorSet) {
     StringTokenizer tokenizer = new StringTokenizer(colorSet, " ");
     tokenizer.nextToken();
    return tokenizer.nextToken();
}

public static int getInt(String text) {
     Pattern p;
    Matcher m;
    //compilation de la regex avec le motif : "a"
    p = Pattern.compile("(\\d+)");
    //créer et associer le moteur à la regex sur la chaîne "ab"
    m = p.matcher(text);
    m.find();
    return Integer.parseInt(m.group(1));
}



public static String getFirstDigit (String text){

    	 Pattern p;
 Matcher m;
 //compilation de la regex avec le motif : "a"
 p = Pattern.compile("^[^\\d]*(\\d)");
 //créer et associer le moteur à la regex sur la chaîne "ab"
 m = p.matcher(text);
 //si le motif est trouvé
 if(!m.find()) {
 System.out.println("Not found first digit: "+ text);
 }else {
    System.out.println("first: " + m.group(1));
 }
 return m.group(1);
}

        
public static String getLastDigit (String text){

    	 Pattern p;
 Matcher m;
 //compilation de la regex avec le motif : "a"
 p = Pattern.compile("(\\d)(?!.*\\d)");
 //créer et associer le moteur à la regex sur la chaîne "ab"
 m = p.matcher(text);
 //si le motif est trouvé
 if(!m.find()) {
 System.out.println("Not found last digit: "+ text);
 } else {
    System.out.println("last: " + m.group(1));
 }
 return m.group(1);
}

private File getFile(String fileName) throws IOException
{
  ClassLoader classLoader = getClass().getClassLoader();
      URL resource = classLoader.getResource(fileName);
       
      if (resource == null) {
          throw new IllegalArgumentException("file is not found!");
      } else {
          return new File(resource.getFile());
      }
}

}
