
import java.util.Scanner;


public  class MyClass {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number: ");
        int number1 = scanner.nextInt();

        System.out.println("Enter operator: ");
        char operator = scanner.next().charAt(0);

        System.out.print("Enter second number: ");
        int number2 = scanner.nextInt();

        // scanner.close();

        // switch (operator) {
        //     case '+':
        //         System.out.println(number1 + " + " + number2 + " = " + (number1 + number2));
        //         break;
        //     case '-':
        //         System.out.println(number1 + " - " + number2 + " = " + (number1 - number2));
        //         break;
        //     case '*':
        //         System.out.println(number1 + " * " + number2 + " = " + (number1 * number2));
        //         break;
        //     case '/':
        //         System.out.println(number1 + " / " + number2 + " = " + (number1 / number2));
        //         break;
        //     default:
        //         System.out.println("Invalid operator!");
        // }

        int result  = switch (operator) {
            case '+' -> number1 + number2;
            case '-' -> number1 - number2;
            case '*' -> number1 * number2;
            case '/' -> number1 / number2;
            default -> -1;
        };

        if (result == -1) {
            System.out.println("Invalid operator!");
        } else {
            System.out.println(number1 + " " + operator + " " + number2 + " = " + result);
        }
    }
}