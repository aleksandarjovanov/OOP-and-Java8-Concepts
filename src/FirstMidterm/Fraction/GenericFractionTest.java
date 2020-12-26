package FirstMidterm.Fraction;

import java.util.Scanner;

class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException() {
        super("Denominator cannot be zero");
    }
}

class GenericFraction<T extends Number,U extends Number>{
    private T numerator;
    private U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {

        if(denominator.doubleValue() == 0.0) throw new ZeroDenominatorException();

        this.numerator = numerator;
        this.denominator = denominator;
    }

    GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException{
        return simplify(new GenericFraction<>(
                numerator.doubleValue() * gf.denominator.doubleValue() + gf.numerator.doubleValue() * denominator.doubleValue(),
                denominator.doubleValue() * gf.denominator.doubleValue()));
    }

    public int GCD(int a, int b) {
        if (b == 0) return a;
        return GCD(b,a%b);
    }

    GenericFraction<Double, Double> simplify(GenericFraction<Double, Double> gf) throws ZeroDenominatorException{
        int gcd = GCD(gf.numerator.intValue(), gf.denominator.intValue());
        gf.denominator /= gcd;
        gf.numerator /= gcd;
        return gf;
    }

    public double toDouble(){
        return numerator.doubleValue() / denominator.doubleValue();
    }

    public String toString(){
        return String.format("%.2f / %.2f", numerator, denominator);
    }
}


public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}
