package Labs.lab7.Anagrams;

import java.io.InputStream;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Anagrams {


    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {

        Scanner scanner = new Scanner(inputStream);
        HashMap<String, LinkedList<String>> anagramsBySortedWord = new HashMap<>(); // nema potreba od comparator, klucot e string koriste go default comparable

        while (scanner.hasNext()) {
            String word = scanner.nextLine();
            String sortedWord = sortWord(word);
            anagramsBySortedWord.putIfAbsent(sortedWord, new LinkedList<>());
            anagramsBySortedWord.get(sortedWord).add(word);
        }

        anagramsBySortedWord.values()
                .stream()
                .filter(set -> set.size() > 4)
                .sorted(Comparator.comparing(LinkedList::getFirst))
                .forEach(list -> System.out.println(list
                        .stream()
                        .collect(Collectors.joining(" "))));

    }

    private static String sortWord(String word) {
        char [] letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }
}
