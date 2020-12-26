package FirstMidterm.DailyTemps;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract class Temperature{
    protected double value;

    public Temperature(double value) {
        this.value = value;
    }
    //***Factory design pattern***//
    public static Temperature createTemperature(String temp) {
        int value = Integer.parseInt(temp.substring(0, temp.length() - 1));
        if(temp.contains("C"))
            return new CTemperature(value);
        else
            return new FTemperature(value);
    }

    public abstract double getTemperature(char scale);
}

class CTemperature extends Temperature{

    public CTemperature(double value) {
        super(value);
    }

    @Override
    public double getTemperature(char scale) {
        if(scale == 'F')
            return value*9.0/5 + 32.0;
        return value;
    }

    @Override
    public String toString() {
        return String.format("%dC", value);
    }
}

class FTemperature extends Temperature{

    public FTemperature(double value) {
        super(value);
    }

    @Override
    public double getTemperature(char scale) {
        if(scale == 'C')
            return (value-32)*5.0/9.0;
        return value;
    }

    @Override
    public String toString() {
        return String.format("%dF", value);
    }
}

class DailyMeasurement implements Comparable<DailyMeasurement>{
    private int day;
    private List<Temperature> temperatures;

    public DailyMeasurement(int day, List<Temperature> temperatures) {
        this.day = day;
        this.temperatures = temperatures;
    }

    public static DailyMeasurement createDailyMeasurement(String line){
        String [] parts = line.split("\\s+");

        List<Temperature> tempTemperatures = IntStream.range(1, parts.length)
                .mapToObj(idx -> Temperature.createTemperature(parts[idx]))
                .collect(Collectors.toList());

        return new DailyMeasurement(Integer.parseInt(parts[0]), tempTemperatures);
    }


    public String toString(char scale) {
        DoubleSummaryStatistics ds = temperatures.stream()
                .mapToDouble(temperature -> temperature.getTemperature(scale))
                .summaryStatistics();

        return String.format("%3d: Count: %3d Min: %6.2f" + scale + " Max: %6.2f" + scale + " Avg: %6.2f" + scale,
                day,
                ds.getCount(),
                ds.getMin(),
                ds.getMax(),
                ds.getAverage());

    }

    @Override
    public int compareTo(DailyMeasurement dailyMeasurement) {
        return Integer.compare(this.day, dailyMeasurement.day);
    }
}

class DailyTemperatures{

    private List<DailyMeasurement> dailyMeasurements;

    public DailyTemperatures() {
        dailyMeasurements = new ArrayList<>();
    }

    public void readTemperatures(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        this.dailyMeasurements = br.lines()
                .map(DailyMeasurement::createDailyMeasurement)
                .collect(Collectors.toList());
    }

    public void writeDailyStats(OutputStream outputStream, char scale){
        PrintWriter pw = new PrintWriter(outputStream);
        dailyMeasurements.sort(Comparator.naturalOrder());
        dailyMeasurements.forEach(dm -> pw.println(dm.toString(scale)));
        pw.flush();
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}