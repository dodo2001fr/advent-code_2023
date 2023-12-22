package fr.dodo.day03;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode3_2 {
	
    public static void main(String[] args) throws IOException {
    String fileName ="day03/input3.txt";
    long value= 0;

    //To avoid referring non-static method inside main() static method
    AventCode3_2 instance = new AventCode3_2();
    
    
    File file = instance.getFile(fileName);
     
    //validate file path
    System.out.println(file.getPath());
        
        try (Stream<String> stream = Files.lines(file.toPath())) {
        
        List<String> lines = stream.collect(Collectors.toList());
        Map<String,List<String>> gearMap = new HashMap<String,List<String>>();

        
        for (int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            System.out.println("Line: " + line);
            String part = "";
            Set<String> gearPos = new HashSet<String>();
            for (int j = 0; j < line.length();j++){
                if (isNumeric(line.charAt(j))){
                    part = part + line.charAt(j);
                    
                    String gearPosTemp = isPart(lines,i,j);
                    if (!gearPosTemp.equals("")){
                        gearPos.add(gearPosTemp);
                    }
                    
                }else {
                    if (part.equals("")){
                        continue;
                    } else {
                        if (gearPos.size()> 0){
                            for (String gearSet : gearPos){
                                if (gearMap.get(gearSet) == null){
                                    List<String> number = new ArrayList<String>();
                                    number.add(part);
                                    gearMap.put(gearSet,number);
                                } else {
                                    gearMap.get(gearSet).add(part);
                                }
                            }

                        } 
                        part = "";
                        gearPos = new HashSet<String>();
                   }    
                }

            }
            if (!part.equals("")){
                 if (gearPos.size()> 0){
                    for (String gearSet : gearPos){

                        if (gearMap.get(gearSet) == null){
                            List<String> number = new ArrayList<String>();
                            number.add(part);
                            gearMap.put(gearSet,number);
                        } else {
                            gearMap.get(gearSet).add(part);
                        }
                    }

                } 
                part = "";
                gearPos = new HashSet<String>();
            }
        }
        
    for (Map.Entry<String, List<String>> entry : gearMap.entrySet()) {
        List<String> listNumber = entry.getValue();
        long gearValue= 0 ;
        if (listNumber.size()> 1){
            for(String part : listNumber){
                if (gearValue == 0){
                    gearValue = Integer.parseInt(part);
                } else {
                    gearValue = gearValue * Integer.parseInt(part);
                }
            }
        }
        value = value + gearValue;
    }
        System.out.println("Value: "+ value);
	}

 }

public static String isPart(List<String> lines, int line, int posChar){
String value = "";

char pos;
if (posChar> 0){
    //test left
    pos = lines.get(line).charAt(posChar-1);
    if (isGear(pos)){
        return (line + "-" + (posChar-1));
    } 

    if (line >0){
        //Test diag upper left
        pos = lines.get(line-1).charAt(posChar-1);
        if (isGear(pos)){
            return ((line-1) + "-" + (posChar-1));
        }

    }

    //Test diag down left
    if (line < lines.size()-1){
        pos = lines.get(line+1).charAt(posChar-1);
        if (isGear(pos)){
            return ((line +1) + "-" + (posChar-1));
        }

    }

}

//test Test upper
if (line >0){
    //Test diag upper left
    pos = lines.get(line-1).charAt(posChar);
    if (isGear(pos)){
        return ((line -1) + "-" + (posChar));
    }

}

//test Down
if (line < lines.size()-1){
    pos = lines.get(line+1).charAt(posChar);
    if (isGear(pos)){
        return ((line + 1) + "-" + (posChar));
    }

}

//test right
if (posChar < lines.get(line).length()-1){
    pos = lines.get(line).charAt(posChar+1);
    if (isGear(pos)){
        return (line + "-" + (posChar+1));
    }

    if (line >0){
        //Test diag upper right
        pos = lines.get(line-1).charAt(posChar+1);
        if (isGear(pos)){
            return ((line-1) + "-" + (posChar+1));
        }

    }

    //Test diag down right
    if (line < lines.size()-1){
        pos = lines.get(line+1).charAt(posChar+1);
        if (isGear(pos)){
            return ((line+1) + "-" + (posChar+1));
        }

    }
}

    return value;
}

 public static boolean isNumeric(char str) { 
    try {  
      Double.parseDouble(str+"");  
      return true;
    } catch(NumberFormatException e){  
      return false;  
    }  
  }

public static boolean isDot(char str) { 
    return str == '.';
}

public static boolean isGear(char str) { 
    return str == '*';
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
