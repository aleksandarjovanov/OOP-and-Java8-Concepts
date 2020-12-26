package firstMidtermExam.zad1;


import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

abstract class Item{
    protected String name;
    protected int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public abstract void scalePrice();

    public abstract String getType();
}

class Cake extends Item{

    public Cake(String name, int price) {
        super(name, price);
    }

    @Override
    public void scalePrice() {
        return;
    }

    @Override
    public String getType() {
        return "C";
    }
}

class Pita extends Item{

    public Pita(String name, int price) {
        super(name, price);
    }

    @Override
    public void scalePrice() {
        this.price += 50;
    }

    @Override
    public String getType() {
        return "P";
    }
}

class Order implements Comparable<Order>{
    private int orderId;
    private List<Item> items;
    private int minOrderItems;

    public Order(int orderId, int minOrderItems) {
        this.orderId = orderId;
        this.minOrderItems = minOrderItems;
        items = new ArrayList<>();
    }

    public int calculateSum(){
        return items.stream().mapToInt(Item::getPrice).sum();
    }

    public List<Item> getItems() {
        return items;
    }

    public int getOrderId() {
        return orderId;
    }



    @Override
    public String toString() {
        return String.format("%d %d %d %d %d", orderId, items.size(), numOfPies(), numOfCakes(), calculateSum());
    }

    private int numOfCakes() {
        return (int) items.stream().filter(item -> item.getType().equals("C")).count();
    }

    private int numOfPies() {
        return (int) items.stream().filter(item -> item.getType().equals("P")).count();
    }

    @Override
    public int compareTo(Order o) {
        return Integer.compare(this.calculateSum(), o.calculateSum());
    }
}

class CakeShopApplication{

    List<Order> orders;
    private int minOrderItems;

    public CakeShopApplication(int minOrderItems) {
        this.minOrderItems = minOrderItems;
        orders = new ArrayList<>();
    }

    public int readCakeOrders(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        orders = br.lines().map(line -> {
            try{
                return mapToOrder(line);
            }catch (InvalidOrderException e){
                System.out.println(e.getMessage());
            }
            return null;
        }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return orders.stream().mapToInt(order -> order.getItems().size()).sum();
    }

    private Order mapToOrder(String line)throws InvalidOrderException{

        String [] parts = line.split("\\s+");
        int orderId =  Integer.parseInt(parts[0]);
        Order order = new Order(orderId, this.minOrderItems);
        Item item = null;

        for(int i = 1; i < parts.length; i+=2){
            if(parts[i].contains("C"))
                 item = new Cake(parts[i], Integer.parseInt(parts[i+1]));
            else
                item = new Pita(parts[i], Integer.parseInt(parts[i+1]));

            order.getItems().add(item);
        }

        if(order.getItems().size() < minOrderItems) throw new InvalidOrderException(order.getOrderId());

        return order;
    }

    public void printLongestOrder(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);

        Order maxOrder = orders.stream().max(Comparator.comparingInt(o -> o.getItems().size())).get();
        pw.print(maxOrder);
        pw.flush();
    }

    public void printAllOrders(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);

        scalePita();

        orders.stream().sorted(Comparator.naturalOrder()).forEach(pw::print);
        pw.flush();
    }

    public void scalePita(){
        orders.forEach(order -> order.getItems()
                .forEach(Item::scalePrice));
    }
}

class InvalidOrderException extends Exception{
    public InvalidOrderException(int orderId) {
        super(String.format("The order with id %d has less items than the minimum allowed.", orderId));
    }
}


public class CakeShopApplicationTest1 {

    public static void main(String[] args) {
        CakeShopApplication cakeShopApplication = new CakeShopApplication(4);

        System.out.println("--- READING FROM INPUT STREAM ---");
        cakeShopApplication.readCakeOrders(System.in);

        System.out.println("--- PRINTING TO OUTPUT STREAM ---");
        cakeShopApplication.printAllOrders(System.out);
    }
}