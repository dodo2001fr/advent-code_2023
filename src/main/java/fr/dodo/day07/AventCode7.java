package fr.dodo.day07;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode7 {

    public static void main(String[] args) throws IOException {
        String fileName = "day07/input7.txt";

        // To avoid referring non-static method inside main() static method
        AventCode7 instance = new AventCode7();

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

        List<Hand> handList = new ArrayList<Hand>();
        for (String line : lines) {
            String[] handArray = line.trim().split(" ");
            Hand hand = new Hand();
            hand.card = handArray[0];
            hand.bit = Long.parseLong(handArray[1].trim());
            handList.add(hand);
        }

        Collections.sort(handList, new Comparator<Hand>() {
            @Override
            public int compare(Hand left, Hand right){
                //-1 less //1 greather
                int result = 0;
                int leftType = getType(left.card);
                int rightType = getType(right.card);
                if (leftType != rightType)
                    return (leftType < rightType ? -1 : 1 );
                for (int i=0 ; i< left.card.length(); i++){
                    char leftChar = left.card.charAt(i);
                    char rightChar = right.card.charAt(i);
                    if (isNumeric(leftChar) && ! isNumeric(rightChar)){
                        return -1;
                    }
                    if (!isNumeric(leftChar) && isNumeric(rightChar)){
                        return 1;
                    }
                    if (isNumeric(leftChar) && isNumeric(rightChar)){
                        if (leftChar< rightChar) return -1;
                        if (leftChar > rightChar) return 1;
                    } else {
                        //A, K, Q, J, T,
                        if (leftChar == 'A'){
                            if (rightChar == 'A') continue;
                            else return 1;
                        } else if (leftChar == 'K'){
                            if (rightChar == 'K') continue;
                            else if (rightChar == 'A') return -1;
                            else  return 1;
                        } else if (leftChar == 'Q'){
                            if (rightChar == 'Q') continue;
                            else if (rightChar == 'A' || rightChar == 'K') return -1;
                            else  return 1;
                        } else if (leftChar == 'J'){
                            if (rightChar == 'J') continue;
                            else if (rightChar == 'A' || rightChar == 'K'  || rightChar == 'Q') return -1;
                            else  return 1;
                        } else if (leftChar == 'T'){
                            if (rightChar == 'T') continue;
                            else  return -1;
                        }

                        
                    }

                    
                }
                return result;
            }
        });

        int rank = handList.size();
        String previous = "";
        for(int i = handList.size() ; i > 0; i--){
            Hand hand = handList.get(i-1);
            if (!previous.equals(hand.card)){
                rank = i;
            }
            System.out.println("hand: " + hand.card + " - " + hand.bit);
            value = value + (rank * hand.bit);
        }

        System.out.println("Value: " + value);
    }

    public int getType(String hand){
        // 0 diff, 1 one pair, 2  two pair, 3 three, 4 full house (2+3), 5 - four card, 6 - five card
        int result = 0;
        List<String> process = new ArrayList<String>();
        int nbPair = 0;
        int nbThree = 0;
        for (int i = 0; i < hand.length(); i++){
            char current = hand.charAt(i);
            String currentStr = ""+current;
            if (process.contains(currentStr)){
                continue;
            }
            process.add(currentStr);
            int count = 1;
            for (int j=i+1; j < hand.length(); j++){
                if (current==hand.charAt(j)){
                    count ++;
                }
            }
            if (count == 5){
                return 6;
            }
            if (count == 4){
                return 5;
            }
            if (count == 3){
                nbThree++;
            }
            if (count == 2){
                nbPair++;
            }

        }
        if (nbThree == 1 && nbPair == 1){
            return 4;
        }
        if (nbThree == 1 && nbPair == 0){
            return 3;
        }
        if (nbPair == 2){
            return 2;
        }
        if (nbPair == 1){
            return 1;
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

    class Hand {
        String card;
        long bit;
    }
}
