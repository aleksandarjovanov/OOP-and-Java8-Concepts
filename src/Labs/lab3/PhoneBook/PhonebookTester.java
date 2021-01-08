package Labs.lab3.PhoneBook;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InvalidNameException extends Exception {
    public String name;
    public InvalidNameException(String name) {
        super();
        this.name = name;
    }
}
class InvalidNumberException extends Exception {
    public InvalidNumberException() {
        super();
    }
}
class MaximumSizeExceddedException extends Exception {
    public MaximumSizeExceddedException() {
        super();
    }
}
class InvalidFormatException extends Exception {
    public InvalidFormatException() {
        super();
    }
}

class Contact{

    private String name;
    private String [] phoneNumbers;

    public Contact(String name, String... phoneNumbers) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        this.name = name;
        this.phoneNumbers = new String[phoneNumbers.length];

        if(!checkName(name)) throw new InvalidNameException(name);
        this.name = name;

        if(!Arrays.stream(phoneNumbers)
                .allMatch(this::checkNumber)) throw new InvalidNumberException();

        if(phoneNumbers.length > 5)
            throw new MaximumSizeExceddedException();

        IntStream.range(0, this.phoneNumbers.length)
                .forEach(i -> this.phoneNumbers[i] = phoneNumbers[i]);
    }

    private boolean checkName(String name) {
        return name.length() > 4&&name.length() < 10&&IntStream.range(0, name.length())
                .allMatch(i -> Character.isLetterOrDigit(name.charAt(i)));
    }

    private boolean checkNumber(String phone) {
        return phone.startsWith("07") && phone.length() == 9 &&
                phone.substring(2,3).matches("[0125678]") &&
                IntStream.range(0, phone.length())
                        .allMatch(i -> Character.isDigit(phone.charAt(i)));
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        return Arrays.stream(this.phoneNumbers)
                .sorted()
                .toArray(String[]::new);
    }

    public void addNumber(String phoneNumber) throws InvalidNumberException {
        if(!checkNumber(phoneNumber)) throw new InvalidNumberException();

        increaseArray();
        this.phoneNumbers[phoneNumbers.length - 1] = phoneNumber;
    }

    private void increaseArray(){
        String[] tmpArr = Arrays.copyOf(phoneNumbers, phoneNumbers.length);
        this.phoneNumbers = new String[tmpArr.length + 1];
        this.phoneNumbers = Arrays.copyOf(tmpArr, phoneNumbers.length);
    }

    @Override
    public String toString() {
        String nameAndNumberOfPNumbers = name + '\n' + phoneNumbers.length + '\n';
        String pNums = Arrays.stream(phoneNumbers)
                .sorted()
                .collect(Collectors.joining("\n"));
        return nameAndNumberOfPNumbers + pNums + "\n";
    }

    public static Contact valueOf(String s) throws InvalidFormatException {
        try {
            Scanner sc = new Scanner(s);

            String name = sc.next();
            String[] pNums = new String[sc.nextInt()];
            for (int i = 0; sc.hasNext(); i++)
                pNums[i] = sc.nextLine();
            return new Contact(name, pNums);
        }catch (Exception e){
            throw new InvalidFormatException();
        }
    }
}

class PhoneBook implements Serializable{

    private Contact [] contacts;

    public PhoneBook() {
        this.contacts = new Contact[0];
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if(contacts.length >= 250) throw new MaximumSizeExceddedException();
        if(Arrays.stream(contacts)
                .anyMatch(c -> c.getName().equals(contact.getName()))) throw new InvalidNameException(contact.getName());

        increaseArray();
        this.contacts[contacts.length - 1] = contact;
    }

    private void increaseArray(){
        Contact[] tmpArr = Arrays.copyOf(contacts, contacts.length);
        this.contacts = new Contact[tmpArr.length + 1];
        this.contacts = Arrays.copyOf(tmpArr, contacts.length);
    }

    public Contact getContactForName(String name){
        return Arrays.stream(contacts)
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public int numberOfContacts(){
        return this.contacts.length;
    }

    public Contact [] getContacts(){
        return Arrays.stream(contacts)
                .sorted(Comparator.comparing(Contact::getName))
                .toArray(Contact[]::new);
    }

    public boolean removeContact(String name){
        int index = extractIndexByGivenObject(name);
        if(index < 0) return false;

        this.contacts = IntStream.range(0, contacts.length)
                .filter(i -> i != index)
                .mapToObj(i -> contacts[i])
                .toArray(Contact[]::new);

        return true;
    }

    private int extractIndexByGivenObject(String name){
        return IntStream.range(0, contacts.length)
                .filter(i -> contacts[i].getName().equals(name))
                .findFirst()
                .orElse(-1);
    }

    @Override
    public String toString() {
        return Arrays.stream(getContacts())
                .map(Contact::toString)
                .collect(Collectors.joining("\n")) + "\n";
    }

    public static boolean saveAsTextFile(PhoneBook phonebook,String path)throws IOException {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(path));
            objectOutputStream.writeObject(phonebook.toString());
            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch (FileNotFoundException e){
            return false;
        }
        return true;
    }
    public static PhoneBook loadFromTextFile(String path) throws InvalidFormatException {
        PhoneBook phoneBook = new PhoneBook();
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(path));
            phoneBook = (PhoneBook) inputStream.readObject();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new InvalidFormatException();
        }
        return phoneBook;
    }

    public Contact[] getContactsForNumber(String number_prefix) {
        return Arrays.stream(contacts)
                .filter(contact -> (
                        IntStream.range(0, contact.getNumbers().length)
                                .anyMatch(i -> contact.getNumbers()[i].startsWith(number_prefix))
                ))
                .toArray(Contact[]::new);
    }

}

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}