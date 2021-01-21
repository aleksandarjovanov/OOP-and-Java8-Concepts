package SecondMidterm.Components;

import java.awt.*;
import java.util.*;

class Component implements Comparable<Component> {
    private String color;
    private int weight;
    private Set<Component> componentSet;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        componentSet = new TreeSet<>();
    }

    public void addComponent(Component component) {
        componentSet.add(component);
    }

    public void changeColor(int weight, String color) {
        if(this.weight < weight)
            this.color = color;
        componentSet.forEach(component -> component.changeColor(weight, color));
    }

    public String format(String crti) {
        String s = String.format("%s%d:%s\n", crti, weight, color);
        for (Component component : componentSet) {
            s+=component.format(crti+"---");
        }
        return s;
    }
    @Override
    public String toString() {
        return format("");
    }

    @Override
    public int compareTo(Component component) {
        return Comparator.comparingInt(Component::getWeight).thenComparing(Component::getColor).compare(this, component);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

}

class Window {
    private String name;
    private Map<Integer, Component> componentsByPosition;

    public Window(String name) {
        this.name = name;
        componentsByPosition = new TreeMap<>();
    }

    public void addComponent(int position, Component component) throws InvalidPositionException {
        if(componentsByPosition.containsKey(position))
            throw new InvalidPositionException(position);
        componentsByPosition.put(position, component);
    }

    public void changeColor(int weight, String color) {
        componentsByPosition.values().forEach(component -> component.changeColor(weight, color));
    }


    public void swichComponents(int pos1, int pos2) {
        Component tmp = componentsByPosition.get(pos1);
        componentsByPosition.put(pos1, componentsByPosition.get(pos2));
        componentsByPosition.put(pos2, tmp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW "+name+"\n");
        componentsByPosition.entrySet().forEach(entry -> sb.append(entry.getKey()+ ":" + entry.getValue()));
        return sb.toString();
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

class InvalidPositionException extends Exception {
    public InvalidPositionException(int position) {
        super(String.format("Invalid position %d, alredy taken!", position));
    }
}

// вашиот код овде