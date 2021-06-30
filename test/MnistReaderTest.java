import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class MnistReaderTest {


    public static final int IMAGE_HEIGHT = 28;
    public static final int IMAGE_WIDTH = 28;

    public static void main(String[] args) throws IOException {
//        Reads all the lines from the file mnist sample csv
        List<String> lines = Files.readAllLines(new File("./mnist_sample_test.csv").toPath());

        //remove the header since it contains only the labels
        lines.remove(0);

        String line = lines.get(0);
        //System.out.println(line);

//        Splitting the dataset based on comma
        String[] parts = line.split(",");
        System.out.println(Arrays.toString(parts));
        System.out.println("Length of each line " + parts.length);
//        The index 0 of the array is the label of the image
        String label = parts[0];  // Storing the label of the image in the variable label

        System.out.println(parts.length);  // 785 (28x28)+1

        int data_line_length = parts.length - 1; // the length of the image excluding the label in index 0
        System.out.println(Math.sqrt(data_line_length)); // this will output 28

//      Creating the image matrix with image height and width as size of the matrix
        String[][] matrix = new String[IMAGE_HEIGHT][IMAGE_WIDTH];  // the image width and height is fixed here.

        //for int i = 0 to i = 27
        // for int j = 0 to j = 27
        // matrix[i][j] = parts[i * 28 + j]
// Function getmatrix reads the mnist file and gets it as a matrix
        getMatrix(parts, matrix);

        System.out.println(label);





    }
// For every 28 variables its a new row.
    private static String[][] getMatrix(String[] parts, String[][] matrix) {
        for (int i = 0; i < IMAGE_HEIGHT; i ++){
//            Looping through the matrix and storing the data rowise. every set of 28 is a row.
            for (int j = 0; j < IMAGE_WIDTH; j++){
                matrix[i][j]  = parts[i * IMAGE_HEIGHT + j];
                String display = "..";
                if (Double.parseDouble(matrix[i][j]) > 100){
                    display = "xx";
                }
                System.out.print(display);
            }
            System.out.println();
        }
        return matrix;
    }

}