package fr.epita.mnist.services;
import fr.epita.mnist.datamodel.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static fr.epita.mnist.services.CentroidClassifier.trainCentroids;
import static fr.epita.mnist.services.CentroidClassifier.trainStdCentroids;

/**
 * This will read the images
 */
public class ImageReader {

    public static final int IMAGE_HEIGHT = 28;
    public static final int IMAGE_WIDTH = 28;
/**
 * INPUT: file object containing the path of the dataset.
 * The read function takes the data from the mnist dataset and converts each image into a matrix (28x28)
 * OUTPUT: provides the image object containing image label and its corresponding 28x28 matrix value*/

    public static List<Image> read(File file) throws IOException {
        List<Image> images = new ArrayList<>();
        List<String> lines = Files.readAllLines(file.toPath());
        //remove the header
        lines.remove(0);

        for (String line : lines) {
            String[] parts = line.split(",");
            int label = Integer.parseInt(parts[0]);
            double[][] matrix = convertToMatrix(parts);
            Image image = new Image(matrix, label);
            images.add(image);
        }


        return images;
    }
/**
 * the convertToMatrix converts the single line data into a 28x28 matrix
 * RETURNS the matrix form of the provided image
 * */
    private static double[][] convertToMatrix(String[] parts) {
        double[][] matrix = new double[IMAGE_HEIGHT][IMAGE_WIDTH];
        for (int i = 0; i < IMAGE_HEIGHT; i++) {
            for (int j = 0; j < IMAGE_WIDTH; j++) {
                matrix[i][j] = Double.parseDouble(parts[i * IMAGE_HEIGHT + j]);
                String display = "..";
                if (matrix[i][j] > 100){
                    display = "xx";
                }
                System.out.print(display);
            }
            System.out.println();
        }
        return matrix;
    }

    /**
     * INPUT: List of images(Image object)
     * This function calculates the distribution of the images in the dataset.
     * Returns a Map containing the Label and its number of occurences in the dataset (i.e) count
    **/
    public static Map<Integer, Integer> newDistribution(List<Image> images){
        Map <Integer, Integer> distributionMapValues = new LinkedHashMap<>();
// Sorting the labels using the sort operation. Since the labels are integers we can use the comparingInt method to sort the values in ascending order
        images.sort(Comparator.comparingInt(Image::getLabel));
        for(Image image: images){
            int currentLable = image.getLabel();  // Gets the label of each image in the list of images
//          the value is initiated to 1 if its the first occurence of the label in the distribution otherwise the count is incremented by 1.
            Integer count = distributionMapValues.computeIfAbsent(currentLable,vals->0)+1;
//            The count is associated with the corresponding label using the .put() function
            distributionMapValues.put(currentLable,count);
        }
//        System.out.println(distributionMapValues);
        return distributionMapValues;
    }

    public static void main(String[] args) throws IOException {
/**        ****************************** FINDING THE DISTRIBUTION OF IMAGES IN THE MNIST DATASET ******************************************** **/
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
            double[][] centroidval = trainCentroids(groupimage);
//            double[][] centroidval = trainStdCentroids(groupimage);
/** Mapping the centroid value of the image with its corresponding key value. This uses linkedhashmaps.*/
            KeyAndCentroid.put(key,centroidval);
        }

/** ***************************************END OF KEY AND CENTROID ASSOCIATION **********************************************************************  **/


        /**
         * F: 2 isolation of 10 occurences of 0
         * Getting first 10 occurences of "0" in the mnist test csv **/
        int Testlabel = 1;
        List<Image> groupzero = getTestImages(Testlabel);

        //        System.out.println(groupzero);
/**
 * INPUT: map of key and centroid, image.
 * Calling the predict function*/
        double predictedOutcome = predict(KeyAndCentroid, groupzero);

    }
    /**
     * GetTestImages is used to get the image data of the provided number from the mnist dataset
     * Returns a List of 10 Image objects of the requested digit
     **/
    public static List<Image> getTestImages(int Testlabel) throws IOException {
        List<Image> testimages = new ArrayList<>();
//        reads all the images from the mnist dataset
        testimages = read(new File("./mnist_test.csv"));
        List<Image> groupzero = new ArrayList<>();
        int testImagecount = 0;
        for(Image testimage: testimages){
//            Checks if the image is the image of the requested digit using getLabel() method
            if(testimage.getLabel()==Testlabel && testImagecount<10){
                groupzero.add(testimage);
                testImagecount+=1;
            }
        }
        return groupzero;
    }
