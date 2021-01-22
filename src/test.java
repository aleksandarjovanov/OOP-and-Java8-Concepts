import java.util.*;

class Person implements Comparable<Person>{
    private String name;
    private int age;

    public static Comparator<Person> comparator = Comparator.comparingInt(p -> p.age);

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @Override
    public String toString() {
        return name + " " + age + "\n";
    }

    @Override
    public int compareTo(Person person) {
        return Integer.compare(this.getAge(), person.getAge());
    }
}

public class test {

    public static void main(String[] args) {

//        HashMap<Integer, String> mapa = new HashMap<>();
//
//        String value = mapa.computeIfAbsent(1, k -> "prv");
//        String value2 = mapa.put(2, "vtor");
//
//        System.out.println(value + "  " + value2);
//
//        System.out.println(mapa);

        TreeSet<Person> set = new TreeSet<>((p1, p2) -> {
            Comparator<Person> comparator = Comparator.comparing(Person::getAge);
            int result = comparator.thenComparing(Person::getName).compare(p1, p2);
            return result;
//            if( (Integer.compare(p1.getAge(), p2.getAge()) == 0) && (p1.getName().compareTo(p2.getName()) == 0) )
//                return 0;
//            return -1;
        });
//        TreeMap<Integer, Person> map = new TreeMap<>(Comparator.reverseOrder());

        Person p1 = new Person("viki", 15);
        Person p2 = new Person("viki", 22);
        Person p4 = new Person("viki", 22);
        Person p3 = new Person("nekoj", 15);

//        map.put(3, p3);
//        map.put(1, p1);
//        map.put(2, p2);
//
//        map.entrySet().forEach(System.out::println);

        set.add(p1);
        set.add(p2);
        set.add(p3);
        set.add(p4);
//
//        p1.setAge(4);
//        map.putIfAbsent(1, p1);
//        map.putIfAbsent(2, p2);
//
//        set.last().setName("asdasdasd");
//
//        set.forEach(p -> p.setName("asd"));
//
       System.out.println(set);
//        System.out.println(map);
    }
}
