package FirstMidterm.WeatherStation;


import javax.print.attribute.standard.NumberUp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Measurement{

    private double temp;
    private double humidity;
    private double wind;
    private double visibility;
    private LocalDateTime time;

    public Measurement(double temp, double humidity, double wind, double visibility, LocalDateTime time) {
        this.temp = temp;
        this.humidity = humidity;
        this.wind = wind;
        this.visibility = visibility;
        this.time = time;
    }

    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWind() {
        return wind;
    }

    public double getVisibility() {
        return visibility;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km ", temp, wind, humidity, visibility) + getTime() + '\n';
    }
}

class WeatherStation{

    private List<Measurement> measurements;
    private int days;

    public WeatherStation(int days) {
        this.days = days;
        measurements = new ArrayList<>();
    }

    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date){
        LocalDateTime tmpDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Measurement measurement = new Measurement(temperature, wind, humidity, visibility, tmpDate);
        if(measurements.isEmpty()) {
            measurements.add(measurement);
            return;
        }

        int daysBetween = 0;
        for(Measurement m : measurements){
            daysBetween = (int) ChronoUnit.DAYS.between(m.getTime(), tmpDate);

            if(daysBetween <= days)
                break;
            measurements.remove(m);
        }

        int secondsBetween = (int) ChronoUnit.SECONDS.between(measurements.get(measurements.size()-1).getTime(), tmpDate);
        if(secondsBetween > 150)
            measurements.add(measurement);
    }

    public int total(){
        return measurements.size();
    }

    public void status(Date from, Date to){
        LocalDateTime tmpFrom = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime tmpTo = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        List<Measurement> rangeMeasurements = measurements.stream().filter(m -> isBetween(m.getTime(), tmpFrom, tmpTo)).collect(Collectors.toList());
        rangeMeasurements.forEach(System.out::print);
        System.out.print(rangeMeasurements.stream().mapToDouble(Measurement::getTemp).average().getAsDouble());

    }

    private boolean isBetween(LocalDateTime mTime, LocalDateTime from, LocalDateTime to) {
        return (mTime.isEqual(from) || mTime.isAfter(from)) && (mTime.isEqual(to) || mTime.isBefore(to));
    }


}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}
