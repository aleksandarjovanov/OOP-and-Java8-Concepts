package lab2.Contacts;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

abstract class Item implements Comparable<Item>{

    private String date;

    public Item(String date){
        this.date = date;
    }

//    public boolean isNewerThan(Contact c){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate oldC = null;
//        LocalDate newC = null;
//        try {
//            oldC = LocalDate.parse(this.date, formatter);
//            newC = LocalDate.parse(c.date, formatter);
//        }
//        catch (Exception e){
//            System.out.println("Problem while converting string to date!");
//        }
//
//        return oldC.compareTo(newC) > 0;
//    }


    public boolean isNewerThan(Item c){
        return this.date.compareTo(c.date) > 0;
    }

    @Override
    public int compareTo(Item contact) {
        return this.isNewerThan(contact) ? 1 : -1;
    }

    abstract public String getType();
}

class EmailContact extends Item {

    private String email;

    public EmailContact(String date, String email){
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }
}

class PhoneContact extends Item {

    enum Operator{ VIP, ONE, TMOBILE }

    private String phone;
    private Operator operator;

    public PhoneContact(String date, String phone){
        super(date);
        this.phone = phone;
        setOperator();
    }

    private void setOperator(){
        char prefix = phone.charAt(2);
        if (prefix == '0' || prefix == '1' || prefix == '2') {
            operator = Operator.TMOBILE;
        } else if (prefix == '5' || prefix == '6') {
            operator = Operator.ONE;
        } else {
            operator = Operator.VIP;
        }
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String getType() {
        return "Phone";
    }
}

class Student{

    private Item[] contacts;
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;


    public Student(String firstName, String lastName, String city, int age, long index){
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        contacts = new Item[0];
    }

    public void increaseContacts(){
        Item[] tmp = new Item[this.contacts.length];
        tmp = Arrays.copyOf(this.contacts, contacts.length);
        this.contacts = new Item[tmp.length + 1];
        this.contacts = Arrays.copyOf(tmp, contacts.length);
    }

    public void addEmailContact(String date, String email){
        increaseContacts();
        this.contacts[contacts.length - 1] = new EmailContact(date, email);
    }

    public void addPhoneContact(String date, String phone){
        increaseContacts();
        contacts[contacts.length - 1] = new PhoneContact(date, phone);
    }

    public int contactsLength(){
        return this.contacts.length;
    }

    public Item[] getEmailContacts(){
        return Arrays.stream(this.contacts)
                .filter(c -> c.getType().equals("Email"))
                .toArray(Item[]::new);
    }

    public Item[] getPhoneContacts(){
        return Arrays.stream(this.contacts)
                .filter(c -> c.getType().equals("Phone"))
                .toArray(Item[]::new);
    }

    public String getCity(){
        return this.city;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public long getIndex() {
        return index;
    }

    public Item getLatestContact(){
        return Arrays.stream(contacts)
                .max(Item::compareTo)
                .get();
    }

    @Override
    public String toString() {
        String result = String.format("{\"ime\":\"%s\", \"prezime\":\"%s\", \"vozrast\":%d, \"grad\":\"%s\", \"indeks\":%d, \"telefonskiKontakti\":[", firstName, lastName, age, city, index);
        result += (getPhoneContacts().length != 0 ? "\"" +
                Arrays.stream(getPhoneContacts()).map(i -> ((PhoneContact) i).getPhone())
                        .collect(Collectors.joining("\", \"")) + "\"" : "") + "], \"emailKontakti\":[";
        result += (getEmailContacts().length != 0 ? "\"" + Arrays.stream(getEmailContacts()).map(i -> ((EmailContact) i).getEmail()).collect(Collectors.joining("\", \"")) + "\"" : "") + "]}";
        return result;
    }
}

class Faculty{

    private Student [] students;
    private String name;

    public Faculty(String name, Student [] students){
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String cityName){
        return (int) Arrays.stream(students)
                .filter(s -> s.getCity().equals(cityName))
                .count();
    }

    public Student getStudent(long index){
        return Arrays.stream(students)
                .filter(s -> s.getIndex() == index)
                .findFirst().get();
    }

    public double getAverageNumberOfContacts(){
        return Arrays.stream(students)
                .mapToInt(Student::contactsLength)
                .average()
                .getAsDouble();
    }

    public Student getStudentWithMostContacts(){
        return Arrays.stream(students)
                .max(Comparator.comparingInt(Student::contactsLength)
                        .thenComparing(Student::getIndex))
                .get();
    }

    @Override
    public String toString() {
        String result = String.format("{\"fakultet\":\"%s\", \"studenti\":[", name);
        result += Arrays.stream(students).
                map(Student::toString)
                .collect(Collectors.joining(", ")) + "]}";
        return result;
    }

}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Item latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0&&faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
