package SecondMidterm.Audition;

import java.util.*;

class Participant{
    private String code;
    private String name;
    private int age;


    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        Participant that = (Participant) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

class Audition{

    private Map<String, Map<String, Participant>> participantsByCity;

    public Audition() {
        this.participantsByCity = new HashMap<>();
    }

    public void addParticipant(String city, String code, String name, int age) {
        Participant participant = new Participant(code, name, age);

//        participantsByCity.computeIfPresent(city, (k, v) -> {
//            v.putIfAbsent(code, participant);
//            return v;
//        });
//        participantsByCity.computeIfAbsent(city, k -> {
//            Map<String, Participant> mapa = new HashMap<>();
//            mapa.put(code, participant);
//            return mapa;
//        });

        participantsByCity.putIfAbsent(city, new HashMap<>());
        Map<String, Participant> innerMap = participantsByCity.get(city);
        innerMap.putIfAbsent(code, participant);

    }

    public void listByCity(String city) {
        participantsByCity.getOrDefault(city, new HashMap<>())  // zastita od exception, vo slucaj da nema kluc city
                .values()
                .stream()
                .sorted(Comparator.comparing(Participant::getName)
                        .thenComparing(Participant::getAge)
                        .thenComparing(Participant::getCode))
                .forEach(System.out::println);
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}