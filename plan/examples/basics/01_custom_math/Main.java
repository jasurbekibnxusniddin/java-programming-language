public class Main {
    public static void main(String[] args) {
        int sum = Math.add(1, 2);
        int diff = Math.subtract(1, 2);
        int product = Math.multiply(1, 2);
        int quotient = Math.divide(1, 2);

        System.out.println(sum);
        System.out.println(diff);
        System.out.println(product);
        System.out.println(quotient);

        int absValue = java.lang.Math.abs(-10);  // calls Java's Math class
        System.out.println(absValue);
    }
}