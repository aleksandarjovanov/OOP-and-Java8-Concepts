package Labs.lab3.PizzaOrderTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.IntStream;

/* Exceptions */
class InvalidPizzaTypeException extends Exception{
    public InvalidPizzaTypeException() {
    }
}

class InvalidExtraTypeException extends Exception{
    public InvalidExtraTypeException() {
    }
}

class ItemOutOfStockException extends Exception{
    public ItemOutOfStockException(Item item) {
    }
}

class ArrayIndexOutOfBоundsException extends Exception{
    public ArrayIndexOutOfBоundsException(int idx) {
    }
}

class EmptyOrder extends Exception{
    public EmptyOrder() {
    }
}

class OrderLockedException extends Exception{
    public OrderLockedException() {
    }
}

interface Item{
    int getPrice();

    boolean equals(Object o);

    String getType();

}

class ExtraItem implements Item{

    private String type;
    private int price;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        this.type = type;
        setPrice();
    }

    public void setPrice() throws InvalidExtraTypeException {
        switch (type){
            case ("Ketchup"): this.price = 3; break;
            case ("Coke"): this.price = 5; break;
            default: throw new InvalidExtraTypeException();
        }
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtraItem)) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return type.equals(extraItem.type);
    }
}

class PizzaItem implements Item{

    private String type;
    private int price;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        this.type = type;
        setPrice();
    }

    public void setPrice() throws InvalidPizzaTypeException {
        switch (type){
            case ("Standard"): this.price = 10; break;
            case ("Pepperoni"): this.price = 12; break;
            case ("Vegetarian"): this.price = 8; break;
            default: throw new InvalidPizzaTypeException();
        }
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PizzaItem)) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return type.equals(pizzaItem.type);
    }
}

class Product{
    private Item item;
    private int count;

    public Product(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return item.equals(product.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, count);
    }
}

class Order{

    private List<Product> products;
    private boolean locked;

    public Order(){
        products = new ArrayList<>();
        locked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {

        if (this.locked) throw new OrderLockedException();
        if(count > 10) throw new ItemOutOfStockException(item);

        Product product = new Product(item, count);
        if(products.contains(product)){
            products.get(products.indexOf(product)).setCount(count);
        }
        else {
            products.add(product);
        }
    }

    public int getPrice(){
        return products.stream()
                .mapToInt(p -> p.getItem().getPrice() * p.getCount()).sum();
    }

    public void displayOrder(){
        IntStream.range(0, products.size())
                .forEach(i -> System.out.println(formString(products.get(i), i)));
        System.out.format("Total:%21d$\n", getPrice());
    }

    private String formString(Product p, int i) {
        return String.format("%3d.%-15sx%2d%5d$", i+1, p.getItem().getType(), p.getCount(), p.getItem().getPrice() * p.getCount());
    }



    public void removeItem(int i) throws ArrayIndexOutOfBоundsException, OrderLockedException {

        if (this.locked) throw new OrderLockedException();

        if(i >= products.size()) throw new ArrayIndexOutOfBоundsException(i);
        products.remove(i);
    }

    public void lock() throws EmptyOrder {
        if(products.isEmpty())
            throw new EmptyOrder();
        else
            this.locked = true;
    }

}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }
}
