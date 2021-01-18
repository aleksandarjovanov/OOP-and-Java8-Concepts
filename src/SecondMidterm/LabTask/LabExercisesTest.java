package SecondMidterm.LabTask;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student {
    private String index;
    private List<Integer> points;
    private String signature;
    public static Comparator<Student> ascendingComparator = Comparator.comparingDouble(Student::summaryPoints)
            .thenComparing(Student::getIndex);
    public static Comparator<Student> descendingComparator = Comparator.comparingDouble(Student::summaryPoints)
            .thenComparing(Student::getIndex).reversed();

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
        setSignature();
    }

    private void setSignature() {
        if(points.size() < 8)
            signature = "NO";
        else
            signature = "YES";
    }

    public double summaryPoints() {
        return (double) points.stream()
                .mapToInt(i -> i)
                .sum() / 10;
    }

    public String getIndex() {
        return index;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, signature, summaryPoints());
    }
}

class LabExercises {
    private List<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        if(ascending){
            students.stream()
                    .sorted(Student.ascendingComparator)
                    .limit(n)
                    .forEach(System.out::println);
        }
        else students.stream()
                .sorted(Student.descendingComparator)
                .limit(n)
                .forEach(System.out::println);
    }

    public List<Student> failedStudents() {
        return students.stream()
                .filter(s -> s.getSignature().equals("NO"))
                .sorted(Comparator.comparing(Student::getIndex).thenComparingDouble(Student::summaryPoints))
                .collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        return students.stream()
                .filter(s -> s.getSignature().equals("YES"))
                .collect(Collectors.groupingBy(k -> getStudentYear(k.getIndex()), Collectors.averagingDouble(Student::summaryPoints)));
    }

    private static Integer getStudentYear(String index) {
        return 20 - Integer.parseInt(index.substring(0, 2));
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}