import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class DistributionTest {
    //    Calculating the number of occurences of each digit
    public static int[] calculateDistribution(int[] numrange) {
//        digit range from 0 to 9
//        int[] numrange = {0,1,2,3,4,5,7,9,6,7,0,0,8,9};
        int[] countMonitor = new int[10];
        HashMap<Integer, Integer> distribution = new HashMap<>();
        for (int i = 0; i < numrange.length; i++) {
            int count = 0;
            for (int j = i + 1; j < numrange.length; j++) {
                if (numrange[i] == numrange[j] && numrange[j] != -1) {
                    System.out.println(numrange[i]);
                    count += 1;
                    numrange[j] = -1;
                }
            }
//            updating the count to the countMonitor
            if (numrange[i] != -1) {
                countMonitor[numrange[i]] = count;
                distribution.put(numrange[i], countMonitor[numrange[i]]);
            }

        }
//        System.out.println(countMonitor);
        for (Integer i : distribution.keySet()) {
            System.out.println("Number: " + i + " Frequency: " + distribution.get(i));
        }
        return countMonitor;
    }

    public static void main(String[] args) {
//        List<Image> images = new ArrayList<>();
//        images = read(new File("./mnist_sample_test.csv"));
//
////        Getting all the labels list from the dataset
//        int[] allLables = new int[images.size()];
//        for (int i = 0; i < images.size(); i++) {
//            System.out.println(images.get(i).getLabel());
//            allLables[i] = images.get(i).getLabel();
//        }
////        Calculating the distribution of each label in the dataset
//        int[] distribution = calculateDistribution(allLables);
//        System.out.println(Arrays.toString(distribution));

    }
}
