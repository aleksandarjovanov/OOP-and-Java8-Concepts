package FirstMidterm.F1Race;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Driver implements Comparable<Driver>{

    private String name;
    private String [] laps;
    private String bestLap;

    public Driver(String name, String[] laps) {
        this.name = name;
        this.laps = laps;
        calculateBestLap();
    }

    private void calculateBestLap() {
        this.bestLap = Arrays.stream(laps).min(Comparator.naturalOrder()).get();
    }

    @Override
    public int compareTo(Driver driver) {
        return this.bestLap.compareTo(driver.bestLap);
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s\n", name, bestLap);
    }
}

class F1Race{

    private List<Driver> drivers;

    public F1Race() {
        this.drivers = new ArrayList<>();
    }

    public void readResults(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        this.drivers = br.lines()
                .map(this::createDriver)
                .collect(Collectors.toList());
    }

    private Driver createDriver(String line) {
        String [] parts = line.split("\\s+");
        return new Driver(parts[0], new String []{parts[1], parts[2], parts[3]});
    }


    public void printSorted(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        drivers.sort(Driver::compareTo);
        IntStream.range(0, drivers.size()).forEach(i -> pw.print(i+1 + ". " + drivers.get(i)));

        pw.flush();
        pw.close();
    }
}


public class F1Test {
    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }
}
