import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CheckedExceptionNoCatch {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter input file name: ");
        String fileName = scanner.nextLine();

        // No try-catch here
        FileReader reader = new FileReader(fileName);

        System.out.println("File opened successfully!");

        scanner.close();
    }
}
