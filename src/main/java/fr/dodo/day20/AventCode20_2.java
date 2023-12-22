package fr.dodo.day20;

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

public class AventCode20_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "input20.txt";

        // To avoid referring non-static method inside main() static method
        AventCode20_2 instance = new AventCode20_2();

        File file = instance.getFile(fileName);

        // validate file path
        System.out.println(file.getPath());

        try (Stream<String> stream = Files.lines(file.toPath())) {
            List<String> lines = stream.collect(Collectors.toList());
            instance.process(lines);

        }
    }

    List<Pulse> queue;
    Map<String, Module> allModules;
    Map<String, Conjonction> conjonction = new HashMap<String, Conjonction>();
    Map<String, String> flipfop = new HashMap<String, String>();

    long highPulse = 0;
    long lowPulse = 0;

    static String BROADCAST = "broadcaster";
    static String ON = "on";
    static String OFF = "off";
    static String LOW = "low";
    static String HIGH = "high";

    static long PUSH = 1000000000;

    public boolean finish = false;
    public int countButton = 0;

    public void process(List<String> lines) {
        long value = 0;

        parse(lines);
        initConjonction();

        long count = 1;

        // search cycle
        while (!finish) {
            pushButton();
            // if (isFlipFlopRest() && isConjonctionReset()) {
            // break;
            // }
            count++;
        }

        System.out.println("nb cycle: " + count + " high " + highPulse + " low " + lowPulse);
        /*
         * / if (count < PUSH) {
         * long remainder = PUSH % count;
         * 
         * // Multiply
         * long multi = PUSH / count;
         * System.out.println("PUSH " + PUSH + "/" + count + " = " + multi);
         * highPulse = highPulse * multi;
         * lowPulse = lowPulse * multi;
         * 
         * for (int i = 0; i < remainder; i++) {
         * pushButton();
         * }
         * }
         */
        System.out.println(" high " + highPulse + " low " + lowPulse);
        value = highPulse * lowPulse;
        System.out.println("Value: " + value);
        System.out.println("NB button: " + countButton);
    }

    public void pushButton() {
        countButton++;
        lowPulse++;
        Pulse pulseFirst = new Pulse(true, BROADCAST, "button");

        queue = new ArrayList<Pulse>();
        queue.add(pulseFirst);

        while (!queue.isEmpty() && !finish) {
            if (finish)
                break;
            Pulse pulse = queue.get(0);
            Module module = allModules.get(pulse.module);
            if (module != null) {
                processPulse(module, pulse.islow, pulse.from);
            }
            queue.remove(0);
        }
    }

    public void processPulse(Module module, boolean isLow, String from) {
        if (BROADCAST.equals(module.type)) {
            fillPulseList(isLow, module);
        } else if (module.type.equals("%")) {
            if (isLow) {
                String state = flipfop.get(module.name);
                if (ON.equals(state)) {
                    flipfop.put(module.name, OFF);
                    isLow = true;
                } else {
                    flipfop.put(module.name, ON);
                    isLow = false;
                }
                fillPulseList(isLow, module);
            }
        } else {
            Conjonction conj = conjonction.get(module.name);
           /*  if (module.name.equals("dg")) {
                if (!conj.allInputIsLow())
                    System.out.println(countButton + " " + conj.print());
            }*/
            conj.received(from, isLow);
            if (conj.allInputIsHish()) {
                isLow = true;
            } else {
                isLow = false;
            }
            fillPulseList(isLow, module);
        }
    }

    public void fillPulseList(boolean isLow, Module module) {

        for (String next : module.nextModule) {
            Pulse pulse = new Pulse(isLow, next, module.name);
            queue.add(pulse);
            if (isLow) {
                if (next.equals("rx")) {
                    System.out.println("push " + countButton);
                    finish = true;
                    break;
                }
                lowPulse++;
            } else {
                highPulse++;
            }
        }
    }

    public boolean isConjonctionReset() {
        for (Map.Entry<String, Conjonction> entry : conjonction.entrySet()) {
            Conjonction value = entry.getValue();
            if (!value.allInputIsLow()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFlipFlopRest() {
        for (Map.Entry<String, String> entry : flipfop.entrySet()) {
            String value = entry.getValue();
            if (HIGH.equals(value)) {
                return false;
            }
        }
        return true;
    }

    public void initConjonction() {
        for (Map.Entry<String, Conjonction> entry : conjonction.entrySet()) {
            String key = entry.getKey();
            for (Map.Entry<String, Module> module : allModules.entrySet()) {
                Module m = module.getValue();
                if (m.nextModule.contains(key)) {
                    Conjonction value = entry.getValue();
                    value.addInput(m.name);
                    conjonction.replace(key, value);
                }
            }
        }
    }

    public void parse(List<String> lines) {
        allModules = new HashMap<String, Module>();

        for (String line : lines) {
            Module module = new Module(line);
            allModules.put(module.name, module);
        }
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

    public class Conjonction {
        public Map<String, String> input = new HashMap<String, String>();

        public void addInput(String name) {
            input.put(name, LOW);
        }

        public void received(String name, boolean isLow) {
            if (isLow) {
                input.put(name, LOW);
            } else {
                input.put(name, HIGH);
            }
        }

        public boolean allInputIsHish() {

            for (Map.Entry<String, String> entry : input.entrySet()) {
                String value = entry.getValue();
                if (LOW.equals(value)) {
                    return false;
                }
            }
            return true;
        }

        public boolean allInputIsLow() {

            for (Map.Entry<String, String> entry : input.entrySet()) {
                String value = entry.getValue();
                if (HIGH.equals(value)) {
                    return false;
                }
            }
            return true;
        }

        public String print() {
            String display = "";
            for (Map.Entry<String, String> entry : input.entrySet()) {
                display = display + " / " + entry.getKey() + ":" + entry.getValue();
            }
            return display;
        }
    }

    public class Pulse {
        public boolean islow;
        public String module;
        public String from;

        public Pulse(boolean isLow, String module, String from) {
            this.islow = isLow;
            this.module = module;
            this.from = from;
        }
    }

    public class Module {
        public String type = "";
        public List<String> nextModule = new ArrayList<String>();
        public String name;

        public Module(String line) {
            if (line.startsWith(BROADCAST)) {
                type = BROADCAST;
                name = BROADCAST;
            } else {
                type = line.substring(0, 1);
                name = line.substring(1, line.indexOf("->")).trim();

                if (type.equals("%")) {
                    flipfop.put(name, OFF);
                } else {
                    conjonction.put(name, new Conjonction());
                }
            }
            String temp = line.substring(line.indexOf("->") + 2);
            if (temp.indexOf(",") == -1) {
                nextModule.add(temp.trim());
            } else {
                String[] modules = temp.trim().split(",");
                for (int i = 0; i < modules.length; i++) {
                    nextModule.add(modules[i].trim());
                }
            }
        }
    }

}