/**
 * INPUT: Set of centroid matrices and image to be predicted
 * This function compares the provided image with every centroid image of the distribution and provides the label of the image
 * that has the least distance from its centroid to the provided image
 * Returns the predicted label of the provided image.**/
    public static double predict(Map<Integer, double[][]> KeyAndCentroid, List<Image> groupzero) {
        int count=0;
//        Defining a confusion matrix to measure the accuracy of the model
//        Looping through the set of images and comparing them with all the centroid values
        for(Image predimage: groupzero){
            double truePositive = 0;
            double trueNegative = 0;
            double falsePositive = 0;
            double falseNegative = 0;

            double[][] imgdata = predimage.getData();
            double min = 9999999;
            double predictedlabel=-1;
            int actualLabel = predimage.getLabel();
//            Take one instance of the image and compare with all instances of distribution(centroid matrices)
            for(Integer key: KeyAndCentroid.keySet()){
                double[][] currentCentroid = KeyAndCentroid.get(key); //gets the centroid matrix associated with each key ranging between 0 to 9
                double euclval = distance(imgdata,currentCentroid);
                if(euclval<min){
                    min = euclval;
                    predictedlabel = key;
                }
                /**    Calculating the True positive **/
                if((actualLabel==key)&&(predictedlabel==actualLabel)){
                    truePositive+=1;
                }
                /** Calculating the False positive **/
                else if((actualLabel!=key)&&(predictedlabel==actualLabel)){
                    falsePositive+=1;
                }
                /** Calculating the True Negative **/
                else if((actualLabel!=key) && (predictedlabel!=actualLabel)){
                    trueNegative+=1;
                }
                /** Calculating the False Negative **/
                else if((actualLabel==key) &&(predictedlabel!=actualLabel)){
                    falseNegative+=1;
                }
            }
            /**
             * Printing the Confusion matrix values(True Positive, True Negative
             , False Positive, False Negative
             **/
            System.out.println("True Positive="+truePositive+" True Negatives="+trueNegative+" False Negative="+falseNegative+" False Positive="+falsePositive);

            /**
             * The modelPerformance function calculates the accuracy, error rate, precision
             and recall based on the confusion matrix values
             **/
            modelPerformance(truePositive, trueNegative, falsePositive, falseNegative);
            /**
             * Prints the predicted Value of the provided image
             **/
            System.out.println("PredictedVal: " + predictedlabel);

//            Returns the predicted label value.
            return predictedlabel;
        }
        return 0;
    }

    /**
     * INPUT: True Positive,True Negative,False Positive, false Negative (Confusion matrix)
     * Evaluates the Accuracy, Error rate, precision and recall values based on the data of confusion matrix
     * **/
    private static void modelPerformance(double truePositive, double trueNegative, double falsePositive, double falseNegative) {
        double accuracy,errorRate,precision,recall = 0.0;
        accuracy = (truePositive + trueNegative)/(truePositive + falsePositive + falseNegative + trueNegative);
        errorRate = (falsePositive + falseNegative)/(truePositive + falsePositive + falseNegative + trueNegative);
        precision = truePositive /(truePositive + falsePositive);
        recall = truePositive /(truePositive + falseNegative);
        System.out.println("ACCURACY=" + accuracy + " errorRate=" + errorRate + " precision="+ precision + " recall="+recall);
    }

    /**
     * INPUT: two matrices of datatype double to find the distance between them
     * Finds the distance between the give image matrix and the centroid of the matrix.
     * Returns the value of square root of sum of values of the 2 matrices.
     **/
    public static double distance(double[][] matrix1,double[][] matrix2){
        double Sum = 0.0;
            for (int i = 0; i < IMAGE_HEIGHT; i++) {
                for (int j = 0; j < IMAGE_WIDTH; j++) {
                    Sum = Sum + Math.pow((matrix1[i][j] - matrix2[i][j]), 2.0);
                }
            }
            double finalval = Math.sqrt(Sum);
//        System.out.println(Math.sqrt(Sum));
        return finalval;
    }
}
