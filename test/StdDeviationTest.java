import fr.epita.mnist.datamodel.Image;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static fr.epita.mnist.services.CentroidClassifier.trainCentroids;
import static fr.epita.mnist.services.CentroidClassifier.trainStdCentroids;
import static fr.epita.mnist.services.ImageReader.*;

public class StdDeviationTest {
    public static void main(String args[]) throws IOException {
        List<Image> images = new ArrayList<>();
        images = read(new File("./mnist_sample_test.csv"));
/**        sends the lists of images to find its distribution to the function newDistribution(List<Images>) **/
        Map<Integer,Integer> valuesOfDistribution = new LinkedHashMap<>();
        valuesOfDistribution = newDistribution(images);
/**     Ordering the distribution in ascending order **/
        Set<Integer> keysDistribution = valuesOfDistribution.keySet();

/** ************************************** ASSOCIATING THE KEY AND ITS CENTROID VALUE ************************************ **/

        /** Getting all the images of each class in the distribution and grouping them together.
         * Eg: if key=0, it gets all the images of '0' and groups them together to send it to the centroid function
         * **/
        Map<Integer, double[][]> KeyAndCentroid = new LinkedHashMap<>();
        for(Integer key: keysDistribution){
            List<Image> groupimage = new ArrayList<>();
            for(Image image:images){
                if(key==image.getLabel()){
                    groupimage.add(image);
                }
            }
/**            ********** SENDING THE IMAGES TO GET CENTROIDS **************  **/
//            double[][] centroidval = trainCentroids(groupimage);
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            double[][] centroidval1 = trainStdCentroids(groupimage);
            KeyAndCentroid.put(key,centroidval1);
    }
        int Testlabel = 2;
        List<Image> groupzero = getTestImages(Testlabel);

        //        System.out.println(groupzero);

        double predictedOutcome = predict(KeyAndCentroid, groupzero);

    }
}
