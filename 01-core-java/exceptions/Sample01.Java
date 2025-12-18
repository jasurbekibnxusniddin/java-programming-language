public class Sample01 {

    public static void main(String[] args) {
        try {
            methodA();
        } catch (ArithmeticException e) {
            System.out.println("Exception handled in main()"+": " + e.getMessage());
        }
    }

    static void methodA() {
        methodB();
    }

    static void methodB() {
        methodC();
    }

    static void methodC() {
        int result = 10 / 0; // Exception occurs here
    }
}
