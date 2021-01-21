package SecondMidterm.UniqNames;

import java.util.*;
import java.util.stream.Collectors;

class NameStatistic {
    private String name;
    private int appearance;
    private int numUniqLetters;

    public NameStatistic(String name) {
        this.name = name;
        calculateNumUniqLetters();
    }

    private void calculateNumUniqLetters() {
        numUniqLetters = (int) name.toLowerCase()
                .chars()
                .distinct()
                .count();
    }

    @Override
    public String toString() {
        return String.format("%s (%d) %d", name, appearance, numUniqLetters);
    }

    public void incrementAppearance() {
        appearance++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAppearance() {
        return appearance;
    }

    public void setAppearance(int appearance) {
        this.appearance = appearance;
    }

    public int getUniqLetters() {
        return numUniqLetters;
    }

    public void setUniqLetters(int uniqLetters) {
        this.numUniqLetters = uniqLetters;
    }
}

class Names {
    private Map<String, NameStatistic> appearanceByName;

    public Names() {
        appearanceByName = new TreeMap<>();
    }

    public void addName(String name) {
        appearanceByName.putIfAbsent(name, new NameStatistic(name));
        appearanceByName.computeIfPresent(name, (k, v) -> {
            v.incrementAppearance();
            return v;
        });
    }

    public void printN(int n) {
        appearanceByName.values()
                .stream()
                .filter(o -> o.getAppearance() >= n)
                .forEach(System.out::println);
    }

    public String findName(int len, int x) {
        List<String> lista = appearanceByName.keySet().stream().filter(name -> name.length() < len).collect(Collectors.toList());
        return lista.get(x % lista.size());
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde