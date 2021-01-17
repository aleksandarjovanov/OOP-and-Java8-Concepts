package SecondMidterm.Airports;

import java.util.*;
import java.util.stream.Collectors;

class Airport {
    String name;
    String country;
    String code;
    int passengers;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }
}

class Flight implements Comparable<Flight> {
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public int compareTo(Flight f) {
        return Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime).thenComparing(Flight::getFrom)
                .compare(this, f);
    }

    @Override
    public String toString() {
        int end = time + duration;
        return String.format("%s-%s %02d:%02d-%02d:%02d %s%dh%02dm", from, to, time / 60, time % 60, (end / 60) % 24,
                end % 60, (end / 60) / 24 > 0 ? "+1d " : "", duration / 60, duration % 60);
    }

}

class Airports {

    private Map<String, Airport> airports;
    private Map<String, Set<Flight>> flightsFrom;
    private Map<String, Set<Flight>> flightsTo;

    public Airports() {
        airports = new HashMap<>();
        flightsFrom = new HashMap<>();
        flightsTo = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.put(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from, to, time, duration);

        flightsFrom.putIfAbsent(from, new TreeSet<>());
        flightsFrom.get(from).add(flight);

        flightsTo.putIfAbsent(to, new TreeSet<>());
        flightsTo.get(to).add(flight);

    }

    public void showFlightsFromAirport(String code) {
        System.out.println(airports.get(code));
        int i = 1;
        for (Flight f : flightsFrom.get(code)) {
            System.out.printf("%d. %s\n", i++, f);
        }
    }

    public void showDirectFlightsFromTo(String from, String to) {

        Set<Flight> tmp = flightsFrom.get(from)
                .stream()
                .filter(f -> f.getTo().equals(to))
                .collect(Collectors.toSet());

        if (tmp.isEmpty())
            System.out.printf("No flights from %s to %s\n", from, to);
        else
            tmp.forEach(System.out::println);
    }

    public void showDirectFlightsTo(String to) {
        flightsTo.get(to).forEach(System.out::println);
    }
}


public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}