package SecondMidterm.EventsCalendar;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Event implements Comparable<Event> {
    private String name, location;
    private LocalDateTime date;
    private int hoursPlusMinutes;
    private static Comparator<Event> comparator = Comparator.comparingInt(Event::getHoursPlusMinutes).thenComparing(Event::getName);

    public Event(String name, String location, LocalDateTime date) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.hoursPlusMinutes = (this.date.getHour() * 60) + this.date.getMinute();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyy HH:mm");
        String rep = date.format(formatter);
        return String.format("%s at %s, %s", rep, location, name);
    }

    @Override
    public int compareTo(Event event) {
        return comparator.compare(this, event);
    }

    public int getHoursPlusMinutes() {
        return hoursPlusMinutes;
    }
}

class EventCalendar {
    private HashMap<LocalDate, TreeSet<Event>> eventsByDate;
    private int year;

    public EventCalendar(int year) {
        this.year = year;
        eventsByDate = new HashMap<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        LocalDateTime dateTime = date.toInstant()
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();

        Event event = new Event(name, location, dateTime);
        if(dateTime.getYear() != year)
            throw new WrongDateException(dateTime);

        eventsByDate.putIfAbsent(dateTime.toLocalDate(), new TreeSet<>());
        eventsByDate.computeIfPresent(dateTime.toLocalDate(), (k, v) -> {
            v.add(event);
            return v;
        });
    }

    public void listEvents(Date date) {
        LocalDateTime dateTime = date.toInstant()
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();
        if(!eventsByDate.containsKey(dateTime.toLocalDate())) {
            System.out.println("No events on this day!");
            return;
        }

        eventsByDate.get(dateTime.toLocalDate()).forEach(System.out::println);
    }

    public void listByMonth() {             // **********NAJBITEN GRUIPING BY************* //
        TreeMap<Integer, Integer> tmp = eventsByDate.entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().getMonthValue(),
                        TreeMap::new,
                        Collectors.summingInt(entry -> entry.getValue().size())
                ));

        IntStream.range(1,13)
                .forEach(i -> tmp.putIfAbsent(i, 0));

        tmp.forEach((k, v) -> System.out.println(k + " : " + v));;
    }
}

class WrongDateException extends Exception {
    public WrongDateException(LocalDateTime date) {
        super(String.format("Wrong date: %s", date.toString()));
    }
}

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde