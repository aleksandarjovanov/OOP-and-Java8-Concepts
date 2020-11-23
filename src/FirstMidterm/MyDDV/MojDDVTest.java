package FirstMidterm.MyDDV;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class Item{

    enum TaxType{
        TYPE_A,
        TYPE_B,
        TYPE_V
    }

    public static final double taxForItemByState = 0.15;

    private double price;
    private TaxType type;
    private float taxReturn;
    private double taxPercentage;

    public Item(String price, String type) {
        this.price = Double.parseDouble(price);
        setType(type);
        calculateTaxReturn();
    }

    private void calculateTaxReturn() {
        this.taxReturn = (float) (price * taxForItemByState * taxPercentage);
    }

    public double getTaxReturn() {
        return taxReturn;
    }

    public double getPrice() {
        return price;
    }

    private void setType(String type) {
        switch (type){
            case "A":
                this.type = TaxType.TYPE_A;
                this.taxPercentage = 0.18;
                break;
            case "B":
                this.type = TaxType.TYPE_B;
                this.taxPercentage = 0.05;
                break;
            case "V":
                this.type = TaxType.TYPE_V;
                this.taxPercentage = 0.0;
                break;
        }
    }
}

class Fiscal {

    private int id;
    private List<Item> items;
    private double sumOfItems;
    private float taxReturn;

    public Fiscal(int id) {
        this.id = id;
        taxReturn = 0.0f;
        items = new ArrayList<>();
    }

    public void addToItems(Item item) {
        items.add(item);
        this.taxReturn += item.getTaxReturn();
    }

    public void calculateSumOfItems() {
        this.sumOfItems = items.stream()
                .mapToDouble(Item::getPrice)
                .sum();
    }

    public double getSumOfItems() {
        return sumOfItems;
    }

    public float getTaxReturn() {
        return taxReturn;
    }

    @Override
    public String toString() {
        return String.format("%10d\t%10d\t%10.5f", id, (int)sumOfItems, taxReturn);
    }
}


class MojDDV{

    private List<Fiscal> fiscals;
    private DoubleSummaryStatistics ds;

    public MojDDV() {
        fiscals = new ArrayList<>();
        ds = new DoubleSummaryStatistics();
    }

    public void readRecords (InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        this.fiscals = br.lines()
                .map(line -> {
                    try {
                        return createFiscal(line);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }



    private Fiscal createFiscal(String line) throws AmountNotAllowedException {
        String [] parts = line.split("\\s+");
        Fiscal fiscal = new Fiscal(Integer.parseInt(parts[0]));
        for(int i = 1; i < parts.length; i+=2){
            Item item = new Item(parts[i], parts[i+1]);
            fiscal.addToItems(item);
        }
        fiscal.calculateSumOfItems();
        if(fiscal.getSumOfItems() > 30000) throw new AmountNotAllowedException("Receipt with amount " +(int)fiscal.getSumOfItems() + " is not allowed to be scanned");
        ds.accept(fiscal.getTaxReturn());
        return fiscal;
    }

    public void printTaxReturns (OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);

        fiscals.forEach(pw::println);

        pw.flush();
    }

    public void printStatistics (OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);

        pw.print(toString());

        pw.flush();
        pw.close();
    }

    @Override
    public String toString() {
        return String.format("min:\t%5.3f\n" +
                "max:\t%5.3f\n" +
                "sum:\t%5.3f\n" +
                "count:\t%-5d\n" +
                "avg:\t%5.3f", ds.getMin(), ds.getMax(), ds.getSum(), ds.getCount(), ds.getAverage());


    }
}

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(String message) {
        super(message);
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}