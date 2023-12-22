package fr.dodo.day19;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode19 {

    public static void main(String[] args) throws IOException {
        String fileName = "input19.txt";

        // To avoid referring non-static method inside main() static method
        AventCode19 instance = new AventCode19();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    public Map<String, Rule> workflows = new HashMap<String,Rule>();
    public long a;
    public long s;
    public long m;
    public long x;

    public void process(List<String> lines) {
        long value = 0;

        List<String> rate = getParameter(lines);
        value = calculateRating(rate);

        System.out.println("x: " + x + " m: " + m + " a: " + a + " s: " + s);
        System.out.println("Value: " + value);
    }

    public long calculateRating(List<String> rates) {
        long result = 0;

        for (String rate : rates) {
            String rateTmp = rate.substring(1, rate.length() - 1);
            result += processWorkflow(rateTmp, "in");
        }

        return result;
    }

    public long processWorkflow(String rate, String workflow) {
        long result = 0;

        String[] rateArr = rate.trim().split(",");

        Rule rule = workflows.get(workflow);
        for (Condition condition : rule.conditions) {

            for (int i = 0; i < rateArr.length; i++) {
                String temp = rateArr[i];
                char part = temp.charAt(0);
                int value = Integer.parseInt(temp.substring(2));

                if (condition.isOtherwise) {
                    if (condition.nextWorkflow != null){
                        return processWorkflow(rate, condition.nextWorkflow);
                    }
                    if (!condition.match) {
                        return 0;
                    } else {
                        return match(rateArr);
                    }
                }

                if (condition.piece == part) {
                    if (condition.operateur == '<') {
                        if (value < condition.value) {
                            if (condition.nextWorkflow != null) {
                                return processWorkflow(rate, condition.nextWorkflow);
                            }
                            if (!condition.match) {
                                return 0;
                            } else {
                                return match(rateArr);
                            }
                        }
                    } else if (condition.operateur == '>') {
                        if (value > condition.value) {
                            if (condition.nextWorkflow != null) {
                                return processWorkflow(rate, condition.nextWorkflow);
                            }
                            if (!condition.match) {
                                return 0;
                            } else {
                                return match(rateArr);
                            }
                        }
                    }
                }

            }
        }

        return result;
    }

    public long match(String[] rates) {
        long result = 0;

        for (int i = 0; i < rates.length; i++) {
            String temp = rates[i];
            char part = temp.charAt(0);
            int value = Integer.parseInt(temp.substring(2));
            match(part, value);
            result += value;
        }
        return result;
    }

    public void match(char part, int value) {
        if (part == 'a') {
            a += value;
        }
        if (part == 'x') {
            x += value;
        }
        if (part == 'm') {
            m += value;
        }
        if (part == 's') {
            s += value;
        }
    }

    public List<String> getParameter(List<String> lines) {
        List<String> result = new ArrayList<String>();

        boolean isWorflow = true;
        for (String line : lines) {
            if (isWorflow) {
                if (line.trim().isEmpty()) {
                    isWorflow = false;
                } else {
                    int position = line.indexOf("{");
                    String name = line.substring(0, position);
                    Rule rule = new Rule(line.substring(position + 1, line.length() - 1));
                    workflows.put(name, rule);
                }
            } else {
                result.add(line);
            }
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

    public class Rule {
        List<Condition> conditions;

        public Rule(String rule) {
            conditions = new ArrayList<Condition>();
            String[] condTable = rule.trim().split(",");
            for (int i = 0; i < condTable.length; i++) {
                conditions.add(new Condition(condTable[i]));
            }
        }
    }

    public class Condition {
        String condition;
        String nextWorkflow;
        char piece;
        char operateur;
        int value;
        boolean match = false;
        boolean isOtherwise = false;

        public Condition(String condition) {
            this.condition = condition;
            if (condition.indexOf(":") == -1) {
                isOtherwise = true;
                if (condition.equals("A")) {
                    match = true;
                    return;
                }

                if (condition.equals("R")) {
                    match = false;
                    return;
                }
                nextWorkflow = condition;
                return;
            }

            piece = condition.charAt(0);
            operateur = condition.charAt(1);

            String temp = condition.substring(2);
            String[] tempArray = temp.trim().split(":");
            value = Integer.parseInt(tempArray[0]);
            String temp2 = tempArray[1];
            if (temp2.equals("A")) {
                match = true;
            } else if (temp2.equals("R")) {
                match = false;
            } else {
                nextWorkflow = temp2;
            }
        }
    }

}
