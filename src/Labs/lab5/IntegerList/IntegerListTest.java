package Labs.lab5.IntegerList;

import java.util.*;
import java.util.stream.IntStream;

class IntegerList{

    private List<Integer> integers;

    public IntegerList() {
        this.integers = new ArrayList<>();
    }

    public IntegerList(Integer... numbers) {
        this.integers = new ArrayList<>(Arrays.asList(numbers));
    }

    public void add(int el, int idx) {
        if (idx > size())
            IntStream.range(size(), idx).forEach(i -> integers.add(0));

        integers.add(idx, el);
    }

    public int remove(int idx) {
        if(idx < 0 || idx > this.integers.size()) throw new ArrayIndexOutOfBoundsException();
        return integers.remove(idx);
    }

    public int size() {
        return integers.size();
    }

    public void set(int e1, int idx){
        if(idx < 0 || idx > this.integers.size()) throw new ArrayIndexOutOfBoundsException();
        this.integers.set(idx, e1);     // different from add, it deletes the element at idx position.
    }

    public int get(int idx){
        if(idx < 0 || idx > this.integers.size()) throw new ArrayIndexOutOfBoundsException();
        return integers.get(idx);
    }

    public int count(int el){
        return (int) integers.stream().filter(i -> i == el).count();
    }

    public void removeDuplicates(){

        for(int i = 0; i < this.integers.size(); i++){
            int lastIdx = this.integers.lastIndexOf(this.integers.get(i));      //lastIndexOf
            if(lastIdx != i){
                this.integers.remove(i);
                i --;                           // bcz when removing list shifts left
            }
        }

//        Collections.reverse(elements);
//        this.elements = this.elements.stream().distinct().collect(Collectors.toCollection(ArrayList::new));   // bcz ArrayList = ArrayList
//        Collections.reverse(elements);
    }

    public int sumFirst(int k){
        if(k > this.integers.size() - 1){
            k = this.integers.size();
        }
        return IntStream.range(0, k).map(i -> integers.get(i)).sum();
    }
//      return this.elements.stream()
//                .limit(k)
//                .mapToInt(Integer::valueOf)
//                .sum();

    public int sumLast(int k){
        return IntStream.range(this.integers.size() - k,this.integers.size()).map(i -> this.integers.get(i)).sum();
//        return this.elements.stream()
//                .skip(this.elements.size()-k)
//                .mapToInt(Integer::valueOf)
//                .sum();
    }

    public void shiftRight(int idx, int k) {
        int newIndex = (idx + k) % integers.size(); // modul za da kruzi        // nemam poima
        int number = integers.remove(idx);
        integers.add(newIndex, number);

    }

    void shiftLeft(int idx, int k){
        int size = integers.size();
        int number = this.integers.remove(idx);
        if(k >= size)
            k -= size * (k/size);
        int newPosition = (idx + size - k) % size;
        integers.add(newPosition, number);
    }

    public IntegerList addValue(int value){
        Integer [] array = this.integers.stream().map(i -> i+value).toArray(Integer[]::new);
        return new IntegerList(array);
    }

}




public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}
