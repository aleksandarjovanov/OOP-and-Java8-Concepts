package FirstMidterm.MinAndMax;

import java.util.Scanner;
import java.io.*;
import java.util.*;

class MinMax<E extends Comparable<E>>{      //nemam poima so e fintaa so brojacot
    E maximum;
    E minimum;
    int countMax;
    int countMin;
    int counter;
    public MinMax(){
        maximum = null;
        countMax = 0;
        minimum = null;
        countMin = 0;
        counter = 0;
    }
    public void update(E el){
        if(maximum == null&&minimum == null){
            maximum = el;
            minimum = el;
            countMax++;
            countMin++;
            return;
        }
        if(maximum.equals(el) || minimum.equals(el)){
            if(maximum.equals(el))
                countMax++;
            else countMin++;
            return;
        }
        if(el.compareTo(maximum) < 0 && el.compareTo(minimum) > 0){
            counter++;
            return;
        }
        if(el.compareTo(maximum) > 0){
            if(!maximum.equals(minimum)){
                counter+=countMax;
                countMax = 1;
            }
            maximum = el;
            return;
        }
        if(el.compareTo(minimum) < 0){
            if(!maximum.equals(minimum)){
                counter+=countMin;
                countMin = 1;
            }
            minimum = el;
            return;
        }
    }
    public E max(){
        return maximum;
    }
    public E min(){
        return minimum;
    }
    public String toString(){
        return minimum.toString()+" "+maximum.toString()+" "+counter+"\n";
    }
}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}