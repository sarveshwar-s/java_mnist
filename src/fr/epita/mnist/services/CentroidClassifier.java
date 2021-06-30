package fr.epita.mnist.services;

import fr.epita.mnist.datamodel.Image;

import java.util.List;

public class CentroidClassifier {
    public static final int IMAGE_HEIGHT = 28;
    public static final int IMAGE_WIDTH = 28;

    /**
     * Calculates the centroid for any set of images sent to the function
     * Returns the centroid(average of index by index values) of each image**/
    public static double[][] trainCentroids(List<Image> allimages){
        int sizeOfImages = allimages.size();
//        we know that the image size is 28x28 and in current dataset we have 10 images in the list
        double[][] centroidValue = new double[28][28];

        /**
         * Taking the matrix of each image from Image class using getData
         * Performing index wise addition and storing the value in the global centroid matrix
         * **/
        for(int i=0;i<sizeOfImages;i++){
            double[][] currentImage = allimages.get(i).getData();
            for(int m=0;m<IMAGE_HEIGHT;m++) {
                for (int n = 0; n < IMAGE_WIDTH; n++) {
                    centroidValue[m][n] = centroidValue[m][n] + currentImage[m][n];
//                    System.out.println(centroidValue[m][n]);
                }
            }
        }
/**
 *        Taking the average value of each index in the centroid matrix
  */
        for(int l=0;l<IMAGE_HEIGHT;l++){
            for(int k=0;k<IMAGE_WIDTH;k++){
                centroidValue[l][k] = centroidValue[l][k] / sizeOfImages;
            }
        }
        return centroidValue;
    }


    public static double[][] trainStdCentroids(List<Image> allimages){
        int sizeOfImages = allimages.size();
//        we know that the image size is 28x28 and in current dataset we have 10 images in the list
        double[][] centroidValue = new double[IMAGE_HEIGHT][IMAGE_WIDTH];

        /**
         * Taking the matrix of each image from Image class using getData
         * Performing index wise addition and storing the value in the global centroid matrix
         * **/
        for(int i=0;i<sizeOfImages;i++){
            double[][] currentImage = allimages.get(i).getData();
            for(int m=0;m<IMAGE_HEIGHT;m++) {
                for (int n = 0; n < IMAGE_WIDTH; n++) {
                    centroidValue[m][n] = centroidValue[m][n] + currentImage[m][n];
//                    System.out.println(centroidValue[m][n]);
                }
            }
        }
        double[][] stdimage = new double[IMAGE_HEIGHT][IMAGE_WIDTH];
//        Taking the average value of each index in the centroid matrix
        for(int i=0;i<sizeOfImages;i++) {
            double[][] currentImage = allimages.get(i).getData();
            for (int l = 0; l < IMAGE_HEIGHT; l++) {
                for (int k = 0; k < IMAGE_WIDTH; k++) {
//                    calculating the mean of the each index for the set of images
                    centroidValue[l][k] = centroidValue[l][k] / sizeOfImages;
//                    taking the standard deviation of the image
                    stdimage[l][k] = stdimage[l][k] + (Math.pow(currentImage[l][k] - centroidValue[l][k], 2.0));
                }
            }
        }

//        finding the standard deviation
            for (int l = 0; l < IMAGE_HEIGHT; l++) {
                for (int k = 0; k < IMAGE_WIDTH; k++) {
                    stdimage[l][k] = Math.sqrt(stdimage[l][k]/sizeOfImages);
                    System.out.print(stdimage[l][k]);
                }
                System.out.println(" ");
        }
        return stdimage;
    }
}
