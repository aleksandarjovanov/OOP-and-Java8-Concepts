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
    public int compareTo(Person person) {
        return name.compareTo(person.name);
    }

    @Override
    public String toString() {
        return name + " " + age + "\n";
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

        TreeSet<Person> set = new TreeSet<>(Person.comparator);
//        TreeMap<Integer, Person> map = new TreeMap<>();

        Person p1 = new Person("viki", 115);
        Person p2 = new Person("ace", 22);

        set.add(p1);
        set.add(p2);

        p1.setAge(4);
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
