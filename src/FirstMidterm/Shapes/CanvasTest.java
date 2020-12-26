package FirstMidterm.Shapes;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract class Shape implements Comparable<Shape>{

    private String id;

    public Shape(String id) {
        this.id = id;
    }

    public abstract double area();
    public abstract double perimeter();
    public abstract void scale(double coef);

    public static Shape createShape(String [] parts) throws InvalidDimensionException {
        int marker = Integer.parseInt(parts[0]);
        String id = parts[1];
        double side = Double.parseDouble(parts[2]);
        switch (marker){
            case (1): return new Circle(id, side);
            case (2): return new Square(id, side);
            case (3): return new Rectangle(id, side, Double.parseDouble(parts[3]));
            default: return null;
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(Shape o) {
        return Double.compare(this.area(), o.area());
    }
}

class Circle extends Shape{

    private double radius;

    public Circle(String id, double radius) throws InvalidDimensionException {
        super(id);
        if(radius == 0.0) throw new InvalidDimensionException();
        this.radius = radius;
    }

    @Override
    public double area() {
        return radius * radius * Math.PI;
    }

    @Override
    public double perimeter() {
        return 2 * radius * Math.PI;
    }

    @Override
    public void scale(double coef) {
        this.radius *= coef;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f", radius, area(), perimeter());
    }
}

class Square extends Shape{

    private double side;

    public Square(String id, double side) throws InvalidDimensionException {
        super(id);
        if(side == 0) throw new InvalidDimensionException();
        this.side = side;
    }

    @Override
    public double area() {
        return side * side;
    }

    @Override
    public double perimeter() {
        return 4 * side;
    }

    @Override
    public void scale(double coef) {
        this.side *= coef;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f", side, area(), perimeter());
    }
}

class Rectangle extends Shape{

    private double a_side;
    private double b_side;

    public Rectangle(String id, double a_side, double b_side) throws InvalidDimensionException {
        super(id);
        if(a_side == 0 || b_side == 0) throw new InvalidDimensionException();
        this.a_side = a_side;
        this.b_side = b_side;
    }

    @Override
    public double area() {
        return a_side * b_side;
    }

    @Override
    public double perimeter() {
        return 2 * (a_side + b_side);
    }

    @Override
    public void scale(double coef) {
        this.a_side *= coef;
        this.b_side *= coef;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f", a_side, b_side, area(), perimeter());
    }
}

class Canvas{
    List<Shape> shapes;

    public Canvas() {
        shapes = new ArrayList<>();
    }

    public void readShapes (InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null){
            String [] parts = line.split("\\s+");
            String id = parts[1];
            try {
                if(id.length() != 6 || !isAlphaNumeric(id)) throw new InvalidIDException(id);
                Shape shape = Shape.createShape(parts);
                shapes.add(shape);
            }catch (InvalidIDException msg){
                System.out.println(msg);
            }catch (InvalidDimensionException msg){
                System.out.println(msg);
                break;
            }
        }
    }

    private boolean isAlphaNumeric(String id){
        return IntStream.range(0,id.length()).allMatch(idx -> Character.isLetterOrDigit(id.charAt(idx)));
    }


    public void scaleShapes(String usedId, double coef){
        shapes.stream()
                .filter(shape -> shape.getId().equals(usedId))
                .forEach(shape -> shape.scale(coef));
    }

    public void printAllShapes(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        //shapes.sort(Comparator.naturalOrder());
        shapes.forEach(pw::println);
        pw.flush();
    }

    public void printByUserId(OutputStream os){
//        PrintWriter pw = new PrintWriter(os);
//
//        Map<String, List<Shape>> shapesById = shapes.stream()
//                .collect(
//                        Collectors.groupingBy(Shape::getId)
//                );
    }

    public void statistics(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        DoubleSummaryStatistics ds = shapes.stream()
                .mapToDouble(Shape::area)
                .summaryStatistics();

        pw.print(String.format("count: %d\n" +
                "sum: %.2f\n" +
                "min: %.2f\n" +
                "avg: %.2f\n" +
                "max: %.2f", ds.getCount(), ds.getSum(), ds.getMin(), ds.getAverage(), ds.getMax()));
        pw.flush();

    }
}

public class CanvasTest {

    public static void main(String[] args) throws IOException {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        canvas.readShapes(System.in);

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}

class InvalidIDException  extends Exception{
    public InvalidIDException(String id) {
        super("ID " + id + " is not valid");
    }
}

class InvalidDimensionException extends Exception{
    public InvalidDimensionException() {
        super("Dimension null is not allowed");
    }
}