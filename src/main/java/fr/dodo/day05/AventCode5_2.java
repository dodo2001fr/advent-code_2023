package fr.dodo.day05;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AventCode5_2 {

    public static void main(String[] args) throws IOException {
        String fileName = "day05/input5.txt";

        // To avoid referring non-static method inside main() static method
        AventCode5_2 instance = new AventCode5_2();

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
        long value = -1;
        
        String firstLine = lines.get(0);
        String[] firstSplit = firstLine.split(":");

        String[] seedArray = firstSplit[1].trim().split(" ");
        List<String> seedStringList = Arrays.asList(seedArray);
        List<Seed> seedList = new ArrayList<Seed>();
        boolean hasSource = false;
        Seed seedTemp = null;
        for (String seed : seedStringList) {
            if (seed.trim().equals(""))
                continue;
            if (seedTemp == null) {
                seedTemp = new Seed();
                seedTemp.source = Long.parseLong(seed);
                hasSource = true;
                continue;
            }
            if (hasSource) {
                seedTemp.range = Long.parseLong(seed);
                seedList.add(seedTemp);
                hasSource = false;
                seedTemp = null;
                continue;
            }

        }

        // Map<Long,Long> seedToOil = this.map(lines,"seed-to-soil");
        List<MapGarden> seedToOil = this.map(lines, "seed-to-soil");
        List<MapGarden> oilTofertilizer = this.map(lines, "soil-to-fertilizer");
        List<MapGarden> fertilizerTowater = this.map(lines, "fertilizer-to-water");
        List<MapGarden> waterTolight = this.map(lines, "water-to-light");
        List<MapGarden> lightTotemperature = this.map(lines, "light-to-temperature");
        List<MapGarden> temperatureTohumidity = this.map(lines, "temperature-to-humidity");
        List<MapGarden> humidityTolocation = this.map(lines, "humidity-to-location");

        for (Seed seed : seedList) {
            List<Long> oilList = getList(seed, seedToOil);
            System.out.println("Oil to Fert done");
            List<Long> fertilizerList = getList(oilList, oilTofertilizer);
            oilList = null;

            System.out.println("Fertiliwer to water done");
            List<Long> waterList = getList(fertilizerList, fertilizerTowater);
            fertilizerList = null;

            System.out.println("Water to light done");
            List<Long> lightList = getList(waterList, waterTolight);
            waterList = null;

            System.out.println("Light to Temperature done");
            List<Long> temperatureList = getList(lightList, lightTotemperature);
            lightList = null;

            System.out.println("Temperature to Humidity done");
            List<Long> humidityList = getList(temperatureList, temperatureTohumidity);
            temperatureList = null;

            System.out.println("Humidity to Location done");
            List<Long> locationList = getList(humidityList, humidityTolocation);
            humidityList = null;

            for (Long location : locationList) {
                long loc = location.longValue();
                if (value == -1) {
                    value = loc;
                } else if (value > loc) {
                    value = loc;
                }

            }
            System.out.println("Min value: " + value);
            locationList = null;

        }

        System.out.println("Final Value: " + value);
    }

    public List<Long> getList(Seed seed, List<MapGarden> map) {
        List<Long> result = new ArrayList<Long>();
        for (long source = seed.source; source < (seed.source + seed.range); source++) {
            Long dest = Long.valueOf(source);
            for (int i = 0; i < map.size(); i++) {
                MapGarden currentMap = map.get(i);
                if (source >= currentMap.source && source < (currentMap.source + currentMap.range)) {
                    dest = Long.valueOf((source - currentMap.source) + currentMap.dest);
                    break;
                }
            }
            result.add(dest);
        }

        return result;
    }

    public List<Seed> getListRange(Seed seed, List<MapGarden> map) {
        List<Seed> result = new ArrayList<Seed>();
        Seed temp = null;
        long lastValue = -1;
        for (long source = seed.source; source < (seed.source + seed.range); source++) {
            Long dest = Long.valueOf(source);
            for (int i = 0; i < map.size(); i++) {
                MapGarden currentMap = map.get(i);
                if (source >= currentMap.source && source < (currentMap.source + currentMap.range)) {
                    dest = Long.valueOf((source - currentMap.source) + currentMap.dest);
                    break;
                }
            }
            if (temp == null) {
                temp = new Seed();
                temp.source = dest;
            } else {
                if (temp.source + 1 != dest) {
                    temp.range = dest - temp.source + 1;
                    result.add(temp);
                    temp = null;
                }
            }
            lastValue = dest;
        }

        if (temp != null) {
            temp.range = lastValue - temp.source + 1;
            result.add(temp);
        }
        return result;
    }

    public List<Long> getList(List<Long> sourceList, List<MapGarden> map) {
        List<Long> result = new ArrayList<Long>();
        for (Long source : sourceList) {
            Long dest = source;
            for (int i = 0; i < map.size(); i++) {
                MapGarden currentMap = map.get(i);
                if (source.longValue() >= currentMap.source
                        && source.longValue() < (currentMap.source + currentMap.range)) {
                    dest = Long.valueOf((source.longValue() - currentMap.source) + currentMap.dest);
                    break;
                }
            }
            result.add(dest);
        }

        return result;
    }

    public List<MapGarden> map(List<String> lines, String action) {
        List<MapGarden> mapList = new ArrayList<MapGarden>();
        boolean findHeader = false;
        for (pos = 1; pos < lines.size(); pos++) {
            String line = lines.get(pos);
            if (line.startsWith(action)) {
                findHeader = true;
                continue;
            } else if (!findHeader) {
                continue;
            }

            if (line.trim().length() == 0) {
                break;
            }

            String[] mapping = line.split(" ");
            long dest = Long.parseLong(mapping[0].trim());
            long source = Long.parseLong(mapping[1].trim());
            long range = Long.parseLong(mapping[2].trim());
            MapGarden map = new MapGarden();
            map.dest = dest;
            map.source = source;
            map.range = range;
            mapList.add(map);
            // for (long i = 0; i < range;i++){
            // result.put(Long.valueOf(source+i),Long.valueOf(dest+i));
            // }

        }
        return mapList;
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

    class MapGarden {
        public long source;
        public long dest;
        public long range;
    }

    class Seed {
        public long source;
        public long range;
    }

}
