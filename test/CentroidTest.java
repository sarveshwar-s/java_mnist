import fr.epita.mnist.datamodel.Image;

import java.util.Arrays;
import java.util.List;

public class CentroidTest {

    public static void trainCentroids(List<Image> allimages){
        int sizeOfImages = allimages.size();
//        we know that the image size is 28x28 and in current dataset we have 10 images in the list
        double[][] centroidValue = new double[28][28];
        for(int i=0;i<2;i++){
            System.out.println("**************************************************************************************");
            double[][] currentImage = allimages.get(i).getData();
//            int count = 0;
            for(int m=0;m<28;m++) {
                for (int n = 0; n < 28; n++) {
                    centroidValue[m][n] = centroidValue[m][n] + currentImage[m][n];
                    System.out.println(centroidValue[m][n]);
                }
                System.out.println("____________________________________________________________________");
            }
//            System.out.println(count);
        }
        double[][] currentImageFirst = allimages.get(0).getData();
        double[][] currentImageSecond = allimages.get(1).getData();
        for(int m=6;m<7;m++) {
            for (int n = 0; n < 28; n++) {
                System.out.print(currentImageFirst[m][n]);
            }
            System.out.println("\n");
            for (int n = 0; n < 28; n++) {
                System.out.print(currentImageSecond[m][n]);
            }
            System.out.println("\n");
            for (int n = 0; n < 28; n++) {
                System.out.print(centroidValue[m][n] + " ");
            }

        }
//Taking the average value of each index in the centroid matrix to find the average representant
        System.out.println(Arrays.toString(centroidValue[0]));
        System.out.println(sizeOfImages);
        for(int l=0;l<28;l++){
            for(int k=0;k<28;k++){
                centroidValue[l][k] = centroidValue[l][k] / sizeOfImages;
            }
        }
        System.out.println(Arrays.toString(centroidValue[0]));

    }

}
