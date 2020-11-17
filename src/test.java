import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;


public class test {



    public static double statistic(List<? extends Number> list){

        double sum = 0;
        for(Number t : list){
            sum += t.doubleValue();
        }
        return sum;
    }

    public static void main(String[] args) {

        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4));

        statistic(list);



    }
}
