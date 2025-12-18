import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Sample02 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

    //    while (true) {
            System.out.print("Enter input file name: ");
            String fileName = scanner.nextLine();

            try {
                FileReader reader = new FileReader(fileName);
                System.out.println("File opened successfully!");
            //    break; // exit loop if file is correct
            } catch (FileNotFoundException e) {
                System.out.println("File not found. Please try again.");
            }
        //}

        scanner.close();
    }
}
