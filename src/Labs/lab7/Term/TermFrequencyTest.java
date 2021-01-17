package Labs.lab7.Term;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


class TermFrequency {

    private Map<String, Integer> wordAppearanceByWord;
    private Set<String> ignoreWords;

    public TermFrequency(InputStream in, String[] stopWords) {
        wordAppearanceByWord = new TreeMap<>();
        ignoreWords = new HashSet<>(Arrays.asList(stopWords));

        Scanner sc = new Scanner(in);


        while (sc.hasNext()) {
            String word = processWord(sc.next());

            if(!(word.isEmpty()) && !ignoreWords.contains(word)) {
                wordAppearanceByWord.putIfAbsent(word, 0);
                wordAppearanceByWord.computeIfPresent(word, (k, v) -> v+=1);
            }
        }
    }

    public void printMap() {
        wordAppearanceByWord.forEach((k, v) -> System.out.println(k + " = " + v));
    }

    private String processWord(String word) {
        return word.toLowerCase().replace(',', '\0').replace('.', '\0').trim();
    }

    public int countTotal() {
        return wordAppearanceByWord.values()
                .stream()
                .mapToInt(i ->i)
                .sum();
    }

    public int countDistinct() {
        return wordAppearanceByWord.size();
    }

    public List<String> mostOften(int k) {
        return wordAppearanceByWord.keySet()
                .stream()
                .sorted(Comparator.comparing(k2 -> wordAppearanceByWord.get(k2)).reversed())
                .limit(k)
                .collect(Collectors.toList());
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
        //tf.printMap();
    }
}
// vasiot kod ovde
